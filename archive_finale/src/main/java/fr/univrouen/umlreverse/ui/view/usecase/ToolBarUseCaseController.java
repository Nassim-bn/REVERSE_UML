/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.view.usecase;


import fr.univrouen.umlreverse.util.Contract;
import javafx.fxml.FXML;

public class ToolBarUseCaseController {
    
    // ATTRIBUTES
    /**
     * The class diagram that will correspond to this tool bar.
     */
    private UsecaseDiagramEditor diagram;
    
    /**
     * Set the current diagram to the parameter.
     * @param uc the class diagram editor that will be set as current diagram
     */
    public void setDiagram(UsecaseDiagramEditor uc) {
         Contract.check(uc != null, "uc must not be null.");
         diagram = uc;
    }
    
    
     /**
     * Create a new Actor.
     */
    @FXML
    public void createActor(){
          
        Contract.check(diagram != null, "diagram must be instantiated.");
         diagram.createActor(0, 0);
    
    }
    
    /**
     * Create a new UseCase.
     */
    @FXML
    public void createUseCase(){
    
         Contract.check(diagram != null, "diagram must be instantiated.");
         diagram.createUsecase(0, 0);
    
    }
    
    
    
     /**
     * Create a new note.
     */
    @FXML
    public void createNote(){
         Contract.check(diagram != null, "diagram must be instantiated.");
         diagram.createNote(0, 0);
     }
    
}
