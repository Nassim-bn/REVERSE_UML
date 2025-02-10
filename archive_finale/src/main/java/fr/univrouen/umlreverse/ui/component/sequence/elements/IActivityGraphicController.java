package fr.univrouen.umlreverse.ui.component.sequence.elements;

import fr.univrouen.umlreverse.model.diagram.sequence.IActivity;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphicController;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;

public interface IActivityGraphicController extends IEntityGraphicController {

	void refresh();
	ISequenceController getDiagramController();
	IActivity getModel();
}
