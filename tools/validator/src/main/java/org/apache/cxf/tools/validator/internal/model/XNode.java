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

package org.apache.cxf.tools.validator.internal.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.xml.namespace.QName;

import org.apache.cxf.common.util.StringUtils;

public class XNode {

    private String prefix;
    private QName name;
    private String attributeName;
    private String attributeValue;
    private XNode parentNode;

    private XNode failurePoint;

    private Map<String, String> nsMap = new HashMap<String, String>();

    public void setFailurePoint(XNode point) {
        this.failurePoint = point;
    }

    public XNode getFailurePoint() {
        return this.failurePoint;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(final String newPrefix) {
        this.prefix = newPrefix;
    }

    public QName getQName() {
        return name;
    }

    public void setQName(final QName newName) {
        this.name = newName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(final String newAttributeName) {
        this.attributeName = newAttributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(final String newAttributeValue) {
        this.attributeValue = newAttributeValue;
    }

    public XNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(final XNode newParentNode) {
        this.parentNode = newParentNode;
    }

    public Map<String, String> getNSMap() {
        return nsMap;
    }

    public String getText() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(name.getLocalPart());
        sb.append(":");
        sb.append(getAttributeValue());
        sb.append("]");
        return sb.toString();
    }

    public String getPlainText() {
        StringBuffer sb = new StringBuffer();
        Stack<XNode> parentNodes = getParentNodes();
        while (!parentNodes.empty()) {
            sb.append(parentNodes.pop().getText());
        }
        sb.append(getText());
        nsMap.put(prefix, name.getNamespaceURI());
        return sb.toString();
    }

    public String getXPath() {
        StringBuffer sb = new StringBuffer();
        sb.append("/");
        sb.append(prefix);
        sb.append(":");
        sb.append(name.getLocalPart());
        if (!StringUtils.isEmpty(attributeName) && !StringUtils.isEmpty(attributeValue)) {
            sb.append("[@");
            sb.append(attributeName);
            sb.append("='");
            sb.append(attributeValue);
            sb.append("']");
        }
        return sb.toString();
    }

    private Stack<XNode> getParentNodes() {
        Stack<XNode> parentNodes = new Stack<XNode>();

        XNode pNode = getParentNode();
        while (pNode != null) {
            nsMap.put(pNode.getPrefix(),
                      pNode.getQName().getNamespaceURI());
            parentNodes.push(pNode);
            pNode = pNode.getParentNode();
        }
        return parentNodes;
    }

    public String toString() {
        Stack<XNode> parentNodes = getParentNodes();
        StringBuffer sb = new StringBuffer();
        while (!parentNodes.empty()) {
            sb.append(parentNodes.pop().getXPath());
        }
        sb.append(getXPath());
        nsMap.put(prefix, name.getNamespaceURI());
        return sb.toString();
    }
}
