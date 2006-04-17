package org.objectweb.celtix.systest.type_test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import org.objectweb.type_test.types1.AnonymousStruct;
import org.objectweb.type_test.types1.BoundedArray;
import org.objectweb.type_test.types1.ChoiceArray;
import org.objectweb.type_test.types1.CompoundArray;
import org.objectweb.type_test.types1.DerivedStructBaseEmpty;
import org.objectweb.type_test.types1.Document;
import org.objectweb.type_test.types1.EmptyAll;
import org.objectweb.type_test.types1.EmptyChoice;
import org.objectweb.type_test.types1.EmptyStruct;
import org.objectweb.type_test.types1.ExtColourEnum;
import org.objectweb.type_test.types1.ExtendsSimpleContent;
import org.objectweb.type_test.types1.ExtendsSimpleType;
import org.objectweb.type_test.types1.FixedArray;
import org.objectweb.type_test.types1.NestedArray;
import org.objectweb.type_test.types1.NestedStruct;
import org.objectweb.type_test.types1.RecursiveStruct;
import org.objectweb.type_test.types1.RecursiveStructArray;
import org.objectweb.type_test.types1.RecursiveUnion;
import org.objectweb.type_test.types1.RecursiveUnionData;
import org.objectweb.type_test.types1.SimpleAll;
import org.objectweb.type_test.types1.SimpleChoice;
import org.objectweb.type_test.types1.SimpleContent1;
import org.objectweb.type_test.types1.SimpleContent2;
import org.objectweb.type_test.types1.SimpleContent3;
import org.objectweb.type_test.types1.SimpleStruct;
import org.objectweb.type_test.types1.StructWithList;
import org.objectweb.type_test.types1.StructWithNillables;
import org.objectweb.type_test.types1.StructWithOptionals;
import org.objectweb.type_test.types1.StructWithUnion;
import org.objectweb.type_test.types1.UnboundedArray;
import org.objectweb.type_test.types1.UnionSimpleContent;
import org.objectweb.type_test.types2.ExtBase64Binary;
import org.objectweb.type_test.types3.ChoiceWithSubstitutionGroup;
import org.objectweb.type_test.types3.ChoiceWithSubstitutionGroupAbstract;
import org.objectweb.type_test.types3.ChoiceWithSubstitutionGroupNil;
import org.objectweb.type_test.types3.ObjectFactory;
import org.objectweb.type_test.types3.RecElNextType;
import org.objectweb.type_test.types3.RecElType;
import org.objectweb.type_test.types3.RecInnerNextType;
import org.objectweb.type_test.types3.RecInnerType;
import org.objectweb.type_test.types3.RecMostInnerNextType;
import org.objectweb.type_test.types3.RecMostInnerType;
import org.objectweb.type_test.types3.RecOuterNextType;
import org.objectweb.type_test.types3.RecOuterType;
import org.objectweb.type_test.types3.SgBaseTypeA;
import org.objectweb.type_test.types3.SgDerivedTypeB;
import org.objectweb.type_test.types3.SgDerivedTypeC;
import org.objectweb.type_test.types3.StructWithMultipleSubstitutionGroups;
import org.objectweb.type_test.types3.StructWithSubstitutionGroup;
import org.objectweb.type_test.types3.StructWithSubstitutionGroupAbstract;
import org.objectweb.type_test.types3.StructWithSubstitutionGroupNil;

public abstract class AbstractTypeTestClient2 extends AbstractTypeTestClient {

    public AbstractTypeTestClient2(String name, QName theServicename,
            QName thePort, String theWsdlPath) {
        super(name, theServicename, thePort, theWsdlPath);
    }

    protected <T> boolean equalsNilable(T x, T y) {
        if (x == null) {
            return y == null;
        } else if (y == null) {
            return false;
        } else {
            return x.equals(y);
        }
    }

    protected <T> boolean notNull(T x, T y) {
        return x != null && y != null;
    }

    // org.objectweb.type_test.types1.EmptyStruct
    
    public void testEmptyStruct() throws Exception {
        EmptyStruct x = new EmptyStruct();
        EmptyStruct yOrig = new EmptyStruct();
        Holder<EmptyStruct> y = new Holder<EmptyStruct>(yOrig);
        Holder<EmptyStruct> z = new Holder<EmptyStruct>();
        EmptyStruct ret;
        if (testDocLiteral) {
            ret = docClient.testEmptyStruct(x, y, z);
        } else {
            ret = rpcClient.testEmptyStruct(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testEmptyStruct(): Null value for inout param",
                       notNull(x, y.value));
            assertTrue("testEmptyStruct(): Null value for out param",
                       notNull(yOrig, z.value));
            assertTrue("testEmptyStruct(): Null return value", notNull(x, ret));
        }

        //Test With Derived Instance
        DerivedStructBaseEmpty derivedX = new DerivedStructBaseEmpty();
        derivedX.setVarFloatExt(-3.14f);
        derivedX.setVarStringExt("DerivedStruct-x");
        derivedX.setAttrString("DerivedAttr-x");
        DerivedStructBaseEmpty derivedY = new DerivedStructBaseEmpty();
        derivedY.setVarFloatExt(1.414f);
        derivedY.setVarStringExt("DerivedStruct-y");
        derivedY.setAttrString("DerivedAttr-y");

        y = new Holder<EmptyStruct>(derivedY);
        z = new Holder<EmptyStruct>();
        if (testDocLiteral) {
            ret = docClient.testEmptyStruct(derivedX, y, z);
        } else {
            ret = rpcClient.testEmptyStruct(derivedX, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testEmptyStruct(): Null value for inout param",
                       notNull(derivedX, y.value));
            assertTrue("testEmptyStruct(): Null value for out param",
                       notNull(derivedY, z.value));
            assertTrue("testEmptyStruct(): Null return value", notNull(derivedX, ret));
        }
    }
    
    // org.objectweb.type_test.types1.SimpleStruct

    protected boolean equals(SimpleStruct x, SimpleStruct y) {
        return (Double.compare(x.getVarFloat(), y.getVarFloat()) == 0)
            && (x.getVarInt().compareTo(y.getVarInt()) == 0)
            && (x.getVarString().equals(y.getVarString()))
            && (equalsNilable(x.getVarAttrString(), y.getVarAttrString()));
    }
    
