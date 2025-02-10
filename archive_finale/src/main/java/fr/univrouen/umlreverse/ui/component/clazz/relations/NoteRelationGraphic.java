package fr.univrouen.umlreverse.ui.component.clazz.relations;

import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphicController;
import fr.univrouen.umlreverse.ui.component.common.relation.ARelationGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphic;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.ISelectionableEntityGraphic;
import java.util.List;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.geometry.Point2D;
import fr.univrouen.umlreverse.model.diagram.clazz.view.INoteClass;

/**
 * A component representing a relation between a note and an entity.
 */
public class NoteRelationGraphic extends ARelationGraphic
		implements ISelectionableEntityGraphic, IRelationGraphic {
	// ATTRIBUTES
	/** The controller associated with this component. */
	private final IRelationGraphicController controller;
    
	// CONSTRUCTORS
    /**
     * 
     * @param viewRelation
     * @param src
     * @param dst 
     * @pre
     *      diagramEditorController != null 
     *      && entity != null
     *      && note != null
     *      && src != null
     *      && dst != null
     * 
     */
    public NoteRelationGraphic(IDiagramEditorController diagramEditorController,
            INoteClass note, IViewEntity entity,
            IEntityGraphic src, IEntityGraphic dst) {
        super(diagramEditorController);
        Contract.check(src != null, "src must not be null.");
        Contract.check(note != null, "viewRelation must not be null.");
        Contract.check(entity != null, "entity must not be null.");
        Contract.check(dst != null, "dst must not be null.");
        
        
        controller = new NoteRelationGraphicController(this, note, entity, diagramEditorController, src, dst);
        updateHead(controller.getTypeHead());
        updateTail(controller.getTypeTail());
        setTypeLineArrow(controller.getTypeLine());
        List<Point2D> points = controller.getPoints();
        if (points.isEmpty()) {
            calculPointRelation();
        } else {     
            load(points);
        }
        nameText.setText(controller.getName());
        arrowTail.setText(controller.getCardinalityStart());
        arrowHead.setText(controller.getCardinalityEnd());
    }
    
    // QUERIES
    @Override
    public IRelationGraphicController getController() {
        return controller;
    }

}
