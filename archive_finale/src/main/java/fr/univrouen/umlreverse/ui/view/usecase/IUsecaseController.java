/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.view.usecase;


import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.usecase.IActor;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.INoteUsecase;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecase;
import fr.univrouen.umlreverse.ui.component.usecase.elements.ActorGraphic;
import fr.univrouen.umlreverse.ui.component.usecase.elements.NoteGraphic;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;

import java.util.Collection;
import java.util.Map;


public interface IUsecaseController extends IDiagramEditorController {
    
    /**
     * Get IDiagramUsecase
     * @return IClassDiagram
     */
    IUsecaseDiagram getDiagram();


    /**
     * Get the map that contain all actors with model at keys and graphic at value.
     * @return Map<IActor,ActorGraphic>
     */
    Map<IActor, ActorGraphic> getActors();
    /**
     * Get list of diagram's NoteGraphic
     * @return List
     */
    Collection<NoteGraphic> getNotes();

// COMMANDS

    /**
     * Create an Actor via a dialog.
     * @param x the x position of mice
     * @param y the y position of mice
     */
    void createActor(double x, double y);
    /**
     * Create relation.
     * @param note the model of the graphic note that will be created
     */
    void createRelation(IEntityRelation note);
    
    void createUsecase(double x, double y);
    
    /**
     * Remove note of diagram.
     * @param note : NoteGraphic
     */
    void removeNote(INoteUsecase note);
    
    /**
     * Remove the param relation to the list relations and then remove it from the draw.
     * @param relation
     *
     */
    void removeRelation(IRelation relation);
    
    /**
     * Remove note of diagram.
     * @param usecase
     */
    void removeUsecase(IUsecase usecase);

    /**
     * Remove actor of diagram.
     * @param act the actor to remove
     */
    void removeActor(IActor act);
}
