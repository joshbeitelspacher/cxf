/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cxf.rs.security.saml.sso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.w3c.dom.Document;

import org.apache.cxf.common.i18n.BundleUtils;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.common.util.Base64Exception;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.message.Message;
import org.apache.cxf.rs.security.saml.DeflateEncoderDecoder;
import org.apache.cxf.rs.security.saml.sso.state.RequestState;
import org.apache.cxf.rs.security.saml.sso.state.ResponseState;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.saml.ext.OpenSAMLUtil;
import org.opensaml.xml.XMLObject;

@Path("sso")
public class RequestAssertionConsumerService extends AbstractSSOSpHandler {
    private static final Logger LOG = 
        LogUtils.getL7dLogger(RequestAssertionConsumerService.class);
    private static final ResourceBundle BUNDLE = 
        BundleUtils.getBundle(RequestAssertionConsumerService.class);
    
    private boolean supportDeflateEncoding = true;
    private boolean supportBase64Encoding = true;

    @Context 
    private MessageContext jaxrsContext;
    
    public void setSupportDeflateEncoding(boolean deflate) {
        supportDeflateEncoding = deflate;
    }
    public boolean isSupportDeflateEncoding() {
        return supportDeflateEncoding;
    }
    
    public void setSupportBase64Encoding(boolean supportBase64Encoding) {
        this.supportBase64Encoding = supportBase64Encoding;
    }
    public boolean isSupportBase64Encoding() {
        return supportBase64Encoding;
    }
    
    @POST
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public Response processSamlResponse(@FormParam(SSOConstants.SAML_RESPONSE) String encodedSamlResponse,
                                        @FormParam(SSOConstants.RELAY_STATE) String relayState) {
        if (relayState == null) {
            reportError("MISSING_RELAY_STATE");
            throw new WebApplicationException(400);
        }
        RequestState requestState = getStateProvider().removeRequestState(relayState);
        if (requestState == null) {
            reportError("MISSING_REQUEST_STATE");
            throw new WebApplicationException(400);
        }
        if (isStateExpired(requestState.getCreatedAt())) {
            reportError("EXPIRED_REQUEST_STATE");
            throw new WebApplicationException(400);
        }
        
        URI targetURI = getTargetURI(requestState.getTargetAddress());
        
        org.opensaml.saml2.core.Response samlResponse = 
            readSAMLResponse(encodedSamlResponse);

        validateSamlResponse(samlResponse, requestState);
        
        // Set the security context
        String securityContextKey = UUID.randomUUID().toString();
        
        long currentTime = System.currentTimeMillis();
        ResponseState responseState = new ResponseState(relayState, currentTime);
        getStateProvider().setResponseState(securityContextKey, responseState);
        
        String contextCookie = createCookie(SSOConstants.SECURITY_CONTEXT_TOKEN,
                                            securityContextKey,
                                            requestState.getWebAppContext());
        
        // Finally, redirect to the service provider endpoint
        return Response.seeOther(targetURI).header("Set-Cookie", contextCookie).build();
        
    }
    
    @GET
    public Response getSamlResponse(@QueryParam(SSOConstants.SAML_RESPONSE) String samlResponse,
                                    @QueryParam(SSOConstants.RELAY_STATE) String relayState) {
        return processSamlResponse(relayState, samlResponse);       
    }
    
    private org.opensaml.saml2.core.Response readSAMLResponse(String samlResponse) {
        if (StringUtils.isEmpty(samlResponse)) {
            reportError("MISSING_SAML_RESPONSE");
            throw new WebApplicationException(400);
        }
        InputStream tokenStream = null;
        if (isSupportBase64Encoding()) {
            try {
                byte[] deflatedToken = Base64Utility.decode(samlResponse);
                tokenStream = isSupportDeflateEncoding() 
                    ? new DeflateEncoderDecoder().inflateToken(deflatedToken)
                    : new ByteArrayInputStream(deflatedToken); 
            } catch (Base64Exception ex) {
                throw new WebApplicationException(400);
            } catch (DataFormatException ex) {
                throw new WebApplicationException(400);
            }
        } else {
            try {
                tokenStream = new ByteArrayInputStream(samlResponse.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                throw new WebApplicationException(400);
            }
        }
        
        Document responseDoc = null;
        try {
            responseDoc = DOMUtils.readXml(new InputStreamReader(tokenStream, "UTF-8"));
        } catch (Exception ex) {
            throw new WebApplicationException(400);
        }
        XMLObject responseObject = null;
        try {
            responseObject = OpenSAMLUtil.fromDom(responseDoc.getDocumentElement());
        } catch (WSSecurityException ex) {
            throw new WebApplicationException(400);
        }
        if (!(responseObject instanceof org.opensaml.saml2.core.Response)) {
            throw new WebApplicationException(400);
        }
        return (org.opensaml.saml2.core.Response)responseObject;
    }
    
    protected void validateSamlResponse(org.opensaml.saml2.core.Response samlResponse,
                                        RequestState requestState) {
        try {
            SAMLProtocolResponseValidator protocolValidator = new SAMLProtocolResponseValidator();
            // TODO Configure Crypto & CallbackHandler object here to validate signatures
            protocolValidator.validateSamlResponse(samlResponse, null, null);
            
            SAMLSSOResponseValidator ssoResponseValidator = new SAMLSSOResponseValidator();
            ssoResponseValidator.setAssertionConsumerURL((String)jaxrsContext.get(Message.REQUEST_URL));
            // TODO client address ssoResponseValidator.setClientAddress(clientAddress);
            ssoResponseValidator.setIssuerIDP(requestState.getIdpServiceAddress());
            ssoResponseValidator.setRequestId(requestState.getSamlRequestId());
            ssoResponseValidator.setSpIdentifier(requestState.getIssuerId());
            
            // TODO post binding
            ssoResponseValidator.validateSamlResponse(samlResponse, false);
        } catch (WSSecurityException ex) {
            reportError("INVALID_SAML_RESPONSE");
            throw new WebApplicationException(400);
        }
    }
    
    private URI getTargetURI(String targetAddress) {
        if (targetAddress != null) {
            try {
                return URI.create(targetAddress);
            } catch (IllegalArgumentException ex) {
                reportError("INVALID_TARGET_URI");
            }
        } else {
            reportError("MISSING_TARGET_URI");
        }
        throw new WebApplicationException(400);
    }
    
    private void reportError(String code) {
        org.apache.cxf.common.i18n.Message errorMsg = 
            new org.apache.cxf.common.i18n.Message(code, BUNDLE);
        LOG.warning(errorMsg.toString());
    }
    
}