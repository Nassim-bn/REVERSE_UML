/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.common.relation;

import static fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController.POSITIONS_RELATION_STYLE_ID;

import java.util.ArrayList;
import java.util.List;

import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphicController;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.ui.component.usecase.dialog.DialogRelation;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class RelationGraphicController implements IRelationGraphicController {
// ATTRIBUTES
    /** The diagram controller that manages the component. */
    private final IDiagramEditorController diagramController;
    /** The relation component. */
    private final IRelationGraphic relation;
    /** The view model associated with the relation component. */
    private final IRelation relationModel;
    /** The context menu associated with the component. */
    private ContextMenu ctxMenu;
    /** The source of the relation. */
    private final IEntityGraphic src;
    /** The target of the relation. */
    private final IEntityGraphic dst;
    /** Are both ends of the relation the same entity? */
    private final boolean oneEntity;
    /** The type of the relation. */
    private final ObjectProperty<RelationTypeEnum> typeProperty;
    /** A bunch of listeners. */
    private ChangeListener<Point2D> moveEntityCLSrc;
    private ChangeListener<Point2D> moveEntityCLDst;
    private ChangeListener<Object> heightEntityCLSrc;
    private ChangeListener<Object> heightEntityCLDst;
    private ChangeListener<Object> widthEntityCLSrc;
    private ChangeListener<Object> widthEntityCLDst;
    private EventHandler<MouseEvent> sourceDND_Event;
    private EventHandler<MouseEvent> destinationDND_Event;
    private EventHandler<MouseEvent> pointDnd_Event;
    
    // CONSTRUCTORS
    /**
     * 
     * @param relation
     * @param relationModel
     * @param diagramController
     * @param src
     * @param dst
     * @pre
     *      relation != null
     *      && relationModel != null
     *      && diagramController != null
     *      && src != null
     *      && dst != null
     */
    public RelationGraphicController(RelationGraphic relation, 
            IRelation relationModel,
            IDiagramEditorController diagramController,
            IEntityGraphic src, IEntityGraphic dst) {
        Contract.check(diagramController != null, 
                "diagramController must not be null.");
        Contract.check(relation != null, "relation must not be null.");
        Contract.check(relationModel != null, "relationModel must not be null.");
        Contract.check(src != null, "src must not be null.");
        Contract.check(dst != null, "dst must not be null.");
        this.relation = relation;
        this.diagramController = diagramController;
        this.src = src;
        this.dst = dst;
        oneEntity = src.equals(dst);
        this.relationModel = relationModel;

        IStyle style = relationModel.getStyle();
        String color = style.getValue(IDiagramEditorController.COLOR_STYLE_ID);    
        Color c = Color.web(color);
        changeColorRelation(c);
        
        TypeHeadArrow head = relationModel.getArrowHead();    
        TypeHeadArrow tail = relationModel.getArrowTail();
        TypeLineArrow line = relationModel.getLineArrow();
        
        RelationTypeEnum type = RelationTypeEnum.CUSTOM;
        for (RelationTypeEnum t : RelationTypeEnum.values()) {
            if (t.getHead() == head &&
            		t.getLine() == line
            		&& t.getTail() == tail) {
                type = t;
            }
        }
        
        typeProperty = new SimpleObjectProperty<>(type);     
        createController();
    }

    // QUERIES
    @Override
    public String getCardinalityEnd() {
        return relationModel.getHeadCardinality();
    }
    
    @Override
    public String getCardinalityStart() {
        return relationModel.getTailCardinality();
    }
    
    @Override
    public Color getColorRelation() {
        IStyle style = relationModel.getStyle();
        return Color.web(style.getValue(
        		IDiagramEditorController.COLOR_STYLE_ID));
    }

    @Override
    public Color getColorText() {
        IStyle style = relationModel.getStyle();
        return Color.web(style.getValue(
        		IDiagramEditorController.TEXT_COLOR_STYLE_ID));
    }
    
    @Override
    public IEntityGraphic getGEntitySrc() {
        return src;
    }

    @Override
    public IEntityGraphic getGEntityDst() {
        return dst;
    }
    
    @Override
    public String getName() {
        return relationModel.getNameRelation();
    }
    
    @Override
    public List<Point2D> getPoints() {
        List<Point2D> res = new ArrayList<>();
        String positions = relationModel.getStyle()
        		.getValue(POSITIONS_RELATION_STYLE_ID);
        if (positions != null) {
            String[] positionsTab = positions.split("\\|");
            Point2D p;
            for (int i = 0; i < positionsTab.length - 1; i += 2) {  
                p = new Point2D(Double.valueOf(positionsTab[i]),
                		Double.valueOf(positionsTab[i + 1]));
                res.add(p);
            }
        }
        return res; 
    }
    
    @Override
    public RelationTypeEnum getType() {
        return typeProperty.getValue();
    }

    @Override
    public TypeHeadArrow getTypeHead() {
        return relationModel.getArrowHead();
    } 
    
    @Override
    public TypeLineArrow getTypeLine() {
        return relationModel.getLineArrow();
    }
    
    @Override
    public TypeHeadArrow getTypeTail() {
        return relationModel.getArrowTail();
    }
       
    // COMMANDS
    @Override
    public void clear() {
        relation.getCircleSrc().removeEventFilter(MouseEvent.MOUSE_DRAGGED, 
                sourceDND_Event);
        relation.getCircleDst().removeEventFilter(MouseEvent.MOUSE_DRAGGED, 
                destinationDND_Event);
        src.positionProperty().removeListener(moveEntityCLSrc); 
        src.heightProperty().removeListener(heightEntityCLSrc);
        src.widthProperty().removeListener(widthEntityCLSrc);
        if (!oneEntity) {
            dst.positionProperty().removeListener(moveEntityCLDst);  
            dst.heightProperty().removeListener(heightEntityCLDst);
            dst.widthProperty().removeListener(widthEntityCLDst);
        }
        
    }
    
    @Override
    public void setCardinalityEnd(String cardinalityEnd) {
        Contract.check(cardinalityEnd != null, "cardinalityEnd must not be null.");
        relationModel.setHeadCardinality(cardinalityEnd);
    }

    @Override
    public void setCardinalityStart(String cardinalityStart) {
        Contract.check(cardinalityStart != null, "cardinalityStart must not be null.");
        relationModel.setTailCardinality(cardinalityStart);
    }

    @Override
    public void setColorRelation(Color colorRelation) {
        Contract.check(colorRelation != null, "colorRelation must not be null.");
        relationModel.addStyle(IDiagramEditorController.COLOR_STYLE_ID,
        		colorRelation.toString());
    }

    @Override
    public void setColorText(Color colorText) {
        Contract.check(colorText != null, "colorText must not be null.");
        relationModel.addStyle(IDiagramEditorController.TEXT_COLOR_STYLE_ID,
        		colorText.toString());
    }
    
    @Override
    public void setName(String name) {
        Contract.check(name != null, "name must not be null.");
        relationModel.setNameRelation(name);
    }
    
    @Override
    public void setType(RelationTypeEnum type) {
        Contract.check(type != null, "type must not be null.");
        typeProperty.setValue(type);
    }
    
    // TOOLS
    /**
     * Set up all the behavior associated with the relation: context menu,
     * mouse listeners, property listeners, model listeners…
     */
    private void createController() {
        ctxMenu = new ContextMenu();
        MenuItem editMI = new MenuItem("Éditer");
        MenuItem cleanMI = new MenuItem("Rafraîchir");
        MenuItem removeMI = new MenuItem("Supprimer");
        
        ctxMenu.getItems().addAll(editMI, cleanMI, removeMI);

        editMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                edit();
            }
        });       
        cleanMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                relationModel.removeStyle(POSITIONS_RELATION_STYLE_ID);
            }
        });      
        removeMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                diagramController.removeRelation(relationModel);
            }
        });   
        
        createControllerRelation();
        createPropertyListeners();
        createModelListeners();
    }
    
    /**
     * Set up the component’s mouse listeners.
     */
    private void createControllerRelation() {
        initializeRelationEvent();
        
        // Show context menu.
        relation.setContextMenuEventOnArrow(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
                new EventHandler<ContextMenuEvent>() {
                        @Override
                        public void handle(ContextMenuEvent event) {
                            relation.setSelected(true);
                            ctxMenu.show(relation.getFirstLine(), 
                                    event.getScreenX(), event.getScreenY());
                        }
                }
        );
        
        // Show DialogEdit when a double click is detected.
        relation.setMouseEventOnArrow(MouseEvent.MOUSE_CLICKED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton() == MouseButton.PRIMARY 
                            && event.getClickCount() == 2) {
                        edit();
                    }
                }
            }
        );
           
        relation.setMouseEventOnArrow(MouseEvent.MOUSE_ENTERED, 
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    relation.getShapes().stream().forEach((shape) -> {
                        shape.setStyle("-fx-stroke-width : 4");
                    });   
                    relation.getCircleSrc().setVisible(true);
                    relation.getCircleDst().setVisible(true);
                    relation.getArrowBodyCircles().stream().forEach((c) -> {
                        c.setVisible(true);
                    });
                }
            }
        );
         
        relation.setMouseEventOnArrow(MouseEvent.MOUSE_EXITED, 
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    relation.getShapes().stream().forEach((shape) -> {
                        shape.setStyle("-fx-stroke-width : 1");
                    });   
                    relation.getCircleSrc().setVisible(false);
                    relation.getCircleDst().setVisible(false);
                    relation.getArrowBodyCircles().stream().forEach((c) -> {
                        c.setVisible(false);
                    });
                }
            }
        );
        relation.getCircleSrc().addEventHandler(MouseEvent.MOUSE_ENTERED,
        		new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                relation.getCircleSrc().setVisible(true);
            }
        });       
        relation.getCircleSrc().addEventHandler(MouseEvent.MOUSE_EXITED,
        		new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                relation.getCircleSrc().setVisible(false);
            }
        });   
        relation.getCircleDst().addEventHandler(MouseEvent.MOUSE_ENTERED,
        		new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                relation.getCircleDst().setVisible(true);
            }
        });      
        relation.getCircleDst().addEventHandler(MouseEvent.MOUSE_EXITED,
        		new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                relation.getCircleDst().setVisible(false);
            }
        }); 
        
        relation.getCircleSrc().addEventFilter(MouseEvent.MOUSE_DRAGGED, 
                sourceDND_Event);
        relation.getCircleDst().addEventFilter(MouseEvent.MOUSE_DRAGGED, 
                destinationDND_Event);       
        relation.setDNDPointEvent(pointDnd_Event);
    }
    
    /**
     * Set up property listeners on the entities on both ends of the relation.
     */
    private void createPropertyListeners() {
        initializeEntitiesEvent();
        src.positionProperty().addListener(moveEntityCLSrc);
        if (!oneEntity) {
            dst.positionProperty().addListener(moveEntityCLDst);
        }
        src.heightProperty().addListener(heightEntityCLSrc);
        dst.heightProperty().addListener(heightEntityCLDst);
        src.widthProperty().addListener(widthEntityCLSrc);
        dst.widthProperty().addListener(widthEntityCLDst);
        
        typeProperty.addListener(new ChangeListener<RelationTypeEnum>() {
            @Override
            public void changed(ObservableValue<? extends RelationTypeEnum> observable,
            		RelationTypeEnum oldValue, RelationTypeEnum newValue) {
                relationModel.setArrowHead(newValue.getHead());
                relationModel.setArrowTail(newValue.getTail());
                relationModel.setLineArrow(newValue.getLine());
            }
        }); 
    }

    /**
     * Set up listeners on the view model.
     */
    private void createModelListeners() {
        relationModel.addPropertyChangeListener("ArrowHeadChanged", evt -> {
            TypeHeadArrow head = (TypeHeadArrow) evt.getNewValue();
            relation.setTypeArrowHead(head);
        });  
        
        relationModel.addPropertyChangeListener("ArrowTailChanged", evt -> {
            TypeHeadArrow tail = (TypeHeadArrow) evt.getNewValue();
            relation.setTypeArrowTail(tail);
        });     
        
        relationModel.addPropertyChangeListener("LineArrowChanged", evt -> {
            TypeLineArrow line = (TypeLineArrow) evt.getNewValue();
            relation.setTypeLineArrow(line);
        });
        
        relationModel.addPropertyChangeListener("NameChanged", evt -> {
            String name = (String) evt.getNewValue();
            relation.getNameText().setText(name);
            relation.calculPointRelation();
        }); 
        
        relationModel.addPropertyChangeListener("TailCardinalityChanged", evt -> {
            String cardi = (String) evt.getNewValue();
            relation.getCardinalityStartText().setText(cardi);
            relation.calculPointRelation();
        });
        
        relationModel.addPropertyChangeListener("HeadCardinalityChanged", evt -> {
            String cardi = (String) evt.getNewValue();
            relation.getCardinalityEndText().setText(cardi);
            relation.calculPointRelation();
        });
        
        relationModel.addPropertyChangeListener("StyleChanged", evt -> {
            IStyle style = (IStyle) evt.getNewValue();
            Color color = getColorRelation();
            changeColorRelation(color);     
            Color textColor = getColorText();
            relation.getCardinalityStartText().setFill(textColor);
            relation.getCardinalityEndText().setFill(textColor);
            relation.getNameText().setFill(textColor);     
            String positions = style.getValue(POSITIONS_RELATION_STYLE_ID);
            if (positions != null) {
                String[] positionsTab = positions.split("\\|");      
                relation.moveArrowTail(Double.valueOf(positionsTab[0]),
                		Double.valueOf(positionsTab[1]));
                relation.getCircleSrc().setVisible(true);   
                relation.moveArrowHead(Double.valueOf(positionsTab[2]),
                		Double.valueOf(positionsTab[3]));
                relation.getCircleDst().setVisible(true);        
                List<Circle> circles = relation.getArrowBodyCircles();
                int k = 0;
                for (int i = 4; i < positionsTab.length; i += 2) {
                    Circle c = circles.get(k);
                    relation.moveArrowBodyPoint(c,
                    		Double.valueOf(positionsTab[i]),
                    		Double.valueOf(positionsTab[i + 1]));
                    ++k;
                } 
                relation.nameTextAutoSize();
            } else {
                relation.calculPointRelation();
            }
        });
    }
    
    /**
     * Create the listeners to be set on the entities.
     */
    private void initializeEntitiesEvent() {
        moveEntityCLSrc = new ChangeListener<Point2D>() {
            @Override
            public void changed(ObservableValue<? extends Point2D> observable,
            		Point2D oldValue, Point2D newValue) {
                PropertyType pt = PropertyType.SRC_ENTITY;
                pt.setPoint(new Point2D(
                        newValue.getX() - oldValue.getX(), 
                        newValue.getY() - oldValue.getY())
                );
                changePositionInModel(pt);
            }
        };    
        
        if (!oneEntity) {         
            moveEntityCLDst = new ChangeListener<Point2D>() {
                @Override
                public void changed(ObservableValue<? extends Point2D> observable,
                		Point2D oldValue, Point2D newValue) {
                PropertyType pt = PropertyType.DST_ENTITY;
                pt.setPoint(new Point2D(
                        newValue.getX() - oldValue.getX(), 
                        newValue.getY() - oldValue.getY())
                );
                changePositionInModel(pt);
                
                }
            };     
        }
        
        heightEntityCLSrc = new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable,
            		Object oldValue, Object newValue) {
                Double newHeight = (Double) newValue - (Double) oldValue;
                PropertyType pt = PropertyType.HEIGHT_SRC;
                pt.setDouble(newHeight);
                changePositionInModel(pt);
            }
        };
        
        heightEntityCLDst = new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable,
            		Object oldValue, Object newValue) {
                Double newHeight = (Double) newValue - (Double) oldValue;
                PropertyType pt = PropertyType.HEIGHT_DST;
                pt.setDouble(newHeight);
                changePositionInModel(pt);
            }
        };
        
        widthEntityCLSrc = new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable,
            		Object oldValue, Object newValue) {
                Double newHeight = (Double) newValue - (Double) oldValue;
                PropertyType pt = PropertyType.WIDTH_SRC;
                pt.setDouble(newHeight);
                changePositionInModel(pt);
            }
        };
        
        widthEntityCLDst = new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable,
            		Object oldValue, Object newValue) {
                Double newHeight = (Double) newValue - (Double) oldValue;
                PropertyType pt = PropertyType.WIDTH_DST;
                pt.setDouble(newHeight);
                changePositionInModel(pt);
            }
        };
    }
    
    /**
     * Update the model
     * @param propertyType
     */
    private void changePositionInModel(PropertyType propertyType) {
        IStyle style = relationModel.getStyle();
        String positions = style.getValue(POSITIONS_RELATION_STYLE_ID);
        if (positions == null && propertyType != PropertyType.SRC 
                && propertyType != PropertyType.DST
                && propertyType != PropertyType.POINT) {
            relation.calculPointRelation();
        } else {
            Circle circle = relation.getCircleSrc();
            double srcX = circle.getCenterX();
            double srcY = circle.getCenterY();
            circle = relation.getCircleDst();
            double dstX = circle.getCenterX();
            double dstY = circle.getCenterY();
            Point2D point;
            switch (propertyType) {
                case HEIGHT_SRC :
                    srcY += propertyType.getDouble();
                    if (oneEntity) {
                        dstY += propertyType.getDouble();
                    }
                    break;
                case HEIGHT_DST :
                    dstY += propertyType.getDouble();
                    break;
                case WIDTH_SRC :
                    srcX += propertyType.getDouble();
                    if (oneEntity) {
                        dstX += propertyType.getDouble();
                    }
                    break;
                case WIDTH_DST :
                    dstX += propertyType.getDouble();
                    break;
                case SRC_ENTITY :
                    point = propertyType.getPoint();
                    srcX += point.getX();
                    srcY += point.getY();
                    if (oneEntity) {
                        dstX += point.getX();
                        dstY += point.getY();
                    }
                    break;
                case DST_ENTITY :
                    point = propertyType.getPoint();
                    dstX += point.getX();
                    dstY += point.getY();
                    break;
                case SRC :
                    point = propertyType.getPoint();
                    srcX = point.getX();
                    srcY = point.getY();
                    break;
                case DST :
                    point = propertyType.getPoint();
                    dstX = point.getX();
                    dstY = point.getY();
                    break;
				default:
					break;
            }
            
            
            StringBuilder sb = new StringBuilder();
            sb.append(srcX);
            sb.append("|").append(srcY);
            sb.append("|").append(dstX);
            sb.append("|").append(dstY);
 
            if (propertyType != PropertyType.POINT) {
                relation.getArrowBodyCircles().stream()
                		.forEach((c) -> sb.append("|").append(c.getCenterX())
                				.append("|").append(c.getCenterY()));
            } else {
                relation.getArrowBodyCircles().stream()
                		.forEach((c) -> {
                    if (c.equals(propertyType.getCircle())) {
                        Point2D p = propertyType.getPoint();
                        sb.append("|").append(p.getX())
                        		.append("|").append(p.getY()); 
                    } else {
                        sb.append("|").append(c.getCenterX())
                        		.append("|").append(c.getCenterY());
                    }
                });
            }
            relationModel.addStyle(POSITIONS_RELATION_STYLE_ID, sb.toString());                   
        }
    }
    
    /**
     * Create the mouse listeners for the relation component.
     */
    private void initializeRelationEvent() {
        sourceDND_Event = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();
                
                PropertyType pt = PropertyType.SRC;
                pt.setPoint(new Point2D(x, y));
                changePositionInModel(pt);
                event.consume();
            }
        };  
        destinationDND_Event = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();          
                PropertyType pt = PropertyType.DST;
                pt.setPoint(new Point2D(x, y));
                changePositionInModel(pt);
                event.consume();
            }
        }; 
        
        pointDnd_Event = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Circle ci = (Circle) event.getSource();
                double x = event.getX();
                double y = event.getY();
                        
                PropertyType pt = PropertyType.POINT;
                pt.setPoint(new Point2D(x, y));
                pt.setCircle(ci);
                changePositionInModel(pt);
                event.consume();
            }
        };   
    }
    
    /**
     * Show a dialog to edit the relation.
     */
    private void edit() {    
        DialogRelation dialog = new DialogRelation(relationModel);
        dialog.showAndWait();      
    } 
    
    /**
     * Set the relation’s color.
     * @param c take a guess
     */
    private void changeColorRelation(Color c) {
        relation.setColorShapes(c);
    }
    
    // NESTED TYPES
    private enum PropertyType {
        SRC_ENTITY,
        DST_ENTITY,
        SRC,
        DST,
        POINT,
        HEIGHT_SRC,
        HEIGHT_DST, 
        WIDTH_SRC,
        WIDTH_DST;
        
    	// ATTRIBUTES
        private double d;
        private Point2D p;
        private Circle c;
      
        // REQUESTS
        public double getDouble() {
            return d;
        }
        
        public Point2D getPoint() {
            return p;
        }
        
        public Circle getCircle() {
            return c;
        }
        
        // COMMANDS
        public void setDouble(double d) {
            this.d = d;
        }
        
        public void setPoint(Point2D p) {
            this.p = p;
        }
        public void setCircle(Circle c) {
            this.c = c;
        }
    }
}