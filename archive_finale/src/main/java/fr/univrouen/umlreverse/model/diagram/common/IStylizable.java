package fr.univrouen.umlreverse.model.diagram.common;

import java.io.Serializable;
import java.util.Map;

import fr.univrouen.umlreverse.model.diagram.util.IStyle;

/**
 * interface to the class that contain a stylebox
 */
public interface IStylizable extends Serializable {


    /**
     * The style values of this
     */
    IStyle getStyle();

    /**
     * add a style
     * @param key
     *      the key
     * @param value
     *      the value
     */
    void addStyle(String key, String value);

    /**
     * addAllStyle allows to add all the styles
     * @param keyValue
     */
    void addAllStyle(Map<String, String> keyValue);
    /**
     * remove a style
     */
    void removeStyle(String key);

    /**
     * clearStyle allows to give the default style
     */
    void clearStyle();
}
