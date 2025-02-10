
package fr.univrouen.umlreverse.model.diagram.sequence;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.common.IDiagramCommon;
import fr.univrouen.umlreverse.model.diagram.common.INote;
import fr.univrouen.umlreverse.model.diagram.sequence.visitor.ISequenceVisitor;
import fr.univrouen.umlreverse.model.diagram.usecase.ISystem;
import fr.univrouen.umlreverse.model.diagram.usecase.System;
import fr.univrouen.umlreverse.model.diagram.usecase.visitor.IUsecaseVisitor;
import fr.univrouen.umlreverse.model.util.RefusedAction;

public interface ISequenceDiagram  extends IDiagramCommon,Serializable {
	// ATTRIBUTES
	String OBJECT_STYLE_ID = "object";

	String OBJECT_ADDED_PROPERTY_NAME = "objectAdded";
	String OBJECT_REMOVED_PROPERTY_NAME = "objectRemoved";

	// REQUESTS

	/**
		 * getObject gives the object who has a specific name
		 * @param name
		 * @return Object
		 */
		IObject getObject(String name);

		/**
		 * getListActors Getter that allows to give the actors list of the diagram
		 * @return Set<IObject>
		 */
		 List<IObject> getObjects();


		 Set<ISegmentCommon> getSegmentsChilds();
	// METHODS
		/**
		 * addObject allows to add a new object into the diagram
		 * @param object
		 * @throws RefusedAction
		 */
		 void addObject(IObject object) throws RefusedAction;

		 void addSegment(ISegmentCommon segment);

		 void addActivity(IActivity activity);


		/**
		 * addSystem allows to add a new group into the diagram
		 * @param group
		 */
		void addSystem(ISystem group);

		/**
		 * removeObject allows to remove an actor from the diagram
		 * @param object
		 */
		 void removeObject(IObject object) ;


		/**
		 * removeSystem allows to remove a group from the diagram
		 * @param group
		 */
		void removeSystem(ISystem group);


		/**
		 * getListNotes Getter that allows to give the notes list of the diagram
		 * @return Set<INote>
		 */
		Set<INote> getNotes();


		/**
		 * removeNote allows to remove a Note from the diagram
		 * @param note
		 */
		void removeNote(INote note);

		void addNote(INote note);


		/**
		 * accept function that allows to use the visitor
		 * @param visitor
		 */
		void accept(ISequenceVisitor visitor);

		Set<ISegmentCommon> getSegments();
		Set<IActivity> getActivities();

		void removeSegment(ISegmentCommon segment);

		void removeActivity(IActivity activity);
}
