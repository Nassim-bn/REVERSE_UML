package fr.univrouen.umlreverse.model.project;


import fr.univrouen.umlreverse.model.util.RefusedAction;

import java.beans.VetoableChangeListener;
import java.io.Serializable;

/**
 * Class representing a variable.
 * ex of valid variables :
 *      String a = null
 *      String b
 *      int<a,b> c = ""
 */
public interface IVariable extends Cloneable, Serializable {

    // REQUESTS

    /**
     * the name.
     * @return
     *      A string not null representing the name of the variable
     */
    String getName();

    /**
     * The type.
     * @return
     *      The type of the variable
     */
    IType getType();

    /**
     * The initialization
     * @return
     *      The string representing the initizalization of the variable.
     */
    String getInitialization();

    /**
     * The array of the listeners of "this"
     * @return
     *      the VetoableChangeListeners contained by the internal VetoableChangeSupport.
     */
    VetoableChangeListener[] getVetoableChangeListeners();

    // METHODS

    /**
     * Set the initialization.
     * @param init
     *      if (a != null): setInitialization(a) => getInitialization().equals(a)
     *      if (a == null): setInitialization(null) => getInitialization() == null
     */
    void setInitialization(String init);

    /**
     * Set the type of the variable.
     * @param type
     *      type != null
     *      setType(a) => getType().equals(a) && getType() != a
     */
    void setType(IType type) throws RefusedAction;

    /**
     * Set the name of the variable.
     * @param name
     *      name != null
     *      setName(a) => getName().equals(a)
     */
    void setName(String name) throws RefusedAction;

    /**
     * Add a new VetoableChangeListener from "this"
     * => getVetoableChangeListeners.contains(listener)
     * @param listener
     *      the new listener
     *      listener != null
     */
    void addVetoableChangeListener(VetoableChangeListener listener);

    /**
     * Remove a VetoableChangeListener from this
     * => !getVetoableChangeListeners.contains(listener)
     * @param listener
     *      The listener to delete.
     *      listener != null
     */
    void removeVetoableChangeListener(VetoableChangeListener listener);

}
