package fr.univrouen.umlreverse.ui.view.clazz;

import fr.univrouen.umlreverse.model.diagram.clazz.view.*;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.model.project.IRelation;
import fr.univrouen.umlreverse.model.project.Relation;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.ui.component.clazz.dialog.DialogNoteEdit;
import fr.univrouen.umlreverse.ui.component.clazz.dialog.DialogOEGNew;
import fr.univrouen.umlreverse.ui.component.clazz.elements.NoteGraphic;
import fr.univrouen.umlreverse.ui.component.clazz.elements.ObjectEntityGraphic;
import fr.univrouen.umlreverse.ui.component.clazz.relations.RelationGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.INoteGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphic;
import fr.univrouen.umlreverse.ui.view.common.ADiagramEditor;
import fr.univrouen.umlreverse.ui.view.common.ADiagramEditorController;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import fr.univrouen.umlreverse.util.ErrorDialog;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import static fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram.*;
import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.NOTE_ADDED_PROPERTY_NAME;
import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.NOTE_REMOVED_PROPERTY_NAME;

public class ClassController extends ADiagramEditorController 
        implements IClassController  {

// ATTRIBUTES
    /**
     * The diagram that will correspond to the controller.
     */
    private final IClassDiagram diagram;
    /**
     * The menu item that will allow the creation of a class.
     */
    private MenuItem createClassMI;
    /**
     * The menu item that will allow the creation of an interface.
     */
    private MenuItem createInterfaceMI;
    /**
     * The menu item that will allow the creation of an enumeration.
     */
    private MenuItem createEnumMI;
    /**
     * The menu item that will allow the creation of an abstract class.
     */
    private MenuItem createAbstractClass;
    /**
     * The context menu that will contain the menu item.
     */
    private ContextMenu ctxMenuDiagram;
    /**
     * The x position of mice.
     */
    private double miceX;
    /**
     * The y position of mice.
     */
    private double miceY;

    /**
     * A map that will contain all note, the model of note as key and the graphic as value.
     */
    private final Map<INoteClass, NoteGraphic> notes;
    /**
     * A map that will contain all entity, the graphic model of entity as key and graphic as value.
     */

    private final Map<IViewEntity, ObjectEntityGraphic> viewEntities;
    /**
     * A map that contain the models of an entity, the intern model as key, the graphic model as value.
     */
    private final Map<IObjectEntity, IViewEntity> objectEntities; 

    /**
     * A map that will contain all relation, the model as key and graphic as value.
     */
    private final Map<IViewRelation, IRelationGraphic> viewRelations;
    /**
     * An object property for the creation of an note about relation.
     */
    private final ObjectProperty<INoteClass> createNoteRelation;
    /**
     * An object property for the creation of a relation.
     */

    private final ObjectProperty<IViewEntity> createEntityRelation;
    /**
     * The note's relation handler.
     */
    private EventHandler<MouseEvent> addNoteRelationEvt;
    /**
     * The entity's relation handler.
     */
    private EventHandler<MouseEvent> addEntityRelationEvt;
    
// CONSTRUCTORS

    /**
     * Constructor of the class controller.
     * @param editor the ADiagramEditor which will be correlated whith the controller
     * @param classDiagram the classDiagram which will be correlated with the controller
     */
    public ClassController(ADiagramEditor editor, IClassDiagram classDiagram) {
        super(editor);
        notes = new HashMap<>();
        viewEntities = new HashMap<>();
        viewRelations = new HashMap<>();
        diagram = classDiagram;
        createNoteRelation = new SimpleObjectProperty<>();
        createEntityRelation = new SimpleObjectProperty<>();
        objectEntities = new HashMap<>();
        createComposant();
        createController();
        
        drawDiagram();
    }

// REQUESTS
    @Override
    public IClassDiagram getDiagram() {
        return diagram;
    }
    
    @Override
    public Collection<NoteGraphic> getNotes() {
        return notes.values();
    }

    @Override
    public Collection<ObjectEntityGraphic> getObjectEntityGraphics() {
        return Collections.unmodifiableCollection(viewEntities.values());
    }
    
    @Override
    public ObjectEntityGraphic getOEG(IViewEntity entity) {
        Contract.check(entity != null, "L'argument entity ne doit pas être nul.");
        return viewEntities.get(entity);
    }

// COMMANDS
    @Override
    public void addToEntityMap(ObjectEntityGraphic entityGraphic) {
        viewEntities.put(entityGraphic.getViewEntity(), entityGraphic);
        objectEntities.put(entityGraphic.getViewEntity().getData(), entityGraphic.getViewEntity());
    }
    
    @Override
    public void createEntity(double x, double y, TypeEntity t) {
        DialogOEGNew dialogOEGNew = new DialogOEGNew(ClassController.this, t);
        dialogOEGNew.showAndWait();
        IViewEntity entity = dialogOEGNew.getViewEntity();
        if (entity != null) {
            entity.addStyle(IDiagramEditorController.POSITION_STYLE_ID, x + "|" + y + "|0|0");
        }
    }
    
    @Override
    public void createEntityRelation(IViewEntity entity) {
        createEntityRelation.setValue(entity);
        addMouseEvent(addEntityRelationEvt);
    }
    
    @Override
    public void createNote(double x, double y) {
        DialogNoteEdit dialog = new DialogNoteEdit(diagram);
        dialog.showAndWait();
        INoteClass note = dialog.getNote();
        if (note != null) {
            note.addStyle(POSITION_STYLE_ID, x + "|" + y + "|0|0");
        }
    }
    
    @Override
    public void createNoteRelation(INoteClass note) {
        createNoteRelation.setValue(note);
        addMouseEvent(addNoteRelationEvt);
    }
    
    @Override
    public void hideObjectEntity(ObjectEntityGraphic oEG) {
        Contract.check(oEG != null, "L'argument oEG ne doit pas être null.");
        diagram.hideEntity(oEG.getController().getViewEntity());
    }
    
    @Override
    public void removeNote(INoteClass note) {
        Contract.check(note != null , 
                "L'argument note ne doit pas être nul.");
        diagram.removeNote(note);
        notes.remove(note);
    }

    @Override
    public void removeObjectEntity(ObjectEntityGraphic oEG) {
        Contract.check(oEG != null, "L'argument oEG ne doit pas être null.");
        diagram.removeEntity(oEG.getController().getViewEntity());
    }

    @Override
    public void removeRelation(IViewRelation relation) {
        Contract.check(relation != null , 
                "L'argument relation ne doit pas être nul.");
        diagram.removeRelation(relation);
    }
    
    @Override
    public void removeRelationGraphic(IRelationGraphic r) {
        editor.getCanvas().getChildren().removeAll(r.getShapes());
        r.clear();
    }

// CONTROLLER
    /**
     * Add controllers on DiagramEditor
     *
     */
    private void createController() {
        createContextMenu();
        createButtonController();
        createModelListeners();
        createPropertyListeners();
    }

    /**
     * Create the context menu for the class diagram.
     */
    private void createContextMenu() {
        // Add Menu context
        editor.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> {
                    ctxMenuDiagram.hide();
                    deselectEntity();
                }
        );
        // Show Menu context
        editor.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
                event -> {
                    if (getSelectedEntity() == null) {
                        double scale = editor.getCanvas().getScale();
                        miceX = event.getX() / scale;
                        miceY = event.getY() / scale;
                        setxCursor(miceX);
                        setyCursor(miceY);
                        ctxMenuDiagram.show(editor, event.getScreenX(), event.getScreenY());
                    }
                }
        );
    }

    /**
     * Create controller for the menu item of the context menu.
     */
    private void createButtonController() {
        createClassMI.setOnAction(event -> {
            createEntity(miceX, miceY, TypeEntity.Clazz);
            deselectEntity();
        });

        createInterfaceMI.setOnAction(event -> {
            createEntity(miceX, miceY, TypeEntity.Interface);
            deselectEntity();
        });

        createEnumMI.setOnAction(event -> {
            createEntity(miceX, miceY, TypeEntity.Enumeration);
            deselectEntity();
        });

        createAbstractClass.setOnAction(actionEvent -> {
            createEntity(miceX, miceY, TypeEntity.Abstract);
            deselectEntity();
        });
    }

    /**
     * Create listener that will listen to the model.
     */
    private void createModelListeners() {
        diagram.addPropertyChangeListener(NOTE_ADDED_PROPERTY_NAME, evt -> {
            INoteClass note = (INoteClass) evt.getNewValue();
            createNoteGraphic(note);
        });

        diagram.addPropertyChangeListener("EntityAdded", evt -> {
            ViewEntity entity = (ViewEntity) evt.getNewValue();
            createEntityGraphic(entity);
        });
        
        diagram.addPropertyChangeListener("RelationAdded", evt -> {
            IViewRelation vr = (IViewRelation) evt.getNewValue();
            createRelationGraphic(vr);
        });
        
        diagram.addPropertyChangeListener(NOTE_REMOVED_PROPERTY_NAME, evt -> {
            // TODO : A finir. Il faut supprimer la relation dans le modele.
            INoteClass note = (INoteClass) evt.getOldValue();
            NoteGraphic noteG = notes.get(note);
            List<IRelationGraphic> relationsNote = noteG.getRelations();

            for (int i = 0; i < relationsNote.size(); ++i) {
                editor.getCanvas().getChildren().removeAll(relationsNote.get(i).getShapes());
            }

            for (int i = 0; i < relationsNote.size(); ++i) {
                IRelationGraphic relation = relationsNote.get(i);
                relation.getEntitySrc().removeRelation(relation);
                relation.getEntityDst().removeRelation(relation);
            }
            notes.remove(note);
            editor.getCanvas().getChildren().remove(noteG);
        });
        
        diagram.getProject().addPropertyChangeListener("EntityRemoved", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Set<IViewEntity> se = diagram.getEntities();
                se.stream().filter((ve) -> (ve.getData().equals(evt.getOldValue()))).map((ve) -> viewEntities.get(ve)).forEach((oe) -> {
                    removeObjectEntity(oe);
                });
            }
        });
        
        diagram.addPropertyChangeListener("RelationRemoved", evt -> {
            IViewRelation vr = (IViewRelation) evt.getOldValue();        
            IRelationGraphic relation = viewRelations.get(vr); 
            IEntityGraphic src = relation.getEntitySrc();
            IEntityGraphic dst = relation.getEntityDst();
            src.removeRelation(relation);
            if (dst != src) {
                dst.removeRelation(relation);
            }  
            editor.getCanvas().getChildren().removeAll(relation.getShapes());
            relation.clear();          
            viewRelations.remove(vr);
        });
        
        // Event detect an entity removed of model and remove of view.
        diagram.addPropertyChangeListener("DiagramChanged", evt -> {
            IViewEntity entity = (IViewEntity) evt.getOldValue();
            ObjectEntityGraphic oEG = viewEntities.get(entity);
            getEditor().getCanvas().getChildren().remove(oEG);
            viewEntities.remove(entity);
            objectEntities.remove(entity.getData());           
        });
    }

    /**
     * Create Property Change listener that will observe the creation of new Entity.
     */
    private void createPropertyListeners() {
        createNoteRelation.addListener(new ChangeListener<INoteClass>() {
            @Override
            public void changed(ObservableValue<? extends INoteClass> observable, 
                    INoteClass oldValue, INoteClass newValue) {
                addNoteRelationEvt = new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getSource().getClass() == ObjectEntityGraphic.class) {
                                ObjectEntityGraphic oeg = (ObjectEntityGraphic) event.getSource();
                                newValue.addEntity(oeg.getViewEntity());
                            } else {
                                ErrorDialog.showError("Erreur", 
                                        "Vous ne pouvez relier une note qu'à "
                                                + "une entité (Interface/Classe/"
                                                + "Enumeration)");
                            }
                            removeMouseEvent(addNoteRelationEvt);
                            event.consume();
                        }
                };
            }
        });

        createEntityRelation.addListener(new ChangeListener<IViewEntity>() {
            @Override
            public void changed(ObservableValue<? extends IViewEntity> observable, 
                    IViewEntity oldValue, IViewEntity newValue) {
                addEntityRelationEvt = new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getSource().getClass() == NoteGraphic.class) {
                                INoteGraphic noteG = (INoteGraphic) event.getSource();
                                getNote(noteG).addEntity(newValue);
                            } else if (event.getSource().getClass() == ObjectEntityGraphic.class) {
                                ObjectEntityGraphic oeg = (ObjectEntityGraphic) event.getSource();
                                IRelation r = new Relation(
                                        Math.random() + "", newValue.getData(), 
                                        oeg.getViewEntity().getData(), 
                                        TypeHeadArrow.None, 
                                        TypeHeadArrow.None, 
                                        TypeLineArrow.Plain);
                                diagram.getProject().addRelation(r);
                                ViewRelation viewRelation = new ViewRelation(r, diagram);
                            } else {
                                ErrorDialog.showError("Erreur", 
                                        "Vous ne pouvez relier une entité qu'à "
                                                + "une note ou une autre entité");
                            }
                            removeMouseEvent(addEntityRelationEvt);
                            event.consume();
                        }
                };
            }
        });
    }
    
    /**
     * To instancie ContextMenu and MenuItems
     */
    private void createComposant() {
        ctxMenuDiagram = new ContextMenu();
        createClassMI = new MenuItem("Créer une classe");
        createInterfaceMI = new MenuItem("Créer une interface");
        createEnumMI = new MenuItem("Créer une énumération");
        createAbstractClass = new MenuItem("Créer une classe abstraite");

        Menu createMenu = new Menu("Créer une entité");
        createMenu.getItems().add(createClassMI);
        createMenu.getItems().add(createInterfaceMI);
        createMenu.getItems().add(createEnumMI);
        createMenu.getItems().add(createAbstractClass);

        ctxMenuDiagram.getItems().add(getCreateNoteMI());
        ctxMenuDiagram.getItems().add(createMenu);
        ctxMenuDiagram.getItems().add(getZoomInMI());
        ctxMenuDiagram.getItems().add(getZoomOutMI());
    }


