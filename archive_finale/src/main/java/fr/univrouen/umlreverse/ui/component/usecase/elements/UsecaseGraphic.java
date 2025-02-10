/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.usecase.elements;

import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecase;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityTextGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityTextGraphicController;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.usecase.IUsecaseController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author antho
 */
public class UsecaseGraphic extends AEntityTextGraphic implements IUsecaseGraphic {
    
// ATTRIBUTES
    private final Ellipse ellipse;
    private final UsecaseGraphicController controller;
    
// CONSTRUCTORS
    public UsecaseGraphic(IUsecaseController diagramController, IUsecase usecase) {
        super(diagramController);
        Contract.check(diagramController != null, "L'argument diagramController "
                + "ne doit pas être nul.");
        Text text = getText();
        text.setTextOrigin(VPos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);
        ellipse = new Ellipse();
        ellipse.centerXProperty().bind(ellipse.radiusXProperty());
        ellipse.centerYProperty().bind(ellipse.radiusYProperty());
        ellipse.radiusXProperty().addListener((o, oldValue, newValue) -> {
        	text.setX(ellipse.getRadiusX() -
        			text.getBoundsInLocal().getWidth() / 2);
        });
        text.yProperty().bind(ellipse.radiusYProperty());
        ellipse.setStroke(Color.web(IDiagramEditorController.COLOR_STROK_SHAPE));
        Pane p = new Pane();
        p.getChildren().add(ellipse);
        p.getChildren().add(text);
        setCenter(p); 
        
        controller = new UsecaseGraphicController(this, diagramController, usecase);
        autosize();
    }
    
// REQUEST
    
    @Override
    public IEntityTextGraphicController getController() {
        return controller;
    }
    
    @Override
    public Ellipse getEllipse() {
        return ellipse;
    }
    
    @Override
    public IEntityRelation getModel() {
        return controller.getModel();
    }
}
