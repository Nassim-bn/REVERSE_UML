package fr.univrouen.umlreverse.model.diagram.usecase;

import fr.univrouen.umlreverse.model.diagram.common.IStylizable;
import fr.univrouen.umlreverse.model.diagram.common.IObservable;
import fr.univrouen.umlreverse.model.diagram.usecase.visitor.IUsecaseVisitor;

import java.io.Serializable;
import java.util.Set;

/**
 *
 *ISystem Interface that allows to build a use case group
 *
 */
public interface ISystem extends IObservable, IStylizable, Serializable {

//REQUESTS
    
    /**
     * getAbsoluteName getter that allows to find the absolute Name of a use case group
     * @return String
     */
     String getAbsoluteName();
     
    /**
     * getParentDiagram getter that permits to give the Parent Diagram of a use case group
     * @return IUsecaseDiagram
     */
    IUsecaseDiagram getDiagram();
    
    /**
     * getListUseCaseGroups Getter that allows to give the use case groups list of one use case group
     * @return Set<ISystem>
     */
     Set<ISystem> getGroups();
     
     /**
     * getNameGroup Getter that allows to give the name of a use case group
     * @return String
     */
     String getNameGroup();

    /**
     * getNotes Getter that allows to give the notes list of a specific use case group
     * @return Set<INoteUsecase>
     */
    Set<INoteUsecase> getNotes();
     
    /**
     * getParentGroup Getter that allows to give the Parent Group
     * @return System
     */
    System getParentGroup();

    /**
     * getListUseCases Getter that allows to give the use cases list of a use case group
     * @return Set<UseCase>
     */
     Set<Usecase> getUseCases();

// COMMANDS  
    /**
     * addGroup allows to add a new group in to a parent one
     * 
     * @param group
     * @throws Exception
     */
    void addGroup(ISystem group) throws Exception;
    
     /**
     * addNote allows to create a new Note into a parent one
     * @param note
     */
    void addNote(INoteUsecase note);
    
    /**
     * addUsecase allows to create a new use case nto a parent one
     * 
     * @param useCase
     * @throws Exception
     */
     void addUsecase(Usecase useCase) throws Exception;
    
    /**
     * setDiagram Setter that allows to set a Parent Diagram for the use case group
     * @param parentDiagram
     */
    void setDiagram(IUsecaseDiagram parentDiagram);

    /**
     * setParentGroup Setter that allows to set a Parent group for the use case group
     * @param parentGroup
     */
    void setParentGroup(System parentGroup);
    
    /**
     * removeGroup allows to remove a use case group from a diagram
     * @param group
     * @throws Exception
     */
    void removeGroup(ISystem group) throws Exception;
	
    /**
     * removeNote allows to remove a Note from a diagram
     * @param note
     * @throws Exception
     */
    void removeNote(INoteUsecase note) throws Exception;

    /**
     * removeUsecase allows to remove a use case from a diagram
     * @param useCase
     * @throws Exception
     */
     void removeUsecase(Usecase useCase) throws Exception;

    /**
     * setNameGroup Setter that allows to set a name group for the use case group
     * @param useCaseGroupName
     */
     void setNameGroup(String useCaseGroupName);

    /**
     * accept function that allows to use the visitor
     * @param visitor
     */
    void accept(IUsecaseVisitor visitor);
}
