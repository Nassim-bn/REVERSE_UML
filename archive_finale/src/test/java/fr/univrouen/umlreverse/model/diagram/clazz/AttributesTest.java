package fr.univrouen.umlreverse.model.diagram.clazz;

import fr.univrouen.umlreverse.model.project.Attribute;
import fr.univrouen.umlreverse.model.project.Type;
import fr.univrouen.umlreverse.model.project.Variable;
import fr.univrouen.umlreverse.model.project.Visibility;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by BELKHOUS on 02/03/2016.
 *
 * Class of test for Attribute Class
 */
public class AttributesTest {

    @Test
    public void testCreatAttributWithAllParamsTrue() throws Exception {

        String property = "p";
        Visibility v = Visibility.Public;
        Variable var = new Variable(new Type("type"), "name");

        //When
        Attribute a = new Attribute(v,property,var);

        //Then
        assertEquals(Visibility.Public, a.getVisibility());
        assertEquals("p",a.getProperty());
        assertEquals(var,a.getVariable());
    }

    @Test
    public void testSettersAttributeTrue() throws Exception {
        Variable var = new Variable(new Type("a"), "b");
        Variable var2 = new Variable(new Type("b"), "b");
        //When
        Attribute a = new Attribute(Visibility.Public,"p",var);

        a.setVisibility(Visibility.Protected);
        a.setVariable(var2);
        a.setProperty("test");

        //Then
        assertEquals(Visibility.Protected, a.getVisibility());
        assertEquals("test",a.getProperty());
        assertEquals(var2,a.getVariable());
    }




    @Test
    public void testFireVetoableChangeTestListenerWhenVisibilityIsChanged() throws Exception {

        Variable var = new Variable(new Type("type"), "name");
        //When
        Attribute a = new Attribute(Visibility.Public,"p",var);

        a.addVetoableChangeListeners(evt -> {
            if (evt.getPropertyName().equals("VisibilityChanged")) {
                throw new IllegalArgumentException("");
            }
        });

        try {
            a.setVisibility(Visibility.Packaged);

        } catch (IllegalArgumentException ignored){

        }
    }







}
