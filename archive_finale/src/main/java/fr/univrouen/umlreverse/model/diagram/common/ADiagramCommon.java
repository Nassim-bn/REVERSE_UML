/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.model.diagram.common;

import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;
import java.util.HashSet;
import java.util.Set;

/**
 * Common code for a diagram.
 */
public abstract class ADiagramCommon extends ADiagram implements IDiagramCommon {
/**
	 * 
	 */
	private static final long serialVersionUID = -7719190710336780062L;
		// ATTRIBUTES
        private final Set<IRelation> relations  = new HashSet<>() ;
// CONSTRUCTORS
    protected ADiagramCommon(IProject project, String name) throws RefusedAction {
        super(project, name);
    }
   
// REQUESTS   
    @Override
    public Set<IRelation> getRelations() {
        return relations;
    }
    
// COMMANDS
    @Override
    public void addRelation(IRelation relation) {
        Contract.check(relation != null, "relation must not be null.");
        boolean b = relations.add(relation);
        if (b) {
            firePropertyChange(RELATION_ADDED_PROPERTY_NAME, null, relation);
        }
    }
      
    @Override
    public void removeRelation(IRelation relation) {
        Contract.check(relation != null, "relation must not be null.");
        boolean b = relations.remove(relation);
        if (b) {
            firePropertyChange(RELATION_REMOVED_PROPERTY_NAME, relation, null);
        }
    }
    
}
