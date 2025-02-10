package fr.univrouen.umlreverse.model.diagram.clazz;

import fr.univrouen.umlreverse.model.diagram.common.Direction;
import fr.univrouen.umlreverse.model.project.*;
import fr.univrouen.umlreverse.model.project.Package;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by BELKHOUS
 */
public class ObjectEntityTest {

    @Test
    public void testGetSetType() throws Exception {
        //Given
        IObjectEntity OE = new ObjectEntity("test", TypeEntity.Clazz, Visibility.Packaged);
        OE.setType(TypeEntity.Abstract);
        //When
        TypeEntity t = OE.getType();
        //Then
        assertEquals(TypeEntity.Abstract,t);
    }

    @Test
    public void testGetSetVisibility() throws Exception {
        IObjectEntity OE = new ObjectEntity("test", TypeEntity.Clazz, Visibility.Packaged);
        //When
        OE.setVisibility(Visibility.Protected);
        //Then
        assertEquals(Visibility.Protected,OE.getVisibility());
    }


    @Test
    public void testAddGetRemoveAbstractTexts() throws Exception {
        IObjectEntity OE = new ObjectEntity("test", TypeEntity.Clazz, Visibility.Packaged);
        List<String> abstractTextsTest = new LinkedList<>();

        abstractTextsTest.add("AbstractText1");
        abstractTextsTest.add("AbstractText2");
        abstractTextsTest.add("AbstractText3");

        OE.addAbstractText("AbstractText1");
        OE.addAbstractText("AbstractText2");
        OE.addAbstractText("AbstractText3");

        assertTrue(OE.getAbstractTexts().contains("AbstractText1"));
        assertTrue(OE.getAbstractTexts().contains("AbstractText2"));
        assertTrue(OE.getAbstractTexts().contains("AbstractText3"));

        assertEquals(OE.getAbstractTexts(),abstractTextsTest);

        OE.removeAbstractText("AbstractText1");
        assertFalse(OE.getAbstractTexts().contains("AbstractText1"));
    }

    @Test
    public void testAddGetRemoveEnumFields() throws Exception {
        IObjectEntity OE = new ObjectEntity("test", TypeEntity.Clazz, Visibility.Packaged);
        List<String> enumFieldsTest = new LinkedList<>();

        enumFieldsTest.add("field1");
        enumFieldsTest.add("field2");

        OE.addEnumField("field1");
        OE.addEnumField("field2");

        assertTrue(OE.getEnumFields().contains("field1"));
        assertTrue(OE.getEnumFields().contains("field2"));
        assertEquals(OE.getEnumFields(),enumFieldsTest);

        OE.removeEnumField("field1");
        assertFalse(OE.getEnumFields().contains("field1"));

    }

    @Test
    public void testAddGetRemoveListMethod() throws Exception {
        IObjectEntity OE = new ObjectEntity("test", TypeEntity.Clazz, Visibility.Packaged);
        Set<IMethod> listMethodTest = new HashSet<>();


        List<Argument> arguments = new LinkedList<>();

        Argument arg1 = new Argument(Direction.In, new Type("type1"), "agrs1", "init1");
        Argument arg2 = new Argument(Direction.InOut, new Type("type2"), "agrs2", "init2");

        arguments.add(arg1);
        arguments.add(arg2);

        Method m = new Method(Visibility.Packaged, new Type("return"), "Name", arguments, "prop");
        listMethodTest.add(m);
        OE.addMethod(m);

        assertTrue(OE.getListMethod().contains(m));

        assertEquals(OE.getListMethod(),listMethodTest);

        OE.removeMethod(m);
        assertFalse(OE.getListMethod().contains(m));
    }

    @Test
    public void testGetSetPackage() throws Exception {
        IObjectEntity OE = new ObjectEntity("test", TypeEntity.Clazz, Visibility.Packaged);
        Package parent = new Package("Parent");
        IPackage p = new Package("package",parent);
        OE.setPackage(p);

        assertEquals(p,OE.getPackage());
    }

    @Test
    public void testGetAbsoluteName() throws Exception {
        IObjectEntity OE = new ObjectEntity("test", TypeEntity.Clazz, Visibility.Packaged);
        String name = "NameEntity";
        Package parentPackage = new Package("parentPackage");
        IPackage p = new Package("NamePackage",parentPackage);
        OE.setName(name);
        OE.setPackage(p);

        assertEquals(OE.getAbsoluteName(), p.getAbsoluteName()+ "." + name);
    }
}