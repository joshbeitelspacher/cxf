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
package org.apache.cxf.ws.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.w3c.dom.Element;

import org.apache.cxf.Bus;
import org.apache.cxf.common.i18n.BundleUtils;
import org.apache.cxf.common.i18n.Message;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.service.model.ServiceInfo;
import org.apache.cxf.ws.policy.attachment.reference.ReferenceResolver;
import org.apache.cxf.ws.policy.attachment.reference.RemoteReferenceResolver;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyReference;
import org.apache.neethi.PolicyRegistry;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Configures a Server, Client, Bus with the specified policies. If a series of 
 * Policy <code>Element</code>s are supplied, these will be loaded into a Policy
 * class using the <code>PolicyBuilder</code> extension on the bus. If the 
 * PolicyEngine has not been started, this feature will start it.
 *
 * @see PolicyBuilder
 * @see AbstractFeature
 */
public class WSPolicyFeature extends AbstractFeature implements ApplicationContextAware {
    
    private static final ResourceBundle BUNDLE = BundleUtils.getBundle(WSPolicyFeature.class);
    
    private Collection<Policy> policies;
    private Collection<Element> policyElements;
    private Collection<Element> policyReferenceElements;
    private boolean ignoreUnknownAssertions;
    private String namespace;
    private AlternativeSelector alternativeSelector; 
    private ApplicationContext context;
  
       
    public WSPolicyFeature() {
        super();
    }

    public WSPolicyFeature(Policy... ps) {
        super();
        policies = new ArrayList<Policy>();
        Collections.addAll(policies, ps);
    }
    
    public void setApplicationContext(ApplicationContext c) throws BeansException {
        context = c;  
    }
    
    @Override
    public void initialize(Bus bus) {
        
        // this should never be null as features are initialised only
        // after the bus and all its extensions have been created
        
        PolicyEngine pe = bus.getExtension(PolicyEngine.class);
        
        synchronized (pe) {
            pe.setEnabled(true);
            pe.setIgnoreUnknownAssertions(ignoreUnknownAssertions);
            if (null != namespace) {
                PolicyConstants pc = bus.getExtension(PolicyConstants.class);
                pc.setNamespace(namespace);
            }
            if (null != alternativeSelector) {
                pe.setAlternativeSelector(alternativeSelector);
            }
        }
    }

    @Override
    public void initialize(Client client, Bus bus) {
        Endpoint endpoint = client.getEndpoint();
        
        intializeEndpoint(endpoint, bus);
    }

    @Override
    public void initialize(Server server, Bus bus) {
        Endpoint endpoint = server.getEndpoint();
        
        intializeEndpoint(endpoint, bus);
    }

    private void intializeEndpoint(Endpoint endpoint, Bus bus) {
        
        initialize(bus);
        
        Collection<Policy> loadedPolicies = null;
        if (policyElements != null || policyReferenceElements != null) {
            loadedPolicies = new ArrayList<Policy>();
            PolicyBuilder builder = bus.getExtension(PolicyBuilder.class); 
            if (null != policyElements) {
                for (Element e : policyElements) {
                    loadedPolicies.add(builder.getPolicy(e));
                }
            }
            if (null != policyReferenceElements) {
                for (Element e : policyReferenceElements) {
                    PolicyReference pr = builder.getPolicyReference(e);
                    Policy resolved = resolveReference(pr, e, builder, bus);
                    if (null != resolved) {
                        loadedPolicies.add(resolved);
                    }
                }
            }
        } 
        
        List<ServiceInfo> sis = endpoint.getService().getServiceInfos();
        for (ServiceInfo si : sis) {
            if (policies != null) {
                for (Policy p : policies) {
                    si.addExtensor(p);
                }
            }
            
            if (loadedPolicies != null) {
                for (Policy p : loadedPolicies) {
                    si.addExtensor(p);
                }
            }
        }
    }
    
    public Collection<Policy> getPolicies() {
        if (policies == null) {
            policies = new ArrayList<Policy>();
        }
        return policies;
    }

    public void setPolicies(Collection<Policy> policies) {
        this.policies = policies;
    }

    public Collection<Element> getPolicyElements() {
        if (policyElements == null) {
            policyElements = new ArrayList<Element>();
        }
        return policyElements;
    }

    public void setPolicyElements(Collection<Element> elements) {
        policyElements = elements;
    }
    
    public Collection<Element> getPolicyReferenceElements() {
        if (policyReferenceElements == null) {
            policyReferenceElements = new ArrayList<Element>();
        }
        return policyReferenceElements;
    }

    public void setPolicyReferenceElements(Collection<Element> elements) {
        policyReferenceElements = elements;
    }
      
    public void setIgnoreUnknownAssertions(boolean ignore) {
        ignoreUnknownAssertions = ignore;
    } 
    
    public void setNamespace(String ns) {
        namespace = ns;
    }
    
    public void setAlternativeSelector(AlternativeSelector as) {
        alternativeSelector = as;
    }
    
    Policy resolveReference(PolicyReference ref, Element e, PolicyBuilder builder, Bus bus) {
        Policy p = null;
        if (!ref.getURI().startsWith("#")) {
            p = resolveExternal(ref, e.getBaseURI(), bus);
        } else {
            p = resolveLocal(ref, e, bus);
        }
        if (null == p) {
            throw new PolicyException(new Message("UNRESOLVED_POLICY_REFERENCE_EXC", BUNDLE, ref.getURI()));
        }
        return p;
    }   
    
    Policy resolveLocal(PolicyReference ref, Element e, final Bus bus) {
        String uri = ref.getURI().substring(1);
        String absoluteURI = e.getBaseURI() + uri;
        PolicyRegistry registry = bus.getExtension(PolicyEngine.class).getRegistry();
        Policy resolved = registry.lookup(absoluteURI);
        if (null != resolved) {
            return resolved;
        }
        ReferenceResolver resolver = new ReferenceResolver() {
            public Policy resolveReference(String uri) {
                PolicyBean pb = (PolicyBean)context.getBean(uri);
                if (null != pb) {
                    PolicyBuilder builder = bus.getExtension(PolicyBuilder.class);
                    return builder.getPolicy(pb.getElement()); 
                }
                return null;
            }
        };
        resolved = resolver.resolveReference(uri);
        if (null != resolved) {
            ref.setURI(absoluteURI);
            registry.register(absoluteURI, resolved);
        }
        return resolved;
    }
    
    protected Policy resolveExternal(PolicyReference ref,  String baseURI, Bus bus) {
        PolicyBuilder builder = bus.getExtension(PolicyBuilder.class);
        ReferenceResolver resolver = new RemoteReferenceResolver(baseURI, builder,
            bus.getExtension(PolicyConstants.class));
        PolicyRegistry registry = bus.getExtension(PolicyEngine.class).getRegistry();
        Policy resolved = registry.lookup(ref.getURI());
        if (null != resolved) {
            return resolved;
        }
        return resolver.resolveReference(ref.getURI());
    }
}
