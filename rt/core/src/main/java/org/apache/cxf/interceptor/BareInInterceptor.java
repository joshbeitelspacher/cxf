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
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.cxf.common.i18n.BundleUtils;
import org.apache.cxf.databinding.DataReader;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.model.BindingMessageInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.MessageInfo;
import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.cxf.service.model.OperationInfo;
import org.apache.cxf.staxutils.DepthXMLStreamReader;
import org.apache.cxf.staxutils.StaxUtils;

public class BareInInterceptor extends AbstractInDatabindingInterceptor {
    private static final Logger LOG = Logger.getLogger(BareInInterceptor.class.getName());
    private static final ResourceBundle BUNDLE = BundleUtils.getBundle(BareInInterceptor.class);

    private static Set<String> filter = new HashSet<String>();

    static {
        filter.add("void");
        filter.add("javax.activation.DataHandler");
    }

    public BareInInterceptor() {
        super();
        setPhase(Phase.UNMARSHAL);
        addAfter(URIMappingInterceptor.class.getName());
    }

    public void handleMessage(Message message) {
        if (isGET(message) && message.getContent(List.class) != null) {
            LOG.info("BareInInterceptor skipped in HTTP GET method");
            return;
        }

        DepthXMLStreamReader xmlReader = getXMLStreamReader(message);
        Exchange exchange = message.getExchange();

        DataReader<XMLStreamReader> dr = getDataReader(message);
        List<Object> parameters = new ArrayList<Object>();

        Endpoint ep = exchange.get(Endpoint.class);
        Service service = ep.getService();
        BindingOperationInfo bop = exchange.get(BindingOperationInfo.class);
        // XXX - Should the BindingMessageInfo.class be put on
        // the message?
        //MessageInfo msgInfo = message.get(MessageInfo.class);
        BindingMessageInfo msgInfo = null;

        boolean client = isRequestor(message);

        Collection<OperationInfo> ops = null;
        if (bop == null) {
            ops = new ArrayList<OperationInfo>();
            ops.addAll(service.getServiceInfo().getInterface().getOperations());
            if (xmlReader.getEventType() == XMLStreamReader.END_ELEMENT && !client) {
                //empty input
                //TO DO : check duplicate operation with no input
                for (OperationInfo op : ops) {
                    MessageInfo bmsg = op.getInput();
                    if (bmsg.getMessageParts().size() == 0) {
                        BindingOperationInfo boi = ep.getEndpointInfo().getBinding().getOperation(op);
                        exchange.put(BindingOperationInfo.class, boi);
                        exchange.put(OperationInfo.class, op);
                        exchange.setOneWay(op.isOneWay());
                    }
                }

            }
        } else if (msgInfo == null) {
            // XXX - Is the call to
            // AbstractInDatabindingInterceptor.getMessageInfo()
            // necessary?  Should we put the BindingMessageInfo on
            // the message instead of the MessageInfo?
            // msgInfo = getMessageInfo(message, bop, exchange);
            getMessageInfo(message, bop);
            if (client) {
                msgInfo = bop.getOutput();
            } else {
                msgInfo = bop.getInput();
            }
        }

        int paramNum = 0;

        
        while (StaxUtils.toNextElement(xmlReader)) {
            QName elName = xmlReader.getName();
            Object o = null;

            MessagePartInfo p;
            if (msgInfo != null && msgInfo.getMessageParts() != null) {
                assert msgInfo.getMessageParts().size() > paramNum;
                p = msgInfo.getMessageParts().get(paramNum);
            } else {
                p = findMessagePart(exchange, ops, elName, client, paramNum);
            }

            if (p == null) {
                throw new Fault(new org.apache.cxf.common.i18n.Message("NO_PART_FOUND", BUNDLE, elName));
            }

            o = dr.read(p, xmlReader);

            if (o != null) {
                parameters.add(o);
            }
            paramNum++;
        }
        if (parameters.size() > 0) {
            message.setContent(List.class, parameters);
        }
    }
}
