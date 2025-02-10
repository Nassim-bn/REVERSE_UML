package fr.univrouen.umlreverse.ui.component.usecase.elements;

import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.INote;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityGraphicController;
import fr.univrouen.umlreverse.ui.component.usecase.dialog.DialogNoteEdit;
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
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.usecase.IUsecaseController;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityTextGraphicController;
import fr.univrouen.umlreverse.model.diagram.usecase.INoteUsecase;


public class NoteGraphicController extends AEntityGraphicController 
        implements IEntityTextGraphicController {

// ATTRIBUTES
    private final NoteGraphic noteG;
    // modelNote is the representation of a note in the model **
    private final INoteUsecase modelNote;
    //private final Map<IViewEntity, NoteRelationGraphic> relations;
    private final IUsecaseController diagramController;
    
// CONSTRUCTORS
    public NoteGraphicController(NoteGraphic noteG, 
            IUsecaseController diagramController, INoteUsecase note) {
        super();
        Contract.check(noteG != null, 
                 "L'argument noteG ne doit pas être nul.");
        Contract.check(diagramController != null, 
                 "L'arguments diagramController ne doit pas être nul.");
        Contract.check(note != null, 
                 "L'argument note ne doit pas être nul.");
        
        this.diagramController = diagramController;
        this.modelNote = note;
        this.noteG = noteG;
        //relations = new HashMap();
        setText();
        IStyle style = modelNote.getStyle();
        setStyle(style, true);
        createController();
    }

// REQUEST
    @Override
    public Color getBackgroundColor() {
        IStyle style = modelNote.getStyle();
        String styleBackgroundColor = style.getValue(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID);
        return Color.web(styleBackgroundColor);
    }
    
    public IEntityRelation getModel() {
        return modelNote;
    }
    
    @Override
    public Color getTextColor() {
        IStyle style = modelNote.getStyle();
        String styleColor = style.getValue(IDiagramEditorController.TEXT_COLOR_STYLE_ID);
        return Color.web(styleColor);
    }

// COMMANDS 
    @Override
    public void loadRelations() {/*
        modelNote.getEntities().stream().forEach((entity) -> {
            createRelationGraphic(entity);
        });*/
    }
    @Override
    public final void setText(String s) {
        Contract.check(s != null, "L'argument text ne doit pas être nul.");
        modelNote.setText(s);
    }
    
    @Override
    public void setBackgroundColor(Color c) {
        Contract.check(c != null, "L'argument c ne doit pas être nul.");
        modelNote.addStyle(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID, c.toString());
    }
    
    @Override
    public void setTextColor(Color c) {
        Contract.check(c != null, "L'argument c ne doit pas être nul.");
        modelNote.addStyle(IDiagramEditorController.TEXT_COLOR_STYLE_ID, c.toString());
    }
    
    @Override
    public void setPosition(Point2D value) {
        Contract.check(value != null, "L'argument value "
                + "ne doit pas être nul.");
        IStyle style = modelNote.getStyle();
        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");
        double tX = Double.valueOf(positionTab[2]);
        double tY = Double.valueOf(positionTab[3]);
        
        modelNote.addStyle(IDiagramEditorController.POSITION_STYLE_ID, 
                value.getX() + "|" + value.getY() + "|" 
                + tX + "|" + tY);
        positionProperty().set(value);
    }
    
    @Override
    public void setTranslatePosition(Point2D value) {
        Contract.check(value != null, "L'argument value "
                + "ne doit pas être nul.");
        IStyle style = modelNote.getStyle();
        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");
        double lX = Double.valueOf(positionTab[0]);
        double lY = Double.valueOf(positionTab[1]);
        
        modelNote.addStyle(IDiagramEditorController.POSITION_STYLE_ID, 
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
        noteG.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
            new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    noteG.setSelected(true);
                    ctxMenu.show(noteG, event.getScreenX(), event.getScreenY()); 
                }
            }
        );
        
        // Show DialogNoteEdit when a double click is detected.
        noteG.addEventFilter(MouseEvent.MOUSE_CLICKED, 
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
                /*Set<IViewEntity> entities = new HashSet();
                modelNote.getEntities().stream().forEach((entity) -> {
                    entities.add(entity);
                });
                
                entities.stream().forEach((entity) -> {
                    modelNote.removeEntity(entity);
                });*/
                diagramController.removeNote(modelNote);
            }
        });
        
        addRelationMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    diagramController.createRelation(modelNote);
            }
        });
        
        modelNote.addPropertyChangeListener(INote.TEXT_CHANGED_PROPERTY_NAME, evt -> {
            setText();
        });
          
        modelNote.addPropertyChangeListener(INote.STYLE_CHANGED_PROPERTY_NAME, evt -> {
            setStyle((IStyle) evt.getNewValue(), false);
        });
        
        /*modelNote.addPropertyChangeListener(IClassNote.ENTITY_ADDED_PROPERTY_NAME, event -> {
            IViewEntity entity = (IViewEntity) event.getNewValue();
            createRelationGraphic(entity);
        });*/
        
        
        /*modelNote.addPropertyChangeListener(IClassNote.ENTITY_REMOVED_PROPERTY_NAME, event -> {
            
            IViewEntity entity = (IViewEntity) event.getOldValue();
            NoteRelationGraphic r = relations.get(entity);
            diagramController.removeRelationGraphic(r);
            relations.remove(entity);
            
        });*/
        
    }
    
// PRIVATE
    /**Relation
     * Show DialogEditNote to edit NoteGraphic.
     * @param
     */
    private void edit() {       
        DialogNoteEdit dialog = new DialogNoteEdit(modelNote);
        dialog.showAndWait();
    } 
    
    private void setText() {
        noteG.getText().setText(modelNote.getText());
        noteG.getRectangle().setWidth(
                    noteG.getText().getLayoutBounds().getWidth() 
                        + NoteGraphic.WIDTH_MARGE_NOTE);
        noteG.getRectangle().setHeight(
                noteG.getText().getLayoutBounds().getHeight() 
                        + NoteGraphic.HEIGHT_MARGE_NOTE);
        noteG.autosize();
    }
    
    private void setStyle(IStyle style, boolean inConstructor) {
        String styleBackgroundColor = style.getValue(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID);
        noteG.getRectangle().setFill(Color.web(styleBackgroundColor));

        String styleColor = style.getValue(IDiagramEditorController.TEXT_COLOR_STYLE_ID);
        noteG.getText().setFill(Color.web(styleColor));

        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");
        double lX = Double.valueOf(positionTab[0]);
        double lY = Double.valueOf(positionTab[1]);
        double tX = Double.valueOf(positionTab[2]);
        double tY = Double.valueOf(positionTab[3]);
        
        noteG.setLayoutX(lX);
        noteG.setLayoutY(lY);
        noteG.setTranslateX(tX);
        noteG.setTranslateY(tY);
        
        if (inConstructor) {
            positionProperty().setValue(new Point2D(tX + lX, tY + lY));
        }
    }
    /*
    private void createRelationGraphic(IViewEntity entity) {
        ObjectEntityGraphic oeg = diagramController.getOEG(entity);
        NoteRelationGraphic r = new NoteRelationGraphic(diagramController, modelNote, entity, oeg, noteG);
        r.drawInGroup(diagramController.getEditor().getCanvas());
        relations.put(entity, r);
    }*/
}
