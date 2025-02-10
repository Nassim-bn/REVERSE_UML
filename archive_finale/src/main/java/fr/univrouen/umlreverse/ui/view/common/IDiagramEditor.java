package fr.univrouen.umlreverse.ui.view.common;

import fr.univrouen.umlreverse.ui.view.util.PannableCanvas;

public interface IDiagramEditor {
    /**
     * Get controller of diagramEditor
     * @return DiagramEditorController
     */
    IDiagramEditorController getController();
    
    /**
     * Get this editor’s canvas
     * @return PannableCanvas
     */
    PannableCanvas getCanvas();
}
