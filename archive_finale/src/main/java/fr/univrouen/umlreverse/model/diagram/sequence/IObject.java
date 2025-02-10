package fr.univrouen.umlreverse.model.diagram.sequence;

import java.io.Serializable;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.sequence.visitor.ISequenceVisitor;
import fr.univrouen.umlreverse.model.diagram.usecase.System;
import fr.univrouen.umlreverse.model.project.IObjectEntity;

public interface IObject  extends IEntityRelation,Serializable {
	// ATTRIBUTES
    String NOTE_ADDED_PROPERTY_NAME = "NoteAdded";
    String NOTE_REMOVED_PROPERTY_NAME = "NoteRemoved";
    String STEREOTYPE_CHANGED_PROPERTY_NAME = "stereotypeChanged";
    String NAME_CHANGED_PROPERTY_NAME = "nameChanged";
    String PARENT_CHANGED_PROPERTY_NAME = "parentChanged";

//REQUESTS

    /**
     * getName Getter of name that permits to get name object
     * @return String
     */
    String getName();

    /**
     * getListNotes Getter of ListNotes that permits to get the notes list
     * @return Set<INoteUsecase>
     */
    Set<INoteSequence> getNotes();

    /**
     * getEntity Getter of entity to have the entity linked with the object
     * @return IObjectEntity
     */
    IObjectEntity getEntity();

    /**
     * return the keeping (garde) of the object (for example actor, view...)
     * @return String
     */
    String getStereotype();

    double getX();

//COMMANDS
    /**
     * addNote allows to add Notes from sequence diagram
     * @param note
     */
    void addNote(INoteSequence note);

    /**
     * removeNote allows to remove Notes from sequence diagram
     * @param note
     */
    void removeNote(INoteSequence note);

    /**
     * setName Setter of name that permits to set the name object
     * @param name
     */
    void setName(String name);



    /**
     * set the keeping (garde) of the object (for example actor, view...)
     * @param keeping
     */
    void setStereotype(String keeping);

    //TODO cadre temporel

    /**
     * accept function that permits to use the visitor
     * @param visitor
     */
    void accept(ISequenceVisitor visitor);

	String getFullName();

}
