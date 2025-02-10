/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.view.usecase;

import fr.univrouen.umlreverse.ui.view.common.ADiagramEditor;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;

public class UsecaseDiagramEditor extends ADiagramEditor {
	// ATTRIBUTES
    private final UsecaseController controller;

    // CONSTRUCTORS
    public UsecaseDiagramEditor(IUsecaseDiagram usecaseDiagram) {
        super();
        controller = new UsecaseController(this, usecaseDiagram);
    }
    
    // REQUEST
    @Override
    public IDiagramEditorController getController() {
        return controller;
    }
    
    // COMMANDES

    /**
     * Create an entity via the controller.
     * @param x the x position of the entity
     * @param y the y position of the entity
     */
    
    public void createUsecase(double x, double y){
        controller.createUsecase(x, y);
    }
    
    public void createActor(double x, double y){
        controller.createActor(x, y);
    }
}
