package fr.univrouen.umlreverse.model.diagram.sequence;

import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.ALL_STYLE_ID;
import static fr.univrouen.umlreverse.model.diagram.common.INote.STYLE_CHANGED_PROPERTY_NAME;
import static fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram.USECASEGROUP_STYLE_ID;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.Observable;
import fr.univrouen.umlreverse.model.diagram.sequence.visitor.ISequenceVisitor;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.diagram.util.Style;
import fr.univrouen.umlreverse.util.Contract;

public class Segment extends SegmentCommon implements ISegment {


	// ATTRIBUTS
	private final Set<ISegmentCommon> listSegment = new HashSet<>() ;
    private final Set<IRelation> listRelation = new HashSet<>();

	// CONSTRUCTEURS
	public Segment(SegmentType type, ISequenceDiagram diagram, String condition) {
		super(type, diagram, condition);
	}

	public Segment(SegmentType type, ISequenceDiagram diagram) {
		super(type, diagram);
	}

	// REQUETES
	@Override
	public Set<ISegmentCommon> getSegments() {
		return listSegment;
	}

	@Override
	public Set<IRelation> getRelations() {
		return listRelation;
	}


	@Override
	public Set<ISegmentCommon> getSegmentsChilds(Set<ISegmentCommon> result) {
		result.addAll(getSegments());
		getSegments().stream().filter(seg -> !seg.getSegmentType().equals(SegmentType.REF)).forEach(seg -> {

			((ISegment) seg).getSegmentsChilds(result);
		});
		return result;

	}

	// COMMANDES

	@Override
	public void addSegment(ISegmentCommon segment) {
		Contract.check(segment != null, "segment can't be null");

		listSegment.add(segment);
        segment.setParent(this);
        firePropertyChange(SEGMENT_ADDED_PROPERTY_NAME, null, listSegment);
	}



	@Override
	public void removeSegment(ISegmentCommon segment) throws Exception {
		Contract.check(segment != null, "segment can't be null");

		if (listSegment.contains(segment)) {
			listSegment.remove(segment);
			firePropertyChange(SEGMENT_REMOVED_PROPERTY_NAME, null, listSegment);
		} else {
			throw new Exception("This segment doesn't exist");
		}
	}

	@Override
	 public void addRelation(IRelation relation) {
		 Contract.check(relation != null, "relation must not be null.");
	     boolean b = listRelation.add(relation);
	    if (b) {
	    	firePropertyChange(RELATION_ADDED_TO_SEGMENT_PROPERTY_NAME, null, relation);
	    }
	 }

	@Override
	 public void removeRelation(IRelation relation) {
	    Contract.check(relation != null, "relation must not be null.");
	    boolean b = listRelation.remove(relation);
	    if (b) {
	    	firePropertyChange(RELATION_REMOVED_TO_SEGMENT_PROPERTY_NAME, relation, null);
	    }
	 }

	public enum SegmentType {
		OPT("opt"),
		ALT("alt"),
		LOOP("loop"),
		BREAK("break"),
		REF("ref"),
		PAR("par"),
		CRITICAL("critical"),
		STRICT("strict"),
		SEQ("seq"),
		IGNORE("ignore"),
		CONSIDER("consider"),
		ASSERT("assert"),
		NEG("neg"),
		ELSE("else");

		private String type;

		SegmentType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}
}