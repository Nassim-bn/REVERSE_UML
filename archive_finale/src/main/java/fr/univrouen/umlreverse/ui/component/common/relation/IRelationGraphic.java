package fr.univrouen.umlreverse.ui.component.common.relation;

import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.ISelectionableEntityGraphic;
import java.util.List;
import java.util.Set;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;


/**
 * Graphic relation of uml diagram. It contains two ArrowHead, one
 * ArrowBody, name and one controller.
 */
public interface IRelationGraphic extends ISelectionableEntityGraphic {
    /**
     * Default radius of circle of relation.
     */
    double RADIUS_POINT = 5;
    /**
     * Default dash of relation.
     */
    double DASH_DEFAULT = 5;
    /**
     * Minimum size of lines.
     */
    double LINE_SIZE_MIN = 20;
    /**
     * Marge of texte relative to lines.
     */
    int MARGE_TEXT = 10;
    
// REQUESTS 
    /**
     * Get circles of arrowbody.
     * @return circles of arrowbody.
     */
    List<Circle> getArrowBodyCircles();
    
    /**
     * Get cardinality end.
     * @return cardinality end.
     */
    String getCardinalityEnd();
    
    /**
     * Get Text of cardinality end.
     * @return Text of cardinality end.
     */
    Text getCardinalityEndText(); 
    
    /**
     * Get cardinality start.
     * @return cardinality start.
     */
    String getCardinalityStart();
    
    /**
     * Get Text of cardinality start.
     * @return Text of cardinality start.
     */
    Text getCardinalityStartText();  
    
    /**
     * Get circle destination.
     * @return circle destination.
     */
    Circle getCircleDst(); 
    
    /**
     * Get circle source.
     * @return circle source.
     */
    Circle getCircleSrc(); 
    
    /**
     * Get color of relation.
     * @return color of relation.
     */
    Color getColorRelation();
    
    /**
     * Get color of text.
     * @return color of text.
     */
    Color getColorText();  
    
    /**
     * Get IEntityGraphic destination of relation.
     * @return IEntityGraphic destination of relation.
     */
    IEntityGraphic getEntityDst();  
    
    /**
     * Get IEntityGraphic source of relation.
     * @return IEntityGraphic source of relation.
     */
    IEntityGraphic getEntitySrc(); 
    
    /**
     * Get first line of relation.
     * @return first line of relation.
     */
    Line getFirstLine();
    
    /**
     * Get Lines of arrowhead.
     * @return Lines of arrowhead.
     */
    List<Line> getHeadLines();
    
    /**
     * Get name relation.
     * @return name relation.
     */
    String getName();
    
    /**
     * Get Text of name relation.
     * @return Text of name relation.
     */
    Text getNameText();  
    
    /**
     * Get all shape of relation.
     * @return All shape of relation.
     */
    Set<Shape> getShapes();
    
    /**
     * Get lines of arrow tail.
     * @return lines of arrow tail.
     */
    List<Line> getTailLines();
    
    /**
     * Get type of relation.
     * @return type of relation.
     */
    RelationTypeEnum getType();
    
// COMMANDS  
    /**
     * Calcul position of relation lines comparated with source and 
     * target entity.
     */
    void calculPointRelation();
    
    /**
     * Clear event of shapes.
     */
    void clear(); 
    /**
     * Draw getShapes() in group g and all shape added after.
     * @param g 
     * @pre
     *      g != null
     */
    void drawInGroup(Group g);
    
    /**
     * Move circle c in arrow body.
     * @param c
     * @param x
     * @param y 
     * @pre 
     *      c != null
     */
    void moveArrowBodyPoint(Circle c, double x, double y);
    
    /**
     * Set new position of arrowhead.
     * @param x
     * @param y 
     */
    void moveArrowHead(double x, double y);
    
    /**
     * Set new position of arrow tail.
     * @param x
     * @param y 
     */
    void moveArrowTail(double x, double y); 
    
    /**
     * Replace the name of relation compared with a point of 
     * middle position relation.
     */
    void nameTextAutoSize();

    /**
     * Set cardinality end.
     * @param cardinalityEnd 
     * @pre
     *      cardinalityEnd != null
     */
    void setCardinalityEnd(String cardinalityEnd);
    
    /**
     * Set cardinality start.
     * @param cardinalityStart
     * @pre
     *      cardinalityStart != null
     */
    void setCardinalityStart(String cardinalityStart);
    
    /**
     * Set color of relation.
     * @param colorRelation
     * @pre
     *      colorRelation != null
     */
    void setColorRelation(Color colorRelation);
    
    /**
     * Set color of shape relation.
     * @param c
     * @pre
     *      c != null
     */
    void setColorShapes(Color c);
    
    /**
     * Set color of text relation.
     * @param colorText
     * @pre
     *      colorText != null
     */
    void setColorText(Color colorText); 
    
    /**
     * Set context menu event on all line of relation. Add this event on all
     * future line of relation.
     * @param eventType
     * @param eventHandler 
     * @pre
     *      eventType != null && eventHandler != null
     */
    void setContextMenuEventOnArrow(EventType<ContextMenuEvent> eventType,
            final EventHandler<? super ContextMenuEvent> eventHandler);
    
    /**
     * Set EventHandler on all point of arrow body. Add this event on all
     * future point of relation.
     * @param pointDnd_Event 
     * @pre
     *      pointDnd_Event != null
     */
    void setDNDPointEvent(EventHandler<MouseEvent> pointDnd_Event);
    
    /**
     * Set name of relation and draw it.
     * @param name 
     */
    void setName(String name);
    
    /**
     * Set MouseEvent on all line of relation. Add this event on all
     * future line of relation.
     * @param eventType
     * @param eventHandler 
     * @pre
     *      eventType != null && eventHandler != null
     */
    void setMouseEventOnArrow(EventType<MouseEvent> eventType,
            final EventHandler<? super MouseEvent> eventHandler); 
    
    /**
     * Set type of relation and redraw relation.
     * @param type 
     * @pre
     *      type != null
     */
    void setType(RelationTypeEnum type);  
    
    /**
     * Set TypeHeadArrow of arrowhead and redraw.
     * @param head 
     * @pre
     *      head != null
     */
    void setTypeArrowHead(TypeHeadArrow head);
    
    /**
     * Set TypeHeadArrow of arrowtail and redraw.
     * @param tail 
     * @pre
     *      tail != null
     */
    void setTypeArrowTail(TypeHeadArrow tail);
    
    /**
     * Set TypeLineArrow of arrow body and redraw.
     * @param line 
     * @pre
     *      line != null
     */
    void setTypeLineArrow(TypeLineArrow line);
}
