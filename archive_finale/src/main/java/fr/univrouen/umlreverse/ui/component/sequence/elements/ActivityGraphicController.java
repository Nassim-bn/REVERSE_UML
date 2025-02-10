package fr.univrouen.umlreverse.ui.component.sequence.elements;

import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.STYLE_CHANGED_PROPERTY_NAME;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import fr.univrouen.umlreverse.model.diagram.sequence.Activity;
import fr.univrouen.umlreverse.model.diagram.sequence.IActivity;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityGraphicController;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityTextGraphicController;
import fr.univrouen.umlreverse.ui.component.sequence.dialog.DialogActivityEdit;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ActivityGraphicController extends AEntityGraphicController
	implements IEntityTextGraphicController, IActivityGraphicController {

	// ATTRIBUTS
    private final ActivityGraphic activityG;
    private final IActivity model;
    private final ISequenceController diagramController;

    // CONSTRUCTOR
	public ActivityGraphicController(ActivityGraphic activityGraphic, ISequenceController diagram, IActivity activity) {
		super();

		Contract.check(activityGraphic != null, "L'argument activityGraphic ne doit pas être nul.");
        Contract.check(diagram != null, "L'arguments diagram ne doit pas être nul.");
        Contract.check(activity != null, "L'argument activity ne doit pas être nul.");

		activityG = activityGraphic;
		diagramController = diagram;
		model = activity;

		setText();
        IStyle style = model.getStyle();
        setStyle(style, true);
        createController();
	}

	// REQUESTS
	@Override
	public IActivity getModel() {
        return model;
    }

	@Override
	public Color getBackgroundColor() {
		IStyle style = model.getStyle();
        String styleBackgroundColor = style.getValue(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID);
        return Color.web(styleBackgroundColor);
	}

	@Override
	public ISequenceController getDiagramController() {
		return diagramController;
	}

	// COMMANDS
	@Override
    public void setPosition(Point2D value) {
        Contract.check(value != null, "L'argument value "
                + "ne doit pas être nul.");
        IStyle style = model.getStyle();
        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");

        //double tX = Double.valueOf(positionTab[2]);
        double tX = Double.parseDouble(positionTab[2]);
        //double tY = Double.valueOf(positionTab[3]);
        double tY = Double.parseDouble(positionTab[3]);

        model.addStyle(IDiagramEditorController.POSITION_STYLE_ID,
                value.getX() + "|" + value.getY() + "|"
                + tX + "|" + tY);
        positionProperty().set(value);
    }

	@Override
	public void setBackgroundColor(Color c) {
		Contract.check(c != null, "L'argument c ne doit pas être nul.");
        model.addStyle(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID, c.toString());
	}

	@Override
    public void setTranslatePosition(Point2D value) {
		Contract.check(value != null, "L'argument value "
                + "ne doit pas être nul.");
        IStyle style = model.getStyle();
        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");
        
        //double x = Double.valueOf(positionTab[2]);
        double x = Double.parseDouble(positionTab[2]);
        //double lX = Double.valueOf(positionTab[0]);
        double lX = Double.parseDouble(positionTab[0]);
        //double lY = Double.valueOf(positionTab[1]);
        double lY = Double.parseDouble(positionTab[1]);
        
        if ((value.getY() >
        diagramController.getObjects().values().stream().findFirst().get().getTranslateY() +
        diagramController.getObjects().values().stream().findFirst().get().getRectangle().getHeight()
        && model.getParent() == null) || (model.getParent() != null)
        		&& value.getY() > diagramController.getActivities().get(model.getParent()).getTranslateY()
        		&& value.getY() < diagramController.getActivities().get(model.getParent()).getTranslateY()
        		+ diagramController.getActivities().get(model.getParent()).getRectangle().getHeight()
        		- activityG.getRectangle().getHeight()) {
        	model.addStyle(IDiagramEditorController.POSITION_STYLE_ID,
                    lX + "|" + lY + "|"
                    + x + "|" + value.getY());
            positionProperty().set(value);
        }
    }

	private void createController() {
        final ContextMenu ctxMenu = getContextMenu();
        MenuItem editMI = getEditMI();
        MenuItem removeMI = getRemoveMI();
        //MenuItem addRelationMI = getAddRelationMI();
        editMI.setText("Ajouter un fils");
        // Show context menu.
        activityG.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
            new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    activityG.setSelected(true);
                    ctxMenu.show(activityG, event.getScreenX(), event.getScreenY());
                }
            }
        );

        // Show DialogNoteEdit when a double click is detected.
        activityG.addEventFilter(MouseEvent.MOUSE_CLICKED,
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
            	IStyle style = model.getStyle();
                String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
		        String[] positionTab = position.split("\\|");

		        //double lX = Double.valueOf(positionTab[0]);
		        double lX = Double.parseDouble(positionTab[0]);
		        //double lY = Double.valueOf(positionTab[1]);
		        double lY = Double.parseDouble(positionTab[1]);
		        //double tX = Double.valueOf(positionTab[2]);
		        double tX = Double.parseDouble(positionTab[2]);
		        //double tY = Double.valueOf(positionTab[3]);
		        double tY = Double.parseDouble(positionTab[3]);

                IActivity act = new Activity(model.getObj(), diagramController.getDiagram());
                act.addStyle(IDiagramEditorController.POSITION_STYLE_ID, lX + 10 + "|" + lX + "|" + tX + "|" + (tY + 10));
                act.setParent(model);

            	diagramController.getDiagram().addActivity(act);

            }
        });


        removeMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                diagramController.removeActivity(model);
            }
        });
