package fr.univrouen.umlreverse.ui.component.sequence.elements;

import fr.univrouen.umlreverse.model.diagram.sequence.ISegment;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegmentCommon;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphicController;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;

public interface ISegmentGraphicController extends IEntityGraphicController {

	void refresh();

	ISequenceController getDiagramController();

	ISegmentCommon getModel();

}
