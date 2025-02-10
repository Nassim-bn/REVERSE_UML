package fr.univrouen.umlreverse.model.io.parser.util.usecase;

import fr.univrouen.umlreverse.model.diagram.usecase.NoteUsecase;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseDiagram;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;

/**
 * An usecase note.
 */
public class ParseUsecaseNote extends ParseUsecaseEntity<NoteUsecase> {
    // CONSTANTS
    private static final String ADD_ERR = "Impossible d'ajouter la note : ";

    private NoteUsecase note;

    public ParseUsecaseNote(ParseUsecaseDiagram diagram, ParseUsecaseGroup group, String text) {
        super(diagram, group, text);
    }

    public NoteUsecase getModel() {
        return note;
    }

    public void addIntoModel() {
        try {
            note = new NoteUsecase(getName(), getDiagram().getModel());
            getDiagram().getModel().addNote(note);
        } catch (RefusedAction refusedAction) {
            getDiagram().getLogger().addError(ADD_ERR + getName());
        }

        getDiagram().getModel().addNote(note);
        note.addStyle(IDiagramEditorController.POSITION_STYLE_ID, (ParseDiagram.count_generation % 5) * ParseDiagram.SCALE_GENERATION_X + "|" + (ParseDiagram.count_generation / 5)  * ParseDiagram.SCALE_GENERATION_Y + "|0|0");
        ++ParseDiagram.count_generation;
    }
}
