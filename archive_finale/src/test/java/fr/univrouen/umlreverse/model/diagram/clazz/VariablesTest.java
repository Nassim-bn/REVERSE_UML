package fr.univrouen.umlreverse.model.diagram.clazz;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.univrouen.umlreverse.model.project.Type;
import fr.univrouen.umlreverse.model.project.Variable;

/**
 * Created by BELKHOUS on 02/03/2016.
 */
public class VariablesTest {

    @Test
    public void testConstructorThreeArguments () throws Exception {
        //when
        Type t = new Type("Type", true);

        //then
        assertEquals("Type", t.getType());
        assertEquals(true, t.isConstant());
        assertEquals(0, t.getGenericArguments().size());
    }

    @Test
    public void testGettersTrue () throws Exception {
        //given
        Type t = new Type("Type");
        String name = "nom";
        String initialization = "init";

        //when
        Variable var = new Variable(t, name, initialization);

        //then
        assertEquals(t, var.getType());
        assertEquals("nom", var.getName());
        assertEquals("init", var.getInitialization());
    }

    @Test
    public void testSettersTrue () throws Exception {
        //given
        Type t = new Type("Type");
        String name = "nom";
        String initialization = "init";
        Variable var = new Variable(t, name, initialization);

        Type newType = new Type("newType");
        //when
        var.setInitialization("initialisation");
        var.setName("newName");
        var.setType(newType);

        //then
        assertEquals(newType, var.getType());
        assertEquals("newName", var.getName());
        assertEquals("initialisation", var.getInitialization());
    }

    @Test
    public void testFireVetoableChangeTestListenerWhenInitializationIsChanged() throws Exception {
        //given
        Type t = new Type("Type");
        String name = "nom";
        String initialization = "init";
        Variable var = new Variable(t, name, initialization);

        var.addVetoableChangeListener(evt -> {
            if (evt.getPropertyName().equals("InitChanged")) {
                throw new IllegalArgumentException("");
            }
        });

        try {
            //when
            var.setInitialization("newInitialization");
            //then
            assertEquals("newInitialization", var.getInitialization());
        } catch (IllegalArgumentException e){

        }
    }

    @Test
    public void testFireVetoableChangeTestListenerWhenTypeIsChanged() throws Exception {
        //given
        Type t = new Type("Type");
        String name = "nom";
        String initialization = "init";
        Variable var = new Variable(t, name, initialization);

        var.addVetoableChangeListener(evt -> {
            if (evt.getPropertyName().equals("TypeChanged")) {
                throw new IllegalArgumentException("");
            }
        });

        try {

            Type newType = new Type("newType");
            //when
            var.setType(newType);
            //then
            assertEquals(newType, var.getType());
        } catch (IllegalArgumentException e){

        }
    }

    @Test
    public void testFireVetoableChangeTestListenerWhenNameIsChanged() throws Exception {
        //given
        Type t = new Type("Type");
        String name = "nom";
        String initialization = "init";
        Variable var = new Variable(t, name, initialization);

        var.addVetoableChangeListener(evt -> {
            if (evt.getPropertyName().equals("NameChanged")) {
                throw new IllegalArgumentException("");
            }
        });

        try {
            //When
            var.setName("newName");
            //then
            assertEquals("newType", var.getType().toString());
        } catch (IllegalArgumentException ignored){

        }
    }

    @Test
    public void equals() throws Exception {
        Variable v1 = new Variable(new Type("a"), "name", "2");
        Variable v2 = new Variable(new Type("a"), "name", "2");
        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
    }

}

