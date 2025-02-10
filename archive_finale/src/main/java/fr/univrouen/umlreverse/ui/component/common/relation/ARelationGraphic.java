package fr.univrouen.umlreverse.ui.component.common.relation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewRelation;
import fr.univrouen.umlreverse.model.diagram.common.Direction;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.ISelectionableEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.EntityPoint.Side;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.ui.component.usecase.elements.IUsecaseGraphic;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;

/**
 * Implements IRelationGraphic and ISelectionableEntityGraphic.
 */
public abstract class ARelationGraphic implements ISelectionableEntityGraphic, 
        IRelationGraphic {
 
// ATTRIBUTES
    private boolean isSelected;
    private final IDiagramEditorController diagramEditorController;

    private Group group;
    protected final IArrowHead arrowTail;
    protected final IArrowBody arrowBody;
    protected final IArrowHead arrowHead; 
    protected final EntityPoint sideSrc;
    protected final EntityPoint sideDst; 
    protected final Text nameText; 
    private final ObjectProperty<TypeHeadArrow> headProperty;
    private final ObjectProperty<TypeHeadArrow> tailProperty;
    private EventType<ContextMenuEvent> menuEvent;
    private EventHandler<? super ContextMenuEvent> menuEventHandler;   
    private final Map<EventType<MouseEvent>, EventHandler<? super MouseEvent>> events;
    
    private PropertyChangeSupport support;
// CONSTRUCTORS
    /**
     * 
     * @param diagramEditorController
     * @pre
     *      diagramEditorController != null 
     */
    protected ARelationGraphic(IDiagramEditorController diagramEditorController) {
        Contract.check(diagramEditorController != null, 
                "diagramEditorController must not be null.");
        this.diagramEditorController = diagramEditorController;
        sideSrc = new EntityPoint();
        sideDst = new EntityPoint();      
        Color c = Color.web(IDiagramEditorController.COLOR_DEFAULT_RELATION);
        arrowTail = new ArrowHead(TypeHeadArrow.None, Point2D.ZERO, Side.TOP, c);
        arrowHead = new ArrowHead(TypeHeadArrow.None, Point2D.ZERO, Side.TOP, c);
        arrowBody = new ArrowBody(0, 0, 0, 0, arrowTail.tailPointProperty(), arrowHead.tailPointProperty());
        arrowBody.setColor(c);     
        nameText = new Text();       
        events = new HashMap<>();
        headProperty = new SimpleObjectProperty<>();
        tailProperty = new SimpleObjectProperty<>();
        createController();
    }
       

// REQUESTS

//********************************************************************************************//    
public void setGroup(Group group) {
        Group oldGroup = this.group;
        this.group = group;
        support.firePropertyChange("group", oldGroup, group);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
//********************************************************************************************//  
    
    //obtenir le controller de la relation
    @Override
    public List<Circle> getArrowBodyCircles() {
        Contract.check(arrowBody != null, "arrowBody must not be null.");
        return arrowBody.getCircles();
    }
    
    //obtenir la cardinalité de la fin de la relation
    @Override
    public String getCardinalityEnd() {
        return getController().getCardinalityEnd();
    }
    
    //obtenir la cardinalité de la fin du texte
    @Override
    public Text getCardinalityEndText() {
        return arrowHead.getText();
    }
    
    //obtenir la cardinalité du début de la relation
    @Override
    public String getCardinalityStart() {
        return getController().getCardinalityStart();
    }
    
    //obtenir la cardinalité du début du texte
    @Override
    public Text getCardinalityStartText() {
        return arrowTail.getText();
    }
    
    //obtenir la couleur de la destination
    @Override
    public Circle getCircleDst() {
        Contract.check(arrowHead != null, "arrowHead must not be nulll.");
        return arrowHead.getHeadCircle();
    }
    
    //obtenir la couleur de la source
    @Override
    public Circle getCircleSrc() {
        Contract.check(arrowTail != null, "arrowTail must not be nulll.");
        return arrowTail.getHeadCircle();
    }
    
    //obtenir la couleur de la relation
    @Override
    public Color getColorRelation() {
        return getController().getColorRelation();
    }
    
    //obtenir la couleur du texte
    @Override
    public Color getColorText() {
        return getController().getColorText();
    }
    
    public abstract IRelationGraphicController getController();
    
    //obtenir le controller de la source
    @Override
    public IEntityGraphic getEntityDst() {
        return getController().getGEntityDst();
    }
    
    //obtenir le controller de la destination
    @Override
    public IEntityGraphic getEntitySrc() {
        return getController().getGEntitySrc();
    }

    
    //obtenir le premier point de la relation
    @Override
    public Line getFirstLine() {
        return arrowBody.getFisrtLine();
    }
    
    //obtenir le groupe
    @Override
    public List<Line> getHeadLines() {
        return arrowHead.getLines();
    }  
 
    //obtenir le nom de la relation
    @Override
    public String getName() {
        return getController().getName();
    }
    
    //obtenir le nom du texte
    @Override
    public Text getNameText() {
        return nameText;
    }
    
    //obtenir le type de la relation
    @Override
    public Set<Shape> getShapes() {
        Set<Shape> res = new HashSet<>(getArrowShapes());
        res.add(nameText);
        return res;
    }    
    
    //obtenir le type de la tête de la relation
    @Override
    public List<Line> getTailLines() {
        return arrowTail.getLines();
    }
    
    //obtenir le type de la relation
    @Override
    public RelationTypeEnum getType() {
        return getController().getType();
    }
    
    @Override
    public boolean isSelected() {
        return isSelected;
    }   

// COMMANDS 

    @Override
    public void clear() {      
        getArrowShapes().stream().map((s) -> {
            s.removeEventHandler(menuEvent, menuEventHandler);
            return s;
        }).forEach(new Consumer<Shape>() {
            @Override
            public void accept(Shape s) {
                events.keySet().stream().forEach(event -> 
                    s.removeEventHandler(event, events.get(event))
                );
            }
        });
        arrowBody.clearAll();
        getController().clear();
    }

    @Override
    public void drawInGroup(Group g) {
        Contract.check(g != null, "g must not be null.");
        group = g;
        drawArrowBodyInGroup();
        group.getChildren().addAll(arrowHead.getShapes());
        group.getChildren().addAll(arrowTail.getShapes());
        group.getChildren().add(nameText);
    }
    
    @Override
    public void moveArrowBodyPoint(Circle c, double x, double y) {
        Contract.check(c != null, "c must not be null.");
        arrowBody.movePoint(c, x, y);
    }
    
    @Override
    public final void moveArrowHead(double x, double y) {
        IEntityGraphic dst = getController().getGEntityDst();      
        Point2D pDst = dst.getCenterPoint();
        double xDst = pDst.getX();
        double yDst = pDst.getY();
        double wDst = dst.getMainWidth() / 2;
        double hDst = dst.getMainHeight() / 2;
        double x1 = xDst - wDst;
        double x2 = xDst + wDst;
        double y1 = yDst - hDst;
        double y2 = yDst + hDst;
        
        if (x1 < x && x < x2 && y1 < y && y < y2) {
            return;
        }          
        double newX;
        double newY;
        Side side = arrowHead.getSide();
        
        if (x <= x1) {
            newX = x1;
            side = Side.LEFT;
        } else if (x >= x2) {
            newX = x2;
            side = Side.RIGHT;
        } else {
            newX = x;
        }
        
        if (y <= y1) {
            newY = y1;
            side = Side.TOP;
        } else if (y >= y2) {
            newY = y2;
            side = Side.BOTTOM;
        } else {
            newY = y;
        }
        
        Point2D newPoint = new Point2D(newX, newY);
        
        arrowHead.moveHeadPoint((dst instanceof IUsecaseGraphic)
        		? new Scale(wDst, hDst, xDst, yDst).transform(
        				pDst.add(newPoint.subtract(pDst).normalize()))
        		: newPoint);
        arrowHead.setSide(side);
    }
    
    @Override
    public final void moveArrowTail(double x, double y) {
        
        IEntityGraphic src = getController().getGEntitySrc();  
        Point2D pSrc = src.getCenterPoint();
        double xSrc = pSrc.getX();
        double ySrc = pSrc.getY();
 
        double wSrc = src.getMainWidth() / 2;
        double hSrc = src.getMainHeight() / 2;
        
        double x1 = xSrc - wSrc;
        double x2 = xSrc + wSrc;
        double y1 = ySrc - hSrc;
        double y2 = ySrc + hSrc;
        
        if (x1 < x && x < x2 && y1 < y && y < y2) {
            return;
        }
        
        double newX;
        double newY;
        Side side = arrowTail.getSide();
        
        if (x <= x1) {
            newX = x1;
            side = Side.LEFT;
        } else if (x >= x2) {
            newX = x2;
            side = Side.RIGHT;
        } else {
            newX = x;
        }
        
        if (y <= y1) {
            newY = y1;
            side = Side.TOP;
        } else if (y >= y2) {
            newY = y2;
            side = Side.BOTTOM;
        } else {
            newY = y;
        }
        
        Point2D newPoint = new Point2D(newX, newY);
        
        arrowTail.moveHeadPoint((src instanceof IUsecaseGraphic)
        		? new Scale(wSrc, hSrc, xSrc, ySrc).transform(
        				pSrc.add(newPoint.subtract(pSrc).normalize()))
        		: newPoint);
        arrowTail.setSide(side);
    }
    
    @Override
    public void nameTextAutoSize() {
        List<Circle> circles = arrowBody.getCircles();
        Circle c = circles.get(circles.size() / 2);
        nameText.setLayoutX(c.getCenterX() + MARGE_TEXT);
        nameText.setLayoutY(c.getCenterY() + MARGE_TEXT);
    }

    @Override
    public void setCardinalityEnd(String cardinalityEnd) {
        Contract.check(cardinalityEnd != null, "cardinalityEnd must not be null.");
        getController().setCardinalityEnd(cardinalityEnd);
    }
    
    @Override
    public void setCardinalityStart(String cardinalityStart) {
        Contract.check(cardinalityStart != null, "cardinalityStart must not be null.");
        getController().setCardinalityStart(cardinalityStart);
    }
    
    @Override
    public void setColorRelation(Color colorRelation) {
        Contract.check(colorRelation != null, "colorRelation must not be null.");
        getController().setColorRelation(colorRelation);
    }

    @Override
    public void setColorShapes(Color c) {
        Contract.check(c != null, "c must not be null.");
        arrowBody.setColor(c);
        arrowHead.setColor(c);
        arrowTail.setColor(c);
    }
    
    @Override
    public void setColorText(Color colorText) {
        Contract.check(colorText != null, "colorText must not be null.");
        getController().setColorText(colorText);
    }
    
    @Override
    public void setContextMenuEventOnArrow(EventType<ContextMenuEvent> eventType,
            final EventHandler<? super ContextMenuEvent> eventHandler) {
        Contract.check(eventType != null, "eventType must not be null.");
        Contract.check(eventHandler != null, "eventHandler must not be null.");
        getArrowShapes().stream().forEach(s -> 
            s.addEventHandler(eventType, eventHandler)
        );
        menuEvent = eventType;
        menuEventHandler = eventHandler;
    }
    

    @Override
    public void setDNDPointEvent(EventHandler<MouseEvent> pointDnd_Event) {
        Contract.check(pointDnd_Event != null, "c must not be null.");
        arrowBody.setDNDPointEvent(pointDnd_Event);
    }
    
    //set the name of the relation
    @Override
    public void setName(String name) {
        Contract.check(name != null, "name must not be null.");
        getController().setName(name);
    }
    
    //set the controller of the relation
    @Override
    public void setMouseEventOnArrow(EventType<MouseEvent> eventType,
            final EventHandler<? super MouseEvent> eventHandler) {
        getArrowShapes().stream().forEach(s -> 
            s.addEventHandler(eventType, eventHandler)
        );
        events.put(eventType, eventHandler);
    }
    
    //set the selected entity
    @Override
    public void setSelected(boolean b) {
        if (b) {
            diagramEditorController.setSelectedEntity(this);
        }
        isSelected = b;
    }
     
    //set the type of the relation
    @Override
    public void setType(RelationTypeEnum type) {
        getController().setType(type);
    }
    
    //set the type of the head of the relation
    @Override
    public void setTypeArrowHead(TypeHeadArrow head) {
        Contract.check(head != null, "head must not be null.");
        headProperty.setValue(head);
    }
    
    //set the type of the tail of the relation
    @Override
    public void setTypeArrowTail(TypeHeadArrow tail) {
        Contract.check(tail != null, "tail must not be null.");
        tailProperty.setValue(tail);
    }
    
    //set the type of the line of the relation
    @Override
    public final void setTypeLineArrow(TypeLineArrow line) {
        Contract.check(line != null, "line must not be null.");
        if (line == TypeLineArrow.Dashed) {
            arrowBody.setDashed(DASH_DEFAULT);
            arrowTail.setDashed(DASH_DEFAULT);
            arrowHead.setDashed(DASH_DEFAULT);
        } else if (line == TypeLineArrow.Plain) {
            arrowBody.setDashed(0);
        } 
    }
    
    //calculer les points de la relation
    @Override
    public final void calculPointRelation() {
        IEntityGraphic src = getController().getGEntitySrc();
        IEntityGraphic dst = getController().getGEntityDst();
        
        Point2D pSrc = src.getCenterPoint();
        Point2D pDst = dst.getCenterPoint();
        double xSrc = pSrc.getX();
        double ySrc = pSrc.getY();
        double xDst = pDst.getX();
        double yDst = pDst.getY();
        double wSrc = src.getMainWidth() / 2;
        double hSrc = src.getMainHeight() / 2;
        double wDst = dst.getMainWidth() / 2;
        double hDst = dst.getMainHeight() / 2;
    
        //on récupère la direction de la relation
        //en fonction de la position des entités
        Direction d = getDirectionOfDestination();
        switch (d) {
            case NORTH:
                sideSrc.setData(EntityPoint.Side.TOP,
                		new Point2D(xSrc, ySrc - hSrc));
                sideDst.setData(EntityPoint.Side.BOTTOM,
                		new Point2D(xDst, yDst + hDst));
                buildRelation();
                break;
            case NORTH_EST:
                sideSrc.setData(EntityPoint.Side.RIGHT,
                		new Point2D(xSrc + wSrc, ySrc));
                sideDst.setData(EntityPoint.Side.BOTTOM,
                		new Point2D(xDst, yDst + hDst));
                buildRelation();
                break;
            case EST:
                sideSrc.setData(EntityPoint.Side.RIGHT,
                		new Point2D(xSrc + wSrc, ySrc));
                sideDst.setData(EntityPoint.Side.LEFT,
                		new Point2D(xDst - wDst, yDst));
                buildRelation();
                break;    
            case SOUTH_EST:
                sideSrc.setData(EntityPoint.Side.RIGHT,
                		new Point2D(xSrc + wSrc, ySrc));
                sideDst.setData(EntityPoint.Side.TOP,
                		new Point2D(xDst, yDst - hDst));
                buildRelation();
                break;
            case SOUTH:
                sideSrc.setData(EntityPoint.Side.BOTTOM,
                		new Point2D(xSrc, ySrc + hSrc));
                sideDst.setData(EntityPoint.Side.TOP,
                		new Point2D(xDst, yDst - hDst));
                buildRelation();
                break;
            case SOUTH_WEST:
                sideSrc.setData(EntityPoint.Side.LEFT,
                		new Point2D(xSrc - wSrc, ySrc));
                sideDst.setData(EntityPoint.Side.TOP,
                		new Point2D(xDst, yDst - hDst));
                buildRelation();
                break;
            case WEST:
                sideSrc.setData(EntityPoint.Side.LEFT,
                		new Point2D(xSrc - wSrc, ySrc));
                sideDst.setData(EntityPoint.Side.RIGHT,
                		new Point2D(xDst + wDst, yDst));
                buildRelation();
                break;
            case NORTH_WEST:
                sideSrc.setData(EntityPoint.Side.LEFT,
                		new Point2D(xSrc - wSrc, ySrc));
                sideDst.setData(EntityPoint.Side.BOTTOM,
                		new Point2D(xDst, yDst + hDst));
                buildRelation();
                break;
            case NULL:
                sideSrc.setData(EntityPoint.Side.RIGHT,
                		new Point2D(xSrc + wSrc, ySrc));
                sideDst.setData(EntityPoint.Side.TOP,
                		new Point2D(xDst, yDst - hDst));
                buildRelationToSameEntity();
                break;
            default:
        }  
        nameTextAutoSize();
    }
    
// PRIVATE 
    
    
    private void createController() {
         headProperty.addListener(new ChangeListener<TypeHeadArrow>() {
             @Override
             public void changed(ObservableValue<? extends TypeHeadArrow> observable, TypeHeadArrow oldValue, TypeHeadArrow newValue) {
                 updateHead(newValue);
             }
         });       
        tailProperty.addListener(new ChangeListener<TypeHeadArrow>() {
             @Override
             public void changed(ObservableValue<? extends TypeHeadArrow> observable, TypeHeadArrow oldValue, TypeHeadArrow newValue) {
                 updateTail(newValue);
             }
         });     
        sideDst.sideProperty().addListener(new ChangeListener<Side>() {
             @Override
             public void changed(ObservableValue<? extends Side> observable, Side oldValue, Side newValue) {
                 arrowHead.setSide(newValue);
             }
         });     
        sideDst.pointProperty().addListener(new ChangeListener<Point2D>() {
             @Override
             public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
                 arrowHead.moveHeadPoint(newValue);
             }
         });
        sideSrc.sideProperty().addListener(new ChangeListener<Side>() {
             @Override
             public void changed(ObservableValue<? extends Side> observable, Side oldValue, Side newValue) {
                 arrowTail.setSide(newValue);
             }
         });
        sideSrc.pointProperty().addListener(new ChangeListener<Point2D>() {
             @Override
             public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
                 arrowTail.moveHeadPoint(newValue);
             }
         });
        
        //Le circle a envoyé un événement de changement de propriété
        //On va donc redessiner la relation
		arrowBody.addPropertyChangeListener(ArrowBody.PROP_CIRCLE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				//Effacer le dessin de la relation
				clearArrowBodyInGroup();
				//Dessiner la relation
				drawArrowBodyInGroup();
				buildRelation();
				
				

				
			}
		});
		
    }
    
    protected Direction getDirectionOfDestination() {
        IEntityGraphic src = getController().getGEntitySrc();
        IEntityGraphic dst = getController().getGEntityDst();
        
        Point2D pSrc = src.getCenterPoint();
        Point2D pDst = dst.getCenterPoint();
        double xSrc = pSrc.getX();
        double ySrc = pSrc.getY();
        double xDst = pDst.getX();
        double yDst = pDst.getY();
        double wSrc = src.getMainWidth() / 2;
        double hSrc = src.getMainHeight() / 2;
        double wDst = dst.getMainWidth() / 2;
        double hDst = dst.getMainHeight() / 2;
        
        if (src == dst) {
            return Direction.NULL;
        }
        
        if (yDst < ySrc && xDst + wDst > xSrc - wSrc && xDst - wDst < xSrc + wSrc)
            // Si dst plut haut que src et dst.droite.x > src.gauche.x et dst.gauche.x < src.droite.x
            return Direction.NORTH;
        else if (xDst - wDst >= xSrc + wSrc && yDst + hDst <= ySrc - hSrc)
            // Si dst.gauche.x >= src.droite.x et dst.y.bas <= src.x.haut
            return Direction.NORTH_EST;
        else if (xDst > xSrc && yDst + hDst > ySrc - hSrc && yDst - hDst < ySrc + hSrc)
            // Si dst a droite de src et dst.bas.y > src.haut.y et dst.haut.y < src.bas.y
            return Direction.EST;
        else if (yDst - hDst >= ySrc + hSrc && xDst - wDst >= xSrc + wSrc)
            // Si dst.haut.y >= src.bas.y et dst.gauche.x >= src.droite.x
            return Direction.SOUTH_EST;
        else if (yDst > ySrc && xDst - wDst < xSrc + wSrc && xDst + wDst > xSrc - wSrc)
            // Si dst est en bas de src et dst.gauche.x < src.droite.x et dst.droite.x > src.gauche.x 
            return Direction.SOUTH;
        else if (xDst + wDst <= xSrc - wSrc && yDst - hDst >= ySrc + hSrc)
            // Si dst.droite.x <= src.gauche.x et dst.haut.y >= src.Bas.y
            return Direction.SOUTH_WEST;
        else if (xDst < xSrc && yDst - hDst < ySrc + hSrc && yDst + hDst > ySrc - hSrc)
            // Si dst est à gauche de src et dst.haut.y < src.bas.y et dst.bas.y > src.haut.y
            return Direction.WEST;
        else
            return Direction.NORTH_WEST;
    }

    private List<Shape> getArrowShapes() {
        List<Shape> arrow = new ArrayList<>(arrowBody.getLines()); 
        arrow.addAll(arrowHead.getShapes());
        arrow.addAll(arrowTail.getShapes());
        return arrow; 
    }
     
    protected final void updateHead(TypeHeadArrow newValue) {
        arrowHead.setType(newValue);
    }
     
    protected final void updateTail(TypeHeadArrow newValue) {
       arrowTail.setType(newValue);
    }
    
    protected void clearArrowBodyInGroup() {
        if (group != null) {
            group.getChildren().removeAll(arrowBody.getShapes());
            group.getChildren().removeAll(arrowBody.getCircles());
            group.getChildren().removeAll(arrowBody.getLines());
        }
    }

    
    //Dessiner le corps de la relation dans le groupe
    protected void drawArrowBodyInGroup() {
        if (group != null) {
        	//Ajouter les points de la relation dans le groupe
            arrowBody.getShapes().stream().filter((s) -> (!group.getChildren().contains(s))).forEach(s -> 
                group.getChildren().add(0, s)
            );
            arrowBody.getLines().stream().map((l) -> {
                l.addEventHandler(menuEvent, menuEventHandler);
                return l;
            }).forEach(new Consumer<Line>() {
                @Override
                public void accept(Line l) {
                    events.keySet().stream().forEach(event -> 
                        l.addEventHandler(event, events.get(event))
                    );
                }
            });
        }
    }
    
    
    //*********************** Construction d'une relation d'une entité à elle-même.*******************************//
    protected void buildRelationToSameEntity() { 
        double xSrc = sideSrc.getPoint().getX();
        double ySrc = sideSrc.getPoint().getY();
        double xDst = sideDst.getPoint().getX();
        IEntityGraphic src = getController().getGEntitySrc();
        double hSrc = src.getMainHeight() / 2;
        double wLine = LINE_SIZE_MIN * 1.5;
        double hLine = LINE_SIZE_MIN * 1.5;     
        clearArrowBodyInGroup();
        arrowBody.clear();  
        arrowBody.moveStartPoint(arrowTail.getTailPoint());
        arrowBody.moveEndPoint(arrowHead.getTailPoint());
        arrowTail.setSide(sideSrc.getSide());
        arrowHead.setSide(sideDst.getSide());
        drawArrowBodyInGroup();
        arrowBody.addPoint(new Point2D(xSrc + wLine, ySrc));
        arrowBody.addPoint(new Point2D(xSrc + wLine, ySrc - hLine - hSrc));
        arrowBody.addPoint(new Point2D(xDst, ySrc - hLine - hSrc));
        drawArrowBodyInGroup();
    }
    
    
    /**
     * Add a list of points to the relation’s body.
     * @param points the list of points to be added
     */
    
    protected void load(List<Point2D> points) {
        for (int i = 2; i < points.size(); ++ i) {
            arrowBody.addPoint(points.get(i));
        }
        Point2D point = points.get(0);
        moveArrowTail(point.getX(), point.getY());
        point = points.get(1);
        moveArrowHead(point.getX(), point.getY());
        nameTextAutoSize();
    }  
    
    
    //*********************** Construction d'une relation ********************************************************//
    protected void buildRelation() {
        Point2D pointSrc = sideSrc.getPoint();
        Point2D pointDst = sideDst.getPoint();      
        EntityPoint.Side sideEnumSrc = sideSrc.getSide();
         
        double xSrc = pointSrc.getX();
        double ySrc = pointSrc.getY();
        double xDst = pointDst.getX();
        double yDst = pointDst.getY();
        
        clearArrowBodyInGroup();
        arrowBody.clear();
        arrowBody.moveStartPoint(arrowTail.getTailPoint());
        arrowBody.moveEndPoint(arrowHead.getTailPoint());
        arrowTail.setSide(sideEnumSrc);
        arrowHead.setSide(sideDst.getSide());      
        drawArrowBodyInGroup();
        
        //si la relation est verticale : on ajoute les points de la relation en fonction de la position des entités
        //pour que la relation soit bien placée entre les deux entités "y" représente la position centrale de la relation
        //et les 3 points ajoutés sont les points centre de la relation
        if (sideEnumSrc == EntityPoint.Side.TOP || sideEnumSrc == EntityPoint.Side.BOTTOM 
        		&& getDirectionOfDestination() != Direction.NORTH_WEST 
        		&& getDirectionOfDestination() != Direction.NORTH_EST 
        		&& getDirectionOfDestination() != Direction.SOUTH_WEST 
        		&& getDirectionOfDestination() != Direction.SOUTH_EST) {
        	
            double y;
            if (sideEnumSrc == EntityPoint.Side.TOP) {
                y = ySrc + (yDst - ySrc) / 2;
            } else {
                y = yDst + (ySrc - yDst) / 2;
            }             
            arrowBody.addPoint(new Point2D(Math.min(xSrc, xDst) +
            		Math.abs(xDst - xSrc) / 2, y));
        } else {
        	
        	if ( getDirectionOfDestination() != Direction.NORTH_WEST 
            		&& getDirectionOfDestination() != Direction.NORTH_EST 
            		&& getDirectionOfDestination() != Direction.SOUTH_WEST 
            		&& getDirectionOfDestination() != Direction.SOUTH_EST) {
        	
        	//si la relation est horizontale : on ajoute les points de la relation en fonction de la position des entités
        	//pour que la relation soit bien placée entre les deux entités "x" représente la position centrale de la relation horizontalement
        	//et les 3 points ajoutés sont les points centre de la relation
        	
            double x;
            if (sideEnumSrc == EntityPoint.Side.RIGHT) {
                x = xSrc + (xDst - xSrc) / 2;
            } else {
                x = xSrc - (xSrc - xDst) / 2;
            } 
            arrowBody.addPoint(new Point2D(x, Math.max(ySrc, yDst) -
            		Math.abs(yDst - ySrc) / 2));
        }
        }
        
        
        //si la relation est diagonale on le constate a partir de la direction de la relation et on ajoute un point qui est le point centre de la relation
        //et qui est situé entre les deux entités pour que la relation soit bien placée entre les deux entités
        //et on aura que 3 points diagonaux au lieu de 4 et on aura une relation diagonale
        
		if (getDirectionOfDestination() == Direction.NORTH_EST 
				|| getDirectionOfDestination() == Direction.SOUTH_EST) {
			arrowBody.addPoint(new Point2D(Math.min(xSrc, xDst) + Math.abs(xDst - xSrc) / 2,
					Math.min(ySrc, yDst) + Math.abs(yDst - ySrc) / 2));
		}
		if (getDirectionOfDestination() == Direction.NORTH_WEST 
                || getDirectionOfDestination() == Direction.SOUTH_WEST) {
			arrowBody.addPoint(new Point2D(Math.min(xSrc, xDst) + Math.abs(xDst - xSrc) / 2,
					Math.min(ySrc, yDst) + Math.abs(yDst - ySrc) / 2));
		}

		drawArrowBodyInGroup();
	}
    
 
	
	//ajouter un point à la relation
	public void addPointInRelation(Double x, Double y) {
		arrowBody.addPoint(new Point2D(x, y));
        drawArrowBodyInGroup();
	}
	
    
// ENUM
    public enum Direction {
        NORTH, NORTH_EST, EST, SOUTH_EST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NULL
    }
}
