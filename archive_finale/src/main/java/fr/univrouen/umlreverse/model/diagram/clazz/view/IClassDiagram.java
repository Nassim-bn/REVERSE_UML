package fr.univrouen.umlreverse.model.diagram.clazz.view;

import fr.univrouen.umlreverse.model.diagram.clazz.visitor.IClassVisitor;
import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.diagram.visitor.IDiagramVisitor;
import fr.univrouen.umlreverse.model.util.RefusedAction;

import java.util.Set;

import static fr.univrouen.umlreverse.model.project.TypeEntity.*;

/**
 * A class diagram.
 */
public interface IClassDiagram extends IDiagram {
	// ATTRIBUES
	String ENTITY_STYLE_ID = "entity";
    String RELATION_STYLE_ID = "relation";
    String CLASS_STYLE_ID = CLASS_ABSTRACTION_ID;
    String PACKAGE_STYLE_ID = "PACKAGE_ABSTARCTION_ID";
    String INTERFACE_STYLE_ID = INTERFACE_ABSTRACTION_ID;
    String ENUM_STYLE_ID = ENUM_ABSTRACTION_ID;
    String ABSTRACT_STYLE_ID = ABSTRACT_ABSTRACTION_ID;

    // REQUESTS

    /**
     * The notes.
     * @return
     *      getNotes() != null
     *      A set containing all notes from the class diagram.
     */
    Set<INoteClass> getNotes();

    /**
     * The entities.
     * @return
     *      getEntities() != null
     *      A set containing all entities from the class diagram
     */
    Set<IViewEntity> getEntities();

    /**
     * The packages.
     * @return
     *      getPackages() != null
     *      A set containing all packages from the class diagram
     */
    Set<IViewPackage> getPackages();

    /**
     * The relations.
     * @return
     *      getRelations() != null
     *      A set containing all relations from the class diagram
     */
    Set<IViewRelation> getRelations();
    boolean containsIdNote(String id);

    // METHODS

    /**
     * Add a note to the diagram.
     * @pre
     *      note != null
     */
    void addNote(INoteClass note) throws RefusedAction;

    /**
     * Remove a note to the diagram.
     * @pre
     *      note != null
     */
    void removeNote(INoteClass note);

    /**
     * Add an entity to the diagram.
     * @pre
     *      entity != null
     */
    void addEntity(IViewEntity entity) throws RefusedAction;

    /**
     * Remove an entity to the diagram.
     * @pre
     *      entity != null
     */
    void removeEntity(IViewEntity entity);

    /**
     * Hide an entity
     */
    void hideEntity(IViewEntity entity);
    /**
     * Add a package to the diagram.
     * @pre
     *      _package != null
     */
    void addPackage(IViewPackage _package);

    /**
     * Remove a package to the diagram.
     * @pre
     *      _package != null
     */
    void removePackage(IViewPackage _package);

    /**
     * Add a relation to the diagram.
     * @pre
     *      relation != null
     */
    void addRelation(IViewRelation relation);

    /**
     * Remove a relation to the diagram.
     * @pre
     *      relation != null
     */
    void removeRelation(IViewRelation relation);

    /**
     * Hide a relation to the diagram
     */
    void hideRelation(IViewRelation relation);

    void accept(IClassVisitor visitor);

    @Override
    void accept(IDiagramVisitor visitor);
}
