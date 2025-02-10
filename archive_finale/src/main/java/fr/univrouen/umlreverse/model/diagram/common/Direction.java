package fr.univrouen.umlreverse.model.diagram.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration to indicate the direction of an argument.
 */
public enum Direction {
    In("In"),
    Out("Out"),
    InOut("InOut"),
    None("");

    /**
     * String associated.
     */
    private String abstraction;

    /**
     * The direction associated to a String
     */
    static Map<String, Direction> stringToDirection;
    static {
        stringToDirection = new HashMap<>();
        for (Direction d : Direction.values()) {
            stringToDirection.put(d.toString(), d);
        }
    }

    /**
     * The String associated
     * @param abstraction
     *      abstraction != null
     */
    Direction(String abstraction) {
        this.abstraction = abstraction;
    }

    /**
     * The abstraction of the direction
     * @return
     *      the string associated
     */
    @Override
    public String toString() {
        return abstraction;
    }

    /**
     * The direction associated to an abstraction.
     * @param abstraction
     *      The abstraction
     * @return
     *      The Direction associated to abstraction, or null if it doesn't exist.
     */
    public static Direction getDirection(String abstraction) {
        return stringToDirection.get(abstraction);
    }
}
