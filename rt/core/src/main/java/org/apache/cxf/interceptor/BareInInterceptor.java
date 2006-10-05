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

package org.apache.cxf.interceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.xml.namespace.QName;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.apache.cxf.common.i18n.BundleUtils;
import org.apache.cxf.databinding.DataReader;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.MessageInfo;
import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.cxf.service.model.OperationInfo;
import org.apache.cxf.staxutils.DepthXMLStreamReader;
import org.apache.cxf.staxutils.StaxUtils;

public class BareInInterceptor extends AbstractInDatabindingInterceptor {

    private static final ResourceBundle BUNDLE = BundleUtils.getBundle(BareInInterceptor.class);

    private static Set<String> filter = new HashSet<String>();
    
    static {
        filter.add("void");
        filter.add("javax.activation.DataHandler");
    }
    
    public BareInInterceptor() {
        super();
        setPhase(Phase.UNMARSHAL);
    }

    public void handleMessage(Message message) {
        DepthXMLStreamReader xmlReader = getXMLStreamReader(message);
        Exchange exchange = message.getExchange();

        DataReader<Message> dr = getMessageDataReader(message);
        List<Object> parameters = new ArrayList<Object>();

        Endpoint ep = exchange.get(Endpoint.class);
        Service service = ep.getService();
        BindingOperationInfo bop = exchange.get(BindingOperationInfo.class);
        MessageInfo msgInfo = message.get(MessageInfo.class);
        
        Collection<OperationInfo> ops = null;
        if (bop == null) {
            ops = new ArrayList<OperationInfo>();
            ops.addAll(service.getServiceInfo().getInterface().getOperations());
        } else if (msgInfo == null) {
            msgInfo = getMessageInfo(message, bop, exchange);
        }
        
        boolean client = isRequestor(message);
        
        int paramNum = 0;
        while (StaxUtils.toNextElement(xmlReader)) {
            QName elName = xmlReader.getName();
            Object o = null;
            
            MessagePartInfo p;
            if (msgInfo != null) {
                p = msgInfo.getMessagePartByIndex(paramNum);
            } else {
                p = findMessagePart(exchange, ops, elName, client, paramNum);
            }
            
            if (p == null) {
                throw new Fault(new org.apache.cxf.common.i18n.Message("NO_PART_FOUND", BUNDLE, elName));
            }
            
            Class<?> cls = (Class) p.getProperty(Class.class.getName());
            if (cls != null && !filter.contains(cls.getName()) && !cls.isArray()) {
                o = dr.read(p.getConcreteName(), message, cls);
            } else {
                o = dr.read(p.getConcreteName(), message, null);
            }
            
            if (o != null) {
                parameters.add(o);
            }
            paramNum++;
        }

        if (message.get(Element.class) != null) {
            parameters.addAll(abstractParamsFromHeader(message.get(Element.class), ep, message));
        }

        // if we didn't know the operation going into this, find it.
        if (bop == null) {
            OperationInfo op = ops.iterator().next();
            bop = ep.getEndpointInfo().getBinding().getOperation(op);
            if (bop != null) {
                exchange.put(BindingOperationInfo.class, bop);
                exchange.setOneWay(op.isOneWay());
            }
        }
        
        message.setContent(List.class, parameters);
    }
    
    private List<Object> abstractParamsFromHeader(Element headerElement, Endpoint ep, Message message) {
        List<Object> paramInHeader = new ArrayList<Object>();
        List<MessagePartInfo> parts = null;
        List<Element> elemInHeader = new ArrayList<Element>();
        for (BindingOperationInfo bop : ep.getEndpointInfo().getBinding().getOperations()) {

            if (isRequestor(message)) {
                parts = bop.getOutput().getMessageInfo().getMessageParts();
            } else {
                parts = bop.getInput().getMessageInfo().getMessageParts();
            }

            for (MessagePartInfo mpi : parts) {
                if (mpi.isInSoapHeader()) {
                    NodeList nodeList = headerElement.getChildNodes();
                    if (nodeList != null) {
                        for (int i = 0; i < nodeList.getLength(); i++) {
                            if (nodeList.item(i).getNamespaceURI().equals(
                                            mpi.getElementQName().getNamespaceURI())
                                            && nodeList.item(i).getLocalName().equals(
                                                            mpi.getElementQName().getLocalPart())) {
                                Element param = (Element) nodeList.item(i);
                                if (!elemInHeader.contains(param)) {
                                    elemInHeader.add(param);
                                }
                            }
                        }

                    }

                }
            }
        }

        for (Iterator iter = elemInHeader.iterator(); iter.hasNext();) {
            Element element = (Element)iter.next();
            paramInHeader.add(getNodeDataReader(message).read(element));
        }
        return paramInHeader;
    }
}
