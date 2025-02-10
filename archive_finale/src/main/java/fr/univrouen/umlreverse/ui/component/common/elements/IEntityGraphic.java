/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.common.elements;

import java.util.List;

import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphic;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public interface IEntityGraphic extends ISelectionableEntityGraphic {
    double WIDTH_ARROW_PANEL = 20;
    double HEIGHT_ARROW_PANEL = 10;
    
// REQUESTS
    /**
     * Used to add compatibility with Pane.
     * @return 
     */
    ReadOnlyDoubleProperty widthProperty();

    /**
     * Used to add compatibility with Pane.
     * @return 
     */
    ReadOnlyDoubleProperty heightProperty();

    /**
     * Get center point of entity graphic.
     * @return 
     */
    Point2D getCenterPoint();
    
    /**
     * Get background color of rectangle.
     * @return backgroundColor
     */
    Color getBackgroundColor();
    
    /**
     * Get ObjectProperty of position.
     * @return 
     */
    ObjectProperty<Point2D> positionProperty();

    /**
     * Gets the value of the property width.
     * @return 
     */
    double getWidth();
    
    /**
     * Gets the value of the property height.
     * @return 
     */
    double getHeight();
    
    /**
     * Get Controller.
     * @return 
     */
    IEntityGraphicController getController();
    
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
     * Set position of entity graphic and move it.
     * @param value 
     * @pre 
     *      value != null
     */
    void setPosition(Point2D value);
    
    /**
     * Set translate position of entity graphic and move it.
     * @param value 
     * @pre 
     *      value != null
     */
    void setTranslatePosition(Point2D value);
    
    /**
     * Sets the value of the property translateX.
     * @param x 
     */
    void setTranslateX(double x);
    
    /**
     * Sets the value of the property translateY.
     * @param y 
     */
    void setTranslateY(double y);

	double getMainHeight();

	double getMainWidth();
    
}