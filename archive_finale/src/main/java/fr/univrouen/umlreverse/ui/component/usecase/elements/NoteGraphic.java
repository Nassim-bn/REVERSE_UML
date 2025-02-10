package fr.univrouen.umlreverse.ui.component.usecase.elements;

import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.ui.component.common.elements.INoteGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.ANoteGraphic;
import fr.univrouen.umlreverse.ui.view.usecase.IUsecaseController;
import fr.univrouen.umlreverse.model.diagram.usecase.INoteUsecase;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityRelationGraphic;

public class NoteGraphic extends ANoteGraphic implements INoteGraphic, IEntityRelationGraphic {
    
// ATTRIBUTES
    private final NoteGraphicController controller;
    
// CONSTRUCTORS
    public NoteGraphic(IUsecaseController diagramController, INoteUsecase note) {
        super(diagramController);
        controller = new NoteGraphicController(this, diagramController, note);
        controller.loadRelations();
        autosize();
    }
    
// REQUEST
    @Override
    public NoteGraphicController getController() {
        return controller;
    }
    @Override
    public IEntityRelation getModel() {
        return controller.getModel();
    }
}