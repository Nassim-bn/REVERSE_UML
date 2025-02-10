package fr.univrouen.umlreverse.model.diagram.sequence;

import java.io.Serializable;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.INote;
import fr.univrouen.umlreverse.model.diagram.sequence.visitor.ISequenceVisitor;
import fr.univrouen.umlreverse.model.diagram.usecase.IEntityUsecase;
import fr.univrouen.umlreverse.model.diagram.usecase.visitor.IUsecaseVisitor;

public interface INoteSequence extends INote, IEntityRelation,Serializable {
	// ATTRIBUTES               
	String ENTITY_ADDED_PROPERTY_NAME = "EntityAdded";
	    String ENTITY_REMOVED_PROPERTY_NAME = "EntityRemoved";
	    
	// REQUESTS
		
		/**
		 * getObjects getter of classEntities that permits to get the object list
		 * @return the Set<ObjectEntity>
		 */
		Set<IObject> getObjects();
		
		/**
		 * addUsecaseEntities that allows to add object entity into a diagram
		 * @param entity
		 */
		void addObjectEntity(IObject entity);
		
		/**
		 * removeObjectEntities that allows to remove object entity into a diagram
		 * @param entity
		 */
		void removeObjectEntity(IObject entity);

		/**
		 * accept funtion that allows to use the visitor
		 * @param visitor
		 */
		void accept(ISequenceVisitor visitor);
}
