package fr.univrouen.umlreverse.model.diagram.util;

import fr.univrouen.umlreverse.model.diagram.common.Observable;
import fr.univrouen.umlreverse.util.Contract;

import java.util.HashMap;
import java.util.Map;

/**
 * A basic implementation of IStyleBox
 */
public class StyleBox  extends Observable implements IStyleBox {

    // ATTRIBUTES

    /**
     * The style
     */
    private Map<String, Map<String, IStyle>> styles = new HashMap<>();

    // CONSTRUCTOR

    public StyleBox() {}

    // REQUESTS

    @Override
    public IStyle getStyle(String selector, String id) {
        Contract.check(selector != null);
        Contract.check(id != null);

        Map<String, IStyle> idToStyle = styles.get(selector);
        if (idToStyle != null) {
            IStyle style = idToStyle.get(id);
            if (style != null) {
                return style;
            }
        }
        return new Style();
    }

    // METHODS

    @Override
    public void addStyle(String selector, String id, String key, String value) {
        Contract.check(selector != null);
        Contract.check(key != null);
        Contract.check(value != null);
        Contract.check(id != null);

        Map<String, IStyle> selectId = styles.get(selector);
        if (selectId == null) {
            selectId = new HashMap<>();
            styles.put(selector, selectId);
        }

        IStyle style = selectId.get(id);
        if (style == null) {
            style = new Style();
            selectId.put(id, style);
        }

        style.add(key, value);
        firePropertyChange("StyleChanged", styles.get(selector), styles.get(selector) );
    }

    @Override
    public void addStyle(String selector, String id, IStyle style) {
        Contract.check(selector != null);
        Contract.check(style != null);
        Contract.check(id != null);

        Map<String, IStyle> selectId = styles.get(selector);
        if (selectId == null) {
            selectId = new HashMap<>();
            styles.put(selector, selectId);
        }
        selectId.put(id, style);
        firePropertyChange("StyleChanged", styles.get(selector), styles.get(selector) );
    }

    @Override
    public void removeStyle(String selector, String id, String key) {
        Contract.check(selector != null);
        Contract.check(key != null);
        Contract.check(id != null);

        Map<String, IStyle> selectMapById = styles.get(selector);
        if (selectMapById != null) {
            IStyle style = selectMapById.get(id);
            if (style != null) {
                String s  = style.getValue(key);
                style.remove(key);
                firePropertyChange("StyleChanged", s, null );

            }
        }

    }

    @Override
    public void removeStyle(String selector, String id) {
        Contract.check(selector != null);
        Contract.check(id != null);

        Map<String, IStyle> styleById = styles.get(selector);
        if (styleById != null) {
            IStyle st = styleById.get(id);
            styleById.remove(id);
            firePropertyChange("StyleChanged", st, null );

        }
    }

    @Override
    public void setStyle(IStyleBox styles) {
        this.styles.clear();
        this.styles.putAll(styles.toMap());
    }

    @Override
    public Map<String, Map<String, IStyle>> toMap() {
        return styles;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : styles.keySet()) {
            sb.append(s + " :\n");
            sb.append("\t" + styles.get(s) + "\n");
        } 
        return sb.toString();
    }
}
