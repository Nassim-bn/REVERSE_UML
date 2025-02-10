package fr.univrouen.umlreverse.model.diagram.sequence;

import java.util.ArrayList;
import java.util.List;

import fr.univrouen.umlreverse.model.diagram.sequence.Segment.SegmentType;
import fr.univrouen.umlreverse.util.Contract;

public class SegmentWithElse extends Segment implements ISegmentWithElse {
	
	private final List<ISegment> elseList = new ArrayList<>();

	// CONSTRUCTEURS
	public SegmentWithElse(SegmentType type, ISequenceDiagram diagram, String condition) {
		super(type, diagram, condition);
	}
	
	public SegmentWithElse(SegmentType type, ISequenceDiagram diagram) {
		super(type, diagram);
	}
	
	//REQUETES

	@Override
	public List<ISegment> getElseList() {
		return elseList;
	}
	
	//COMMANDES
	
	@Override
	public void addElseToList(ISegment els) {
		Contract.check(els != null, "else can't be null");
		elseList.add(els);
		firePropertyChange(ELSE_ADDED_PROPERTY_NAME, null, elseList);
	}
	
	@Override
	public void removeElseToList(ISegment els) {
		Contract.check(els != null, "else can't be null");
		elseList.remove(els);
		firePropertyChange(ELSE_ADDED_PROPERTY_NAME, null, elseList);
	}
}
