package fr.univrouen.umlreverse.model.diagram.sequence;

import java.io.Serializable;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.common.IObservable;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.IStylizable;
import fr.univrouen.umlreverse.model.diagram.sequence.Segment.SegmentType;
import fr.univrouen.umlreverse.model.diagram.sequence.visitor.ISequenceVisitor;

public interface ISegment extends IObservable, IStylizable, Serializable, ISegmentCommon {

	
    

	/**
      * getListSegment Getter that allows to give the segment list of a sequence group.
      * @return Set<ISegment>
      */
      Set<ISegmentCommon> getSegments();

      Set<ISegmentCommon> getSegmentsChilds(Set<ISegmentCommon> result);
	// COMMANDES
	

    /**
     * addSegment allows to create a new segment into a parent one.
     *
     * @param segment
     * @throws Exception
     */
     void addSegment(ISegmentCommon segment) throws Exception;

     /**
      * removeSegment allows to remove a segment from a diagram.
      * @param segment
      * @throws Exception
      */
     void removeSegment(ISegmentCommon segment) throws Exception;
     
	void addRelation(IRelation relation);

	void removeRelation(IRelation relation);

	Set<IRelation> getRelations();
}
