
package fr.univrouen.umlreverse.model.project;


import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.util.RefusedAction;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

/**
 * IProject interface represents the project
 */
public interface IProject extends Serializable {

    // REQUESTS

    /**
     * Add a diagram.
     */
    void addDiagram(IDiagram diagram) throws RefusedAction;
    
    /**
     * add an entity in project
     */
    void addEntity(IObjectEntity entity) throws RefusedAction;
    
    /**
     * Add a package in project
     */
    void addPackage(IPackage _package) throws RefusedAction;

    void addPropertyChangeListener(String name, PropertyChangeListener listener);

    /**
     * addRelation allows to add relations
     * @param relation
     *      the relation to add
     */
    void addRelation(IRelation relation);
    /**
     * Test if IProject contains an entity
     */
    boolean contains(IObjectEntity entity);
    /**
     * Test if IProject contains a package
     */
    boolean contains(IPackage _package);
    /**
     * Test if IProject contains a relation
     */
    boolean contains(IRelation relation);

    IDiagram getDiagramByName(String name);

    /**
     * return a list of all entities that the project contains
     * @return allEntities
     */
    Set<IObjectEntity> getAllEntities();

    /**
     * getter of listDiagrams
     * @return the listDiagrams
     */
    Set<IDiagram> getDiagrams();

    // METHODS

    String getName();

    PropertyChangeListener[] getPropertyChangeListeners();

    /**
     * getter of listRelations
     * @return the listRelations
     */
    Set<IRelation> getRelations();

    /**
     * Must have more details
     *
     * @return the root package
     */
    IPackage getRootPackage();

    /**
     * removeDiagram allows to remove diagrams
     * @param diagram
     *      the diagram to remove
     */
    void removeDiagram(IDiagram diagram);

    /**
     * Remove an entity.
     */
    void removeEntity(IObjectEntity entity);

    /**
     * Remove a package.
     */
    void removePackage(IPackage aPackage);

    void removePropertyChangeListener(String name, PropertyChangeListener listener);
    /**
     * removeRelation allows to remove relations
     * @param relation
     *      the relation to remove
     */
    void removeRelation(IRelation relation);

    /**
     * save the project in the file "file"
     */
    void serialize(File file) throws IOException;

    void setName(String name);
}
