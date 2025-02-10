package fr.univrouen.umlreverse.ui.component.sequence.elements;

import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.STYLE_CHANGED_PROPERTY_NAME;
import static fr.univrouen.umlreverse.model.diagram.usecase.IEntityUsecase.NAME_CHANGED_PROPERTY_NAME;

import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.model.diagram.sequence.SequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityGraphicController;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityTextGraphicController;
import fr.univrouen.umlreverse.ui.component.sequence.dialog.DialogObjectEdit;
import fr.univrouen.umlreverse.ui.component.usecase.elements.NoteGraphic;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;
import fr.univrouen.umlreverse.ui.view.sequence.SequenceController;
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

public class ObjectGraphicController extends AEntityGraphicController
		implements IEntityTextGraphicController, IObjectGraphicController {

	// ATTRIBUTES
    private final ObjectGraphic objectG;
    private final IObject model;
    private final ISequenceController diagramController;

// CONSTRUCTOR
    public ObjectGraphicController(ObjectGraphic objectG, ISequenceController diagramController, IObject model) {
        super();
        Contract.check(objectG != null,
                 "L'argument noteG ne doit pas être nul.");
        Contract.check(diagramController != null,
                 "L'arguments diagramController ne doit pas être nul.");
        Contract.check(model != null,
                 "L'argument note ne doit pas être nul.");

        this.diagramController = diagramController;
        this.model = model;
        this.objectG = objectG;
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

    public IEntityRelation getModel() {
        return model;
    }

    @Override
    public Color getTextColor() {
        IStyle style = model.getStyle();
        String styleColor = style.getValue(IDiagramEditorController.TEXT_COLOR_STYLE_ID);
        return Color.web(styleColor);
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
        model.setName(s);
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

    @Override
    public void setTranslatePosition(Point2D value) {
        Contract.check(value != null, "L'argument value "
                + "ne doit pas être nul.");
        IStyle style = model.getStyle();
        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");
        double y = Double.valueOf(positionTab[3]);
        double lX = Double.valueOf(positionTab[0]);
        double lY = Double.valueOf(positionTab[1]);

        model.addStyle(IDiagramEditorController.POSITION_STYLE_ID,
                lX + "|" + lY + "|"
                + value.getX() + "|" + y);
        positionProperty().set(value);
    }

// CONTROLLER
    private void createController() {
        final ContextMenu ctxMenu = getContextMenu();
        MenuItem editMI = getEditMI();
        MenuItem removeMI = getRemoveMI();
        MenuItem addRelationMI = getAddRelationMI();

        // Show context menu.
        objectG.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
            new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    objectG.setSelected(true);
                    ctxMenu.show(objectG, event.getScreenX(), event.getScreenY());
                }
            }
        );

        // Show DialogNoteEdit when a double click is detected.
        objectG.addEventFilter(MouseEvent.MOUSE_CLICKED,
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
                diagramController.removeObject(model);
            }
        });

        addRelationMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    diagramController.createRelation(model);
            }
        });
        model.addPropertyChangeListener(NAME_CHANGED_PROPERTY_NAME, evt -> {
            setText();
        });

        model.addPropertyChangeListener(IObject.STEREOTYPE_CHANGED_PROPERTY_NAME, evt -> {
            setText();
        });

        model.addPropertyChangeListener(IObject.RELATION_ADDED_PROPERTY_NAME, evt -> {
            setText();
        });
        model.getEntity().addPropertyChangeListener("NameChanged", evt -> {
        	setText();
        });
        model.addPropertyChangeListener(STYLE_CHANGED_PROPERTY_NAME, evt -> {
            setStyle((IStyle) evt.getNewValue(), false);
        });
    }

// PRIVATE
    /**Relation
     * Show DialogEditNote to edit NoteGraphic.
     * @param
     */
    private void edit() {
        DialogObjectEdit dialog = new DialogObjectEdit(diagramController, model);
        dialog.showAndWait();
    }

    private void setText() {
        objectG.getText().setText(model.getFullName());
        objectG.getRectangle().setWidth(
                    objectG.getText().getLayoutBounds().getWidth()
                        + NoteGraphic.WIDTH_MARGE_NOTE);
        objectG.getRectangle().setHeight(
                objectG.getText().getLayoutBounds().getHeight()
                        + NoteGraphic.HEIGHT_MARGE_NOTE);
        objectG.getLifeLine().setStartX(objectG.getRectangle().getX()
        		+ objectG.getRectangle().getWidth()/2);
        objectG.getLifeLine().setStartY(objectG.getRectangle().getY()
        		+ objectG.getRectangle().getHeight());
        objectG.getLifeLine().setEndX(objectG.getLifeLine().getStartX());
        // TODO scale avec les cadre de vie
        if (diagramController.getObjects().size() == 0) {
        	objectG.getLifeLine().setEndY(objectG.getLifeLine().getStartY() + objectG.getTranslateY() + objectG.getRectangle().getHeight() + 20);
        } else {
        	 objectG.getLifeLine().setEndY(diagramController.getNextYRelationToObjectPosition());
        }
        objectG.autosize();
    }

    private void setStyle(IStyle style, boolean inConstructor) {
        String styleBackgroundColor = style.getValue(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID);
        if (styleBackgroundColor == null) {
        	styleBackgroundColor = IDiagramEditorController.BACKGROUND_COLOR_DEFAULT_ENTITY;
        	getModel().addStyle(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID, styleBackgroundColor);
        }
        objectG.getRectangle().setFill(Color.web(styleBackgroundColor));
        String styleColor = style.getValue(IDiagramEditorController.TEXT_COLOR_STYLE_ID);
        if (styleColor == null) {
        	styleColor = IDiagramEditorController.TEXT_COLOR_DEFAULT;
        	getModel().addStyle(IDiagramEditorController.TEXT_COLOR_STYLE_ID, styleColor);
        }
        objectG.getText().setFill(Color.web(styleColor));

        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");
        double lX = Double.valueOf(positionTab[0]);
        double lY = Double.valueOf(positionTab[1]);
        double tX = Double.valueOf(positionTab[2]);
        double tY = Double.valueOf(positionTab[3]);

        objectG.setLayoutX(lX);
        objectG.setLayoutY(lY);
        objectG.setTranslateX(tX);
        objectG.setTranslateY(tY);

        if (inConstructor) {
            positionProperty().setValue(new Point2D(tX + lX, tY + lY));
        }
    }

}
