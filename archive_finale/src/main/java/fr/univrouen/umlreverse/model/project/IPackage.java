package fr.univrouen.umlreverse.model.project;

import fr.univrouen.umlreverse.model.util.RefusedAction;

import java.io.Serializable;
import java.util.Set;

/**
 * the interface to represent the package.
 */
public interface IPackage extends IComponent, Serializable {

	// REQUESTS

	/**
	 * getter of packages
	 * @return the packages
	 */
	Set<IPackage> getPackages();

	Set<IObjectEntity> getAllEntitiesIncluded();

	/**
	 * getter of entities that have a specific type (filter)
	 * @param filtre
	 * 		the filter
	 * @return tmpListEntitiesType
	 */
	Set<IObjectEntity> getEntities(TypeEntity filtre);

	/**
	 * getter of entities that have a specific name
	 * @param filterByName
	 * 		the filter
	 * @return
	 * 		the list of entities
	 */
	Set<IObjectEntity> getEntities(String filterByName);

	/**
	 * getter of all the entities
	 * @return the listEntities
	 */
	Set<IObjectEntity> getListEntities();

	// METHODS

	/**
	 * addPackage allows to add packages
	 * @param _package
	 * 		the package to add
	 */
	void addPackage(IPackage _package) throws RefusedAction;

	/**
	 * removePackage allows to remove packages
	 * @param _package
	 * 		the package to remove
	 */
	void removePackage(IPackage _package);

	/**
	 * addEntity allows to add entities
	 * @param entity
	 * 		the entity to add
	 */
	void addEntity(IObjectEntity entity) throws RefusedAction;

	/**
	 * removeEntity allows to remove entities
	 * @param entity
	 * 		the entity to remove
	 */
	void removeEntity(IObjectEntity entity);
}
