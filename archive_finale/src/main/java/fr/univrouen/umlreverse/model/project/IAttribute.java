package fr.univrouen.umlreverse.model.project;

import fr.univrouen.umlreverse.model.util.RefusedAction;

/**
 * Interface representing an Attribute
 */
public interface IAttribute extends IField {

    // REQUESTS

    /**
     * The visibility
     */
    Visibility getVisibility();

    /**
     * The property
     */
    String getProperty();

    /**
     * The variable
     */
    Variable getVariable();

    // METHODS

    /**
     * Set the visibility
     * @param visibility
     *      the new visibility
     */
    void setVisibility(Visibility visibility);

    /**
     * Set the property
     * @param property
     *      the new property
     */
    void setProperty(String property);

    /**
     * Set the variable
     * @param variable
     *      The new variable
     */
    void setVariable(Variable variable) throws RefusedAction;
}
