package fr.univrouen.umlreverse.ui.component.common.relation;

import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphic;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public interface IRelationGraphicController {
    /**
     * 
     * @return cardinality end.
     */
    String getCardinalityEnd();
    
    /**
     * 
     * @return cardinality start.
     */
    String getCardinalityStart();
    
    /**
     * 
     * @return color of relation.
     */
    Color getColorRelation();
    
    /**
     * 
     * @return clor of text.
     */
    Color getColorText();
    
    /**
     * 
     * @return entity destionation of relation.
     */
    IEntityGraphic getGEntityDst();
    
    /**
     * 
     * @return entity source of relation.
     */
    IEntityGraphic getGEntitySrc(); 
    
    /**
     * 
     * @return name of relation.
     */
    String getName();
    
    /**
     * 
     * @return points of body relation.
     */
    List<Point2D> getPoints();
    
    /**
     * 
     * @return return RelationTypeEnum of relation.
     */
    RelationTypeEnum getType();
    
    /**
     * 
     * @return TypeHeadArrow of arrowhead.
     */
    TypeHeadArrow getTypeHead(); 
    
    /**
     * 
     * @return TypeLineArrow of lines relation.
     */
    TypeLineArrow getTypeLine();
    
    /**
     * 
     * @return TypeHeadArrow of tail relation.
     */
    TypeHeadArrow getTypeTail();

// COMMANDS
    
    /**
     * Clear relation.
     */
    void clear();
    
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
     * Set color relation.
     * @param colorRelation 
     * @pre
     *      colorRelation != null
     */
    void setColorRelation(Color colorRelation);
    
    /**
     * Set color text relation.
     * @param colorText 
     * @pre
     *      colorText != null
     */
    void setColorText(Color colorText);
    
    /**
     * Set name of relation.
     * @param name 
     */
    void setName(String name);
    
    /**
     * Set type of relation.
     * @param type 
     * @pre 
     *      type != null
     */
    void setType(RelationTypeEnum type);
}
