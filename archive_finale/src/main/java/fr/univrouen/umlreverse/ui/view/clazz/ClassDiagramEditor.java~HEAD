/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.view.clazz;

import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.ui.view.common.ADiagramEditor;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;

public class ClassDiagramEditor extends ADiagramEditor {
    
// ATTRIBUTES
    /**
     * The controller of the editor.
     */
    private final ClassController controller;

// CONSTRUCTORS

    /**
     * Constructor of the class diagram editor.
     * @param classDiagram the model of the futur class diagram editor
     */
    public ClassDiagramEditor(IClassDiagram classDiagram) {
        super();
        controller = new ClassController(this, classDiagram);
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
     * @param t the type of the entity
     */
    public void createEntity(double x, double y, TypeEntity t) {
       controller.createEntity(x, y, t);
    }
}
