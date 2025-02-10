package fr.univrouen.umlreverse.model.diagram.sequence;

import java.util.List;

public interface ISegmentWithElse extends ISegment {

	List<ISegment> getElseList();

	void addElseToList(ISegment els);

	void removeElseToList(ISegment els);
	
}
