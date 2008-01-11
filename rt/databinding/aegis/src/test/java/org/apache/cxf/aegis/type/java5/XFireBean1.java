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
package org.apache.cxf.aegis.type.java5;

@SuppressWarnings("deprecation")
public class XFireBean1 {
    private String elementProperty;
    private String attributeProperty;
    private String bogusProperty;

    @org.codehaus.xfire.aegis.type.java5.XmlAttribute
    public String getAttributeProperty() {
        return attributeProperty;
    }

    public void setAttributeProperty(String attributeProperty) {
        this.attributeProperty = attributeProperty;
    }

    public String getBogusProperty() {
        return bogusProperty;
    }

    public void setBogusProperty(String bogusProperty) {
        this.bogusProperty = bogusProperty;
    }

    @org.codehaus.xfire.aegis.type.java5.XmlElement(type = CustomStringType.class)
    public String getElementProperty() {
        return elementProperty;
    }

    public void setElementProperty(String elementProperty) {
        this.elementProperty = elementProperty;
    }
}