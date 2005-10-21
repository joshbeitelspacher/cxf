package org.objectweb.celtix.systest.handlers;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;
import org.objectweb.celtix.BusException;
import org.objectweb.celtix.systest.common.ClientServerTestBase;
import org.objectweb.handler_test.HandlerTest;
import org.objectweb.handler_test.HandlerTestService;
import org.objectweb.handler_test.PingException;
import org.objectweb.handler_test.types.PingResponse;
import org.objectweb.hello_world_soap_http.types.GreetMe;


public class HandlerInvocationTest extends ClientServerTestBase {
    
    private QName serviceName = new QName("http://objectweb.org/hello_world_soap_http", 
                                          "HandlerTestService");    
    private QName portName = new QName("http://objectweb.org/hello_world_soap_http", "SoapPort");
    
    private URL wsdl;
    private HandlerTestService service;
    private HandlerTest handlerTest;

    protected void onetimeSetUp() { 
        launchServer(Server.class);
    } 


    public void setUp() throws BusException {
        try { 
            super.setUp();
            
            wsdl = HandlerInvocationTest.class.getResource("/handler_test.wsdl");
            service = new HandlerTestService(wsdl, serviceName);
            handlerTest = (HandlerTest) service.getPort(portName, HandlerTest.class);

        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.toString());
        }
    }


    public void testLogicalHandlerInvoked() throws PingException { 
        
        TestHandler<LogicalMessageContext> handler1 = new TestHandler<LogicalMessageContext>();        
        TestHandler<LogicalMessageContext>  handler2 = new TestHandler<LogicalMessageContext> ();
        
        addHandlersToChain((BindingProvider)handlerTest, handler1, handler2);
        
        List<String> resp = handlerTest.ping();
        assertNotNull(resp);      

        assertTrue("handle message was not invoked", handler1.isHandleMessageInvoked());
        assertTrue("handle message was not invoked", handler2.isHandleMessageInvoked());
        assertTrue("close must be  called", handler1.isCloseInvoked());
        assertTrue("close must be  called", handler2.isCloseInvoked());

        assertEquals(5, resp.size()); 
        // the server has encoded into the response the order in
        // which the handlers have been invoked, parse it and make
        // sure everything is ok
        //
        
        // expected order for inbound interceptors
        //
        String[] names = {"handler2", "handler1", "servant", "handler1", "handler2"}; 
        Iterator iter = resp.iterator();
        for (String expected : names) {
            assertEquals(expected, iter.next());
        }
    }
    

    public void testLogicalHandlerStopProcessing() throws PingException {

        final String clientHandlerMessage = "handler1 client side"; 

        TestHandler<LogicalMessageContext> handler1 = new TestHandler<LogicalMessageContext>() {
            public boolean handleMessage(LogicalMessageContext ctx) {
                super.handleMessage(ctx);
                
                try {
                    Boolean outbound = (Boolean)ctx.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY); 
                    if (!outbound) {
                        LogicalMessage msg = ctx.getMessage();
                        assertNotNull("logical message is null", msg);
                        JAXBContext jaxbCtx = JAXBContext.newInstance(GreetMe.class.getPackage().getName());

                        PingResponse resp = new PingResponse();
                        resp.getHandlersInfo().add(clientHandlerMessage);
                        msg.setPayload(resp, jaxbCtx);
                    }

                } catch (Exception e) {
                    e.printStackTrace(); 
                    fail(e.toString());
                }

                return false;
            }
        };        
        TestHandler<LogicalMessageContext>  handler2 = new TestHandler<LogicalMessageContext> ();
        
        addHandlersToChain((BindingProvider)handlerTest, handler1, handler2);

        List<String> resp = handlerTest.ping();
        assertEquals(1, resp.size());      
        assertEquals(clientHandlerMessage, resp.get(0));
        
        assertEquals("handler must be invoked for inbound and outbound message", 
                     2, handler1.getHandleMessageInvoked());
        assertTrue("second handler should not be invoked", !handler2.isHandleMessageInvoked());
        assertTrue("close must be  called", handler1.isCloseInvoked());
        assertTrue("second handler must not be closed", !handler2.isCloseInvoked());
    }


    public void testLogicalHandlerStopProcessingServerSide() throws PingException {

        List<String> resp = handlerTest.pingWithArgs("handler2 inbound stop");
        assertEquals(1, resp.size());
        assertEquals("handler2", resp.get(0));
    }


    public void testLogicalHandlerThrowsProtocolException() throws Exception { 

        final String clientHandlerMessage = "handler1 client side"; 

        final Holder<Boolean> handleFaultCalled = new Holder<Boolean>(Boolean.FALSE); 

        TestHandler<LogicalMessageContext> handler1 = new TestHandler<LogicalMessageContext>() {
            public boolean handleMessage(LogicalMessageContext ctx) {
                super.handleMessage(ctx);
                throw new ProtocolException(clientHandlerMessage); 
            }

            public boolean handleFault(LogicalMessageContext ctx) { 
                
                // handle fault should be called on inbound leg only
                Boolean inbound = !(Boolean)ctx.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
                assertTrue(inbound);
                handleFaultCalled.value = Boolean.TRUE;
                return true;
            }
        };        
        TestHandler<LogicalMessageContext>  handler2 = new TestHandler<LogicalMessageContext> ();
        
        addHandlersToChain((BindingProvider)handlerTest, handler1, handler2);

        try {
            List<String> resp = handlerTest.ping();
            fail("did not get expected exception");
        } catch (ProtocolException e) {
            assertEquals(clientHandlerMessage, e.getMessage()); 
        }
        assertTrue(handleFaultCalled.value); 
        assertTrue(!handler2.isHandleFaultInvoked());
        assertTrue(handler1.isCloseInvoked());
        assertTrue(!handler2.isCloseInvoked());
    } 



    public void testLogicalHandlerThrowsProtocolExceptionServerSide() throws PingException {
        try {
            handlerTest.pingWithArgs("handler2 inbound throw javax.xml.ws.ProtocolException");
            fail("did not get expected exception");
        } catch (ProtocolException e) {
            // happy now
        }
    }


    public void testLogicalHandlerHandlerFault() { 

        TestHandler<LogicalMessageContext> handler1 = new TestHandler<LogicalMessageContext>();
        TestHandler<LogicalMessageContext>  handler2 = new TestHandler<LogicalMessageContext>();
        addHandlersToChain((BindingProvider)handlerTest, handler1, handler2);
        
        try {
            handlerTest.pingWithArgs("servant throw exception"); 
            fail("did not get expected PingException"); 
        } catch (PingException e) {
            assertTrue(e.getMessage().contains("from servant"));
            System.out.println(e.getFaultInfo().getDetail()); 
        }

        assertEquals(1, handler1.getHandleMessageInvoked());
        assertEquals(1, handler2.getHandleMessageInvoked());
        assertEquals(1, handler1.getHandleFaultInvoked()); 
        assertEquals(1, handler2.getHandleFaultInvoked()); 
    } 


    public void xxxxxxxxxtestLogicalHandlerOneWay() {
        TestHandler<LogicalMessageContext> handler1 = new TestHandler<LogicalMessageContext>();
        TestHandler<LogicalMessageContext>  handler2 = new TestHandler<LogicalMessageContext>();
        addHandlersToChain((BindingProvider)handlerTest, handler1, handler2);

        handlerTest.pingOneWay(); 

        assertEquals(1, handler1.getHandleMessageInvoked());
        assertEquals(1, handler2.getHandleMessageInvoked());
    }

    private void addHandlersToChain(BindingProvider bp, Handler...handlers) { 
        List<Handler> handlerChain = bp.getBinding().getHandlerChain();
        assertNotNull(handlerChain);        
        for (Handler h : handlers) {
            handlerChain.add(h);
        }
    }
}
