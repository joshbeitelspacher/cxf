package org.objectweb.celtix.jca.celtix;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnection;
import javax.security.auth.Subject;
import javax.xml.namespace.QName;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.easymock.classextension.EasyMock;
import org.objectweb.celtix.Bus;
import org.objectweb.celtix.connector.CeltixConnectionFactory;
import org.objectweb.hello_world_soap_http.Greeter;



public class ManagedConnectionFactoryImplTest extends TestCase {

    protected ManagedConnectionFactoryImpl mci;

    protected CeltixConnectionRequestInfo cri;

    protected CeltixConnectionRequestInfo cri2;

    protected CeltixConnectionRequestInfo cri3;

    public ManagedConnectionFactoryImplTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        
        mci = createManagedConnectionFactoryImpl();
        
        URL wsdl = getClass().getResource("/wsdl/hello_world.wsdl");
        
        //QName serviceName = new QName("http://objectweb.org/hello_world_soap_http", "SOAPService"); 
        
        //QName portName = new QName("http://objectweb.org/hello_world_soap_http", "SoapPort");
        
        cri = new CeltixConnectionRequestInfo(Greeter.class, wsdl, new QName("service1"),
                                              new QName("fooPort1"));
        
        cri2 = new CeltixConnectionRequestInfo(Greeter.class, wsdl, new QName("service2"),
                                              new QName("fooPort2"));
        cri3 = new CeltixConnectionRequestInfo(Greeter.class, wsdl, new QName("service3"),
                                              new QName("fooPort3"));
    }

    public void testSetConfigurationDomain() throws Exception {
        final String domainName = "SomeDomain";
        Properties p = new Properties();
        ManagedConnectionFactoryImpl mcf = new ManagedConnectionFactoryImpl(p);

        mcf.setConfigurationDomain(domainName);
        assertTrue(p.containsValue(domainName));
        assertEquals(domainName, mcf.getConfigurationDomain());
    }

    public void testGetConfigurationDomainReturnsDefaultValue() throws Exception {
        Properties p = new Properties();
        ManagedConnectionFactoryImpl mcf = new ManagedConnectionFactoryImpl(p);
        assertEquals(ManagedConnectionFactoryImpl.CONFIG_DOMAIN, mcf.getConfigurationDomain());
    }

    public void testSetConfigurationScope() throws Exception {
        final String name = "a.b.c";
        Properties p = new Properties();
        ManagedConnectionFactoryImpl mcf = new ManagedConnectionFactoryImpl(p);

        mcf.setConfigurationScope(name);
        assertTrue(p.containsValue(name));
        assertEquals(name, mcf.getConfigurationScope());
    }

    public void testGetConfigurationScopeReturnsDefaultValue() throws Exception {
        Properties p = new Properties();
        ManagedConnectionFactoryImpl mcf = new ManagedConnectionFactoryImpl(p);
        assertEquals(ManagedConnectionFactoryImpl.CONFIG_SCOPE, mcf.getConfigurationScope());
    }

   
    public void testSetEJBServicePropertiesURL() throws Exception {
        final String name = "file://foo.txt";
        Properties p = new Properties();
        ManagedConnectionFactoryImpl mcf = new ManagedConnectionFactoryImpl(p);

        mcf.setEJBServicePropertiesURL(name);
        assertTrue(p.containsValue(name));
        assertEquals(name, mcf.getEJBServicePropertiesURL());
    }

    public void testSetMonitorEJBServiceProperties() throws Exception {
        final Boolean value = Boolean.TRUE;
        Properties p = new Properties();
        ManagedConnectionFactoryImpl mcf = new ManagedConnectionFactoryImpl(p);

        mcf.setMonitorEJBServiceProperties(value);
        assertTrue(p.containsValue(value.toString()));
        assertEquals(value, mcf.getMonitorEJBServiceProperties());
    }

    public void testSetEJBServicePropertiesPollInterval() throws Exception {
        final Integer value = new Integer(10);
        Properties p = new Properties();
        ManagedConnectionFactoryImpl mcf = new ManagedConnectionFactoryImpl(p);

        mcf.setEJBServicePropertiesPollInterval(value);
        assertTrue(p.containsValue(value.toString()));
        assertEquals(value, mcf.getEJBServicePropertiesPollInterval());
    }

    public void testSetJAASLoginConfigName() throws Exception {
        final String name = "jaaslogin";
        Properties p = new Properties();
        ManagedConnectionFactoryImpl mcf = new ManagedConnectionFactoryImpl(p);

        mcf.setJAASLoginConfigName(name);
        assertTrue(p.containsValue(name));
        assertEquals(name, mcf.getJAASLoginConfigName());
    }

    public void testSetJAASLoginUserName() throws Exception {
        final String name = "jaasuser";
        Properties p = new Properties();
        ManagedConnectionFactoryImpl mcf = new ManagedConnectionFactoryImpl(p);

        mcf.setJAASLoginUserName(name);
        assertTrue(p.containsValue(name));
        assertEquals(name, mcf.getJAASLoginUserName());
    }

    public void testSetJAASLoginPassword() throws Exception {
        final String name = "jaaspassword";
        Properties p = new Properties();
        ManagedConnectionFactoryImpl mcf = new ManagedConnectionFactoryImpl(p);

        mcf.setJAASLoginPassword(name);
        assertTrue(p.containsValue(name));
        assertEquals(name, mcf.getJAASLoginPassword());
    }

    public void testSetLogLevelSetsLevelOnPlugin() throws Exception {
        Properties props = new Properties();
        ManagedConnectionFactoryImpl propsmcf = new ManagedConnectionFactoryImpl(props);

        final String logLevel = "DEBUG";
        propsmcf.setLogLevel(logLevel);
        assertTrue("prop is set", props.containsValue(logLevel));
    }

    public void testNoDefaultValueForJAASLoginConfigName() throws Exception {
        Properties p = new Properties();
        ManagedConnectionFactoryImpl mcf = new ManagedConnectionFactoryImpl(p);

        String value = mcf.getJAASLoginConfigName();
        assertNull("value must not be preset ", value);
    }

    public void testGetPropsURLFromBadURL() throws Exception {
        try {
            ManagedConnectionFactoryImpl mcf = new ManagedConnectionFactoryImpl();
            mcf.setEJBServicePropertiesURL("rubbish_bad:/rubbish_name.properties");
            mcf.getEJBServicePropertiesURLInstance();
            fail("expect an exception .");
        } catch (ResourceException re) {
            assertTrue("Cause is MalformedURLException, cause: " + re.getCause(),
                       re.getCause() instanceof MalformedURLException);
            assertTrue("Error message should contains rubbish_bad",
                       re.getMessage().indexOf("rubbish_bad") != -1);
        }
    }

    public void testImplementsEqualsAndHashCode() throws Exception {
        Method equalMethod = mci.getClass().getMethod("equals", new Class[] {Object.class});
        Method hashCodeMethod = mci.getClass().getMethod("hashCode", (Class[])null);

        assertTrue("not Object's equals method", equalMethod != Object.class
            .getDeclaredMethod("equals", new Class[] {Object.class}));
        assertTrue("not Object's hashCode method", hashCodeMethod != Object.class
            .getDeclaredMethod("hashCode", (Class[])null));
        assertEquals("equal with its self", mci, mci);
        assertTrue("not equal with another", !mci.equals(new ManagedConnectionFactoryImpl()));
        assertTrue("not equal with another thing", !mci.equals(this));
    }

    public void testMatchManagedConnectionsWithUnboundConnection() throws Exception {
        mci = new ManagedConnectionFactoryImplTester();
        Object unboundMC = mci.createManagedConnection(null, null);
        assertNotNull("MC must not be null.", unboundMC);
        Set<Object> mcSet = new HashSet<Object>();
        mcSet.add(unboundMC);
        assertSame("Must be same managed connection instance.",
                   mci.matchManagedConnections(mcSet, null, cri), unboundMC);
    }

    public void testMatchManagedConnectionsWithBoundConnections() throws Exception {

    
        Subject subj = new Subject();
        //NOTE Clean up the current bus instance
        //Bus.setCurrent(null);

        Bus bus = Bus.init();
        //EasyMock.reset(bus);
        ManagedConnectionFactoryImpl factory = EasyMock.createMock(ManagedConnectionFactoryImpl.class);
        factory.getBus();
        // In ManagedConnectionImpl:
        // one for getCeltixServiceFromBus , another for createInvocationHandler
        EasyMock.expectLastCall().andReturn(bus).times(4);
       
        EasyMock.replay(factory);
       

        ManagedConnectionImpl mc1 = new ManagedConnectionImpl(factory, cri, subj);
        Object connection = mc1.getConnection(subj, cri);
        assertNotNull("connection must not be null.", connection);

        ManagedConnectionImpl mc2 = new ManagedConnectionImpl(factory, cri2, subj);
        connection = mc2.getConnection(subj, cri2);
        assertNotNull("connection must not be null.", connection);
        
        EasyMock.verify(factory);
        
        Set<ManagedConnection> mcSet = new HashSet<ManagedConnection>();
        mcSet.add(mc1);
        mcSet.add(mc2);

        assertSame("MC1 must be selected.", mci.matchManagedConnections(mcSet, subj, cri), mc1);
        assertSame("MC2 must be selected.", mci.matchManagedConnections(mcSet, subj, cri2), mc2);
        assertNull("No connection must be selected.", mci.matchManagedConnections(mcSet, subj, cri3));
    }

    public void testValidateConnection() throws Exception {
        mci.validateReference(null, null);
    }

    public void testCreateConnectionFactoryNoArgsThrowsNotSupported() throws Exception {
        try {
            mci.createConnectionFactory();
            fail("expect non managed not supported on null MC");
        } catch (ResourceException expectd) {
            // do nothing here 
        }
    }

    public void testCreateConnectionFactoryNullCMThrows() throws Exception {
        try {
            mci.createConnectionFactory(null);
            fail("expect non managed not supported on null MC");
        } catch (ResourceException expectd) {
            // do nothing here
        }
    }

    public void testCreateConnectionFactoryCM() throws Exception {
        ManagedConnectionFactoryImplTester mcit = new ManagedConnectionFactoryImplTester();
        ConnectionManager connManager = EasyMock.createMock(ConnectionManager.class);
        assertTrue("We get a CF back",
                   mcit.createConnectionFactory(connManager) instanceof CeltixConnectionFactory);
        assertEquals("init was called once", 1, mcit.initCalledCount);
    }

    public void testCreateManagedConnection() throws Exception {
        ManagedConnectionFactoryImplTester mcit = new ManagedConnectionFactoryImplTester();
        assertTrue("We get a ManagedConnection back",
                   mcit.createManagedConnection(null, null) instanceof ManagedConnection);
        assertEquals("init was called once", 1, mcit.initCalledCount);
    }

    public void testCloseDoesNothing() throws Exception {
        mci.close();
    }

    /*public void testGetBusReturnNotNullIfConnectionFactoryCreated() throws ResourceException, Exception {
        System.setProperty("test.bus.class", DummyBus.class.getName());
        ClassLoader originalCl = Thread.currentThread().getContextClassLoader();
        try {
            // do this for MockObject creation
            Thread.currentThread().setContextClassLoader(mci.getClass().getClassLoader());

            mci.setArtixInstallDir(DummyBus.vobRoot());
            mci.setArtixCEURL(DummyBus.artixCEURL);
            ConnectionManager cm = (ConnectionManager)MockObjectFactory.create(Class
                .forName(ConnectionManager.class.getName(), true, mci.getClass().getClassLoader()));

            mci.createConnectionFactory(cm);
            assertNotNull("getBus() return not null after a connection factory created", mci.getBus());
        } finally {
            Thread.currentThread().setContextClassLoader(originalCl);
        }
    }

    public void testGetBusReturnNullIfNoConnectionFactoryCreated() throws ResourceException, Exception {
        System.setProperty("test.bus.class", DummyBus.class.getName());
        assertNull("getBus() return null", mci.getBus());
    }

    public void testBusInitializedForEachManagedConnectionFactory() throws ResourceException, Exception {
        System.setProperty("test.bus.class", DummyBus.class.getName());

        ClassLoader originalCl = Thread.currentThread().getContextClassLoader();
        try {
            // do this for MockObject creation
            Thread.currentThread().setContextClassLoader(mci.getClass().getClassLoader());

            Class dummyBusClass = Class.forName(DummyBus.class.getName(), true, mci.getClass()
                .getClassLoader());
            Field initializeCount = dummyBusClass.getField("initializeCount");

            mci.setCeltixInstallDir(DummyBus.vobRoot());
            mci.setCeltixCEURL(DummyBus.artixCEURL);
            ConnectionManager cm = (ConnectionManager)MockObjectFactory.create(Class
                .forName(ConnectionManager.class.getName(), true, mci.getClass().getClassLoader()));

            // first call
            mci.createConnectionFactory(cm);
            assertEquals("bus should be initialized once", 1, initializeCount.getInt(null));

            ManagedConnectionFactoryImpl mci2 = createManagedConnectionFactoryImpl();
            mci2.setArtixInstallDir(DummyBus.vobRoot());
            mci2.setArtixCEURL(DummyBus.artixCEURL);

            // second call
            mci2.createConnectionFactory(cm);
            assertEquals("bus initialized twice after second call", 2, initializeCount.getInt(null));
        } finally {
            Thread.currentThread().setContextClassLoader(originalCl);
        }
    }*/

    protected ManagedConnectionFactoryImpl createManagedConnectionFactoryImpl() {
        return new ManagedConnectionFactoryImpl();
    }

    public static Test suite() {
        return new TestSuite(ManagedConnectionFactoryImplTest.class);
    }

    public static void main(String[] args) {
        TestRunner.main(new String[] {ManagedConnectionFactoryImplTest.class.getName()});
    }
}

class ManagedConnectionFactoryImplTester extends ManagedConnectionFactoryImpl {
    int initCalledCount;

    ManagedConnectionFactoryImplTester() {
        super();
    }

    // dummy out init as it does all the JBus work
    protected void init(ClassLoader cl) {
        //busFactory = new BusFactory(this);
        initCalledCount++;
    }
    
}
