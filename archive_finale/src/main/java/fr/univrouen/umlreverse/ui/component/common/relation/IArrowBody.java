package fr.univrouen.umlreverse.ui.component.common.relation;

import java.beans.PropertyChangeListener;
import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 * Graphic arrowbody of uml diagram.
 */
public interface IArrowBody {
// REQUESTS
    /**
     * 
     * @return circles of arrowbody.
     */
    List<Circle> getCircles();
    /**
     * 
     * @return first line of arrowbody.
     */
    Line getFisrtLine(); 
    /**
     * 
     * @return all lines of arrowbody.
     */
    List<Line> getLines(); 
    /**
     * 
     * @return all shapes of arrowbody.
     */
    List<Shape> getShapes();    
    
// COMMANDS
    /**
     * Add point in end arrowbody. The last line are connected on this point.
     * A new line is created.
     * If Point move, the 2 last lines move too.
     * @param p 
     * @pre
     *      p != null
     */
    void addPoint(Point2D p); 
    
	/**
	 * Add point in position. The line connected move too.
	 * 
	 * @param p
	 * @pre p != null
	 */
    public void addPointInPosition(Point2D p, Point2D src, Point2D dest);
    
    /**
	 * Remove point. The line connected move too.
	 * 
	 * @param p
	 * @pre p != null
	 */
    void removePoint(Circle c);
    
	/**
	 * Add point in end arrowbody. The last line are connected on this point. A new
	 * line is created. If Point move, the 2 last lines move too.
	 * 
	 * @param p
	 * @pre p != null
	 */
        
    
    /**
     * Execute clearAll() and initizialise first line, start and end point with 
     * property.
     */
    void clear();
    /**
     * Clear all lines with all property.
     */
    void clearAll();
    /**
     * Move end point. The line connected move too.
     * @param p 
     * @pre
     *      p != null
     */
    void moveEndPoint(Point2D p);  
    /**
     * Move point. The lines connected move too.
     * @param c 
     * @param x 
     * @param y 
     * @pre
     *      c != null
     */
    void movePoint(Circle c, double x, double y);  
    /**
     * Move start point. The line connected move too.
     * @param p 
     * @pre
     *      p != null
     */
    void moveStartPoint(Point2D p);
    /**
     * Change color of lines.
     * @param c 
     * @pre 
     *      c != null     
     */
    void setColor(Color c);
    /**
     * Defines the array representing the lengths of the dash segments.
     * If dash is less than 0 then the dash is clear.
     * @param dash 
     */
    void setDashed(double dash);
    
    /**
     * Set EventHandler<MouseEvent> on all point (circle) build after.
     * @param pointDnd_Event 
     * @pre
     *      pointDnd_Event != null
     */
    void setDNDPointEvent(EventHandler<MouseEvent> pointDnd_Event);
	void addPropertyChangeListener(String propCircle, PropertyChangeListener propertyChangeListener);
	void removePropertyChangeListener(String propCircle,PropertyChangeListener propertyChangeListener);
	void firePropertyChange(String propCircle, Object oldValue, Object newValue);
}