    public void testSimpleStruct() throws Exception {
        SimpleStruct x = new SimpleStruct();
        x.setVarFloat(3.14f);
        x.setVarInt(new BigInteger("42"));
        x.setVarString("Hello There");

        SimpleStruct yOrig = new SimpleStruct();
        yOrig.setVarFloat(1.414f);
        yOrig.setVarInt(new BigInteger("13"));
        yOrig.setVarString("Cheerio");

        Holder<SimpleStruct> y = new Holder<SimpleStruct>(yOrig);
        Holder<SimpleStruct> z = new Holder<SimpleStruct>();
        SimpleStruct ret;
        if (testDocLiteral) {
            ret = docClient.testSimpleStruct(x, y, z);
        } else {
            ret = rpcClient.testSimpleStruct(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testSimpleStruct(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testSimpleStruct(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testSimpleStruct(): Incorrect return value", equals(x, ret));
        }
    }
    
    // org.objectweb.type_test.types1.StructWithNillables

    protected boolean equals(StructWithNillables x, StructWithNillables y) {
        return equalsNilable(x.getVarFloat(), y.getVarFloat())
            && equalsNilable(x.getVarInt(), x.getVarInt())
            && equalsNilable(x.getVarString(), y.getVarString())
            && equalsNilable(x.getVarStruct(), y.getVarStruct());
    }
    
    public void testStructWithNillables() throws Exception {
        StructWithNillables x = new StructWithNillables();
        StructWithNillables yOrig = new StructWithNillables();
        yOrig.setVarFloat(new Float(1.414f));
        yOrig.setVarInt(new Integer(13));
        yOrig.setVarString("Cheerio");

        Holder<StructWithNillables> y = new Holder<StructWithNillables>(yOrig);
        Holder<StructWithNillables> z = new Holder<StructWithNillables>();
        StructWithNillables ret;
        if (testDocLiteral) {
            ret = docClient.testStructWithNillables(x, y, z);
        } else {
            ret = rpcClient.testStructWithNillables(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testStructWithNillables(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testStructWithNillables(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testStructWithNillables(): Incorrect return value", equals(x, ret));
        }
    }

    // org.objectweb.type_test.types1.AnonymousStruct
    // XXX - generated code flattens nested struct into top-level class.
    protected boolean equals(AnonymousStruct x, AnonymousStruct y) {
        return (x.getVarFloat() == y.getVarFloat())
            && (x.getVarInt() == y.getVarInt())
            && (x.getVarString().equals(y.getVarString()));
    }
    
    public void testAnonymousStruct() throws Exception {
        AnonymousStruct x = new AnonymousStruct();
        x.setVarInt(100);
        x.setVarString("hello");
        x.setVarFloat(1.1f);

        AnonymousStruct yOrig = new AnonymousStruct();
        yOrig.setVarInt(11);
        yOrig.setVarString("world");
        yOrig.setVarFloat(10.1f);

        Holder<AnonymousStruct> y = new Holder<AnonymousStruct>(yOrig);
        Holder<AnonymousStruct> z = new Holder<AnonymousStruct>();
        AnonymousStruct ret;
        if (testDocLiteral) {
            ret = docClient.testAnonymousStruct(x, y, z);
        } else {
            ret = rpcClient.testAnonymousStruct(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testAnonymousStruct(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testAnonymousStruct(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testAnonymousStruct(): Incorrect return value", equals(x, ret));
        }
    }
    
    // org.objectweb.type_test.types1.NestedStruct

    protected boolean equals(NestedStruct x, NestedStruct y) {
        return (x.getVarInt() == y.getVarInt())
            && (x.getVarFloat().compareTo(y.getVarFloat()) == 0)
            && (x.getVarString().equals(y.getVarString()))
            && equalsNilable(x.getVarEmptyStruct(), y.getVarEmptyStruct())
            && equalsNilableStruct(x.getVarStruct(), y.getVarStruct());
    }

    protected boolean equalsNilable(EmptyStruct x, EmptyStruct y) {
        if (x == null) {
            return y == null;
        }
        return y != null;
    }

    protected boolean equalsNilableStruct(SimpleStruct x, SimpleStruct y) {
        if (x == null) {
            return y == null;
        } else if (y == null) {
            return false;
        } else {
            return equals(x, y);
        }
    }
    
    public void testNestedStruct() throws Exception {
        SimpleStruct xs = new SimpleStruct();
        xs.setVarFloat(30.14);
        xs.setVarInt(new BigInteger("420"));
        xs.setVarString("NESTED Hello There"); 
        NestedStruct x = new NestedStruct();
        x.setVarFloat(new BigDecimal("3.14"));
        x.setVarInt(42);
        x.setVarString("Hello There");
        x.setVarEmptyStruct(new EmptyStruct());
        x.setVarStruct(xs);

        SimpleStruct ys = new SimpleStruct();
        ys.setVarFloat(10.414);
        ys.setVarInt(new BigInteger("130"));
        ys.setVarString("NESTED Cheerio");

        NestedStruct yOrig = new NestedStruct();
        yOrig.setVarFloat(new BigDecimal("1.414"));
        yOrig.setVarInt(13);
        yOrig.setVarString("Cheerio");
        yOrig.setVarEmptyStruct(new EmptyStruct());
        yOrig.setVarStruct(ys);

        Holder<NestedStruct> y = new Holder<NestedStruct>(yOrig);
        Holder<NestedStruct> z = new Holder<NestedStruct>();
        NestedStruct ret;
        if (testDocLiteral) {
            ret = docClient.testNestedStruct(x, y, z);
        } else {
            ret = rpcClient.testNestedStruct(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testNestedStruct(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testNestedStruct(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testNestedStruct(): Incorrect return value", equals(x, ret));
        }
    }
    
    // org.objectweb.type_test.types1.FixedArray
    
    public void testFixedArray() throws Exception {
        FixedArray x = new FixedArray();
        x.getItem().addAll(Arrays.asList(Integer.MIN_VALUE, 0, Integer.MAX_VALUE));

        FixedArray yOrig = new FixedArray();
        yOrig.getItem().addAll(Arrays.asList(-1, 0, 1));

        Holder<FixedArray> y = new Holder<FixedArray>(yOrig);
        Holder<FixedArray> z = new Holder<FixedArray>();
        FixedArray ret;
        if (testDocLiteral) {
            ret = docClient.testFixedArray(x, y, z);
        } else {
            ret = rpcClient.testFixedArray(x, y, z);
        }
        if (!perfTestOnly) {
            for (int i = 0; i < 3; i++) {
                assertEquals("testFixedArray(): Incorrect value for inout param",
                             x.getItem().get(i), y.value.getItem().get(i));
                assertEquals("testFixedArray(): Incorrect value for out param",
                             yOrig.getItem().get(i), z.value.getItem().get(i));
                assertEquals("testFixedArray(): Incorrect return value",
                             x.getItem().get(i), ret.getItem().get(i));
            }
        }
    }
    
    // org.objectweb.type_test.types1.BoundedArray
    
    public void testBoundedArray() throws Exception {
        BoundedArray x = new BoundedArray();
        x.getItem().addAll(Arrays.asList(-100.00f, 0f, 100.00f));
        BoundedArray yOrig = new BoundedArray();
        yOrig.getItem().addAll(Arrays.asList(-1f, 0f, 1f));

        Holder<BoundedArray> y = new Holder<BoundedArray>(yOrig);
        Holder<BoundedArray> z = new Holder<BoundedArray>();
        BoundedArray ret;
        if (testDocLiteral) {
            ret = docClient.testBoundedArray(x, y, z);
        } else {
            ret = rpcClient.testBoundedArray(x, y, z);
        }
        if (!perfTestOnly) {
            float delta = 0.0f;

            int xSize = x.getItem().size(); 
            int ySize = y.value.getItem().size(); 
            int zSize = z.value.getItem().size(); 
            int retSize = ret.getItem().size(); 
            assertTrue("testBoundedArray() array size incorrect",
                       xSize == ySize && ySize == zSize && zSize == retSize && xSize == 3);
            for (int i = 0; i < xSize; i++) {
                assertEquals("testBoundedArray(): Incorrect value for inout param",
                             x.getItem().get(i), y.value.getItem().get(i), delta);
                assertEquals("testBoundedArray(): Incorrect value for out param",
                             yOrig.getItem().get(i), z.value.getItem().get(i), delta);
                assertEquals("testBoundedArray(): Incorrect return value",
                             x.getItem().get(i), ret.getItem().get(i), delta);
            }
        }
    }
    
    // org.objectweb.type_test.types1.UnboundedArray

    protected boolean equals(UnboundedArray x, UnboundedArray y) {
        List<String> xx = x.getItem();
        List<String> yy = y.getItem();
        if (xx.size() != yy.size()) {
            return false;
        }
        for (int i = 0; i < xx.size(); i++) {
            if (!xx.get(i).equals(yy.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public void testUnboundedArray() throws Exception {
        UnboundedArray x = new UnboundedArray();
        x.getItem().addAll(Arrays.asList("AAA", "BBB", "CCC"));
        UnboundedArray yOrig = new UnboundedArray();
        yOrig.getItem().addAll(Arrays.asList("XXX", "YYY", "ZZZ"));

        Holder<UnboundedArray> y = new Holder<UnboundedArray>(yOrig);
        Holder<UnboundedArray> z = new Holder<UnboundedArray>();
        UnboundedArray ret;
        if (testDocLiteral) {
            ret = docClient.testUnboundedArray(x, y, z);
        } else {
            ret = rpcClient.testUnboundedArray(x, y, z);
        }
        if (!perfTestOnly) {
            for (int i = 0; i < 3; i++) {
                assertTrue("testUnboundedArray(): Incorrect value for inout param", equals(x, y.value));
                assertTrue("testUnboundedArray(): Incorrect value for out param", equals(yOrig, z.value));
                assertTrue("testUnboundedArray(): Incorrect return value", equals(x, ret));
            }
        }
    }
    
    // org.objectweb.type_test.types1.CompoundArray
    
    protected boolean equals(CompoundArray x, CompoundArray y) {
        return x.getArray1().equals(y.getArray1())
            && x.getArray2().equals(y.getArray2());
    }
    
    public void testCompoundArray() throws Exception {
        CompoundArray x = new CompoundArray();
        x.getArray1().addAll(Arrays.asList("AAA", "BBB", "CCC"));
        x.getArray2().addAll(Arrays.asList("aaa", "bbb", "ccc"));

        CompoundArray yOrig = new CompoundArray();
        yOrig.getArray1().addAll(Arrays.asList("XXX", "YYY", "ZZZ"));
        yOrig.getArray2().addAll(Arrays.asList("xxx", "yyy", "zzz"));

        Holder<CompoundArray> y = new Holder<CompoundArray>(yOrig);
        Holder<CompoundArray> z = new Holder<CompoundArray>();
        CompoundArray ret;
        if (testDocLiteral) {
            ret = docClient.testCompoundArray(x, y, z);
        } else {
            ret = rpcClient.testCompoundArray(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testCompoundArray(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testCompoundArray(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testCompoundArray(): Incorrect return value", equals(x, ret));
        }
    }

    // org.objectweb.type_test.types1.NestedArray
    
    public void testNestedArray() throws Exception {
        String[][] xs = {{"AAA", "BBB", "CCC"}, {"aaa", "bbb", "ccc"}, {"a_a_a", "b_b_b", "c_c_c"}};
        String[][] ys = {{"XXX", "YYY", "ZZZ"}, {"xxx", "yyy", "zzz"}, {"x_x_x", "y_y_y", "z_z_z"}};

        NestedArray x = new NestedArray();
        NestedArray yOrig = new NestedArray();

        List<UnboundedArray> xList = x.getSubarray();
        List<UnboundedArray> yList = yOrig.getSubarray();
        
        for (int i = 0; i < 3; i++) {
            UnboundedArray xx = new UnboundedArray();
            xx.getItem().addAll(Arrays.asList(xs[i]));
            xList.add(xx);
            UnboundedArray yy = new UnboundedArray();
            yy.getItem().addAll(Arrays.asList(ys[i]));
            yList.add(yy);
        }

        Holder<NestedArray> y = new Holder<NestedArray>(yOrig);
        Holder<NestedArray> z = new Holder<NestedArray>();
        NestedArray ret;
        if (testDocLiteral) {
            ret = docClient.testNestedArray(x, y, z);
        } else {
            ret = rpcClient.testNestedArray(x, y, z);
        }
        if (!perfTestOnly) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    assertEquals("testNestedArray(): Incorrect value for inout param",
                        x.getSubarray().get(i).getItem().get(j), 
                        y.value.getSubarray().get(i).getItem().get(j));
                    assertEquals("testNestedArray(): Incorrect value for out param",
                        yOrig.getSubarray().get(i).getItem().get(j),
                        z.value.getSubarray().get(i).getItem().get(j));
                    assertEquals("testNestedArray(): Incorrect return value",
                        x.getSubarray().get(i).getItem().get(j), ret.getSubarray().get(i).getItem().get(j));
                }
            }
        }
    }
    
    // org.objectweb.type_test.types1.StructWithList
    
    protected void assertEquals(String msg, StructWithList x, StructWithList y) throws Exception {
        assertTrue(msg, x != null);
        assertTrue(msg, y != null);

        List<String> xVar = x.getVarList();
        List<String> yVar = y.getVarList();
        assertTrue(xVar.size() == yVar.size());
        for (int i = 0; i < xVar.size(); ++i) {
            assertEquals(msg, xVar.get(i), yVar.get(i));
        }

        List<Integer> xAttr = x.getAttribList();
        List<Integer> yAttr = y.getAttribList();
        if (xAttr == null) {
            assertTrue(msg, yAttr == null);
        } else {
            assertTrue(xAttr.size() == yAttr.size());
            for (int i = 0; i < xAttr.size(); ++i) {
                assertEquals(msg, xAttr.get(i), yAttr.get(i));
            }
        }
    }

    public void testStructWithList() throws Exception {
        StructWithList x = new StructWithList();
        x.getVarList().add("I");
        x.getVarList().add("am");
        x.getVarList().add("StructWithList");

        StructWithList yOrig = new StructWithList();
        yOrig.getVarList().add("Does");
        yOrig.getVarList().add("StructWithList");
        yOrig.getVarList().add("work");

        Holder<StructWithList> y = new Holder<StructWithList>(yOrig);
        Holder<StructWithList> z = new Holder<StructWithList>();
        StructWithList ret;
        if (testDocLiteral) {
            ret = docClient.testStructWithList(x, y, z);
        } else {
            ret = rpcClient.testStructWithList(x, y, z);
        }
        if (!perfTestOnly) {
            assertEquals("testStructWithList(): Incorrect value for inout param", x, y.value);
            assertEquals("testStructWithList(): Incorrect value for out param", yOrig, z.value);
            assertEquals("testStructWithList(): Incorrect return value", x, ret);
        }

        x.getAttribList().add(1);
        x.getAttribList().add(2);
        x.getAttribList().add(3);
        y.value = yOrig;
        if (testDocLiteral) {
            ret = docClient.testStructWithList(x, y, z);
        } else {
            ret = rpcClient.testStructWithList(x, y, z);
        }
        if (!perfTestOnly) {
            assertEquals("testStructWithList(): Incorrect value for inout param", x, y.value);
            assertEquals("testStructWithList(): Incorrect value for out param", yOrig, z.value);
            assertEquals("testStructWithList(): Incorrect return value", x, ret);
        }

        yOrig.getAttribList().add(4);
        yOrig.getAttribList().add(5);
        yOrig.getAttribList().add(6);
        y.value = yOrig;
        if (testDocLiteral) {
            ret = docClient.testStructWithList(x, y, z);
        } else {
            ret = rpcClient.testStructWithList(x, y, z);
        }
        if (!perfTestOnly) {
            assertEquals("testStructWithList(): Incorrect value for inout param", x, y.value);
            assertEquals("testStructWithList(): Incorrect value for out param", yOrig, z.value);
            assertEquals("testStructWithList(): Incorrect return value", x, ret);
        }
    }
    
    // org.objectweb.type_test.types1.StructWithUnion

    protected void assertEquals(String msg, StructWithUnion x, StructWithUnion y) throws Exception {
        assertTrue(msg, x != null);
        assertTrue(msg, y != null);
        assertEquals(msg, x.getVarUnion(), y.getVarUnion());
        assertEquals(msg, x.getAttribUnion(), y.getAttribUnion());
    }

    public void testStructWithUnion() throws Exception {
        StructWithUnion x = new StructWithUnion();
        x.setVarUnion("999");
        StructWithUnion yOrig = new StructWithUnion();
        yOrig.setVarUnion("-999");

        Holder<StructWithUnion> y = new Holder<StructWithUnion>(yOrig);
        Holder<StructWithUnion> z = new Holder<StructWithUnion>();
        StructWithUnion ret;
        if (testDocLiteral) {
            ret = docClient.testStructWithUnion(x, y, z);
        } else {
            ret = rpcClient.testStructWithUnion(x, y, z);
        }
        if (!perfTestOnly) {
            assertEquals("testStructWithUnion(): Incorrect value for inout param", x, y.value);
            assertEquals("testStructWithUnion(): Incorrect value for out param", yOrig, z.value);
            assertEquals("testStructWithUnion(): Incorrect return value", x, ret);
        }

        x.setAttribUnion("99");
        y.value = yOrig;
        if (testDocLiteral) {
            ret = docClient.testStructWithUnion(x, y, z);
        } else {
            ret = rpcClient.testStructWithUnion(x, y, z);
        }
        if (!perfTestOnly) {
            assertEquals("testStructWithUnion(): Incorrect value for inout param", x, y.value);
            assertEquals("testStructWithUnion(): Incorrect value for out param", yOrig, z.value);
            assertEquals("testStructWithUnion(): Incorrect return value", x, ret);
        }

        yOrig.setAttribUnion("-99");
        y.value = yOrig;
        if (testDocLiteral) {
            ret = docClient.testStructWithUnion(x, y, z);
        } else {
            ret = rpcClient.testStructWithUnion(x, y, z);
        }
        if (!perfTestOnly) {
            assertEquals("testStructWithUnion(): Incorrect value for inout param", x, y.value);
            assertEquals("testStructWithUnion(): Incorrect value for out param", yOrig, z.value);
            assertEquals("testStructWithUnion(): Incorrect return value", x, ret);
        }
    }
    
    // org.objectweb.type_test.types1.EmptyChoice

    public void testEmptyChoice() throws Exception {
        EmptyChoice x = new EmptyChoice();
        EmptyChoice yOrig = new EmptyChoice();
        Holder<EmptyChoice> y = new Holder<EmptyChoice>(yOrig);
        Holder<EmptyChoice> z = new Holder<EmptyChoice>();
        EmptyChoice ret;
        if (testDocLiteral) {
            ret = docClient.testEmptyChoice(x, y, z);
        } else {
            ret = rpcClient.testEmptyChoice(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testEmptyChoice(): Null value for inout param",
                       notNull(x, y.value));
            assertTrue("testEmptyChoice(): Null value for out param",
                       notNull(yOrig, z.value));
            assertTrue("testEmptyChoice(): Null return value", notNull(x, ret));
        }
    }
    
    // org.objectweb.type_test.types1.SimpleChoice
    
    protected boolean equals(SimpleChoice x, SimpleChoice y) {
        if (x.getVarFloat() != null && y.getVarFloat() != null) {
            return x.getVarFloat().compareTo(y.getVarFloat()) == 0;
        } else if (x.getVarInt() != null && y.getVarInt() != null) {
            return x.getVarInt().compareTo(y.getVarInt()) == 0;
        } else if (x.getVarString() != null && y.getVarString() != null) {
            return x.getVarString().equals(y.getVarString());
        } else {
            return false;
        }
    }

    public void testSimpleChoice() throws Exception {
        SimpleChoice x = new SimpleChoice();
        x.setVarFloat(-3.14f);
        SimpleChoice yOrig = new SimpleChoice();
        yOrig.setVarString("Cheerio");

        Holder<SimpleChoice> y = new Holder<SimpleChoice>(yOrig);
        Holder<SimpleChoice> z = new Holder<SimpleChoice>();

        SimpleChoice ret;
        if (testDocLiteral) {
            ret = docClient.testSimpleChoice(x, y, z);
        } else {
            ret = rpcClient.testSimpleChoice(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testSimpleChoice(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testSimpleChoice(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testSimpleChoice(): Incorrect return value", equals(x, ret));
        }
    }
    
    // org.objectweb.type_test.types1.EmptyAll

    public void testEmptyAll() throws Exception {
        EmptyAll x = new EmptyAll();
        EmptyAll yOrig = new EmptyAll();
        Holder<EmptyAll> y = new Holder<EmptyAll>(yOrig);
        Holder<EmptyAll> z = new Holder<EmptyAll>();
        EmptyAll ret;
        if (testDocLiteral) {
            ret = docClient.testEmptyAll(x, y, z);
        } else {
            ret = rpcClient.testEmptyAll(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testEmptyAll(): Null value for inout param",
                       notNull(x, y.value));
            assertTrue("testEmptyAll(): Null value for out param",
                       notNull(yOrig, z.value));
            assertTrue("testEmptyAll(): Null return value", notNull(x, ret));
        }
    }
    
    // org.objectweb.type_test.types1.SimpleAll

    protected boolean equals(SimpleAll x, SimpleAll y) {
        return (x.getVarFloat() == y.getVarFloat())
            && (x.getVarInt() == y.getVarInt())
            && (x.getVarString().equals(y.getVarString()))
            && (x.getVarAttrString().equals(y.getVarAttrString()));
    }
    
    public void testSimpleAll() throws Exception {
        SimpleAll x = new SimpleAll();
        x.setVarFloat(3.14f);
        x.setVarInt(42);
        x.setVarString("Hello There");
        x.setVarAttrString("Attr-x");

        SimpleAll yOrig = new SimpleAll();
        yOrig.setVarFloat(-9.14f);
        yOrig.setVarInt(10);
        yOrig.setVarString("Cheerio");
        yOrig.setVarAttrString("Attr-y");

        Holder<SimpleAll> y = new Holder<SimpleAll>(yOrig);
        Holder<SimpleAll> z = new Holder<SimpleAll>();

        SimpleAll ret;
        if (testDocLiteral) {
            ret = docClient.testSimpleAll(x, y, z);
        } else {
            ret = rpcClient.testSimpleAll(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testSimpleAll(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testSimpleAll(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testSimpleAll(): Incorrect return value", equals(x, ret));
        }
    }
    
    // org.objectweb.type_test.types1.StructWithOptionals

    protected boolean equals(StructWithOptionals x, StructWithOptionals y) {
        return equalsNilable(x.getVarFloat(), y.getVarFloat())
            && equalsNilable(x.getVarInt(), x.getVarInt())
            && equalsNilable(x.getVarString(), y.getVarString())
            && equalsNilable(x.getVarStruct(), y.getVarStruct());
    }
    
    public void testStructWithOptionals() throws Exception {
        StructWithOptionals x = new StructWithOptionals();
        StructWithOptionals yOrig = new StructWithOptionals();
        yOrig.setVarFloat(new Float(1.414f));
        yOrig.setVarInt(new Integer(13));
        yOrig.setVarString("Cheerio");

        Holder<StructWithOptionals> y = new Holder<StructWithOptionals>(yOrig);
        Holder<StructWithOptionals> z = new Holder<StructWithOptionals>();

        StructWithOptionals ret;
        if (testDocLiteral) {
            ret = docClient.testStructWithOptionals(x, y, z);
        } else {
            ret = rpcClient.testStructWithOptionals(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testStructWithOptionals(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testStructWithOptionals(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testStructWithOptionals(): Incorrect return value", equals(x, ret));
        }
    }
    
    // org.objectweb.type_test.types1.RecursiveStruct

    protected boolean equals(RecursiveStruct x, RecursiveStruct y) {
        return (x.getVarFloat() == y.getVarFloat())
            && (x.getVarInt() == y.getVarInt())
            && (x.getVarString().equals(y.getVarString()))
            && equals(x.getVarStructArray(), y.getVarStructArray());
    }
    
    public void testRecursiveStruct() throws Exception {
        RecursiveStruct xtmp = new RecursiveStruct();
        xtmp.setVarFloat(0.14f);
        xtmp.setVarInt(4);
        xtmp.setVarString("tmp-x");
        xtmp.setVarStructArray(new RecursiveStructArray());

        RecursiveStruct ytmp = new RecursiveStruct();
        ytmp.setVarFloat(0.414f);
        ytmp.setVarInt(1);
        ytmp.setVarString("tmp-y");
        ytmp.setVarStructArray(new RecursiveStructArray());

        RecursiveStructArray arr = new RecursiveStructArray();
        arr.getItem().add(xtmp);
        arr.getItem().add(ytmp);

        RecursiveStruct x = new RecursiveStruct();
        x.setVarFloat(3.14f);
        x.setVarInt(42);
        x.setVarString("RecStruct-x");
        x.setVarStructArray(arr);

        RecursiveStruct yOrig = new RecursiveStruct();
        yOrig.setVarFloat(1.414f);
        yOrig.setVarInt(13);
        yOrig.setVarString("RecStruct-y");
        yOrig.setVarStructArray(arr);

        Holder<RecursiveStruct> y = new Holder<RecursiveStruct>(yOrig);
        Holder<RecursiveStruct> z = new Holder<RecursiveStruct>();
        RecursiveStruct ret;
        if (testDocLiteral) {
            ret = docClient.testRecursiveStruct(x, y, z);
        } else {
            ret = rpcClient.testRecursiveStruct(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testRecursiveStruct(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testRecursiveStruct(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testRecursiveStruct(): Incorrect return value", equals(ret, x));
        }
    }
    
    // org.objectweb.type_test.types1.RecursiveStructArray

    protected boolean equals(RecursiveStructArray x, RecursiveStructArray y) {
        List<RecursiveStruct> xx = x.getItem();
        List<RecursiveStruct> yy = y.getItem();
        if (xx.isEmpty() && yy.isEmpty()) {
            return true;
        }
        if (xx.size() != yy.size()) {
            return false;
        }
        for (int i = 0; i < xx.size(); i++) {
            if (!equals(xx.get(i), yy.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public void testRecursiveStructArray() throws Exception {
        RecursiveStruct xtmp = new RecursiveStruct();
        xtmp.setVarFloat(0.14f);
        xtmp.setVarInt(4);
        xtmp.setVarString("tmp-x");
        xtmp.setVarStructArray(new RecursiveStructArray());

        RecursiveStruct ytmp = new RecursiveStruct();
        ytmp.setVarFloat(0.414f);
        ytmp.setVarInt(1);
        ytmp.setVarString("tmp-y");
        ytmp.setVarStructArray(new RecursiveStructArray());

        RecursiveStructArray x = new RecursiveStructArray();
        x.getItem().add(xtmp);
        x.getItem().add(ytmp);
        RecursiveStructArray yOrig = new RecursiveStructArray();
        yOrig.getItem().add(ytmp);
        yOrig.getItem().add(xtmp);

        Holder<RecursiveStructArray> y = new Holder<RecursiveStructArray>(yOrig);
        Holder<RecursiveStructArray> z = new Holder<RecursiveStructArray>();
        RecursiveStructArray ret;
        if (testDocLiteral) {
            ret = docClient.testRecursiveStructArray(x, y, z);
        } else {
            ret = rpcClient.testRecursiveStructArray(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testRecursiveStructArray(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testRecursiveStructArray(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testRecursiveStructArray(): Incorrect return value", equals(ret, x));
        }
    }

    // org.objectweb.type_test.types1.RecursiveUnionData

    protected boolean equals(RecursiveUnion x, RecursiveUnion y) {
        if (x.getVarString() != null && y.getVarString() != null) {
            return x.getVarString().equals(y.getVarString());
        }
        if (x.getVarChoice() != null && y.getVarChoice() != null) {
            return equals(x.getVarChoice(), y.getVarChoice());
        }
        return false;
    }

    public void testRecursiveUnion() throws Exception {
        RecursiveUnion tmp1 = new RecursiveUnion();
        tmp1.setVarString("RecusiveUnion-1");
        RecursiveUnion tmp2 = new RecursiveUnion();
        tmp2.setVarString("RecusiveUnion-2");

        RecursiveUnionData xData = new RecursiveUnionData();
        ChoiceArray xChoice = new ChoiceArray();
        xChoice.getItem().add(tmp1);
        xChoice.getItem().add(tmp2);
        xData.setVarInt(5);
        xData.setVarChoiceArray(xChoice);

        RecursiveUnion x = new RecursiveUnion();
        x.setVarChoice(xData);

        RecursiveUnionData yData = new RecursiveUnionData();
        ChoiceArray yChoice = new ChoiceArray();
        yChoice.getItem().add(tmp1);
        yChoice.getItem().add(tmp2);
        yData.setVarInt(-5);
        yData.setVarChoiceArray(yChoice);

        RecursiveUnion yOrig = new RecursiveUnion();
        yOrig.setVarChoice(yData);

        Holder<RecursiveUnion> y = new Holder<RecursiveUnion>(yOrig);
        Holder<RecursiveUnion> z = new Holder<RecursiveUnion>();
        RecursiveUnion ret;
        if (testDocLiteral) {
            ret = docClient.testRecursiveUnion(x, y, z);
        } else {
            ret = rpcClient.testRecursiveUnion(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testRecursiveUnion(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testRecursiveUnion(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testRecursiveUnion(): Incorrect return value", equals(ret, x));
        }
    }

    // org.objectweb.type_test.types1.RecursiveUnionData

    protected boolean equals(RecursiveUnionData x, RecursiveUnionData y) {
        return x.getVarInt() == y.getVarInt()
            && equals(x.getVarChoiceArray(), y.getVarChoiceArray());
    }

    public void testRecursiveUnionData() throws Exception {
        RecursiveUnion tmp1 = new RecursiveUnion();
        tmp1.setVarString("RecusiveUnion-1");
        RecursiveUnion tmp2 = new RecursiveUnion();
        tmp2.setVarString("RecusiveUnion-2"); 

        RecursiveUnionData x = new RecursiveUnionData();
        ChoiceArray xChoice = new ChoiceArray();
        xChoice.getItem().add(tmp1);
        xChoice.getItem().add(tmp2);
        x.setVarInt(5);
        x.setVarChoiceArray(xChoice);

        RecursiveUnionData yOrig = new RecursiveUnionData();
        ChoiceArray yOrigchoice = new ChoiceArray();
        xChoice.getItem().add(tmp1);
        xChoice.getItem().add(tmp2);
        yOrig.setVarInt(-5);
        yOrig.setVarChoiceArray(yOrigchoice);

        Holder<RecursiveUnionData> y = new Holder<RecursiveUnionData>(yOrig);
        Holder<RecursiveUnionData> z = new Holder<RecursiveUnionData>();
        RecursiveUnionData ret;
        if (testDocLiteral) {
            ret = docClient.testRecursiveUnionData(x, y, z);
        } else {
            ret = rpcClient.testRecursiveUnionData(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testRecursiveUnionData(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testRecursiveUnionData(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testRecursiveUnionData(): Incorrect return value", equals(ret, x));
        }
    }
    
    // org.objectweb.type_test.types1.ChoiceArray

    protected boolean equals(ChoiceArray x, ChoiceArray y) {
        List<RecursiveUnion> xx = x.getItem();
        List<RecursiveUnion> yy = y.getItem();
        if (xx.isEmpty() && yy.isEmpty()) {
            return true;
        }
        if (xx.size() != yy.size()) {
            return false;
        }
        for (int i = 0; i < xx.size(); i++) {
            if (!equals(xx.get(i), yy.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public void testChoiceArray() throws Exception {
        RecursiveUnion tmp1 = new RecursiveUnion();
        tmp1.setVarString("RecusiveUnion-1");
        RecursiveUnion tmp2 = new RecursiveUnion();
        tmp2.setVarString("RecusiveUnion-2");

        ChoiceArray x = new ChoiceArray();
        x.getItem().add(tmp1);
        x.getItem().add(tmp2);
        ChoiceArray yOrig = new ChoiceArray();
        yOrig.getItem().add(tmp2);
        yOrig.getItem().add(tmp1);

        Holder<ChoiceArray> y = new Holder<ChoiceArray>(yOrig);
        Holder<ChoiceArray> z = new Holder<ChoiceArray>();
        ChoiceArray ret;
        if (testDocLiteral) {
            ret = docClient.testChoiceArray(x, y, z);
        } else {
            ret = rpcClient.testChoiceArray(x, y, z);
        }

        if (!perfTestOnly) {
            assertTrue("testChoiceArray(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testChoiceArray(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testChoiceArray(): Incorrect return value", equals(ret, x));
        }
    }

    // org.objectweb.type_test.types2.ExtendsSimpleType
    
    public void testExtendsSimpleType() throws Exception {
        ExtendsSimpleType x = new ExtendsSimpleType();
        x.setValue("foo");
        ExtendsSimpleType yOriginal = new ExtendsSimpleType();
        yOriginal.setValue("bar");
        Holder<ExtendsSimpleType> y = new Holder<ExtendsSimpleType>(yOriginal);
        Holder<ExtendsSimpleType> z = new Holder<ExtendsSimpleType>();
        ExtendsSimpleType ret;
        if (testDocLiteral) {
            ret = docClient.testExtendsSimpleType(x, y, z);
        } else {
            ret = rpcClient.testExtendsSimpleType(x, y, z);
        }
        if (!perfTestOnly) {
            assertEquals(x.getValue(), y.value.getValue());
            assertEquals(yOriginal.getValue(), z.value.getValue());
            assertEquals(x.getValue(), ret.getValue());
        }
    }
    
    // org.objectweb.type_test.types1.ExtendsSimpleContent

    public void testExtendsSimpleContent() throws Exception {
        ExtendsSimpleContent x = new ExtendsSimpleContent();
        x.setValue("foo");

        ExtendsSimpleContent yOriginal = new ExtendsSimpleContent();
        yOriginal.setValue("bar");
        Holder<ExtendsSimpleContent> y = new Holder<ExtendsSimpleContent>(yOriginal);
        Holder<ExtendsSimpleContent> z = new Holder<ExtendsSimpleContent>();
        ExtendsSimpleContent ret;
        if (testDocLiteral) {
            ret = docClient.testExtendsSimpleContent(x, y, z);
        } else {
            ret = rpcClient.testExtendsSimpleContent(x, y, z);
        }
        if (!perfTestOnly) {
            assertEquals(x.getValue(), y.value.getValue());
            assertEquals(yOriginal.getValue(), z.value.getValue());
            assertEquals(x.getValue(), ret.getValue());
        }
    }

    // org.objectweb.type_test.types1.Document
    
    protected void equals(String msg, Document x, Document y) throws Exception {
        assertEquals(msg, x.getValue(), y.getValue());
        assertEquals(msg, x.getID(), y.getID());
    }
    
    public void testDocument() throws Exception {
        Document x = new Document();
        x.setValue("content-x");
        x.setID("Hello There");
        Document yOrig = new Document();
        yOrig.setID("Cheerio");
        yOrig.setValue("content-y");

        Holder<Document> y = new Holder<Document>(yOrig);
        Holder<Document> z = new Holder<Document>();

        Document ret;
        if (testDocLiteral) {
            ret = docClient.testDocument(x, y, z);
        } else {
            ret = rpcClient.testDocument(x, y, z);
        }
        if (!perfTestOnly) {
            equals("testDocument(): Incorrect value for inout param", x, y.value);
            equals("testDocument(): Incorrect value for out param", yOrig, z.value);
            equals("testDocument(): Incorrect return value", x, ret);
        }

        x = new Document();
        yOrig = new Document();
        x.setValue("content-x");
        yOrig.setValue("content-y");
        x.setID(null);
        yOrig.setID(null);
        y = new Holder<Document>(yOrig);
        z = new Holder<Document>();

        if (testDocLiteral) {
            ret = docClient.testDocument(x, y, z);
        } else {
            ret = rpcClient.testDocument(x, y, z);
        }
        if (!perfTestOnly) {
            equals("testDocument(): Incorrect value for inout param", x, y.value);
            equals("testDocument(): Incorrect value for out param", yOrig, z.value);
            equals("testDocument(): Incorrect return value", x, ret);
            assertNull(y.value.getID());
            assertNull(ret.getID());
        }
    }
    
    //  org.objectweb.type_test.types1.ExtColourEnum
    // XXX - TODO - ColourEnum generated method fromValue() isn't static...
    protected boolean equals(ExtColourEnum x, ExtColourEnum y) {
        return (x.getAttrib1() == y.getAttrib1())
            && (x.getAttrib2().equals(y.getAttrib2()))
            && (x.getValue().equals(y.getValue()));
    }
    
    public void testExtColourEnum() throws Exception {
        /*
        ExtColourEnum x = new ExtColourEnum();
        x.setAttrib1(new Integer(1));
        x.setAttrib2("Ax");
        x.setValue(ColourEnum.fromValue("RED"));

        ExtColourEnum yOrig = new ExtColourEnum();
        yOrig.setAttrib1(new Integer(10));
        yOrig.setAttrib2("Ay");
        yOrig.setValue(ColourEnum.fromValue("GREEN"));

        Holder<ExtColourEnum> y = new Holder<ExtColourEnum>(yOrig);
        Holder<ExtColourEnum> z = new Holder<ExtColourEnum>();
        ExtColourEnum ret;
        if (testDocLiteral) {
            ret = docClient.testExtColourEnum(x, y, z);
        } else {
            ret = rpcClient.testExtColourEnum(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testExtColourEnum(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testExtColourEnum(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testExtColourEnum(): Incorrect return value", equals(x, ret));
        }
        */
    }
    
    // org.objectweb.type_test.types1.ExtBase64Binary;

    protected boolean equals(ExtBase64Binary x, ExtBase64Binary y) {
        return x.getId() == y.getId() && Arrays.equals(x.getValue(), y.getValue());
    }

    public void testExtBase64Binary() throws Exception {
        ExtBase64Binary x1 = new ExtBase64Binary();
        x1.setValue("base64a".getBytes());
        x1.setId(1);

        ExtBase64Binary y1 = new ExtBase64Binary();
        y1.setValue("base64b".getBytes());
        y1.setId(2);

        Holder<ExtBase64Binary> y1Holder = new Holder<ExtBase64Binary>(y1);
        Holder<ExtBase64Binary> z1 = new Holder<ExtBase64Binary>();
        ExtBase64Binary ret;
        if (testDocLiteral) {
            ret = docClient.testExtBase64Binary(x1, y1Holder, z1);
        } else {
            ret = rpcClient.testExtBase64Binary(x1, y1Holder, z1);
        }

        if (!perfTestOnly) {
            assertTrue("testExtBase64Binary(): Incorrect value for inout param",
                       equals(x1, y1Holder.value));
            assertTrue("testExtBase64Binary(): Incorrect value for out param",
                       equals(y1, z1.value));
            assertTrue("testExtBase64Binary(): Incorrect return value", equals(x1, ret));
        }
    }

    // org.objectweb.type_test.types3.StructWithSubstitutionGroup

    protected boolean equals(StructWithSubstitutionGroup x, StructWithSubstitutionGroup y) {
        if (!x.getSg01BaseElementA().isNil() 
            && !y.getSg01BaseElementA().isNil()) {
            SgBaseTypeA xTypeA = x.getSg01BaseElementA().getValue();
            SgBaseTypeA yTypeA = y.getSg01BaseElementA().getValue();
            return equals(xTypeA, yTypeA);
        }
        return false;
    }
    
    public void testStructWithSubstitutionGroup() throws Exception {
        SgBaseTypeA baseA = new SgBaseTypeA();
        baseA.setVarInt(new BigInteger("1"));

        SgDerivedTypeB derivedB = new SgDerivedTypeB();
        derivedB.setVarInt(new BigInteger("32"));
        derivedB.setVarString("foo");

        ObjectFactory objectFactory = new ObjectFactory();

        StructWithSubstitutionGroup x = new StructWithSubstitutionGroup();
        JAXBElement<? extends SgBaseTypeA> elementA = objectFactory.createSg01BaseElementA(baseA);
        x.setSg01BaseElementA(elementA);
        StructWithSubstitutionGroup yOrig = new StructWithSubstitutionGroup();
        JAXBElement<? extends SgBaseTypeA> elementB = objectFactory.createSg01DerivedElementB(derivedB);
        yOrig.setSg01BaseElementA(elementB);

        Holder<StructWithSubstitutionGroup> y = new Holder<StructWithSubstitutionGroup>(yOrig);
        Holder<StructWithSubstitutionGroup> z = new Holder<StructWithSubstitutionGroup>();
        StructWithSubstitutionGroup ret;
        if (testDocLiteral) {
            ret = docClient.testStructWithSubstitutionGroup(x, y, z);
        } else {
            ret = rpcClient.testStructWithSubstitutionGroup(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testStructWithSubstitutionGroup(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testStructWithSubstitutionGroup(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testStructWithSubstitutionGroup(): Incorrect return value", equals(x, ret));
        }
    }

    // org.objectweb.type_test.types3.StructWithSubstitutionGroupAbstract

    protected boolean equals(StructWithSubstitutionGroupAbstract x, StructWithSubstitutionGroupAbstract y) {
        if (x.getSg03AbstractBaseElementA() != null
            && y.getSg03AbstractBaseElementA() != null) {
            SgBaseTypeA xTypeA = x.getSg03AbstractBaseElementA().getValue();
            SgBaseTypeA yTypeA = y.getSg03AbstractBaseElementA().getValue();
            return equals(xTypeA, yTypeA);
        }
        return false;
    }

    public void testStructWithSubstitutionGroupAbstract() throws Exception {
        SgDerivedTypeB derivedB = new SgDerivedTypeB();
        derivedB.setVarInt(new BigInteger("32"));
        derivedB.setVarString("foo");

        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<SgDerivedTypeB> elementB = objectFactory.createSg03DerivedElementB(derivedB);

        SgDerivedTypeC derivedC = new SgDerivedTypeC();
        derivedC.setVarInt(new BigInteger("32"));
        derivedC.setVarFloat(3.14f);

        JAXBElement<SgDerivedTypeC> elementC = objectFactory.createSg03DerivedElementC(derivedC);

        StructWithSubstitutionGroupAbstract x = new StructWithSubstitutionGroupAbstract();
        x.setSg03AbstractBaseElementA(elementC);
        StructWithSubstitutionGroupAbstract yOrig = new StructWithSubstitutionGroupAbstract();
        yOrig.setSg03AbstractBaseElementA(elementB);

        Holder<StructWithSubstitutionGroupAbstract> y = 
            new Holder<StructWithSubstitutionGroupAbstract>(yOrig);
        Holder<StructWithSubstitutionGroupAbstract> z = new Holder<StructWithSubstitutionGroupAbstract>();
        StructWithSubstitutionGroupAbstract ret;
        if (testDocLiteral) {
            ret = docClient.testStructWithSubstitutionGroupAbstract(x, y, z);
        } else {
            ret = rpcClient.testStructWithSubstitutionGroupAbstract(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testStructWithSubstitutionGroupAbstract(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testStructWithSubstitutionGroupAbstract(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testStructWithSubstitutionGroupAbstract(): Incorrect return value", equals(x, ret));
        }
    }

    // org.objectweb.type_test.types3.StructWithSubstitutionGroupNil

    protected boolean equals(StructWithSubstitutionGroupNil x, StructWithSubstitutionGroupNil y) {
        if (x.getSg04NillableBaseElementA().isNil()) {
            return y.getSg04NillableBaseElementA().isNil();
        } else if (y.getSg04NillableBaseElementA().isNil()) {
            return false;
        } else {
            SgBaseTypeA xTypeA = x.getSg04NillableBaseElementA().getValue();
            SgBaseTypeA yTypeA = y.getSg04NillableBaseElementA().getValue();
            return equals(xTypeA, yTypeA);
        }
    }
    
    public void testStructWithSubstitutionGroupNil() throws Exception {
        StructWithSubstitutionGroupNil x = new StructWithSubstitutionGroupNil();
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<? extends SgBaseTypeA> element = objectFactory.createSg04NillableBaseElementA(null);
        x.setSg04NillableBaseElementA(element);
        StructWithSubstitutionGroupNil yOrig = new StructWithSubstitutionGroupNil();
        element = objectFactory.createSg04NillableBaseElementA(null);
        yOrig.setSg04NillableBaseElementA(element);

        Holder<StructWithSubstitutionGroupNil> y = new Holder<StructWithSubstitutionGroupNil>(yOrig);
        Holder<StructWithSubstitutionGroupNil> z = new Holder<StructWithSubstitutionGroupNil>();
        StructWithSubstitutionGroupNil ret;
        if (testDocLiteral) {
            ret = docClient.testStructWithSubstitutionGroupNil(x, y, z);
        } else {
            ret = rpcClient.testStructWithSubstitutionGroupNil(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testStructWithSubstitutionGroupNil(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testStructWithSubstitutionGroupNil(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testStructWithSubstitutionGroupNil(): Incorrect return value", equals(x, ret));
        }
    }

    // org.objectweb.type_test.types3.StructWithMultipleSubstitutionGroups

    protected boolean equals(StructWithMultipleSubstitutionGroups x, StructWithMultipleSubstitutionGroups y) {
        if (Double.compare(x.getVarFloat(), y.getVarFloat()) != 0) {
            return false;
        }
        if (x.getVarInt().compareTo(y.getVarInt()) != 0) {
            return false;
        }
        if (!x.getVarString().equals(y.getVarString())) {
            return false;
        }
        if (x.getSg01BaseElementA().isNil()) {
            if (!y.getSg01BaseElementA().isNil()) {
                return false;
            }
        } else if (y.getSg01BaseElementA().isNil()) {
            return false;
        } else {
            SgBaseTypeA xTypeA = x.getSg01BaseElementA().getValue();
            SgBaseTypeA yTypeA = y.getSg01BaseElementA().getValue();
            if (!equals(xTypeA, yTypeA)) {
                return false;
            }
        }
        if (x.getSg02BaseElementA().isNil()) {
            if (!y.getSg02BaseElementA().isNil()) {
                return false;
            }
        } else if (y.getSg02BaseElementA().isNil()) {
            return false;
        } else {
            SgBaseTypeA xTypeA = x.getSg02BaseElementA().getValue();
            SgBaseTypeA yTypeA = y.getSg02BaseElementA().getValue();
            return equals(xTypeA, yTypeA);
        }
        return true;
    }
    
    public void testStructWithMultipleSubstitutionGroups() throws Exception {
        SgBaseTypeA baseA = new SgBaseTypeA();
        baseA.setVarInt(new BigInteger("1"));
        
        SgDerivedTypeB derivedB = new SgDerivedTypeB();
        derivedB.setVarInt(new BigInteger("32"));
        derivedB.setVarString("y-SgDerivedTypeB");
        
        SgDerivedTypeC derivedC = new SgDerivedTypeC();
        derivedC.setVarInt(new BigInteger("1"));
        derivedC.setVarFloat(3.14f);
        
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<? extends SgBaseTypeA> x1 = objectFactory.createSg01DerivedElementB(derivedB);
        JAXBElement<? extends SgBaseTypeA> x2 = objectFactory.createSg02BaseElementA(baseA);
        JAXBElement<? extends SgBaseTypeA> y1 = objectFactory.createSg01DerivedElementB(derivedB);
        JAXBElement<? extends SgBaseTypeA> y2 = objectFactory.createSg02DerivedElementC(derivedC);
        
        StructWithMultipleSubstitutionGroups x = new StructWithMultipleSubstitutionGroups();
        x.setVarFloat(111.1f);
        x.setVarInt(new BigInteger("100"));
        x.setVarString("x-varString");
        x.setSg01BaseElementA(x1);
        x.setSg02BaseElementA(x2);
        StructWithMultipleSubstitutionGroups yOrig = new StructWithMultipleSubstitutionGroups();
        yOrig.setVarFloat(1.1f);
        yOrig.setVarInt(new BigInteger("10"));
        yOrig.setVarString("y-varString");
        yOrig.setSg01BaseElementA(y1);
        yOrig.setSg02BaseElementA(y2);

        Holder<StructWithMultipleSubstitutionGroups> y = 
            new Holder<StructWithMultipleSubstitutionGroups>(yOrig);
        Holder<StructWithMultipleSubstitutionGroups> z = 
            new Holder<StructWithMultipleSubstitutionGroups>();
        StructWithMultipleSubstitutionGroups ret;
        if (testDocLiteral) {
            ret = docClient.testStructWithMultipleSubstitutionGroups(x, y, z);
        } else {
            ret = rpcClient.testStructWithMultipleSubstitutionGroups(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testStructWithMultipleSubstitutionGroups(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testStructWithMultipleSubstitutionGroups(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testStructWithMultipleSubstitutionGroups(): Incorrect return value",
                       equals(x, ret));
        }
    }

    // org.objectweb.type_test.types3.ChoiceWithSubstitutionGroupAbstract

    protected boolean equals(ChoiceWithSubstitutionGroupAbstract x, ChoiceWithSubstitutionGroupAbstract y) {
        if (x.getVarInt() != null && y.getVarInt() != null
            && x.getVarInt().equals(y.getVarInt())) {
            return true;
        }
        if (!x.getSg03AbstractBaseElementA().isNil() && !y.getSg03AbstractBaseElementA().isNil()) {
            SgBaseTypeA xTypeA = x.getSg03AbstractBaseElementA().getValue();
            SgBaseTypeA yTypeA = y.getSg03AbstractBaseElementA().getValue();
            return equals(xTypeA, yTypeA);
        }
        return false;
    }
    
    public void testChoiceWithSubstitutionGroupAbstract() throws Exception {
        SgDerivedTypeB derivedB = new SgDerivedTypeB();
        derivedB.setVarInt(new BigInteger("32"));
        derivedB.setVarString("foo");

        SgDerivedTypeC derivedC = new SgDerivedTypeC();
        derivedC.setVarInt(new BigInteger("32"));
        derivedC.setVarFloat(3.14f);

        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<? extends SgBaseTypeA> elementB = objectFactory.createSg03DerivedElementB(derivedB);
        JAXBElement<? extends SgBaseTypeA> elementC = objectFactory.createSg03DerivedElementC(derivedC);
        
        ChoiceWithSubstitutionGroupAbstract x = new ChoiceWithSubstitutionGroupAbstract();
        x.setSg03AbstractBaseElementA(elementC);
        ChoiceWithSubstitutionGroupAbstract yOrig = new ChoiceWithSubstitutionGroupAbstract();
        yOrig.setSg03AbstractBaseElementA(elementB);

        Holder<ChoiceWithSubstitutionGroupAbstract> y =
            new Holder<ChoiceWithSubstitutionGroupAbstract>(yOrig);
        Holder<ChoiceWithSubstitutionGroupAbstract> z =
            new Holder<ChoiceWithSubstitutionGroupAbstract>();
        ChoiceWithSubstitutionGroupAbstract ret;
        if (testDocLiteral) {
            ret = docClient.testChoiceWithSubstitutionGroupAbstract(x, y, z);
        } else {
            ret = rpcClient.testChoiceWithSubstitutionGroupAbstract(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testChoiceWithSubstitutionGroupAbstract(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testChoiceWithSubstitutionGroupAbstract(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testChoiceWithSubstitutionGroupAbstract(): Incorrect return value", equals(x, ret));
        }
    }

    // org.objectweb.type_test.types3.ChoiceWithSubstitutionGroupNil

    protected boolean equals(ChoiceWithSubstitutionGroupNil x, ChoiceWithSubstitutionGroupNil y) {
        if (x.getVarInt() != null) {
            if (y.getVarInt() == null) {
                // x null, y non-null
                return false;
            } else if (x.getVarInt().isNil()) {
                return y.getVarInt().isNil();
            } else {
                if (y.getVarInt().isNil()) {
                    return false;
                }
                return x.getVarInt().getValue().equals(y.getVarInt().getValue());
            }
        } else if (y.getVarInt() != null) {
            return false;
        }
        if (x.getSg04NillableBaseElementA() != null) {
            if (y.getSg04NillableBaseElementA() == null) {
                // x null, y non-null
                return false;
            } else if (x.getSg04NillableBaseElementA().isNil()) {
                return y.getSg04NillableBaseElementA().isNil();
            } else {
                if (y.getSg04NillableBaseElementA().isNil()) {
                    return false;
                }
                SgBaseTypeA xTypeA = x.getSg04NillableBaseElementA().getValue();
                SgBaseTypeA yTypeA = y.getSg04NillableBaseElementA().getValue();
                return equals(xTypeA, yTypeA);
            }
        } else {
            return y.getSg04NillableBaseElementA() == null;
        }
    }
    
    public void testChoiceWithSubstitutionGroupNil() throws Exception {
        ObjectFactory objectFactory = new ObjectFactory();
        
        ChoiceWithSubstitutionGroupNil x = new ChoiceWithSubstitutionGroupNil();
        JAXBElement<BigInteger> varInt =
            objectFactory.createChoiceWithSubstitutionGroupNilVarInt(null);
        x.setVarInt(varInt);
        ChoiceWithSubstitutionGroupNil yOrig = new ChoiceWithSubstitutionGroupNil();
        JAXBElement<? extends SgBaseTypeA> elementA = 
            objectFactory.createSg04NillableBaseElementA(null);
        yOrig.setSg04NillableBaseElementA(elementA);

        Holder<ChoiceWithSubstitutionGroupNil> y = new Holder<ChoiceWithSubstitutionGroupNil>(yOrig);
        Holder<ChoiceWithSubstitutionGroupNil> z = new Holder<ChoiceWithSubstitutionGroupNil>();
        ChoiceWithSubstitutionGroupNil ret;
        if (testDocLiteral) {
            ret = docClient.testChoiceWithSubstitutionGroupNil(x, y, z); 
        } else {
            ret = rpcClient.testChoiceWithSubstitutionGroupNil(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testChoiceWithSubstitutionGroupNil(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testChoiceWithSubstitutionGroupNil(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testChoiceWithSubstitutionGroupNil(): Incorrect return value", equals(x, ret));
        }
    }

    // org.objectweb.type_test.types3.ChoiceWithSubstitutionGroup

    protected boolean equals(SgBaseTypeA x, SgBaseTypeA y) {
        if (x == null) {
            return y == null;
        } else if (y == null) {
            return false;
        }
        if (x.getVarInt().compareTo(y.getVarInt()) != 0) {
            return false;
        }
        if (x instanceof SgDerivedTypeC) {
            if (y instanceof SgDerivedTypeC) {
                SgDerivedTypeC xTypeC = (SgDerivedTypeC)x;
                SgDerivedTypeC yTypeC = (SgDerivedTypeC)y;
                return equals(xTypeC, yTypeC);
            } else {
                return false;
            }
        } else if (x instanceof SgDerivedTypeB) {
            if (y instanceof SgDerivedTypeB) {
                SgDerivedTypeB xTypeB = (SgDerivedTypeB)x;
                SgDerivedTypeB yTypeB = (SgDerivedTypeB)y;
                return equals(xTypeB, yTypeB);
            } else {
                return false;
            }
        }
        return true;
    }

    protected boolean equals(SgDerivedTypeB x, SgDerivedTypeB y) {
        return x.getVarString().equals(y.getVarString());
    }

    protected boolean equals(SgDerivedTypeC x, SgDerivedTypeC y) {
        return Double.compare(x.getVarFloat(), y.getVarFloat()) == 0;
    }

    protected boolean equals(ChoiceWithSubstitutionGroup x, ChoiceWithSubstitutionGroup y) {
        if (x.getVarInt() != null && y.getVarInt() != null
            && x.getVarInt().compareTo(y.getVarInt()) == 0) {
            return true;
        }
        if (!x.getSg01BaseElementA().isNil() && !y.getSg01BaseElementA().isNil()) {
            SgBaseTypeA xTypeA = x.getSg01BaseElementA().getValue();
            SgBaseTypeA yTypeA = y.getSg01BaseElementA().getValue();
            return equals(xTypeA, yTypeA);
        }
        return false;
    }

    public void testChoiceWithSubstitutionGroup() throws Exception {
        SgBaseTypeA baseA = new SgBaseTypeA();
        baseA.setVarInt(new BigInteger("1"));

        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<? extends SgBaseTypeA> elementA = objectFactory.createSg01BaseElementA(baseA);

        SgDerivedTypeB derivedB = new SgDerivedTypeB();
        derivedB.setVarInt(new BigInteger("32"));
        derivedB.setVarString("SgDerivedTypeB");

        JAXBElement<? extends SgBaseTypeA> elementB = objectFactory.createSg01DerivedElementB(derivedB);

        ChoiceWithSubstitutionGroup x = new ChoiceWithSubstitutionGroup();
        x.setSg01BaseElementA(elementA);
        ChoiceWithSubstitutionGroup yOrig = new ChoiceWithSubstitutionGroup();
        yOrig.setSg01BaseElementA(elementB);
        
        Holder<ChoiceWithSubstitutionGroup> y =
            new Holder<ChoiceWithSubstitutionGroup>(yOrig);
        Holder<ChoiceWithSubstitutionGroup> z =
            new Holder<ChoiceWithSubstitutionGroup>();
        assertTrue("yoo: ", equals(y.value, y.value));
        ChoiceWithSubstitutionGroup ret;
        if (testDocLiteral) {
            ret = docClient.testChoiceWithSubstitutionGroup(x, y, z);
        } else {
            ret = rpcClient.testChoiceWithSubstitutionGroup(x, y, z);
        }
        if (!perfTestOnly) {
            assertTrue("testChoiceWithSubstitutionGroup(): Incorrect value for inout param",
                       equals(x, y.value));
            assertTrue("testChoiceWithSubstitutionGroup(): Incorrect value for out param",
                       equals(yOrig, z.value));
            assertTrue("testChoiceWithSubstitutionGroup(): Incorrect return value", equals(x, ret));
        }
    }

    // org.objectweb.type_test.types3.RecElNextType

    protected boolean equals(RecElNextType x, RecElNextType y) {
        List<RecElType> xx = x.getRecEl();
        List<RecElType> yy = y.getRecEl();

        if (xx.size() != yy.size()) {
            return false;
        }
        for (int i = 0; i < xx.size(); i++) {
            if (!equals(xx.get(i), yy.get(i))) {
                return false;
            }
        }

        return true;
    }

    protected boolean equals(RecElType x, RecElType y) {
        return x.getVarInt() == y.getVarInt()
            && equals(x.getRecElNext(), y.getRecElNext());
    }

    public void testRecElType() throws Exception {
        RecElType x = new RecElType();
        RecElType y = new RecElType();
        RecElNextType xn = new RecElNextType();
        RecElNextType yn = new RecElNextType();

        y.setVarInt(123);
        y.setRecElNext(yn);

        xn.getRecEl().add(y);
        x.setVarInt(456);
        x.setRecElNext(xn);

        Holder<RecElType> yh = new Holder<RecElType>(y);
        Holder<RecElType> zh = new Holder<RecElType>();
        RecElType ret;
        if (testDocLiteral) {
            ret = docClient.testRecElType(x, yh, zh);
        } else {
            ret = rpcClient.testRecElType(x, yh, zh);
        }

        if (!perfTestOnly) {
            assertTrue("testRecElType(): Incorrect value for inout param",
                equals(x, yh.value));
            assertTrue("testRecElType(): Incorrect value for inout param",
                equals(y, zh.value));
            assertTrue("testRecElType(): Incorrect value for inout param",
                equals(ret, x));
        }
    }

    // org.objectweb.type_test.types3.RecOuterType

    protected boolean equals(RecMostInnerType x, RecMostInnerType y) {
        return x.getVarInt() == y.getVarInt()
            && equals(x.getRecMostInnerNext(), y.getRecMostInnerNext());
    }

    protected boolean equals(RecMostInnerNextType x, RecMostInnerNextType y) {
        List<RecMostInnerType> xx = x.getRecMostInner();
        List<RecMostInnerType> yy = y.getRecMostInner();

        if (xx.size() != yy.size()) {
            return false;
        }
        for (int i = 0; i < xx.size(); i++) {
            if (!equals(xx.get(i), yy.get(i))) {
                return false;
            }
        }

        return true;
    }

    protected boolean equals(RecInnerType x, RecInnerType y) {
        List<RecMostInnerType> mitx = x.getRecMostInner();
        List<RecMostInnerType> mity = y.getRecMostInner();

        if (mitx.size() != mity.size()) {
            return false;
        }
        for (int i = 0; i < mitx.size(); i++) {
            if (!equals(mitx.get(i), mity.get(i))) {
                return false;
            }
        }

        return x.getVarInt() == y.getVarInt()
            && equals(x.getRecInnerNext(), y.getRecInnerNext());
    }

    protected boolean equals(RecInnerNextType x, RecInnerNextType y) {
        List<RecInnerType> xx = x.getRecInner();
        List<RecInnerType> yy = y.getRecInner();

        if (xx.size() != yy.size()) {
            return false;
        }
        for (int i = 0; i < xx.size(); i++) {
            if (!equals(xx.get(i), yy.get(i))) {
                return false;
            }
        }

        return true;
    }

    protected boolean equals(RecOuterNextType x, RecOuterNextType y) {
        List<RecOuterType> xx = x.getRecOuter();
        List<RecOuterType> yy = y.getRecOuter();

        if (xx.size() != yy.size()) {
            return false;
        }
        for (int i = 0; i < xx.size(); i++) {
            if (!equals(xx.get(i), yy.get(i))) {
                return false;
            }
        }

        return true;
    }

    protected boolean equals(RecOuterType x, RecOuterType y) {
        List<RecMostInnerType> mitx = x.getRecMostInner();
        List<RecMostInnerType> mity = y.getRecMostInner();
        List<RecInnerType> itx = x.getRecInner();
        List<RecInnerType> ity = y.getRecInner();
 
        if (mitx.size() != mity.size() || itx.size() != ity.size()) {
            return false;
        }
        for (int i = 0; i < mitx.size(); i++) {
            if (!equals(mitx.get(i), mity.get(i))) {
                return false;
            }
        }
        for (int i = 0; i < itx.size(); i++) {
            if (!equals(itx.get(i), ity.get(i))) {
                return false;
            }
        }
        return x.getVarInt() == y.getVarInt()
            && equals(x.getRecOuterNext(), y.getRecOuterNext());
    }

    public void testRecOuterType() throws Exception {
        RecMostInnerType mitx = new RecMostInnerType();
        RecMostInnerType mity = new RecMostInnerType();
        RecMostInnerNextType mitxNext = new RecMostInnerNextType();
        RecMostInnerNextType mityNext = new RecMostInnerNextType();
        mitx.setRecMostInnerNext(mitxNext);
        mity.setRecMostInnerNext(mityNext);

        RecInnerType itx = new RecInnerType();
        RecInnerType ity = new RecInnerType();
        RecInnerNextType itxNext = new RecInnerNextType();
        RecInnerNextType ityNext = new RecInnerNextType();
        itx.setRecInnerNext(itxNext);
        ity.setRecInnerNext(ityNext);

        RecOuterType otx = new RecOuterType();
        RecOuterType oty = new RecOuterType();
        RecOuterNextType otxNext = new RecOuterNextType();
        RecOuterNextType otyNext = new RecOuterNextType();
        otx.setRecOuterNext(otxNext);
        oty.setRecOuterNext(otyNext);

        mitx.setVarInt(11);
        mity.setVarInt(12);
        mitxNext.getRecMostInner().add(mity);

        itx.setVarInt(21);
        ity.setVarInt(22);
        itxNext.getRecInner().add(ity);
        itx.getRecMostInner().add(mitx);

        otx.setVarInt(31);
        oty.setVarInt(32);
        otxNext.getRecOuter().add(oty);
        otx.getRecInner().add(itx);
        otx.getRecMostInner().add(mitx);

        Holder<RecOuterType> yh = new Holder<RecOuterType>(oty);
        Holder<RecOuterType> zh = new Holder<RecOuterType>();
        RecOuterType ret;
        if (testDocLiteral) {
            ret = docClient.testRecOuterType(otx, yh, zh);
        } else {
            ret = rpcClient.testRecOuterType(otx, yh, zh);
        }
        if (!perfTestOnly) {
            assertTrue("testRecOuterType(): Incorrect value for inout param",
                equals(otx, yh.value));
            assertTrue("testRecOuterType(): Incorrect value for inout param",
                equals(oty, zh.value));
            assertTrue("testRecOuterType(): Incorrect value for inout param",
                equals(ret, otx));
        }
    }
    
    // org.objectweb.type_test.types1.SimpleContent1

    protected void equals(String msg, SimpleContent1 x, SimpleContent1 y) throws
Exception {
        assertEquals(msg, x.getAttrib1A(), y.getAttrib1A());
        assertEquals(msg, x.getAttrib1B(), y.getAttrib1B());
        assertEquals(msg, x.getValue(), y.getValue());
    }

    public void testSimpleContent1() throws Exception {
        SimpleContent1 x1 = new SimpleContent1();
        x1.setValue("foo");
        x1.setAttrib1A(new Byte((byte)1));
        x1.setAttrib1B(new Short((short)2));

        SimpleContent1 y1 = new SimpleContent1();
        y1.setValue("bar");
        y1.setAttrib1A(new Byte((byte)3));
        y1.setAttrib1B(new Short((short)4));

        Holder<SimpleContent1> y1Holder = new Holder<SimpleContent1>(y1);
        Holder<SimpleContent1> z1 = new Holder<SimpleContent1>();
        SimpleContent1 ret;
        if (testDocLiteral) {
            ret = docClient.testSimpleContent1(x1, y1Holder, z1);
        } else {
            ret = rpcClient.testSimpleContent1(x1, y1Holder, z1);
        }
        if (!perfTestOnly) {
            equals("testSimpleContent1(): Incorrect value for inout param", x1, y1Holder.value);
            equals("testSimpleContent1(): Incorrect value for out param", y1, z1.value);
            equals("testSimpleContent1(): Incorrect return value", x1, ret);
        }
    }
    
    // org.objectweb.type_test.types1.SimpleContent2

    protected void equals(String msg, SimpleContent2 x, SimpleContent2 y) throws
Exception {
        assertEquals(msg, x.getAttrib2A(), y.getAttrib2A());
        assertEquals(msg, x.getAttrib2B(), y.getAttrib2B());
        equals(msg, (SimpleContent1)x, (SimpleContent1)y);
    }

    public void testSimpleContent2() throws Exception {
        SimpleContent2 x2 = new SimpleContent2();
        x2.setValue("foo");
        x2.setAttrib1A(new Byte((byte)1));
        x2.setAttrib1B(new Short((short)2));
        x2.setAttrib2A(new Integer(5));
        x2.setAttrib2B(new Long(6));

        SimpleContent2 y2 = new SimpleContent2();
        y2.setValue("bar");
        y2.setAttrib1A(new Byte((byte)3));
        y2.setAttrib1B(new Short((short)4));
        y2.setAttrib2A(new Integer(7));
        y2.setAttrib2B(new Long(8));

        Holder<SimpleContent2> y2Holder = new Holder<SimpleContent2>(y2);
        Holder<SimpleContent2> z2 = new Holder<SimpleContent2>();
        SimpleContent2 ret;
        if (testDocLiteral) {
            ret = docClient.testSimpleContent2(x2, y2Holder, z2);
        } else {
            ret = rpcClient.testSimpleContent2(x2, y2Holder, z2);
        }
        if (!perfTestOnly) {
            equals("testSimpleContent2(): Incorrect value for inout param", x2, y2Holder.value);
            equals("testSimpleContent2(): Incorrect value for out param", y2, z2.value);
            equals("testSimpleContent2(): Incorrect return value", x2, ret);
        }
    }
    
    // org.objectweb.type_test.types1.SimpleContent3

    protected void equals(String msg, SimpleContent3 x, SimpleContent3 y) throws
Exception {
        assertEquals(msg, x.getAttrib3A(), y.getAttrib3A());
        assertEquals(msg, x.isAttrib3B(), y.isAttrib3B());
        equals(msg, (SimpleContent2)x, (SimpleContent2)y);
    }

    public void testSimpleContent3() throws Exception {
        SimpleContent3 x3 = new SimpleContent3();
        x3.setValue("foo");
        x3.setAttrib1A(new Byte((byte)1));
        x3.setAttrib1B(new Short((short)2));
        x3.setAttrib2A(new Integer(5));
        x3.setAttrib2B(new Long(6));
        x3.setAttrib3A("xxx");
        x3.setAttrib3B(Boolean.TRUE);

        SimpleContent3 y3 = new SimpleContent3();
        y3.setValue("bar");
        y3.setAttrib1A(new Byte((byte)3));
        y3.setAttrib1B(new Short((short)4));
        y3.setAttrib2A(new Integer(7));
        y3.setAttrib2B(new Long(8));
        y3.setAttrib3A("yyy");
        y3.setAttrib3B(Boolean.FALSE);

        Holder<SimpleContent3> y3Holder = new Holder<SimpleContent3>(y3);
        Holder<SimpleContent3> z3 = new Holder<SimpleContent3>();
        SimpleContent3 ret;
        if (testDocLiteral) {
            ret = docClient.testSimpleContent3(x3, y3Holder, z3);
        } else {
            ret = rpcClient.testSimpleContent3(x3, y3Holder, z3);
        }
        if (!perfTestOnly) {
            equals("testSimpleContent3(): Incorrect value for inout param", x3, y3Holder.value);
            equals("testSimpleContent3(): Incorrect value for out param", y3, z3.value);
            equals("testSimpleContent3(): Incorrect return value", x3, ret);
        }
    }
    
    // org.objectweb.type_test.types1.UnionSimpleContent

    protected void assertEquals(String msg, UnionSimpleContent x, UnionSimpleContent y) throws Exception {
        assertTrue(msg, x != null);
        assertTrue(msg, y != null);
        assertEquals(msg, x.getValue(), y.getValue());
    }
    
    // XXX - union setValue(String)
    public void testUnionSimpleContent() throws Exception {
        UnionSimpleContent x = new UnionSimpleContent();
        x.setValue("5");
        UnionSimpleContent yOrig = new UnionSimpleContent();
        yOrig.setValue("-7");

        Holder<UnionSimpleContent> y = new Holder<UnionSimpleContent>(yOrig);
        Holder<UnionSimpleContent> z = new Holder<UnionSimpleContent>();
        UnionSimpleContent ret;
        if (testDocLiteral) {
            ret = docClient.testUnionSimpleContent(x, y, z);
        } else {
            ret = rpcClient.testUnionSimpleContent(x, y, z);
        }
        if (!perfTestOnly) {
            assertEquals("testUnionSimpleContent(): Incorrect value for inout param", x, y.value);
            assertEquals("testUnionSimpleContent(): Incorrect value for out param", yOrig, z.value);
            assertEquals("testUnionSimpleContent(): Incorrect return value", x, ret);
        }
    }

}
