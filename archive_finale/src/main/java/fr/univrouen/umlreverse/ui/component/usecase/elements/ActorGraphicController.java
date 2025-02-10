package fr.univrouen.umlreverse.ui.component.usecase.elements;

import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.usecase.Actor;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityGraphicController;
import fr.univrouen.umlreverse.ui.component.usecase.dialog.DialogActorEdit;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.usecase.IUsecaseController;
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

import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.STYLE_CHANGED_PROPERTY_NAME;
import static fr.univrouen.umlreverse.model.diagram.usecase.IEntityUsecase.NAME_CHANGED_PROPERTY_NAME;


public class ActorGraphicController extends AEntityGraphicController implements IActorGraphicController {

    /**
     * The Actor graphic that was controlled by this class.
     */
    private ActorGraphic ag;
    /**
     * The model of the graphic actor.
     */
    private Actor act;
    /**
     * The editor controller.
     */
    private IUsecaseController diagramController;

    /**
     * Constructor of this controller.
     * @param diagramController diagram controller
     * @param aG actor graphic
     * @param act model of the actor
     */
    public ActorGraphicController(IUsecaseController diagramController, ActorGraphic aG, Actor act) {
        super();
        Contract.check(diagramController != null,
                "L'arguments diagramController ne doit pas être nul.");
        Contract.check(aG != null);
        Contract.check(act != null);
        this.ag = aG;
        this.act = act;
        this.diagramController = diagramController;
        createController();
    }

    /**
     * Getter of Actor model.
     * @return IEntityRelation
     */
    public IEntityRelation getModel() {
        return act;
    }

    /**
     * Get the name of the Actor graphic.
     * @return String
     */
    public String getName(){
        return act.getName();
    }
    @Override
    public Color getBackgroundColor() {
        IStyle style = act.getStyle();
        String styleBackgroundColor = style.getValue(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID);
        return Color.web(styleBackgroundColor);
    }

    public Color getTextColor() {
        IStyle style = act.getStyle();
        String styleTextColor = style.getValue(IDiagramEditorController.TEXT_COLOR_STYLE_ID);
        return Color.web(styleTextColor);
    }

    public Point2D getPosition(){
        IStyle style = act.getStyle();
        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");
        double tX = Double.valueOf(positionTab[0]);
        double tY = Double.valueOf(positionTab[1]);
        return new Point2D( tX, tY);
    }
    @Override
    public void setBackgroundColor(Color c) {
        Contract.check(c != null, "L'argument c ne doit pas être nul.");
        act.addStyle(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID, c.toString());
    }


    @Override
    public void setPosition(Point2D value) {
        Contract.check(value != null, "L'argument value "
                + "ne doit pas être nul.");
        IStyle style = act.getStyle();
        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");
        double tX = Double.valueOf(positionTab[2]);
        double tY = Double.valueOf(positionTab[3]);

        act.addStyle(IDiagramEditorController.POSITION_STYLE_ID,
                value.getX() + "|" + value.getY() + "|"
                        + tX + "|" + tY);
        positionProperty().set(value);
    }

    @Override
    public void setTranslatePosition(Point2D value) {
        Contract.check(value != null, "L'argument value "
                + "ne doit pas être nul.");
        IStyle style = act.getStyle();
        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");
        double lX = Double.valueOf(positionTab[0]);
        double lY = Double.valueOf(positionTab[1]);

        act.addStyle(IDiagramEditorController.POSITION_STYLE_ID,
                lX + "|" + lY + "|"
                        + value.getX() + "|" + value.getY());
        positionProperty().set(value);

    }


    /**
     * Create the menu and controller for ActorGraphic.
     */
    private void createController() {



        final ContextMenu ctxMenu = getContextMenu();
        MenuItem editMI = getEditMI();
        MenuItem removeMI = getRemoveMI();
        MenuItem addRelationMI = getAddRelationMI();

        // Show context menu.
        ag.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
                new EventHandler<ContextMenuEvent>() {
                    @Override
                    public void handle(ContextMenuEvent event) {
                        ag.setSelected(true);
                        ctxMenu.show(ag, event.getScreenX(), event.getScreenY());
                    }
                }
        );

        // Show DialogNoteEdit when a double click is detected.
        ag.addEventFilter(MouseEvent.MOUSE_CLICKED,
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
                diagramController.removeActor(act);
            }
        });

        addRelationMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                diagramController.createRelation(act);
            }
        });

        //manage name modification
        act.addPropertyChangeListener(NAME_CHANGED_PROPERTY_NAME, evt -> {
            //ag.draw();
            ag.setName();
        });

        //manage style modification
        act.addPropertyChangeListener(STYLE_CHANGED_PROPERTY_NAME, evt -> {
            setStyle((IStyle) evt.getNewValue(), false);
            //ag.draw();
            ag.setStyleToActor();
        });

    }
    /**
     * Show DialogEditNote to edit Actor.
     */
    private void edit() {
        DialogActorEdit dialog = new DialogActorEdit(act);
        dialog.showAndWait();
    }

    /**
     * Update the component’s position to a given style.
     * @param style the style to be applied
     * @param inConstructor OMG who the hell wrote this ugly hack?
     */
    private void setStyle(IStyle style, boolean inConstructor) {
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
        ag.setLayoutX(lX);
        ag.setLayoutY(lY);
        ag.setTranslateX(tX);
        ag.setTranslateY(tY);

        if (inConstructor) {
            positionProperty().setValue(new Point2D(tX + lX, tY + lY));
        }
    }

}
