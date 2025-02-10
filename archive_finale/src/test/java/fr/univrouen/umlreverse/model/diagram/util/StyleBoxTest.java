package fr.univrouen.umlreverse.model.diagram.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fr.univrouen.umlreverse.model.diagram.util.IStyleBox;
import fr.univrouen.umlreverse.model.diagram.util.Style;
import fr.univrouen.umlreverse.model.diagram.util.StyleBox;


public class StyleBoxTest {
    private IStyleBox styles;
    @Before public void init() {
        styles = new StyleBox();
    }


    @Test
    public void testAdd4args() {
        //GIVEN
        //WHEN
        styles.addStyle("selector", "id", "a", "b");
        //THEN
        assertEquals("b", styles.getStyle("selector", "id").getValue("a"));
    }

    @Test
    public void testAdd3args() {
        //GIVEN
        //WHEN
        styles.addStyle("selector", "id", new Style());
        //THEN
        assertThat(styles.getStyle("selector","id").toMap()).isEmpty();
    }

    @Test
    public void testRemoveStyle3args() {
        //GIVEN
        styles.addStyle("selector", "id", "a", "b");
        //WHEN
        styles.removeStyle("selector", "id", "a");
        //THEN
        assertThat(styles.getStyle("selector", "id").toMap()).isEmpty();
    }

    @Test
    public void testRemoveStyle2args() {
        //GIVEN
        styles.addStyle("selector", "id", "a", "b");
        //WHEN
        styles.removeStyle("selector", "id");
        //THEN
        assertThat(styles.getStyle("selector","id").toMap()).isEmpty();
    }
}