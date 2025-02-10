package fr.univrouen.umlreverse.ui.component.common.elements;

import fr.univrouen.umlreverse.ui.component.common.util.NoteShape;
import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.INote;
import fr.univrouen.umlreverse.ui.component.common.elements.NoteGraphicController;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class NoteGraphic extends AEntityTextGraphic implements INoteGraphic, IEntityRelationGraphic {

	// ATTRIBUTES
    private final NoteShape rectangle;
    private final NoteGraphicController controller;
    
    // CONSTRUCTORS
    public NoteGraphic(IDiagramEditorController diagramController, INote note) {
        super(diagramController);
        Contract.check(diagramController != null, "L'argument diagramController "
                + "ne doit pas être nul.");
        Text text = getText();
        rectangle = new NoteShape(
                text.getLayoutBounds().getWidth() + WIDTH_MARGE_NOTE,
                text.getLayoutBounds().getHeight() + HEIGHT_MARGE_NOTE);       
        rectangle.setStroke(Color.web(IDiagramEditorController.COLOR_STROK_SHAPE));
        Pane p = new Pane();
        p.getChildren().add(rectangle);
        p.getChildren().add(text);
        setCenter(p); 
        
        controller = new NoteGraphicController(this, diagramController, note);
        controller.loadRelations();
        autosize();
    }
    
// REQUEST
    
    @Override
    public NoteShape getRectangle() {
        return rectangle;
    }

    @Override
    public NoteGraphicController getController() {
        return controller;
    }
    
    @Override
    public IEntityRelation getModel() {
        return controller.getModel();
    }

	
}
