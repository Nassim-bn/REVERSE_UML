/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.common.elements;

import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphic;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;


public interface IEntityGraphicController {
    
     /**
     * Get context menu.
     * @return ctxMenu
     */
    ContextMenu getContextMenu();
    
    /**
     * Get MenuItem to edit entity.
     * @return editMI
     */
    MenuItem getEditMI();
    
    /**
     * Get MenuItem to remove entity.
     * @return removeMI
     */
    MenuItem getRemoveMI();
    
    /**
     * Get MenuItem to add relation.
     * @return Get item which permit to add relation.
     */
    MenuItem getAddRelationMI();
    

    /**
     * Get background color of AEntityGraphic.
     * @return backgroundColor
     */
    Color getBackgroundColor();

    /**
     * Get position of entity view.
     * @return 
     */
    Point2D getPosition();

    /**
     * Get property of position of entity.
     * @return 
     */
    ObjectProperty<Point2D> positionProperty();
    
    List<IRelationGraphic> getRelations();

// COMMANDS
    
    void addRelation(IRelationGraphic r);
    void removeRelation(IRelationGraphic r);

    /**
     * Set background color of note.
     * @param c 
     */
    void setBackgroundColor(Color c);
    
    /**
     * Set position to entity on his parent.
     * @param value 
     * @pre
     *      value != null
     */
    void setPosition(Point2D value);
    
    /**
     * Set translate position to entity on his parent.
     * @param value 
     * @pre
     *      value != null
     */
    void setTranslatePosition(Point2D value) ;

}
