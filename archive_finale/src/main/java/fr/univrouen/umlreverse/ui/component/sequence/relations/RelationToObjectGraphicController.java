/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.sequence.relations;

import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.STYLE_CHANGED_PROPERTY_NAME;

import java.lang.reflect.InvocationTargetException;

import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.sequence.IActivity;
import fr.univrouen.umlreverse.model.diagram.sequence.IRelationToObject;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityGraphicController;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphicController;
import fr.univrouen.umlreverse.ui.component.common.relation.type.Owner;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationType;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.ui.component.sequence.dialog.DialogRelationToObject;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class RelationToObjectGraphicController extends AEntityGraphicController
		implements IEntityGraphicController {
// ATTRIBUTES
    /** The diagram controller that manages the component. */
    private final ISequenceController diagramController;
    /** The relation component. */
    private final RelationToObjectGraphic relationG;
    /** The view relationModel associated with the relation component. */
    private final IRelationToObject model;
	// ATTRIBUTES
 
// CONSTRUCTOR
    public RelationToObjectGraphicController(RelationToObjectGraphic relationG, ISequenceController diagramController, IRelationToObject model) {
        super();
        Contract.check(relationG != null, 
                 "L'argument noteG ne doit pas être nul.");
        Contract.check(diagramController != null, 
                 "L'arguments diagramController ne doit pas être nul.");
        Contract.check(model != null, 
                 "L'argument note ne doit pas être nul.");
        
        this.diagramController = diagramController;
        this.model = model;
        this.relationG = relationG;
        createController();

        IStyle style = model.getStyle();
        setStyle(style, true);
        
    }

    // REQUEST	
    @Override
    public Color getBackgroundColor() {
        IStyle style = model.getStyle();
        String styleBackgroundColor = style.getValue(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID);
        return Color.web(styleBackgroundColor);
    }
    
    public IRelationToObject getModel() {
        return model;
    }
    
    public ISequenceController getDiagramController() {
    	return diagramController;
    }
// COMMANDS 
    
    
    @Override
    public void setBackgroundColor(Color c) {
        Contract.check(c != null, "L'argument c ne doit pas être nul.");
        model.addStyle(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID, c.toString());
    }
 
    @Override
    public void setPosition(Point2D value) {
        Contract.check(value != null, "L'argument value "
                + "ne doit pas être nul.");

        
        IStyle style = model.getStyle();
        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");

        double tX = Double.valueOf(positionTab[2]);
        double tY = Double.valueOf(positionTab[3]);

        model.addStyle(IDiagramEditorController.POSITION_STYLE_ID,
                value.getX() + "|" + value.getY() + "|"
                + tX + "|" + tY);
        positionProperty().set(value);
    }
   
    
    @Override
    public void setTranslatePosition(Point2D value) {
    	Contract.check(value != null, "L'argument value "
                + "ne doit pas être nul.");
    	
    	// if the relation is not above the objects, move the relation
    	if (value.getY() > 
        		diagramController.getObjects().values().stream().findFirst().get().getTranslateY() +
        		diagramController.getObjects().values().stream().findFirst().get().getRectangle().getHeight()) {

    		IStyle style = model.getStyle();
	        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
	        String[] positionTab = position.split("\\|");
	        double x = Double.valueOf(positionTab[2]);
	        double lX = Double.valueOf(positionTab[0]);
	        double lY = Double.valueOf(positionTab[1]);
	
	        model.addStyle(IDiagramEditorController.POSITION_STYLE_ID,
	                lX + "|" + lY + "|"
	                + x + "|" + value.getY());
	        
	        value = new Point2D(x, value.getY());
	        positionProperty().set(value);
	    	
    	}
    }
    
    
    public void setName(String name) {
		getModel().setNameRelation(name);
	}

	public String getName() {
		return getModel().getNameRelation();
	}
	
// CONTROLLER 
    private void createController() {
        final ContextMenu ctxMenu = getContextMenu();
        MenuItem editMI = getEditMI();
        MenuItem removeMI = getRemoveMI();
        MenuItem addRelationMI = getAddRelationMI();
        
        
        addRelationMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    diagramController.createRelation(model);
            }
        });
        
        final double borderWidth = 1;
        relationG.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				relationG.setTranslateX(relationG.getTranslateX() - 1);
				relationG.setTranslateY(relationG.getTranslateY() - 3);
				relationG.setStyle("-fx-border-color: #ff0000;"
						+ "-fx-border-width:"+ borderWidth+";-fx-border-insets:2 0 0 0");		
			}
		});
        
        relationG.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				relationG.setTranslateX(relationG.getTranslateX() + 1);
				relationG.setTranslateY(relationG.getTranslateY() + 3);
				relationG.setStyle("");		
			}
		});
        
        
        // Show context menu.
        relationG.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
            new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    relationG.setSelected(true);
                    ctxMenu.show(relationG, event.getScreenX(), event.getScreenY()); 
                }
            }
        );
        
        // Show DialogNoteEdit when a double click is detected.
        relationG.addEventFilter(MouseEvent.MOUSE_CLICKED, 
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY 
                        && event.getClickCount() == 2) {
                    edit();
                }
            }
        });
        
        editMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                edit();
            }
        });
        
        removeMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                diagramController.removeRelation(model);
            }
        });
        
        //Not supposed to be used
        getModel().addPropertyChangeListener(IRelation.NAME_CHANGED_PROPERTY_NAME, evt -> {
        	String newName = (String) evt.getNewValue();
        	relationG.getNameText().setText(newName);
        });
        
        getModel().addPropertyChangeListener(IRelationToObject.TEXT_CHANGE_PROPERTY_NAME, evt -> {
        	String newName = (String) evt.getNewValue();
        	relationG.setText(newName);
        });
        
        
        model.addPropertyChangeListener(STYLE_CHANGED_PROPERTY_NAME, evt -> {
            setStyle((IStyle) evt.getNewValue(), false);
        });
        
        getModel().addPropertyChangeListener(IRelationToObject.ACTIVITY_SRC_CHANGE_PROPERTY_NAME, evt -> {
        	refresh();
        });
        
        getModel().addPropertyChangeListener(IRelationToObject.ACTIVITY_DST_CHANGE_PROPERTY_NAME, evt -> {
        	refresh();
        });
        
        //when the model change the relation type
        getModel().addPropertyChangeListener(IRelation.TYPE_CHANGE_PROPERTY_NAME, evt -> {
        	RelationTypeEnum newType = (RelationTypeEnum) evt.getNewValue();
        	RelationType newArrow = null;
        	
        	// création de la création du composant graphic de la relation à l'aide de la méthode newInstance
        	if (relationG.getEntitySrc() == relationG.getEntityDst()) {
        		newArrow = new Owner(
        				Math.max(RelationToObjectGraphic.OWNER_WIDTH_, 
        						relationG.getNameText().getWrappingWidth()), 10);
        	} else {
	        	try {
	        		 newArrow = newType.getGroup().getClass().getConstructor(double.class).newInstance(relationG.getWidthRelation());
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
        	}
        	relationG.setRelationType(newArrow);
        	
        	refresh();
        	relationG.getNameText().setWrappingWidth(relationG.getWidthRelation());
        	relationG.RefreshRelationDirection();
        });
        

        //When src move, change the size of the relation and the arrow's direction
        relationG.getEntitySrc().positionProperty().addListener(new ChangeListener<Point2D>() {

			@Override
			public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
				Point2D srcPoint = relationG.getEntitySrc().lifeLineStartPosition();
				Point2D dstPoint = relationG.getEntityDst().lifeLineStartPosition();
				
		       
	        	relationG.getNameText().setWrappingWidth(relationG.getWidthRelation());
				if (srcPoint.getX() < dstPoint.getX()) {
					
					model.addStyle(IDiagramEditorController.POSITION_STYLE_ID,
			                0 + "|" + 0 + "|"
			                + srcPoint.getX() + "|" + relationG.positionProperty().get().getY());
					
				} else {
					//inverse la direction de la fleche
				}
				refresh();
			}
		});
       
        //When dst move, change the size of the relation and the arrow's direction
        relationG.getEntityDst().positionProperty().addListener(new ChangeListener<Point2D>() {

			@Override
			public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
				Point2D srcPoint = relationG.getEntitySrc().lifeLineStartPosition();
				Point2D dstPoint = relationG.getEntityDst().lifeLineStartPosition();
				
		        
				
	        	relationG.getNameText().setWrappingWidth(relationG.getWidthRelation());
				if (srcPoint.getX() < dstPoint.getX()) {
					
				} else {
					//inverse la direction de la fleche
					model.addStyle(IDiagramEditorController.POSITION_STYLE_ID,
			                0 + "|" + 0 + "|"
			                + dstPoint.getX() + "|" + relationG.positionProperty().get().getY());
					
				}
				
				refresh();
			}
		});
      
    }
    
