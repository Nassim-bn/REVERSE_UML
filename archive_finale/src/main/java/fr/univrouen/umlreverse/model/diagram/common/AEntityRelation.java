/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.model.diagram.common;

import fr.univrouen.umlreverse.util.Contract;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * AEntityRelation abstract class that permits to represent a relation that can be used by all of the Diagrams (Common code)
 */
public abstract class AEntityRelation extends AEntity implements IEntityRelation {
    
/**
	 * 
	 */
	private static final long serialVersionUID = 5043049397117546046L;
	// ATTRIBUTES
	
	// WARNING if there is two relation to the same IEntityRelation, only one will be stocked
	// Use IDiagram.getRelations to get all relations and filter with relaition.getEntitySource.equals(this) / relaition.getEntityTarget.equals(this)
    protected final Map<IEntityRelation, IRelation> relations = new HashMap<>();
    
// CONSTRUCTORS
    protected AEntityRelation(IDiagramCommon diagram) {
        super(diagram);
    }
    
// REQUESTS

    @Override
    public IRelation getRelation(IEntityRelation entity) {
        Contract.check(entity != null, "entity must not be null.");
        return relations.get(entity);
    }
	
    @Override
    public Set<IRelation> getRelations() {
        Set<IRelation> res = new HashSet<>(relations.values());
        return res;
    }
  
// COMMANDS
    @Override
    public IRelation addRelation(IEntityRelation entity) {
        Contract.check(entity != null, "entity must not be null.");
        IRelation relation = new Relation(getDiagram(), this, entity);
        relations.put(entity, relation);
        entity.addRelationDst(relation, entity);
        getDiagram().addRelation(relation);
        firePropertyChange(RELATION_ADDED_PROPERTY_NAME, null, relation);
		return relation;
    }

    /**
     * addRelationDst allows to add relation into the map of relations
     * @param relation
     * @param entity
     */
    public void addRelationDst(IRelation relation, IEntityRelation entity) {
        Contract.check(entity != null, "entity must not be null.");
        relations.put(entity, relation);
    }
        
    @Override
    public void removeRelation(IEntityRelation entity) {
        Contract.check(entity != null, "entity must not be null.");
        IRelation relation = relations.get(entity);
        relations.remove(entity);
        entity.removeRelationDst(entity);
        getDiagram().removeRelation(relation);
        firePropertyChange(RELATION_REMOVED_PROPERTY_NAME, relation, null);
    }

    @Override
    public void removeRelationDst(IEntityRelation entity) {
        Contract.check(entity != null, "entity must not be null.");
        relations.remove(entity);
    }
}
