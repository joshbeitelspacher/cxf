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
package org.apache.cxf.rs.security.saml;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.security.auth.callback.CallbackHandler;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.rs.security.common.CryptoLoader;
import org.apache.cxf.rs.security.common.SecurityUtils;
import org.apache.cxf.ws.security.SecurityConstants;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.saml.ext.AssertionWrapper;
import org.apache.ws.security.saml.ext.SAMLParms;

public final class SAMLUtils {
    private static final Logger LOG = 
        LogUtils.getL7dLogger(SAMLUtils.class);
    
    private SAMLUtils() {
        
    }
    
    public static AssertionWrapper createAssertion(Message message) throws Fault {
        CallbackHandler handler = SecurityUtils.getCallbackHandler(
             message, SAMLUtils.class, SecurityConstants.SAML_CALLBACK_HANDLER);
        SAMLParms samlParms = new SAMLParms();
        samlParms.setCallbackHandler(handler);
        try {
            AssertionWrapper assertion = new AssertionWrapper(samlParms);
            boolean selfSignAssertion = 
                MessageUtils.getContextualBoolean(
                    message, SecurityConstants.SELF_SIGN_SAML_ASSERTION, false
                );
            if (selfSignAssertion) {
                //--- This code will be moved to a common utility class
                Crypto crypto = new CryptoLoader().getCrypto(message, 
                                          SecurityConstants.SIGNATURE_CRYPTO,
                                          SecurityConstants.SIGNATURE_PROPERTIES);
                
                String user = 
                    SecurityUtils.getUserName(message, crypto, SecurityConstants.SIGNATURE_USERNAME);
                if (StringUtils.isEmpty(user)) {
                    return assertion;
                }
        
                String password = 
                    SecurityUtils.getPassword(message, user, WSPasswordCallback.SIGNATURE, 
                            SAMLUtils.class);
                
                assertion.signAssertion(user, password, crypto, false);
            }
            return assertion;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            LOG.warning(sw.toString());
            throw new Fault(new RuntimeException(ex.getMessage() + ", stacktrace: " + sw.toString()));
        }
        
    }
}