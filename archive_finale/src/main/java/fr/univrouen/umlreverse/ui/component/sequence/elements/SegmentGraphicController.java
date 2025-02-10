package fr.univrouen.umlreverse.ui.component.sequence.elements;

import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.STYLE_CHANGED_PROPERTY_NAME;
import static fr.univrouen.umlreverse.model.diagram.usecase.IEntityUsecase.NAME_CHANGED_PROPERTY_NAME;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.model.diagram.sequence.IRelationToObject;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegment;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegmentCommon;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegmentRef;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegmentWithElse;
import fr.univrouen.umlreverse.model.diagram.sequence.RelationToObject;
import fr.univrouen.umlreverse.model.diagram.sequence.Segment;
import fr.univrouen.umlreverse.model.diagram.sequence.SequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.Segment.SegmentType;
import fr.univrouen.umlreverse.model.diagram.sequence.SegmentWithElse;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityGraphicController;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityTextGraphicController;
import fr.univrouen.umlreverse.ui.component.sequence.dialog.DialogObjectEdit;
import fr.univrouen.umlreverse.ui.component.sequence.dialog.DialogSegmentEdit;
import fr.univrouen.umlreverse.ui.component.usecase.elements.NoteGraphic;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;
import fr.univrouen.umlreverse.util.Contract;
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
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class SegmentGraphicController extends AEntityGraphicController
implements IEntityTextGraphicController, ISegmentGraphicController {

	private final SegmentGraphic segmentG;
	private final ISegmentCommon model;
	 private final ISequenceController diagramController;

	public SegmentGraphicController(SegmentGraphic segmentGraphic, ISequenceController diagram, ISegmentCommon segments2) {
		 super();
	        Contract.check(segmentGraphic != null,
	                 "L'argument noteG ne doit pas être nul.");
	        Contract.check(diagram != null,
	                 "L'arguments diagramController ne doit pas être nul.");
	        Contract.check(segments2 != null,
	                 "L'argument note ne doit pas être nul.");

	        this.diagramController = diagram;
	        this.model = segments2;
	        this.segmentG = segmentGraphic;
	        setPosition();
	        setText();
	        IStyle style = model.getStyle();
	        setStyle(style, true);
	        createController();
	}

	// REQUEST
    @Override
    public Color getBackgroundColor() {
        IStyle style = model.getStyle();
        String styleBackgroundColor = style.getValue(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID);
        return Color.web(styleBackgroundColor);
    }

    @Override
    public ISegmentCommon getModel() {
        return model;
    }

    @Override
    public Color getTextColor() {
        IStyle style = model.getStyle();
        String styleColor = style.getValue(IDiagramEditorController.TEXT_COLOR_STYLE_ID);
        return Color.web(styleColor);
    }

    @Override
    public ISequenceController getDiagramController() {
    	return diagramController;
    }

// COMMANDS

    @Override
    public void refresh() {
    	setText();
    }

    @Override
    public void loadRelations() {
    }

    @Override
    public final void setText(String s) {
        Contract.check(s != null, "L'argument text ne doit pas être nul.");
        model.setCondition(s);
    }

    @Override
    public void setBackgroundColor(Color c) {
        Contract.check(c != null, "L'argument c ne doit pas être nul.");
        model.addStyle(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID, c.toString());
    }

    @Override
    public void setTextColor(Color c) {
        Contract.check(c != null, "L'argument c ne doit pas être nul.");
        model.addStyle(IDiagramEditorController.TEXT_COLOR_STYLE_ID, c.toString());
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
    
    public void setPosition() {
    	double ymax = 0;

		if (model.getParent() != null) {
			ymax = ymax + diagramController.getSegments().get(model.getParent()).getTranslateY() + 25;
			for (ISegmentCommon seg : model.getParent().getSegments()) {
				if (!seg.equals(model)) {
					ymax = ymax + diagramController.getSegments().get(seg).getRectangle().getHeight() + 10;
				}
			}
		} else {
			ymax = diagramController.getNextYRelationToObjectPosition();
		}
		Point2D srcPoint = diagramController.getObjects().get(model.getStart()).lifeLineStartPosition();
		Point2D dstPoint = diagramController.getObjects().get(model.getEnd()).lifeLineStartPosition();

		double xmax = Double.min(srcPoint.getX(), dstPoint.getX()) - 40;
		xmax = xmax + 10 * model.getLevel();
		model.addStyle(IDiagramEditorController.POSITION_STYLE_ID, 0 + "|" + 0 + "|" + xmax + "|" + ymax);
    }

    @Override
    public void setTranslatePosition(Point2D value) {
        Contract.check(value != null, "L'argument value "
                + "ne doit pas être nul.");
        IStyle style = model.getStyle();
        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");
        double lX = Double.valueOf(positionTab[0]);
        double lY = Double.valueOf(positionTab[1]);
        if ((value.getY() >
        diagramController.getObjects().values().stream().findFirst().get().getTranslateY() +
        diagramController.getObjects().values().stream().findFirst().get().getRectangle().getHeight()
        && model.getParent() == null) || (model.getParent() != null)
        		&& value.getY() > diagramController.getSegments().get(model.getParent()).getTranslateY()
        		&& value.getY() < diagramController.getSegments().get(model.getParent()).getTranslateY()
        		+ diagramController.getSegments().get(model.getParent()).getRectangle().getHeight()
        		- segmentG.getRectangle().getHeight()) {
	        model.addStyle(IDiagramEditorController.POSITION_STYLE_ID,
	                lX + "|" + lY + "|"
	                + segmentG.getTranslateX() + "|" + value.getY());
	        positionProperty().set(value);
        }
    }

// CONTROLLER
    private void createController() {
        final ContextMenu ctxMenu = getContextMenu();
        MenuItem editMI = getEditMI();
        MenuItem removeMI = getRemoveMI();
		//TODO relation note-segment
        ctxMenu.getItems().remove(getAddRelationMI());
        if (model.getSegmentType().equals(SegmentType.ALT)) {
        	MenuItem elseMI = new MenuItem("ajouter un else...");
        	ctxMenu.getItems().add(elseMI);
        	elseMI.setOnAction(e -> {
        		
        		addElse();
        	});
        }
        // Show context menu.
        segmentG.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
            new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    segmentG.setSelected(true);
                    ctxMenu.show(segmentG, event.getScreenX(), event.getScreenY());
                }
            }
        );

        // Show DialogNoteEdit when a double click is detected.
        segmentG.addEventFilter(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                	Optional<Line> l = segmentG.getElses().keySet().stream().filter(line -> (line.getStartY() <= event.getY()))
                        	.findFirst();
                	if (l.isPresent()) {
                		ISegment els = segmentG.getElses().get(l.get());
                		DialogSegmentEdit dialog = new DialogSegmentEdit(diagramController, els);
                        dialog.showAndWait();
                	} else {
                		edit();
                	}
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
                diagramController.removeSegment(model);
            }
        });
        model.addPropertyChangeListener(ISegment.CONDITION_CHANGED_PROPERTY_NAME, evt -> {
            setText();
        });


        model.addPropertyChangeListener(STYLE_CHANGED_PROPERTY_NAME, evt -> {
            setStyle((IStyle) evt.getNewValue(), false);
        });

        model.addPropertyChangeListener(ISegment.RELATION_ADDED_TO_SEGMENT_PROPERTY_NAME, evt -> {
    		setText();
    	});
        
        model.addPropertyChangeListener(ISegment.RELATION_REMOVED_TO_SEGMENT_PROPERTY_NAME, evt -> {
    		setText();
    	});

        diagramController.getObjects().get(model.getStart()).positionProperty().addListener(new ChangeListener<Point2D>() {

			@Override
			public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
				Point2D srcPoint = diagramController.getObjects().get(model.getStart()).lifeLineStartPosition();
				Point2D dstPoint = diagramController.getObjects().get(model.getEnd()).lifeLineStartPosition();
				double width = Math.abs(srcPoint.getX()
						- dstPoint.getX());
		        segmentG.getRectangle().setWidth(width + 150 - 20 * model.getLevel());
				if (srcPoint.getX() < dstPoint.getX()) {
					segmentG.setTranslateX(srcPoint.getX() - 40 + 10 * model.getLevel());
				}

			}
		});

        diagramController.getObjects().get(model.getEnd()).positionProperty().addListener(new ChangeListener<Point2D>() {

			@Override
			public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
				Point2D srcPoint = diagramController.getObjects().get(model.getStart()).lifeLineStartPosition();
				Point2D dstPoint = diagramController.getObjects().get(model.getEnd()).lifeLineStartPosition();
				double width = Math.abs(srcPoint.getX()
						- dstPoint.getX());
		        segmentG.getRectangle().setWidth(width + 150 - 20 * model.getLevel());
				if (srcPoint.getX() > dstPoint.getX()) {
					segmentG.setTranslateX(dstPoint.getX() - 40);
				}
			}
		});

    }

