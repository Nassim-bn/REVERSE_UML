package fr.univrouen.umlreverse.ui.component.common.relation;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;

/**
 * A point on graphic entity. It contains a point and side of point on entity.
 * These informations is contain in property that other object can obtain.
 */
public class EntityPoint {
// ATTRIBUTES
        private final ObjectProperty<Side> sideProperty;
        private final ObjectProperty<Point2D> pointProperty;
        
        /**
         * Initialise point ans side properties.
         */
        public EntityPoint() {
            pointProperty = new SimpleObjectProperty<>();
            sideProperty = new SimpleObjectProperty<>();
        }
        
        /**
         * @return point of entity.
         */
        public Point2D getPoint() {
            return pointProperty.get();
        }
        
        /**
         * 
         * @return point property of entity.
         */
        public final ObjectProperty<Point2D> pointProperty() {
            return pointProperty;
        }

        /**
         * 
         * @return side of entity.
         */
        public Side getSide() {
            return sideProperty.get();
        }
        
        /**
         * 
         * @return side property of entity.
         */
        public final ObjectProperty<Side> sideProperty() {
            return sideProperty;
        }
        
        /**
         * Set point in point property. 
         * @param x 
         */
        public void setPoint(Point2D x) {
            pointProperty.set(x);
        }

        /**
         * Set side in side property.
         * @param side 
         */
        public void setSide(Side side) {
            sideProperty.setValue(side);
        }
        
        /**
         * Set side and point in properties.
         * @param bE
         * @param x 
         */
        public void setData(Side bE, Point2D x) {
            setSide(bE);
            setPoint(x);
        }
       
    /**
     * Side of entity.
     */
    public enum Side {
        TOP, RIGHT, BOTTOM, LEFT
    }
}