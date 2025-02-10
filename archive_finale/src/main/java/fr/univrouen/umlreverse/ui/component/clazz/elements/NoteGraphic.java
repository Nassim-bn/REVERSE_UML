package fr.univrouen.umlreverse.ui.component.clazz.elements;

import fr.univrouen.umlreverse.ui.component.common.elements.INoteGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.ANoteGraphic;
import fr.univrouen.umlreverse.ui.view.clazz.IClassController;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityTextGraphicController;
import fr.univrouen.umlreverse.model.diagram.clazz.view.INoteClass;

/**
 * A component representing a graphic note.
 */
public class NoteGraphic extends ANoteGraphic implements INoteGraphic {
	// ATTRIBUTES
	/** The controller associated with this NoteGraphic. */
    private final IEntityTextGraphicController controller;

    // CONSTRUCTORS
    /**
     * Create a component representing the given note in a diagram with a given
     * controller.
     * @param diagramController the diagram controller
     * @param note the note to represent
     */
    public NoteGraphic(IClassController diagramController, INoteClass note) {
        super(diagramController);
        controller = new NoteGraphicController(this, diagramController, note);
        controller.loadRelations();
        autosize();
    }

    // QUERIES
    @Override
    public IEntityTextGraphicController getController() {
        return controller;
    }
}