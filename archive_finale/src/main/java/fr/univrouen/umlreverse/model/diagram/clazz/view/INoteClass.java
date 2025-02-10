package fr.univrouen.umlreverse.model.diagram.clazz.view;

import java.io.Serializable;
import java.util.List;

import fr.univrouen.umlreverse.model.diagram.clazz.visitor.IClassVisitor;
import fr.univrouen.umlreverse.model.diagram.common.IObservable;
import fr.univrouen.umlreverse.model.diagram.common.IStylizable;

/**
 * A note to class diagram
 */
public interface INoteClass extends IObservable, Serializable, IStylizable{

	// ATTRIBUTES
	String ENTITY_ADDED_PROPERTY_NAME = "EntityAdded";
    String ENTITY_REMOVED_PROPERTY_NAME = "EntityRemoved";
    String RELATION_REMOVED_PROPERTY_NAME = "RelationRemoved";
    String RELATION_ADDED_PROPERTY_NAME = "RelationAdded";
    String CONSTRAINT_ENTITY_ADDED_PROPERTY_NAME = "ConstraintEntityAdded";
    String CONSTRAINT_ENTITY_REMOVED_PROPERTY_NAME = "ConstraintEntityRemoved";
    String CONSTRAINT_RELATION_ADDED_PROPERTY_NAME = "ConstraintRelationAdded";
    String CONSTRAINT_RELATION_REMOVED_PROPERTY_NAME = "ConstraintRelationRemoved";

    /**
     * the diagram
     */
    IClassDiagram getDiagram();

    /**
     * The entities concerned by the note
     */
    List<IViewEntity> getEntities();

    /**
     * The entities concerned by the note with constraint
     */
    List<IViewEntity> getConstraintEntities();



    /**
     * The relations concerned by the note
     */
    List<IViewRelation> getViewRelations();

    /**
     * The relations concerned by the note with constraint
     */
    List<IViewRelation> getConstraintRelations();

    /**
     * The text
     */
    String getText();

    String getId();

    String getStyleId();
    // METHODS

    /**
     * Add a targeted entity
     * @param entity
     *      the entity to link
     */
    void addEntity(IViewEntity entity);

    /**
     * Add a targeted entity as constraint
     * @param entity
     *      the entity to link
     */
    void addConstraintEntity(IViewEntity entity);

    /**
     * Remove a linked entity
     * @param entity
     *      the entity to unlink
     */
    void removeEntity(IViewEntity entity);

    /**
     * Remove a linked entity as constraint
     * @param entity
     *      the entity to unlink
     */
    void removeConstraintEntity(IViewEntity entity);

    /**
     * Add a targeted relation
     * @param relation
     *      the relation to link
     */
    void addRelation(IViewRelation relation);

    /**
     * Add a targeted relation as constraint
     * @param relation
     *      the relation to link
     */
    void addConstraintRelation(IViewRelation relation);

    /**
     * Remove a linked relation
     * @param relation
     *      the relation to unlink
     */
    void removeRelation(IViewRelation relation);

    /**
     * Remove a linked relation as constraint
     * @param relation
     *      the relation to unlink
     */
    void removeConstraintRelation(IViewRelation relation);

    void accept(IClassVisitor visitor);

    /**
     * Setter of text
     * @param text
     *      the new text
     */
    void setText(String text);
}
