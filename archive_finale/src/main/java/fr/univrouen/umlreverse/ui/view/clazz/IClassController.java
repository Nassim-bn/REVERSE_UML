/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.view.clazz;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewRelation;
import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.ui.component.clazz.elements.NoteGraphic;
import fr.univrouen.umlreverse.ui.component.clazz.elements.ObjectEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphic;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;

import java.util.Collection;
import java.util.Map;

import fr.univrouen.umlreverse.model.diagram.clazz.view.INoteClass;


public interface IClassController extends IDiagramEditorController {

// REQUESTS
    /**
     * Get IClassDiagram
     * @return IClassDiagram
     */
    IClassDiagram getDiagram();

    /**
     * Get an unmodifiableList of the OEG in this diagram.
     * @return an unmodifiableList of the OEG in this diagram.
     */
    Collection<ObjectEntityGraphic> getObjectEntityGraphics();

    /**
     * Getter of the entity graphic that correspond to the parameter.
     * @param entity the model of the entity that you want to find
     * @return ObjectEntityGraphic
     */
    ObjectEntityGraphic getOEG(IViewEntity entity);
    
    /**
     * Get list of diagram's NoteGraphic
     * @return List
     */
    Collection<NoteGraphic> getNotes();

// COMMANDS

   
    /**
     * Remove package in canvas and model.
     * @pre
     *   packageG != null
     * @post
     *   Package is removed in canvas and model.
    public void removePackage(PackageGraphic packageG);
    */
    
    /**
     * Remove the param relation to the list relations and then remove it from the draw.
     * @param relation
     *
     */
    void removeRelation(IViewRelation relation);

    /**
     * Remove the relation graphic that correspond to the parameter.
     * @param r the model of the relation that you want to remove
     */
    void removeRelationGraphic(IRelationGraphic r);
    /**
     * Remove ObjectEntityGraphic in canvas and model.
     * @param oEG the ObjectEntityGraphic.
     * @pre
     *   oEG != null
     * @post
     *   oEG is removed in canvas and model.
     */
    void removeObjectEntity(ObjectEntityGraphic oEG);

    /**
     * Remove ObjectEntityGraphic in canvas and model.
     * @param oEG the ObjectEntityGraphic.
     * @pre
     *   oEG != null
     * @post
     *   oEG is removed in canvas.
     */
    void hideObjectEntity(ObjectEntityGraphic oEG);
    void createEntity(double x, double y, TypeEntity t);
    
    /**
     * Remove note of diagram.
     * @param note : NoteGraphic
     */

    void removeNote(INoteClass note);
    
    /**
     * Create a note for a relation.
     * @param note the model of the graphic note that will be created
     */
    void createNoteRelation(INoteClass note);

    /**
     * Create a relation with an entity.
     * @param entity the entity that will be in the relation
     */
    void createEntityRelation(IViewEntity entity);

	Map<IObjectEntity, IViewEntity> getObjectEntities();
}
