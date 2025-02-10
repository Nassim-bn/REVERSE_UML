/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.usecase.elements;

import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.STYLE_CHANGED_PROPERTY_NAME;
import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import static fr.univrouen.umlreverse.model.diagram.usecase.IEntityUsecase.NAME_CHANGED_PROPERTY_NAME;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecase;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityGraphicController;
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
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityTextGraphicController;
import fr.univrouen.umlreverse.ui.component.usecase.dialog.DialogUsecaseEdit;


public class UsecaseGraphicController extends AEntityGraphicController implements IEntityTextGraphicController {
    
// ATTRIBUTES
    private final UsecaseGraphic usecaseG;
    private final IUsecase model;
    private final IUsecaseController diagramController;
 
// CONSTRUCTOR
    public UsecaseGraphicController(UsecaseGraphic usecaseG, 
            IUsecaseController diagramController, IUsecase usecase) {
        super();
        Contract.check(usecaseG != null, 
                 "L'argument noteG ne doit pas être nul.");
        Contract.check(diagramController != null, 
                 "L'arguments diagramController ne doit pas être nul.");
        Contract.check(usecase != null, 
                 "L'argument note ne doit pas être nul.");
    
        this.diagramController = diagramController;
        this.model = usecase;
        this.usecaseG = usecaseG;
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
        double lX = Double.valueOf(positionTab[0]);
        double lY = Double.valueOf(positionTab[1]);
        
        model.addStyle(IDiagramEditorController.POSITION_STYLE_ID, 
                lX + "|" + lY + "|" 
                + value.getX() + "|" + value.getY());    
        positionProperty().set(value);
    }
 
// CONTROLLER 
    private void createController() {
        final ContextMenu ctxMenu = getContextMenu();
        MenuItem editMI = getEditMI();
        MenuItem removeMI = getRemoveMI();
        MenuItem addRelationMI = getAddRelationMI();
        
        // Show context menu.
        usecaseG.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
            new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    usecaseG.setSelected(true);
                    ctxMenu.show(usecaseG, event.getScreenX(), event.getScreenY()); 
                }
            }
        );
        
        // Show DialogNoteEdit when a double click is detected.
        usecaseG.addEventFilter(MouseEvent.MOUSE_CLICKED, 
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
                diagramController.removeUsecase(model);
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
        DialogUsecaseEdit dialog = new DialogUsecaseEdit(model);
        dialog.showAndWait();
    } 
    
    private void setText() {
        usecaseG.getText().setText(model.getName());
        usecaseG.getEllipse().setRadiusX(
                    usecaseG.getText().getLayoutBounds().getWidth() 
                        + NoteGraphic.WIDTH_MARGE_NOTE);
        usecaseG.getEllipse().setRadiusY(
                usecaseG.getText().getLayoutBounds().getHeight() 
                        + NoteGraphic.HEIGHT_MARGE_NOTE);
        usecaseG.autosize();
    }
    
    private void setStyle(IStyle style, boolean inConstructor) {
        String styleBackgroundColor = style.getValue(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID);
        usecaseG.getEllipse().setFill(Color.web(styleBackgroundColor));

        String styleColor = style.getValue(IDiagramEditorController.TEXT_COLOR_STYLE_ID);
        usecaseG.getText().setFill(Color.web(styleColor));

        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");
        double lX = Double.valueOf(positionTab[0]);
        double lY = Double.valueOf(positionTab[1]);
        double tX = Double.valueOf(positionTab[2]);
        double tY = Double.valueOf(positionTab[3]);
        
        usecaseG.setLayoutX(lX);
        usecaseG.setLayoutY(lY);
        usecaseG.setTranslateX(tX);
        usecaseG.setTranslateY(tY);
        
        if (inConstructor) {
            positionProperty().setValue(new Point2D(tX + lX, tY + lY));
        }
    }
}
