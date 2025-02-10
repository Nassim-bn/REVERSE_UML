package fr.univrouen.umlreverse.model.io.parser.util.usecase;

import fr.univrouen.umlreverse.model.diagram.usecase.Actor;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseDiagram;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;

/**
 * An usecase actor.
 */
public class ParseUsecaseActor extends ParseUsecaseEntity<Actor> {
    private Actor actor;

    public ParseUsecaseActor(ParseUsecaseDiagram diagram, ParseUsecaseGroup group, String name) {
        super(diagram, group, name);
    }

    public Actor getModel() {
        return actor;
    }

    public void addIntoModel() {
        actor = new Actor(getName(), getParent().getModel());
        getDiagram().getModel().addActor(actor);
        actor.addStyle(IDiagramEditorController.POSITION_STYLE_ID, (ParseDiagram.count_generation % 5) * ParseDiagram.SCALE_GENERATION_X + "|" + (ParseDiagram.count_generation / 5)  * ParseDiagram.SCALE_GENERATION_Y + "|0|0");
        ++ParseDiagram.count_generation;
    }

}