// PRIVATES

    /**
     * Add mouse listener to Entity.
     * @param event the Event Handler that will be add
     */
    private void addMouseEvent(EventHandler<MouseEvent> event) {
        editor.setCursor(Cursor.OPEN_HAND);
        viewEntities.values().stream().forEach((oeg) -> {
            oeg.addEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
        notes.values().stream().forEach((noteG) -> {
            noteG.addEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
        editor.addEventHandler(MouseEvent.MOUSE_CLICKED, event);
    }

    /**
     * Create an entity graphic that will correspond to the IViewEntity given in parameter.
     * @param entity the model of the entity that will be created
     */
    private void createEntityGraphic(IViewEntity entity) {
        ObjectEntityGraphic entityGraphic = new ObjectEntityGraphic(this, entity);
        viewEntities.put(entity, entityGraphic);
        objectEntities.put(entity.getData(), entity);
        entityGraphic.addEventHandler(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        entityGraphic.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());
        editor.getCanvas().getChildren().add(entityGraphic);
    }

    /**
     * Create a graphic Note that will correspond to the INote given in parameter.
     * @param note the model of the graphic note that will be created
     */
    private void createNoteGraphic(INoteClass note) {
            NoteGraphic noteG = new NoteGraphic(this, note);
            notes.put(note, noteG);
            noteG.addEventHandler(MouseEvent.MOUSE_PRESSED,
                    nodeGestures.getOnMousePressedEventHandler());
            noteG.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                    nodeGestures.getOnMouseDraggedEventHandler());
            editor.getCanvas().getChildren().add(noteG);
    }

    /**
     * Create a graphic relation that will correspond to the IViewRelation given in parameter.
     * @param vr the model of the graphic relation that will be created
     */
    private void createRelationGraphic(IViewRelation vr) {
        try {
            IRelationGraphic rg = new RelationGraphic(this, vr,
                    viewEntities.get(objectEntities.get(vr.getEntitySource())),
                    viewEntities.get(objectEntities.get(vr.getEntityTarget())));
            rg.drawInGroup(editor.getCanvas());
            viewRelations.put(vr, rg);
        } catch (IllegalArgumentException ex) {

        }
    }
    
    /**
     * Getter that give the model of a graphic note.
     * @param noteG the graphic note
     * @return INote
     */
    private INoteClass getNote(INoteGraphic noteG) {
        for (INoteClass n : notes.keySet()) {
            if (noteG == notes.get(n)) {
                return n;
            }
        }
        return null;
    }
    
    /**
     * Draw model in editor.
     */
    private void drawDiagram() {
        // ALL
        diagram.addStyle(ALL_STYLE_ID, ALL_STYLE_ID, TEXT_COLOR_STYLE_ID, TEXT_COLOR_DEFAULT);
        // NOTES
        diagram.addStyle(ALL_STYLE_ID, NOTE_STYLE_ID, POSITION_STYLE_ID, POSITION_DEFAULT);
        diagram.addStyle(ALL_STYLE_ID, NOTE_STYLE_ID, BACKGROUND_COLOR_STYLE_ID, BACKGROUND_COLOR_DEFAULT_NOTE);
        // ENTITIES
        diagram.addStyle(ALL_STYLE_ID, CLASS_STYLE_ID, BACKGROUND_COLOR_STYLE_ID, BACKGROUND_COLOR_DEFAULT_ENTITY);
        diagram.addStyle(ALL_STYLE_ID, CLASS_STYLE_ID, POSITION_STYLE_ID, POSITION_DEFAULT);
        diagram.addStyle(ALL_STYLE_ID, INTERFACE_STYLE_ID, BACKGROUND_COLOR_STYLE_ID, BACKGROUND_COLOR_DEFAULT_ENTITY);
        diagram.addStyle(ALL_STYLE_ID, INTERFACE_STYLE_ID, POSITION_STYLE_ID, POSITION_DEFAULT);
        diagram.addStyle(ALL_STYLE_ID, ENUM_STYLE_ID, BACKGROUND_COLOR_STYLE_ID, BACKGROUND_COLOR_DEFAULT_ENTITY);
        diagram.addStyle(ALL_STYLE_ID, ENUM_STYLE_ID, POSITION_STYLE_ID, POSITION_DEFAULT);
        diagram.addStyle(ALL_STYLE_ID, ABSTRACT_STYLE_ID, BACKGROUND_COLOR_STYLE_ID, BACKGROUND_COLOR_DEFAULT_ENTITY);
        diagram.addStyle(ALL_STYLE_ID, ABSTRACT_STYLE_ID, POSITION_STYLE_ID, POSITION_DEFAULT);
        // RELATIONS
        diagram.addStyle(ALL_STYLE_ID, RELATION_STYLE_ID, COLOR_STYLE_ID, COLOR_DEFAULT_RELATION);
        
        diagram.getEntities().stream().forEach((ve) -> {
            createEntityGraphic(ve);
        });  
        diagram.getNotes().stream().forEach((note) -> {
            createNoteGraphic(note);
        });   
        diagram.getRelations().stream().forEach((relation) -> {
            createRelationGraphic(relation);
        });
    }

    /**
     * Remove mouse listener of all Entities.
     * @param event the event handler that will be remove
     */
    private void removeMouseEvent(EventHandler<MouseEvent> event) {
        editor.setCursor(Cursor.DEFAULT);
        notes.values().stream().forEach((dst) -> {
            dst.removeEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
        viewEntities.values().stream().forEach((OEG) -> {
            OEG.removeEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
        editor.removeEventHandler(MouseEvent.MOUSE_CLICKED, event);
    }
}
