package fr.univrouen.umlreverse.model.diagram.util;

import java.io.Serializable;
import java.util.Map;

import fr.univrouen.umlreverse.model.diagram.common.IObservable;

/**
 * An interface to the style box.
 * A style box regroup all style with selector pointing on an ensemble of key/value
 */
public interface IStyleBox extends Serializable, IObservable {

    // REQUESTS

    /**
     * The style
     * @param selector
     *      the style depending of the selector
     * @return
     *      the ensemble of key/value style for this selector
     */
    IStyle getStyle(String selector, String id);

    // METHODS

    /**
     * Add an ensemble key/value to the style of selector.
     * @param selector
     *      the selector
     * @param id
     *      the id
     * @param key
     *      the key
     * @param value
     *      the value
     */
    void addStyle(String selector, String id, String key, String value);

    /**
     * set a style to the style of selector.
     * @param selector
     *      the selector
     * @param id
     *      the id
     * @param style
     *      the style to set
     */
    void addStyle(String selector, String id, IStyle style);
    /**
     * Remove a couple key/value from selector.
     * @param selector
     *      the selector
     * @param key
     *      the key
     */
    void removeStyle(String selector, String id, String key);

    /**
     * Remove totally a selector
     * @param selector
     *      the selector
     */
    void removeStyle(String selector, String id);

    /**
     * Set a new style
     * @param styles
     *      the new style
     */
    void setStyle(IStyleBox styles);

    /**
     * Transform a styleBox by a map
     */
    Map<String,Map<String,IStyle>> toMap();
}
