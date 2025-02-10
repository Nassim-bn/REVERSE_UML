package fr.univrouen.umlreverse.model.diagram.sequence;

import fr.univrouen.umlreverse.model.diagram.sequence.Segment.SegmentType;

public class SegmentRef extends SegmentCommon implements ISegmentRef {
	
	//ATTRIBUTS
	private ISequenceDiagram diagramContained;

	// CONSTRUCTEURS
	public SegmentRef(SegmentType type, ISequenceDiagram diagram, String condition) {
		super(type, diagram, condition);
	}
	
	public SegmentRef(SegmentType type, ISequenceDiagram diagram) {
		super(type, diagram);
	}

	@Override
	public ISequenceDiagram getDiagramContained() {
		return diagramContained;
	}

	@Override
	public void setDiagramContained(ISequenceDiagram diagramContained) {
		this.diagramContained = diagramContained;
	}

}
