package fr.univrouen.umlreverse.model.util;

/**
 * Exception for a refused action in the model.
 */
public class RefusedAction extends Exception {
    public RefusedAction(ErrorAbstraction error) {
        super(error.getCode());
    }
}
