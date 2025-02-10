package fr.univrouen.umlreverse.model.diagram.usecase;


import fr.univrouen.umlreverse.model.diagram.common.IDiagramCommon;
import fr.univrouen.umlreverse.model.diagram.usecase.visitor.IUsecaseVisitor;

import java.io.Serializable;
import java.util.Set;

/**
 * @author BELKHOUS
 * 
 * Interface that will allows to build a use case diagram
 *
 */

public interface IUsecaseDiagram extends IDiagramCommon,Serializable {
// ATTRIBUTES
String ACTOR_STYLE_ID = "actor";
    String USECASE_STYLE_ID = "usecase";
    String USECASEGROUP_STYLE_ID = "usecase-group";
    
    String USECASE_ADDED_PROPERTY_NAME = "UsecaseAdded";
    String ACTOR_ADDED_PROPERTY_NAME = "ActorAdded";
    String GROUP_ADDED_PROPERTY_NAME = "GroupAdded";
    String USECASE_REMOVED_PROPERTY_NAME = "UsecaseRemoved";
    String ACTOR_REMOVED_PROPERTY_NAME = "ActorRemoved";
    String GROUP_ADDED_REMOVED_NAME = "GroupRemoved";
    
    
// REQUESTS   
	/**
	 * getActor gives the actor who has a specific name
	 * @param name
	 * @return Actor
	 */
	Actor getActor(String name) ;
        
	/**
	 * getListActors Getter that allows to give the actors list of the diagram
	 * @return Set<Actor>
	 */
	 Set<Actor> getActors();
         
	/**
	 * getListNotes Getter that allows to give the notes list of the diagram
	 * @return Set<INote>
	 */
	Set<INoteUsecase> getNotes();

	/**
	 * getDefaultGroup Getter that allows to give the default group of the diagram
	 * @return System
	 */
	System getRootSystem();

	/**
	 * getListUseCaseGroups Getter that allows to give the use case groups list of the diagram
	 * @return Set<IUseCaseGroup>
	 */
	 Set<ISystem> getSystems();
         
	/**
	 * getUsecase gives the use case who has a specific name
	 * @param name
	 * @return UseCase
	 */
	IUsecase getUsecase(String name);

	/**
	 * getListUseCases Getter that allows to give the use cases list of the diagram
	 * @return Set<UseCase>
	 */
	 Set<IUsecase> getUsecases();

// METHODS	
	/**
	 * addActor allows to add a new actor into the diagram
	 * @param actor
	 */
	 void addActor(Actor actor);
         
	 /**
	 * addNote allows to create a new Note into the diagram
	 *
	 * @param note
	 */
	 void addNote(INoteUsecase note);
        
	/**
	 * addEntity allows to create a new use case into the diagram
	 * @param useCase
	 */
	 void addUsecase(IUsecase useCase);
         
	/**
	 * addSystem allows to add a new group into the diagram
	 * @param group
	 */
	void addSystem(ISystem group);
        
	/**
	 * removeActor allows to remove an actor from the diagram
	 * @param actor
	 */
	 void removeActor(Actor actor) ;
        
	/**
	 * removeNote allows to remove a Note from the diagram
	 * @param note
	 */
	void removeNote(INoteUsecase note);
	
	/**
	 * removeSystem allows to remove a group from the diagram
	 * @param group
	 */
	void removeSystem(ISystem group);

	/**
	 * removeEntity allows to remove a use case from the diagram
	 * @param useCase
	 */
	 void removeUsecase(IUsecase useCase);

	/**
	 * accept function that allows to use the visitor
	 * @param visitor
	 */
	void accept(IUsecaseVisitor visitor);
}
