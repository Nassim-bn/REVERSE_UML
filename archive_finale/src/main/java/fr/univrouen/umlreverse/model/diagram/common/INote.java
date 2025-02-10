package fr.univrouen.umlreverse.model.diagram.common;

import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.usecase.IEntityUsecase;
import fr.univrouen.umlreverse.model.diagram.usecase.visitor.IUsecaseVisitor;
import fr.univrouen.umlreverse.model.diagram.visitor.IDiagramVisitor;

/**
 * A note to class diagram
 */
public interface INote extends IEntity, IEntityRelation {

	// ATTRIBUTES
	String TEXT_CHANGED_PROPERTY_NAME = "TextChanged";
    String STYLE_CHANGED_PROPERTY_NAME = "StyleChanged";
             
 	String ENTITY_ADDED_PROPERTY_NAME = "EntityAdded";
 	String ENTITY_REMOVED_PROPERTY_NAME = "EntityRemoved";
    
// REQUESTS

    /**
     * The text
     */
    String getText();


    // METHODS

    /**
     * Setter of text
     * @param text
     *      the new text
     */
    void setText(String text);
    
    
    
    /**
	 * getUsecaseEntities getter of classEntities that permits to get the use cases list
	 * @return the Set<ObjectEntity>
	 */
	Set<IEntityRelation> getEntityRelation();
	
	/**
	 * addUsecaseEntities that allows to add use case entity into a diagram
	 * @param entity
	 */
	void addEntityRelation(IEntityRelation entity);
	
	/**
	 * removeUsecaseEntities that allows to remove use case entity into a diagram
	 * @param entity
	 */
	void removeEntityRelation(IEntityRelation entity);

	/**
	 * accept funtion that allows to use the visitor
	 * @param visitor
	 */
	void accept(IDiagramVisitor visitor);
}
