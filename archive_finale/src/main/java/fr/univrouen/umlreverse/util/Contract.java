package fr.univrouen.umlreverse.util;

/**
 * Class to factorize the usual test of the arguments
 */
public class Contract {
    private Contract() {
        // Singleton
    }
    public static void check(boolean test, String message) {
        if (!test) {
            throw new IllegalArgumentException(message);
        }
    }
    public static void check(boolean test) {
        check(test, "Empty Message");
    }

    public static void checkOrFailure(boolean test, String message) {
        if (!test) {
            throw new AssertionError(message);
        }
    }
    public static void checkOrFailure(boolean test) {
        checkOrFailure(test, "Empty Message");
    }
}
