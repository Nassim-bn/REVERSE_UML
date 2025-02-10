package fr.univrouen.umlreverse.ui.view.sequence;

import java.util.Map;
import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.INote;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.sequence.IActivity;
import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.model.diagram.sequence.IRelationToObject;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegmentCommon;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.ui.component.common.elements.EnlargePoint;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.NoteGraphic;
import fr.univrouen.umlreverse.ui.component.sequence.elements.ActivityGraphic;
import fr.univrouen.umlreverse.ui.component.sequence.elements.ObjectGraphic;
import fr.univrouen.umlreverse.ui.component.sequence.elements.SegmentGraphic;
import fr.univrouen.umlreverse.ui.component.sequence.relations.RelationToObjectGraphic;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;

public interface ISequenceController extends IDiagramEditorController {

    ISequenceDiagram getDiagram();

    Map<IObject, ObjectGraphic> getObjects();

    Map<INote, NoteGraphic> getNotes();

    Map<IActivity, ActivityGraphic> getActivities();

	Map<ISegmentCommon, SegmentGraphic> getSegments();

	Map<IRelationToObject, RelationToObjectGraphic> getRelationsToObject();

    // COMMANDS
    void createRelation(IEntityRelation note);

    void createObject(double x, double y);

	void createSegment(double x, double y);

	void createActivite(double x, double y);

    void removeRelation(IRelation relation);

	void removeNote(INote note);

	void removeObject(IObject object);

	void removeSegment(ISegmentCommon model);

	/**
	 * return the position where to put the next relationToObject
	 * */
	double getNextYRelationToObjectPosition();

	EnlargePoint bindToEnlargeActivityPoint(IEntityGraphic bindedObject);

	void removeActivity(IActivity activity);
}
