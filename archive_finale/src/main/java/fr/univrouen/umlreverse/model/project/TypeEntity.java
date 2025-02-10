package fr.univrouen.umlreverse.model.project;


import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration to define the possible type of an ObjectEntity.
 */
public enum TypeEntity {

    Clazz("Class", "Classe",  "#82B9FF" ),
    Interface("Interface", "Interface", "0xEE82EE"),
    Enumeration("Enum", "Énumération", "0xFF0000"),
    Abstract("Abstract class", "Classe abstraite", "0x00FFFF"),
    Packag("Paquetage", "Paquetage", "0xFFFFFF"),
    ;

    public static final String CLASS_ABSTRACTION_ID = "Class";
    public static final String INTERFACE_ABSTRACTION_ID = "Interface";
    public static final String ENUM_ABSTRACTION_ID = "Enum";
    public static final String ABSTRACT_ABSTRACTION_ID = "Abstract class";
    public static final String PACKAGE_ABSTRACTION_ID = "Paquetage";

    /**
     * the toString() associated.
     */
    private String abstraction;

    /**
     * the getViewName() associated.
     */
    private String viewName;

    /**
     * the color of the uml circle.
     */
    private String color;
    /**
     * The link from the abstraction to the TypeEntity
     */
    private static Map<String, TypeEntity> abstractionToTypeEntity= new HashMap<>();
    static {
        for (TypeEntity t : TypeEntity.values()) {
            abstractionToTypeEntity.put(t.toString(), t);
        }
    }

    /**
     * a TypeEntity.
     * @param abstraction
     *      abstraction != null
     *      t.toString().equals(toString()) => (t == this)
     * @param viewName
     *      viewname != null
     */
    TypeEntity(String abstraction, String viewName, String color) {
        this.abstraction = abstraction;
        this.viewName = viewName;
        this.color = color;
    }

    /**
     * Abstraction function.
     * @return
     *      the string associated
     */
    public String toString() {
        return abstraction;
    }

    /**
     * View's name of this type.
     * @return
     *      the string corresponding to a name for the view.
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * The typeEntity associated to s
     * @param s
     *      The string
     * @return
     *      The typeEntity associated to s, if it doesn't exist return null
     */
    public static TypeEntity getTypeEntity(String s) {
        return abstractionToTypeEntity.get(s);
    }


    public String getColor() {
        return color;
        }
}
