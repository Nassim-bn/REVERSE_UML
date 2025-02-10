/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.common.relation;

import java.util.List;

import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.ARelationGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphicController;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.geometry.Point2D;

/**
 *
 * @author antho
 */
public class RelationGraphic extends ARelationGraphic {
    
// ATTRIBUTES
    /** The controller associated with this component. */
    private final IRelationGraphicController controller;
    
// CONSTRUCTORS
     public RelationGraphic(IDiagramEditorController diagramEditorController,
            IRelation relation,
            IEntityGraphic src, IEntityGraphic dst) {
        super(diagramEditorController);
        Contract.check(src != null, "src must not be null.");
        Contract.check(relation != null, "viewRelation must not be null.");
        Contract.check(dst != null, "dst must not be null.");
        
        controller = new RelationGraphicController(this, relation, diagramEditorController, src, dst);
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
  
// COMMANDS
    
    // TOOLS
    
    
    
    
}
