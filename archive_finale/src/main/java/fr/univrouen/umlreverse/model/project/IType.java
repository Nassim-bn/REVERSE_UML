package fr.univrouen.umlreverse.model.project;

import fr.univrouen.umlreverse.model.util.RefusedAction;

import java.beans.VetoableChangeListener;
import java.io.Serializable;
import java.util.List;

/**
 * IType is an interface defining the types.
 * The type can be a constant type, a generic type or a standard type.
 */
public interface IType extends Serializable {
	
    // REQUESTS

    /**
     * If "this" is a constant type.
     * @return
     *      True if this is a constant type, False in another case.
     */
    boolean isConstant();

    /**
     * The generic arguments.
     * @return
     *      A copy of the generic arguments of "this"
     */
    List<IType> getGenericArguments();

    /**
     * If "this" is a generic type.
     * @return
     *      getGenericArguments().size() > 0
     */
    boolean isGeneric();

    /**
     * The type without the const and the generic arguments.
     * @return
     *      the type without the other attributes of "this"
     */
    String getType();

    /**
     * The array of the listeners of "this"
     * @return
     *      the VetoableChangeListeners contained by the internal VetoableChangeSupport.
     */
    VetoableChangeListener[] getVetoableChangeListeners();

    // METHODS

    /**
     * Set the list of the generic arguments of "this"
     * Replace the arguments from the old list by a copy of arguments contained in arguments
     * @param arguments
     *      The new generic arguments
     *      arguments != null
     */
    void setGenericArguments(List<IType> arguments) throws RefusedAction;

    /**
     * Set "this" as a constant type or not, depending of isConst
     * @param isConst
     *      Define if "this" is generic.
     */
    void setConstant(boolean isConst);

    /**
     * Set the basic type of "this"
     * @param type
     *      type != null
     *      type.length > 0
     */
    void setType(String type) throws RefusedAction;

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
