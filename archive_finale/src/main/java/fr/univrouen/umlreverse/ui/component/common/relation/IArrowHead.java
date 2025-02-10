package fr.univrouen.umlreverse.ui.component.common.relation;

import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.ui.component.common.relation.EntityPoint.Side;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

/**
 * Graphic arrowhead of uml diagram. 
 */
public interface IArrowHead {
// ATTRIBUTES
    /**
     * Default with of arrowhead.
     */
    double HEAD_WITH = 15;
    /**
     * Default height of arrowhead.
     */
    double HEAD_HEIGHT = 15;
    /**
     * Default width of polygon of arrowhead.
     */
    double POLY_WIDTH = 20;
    /**
     * Default line size of tail arrowhead.
     */
    double LINE_SIZE_TAIL = 5;
    
// REQUESTS 
   /**
    * 
    * @return circle on arrow tip.
    */
   Circle getHeadCircle(); 
   /**
    * 
    * @return point of arrow tip.
    */
   Point2D getHeadPoint(); 
   /**
    * 
    * @return all lines of arrowhead.
    */
   List<Line> getLines(); 
   /**
    * 
    * @return all shape of arrowhead.
    */
   List<Shape> getShapes();
   /**
    * 
    * @return the side of arrowhead relative to entity.
    */
   Side getSide();
   /**
    * 
    * @return tail point of arrow head.
    */
   Point2D getTailPoint();
   
   /**
    * 
    * @return Text.
    */
   Text getText();
   /**
    * 
    * @return return type of arrowhead.
    */
   TypeHeadArrow getType();
   /**
    * 
    * @return tail point property.
    */
   ObjectProperty<Point2D> tailPointProperty();
   
// COMMANDS  
   /**
    * Move getHeadPoint() to point and all shapes of arrowhead.
    * @param point 
    * @pre
    *   point != null
    */
   void moveHeadPoint(Point2D point);
   /**
    * Change color of arrowhead.
    * @param c 
    * @pre 
    *   c != null
    */
   void setColor(Color c);
   /**
    * Defines the array representing the lengths of the dash segments.
    * If d is less than 0 then the dash is clear.
    * @param d 
    */
   void setDashed(double d);
   /**
    * Change getSide() and redraw the lines and poligon of entity.
    * @param side 
    * @pre
    *   side != null
    * @post
    *   getSide == side
    */
   void setSide(Side side); 
   /**
    * Set point in tailPointProperty().
    * @param p 
    */
   void setTailPoint(Point2D p);
   
   /**
    * Set text in Text.
    * @param s 
    * @pre
    *       s != null
    */
   void setText(String s);
   /**
    * Set type of arrow head and makes visible the good lines of arrowhead.
    * @param type
    * @pre
    *   type != null
    * @post
    *   getText().getText() == s
    */
   void setType(TypeHeadArrow type);  
}
