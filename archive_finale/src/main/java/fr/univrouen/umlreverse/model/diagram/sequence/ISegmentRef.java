package fr.univrouen.umlreverse.model.diagram.sequence;

public interface ISegmentRef extends ISegmentCommon {

	ISequenceDiagram getDiagramContained();

	void setDiagramContained(ISequenceDiagram diagramContained);

}
