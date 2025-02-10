
package fr.univrouen.umlreverse.model.diagram.usecase;

import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.INote;
import fr.univrouen.umlreverse.model.diagram.usecase.visitor.IUsecaseVisitor;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * INote interface is the interface of notes (comments) on a diagram
 *  
 */
public interface INoteUsecase extends INote, IEntityRelation,Serializable {
    
	
	/**
	 * getUsecaseEntities getter of classEntities that permits to get the use cases list
	 * @return the Set<ObjectEntity>
	 */
	Set<IEntityUsecase> getUsecaseEntities();
	
	/**
	 * addUsecaseEntities that allows to add use case entity into a diagram
	 * @param entity
	 */
	void addUsecaseEntity(IEntityUsecase entity);
	
	/**
	 * removeUsecaseEntities that allows to remove use case entity into a diagram
	 * @param entity
	 */
	void removeUsecaseEntity(IEntityUsecase entity);

	/**
	 * accept funtion that allows to use the visitor
	 * @param visitor
	 */
	void accept(IUsecaseVisitor visitor);
}