// PRIVATE
    /**Relation
     * Show DialogEditNote to edit NoteGraphic.
     * @param
     */
    private void edit() {
        DialogSegmentEdit dialog = new DialogSegmentEdit(diagramController, model);
        dialog.showAndWait();
    }

    private void addElse() {
    	ISegment els = new Segment(SegmentType.ELSE, model.getDiagram());
    	els.setStart(model.getStart());
    	els.setEnd(model.getEnd());
    	DialogSegmentEdit dialog = new DialogSegmentEdit(diagramController, els);
        dialog.showAndWait();
    	((SegmentWithElse) model)
		.addElseToList(els);
    	Line elseLine = new Line();
    	elseLine.setStartX(segmentG.getRectangle().getTranslateX());
    	elseLine.setEndX(segmentG.getRectangle().getTranslateX() + segmentG.getRectangle().getWidth());
    	elseLine.setStartY(segmentG.getRectangle().getHeight());
    	elseLine.setEndY(segmentG.getRectangle().getHeight());
    	elseLine.setStroke(Color.web(IDiagramEditorController.COLOR_STROK_SHAPE));
    	elseLine.getStrokeDashArray().add(5d);
    	Text elseText = new Text("[" + els.getCondition() + "]");
    	elseText.setX(elseLine.getStartX() + 15);
    	elseText.setY(elseLine.getStartY() + 15);
    	segmentG.getElses().put(elseLine, els);
    	segmentG.getRectangle().setHeight(segmentG.getRectangle().getHeight() * 2);
    	
    	segmentG.getPane().getChildren().add(elseLine);
    	segmentG.getPane().getChildren().add(elseText);
    }
    
    private void setText() {
        segmentG.getText().setText(model.getSegmentType().getType());
        segmentG.getText().setX(5);
        segmentG.getText().setY(15);
        segmentG.getRectangleTitle().setHeight(segmentG.getText().getLayoutBounds().getHeight()
                + 5);
        segmentG.getRectangleTitle().setWidth(segmentG.getText().getLayoutBounds().getWidth()
                + 10);
        if (!model.getCondition().equals("")) {
        	segmentG.getText().setText(model.getSegmentType().getType() + "    [" + model.getCondition() + "]");
        }
        double height = 50;
        if (!model.getSegmentType().equals(SegmentType.REF)) {
	        for (ISegmentCommon seg : ((ISegment) model).getSegments()) {
	        	height = height + diagramController.getSegments().get(seg).getRectangle().getHeight();
	        }
	        height = height + 20 * ((ISegment) model).getRelations().size();
	        if (model.getSegmentType().equals(SegmentType.ALT)) {
	        	height = height * (((SegmentWithElse) model).getElseList().size() + 1);
	        }
        } else {
        	segmentG.getSequenceName().setText(((ISegmentRef) model).getDiagramContained().getName());
        	segmentG.getSequenceName().setX(5);
        	segmentG.getSequenceName().setY(35);
        }

        segmentG.getRectangle().setHeight(height);
        // TODO créer une constante pour 150
        Point2D srcPoint = diagramController.getObjects().get(model.getStart()).lifeLineStartPosition();
		Point2D dstPoint = diagramController.getObjects().get(model.getEnd()).lifeLineStartPosition();
		double width = Math.abs(srcPoint.getX()
				- dstPoint.getX());
        segmentG.getRectangle().setWidth(width + 150 - 20 * model.getLevel());
        segmentG.autosize();
    }

    private void setStyle(IStyle style, boolean inConstructor) {
        segmentG.getRectangle().setFill(Color.TRANSPARENT);
        segmentG.getRectangleTitle().setFill(Color.web(IDiagramEditorController.BACKGROUND_COLOR_DEFAULT_ENTITY));
        segmentG.getText().setFill(Color.web(IDiagramEditorController.TEXT_COLOR_DEFAULT));

        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");
        double lX = Double.valueOf(positionTab[0]);
        double lY = Double.valueOf(positionTab[1]);
        double tX = Double.valueOf(positionTab[2]);
        double tY = Double.valueOf(positionTab[3]);

        segmentG.setLayoutX(lX);
        segmentG.setLayoutY(lY);
        segmentG.setTranslateX(tX);
        segmentG.setTranslateY(tY);

        if (inConstructor) {
            positionProperty().setValue(new Point2D(tX + lX, tY + lY));
        }
    }

}
