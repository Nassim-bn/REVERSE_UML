/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.model.diagram.common;

import java.util.Set;

/**
 * Common interface for a diagram.
 */
public interface IDiagramCommon extends IDiagram {
// REQUESTS

    /**
     * getRelations Getter that permits to give the relations list from a use case diagram
     * @return Set<IRelation>
     */
    Set<IRelation> getRelations();
// COMMANDS

    /**
     * addRelation allows to add relations into a use case diagram
     * @param relation
     */
    void addRelation(IRelation relation);

    /**
     * removeRelation allows to remove relations from a use case diagram
     * @param relation
     */
    void removeRelation(IRelation relation);

	void addNote(INote note);
    
    
    
}
