package fr.univrouen.umlreverse.ui.component.common.relation;

import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.ui.component.common.relation.EntityPoint.Side;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

/**
 * Implements IArrowHead. It contains 6 line, 1 polygon, 1 Text.
 * These shapes are visible by type.
 */
public class ArrowHead implements IArrowHead {
// ATTRIBUTES 
    private static final int HEAD_ARROW_INDEX_1 = 0;
    private static final int HEAD_ARROW_INDEX_2 = 1;
    private static final int HEAD_ARROW_INDEX_3 = 2;
    private static final int HEAD_ARROW_INDEX_4 = 3;
    private static final int HEAD_ARROW_INDEX_5 = 4;
    private static final int HEAD_ARROW_INDEX_6 = 5;
    
    private final List<Line> lines;
    private final Polygon polygon;
    private final Text text;
    private final Circle circle;
    private final ObjectProperty<Point2D> pointProperty;
    private final ObjectProperty<Side> sideProperty;
    private final ObjectProperty<TypeHeadArrow> typeProperty;
    private final ObjectProperty<Point2D> tailPointProperty;
    private Color color;
    
// CONSTRUCTORS
   /**
    * Initialize arrowhead.
    * @param type
    * @param p
    * @param b
    * @param c
    * @pre
    *   type != null
    *   p != null
    *   b != null
    *   c != null
    * @post
    *   getType() == type &&
    *   getLines() != null && getLines().size == 6 &&
    *   getSide() == v
    */
   public ArrowHead(TypeHeadArrow type, Point2D p, Side b, Color c) {
       Contract.check(type != null, "Le type ne doit pas être nul.");
       Contract.check(p != null, "Le point ne doit pas être nul.");
       Contract.check(b != null, "Le coté ne doit pas être nul.");
       Contract.check(c != null, "La couleur ne doit pas être nul.");
       color = c;
       lines = new ArrayList<>(); {
            for (int i = 0; i <= HEAD_ARROW_INDEX_6; ++i) {
                Line l = new Line();
                lines.add(l);
            }
       }
       polygon = new Polygon(p.getX(), p.getY(), 
                POLY_WIDTH / 2, POLY_WIDTH / 2, 
                POLY_WIDTH, 0, 
                POLY_WIDTH / 2, - POLY_WIDTH / 2);
       polygon.setStroke(Color.web(IDiagramEditorController.COLOR_DEFAULT_RELATION));
       text = new Text();
       circle = new Circle(p.getX(), p.getY(), IRelationGraphic.RADIUS_POINT, Color.BLACK);
       circle.setVisible(false);
       pointProperty = new SimpleObjectProperty<>(p);
       typeProperty = new SimpleObjectProperty<>(type);
       sideProperty =  new SimpleObjectProperty<>(b);
       tailPointProperty = new SimpleObjectProperty<>(new Point2D(0, 0));
       createController();
       changePoint(new Point2D(0, 0), p);
       changeType(type);
       changeSide(b);
   }
    
// REQUESTS
   
   @Override
   public List<Shape> getShapes() {
       List<Shape> list = new ArrayList<>(lines);
       list.add(polygon);
       list.add(circle);
       list.add(text);
       return list;
   }
   
   @Override
   public List<Line> getLines() {
       return lines;
   }
   
   @Override
   public Point2D getHeadPoint() {
       return pointProperty.get();
   }
   
   @Override
   public Circle getHeadCircle() {
       return circle;
   }
   
   @Override
   public Point2D getTailPoint() {
       return tailPointProperty.get();
   }
   
   @Override
   public ObjectProperty<Point2D> tailPointProperty() {
       return tailPointProperty;
   }
   
   @Override
   public TypeHeadArrow getType() {
       return typeProperty.get();
   }
   
   @Override
   public Side getSide() {
       return sideProperty.get();
   }
   
   @Override
   public Text getText() {
       return text;
   }
   
// COMMANDS
   @Override
   public void setType(TypeHeadArrow type) {
       Contract.check(type != null, "Le type ne doit pas être nul.");
        typeProperty.setValue(type);
   }
   
   @Override
   public void moveHeadPoint(Point2D point) {
       Contract.check(point != null, "Le point ne doit pas être nul.");
        pointProperty.setValue(point);
   }
   
