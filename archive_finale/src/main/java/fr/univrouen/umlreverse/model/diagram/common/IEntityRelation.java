package fr.univrouen.umlreverse.model.diagram.common;

import java.util.Set;

/**
 * Interface for all entities who can be added a relation.
 */
public interface IEntityRelation extends IEntity {
 // ATTRIBUTES   
 String RELATION_ADDED_PROPERTY_NAME = "relationAdded";
    String RELATION_REMOVED_PROPERTY_NAME = "relationRemoved";
    
// REQUESTS
    IRelation getRelation(IEntityRelation entity);
    Set<IRelation> getRelations();
    
// COMMANDS

    /**
     * addRelation allows to add a relation into a diagram
     * @param entity
     * @return 
     */
    IRelation addRelation(IEntityRelation entity);

    /**
     * addRelationDst allows to add a relation into the tree view diagram of the application
     * @param relation
     * @param entity
     */
    void addRelationDst(IRelation relation, IEntityRelation entity);

    /**
     * removeRelation allow to remove a relation from the diagram
     * @param entity
     */
    void removeRelation(IEntityRelation entity);

    /**
     * removeRelationDst allows to remove a relation infromto the tree view diagram of the application
     * @param entity
     */
    void removeRelationDst(IEntityRelation entity);
}
