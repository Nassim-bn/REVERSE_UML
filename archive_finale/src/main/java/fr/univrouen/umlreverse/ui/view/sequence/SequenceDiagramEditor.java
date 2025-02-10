package fr.univrouen.umlreverse.ui.view.sequence;

import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.ui.view.common.ADiagramEditor;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;

public class SequenceDiagramEditor extends ADiagramEditor {

	// ATTRIBUTES
    private final SequenceController controller;

    // CONSTRUCTORS
    public SequenceDiagramEditor(ISequenceDiagram SequenceDiagram) {
        super();
        controller = new SequenceController(this, SequenceDiagram);
    }

    // REQUEST
    @Override
    public IDiagramEditorController getController() {
        return controller;
    }

    // COMMANDES

    /**
     * Create an entity via the controller.
     * @param x the x position of the entity
     * @param y the y position of the entity
     */

    public void createSequence(double x, double y){
        //controller.createSequence(x, y);
    }


	public void createObject(int i, int j) {
		controller.createObject(i,j);
	}

	public void createSegment(int i, int j) {
		controller.createSegment(i,j);

	}

	public void createActivite(int i, int j) {
		controller.createActivite(i,j);

	}

}