   @Override
   public void setSide(Side side) {
       Contract.check(side != null, "Le side ne doit pas être nul.");
       sideProperty.setValue(side);
   }
   
   @Override
   public void setTailPoint(Point2D p) {
       Contract.check(p != null, "p ne doit pas être nul.");
       tailPointProperty.set(p);
   }    
   
   @Override
   public void setDashed(double d) {
       if (d > 0) {
           lines.get(HEAD_ARROW_INDEX_4).getStrokeDashArray().add(d);
           lines.get(HEAD_ARROW_INDEX_5).getStrokeDashArray().add(d);
           lines.get(HEAD_ARROW_INDEX_6).getStrokeDashArray().add(d);
       } else {
           lines.get(HEAD_ARROW_INDEX_4).getStrokeDashArray().clear();
           lines.get(HEAD_ARROW_INDEX_5).getStrokeDashArray().clear();
           lines.get(HEAD_ARROW_INDEX_6).getStrokeDashArray().clear();
       }
   }
   
   @Override
   public void setColor(Color c) {
       Contract.check(c != null, "L'argument c ne doit pas être nul.");
       color = c;
       lines.stream().forEach(l -> {
           l.setStroke(c);
       });
       polygon.setStroke(c);
       if (getType() == TypeHeadArrow.Aggregation) {
           polygon.setFill(Color.WHITE);
       } else if (getType() == TypeHeadArrow.Composition) {
           polygon.setFill(c);
       }
   }
   
   @Override
   public void setText(String s) {
       Contract.check(s != null, "s must not be null.");
       text.setText(s);
   }
    
// PRIVATE
   
    private void createController() {
        pointProperty.addListener(new ChangeListener<Point2D>() {
            @Override
            public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
                changePoint(oldValue, newValue);
            }
        });
        typeProperty.addListener(new ChangeListener<TypeHeadArrow>() {
            @Override
            public void changed(ObservableValue<? extends TypeHeadArrow> observable, TypeHeadArrow oldValue, TypeHeadArrow newValue) {
                changeType(newValue);
            }
        });
        
