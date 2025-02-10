package fr.univrouen.umlreverse.ui.component.clazz.relations;

import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphicController;
import fr.univrouen.umlreverse.ui.component.common.relation.ARelationGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphic;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewRelation;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.ISelectionableEntityGraphic;
import java.util.List;
import fr.univrouen.umlreverse.util.Contract;
import javafx.geometry.Point2D;
import fr.univrouen.umlreverse.ui.view.clazz.IClassController;

/**
 * A component that represents a relation.
 */
public class RelationGraphic extends ARelationGraphic
		implements ISelectionableEntityGraphic, IRelationGraphic {
// ATTRIBUTES
    /** The controller associated with this component. */
    private final RelationGraphicController controller;
      
// CONSTRUCTORS
    /**
     * 
     * @param diagramEditorController
     * @param viewRelation
     * @param src
     * @param dst 
     * @pre
     *      diagramEditorController != null 
     *      && viewRelation != null 
     *      && src != null
     *      && dst != null
     * 
     */
    public RelationGraphic(IClassController diagramEditorController,
            IViewRelation viewRelation,
            IEntityGraphic src, IEntityGraphic dst) {
        super(diagramEditorController);
        Contract.check(viewRelation != null, "viewRelation must not be null.");
        Contract.check(src != null, "src must not be null.");
        Contract.check(dst != null, "dst must not be null.");
        
        controller = new RelationGraphicController(this, viewRelation, diagramEditorController, src, dst);
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
