package fr.univrouen.umlreverse.model.diagram.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.diagram.util.Style;


public class StyleTest {

    private IStyle st;

    @Before
    public void setUp() {
        st = new Style();
    }

    @Test
    public void testGetValue() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        assertThat(st).isEqualToComparingFieldByField(new Style());
    }

    @Test
    public void testToMap() throws Exception {
        //GIVEN
        String key = "key";
        String value = "value";
        st.add(key, value);
        //WHEN
        Map<String, String> ma = st.toMap();
        //THEN
        assertThat(ma.size()).isEqualTo(1);
        assertThat(ma.get(key)).isEqualTo(value);
    }

    @Test
    public void testAdd() throws Exception {
        //GIVEN
        String key = "key";
        String value = "value";
        //WHEN
        st.add(key, value);
        //THEN
        assertThat(st.getValue(key)).isEqualTo(value);
    }

    @Test
    public void testRemove() throws Exception {
        //GIVEN
        String key = "key";
        String value = "value";
        st.add(key, value);
        //WHEN
        st.remove(key);
        //THEN
        assertThat(st.getValue(key)).isNull();
    }

    @Test
    public void testPutAll() throws Exception {
        //GIVEN
        String key = "key";
        String value = "value";
        IStyle sty = new Style();
        sty.add(key, value);
        st.add(value, key);
        //WHEN
        st.putAll(sty);
        //THEN
        assertThat(st.getValue(key)).isEqualTo(value);
        assertThat(st.getValue(value)).isEqualTo(key);
    }

    @Test
    public void testToString() throws Exception {
        //GIVEN
        String key = "key";
        String value = "value";
        st.add(key, value);
        //WHEN
        String s = st.toString();
        //THEN
        assertThat(s).isEqualTo("[key : value]");
    }
}