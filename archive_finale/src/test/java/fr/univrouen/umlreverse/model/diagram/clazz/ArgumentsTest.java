package fr.univrouen.umlreverse.model.diagram.clazz;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fr.univrouen.umlreverse.model.project.Argument;
import fr.univrouen.umlreverse.model.diagram.common.Direction;
import fr.univrouen.umlreverse.model.project.IType;
import fr.univrouen.umlreverse.model.project.Type;

/**
 * Created by BELKHOUS on 06/03/2016.
 */
public class ArgumentsTest {

    private Direction direction;
    private IType type;
    private String name;
    private String initialization;
    private Argument arg;

    @Before
    public void setUp() throws Exception {
        direction = Direction.In;
        type = new Type("type");
        name = "agrs";
        initialization = "init";
        arg = new Argument(direction,type,name,initialization);
    }

    @Test
    public void testArgumentsConstructerWithGetter () throws Exception {
        //when
        Argument arg = new Argument(direction,type,name,initialization);
        //then
        assertEquals(direction, arg.getDirection());
        assertEquals(type, arg.getType());
        assertEquals(name, arg.getName());
        assertEquals(initialization, arg.getInitialization());
    }

    @Test
    public void testSetters () throws Exception {
        //Given
        IType newType = new Type("newType");
        //when
        arg.setDirection(Direction.InOut);
        arg.setInitialization("newInit");
        arg.setName("newName");
        arg.setType(newType);

        //then
        assertEquals(Direction.InOut, arg.getDirection());
        assertEquals(newType, arg.getType());
        assertEquals("newName", arg.getName());
        assertEquals("newInit", arg.getInitialization());
    }

    @Test
    public void testFireVetoableChangeTestListenerWhenDirectionIsChanged() throws Exception {

        arg.addVetoableChangeListener(evt -> {
            if (evt.getPropertyName().equals("DirectionChanged")) {
                throw new IllegalArgumentException("");
            }
        });

        try {
            //When
            arg.setDirection(Direction.InOut);
            //then
            assertEquals(Direction.InOut, arg.getDirection());
        } catch (IllegalArgumentException ignored){

        }
    }

    @Test
    public void testEquals() throws Exception {
        Argument v1 = new Argument(Direction.In, new Type("a"), "name", "2");
        Argument v2 = new Argument(Direction.In, new Type("a"), "name", "2");
        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
    }

}
