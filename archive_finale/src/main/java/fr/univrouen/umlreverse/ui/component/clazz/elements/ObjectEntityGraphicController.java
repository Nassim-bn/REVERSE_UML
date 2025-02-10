package fr.univrouen.umlreverse.ui.component.clazz.elements;

import fr.univrouen.umlreverse.ui.component.clazz.dialog.DialogOEGEdit;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewPackage;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegment;
import fr.univrouen.umlreverse.model.diagram.sequence.Segment.SegmentType;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityGraphicController;
import fr.univrouen.umlreverse.ui.component.packag.elements.PackageGraphic;
import fr.univrouen.umlreverse.ui.component.sequence.elements.SegmentGraphic;
import fr.univrouen.umlreverse.ui.view.clazz.ClassController;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.packag.PackageController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * The controller used to draw an ObjectEntity.
 */
public class ObjectEntityGraphicController extends AEntityGraphicController
        implements IObjectGraphicController {
	// ATTRIBUTES
	/** The component associated with this controller. */
    private final ObjectEntityGraphic entity;
    /** The entity represented by the component. */
    private final IViewEntity model;
    /** The diagram controller managing the entity. */
    private final ClassController diagramController;

    // CONSTRUCTORS
    /**
     * Creates the controller and sets the values of the OEG.
     * @param entity the view associated.
     * @param diagramController the diagram currently in use.
     * @param model
     */
    public ObjectEntityGraphicController(ObjectEntityGraphic entity,
                                         ClassController diagramController, IViewEntity model) {
        super();
        Contract.check(entity != null, "L'argument entity ne doit pas être nul.");
        Contract.check(model != null, "L'argument model ne doit pas être nul.");
        Contract.check(diagramController != null, "L'argument diagramController ne doit pas être nul.");

        this.entity = entity;
        this.diagramController = diagramController;
        this.model = model;

        IStyle style = model.getStyle();
        changePosition(style, true);
        createController();
    }

    // QUERIES
    @Override
    public IViewEntity getViewEntity() {
        return model;
    }

    @Override
    public ClassController getDiagrController() {
    	return diagramController;
    }

    @Override
    public Color getBackgroundColor() {
        IStyle style = model.getStyle();
        String styleBackgroundColor = style.getValue(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID);
        return Color.web(styleBackgroundColor);
    }

    @Override
    public Color getTextColor() {
        IStyle style = model.getStyle();
        String styleColor = style.getValue(IDiagramEditorController.TEXT_COLOR_STYLE_ID);
        return Color.web(styleColor);
    }

    // COMMANDS
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
        positionProperty().set(new Point2D(value.getX() + tX, value.getY() + tY));
    }

    @Override
    public void setTranslatePosition(Point2D value) {
        Contract.check(value != null, "L'argument value "
                + "ne doit pas être nul.");
        IStyle style = model.getStyle();
        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        double lX = 0;
        double lY = 0;
        if (position != null) {
            String[] positionTab = position.split("\\|");
            lX = Double.valueOf(positionTab[0]);
            lY = Double.valueOf(positionTab[1]);
        }

        model.addStyle(IDiagramEditorController.POSITION_STYLE_ID,
                lX + "|" + lY + "|"
                + value.getX() + "|" + value.getY());
        positionProperty().set(new Point2D(value.getX() + lX, value.getY() + lY));
    }

    // TOOLS
    /**
     * Creates and populates the context menu associated with the component.
     */
    private void createController() {
        final ContextMenu ctxMenu = getContextMenu();
        MenuItem editMI = getEditMI();
        MenuItem removeMI = getRemoveMI();
        // Show context menu.
        entity.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
        		new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                entity.setSelected(true);
                ctxMenu.show(entity, event.getScreenX(), event.getScreenY());
            }
        });

        // Show DialogNoteEdit when a double click is detected.
        entity.addEventFilter(MouseEvent.MOUSE_CLICKED,
                event -> {
                    if (event.getButton() == MouseButton.PRIMARY
                            && event.getClickCount() == 2) {
                        edit();
                    }
                }
        );
        editMI.setOnAction(event -> edit());
        removeMI.setOnAction(event ->
            diagramController.hideObjectEntity(entity)
        );
        getAddRelationMI().setOnAction(
        		event -> diagramController.createEntityRelation(model));
        getViewEntity().addPropertyChangeListener(event -> entity.refresh());

        model.addPropertyChangeListener("StyleChanged",
        		event -> changePosition((IStyle) event.getNewValue(), false));
    }

    /**
     * Show DialogOEGEdit to edit ObjectEntityGraphic.
     */
    private void edit() {
        DialogOEGEdit dialog = new DialogOEGEdit(model);
        dialog.showAndWait();
    }

    /**
     * Update the component’s position to a given style.
     * @param style the style to be applied
     * @param inConstructor OMG who the hell wrote this ugly hack?
     */
    private void changePosition(IStyle style, boolean inConstructor) {
        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        double lX = 0;
        double lY = 0;
        double tX = 0;
        double tY = 0;
        if (position != null) {
            String[] positionTab = position.split("\\|");
            lX = Double.valueOf(positionTab[0]);
            lY = Double.valueOf(positionTab[1]);
            tX = Double.valueOf(positionTab[2]);
            tY = Double.valueOf(positionTab[3]);
        }
        entity.setLayoutX(lX);
        entity.setLayoutY(lY);
        entity.setTranslateX(tX);
        entity.setTranslateY(tY);

        if (inConstructor) {
            positionProperty().setValue(new Point2D(tX + lX, tY + lY));
        }
    }
}
