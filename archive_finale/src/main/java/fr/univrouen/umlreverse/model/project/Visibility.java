package fr.univrouen.umlreverse.model.project;


import java.util.HashMap;
import java.util.Map;

/**
 * Class defining all the visibility for methods, attributes and class.
 */
public enum Visibility {
    Public("public", '+'),
    Protected("protected", '#'),
    Packaged("default", '~'),
    Private("private", '-');

    /**
     * The String associated.
     */
    private String abstraction;

    /**
     * the symbol associated.
     */
    private char symbol;

    /**
     * Association of the symbol to the visibility.
     */
    private static Map<Character, Visibility> symbolToVisibility = new HashMap<>();
    static {
        for (Visibility v : Visibility.values()) {
            symbolToVisibility.put(v.getSymbol(), v);
        }
    }

    /**
     * Association of the abstraction to the visibility.
     */
    private static Map<String, Visibility> abstractionToVisibility = new HashMap<>();
    static {
        for (Visibility v : Visibility.values()) {
            abstractionToVisibility.put(v.toString(), v);
        }
    }

    /**
     * A visibility.
     * @param abstraction
     *      abstraction != null
     *      forall v in Visibility.values
     *          if v != this
     *                v.getAbstraction() != getAbstraction()
     *          end if
     *      end forall
     * @param symbol
     *      symbol != null
     *      forall v in Visibility.values
     *          if v != this
     *                v.getSymbol() != getSymbol()
     *          end if
     *      end forall
     */
    Visibility(String abstraction, char symbol) {
        this.abstraction = abstraction;
        this.symbol = symbol;
    }

    /**
     * The symbol associated
     * @return
     *      the symbol associated to "this"
     *      getSymbol() != null
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * The abstraction of this.
     * @return
     *      the abstraction string associated to the visibility
     *      toString() != null
     */
    @Override
    public String toString() {
        return abstraction;
    }

    /**
     * the visibility associated to a symbol.
     * @param symbol
     *      the symbol
     * @return
     *      the visibility associated to the symbol, or null if it doesn't exist.
     */
    public static Visibility getVisibility(char symbol) {
        return symbolToVisibility.get(symbol);
    }

    /**
     * The visibility associated to an abstraction.
     * @param abstraction
     *      the abstraction
     * @return
     *      the visibility associated to the abstraction, or null if it doesn't exist.
     */
    public static Visibility getVisibility(String abstraction) {
        return abstractionToVisibility.get(abstraction);
    }
}