/*
        addRelationMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    diagramController.createRelation(model);
            }
        });*/
        model.addPropertyChangeListener(IActivity.CREATED_ACTIVITY_PROPERTY_NAME, evt -> 
            setText()
        );

        model.addPropertyChangeListener(STYLE_CHANGED_PROPERTY_NAME, evt -> 
            setStyle((IStyle) evt.getNewValue(), false)
        );

        model.getObj().addPropertyChangeListener(STYLE_CHANGED_PROPERTY_NAME, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				String position = ((IStyle) evt.getNewValue()).getValue(IDiagramEditorController.POSITION_STYLE_ID);
		        String[] positionTab = position.split("\\|");
		        //double lX = Double.valueOf(positionTab[0]);
		        double lX = Double.parseDouble(positionTab[0]);
		        //double lY = Double.valueOf(positionTab[1]);
		        double lY = Double.parseDouble(positionTab[1]);
		        double tX = diagramController.getObjects().get(model.getObj()).lifeLineStartPosition().getX() - activityG.getWidth()/2;

		        String positionAct = model.getStyle().getValue(IDiagramEditorController.POSITION_STYLE_ID);
		        String[] positionTab1 = positionAct.split("\\|");
		        //double tY1 = Double.valueOf(positionTab1[3]);
		        double tY1 = Double.parseDouble(positionTab1[3]);

				model.addStyle(IDiagramEditorController.POSITION_STYLE_ID, lX + "|" + lY + "|" + (tX + model.getLevel() * 10) + "|" + tY1);
			}
		});

        model.getObj().addPropertyChangeListener(IActivity.PARENT_CHANGED_PROPERTY_NAME, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String position = ((IStyle) evt.getNewValue()).getValue(IDiagramEditorController.POSITION_STYLE_ID);
		        String[] positionTab = position.split("\\|");
		        
		        //double lX = Double.valueOf(positionTab[0]);
		        double lX = Double.parseDouble(positionTab[0]);
		        //double lY = Double.valueOf(positionTab[1]);
		        double lY = Double.parseDouble(positionTab[1]);
		       // double tX = Double.valueOf(positionTab[2]);
		        double tX = Double.parseDouble(positionTab[2]);
		        //double tY = Double.valueOf(positionTab[3]);
		        double tY = Double.parseDouble(positionTab[3]);

		        model.addStyle(IDiagramEditorController.POSITION_STYLE_ID, lX + "|" + lY + "|" + (tX + 10) + "|" + tY);
			}
        });

        model.addPropertyChangeListener(IActivity.RELATION_ADDED_TO_ACTIVITY_PROPERTY_NAME, evt -> 
    		setText()
    	);
    }

	@Override
    public void refresh() {
    	setText();
    }

	private void setText() {
		double height = 40;
		model.getActivitiesChilds(new HashSet<IActivity>());

		for (IActivity act : model.getActivity()) {
        	height = height + diagramController.getActivities().get(act).getRectangle().getHeight();
        }

		height = height / 2;

		if (model.getRelation().isEmpty()) {
			height = height + model.getRelation().size() * 20;
		}

        activityG.getRectangle().setHeight(height);
        activityG.getRectangle().setWidth(activityG.getRectActWidth());
        activityG.autosize();
    }

	private void setStyle(IStyle style, boolean inConstructor) {
		activityG.getRectangle().setFill(Color.web(IDiagramEditorController.BACKGROUND_COLOR_DEFAULT_ENTITY));

        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");
        
        //double lX = Double.valueOf(positionTab[0]);
        double lX = Double.parseDouble(positionTab[0]);
        //double lY = Double.valueOf(positionTab[1]);
        double lY = Double.parseDouble(positionTab[1]);
        //double tX = Double.valueOf(positionTab[2]);
        double tX = Double.parseDouble(positionTab[2]);
        //double tY = Double.valueOf(positionTab[3]);
        double tY = Double.parseDouble(positionTab[3]);

        String size = style.getValue(IDiagramEditorController.SIZE_STYLE_ID);
        if (size == null) {
        	size = activityG.getRectangle().getWidth() + "|" + activityG.getRectangle().getHeight();
        }
        String[] sizeTab = size.split("\\|");
        //double h = Double.valueOf(sizeTab[1]);
        double h = Double.parseDouble(sizeTab[1]);

        activityG.getRectangle().setHeight(h);

        activityG.setLayoutX(lX);
        activityG.setLayoutY(lY);
        activityG.setTranslateX(tX);
        activityG.setTranslateY(tY);

        if (inConstructor) {
            positionProperty().setValue(new Point2D(tX + lX, tY + lY));
        }
    }

	private void edit() {
		DialogActivityEdit dialog = new DialogActivityEdit(diagramController, model);
        dialog.showAndWait();
    }

	@Override
	public Color getTextColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadRelations() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setText(String s) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setTextColor(Color c) {
		// TODO Auto-generated method stub
	}
}