        sideProperty.addListener(new ChangeListener<Side>() {
            @Override
            public void changed(ObservableValue<? extends Side> observable, Side oldValue, Side newValue) {
                changeSide(newValue);
            }
        });
    }
    
    private void changePoint(Point2D oldP, Point2D newP) {
        double x = newP.getX() - oldP.getX();
        double y = newP.getY() - oldP.getY();
        lines.stream().map((l) -> {
            l.setStartX(l.getStartX() + x);
            return l;
        }).map((l) -> {
            l.setStartY(l.getStartY() + y);
            return l;
        }).map((l) -> {
            l.setEndX(l.getEndX() + x);
            return l;
        }).forEach((l) -> {
            l.setEndY(l.getEndY() + y);
        });
        Point2D tail = getTailPoint();
        setTailPoint(new Point2D(tail.getX() + x, tail.getY() + y));
        
        polygon.setLayoutX(polygon.getLayoutX() + x);
        polygon.setLayoutY(polygon.getLayoutY() + y);
        
        text.setLayoutX(text.getLayoutX() + x);
        text.setLayoutY(text.getLayoutY() + y);
        
        circle.setCenterX(newP.getX());
        circle.setCenterY(newP.getY());
    }
    
    private void changeType(TypeHeadArrow t) {
        if (t == TypeHeadArrow.None) {
            lines.get(HEAD_ARROW_INDEX_1).setVisible(false);
            lines.get(HEAD_ARROW_INDEX_2).setVisible(false);
            lines.get(HEAD_ARROW_INDEX_3).setVisible(false);
            lines.get(HEAD_ARROW_INDEX_4).setVisible(true);
            lines.get(HEAD_ARROW_INDEX_5).setVisible(true);
            lines.get(HEAD_ARROW_INDEX_6).setVisible(true);
            
            polygon.setVisible(false);
        }
        if (t == TypeHeadArrow.EmptyHead) {
            lines.get(HEAD_ARROW_INDEX_1).setVisible(true);
            lines.get(HEAD_ARROW_INDEX_2).setVisible(true);
            lines.get(HEAD_ARROW_INDEX_3).setVisible(false);
            lines.get(HEAD_ARROW_INDEX_4).setVisible(true);
            lines.get(HEAD_ARROW_INDEX_5).setVisible(true);
            lines.get(HEAD_ARROW_INDEX_6).setVisible(true);
            
            polygon.setVisible(false);
        }
        if (t == TypeHeadArrow.FullHead) {
            lines.get(HEAD_ARROW_INDEX_1).setVisible(true);
            lines.get(HEAD_ARROW_INDEX_2).setVisible(true);
            lines.get(HEAD_ARROW_INDEX_3).setVisible(true);
            lines.get(HEAD_ARROW_INDEX_4).setVisible(false);
            lines.get(HEAD_ARROW_INDEX_5).setVisible(false);
            lines.get(HEAD_ARROW_INDEX_6).setVisible(true);
            
            polygon.setVisible(false);
        }
        if (t == TypeHeadArrow.Aggregation) {
            lines.get(HEAD_ARROW_INDEX_1).setVisible(false);
            lines.get(HEAD_ARROW_INDEX_2).setVisible(false);
            lines.get(HEAD_ARROW_INDEX_3).setVisible(false);
            lines.get(HEAD_ARROW_INDEX_4).setVisible(false);
            lines.get(HEAD_ARROW_INDEX_5).setVisible(true);
            lines.get(HEAD_ARROW_INDEX_6).setVisible(true);
            
            polygon.setVisible(true);
            polygon.setStroke(color);
            polygon.setFill(Color.WHITE);   
        }
        if (t == TypeHeadArrow.Composition) {
            lines.get(HEAD_ARROW_INDEX_1).setVisible(false);
            lines.get(HEAD_ARROW_INDEX_2).setVisible(false);
            lines.get(HEAD_ARROW_INDEX_3).setVisible(false);
            lines.get(HEAD_ARROW_INDEX_4).setVisible(false);
            lines.get(HEAD_ARROW_INDEX_5).setVisible(true);
            lines.get(HEAD_ARROW_INDEX_6).setVisible(true);
            
            polygon.setVisible(true);
            polygon.setStroke(color);
            polygon.setFill(color);
        }
    }
    
    private void changeSide(Side b) {
        Point2D pointDst = getHeadPoint();
    
        double xDst = pointDst.getX();
        double yDst = pointDst.getY();
        
        lines.get(HEAD_ARROW_INDEX_1).setStartX(xDst);
        lines.get(HEAD_ARROW_INDEX_1).setStartY(yDst);
              
        lines.get(HEAD_ARROW_INDEX_2).setStartX(xDst);
        lines.get(HEAD_ARROW_INDEX_2).setStartY(yDst);
        
        lines.get(HEAD_ARROW_INDEX_4).setStartX(xDst);
        lines.get(HEAD_ARROW_INDEX_4).setStartY(yDst); 
        
        switch (b) {
            case TOP :                     
                lines.get(HEAD_ARROW_INDEX_1).setEndX(xDst - HEAD_WITH / 2);
                lines.get(HEAD_ARROW_INDEX_1).setEndY(yDst - HEAD_HEIGHT);
                
                lines.get(HEAD_ARROW_INDEX_2).setEndX(xDst  + HEAD_WITH / 2);
                lines.get(HEAD_ARROW_INDEX_2).setEndY(yDst - HEAD_HEIGHT);
                
                lines.get(HEAD_ARROW_INDEX_3).setStartX(xDst - HEAD_WITH / 2);
                lines.get(HEAD_ARROW_INDEX_3).setStartY(yDst - HEAD_HEIGHT);     
                lines.get(HEAD_ARROW_INDEX_3).setEndX(xDst  + HEAD_WITH / 2);
                lines.get(HEAD_ARROW_INDEX_3).setEndY(yDst - HEAD_HEIGHT);

                lines.get(HEAD_ARROW_INDEX_4).setEndX(xDst);
                lines.get(HEAD_ARROW_INDEX_4).setEndY(yDst - POLY_WIDTH);
                
                lines.get(HEAD_ARROW_INDEX_5).setStartX(xDst);
                lines.get(HEAD_ARROW_INDEX_5).setStartY(yDst - POLY_WIDTH);     
                lines.get(HEAD_ARROW_INDEX_5).setEndX(xDst);
                lines.get(HEAD_ARROW_INDEX_5).setEndY(yDst - HEAD_HEIGHT);
                
                lines.get(HEAD_ARROW_INDEX_6).setStartX(xDst);
                lines.get(HEAD_ARROW_INDEX_6).setStartY(yDst - HEAD_HEIGHT);     
                lines.get(HEAD_ARROW_INDEX_6).setEndX(xDst);
                lines.get(HEAD_ARROW_INDEX_6).setEndY(yDst - HEAD_HEIGHT - LINE_SIZE_TAIL);
                
                setTailPoint(new Point2D(xDst, yDst - HEAD_HEIGHT - LINE_SIZE_TAIL));
                
                polygon.setLayoutX(xDst - POLY_WIDTH / 2);
                polygon.setLayoutY(yDst - POLY_WIDTH / 2);
                
                text.setLayoutX(xDst + HEAD_WITH);
                text.setLayoutY(yDst - HEAD_HEIGHT);
                break;
                
            case RIGHT : 
                lines.get(HEAD_ARROW_INDEX_1).setEndX(xDst + HEAD_HEIGHT);
                lines.get(HEAD_ARROW_INDEX_1).setEndY(yDst - HEAD_WITH / 2);                            
                lines.get(HEAD_ARROW_INDEX_2).setEndX(xDst + HEAD_HEIGHT);
                lines.get(HEAD_ARROW_INDEX_2).setEndY(yDst + HEAD_WITH / 2);
                
                lines.get(HEAD_ARROW_INDEX_3).setStartX(xDst + HEAD_HEIGHT);
                lines.get(HEAD_ARROW_INDEX_3).setStartY(yDst - HEAD_WITH / 2);                            
                lines.get(HEAD_ARROW_INDEX_3).setEndX(xDst + HEAD_HEIGHT);
                lines.get(HEAD_ARROW_INDEX_3).setEndY(yDst + HEAD_WITH / 2);
                   
                lines.get(HEAD_ARROW_INDEX_4).setEndX(xDst + POLY_WIDTH);
                lines.get(HEAD_ARROW_INDEX_4).setEndY(yDst);
                
                lines.get(HEAD_ARROW_INDEX_5).setStartX(xDst + POLY_WIDTH);
                lines.get(HEAD_ARROW_INDEX_5).setStartY(yDst);     
                lines.get(HEAD_ARROW_INDEX_5).setEndX(xDst + HEAD_HEIGHT);
                lines.get(HEAD_ARROW_INDEX_5).setEndY(yDst);
                
                lines.get(HEAD_ARROW_INDEX_6).setStartX(xDst + HEAD_HEIGHT);
                lines.get(HEAD_ARROW_INDEX_6).setStartY(yDst);     
                lines.get(HEAD_ARROW_INDEX_6).setEndX(xDst + HEAD_HEIGHT + LINE_SIZE_TAIL);
                lines.get(HEAD_ARROW_INDEX_6).setEndY(yDst);
                
                setTailPoint(new Point2D(xDst + HEAD_HEIGHT + LINE_SIZE_TAIL, yDst));
                
                polygon.setLayoutX(xDst);
                polygon.setLayoutY(yDst);
                
                text.setLayoutX(xDst + HEAD_HEIGHT);
                text.setLayoutY(yDst - HEAD_WITH);
                break;
            case BOTTOM : 
                lines.get(HEAD_ARROW_INDEX_1).setEndX(xDst - HEAD_WITH / 2);
                lines.get(HEAD_ARROW_INDEX_1).setEndY(yDst + HEAD_HEIGHT);               
                lines.get(HEAD_ARROW_INDEX_2).setEndX(xDst  + HEAD_WITH / 2);
                lines.get(HEAD_ARROW_INDEX_2).setEndY(yDst + HEAD_HEIGHT);
                
                lines.get(HEAD_ARROW_INDEX_3).setStartX(xDst - HEAD_WITH / 2);
                lines.get(HEAD_ARROW_INDEX_3).setStartY(yDst + HEAD_HEIGHT);               
                lines.get(HEAD_ARROW_INDEX_3).setEndX(xDst  + HEAD_WITH / 2);
                lines.get(HEAD_ARROW_INDEX_3).setEndY(yDst + HEAD_HEIGHT);  
       
                lines.get(HEAD_ARROW_INDEX_4).setEndX(xDst);
                lines.get(HEAD_ARROW_INDEX_4).setEndY(yDst + POLY_WIDTH);
                
                lines.get(HEAD_ARROW_INDEX_5).setStartX(xDst);
                lines.get(HEAD_ARROW_INDEX_5).setStartY(yDst + POLY_WIDTH);     
                lines.get(HEAD_ARROW_INDEX_5).setEndX(xDst);
                lines.get(HEAD_ARROW_INDEX_5).setEndY(yDst + HEAD_HEIGHT);
                
                lines.get(HEAD_ARROW_INDEX_6).setStartX(xDst);
                lines.get(HEAD_ARROW_INDEX_6).setStartY(yDst + HEAD_HEIGHT);     
                lines.get(HEAD_ARROW_INDEX_6).setEndX(xDst);
                lines.get(HEAD_ARROW_INDEX_6).setEndY(yDst + HEAD_HEIGHT + LINE_SIZE_TAIL);
                
                setTailPoint(new Point2D(xDst, yDst + HEAD_HEIGHT + LINE_SIZE_TAIL));
                
                polygon.setLayoutX(xDst - POLY_WIDTH / 2);
                polygon.setLayoutY(yDst + POLY_WIDTH / 2);
                
                text.setLayoutX(xDst + HEAD_WITH);
                text.setLayoutY(yDst + HEAD_HEIGHT);
                break;
            case LEFT : 
                lines.get(HEAD_ARROW_INDEX_1).setEndX(xDst - HEAD_HEIGHT);
                lines.get(HEAD_ARROW_INDEX_1).setEndY(yDst - HEAD_WITH / 2);               
                lines.get(HEAD_ARROW_INDEX_2).setEndX(xDst - HEAD_HEIGHT);
                lines.get(HEAD_ARROW_INDEX_2).setEndY(yDst + HEAD_WITH / 2);
                
                lines.get(HEAD_ARROW_INDEX_3).setStartX(xDst - HEAD_HEIGHT);
                lines.get(HEAD_ARROW_INDEX_3).setStartY(yDst - HEAD_WITH / 2);               
                lines.get(HEAD_ARROW_INDEX_3).setEndX(xDst - HEAD_HEIGHT);
                lines.get(HEAD_ARROW_INDEX_3).setEndY(yDst + HEAD_WITH / 2);
                     
                lines.get(HEAD_ARROW_INDEX_4).setEndX(xDst - POLY_WIDTH);
                lines.get(HEAD_ARROW_INDEX_4).setEndY(yDst);
                
                lines.get(HEAD_ARROW_INDEX_5).setStartX(xDst - POLY_WIDTH);
                lines.get(HEAD_ARROW_INDEX_5).setStartY(yDst);     
                lines.get(HEAD_ARROW_INDEX_5).setEndX(xDst - HEAD_HEIGHT);
                lines.get(HEAD_ARROW_INDEX_5).setEndY(yDst);
                
                lines.get(HEAD_ARROW_INDEX_6).setStartX(xDst - HEAD_HEIGHT);
                lines.get(HEAD_ARROW_INDEX_6).setStartY(yDst);     
                lines.get(HEAD_ARROW_INDEX_6).setEndX(xDst - HEAD_HEIGHT - LINE_SIZE_TAIL);
                lines.get(HEAD_ARROW_INDEX_6).setEndY(yDst);
                
                setTailPoint(new Point2D(xDst - HEAD_HEIGHT - LINE_SIZE_TAIL, yDst)); 
                
                polygon.setLayoutX(xDst - POLY_WIDTH);
                polygon.setLayoutY(yDst);
                
                text.setLayoutX(xDst - text.getLayoutBounds().getWidth() - HEAD_HEIGHT);
                text.setLayoutY(yDst - HEAD_WITH);
                break;
        }
    }
}
