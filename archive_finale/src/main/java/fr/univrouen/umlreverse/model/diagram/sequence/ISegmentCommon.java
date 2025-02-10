package fr.univrouen.umlreverse.model.diagram.sequence;

import java.io.Serializable;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.common.IObservable;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.IStylizable;
import fr.univrouen.umlreverse.model.diagram.sequence.Segment.SegmentType;
import fr.univrouen.umlreverse.model.diagram.sequence.visitor.ISequenceVisitor;

public interface ISegmentCommon  extends IObservable, IStylizable, Serializable {

	String RELATION_ADDED_PROPERTY_NAME = "RelationAdded";
    String RELATION_REMOVED_PROPERTY_NAME = "RelationRemoved";

    String CONDITION_CHANGED_PROPERTY_NAME = "ConditionChanged";

    String PARENT_CHANGED_PROPERTY_NAME = "ParentChanged";

    String SEGMENT_ADDED_PROPERTY_NAME = "SegmentAdded";
    String SEGMENT_REMOVED_PROPERTY_NAME = "SegmentRemoved";

    String ELSE_ADDED_PROPERTY_NAME = "ElseAdded";
    String ELSE_REMOVED_PROPERTY_NAME = "ElseRemoved";

    String SEGMENT_STYLE_ID = "segment";

    String RELATION_ADDED_TO_SEGMENT_PROPERTY_NAME = "RelationAddedSegment";
    String RELATION_REMOVED_TO_SEGMENT_PROPERTY_NAME = "RelationRemovedSegment";

	/**
	 * Return type of the segment.
	 */
	SegmentType getSegmentType();

	/**
	 * Return condition of the segment.
	 */
	String getCondition();

	/**
     * getParentDiagram getter that permits to give the Parent Diagram of a sequence group.
     * @return ISequenceDiagram
     */
    ISequenceDiagram getDiagram();

     /**
      * getParentGroup Getter that allows to give the Parent Group.
      * @return ISegment
      */
     ISegment getParent();

     double getY();

	// COMMANDES
	/**
	 * Modify condition of the segment.
	 *
	 * @param
	 *     String condition
	 */
	void setCondition(String condition);

     /**
      * setParent Setter that allows to set a parent for the segment.
      * @param parentGroup
      */
     void setParent(ISegment parent);

	/**
     * accept function that permits to use the visitor.
     *
     * @param
     *     ISequenceVisitor visitor
     */
    void accept(ISequenceVisitor visitor);

	void setStart(IObject start);

	void setEnd(IObject end);

	IObject getStart();

	IObject getEnd();

	int getLevel();
}
