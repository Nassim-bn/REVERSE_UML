package fr.univrouen.umlreverse.model.project;

import java.beans.VetoableChangeListener;
import java.io.Serializable;
import java.util.Set;

/**
 * A super class for the Attribute and Methods
 */
public interface IField extends Serializable {

    // REQUESTS

    /**
     * The vetoable listeners
     */
    VetoableChangeListener[] getVetoableChangeListeners();

    // METHODS


    /**
     * The modifiers.
     */
    Set<Modifier> getModifier();

    /**
     * Add a new vetoable listener
     * @param listener
     *      the listener
     */
    void addVetoableChangeListeners(VetoableChangeListener listener);

    /**
     * Remove a vetoable listener
     * @param listener
     *      the listener
     */
    void removeVetoableChangeListeners(VetoableChangeListener listener);

    /**
     * Add a modifier
     * @pre
     *      modifier != null
     */
    void addModifier(Modifier modifier);

    /**
     * Remove a modifier
     * @pre
     *      modifier != null
     */
    void removeModifier(Modifier modifier);

    /**
     * remove all modifiers
     */
    void clearModifier();
}
