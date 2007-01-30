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

package org.apache.cxf.transport.local;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.AbstractTransportFactory;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.ConduitInitiator;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;

public class LocalTransportFactory extends AbstractTransportFactory
    implements DestinationFactory, ConduitInitiator {
   
    public static final String TRANSPORT_ID = "http://cxf.apache.org/transports/local";
    
    private static final Logger LOG = Logger.getLogger(LocalTransportFactory.class.getName());
    private static final Set<String> URI_PREFIXES = new HashSet<String>();
    static {
        URI_PREFIXES.add("local://");
    }
    
    private Map<String, Destination> destinations = new HashMap<String, Destination>();

    
    public LocalTransportFactory() {
        super();
        List<String> ids = new ArrayList<String>();
        ids.add(TRANSPORT_ID);
        setTransportIds(ids);
    }

    public Destination getDestination(EndpointInfo ei) throws IOException {
        return getDestination(ei, createReference(ei));
    }

    protected Destination getDestination(EndpointInfo ei,
                                         EndpointReferenceType reference)
        throws IOException {
        Destination d = destinations.get(reference.getAddress().getValue());
        if (d == null) {
            d = createDestination(ei, reference);
            destinations.put(reference.getAddress().getValue(), d);
        }
        return d;
    }

    private Destination createDestination(EndpointInfo ei, EndpointReferenceType reference) {
        LOG.info("Creating destination for address " + reference.getAddress().getValue());
        return new LocalDestination(this, reference, ei);
    }

    void remove(LocalDestination destination) {
        destinations.remove(destination);
    }

    public Conduit getConduit(EndpointInfo ei) throws IOException {
        return new LocalConduit((LocalDestination)getDestination(ei));
    }

    public Conduit getConduit(EndpointInfo ei, EndpointReferenceType target) throws IOException {
        return new LocalConduit((LocalDestination)getDestination(ei, target));
    }

    EndpointReferenceType createReference(EndpointInfo ei) {
        EndpointReferenceType epr = new EndpointReferenceType();
        AttributedURIType address = new AttributedURIType();
        address.setValue(ei.getAddress());
        epr.setAddress(address);
        return epr;
    }

    public Set<String> getUriPrefixes() {
        return URI_PREFIXES;
    }

}
