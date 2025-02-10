package fr.univrouen.umlreverse.model.diagram.clazz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.univrouen.umlreverse.model.diagram.common.Direction;
import fr.univrouen.umlreverse.model.project.Argument;
import fr.univrouen.umlreverse.model.project.IType;
import fr.univrouen.umlreverse.model.project.Method;
import fr.univrouen.umlreverse.model.project.Type;
import fr.univrouen.umlreverse.model.project.Visibility;

/**
 * Test for the class Method
 */
public class MethodsTest {


    private Method m;
    private Argument arg1,arg2;


    @Before
    public void setUp() throws Exception {
        arg1 = new Argument(Direction.In, new Type("typeArgInit1"), "initArg1", "init1");
        arg2 = new Argument(Direction.InOut, new Type("typeArgInit2"), "initArg2", "init2");
        LinkedList<Argument> arguments = new LinkedList<>();
        m = new Method(Visibility.Private, new Type("typeInit"), "nameInit", arguments);
    }

    @Test
    public void testGetterAndSetterNameTrue() throws Exception {
        //given
        String name = "Name";
        //when
        m.setName(name);
        //then
        assertEquals(name, m.getName());
    }

    @Test
    public void testGetterAndSetterArgumentsTrue() throws Exception {
        //given

        List<Argument> argumentsTest = new LinkedList<>();
        argumentsTest.add(arg1);
        argumentsTest.add(arg2);
        //when
        m.setArguments(argumentsTest);
        //then
        assertTrue(m.getArguments().contains(arg1));
        assertTrue(m.getArguments().contains(arg2));
    }

    @Test
    public void testGetterAndSetterPropertyTrue() throws Exception {
        //given

        String property = "prop";
        //when
        m.setProperty(property);
        //then
        assertEquals(property, m.getProperty());
    }

    @Test
    public void testGetterAndSetterReturnTypeTrue() throws Exception {
        //given
        IType returnType = new Type("return");
        //when
        m.setReturn(returnType);
        //then
        assertEquals(returnType, m.getReturn());
    }

    @Test
    public void testGetterAndSetterVisibilityTrue() throws Exception {
        //given
        Visibility visibility = Visibility.Packaged;
        //when
        m.setVisibility(visibility);
        //then
        assertEquals(visibility, m.getVisibility());
    }
}