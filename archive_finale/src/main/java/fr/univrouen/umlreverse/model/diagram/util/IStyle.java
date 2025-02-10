package fr.univrouen.umlreverse.model.diagram.util;

import java.io.Serializable;
import java.util.Map;

/**
 * A style is a set of string couple : key/value.
 */
public interface IStyle extends Serializable  {
    /**
     * The value for key.
     * @param key
     *      the key
     * @return
     *      the value
     */
    String getValue(String key);

    /**
     * Transform a style into a map.
     * @return
     *     a map with all key/value
     */
    Map<String, String> toMap();

    /**
     * Add a couple key/value
     */
    void add(String key, String value);

    /**
     * Remove the key.
     */
    void remove(String key);

    /**
     * Added or replace with all key/value in style
     */
    void putAll(IStyle style);
}
