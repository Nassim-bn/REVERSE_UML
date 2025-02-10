package fr.univrouen.umlreverse.model.diagram.clazz.view;

import java.io.Serializable;
import java.util.Set;
import fr.univrouen.umlreverse.model.diagram.clazz.visitor.IClassVisitor;
import fr.univrouen.umlreverse.model.diagram.common.IObservable;
import fr.univrouen.umlreverse.model.diagram.common.IStylizable;
import fr.univrouen.umlreverse.model.util.RefusedAction;

/**
 * A view on a package
 */
public interface IViewPackage extends Serializable, IStylizable, IObservable, IViewEntity {

	String PACKAGE_STYLE_ID = "package";

	String NAME_CHANGED_PROPERTY_NAME = "NameChanged";

	String PACKAGE_ADDED_PROPERTY_NAME = "PackageAdded";
	String PACKAGE_REMOVED_PROPERTY_NAME = "PackageRemoved";

    String PARENT_CHANGED_PROPERTY_NAME = "ParentChanged";

	String STYLE_CHANGED_PROPERTY_NAME = "StyleChanged";

	String ENTITY_ADDED_TO_PACKAGE_PROPERTY_CHANGED = "EntityAdded";
	String ENTITY_REMOVED_TO_PACKAGE_PROPERTY_CHANGED = "EntityRemoved";

	String PARAMETER_CHANGED_PROPERTY_NAME = "ParameterChanged";

    /**
     * The diagram
     */
    IClassDiagram getDiagram();

    /**
     * getStyleId Getter that permits to give the id of the style
     * @return String
     */
    String getStyleId();

    /**
     * getter of name
     * @return the name
     */
    String getName();

    /**
     * getter of names of parameters
     * @return parameter's name
     */
	String getNameParameter();

    /**
     * The parent folder
     */
    IViewPackage getPackage();

    /**
     * The specific id of package for packages style ids
     */
	String getId();

    /**
     * The absolute name
     */
    String getAbsoluteName();

    /**
     * getter of packages
     * @return the packages
     */
    Set<IViewPackage> getPackages();

    /**
     * getter of all the entities
     * @return the listEntities
     */
    Set<IViewEntity> getEntities();

    /**
     * getter of all the parameters
     * @return the parameters (name of entities)
     */
	Set<String> getParameters();

    // METHODS


    /**
     * addEntity allows to add entities
     * @param entity
     * 		the entity to add
     */
    void addEntity(IViewEntity entity);

    /**
     * removeEntity allows to remove entities
     * @param entity
     * 		the entity to remove
     */
    void removeEntity(IViewEntity entity);

    /**
     * addParameter allows to add parameter of a package
     * @param parameter
     * 		the entity to remove
     */
	void addParameter(String parameter);

	/**
     * removeParameter allows to remove parameter of a package
     * @param parameter
     * 		the entity to remove
     */
	void removeParameter(String parameter);

    /**
     * setter of parent
     */
    void setPackage(IViewPackage parent);

    /**
     * setter of name
     * @param namePackage the name to set
     * @throws RefusedAction 
     */
    void setName(String namePackage) throws RefusedAction;

    /**
     * setter of parameters
     * @param string of the name parameters
     */
    void setParameterName(String name);

    /**
     * visitor
     * @param visitor
     */
    void accept(IClassVisitor visitor);
}
