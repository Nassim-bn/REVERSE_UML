
package fr.univrouen.umlreverse.model.project;

import fr.univrouen.umlreverse.model.util.RefusedAction;

import java.io.Serializable;

/**
 * IComponent interface is the interface of components
 * (Composite Pattern : Package, IObjectEntity)
 */
public interface IComponent extends Serializable {

	// REQUESTS

	/**
	 * getter of name
	 * @return the name
	 */
	String getName();

	/**
	 * The parent folder
	 */
	IPackage getPackage();

	/**
	 * The absolute name
	 */
	String getAbsoluteName();

	// METHODS

	/**
	 * setter of parent
	 */
	void setPackage(IPackage parent) throws RefusedAction;

	/**
	 * setter of name
	 * @param namePackage the name to set
	 */
	void setName(String namePackage) throws RefusedAction;
}
