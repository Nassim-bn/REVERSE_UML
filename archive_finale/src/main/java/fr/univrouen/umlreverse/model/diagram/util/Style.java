package fr.univrouen.umlreverse.model.diagram.util;

import fr.univrouen.umlreverse.util.Contract;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic implementation of IStyle
 */
public class Style implements IStyle  {

    private Map<String, String> values = new HashMap<>();

    @Override
    public String getValue(String key) {
        if (key == null) {
            return null;
        }
        return values.get(key);
    }

    @Override
    public Map<String, String> toMap() {
        return new HashMap<>(values);
    }

    @Override
    public void add(String key, String value) {
        Contract.check(key != null);
        values.put(key, value);
    }

    @Override
    public void remove(String key) {
        if (key != null)
            values.remove(key);
    }

    @Override
    public void putAll(IStyle style) {
        values.putAll(style.toMap());
    }

    @Override
    public String toString() {
        String result = ("[");
        for (String key : values.keySet()) {
            result += (key + " : " + values.get(key));
        }
        result += ("]");
        return result;
    }
}
