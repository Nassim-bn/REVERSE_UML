package fr.univrouen.umlreverse.model.diagram.clazz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import fr.univrouen.umlreverse.model.project.IType;
import fr.univrouen.umlreverse.model.project.Type;

/**
 * Class of test for the class Type.
 */
public class TypeTest {
    @Test
    public void testConstructorOneArgument () throws Exception {
        Type t = new Type("Type");
        assertEquals("Type", t.getType());
        assertEquals(false, t.isConstant());
        assertEquals(0, t.getGenericArguments().size());
    }

    @Test
    public void testConstructorTwoArguments () throws Exception {
        Type t = new Type("Type", true);
        assertEquals("Type", t.getType());
        assertEquals(true, t.isConstant());
        assertEquals(0, t.getGenericArguments().size());
    }

    @Test
    public void testConstructorThreeArguments () throws Exception {
        List<IType> generic = new ArrayList<>();
        generic.add(new Type("GenericType1"));
        generic.add(new Type("GenericType2"));
        Type t = new Type("Type", true, generic);

        assertEquals("Type", t.getType());
        assertEquals(true, t.isConstant());
        List<String> s = t.getGenericArguments().stream().map(IType::getType).collect(Collectors.toList());
        assertEquals(true, s.get(0).equals("GenericType1") && s.get(1).equals("GenericType2"));
    }

    @Test
    public void testIsGenericArgumentsEmptyReturnFalse() throws Exception {
        List<IType> generic = new ArrayList<>();
        Type t = new Type("Type", true, generic);
        assertEquals(false, t.isGeneric());
    }


    @Test
    public void testIsGenericArgumentsNotEmptyReturnTrue() throws Exception {
        List<IType> generic = new ArrayList<>();
        generic.add(new Type("Peanut"));

        Type t = new Type("Type", true, generic);
        assertEquals(true, t.isGeneric());
    }

    @Test
    public void testToStringComplexType() throws Exception {
        List<IType> generic = new ArrayList<>();
        generic.add(new Type("GenericType1"));
        generic.add(new Type("GenericType2"));

        Type t = new Type("Type", true, generic);
        assertEquals("const Type<GenericType1, GenericType2>", t.toString());
    }

    @Test
    public void testToStringWithoutGeneric() throws Exception {
        Type t = new Type("Type");
        assertEquals("Type", t.toString());
    }

    @Test
    public void testEqualsSimilarReturnTrue() throws Exception {
        List<IType> generic1 = new ArrayList<>();
        generic1.add(new Type("GenericType1"));
        generic1.add(new Type("GenericType2"));
        Type t1 = new Type("Type", true, generic1);

        List<IType> generic2 = new ArrayList<>();
        generic2.add(new Type("GenericType1"));
        generic2.add(new Type("GenericType2"));
        Type t2 = new Type("Type", true, generic2);

        assertEquals(true, t1.equals(t2));
    }

    @Test
    public void testHashCodeEqualsGiveSameHashCode() throws Exception {
        List<IType> generic1 = new ArrayList<>();
        generic1.add(new Type("GenericType1"));
        generic1.add(new Type("GenericType2"));
        Type t1 = new Type("Type", true, generic1);

        List<IType> generic2 = new ArrayList<>();
        generic2.add(new Type("GenericType1"));
        generic2.add(new Type("GenericType2"));
        Type t2 = new Type("Type", true, generic2);

        assertEquals(true, t1.hashCode() == t2.hashCode());
    }

    @Test
    public void testSetGenericArgumentsArgNullGiveEmptyGenericList() throws Exception {
        List<IType> generic = new ArrayList<>();
        generic.add(new Type("GenericType1"));
        generic.add(new Type("GenericType2"));
        Type t = new Type("Type", true, generic);
        t.setGenericArguments(null);
        assertEquals(true, t.getGenericArguments().isEmpty());
    }

    @Test
    public void testSetTypeNullReturnException() throws Exception {
        Type t = new Type("a");
        try {
            t.setType(null);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // waited exception
        }
    }

    @Test
    public void testSetConstantArgTrueGiveTrue() throws Exception {
        Type t = new Type("a");
        t.setConstant(true);
        assertEquals(true, t.isConstant());
    }

    @Test
    public void testSetConstantArgFalseGiveFalse() throws Exception {
        Type t = new Type("a");
        t.setConstant(false);
        assertEquals(false, t.isConstant());
    }

    @Test
    public void testFireVetoableChangeTestListenerWhenNameIsChanged() throws Exception {
        Type t = new Type("a");
        t.addVetoableChangeListener(evt -> {
            if (evt.getPropertyName().equals("TypeChanged")) {
                throw new IllegalArgumentException("");
            }
        });

        try {
            t.setType("b");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // waited exception
        }
    }

    @Test
    public void testTypeFromString() throws Exception {
        IType t = Type.getTypefromString("int");
        assert t != null;
        assertEquals(t.getType(), "int");
        assertEquals(t.getGenericArguments().size(), 0);
        assertEquals(t.isConstant(), false);

        IType t2 = Type.getTypefromString("const int");
        assert t2 != null;
        assertEquals(t2.getType(), "int");
        assertEquals(t2.getGenericArguments().size(), 0);
        assertEquals(t2.isConstant(), true);

        IType t3 = Type.getTypefromString("       const        i n t     <      const type1    <type imbr ique   ,    const      cacah uete>      ,      autre type     >     ");
        assert t3 != null;
        List<IType> generic2 = new LinkedList<>();
        generic2.add(new Type("type imbr ique"));
        generic2.add(new Type("cacah uete", true));
        List<IType> generic = new LinkedList<>();
        generic.add(new Type("type1", true, generic2));
        generic.add(new Type("autre type"));
        generic2.add(new Type("type imbrique"));
        assertEquals("i n t", t3.getType());
        assertEquals(generic, t3.getGenericArguments());
        assertEquals(true, t3.isConstant());

        assertEquals(null, Type.getTypefromString(""));
        assertEquals(null, Type.getTypefromString("const const const<"));
        assertEquals(null, Type.getTypefromString("a<<>"));
        assertEquals(null, Type.getTypefromString("a<>"));
        assertEquals(null, Type.getTypefromString("a<<>>>"));
        assertEquals(null, Type.getTypefromString("const "));
    }

}