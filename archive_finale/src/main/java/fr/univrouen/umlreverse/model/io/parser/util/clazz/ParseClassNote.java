package fr.univrouen.umlreverse.model.io.parser.util.clazz;

import fr.univrouen.umlreverse.model.diagram.clazz.view.NoteClass;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseDiagram;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;

import java.util.UUID;

/**
 * A class note.
 */
public class ParseClassNote extends ParseClassEntity {
    // CONSTANTS
    private static final String ADD_ERR = "Impossible d'ajouter la note : ";

    // ATTRIBUTES
    private NoteClass model;

    // CONSTRUCTOR
    public ParseClassNote(ParseClassDiagram diagram, ParseClassPackage parent, String text) {
        super(diagram, parent, text);
        parent.add(this);
        model = null;
    }

    // REQUESTS
    public NoteClass getModel() {
        return model;
    }

    // COMMANDS
    @Override
    public void addIntoModel() {
        try {
            model = new NoteClass("" + UUID.randomUUID(), getName(), getDiagram().getModel());
            getDiagram().getModel().addNote(model);
            model.addStyle(IDiagramEditorController.POSITION_STYLE_ID, (ParseDiagram.count_generation % 5) * ParseDiagram.SCALE_GENERATION_X + "|" + (ParseDiagram.count_generation / 5)  * ParseDiagram.SCALE_GENERATION_Y + "|0|0");
            ++ParseDiagram.count_generation;
        } catch (RefusedAction refusedAction) {
            getDiagram().getLogger().addError(ADD_ERR + getName());
        }
    }
}
