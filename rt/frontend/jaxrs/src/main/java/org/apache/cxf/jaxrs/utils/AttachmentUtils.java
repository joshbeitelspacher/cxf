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

package org.apache.cxf.jaxrs.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.common.i18n.BundleUtils;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.MultipartID;

public final class AttachmentUtils {
    private static final Logger LOG = LogUtils.getL7dLogger(AttachmentUtils.class);
    private static final ResourceBundle BUNDLE = BundleUtils.getBundle(AttachmentUtils.class);
    
    private AttachmentUtils() {
    }
    
    public static Map<String, DataHandler> getAttachments(MessageContext mc) {
        return CastUtils.cast((Map)mc.get(MessageContext.INBOUND_MESSAGE_ATTACHMENTS));
    }
    
    public static Object getMultipart(Class<Object> c, Annotation[] anns, 
         MediaType mt, MessageContext mc, InputStream is) throws IOException {
        InputStream stream = null;
        MultipartID id = AnnotationUtils.getAnnotation(anns, MultipartID.class);
        if (id != null) {
            String contentId = id.value();
            String rootId = mt.getParameters().get("start");
            if (rootId != null) {
                rootId = rootId.replace("\"", "").replace("'", "");
                if (rootId.equalsIgnoreCase(contentId)) {
                    stream = is;
                }
            }
            if (stream == null) {
                // TODO: looks like the lazy attachments collection can only be accessed this way
                for (Map.Entry<String, DataHandler> entry : getAttachments(mc).entrySet()) {
                    if (entry.getKey().equals(contentId)) {
                        DataHandler dh = entry.getValue();
                        return DataHandler.class.isAssignableFrom(c) ? dh 
                            : DataSource.class.isAssignableFrom(c) ? dh.getDataSource()
                            : dh.getInputStream();
                    }
                }
                org.apache.cxf.common.i18n.Message errorMsg = 
                    new org.apache.cxf.common.i18n.Message("MULTTIPART_ID_NOT_FOUND", 
                                                           BUNDLE, 
                                                           contentId,
                                                           mt.toString());
                LOG.warning(errorMsg.toString());
            }
        } else {
            stream = is;
        }
        if (stream != null) {
            if (DataSource.class.isAssignableFrom(c)) {
                return new ByteArrayDataSource(stream, mt.toString());
            } else if (DataHandler.class.isAssignableFrom(c)) {
                return new DataHandler(new ByteArrayDataSource(stream, mt.toString()));
            } 
        }
        return stream;
    }
}
