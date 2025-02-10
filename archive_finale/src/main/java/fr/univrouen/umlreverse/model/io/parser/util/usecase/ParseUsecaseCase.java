package fr.univrouen.umlreverse.model.io.parser.util.usecase;

import fr.univrouen.umlreverse.model.diagram.usecase.Usecase;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseDiagram;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;

/**
 * An usecase case.
 */
public class ParseUsecaseCase extends ParseUsecaseEntity<Usecase> {
    private Usecase usecase;

    public ParseUsecaseCase(ParseUsecaseDiagram diagram, ParseUsecaseGroup group, String name) {
        super(diagram, group, name);
    }

    public Usecase getModel() {
        return usecase;
    }

    public void addIntoModel() {
        usecase = new Usecase(getName(), getParent().getModel());
        getDiagram().getModel().addUsecase(usecase);
        usecase.addStyle(IDiagramEditorController.POSITION_STYLE_ID, (ParseDiagram.count_generation % 5) * ParseDiagram.SCALE_GENERATION_X + "|" + (ParseDiagram.count_generation / 5)  * ParseDiagram.SCALE_GENERATION_Y + "|0|0");
        ++ParseDiagram.count_generation;
    }
}
