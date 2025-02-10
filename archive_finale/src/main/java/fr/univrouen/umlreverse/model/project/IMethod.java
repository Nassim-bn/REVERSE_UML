package fr.univrouen.umlreverse.model.project;


import fr.univrouen.umlreverse.model.util.RefusedAction;

import java.util.List;

/**
 * Interface representing a method
 */
public interface IMethod extends IField{

	String NAME_CHANGED_PROPERTY_NAME = "NameChanged";
	String ARGUMENTS_CHANGE_PROPERTY_NAME = "ArgumentsChanged";
	String TYPE_CHANGE_PROPERTY_NAME = "TypeChanged";
	
    // REQUESTS

    /**
     * The return type
     */
    IType getReturn();

    /**
     * The name
     */
    String getName();
    /**
     *  A copy of the list of arguments
     */
    List<Argument> getArguments();

    /**
     * The property
     */
    String getProperty();

    /**
     * The visibility
     */
    Visibility getVisibility();

    // METHODS

    /**
     * Set the visibility.
     * @param visibility
     *      The new visibility
     */
    void setVisibility(Visibility visibility);

    /**
     * Set the property
     * @param property
     *      the new property
     */
    void setProperty(String property);

    /**
     * Set the type of return
     * @param type
     *      the return
     */
    void setReturn(IType type);

    /**
     * Set the name
     * @param name
     *      The new name
     */
    void setName(String name) throws RefusedAction;

    /**
     * Set the arguments
     * @param arguments
     *      The new arguments
     */
    void setArguments(List<Argument> arguments) throws RefusedAction;
}
