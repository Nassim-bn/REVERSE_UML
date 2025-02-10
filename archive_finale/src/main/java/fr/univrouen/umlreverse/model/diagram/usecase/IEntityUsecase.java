package fr.univrouen.umlreverse.model.diagram.usecase;

import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.usecase.visitor.IUsecaseVisitor;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * IEntityUsecase Interface that allows to build a UseCaseEntity
 *
 */
public interface IEntityUsecase extends IEntityRelation,Serializable {
    
// ATTRIBUTES 
    String NOTE_ADDED_PROPERTY_NAME = "NoteAdded";
    String NOTE_REMOVED_PROPERTY_NAME = "NoteRemoved";
    
    String NAME_CHANGED_PROPERTY_NAME = "nameChanged";
    String PARENT_CHANGED_PROPERTY_NAME = "parentChanged";

//REQUESTS
    
    /**
     * getName Getter of name that permits to get name actor
     * @return String
     */
    String getName();
    
    /**
     * getListNotes Getter of ListNotes that permits to get the notes list
     * @return Set<INoteUsecase>
     */
    Set<INoteUsecase> getNotes();

    /**
     * getParentGroup Getter of ParentGroup that permits to get the parent group
     * @return System
     */
    System getParentGroup();

//COMMANDS
    /**
     * addNote allows to add Notes from use case diagram
     * @param note
     */
    void addNote(INoteUsecase note);
    
    /**
     * removeNote allows to remove Notes from use case diagram
     * @param note
     */
    void removeNote(INoteUsecase note);
    
    /**
     * setName Setter of name that permits to set the name actor
     * @param name
     */
    void setName(String name);
    
    /**
     * setParentGroup Setter of ParentGroup that permits to set the parent group diagram
     * @param parentGroup
     */
    void setParentGroup(System parentGroup);

    /**
     * accept function that permits to use the visitor
     * @param visitor
     */
    void accept(IUsecaseVisitor visitor);
}
