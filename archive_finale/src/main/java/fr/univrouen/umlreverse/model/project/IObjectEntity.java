
package fr.univrouen.umlreverse.model.project;

import java.beans.PropertyChangeListener;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.util.RefusedAction;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * IObjectEntity interface represents classes, enumerations, interfaces & abstract classes
 */
public interface IObjectEntity extends IComponent, Serializable {

	// REQUESTS
	PropertyChangeListener[] getPropertyChangeListeners();

	/**
	 * getter of abstractText
	 * @return the list of abstract string
	 */
	List<String> getAbstractTexts();

	/**
	 * getter of enumField
	 * @return the list of enum field
	 */
	List<String> getEnumFields();

	/**
	 * getter of type
	 * @return the type
	 */
	TypeEntity getType();
	
	/**
	 * getter of visibility
	 * @return the visibility
	 */
	Visibility getVisibility();

	/**
	 * getter of listAttribute
	 * @return the listAttribute
	 */
	Set<IAttribute> getListAttribute();

	/**
	 * getter of listViewer
	 * @return the listViewer
	 */
	Set<IViewEntity> getListViewer();

	/**
	 * getter of listMethod
	 * @return the listMethod
	 */
	Set<IMethod> getListMethod();

	// METHODS

	/**
	 * setter of type
	 * @param type the type to set
	 */
	void setType(TypeEntity type);

	/**
	 * add an enumerate field.
	 * @param field
	 * 		field != null && field.length() > 0
	 * 		the new enumerate field
     */
	void addEnumField(String field);

	/**
	 * remove an enumerate field.
	 * @param field
	 * 		the old enumerate field
	 */
	void removeEnumField(String field);

	/**
	 * add a abstractTextfield
	 */
	void addAbstractText(String text);

	/**
	 * remove a abstractTextfield
	 */
	void removeAbstractText(String text);

	/**
	 * setter of visibility
	 * @param visibility the visibility to set
	 */
	void setVisibility(Visibility visibility);
	
	/**
	 * addAttribute allows to add attributs
	 * @param attribute
	 * 		the attribute to add
	 */
	void addAttribute(IAttribute attribute) throws RefusedAction;
	
	/**
	 * removeAttribute allows to remove attributs
	 * @param attribute
	 * 		the attribute to remove
	 */
	void removeAttribute(IAttribute attribute);
	
	/**
	 * addMethod allows to add methodes
	 * @param method
	 * 		the method to add
	 */
	void addMethod(IMethod method) throws RefusedAction;
	
	/**
	 * removeMethod allows to remove methodes
	 * @param method
	 * 		the method to remove
	 */
	void removeMethod(IMethod method);
	
	/**
	 * addViewer allows to add viewer
	 * @param view
	 * 		the view to add
	 */
	void addViewer(IViewEntity view);
	
	/**
	 * removeViewer allows to remove
	 * @param view
	 * 		the view to remove
	 */
	void removeViewer(IViewEntity view);

	/**
	 * add a propertyChangeListenner to the IObjectEntity to a specific type of property
	 * @param name the name of the property
	 * @param l the change Listenner
	 */
	void addPropertyChangeListener(String name, PropertyChangeListener l);
	/**
	 * remove a propertyChangeListenner to the IObjectEntity to a specific type of property
	 * @param name the name of the property
	 * @param l the change Listenner
	 */
	void removePropertyChangeListener(String name, PropertyChangeListener l);
	/**
	 * add a PropertyChangeListenner to the IObjectEntity
	 * @param l the change Listenner to add
	 */
	void addPropertyChangeListener(PropertyChangeListener l);

	/**
	 * remove a PropertyChangeListenner to the IObjectEntity
	 * @param l the change Listenner to remove
	 */
	void removePropertyChangeListener(PropertyChangeListener l);
}