// PRIVATE
    /**Relation
     * Show DialogEditNote to edit NoteGraphic.
     * @param
     */
    private void edit() {       
        DialogRelationToObject dialog = new DialogRelationToObject(model);
        dialog.showAndWait();
    } 
    
    
    private void setStyle(IStyle style, boolean inConstructor) {

        String styleColor = style.getValue(IDiagramEditorController.TEXT_COLOR_STYLE_ID);

        relationG.getNameText().setFill(Color.web(styleColor));

        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");
        double tX = Double.valueOf(positionTab[2]);
        double tY = Double.valueOf(positionTab[3]);

        relationG.setTranslateX(tX);
        relationG.setTranslateY(tY);

        if (inConstructor) {
            positionProperty().setValue(new Point2D(tX, tY));
        }
    }

    
    public void refresh() {
    	double shiftSrc = 0;
    	double shiftDst = 0;
    	if (relationG.getEntitySrc().getCenterPoint().getX() <= relationG.getEntityDst().getCenterPoint().getX()) {
    		shiftSrc = (getModel().getActivityContainer() == null 
    				? 0 : (getModel().getActivityContainer().getLevel())*10 + 5);
    		shiftDst = (getModel().getActivityDstContainer() == null 
    				? 0 : 5);
    		
    		relationG.setTranslateX(relationG.getEntitySrc().lifeLineStartPosition().getX() + shiftSrc);
    		
			relationG.getRelationType().setRotate(0);
    	} else {
    		shiftSrc = (getModel().getActivityContainer() == null ? 0 : 5);
    		shiftDst = (getModel().getActivityDstContainer() == null 
    				? 0 : (getModel().getActivityDstContainer().getLevel())*10 + 5);
    		relationG.setTranslateX(relationG.getEntityDst().lifeLineStartPosition().getX() + shiftDst);

			relationG.getRelationType().setRotate(180);
    	}
    	
    	double width = Math.max(RelationToObjectGraphic.OWNER_WIDTH_, 
				relationG.getNameText().getWrappingWidth());
    	if (relationG.getEntitySrc() != relationG.getEntityDst()) {
    		width = relationG.getWidthRelation() - shiftSrc - shiftDst;	
    	}
		relationG.getRelationType().draw(width);
    	
    }
	
    
}