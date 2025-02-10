/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.view.usecase;


import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.INote;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.usecase.*;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityRelationGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphic;
import fr.univrouen.umlreverse.ui.component.usecase.dialog.DialogActorEdit;
import fr.univrouen.umlreverse.ui.component.usecase.dialog.DialogNoteEdit;
import fr.univrouen.umlreverse.ui.component.usecase.dialog.DialogUsecaseEdit;
import fr.univrouen.umlreverse.ui.component.usecase.elements.ActorGraphic;
import fr.univrouen.umlreverse.ui.component.usecase.elements.NoteGraphic;
import fr.univrouen.umlreverse.ui.component.usecase.elements.UsecaseGraphic;
import fr.univrouen.umlreverse.ui.component.usecase.relations.RelationGraphic;
import fr.univrouen.umlreverse.ui.view.common.ADiagramEditor;
import fr.univrouen.umlreverse.ui.view.common.ADiagramEditorController;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import fr.univrouen.umlreverse.util.ErrorDialog;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.ALL_STYLE_ID;
import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.NOTE_ADDED_PROPERTY_NAME;
import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.NOTE_REMOVED_PROPERTY_NAME;
import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.NOTE_STYLE_ID;
import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.RELATION_ADDED_PROPERTY_NAME;
import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.RELATION_REMOVED_PROPERTY_NAME;
import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.RELATION_STYLE_ID;
import static fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram.*;

public class UsecaseController extends ADiagramEditorController implements IUsecaseController {
    // ATTRIBUTES
    private final IUsecaseDiagram diagram;
    private final Map<INoteUsecase, NoteGraphic> notes;
    private final Map<IUsecase, UsecaseGraphic> usecases;
    private final Map<IRelation, IRelationGraphic> relations;
    private final Map<IActor, ActorGraphic> actors;
    /**
     * An object property for the creation of an note about relation.
     */
    private final ObjectProperty<IEntityRelation> createRelation;
    private ContextMenu ctxMenuDiagram;
    private double miceX;
    private double miceY;
    /**
     * The note's relation handler.
     */

    private EventHandler<MouseEvent> addRelationEvt;

    // BUTTONS
    /**
     * The menu item that will allow the creation of a usecase.
     */
    private MenuItem createUsecaseMI;

    private MenuItem createActorMI;

    // CONSTRUTORS
    public UsecaseController(ADiagramEditor editor, IUsecaseDiagram usecaseDiagram) {
        super(editor);
        diagram = usecaseDiagram;
        notes = new HashMap<>();
        usecases = new HashMap<>();
        relations = new HashMap<>();
        actors = new HashMap<>();
        createRelation = new SimpleObjectProperty<>();
        createComposant();
        createController();
        drawDiagram();
    }

    // REQUESTS
    @Override
    public Map<IActor, ActorGraphic> getActors() {
        return actors;
    }

    @Override
    public IUsecaseDiagram getDiagram() {
        return diagram;
    }

    @Override
    public Collection<NoteGraphic> getNotes() {
        return notes.values();
    }

    // COMMANDS
    @Override
    public void createNote(double x, double y) {
        DialogNoteEdit dialog = new DialogNoteEdit(diagram);
        dialog.showAndWait();
        INoteUsecase note = dialog.getNote();
        if (note != null) {
        	int pos = 10;
        	if (getDiagram().getNotes().size() < 7) {
            	pos += 60 * getDiagram().getNotes().size();
        	} else {
        		pos = 70;
        	}
            note.addStyle(POSITION_STYLE_ID, x + "|" + y + "|" + pos + "|10");
        }
    }

    @Override
    public void createUsecase(double x, double y) {
        DialogUsecaseEdit dialog = new DialogUsecaseEdit(this.diagram);
        dialog.showAndWait();
        IUsecase usecase = dialog.getUsecase();
        int posX = 250;
        int posY = 10;
        if (usecase != null) {
        	if (getDiagram().getUsecases().size() < 5) {
            	posY += getDiagram().getUsecases().size() * 80;
        	} else {
        		posY = 220;
        		posX = 140;
        	}
            usecase.addStyle(IDiagramEditorController.POSITION_STYLE_ID, x + "|" + y + "|" + posX + "|" + posY);
        }
    }

    @Override
    public void createRelation(IEntityRelation entity) {
        createRelation.setValue(entity);
        addMouseEvent(addRelationEvt);
    }

    @Override
    public void removeNote(INoteUsecase note) {
        Contract.check(note != null,
                "note must not be null.");
        diagram.removeNote(note);
        notes.remove(note);
    }

    @Override
    public void removeRelation(IRelation relation) {
        Contract.check(relation != null,
                "L'argument relation ne doit pas être nul.");
        diagram.removeRelation(relation);
    }

    @Override
    public void removeUsecase(IUsecase usecase) {
        Contract.check(usecase != null,
                "usecase must not be null.");
        diagram.removeUsecase(usecase);
        usecases.remove(usecase);
    }

	@Override
	public void removeActor(IActor act){
	    Contract.check(act != null, "The actor must not be null.");
	    diagram.removeActor((Actor)act);
	    actors.remove(act);
	}

    @Override
    public void createActor(double x, double y) {
        DialogActorEdit dae = new DialogActorEdit(diagram);
        dae.showAndWait();

        Actor act = (Actor) dae.getActor();
        int posY = 20;
        int posX = 20;
        if (act != null) {
        	if (getDiagram().getActors().size() < 5) {
            	posY = getDiagram().getActors().size() * 90;
        	} else {
        		posY = 150;
        		posX = 150;
        	}
    		act.addStyle(IDiagramEditorController.POSITION_STYLE_ID, x + "|" + y + "|" + posX + "|" + posY);
        }
    }

    // CONTROLLER
    /**
     * Add controllers on DiagramEditor
     */
    private void createController() {
        createContextMenu();
        createButtonController();
        createModelListeners();
        createPropertyListeners();
    }

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

    private void createButtonController() {
        createActorMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createActor(miceX, miceY);
                deselectEntity();
            }

        });

        createUsecaseMI.setOnAction(event -> {
            createUsecase(miceX, miceY);
            deselectEntity();
        });
    }

    private void createModelListeners() {
        // NOTE
        diagram.addPropertyChangeListener(NOTE_ADDED_PROPERTY_NAME, evt -> {
            INoteUsecase note = (INoteUsecase) evt.getNewValue();
            createNoteGraphic(note);
        });

        diagram.addPropertyChangeListener(NOTE_REMOVED_PROPERTY_NAME, evt -> {
            INoteUsecase note = (INoteUsecase) evt.getOldValue();
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

        // USECASE
        diagram.addPropertyChangeListener(USECASE_ADDED_PROPERTY_NAME, evt -> {
            IUsecase usecase = (IUsecase) evt.getNewValue();
            createUseCaseGraphic(usecase);
        });

        diagram.addPropertyChangeListener(USECASE_REMOVED_PROPERTY_NAME, evt -> {
            IUsecase usecase = (IUsecase) evt.getOldValue();
            UsecaseGraphic usecaseG = usecases.get(usecase);

            usecases.remove(usecase);
            editor.getCanvas().getChildren().remove(usecaseG);
        });

        // RELATION
        diagram.addPropertyChangeListener(RELATION_ADDED_PROPERTY_NAME, evt -> {
            IRelation relation = (IRelation) evt.getNewValue();
            createRelationGraphic(relation);
        });

        diagram.addPropertyChangeListener(RELATION_REMOVED_PROPERTY_NAME, evt -> {
            IRelation vr = (IRelation) evt.getOldValue();
            IRelationGraphic relation = relations.get(vr);
            IEntityGraphic src = relation.getEntitySrc();
            IEntityGraphic dst = relation.getEntityDst();
            src.removeRelation(relation);
            if (dst != src) {
                dst.removeRelation(relation);
            }
            editor.getCanvas().getChildren().removeAll(relation.getShapes());
            relation.clear();
            //viewRelations.remove(vr);
        });

        //ACTOR
        diagram.addPropertyChangeListener(ACTOR_REMOVED_PROPERTY_NAME, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                editor.getCanvas().getChildren().remove(actors.get(evt.getOldValue()));
            }
        });
        diagram.addPropertyChangeListener(ACTOR_ADDED_PROPERTY_NAME, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Actor actor = (Actor) evt.getNewValue();
                createActorGraphic(actor);
            }
        });

    }

    private void createPropertyListeners() {
        createRelation.addListener(new ChangeListener<IEntityRelation>() {
            @Override
            public void changed(ObservableValue<? extends IEntityRelation> observable,
                                IEntityRelation oldValue, IEntityRelation newValue) {
                addRelationEvt = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getSource().getClass() == NoteGraphic.class) {
                            IEntityRelationGraphic entityG = (IEntityRelationGraphic) event.getSource();
                            IEntityRelation entity = entityG.getModel();
                            newValue.addRelation(entity);
                        } else if (event.getSource().getClass() == UsecaseGraphic.class) {
                            IEntityRelationGraphic entityG = (IEntityRelationGraphic) event.getSource();
                            IEntityRelation entity = entityG.getModel();
                            newValue.addRelation(entity);
                        } else if (event.getSource().getClass() == ActorGraphic.class) {
                            IEntityRelationGraphic entityG = (IEntityRelationGraphic) event.getSource();
                            IEntityRelation entity = entityG.getModel();
                            newValue.addRelation(entity);
                        } else {
                            ErrorDialog.showError("Erreur",
                                    "Relation impossible.");
                        }
                        removeMouseEvent(addRelationEvt);
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
        createUsecaseMI = new MenuItem("Créer un cas");
        createActorMI = new MenuItem("Créer un Acteur");
        Menu createMenu = new Menu("Créer une entité");
        {
            createMenu.getItems().add(createUsecaseMI);
            createMenu.getItems().add(createActorMI);

        }

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
        notes.values().stream().forEach((noteG) -> {
            noteG.addEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
        usecases.values().stream().forEach((usescaseG) -> {
            usescaseG.addEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
        actors.values().stream().forEach((actorG) -> {
            actorG.addEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
        editor.addEventHandler(MouseEvent.MOUSE_CLICKED, event);
    }

    private void createActorGraphic(Actor a) {
        ActorGraphic ag = new ActorGraphic(this, a);
        actors.put(a, ag);
        ag.addEventHandler(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        ag.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());
        editor.getCanvas().getChildren().add(ag);
    }

    private void createNoteGraphic(INoteUsecase note) {
        NoteGraphic noteG = new NoteGraphic(this, note);
        notes.put(note, noteG);
        noteG.addEventHandler(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        noteG.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());
        editor.getCanvas().getChildren().add(noteG);
    }

    /**
     * Create a graphic relation that will correspond to the IRelation given in parameter.
     *
     * @param vr the model of the graphic relation that will be created
     */
    private void createRelationGraphic(IRelation vr) {
        IEntityRelationGraphic src = getEntitiesRelation().get(vr.getEntitySource());
        IEntityRelationGraphic dst = getEntitiesRelation().get(vr.getEntityTarget());
        IRelationGraphic rg = new RelationGraphic(this, vr, src, dst);
        rg.drawInGroup(editor.getCanvas());
        relations.put(vr, rg);
    }

    private void createUseCaseGraphic(IUsecase usecase) {
        UsecaseGraphic usecaseG = new UsecaseGraphic(this, usecase);
        usecases.put(usecase, usecaseG);
        usecaseG.addEventHandler(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        usecaseG.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());
        editor.getCanvas().getChildren().add(usecaseG);
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
        // USECASE
        diagram.addStyle(ALL_STYLE_ID, USECASE_STYLE_ID, POSITION_STYLE_ID, POSITION_DEFAULT);
        diagram.addStyle(ALL_STYLE_ID, USECASE_STYLE_ID, BACKGROUND_COLOR_STYLE_ID, BACKGROUND_COLOR_DEFAULT_ENTITY);
        // ACTOR
        diagram.addStyle(ALL_STYLE_ID, ACTOR_STYLE_ID, POSITION_STYLE_ID, POSITION_DEFAULT);
        diagram.addStyle(ALL_STYLE_ID, ACTOR_STYLE_ID, BACKGROUND_COLOR_STYLE_ID, BACKGROUND_COLOR_DEFAULT_ACTOR);
        // RELATIONS
        diagram.addStyle(ALL_STYLE_ID, RELATION_STYLE_ID, COLOR_STYLE_ID, COLOR_DEFAULT_RELATION);

        /*
        diagram.getEntities().stream().forEach((ve) -> {
            createEntityGraphic(ve);
        });  */
        diagram.getActors().stream().forEach((ve) -> {
            createActorGraphic(ve);
        });
        diagram.getNotes().stream().forEach((note) -> {
            createNoteGraphic(note);
        });
        diagram.getUsecases().stream().forEach((useCase) -> {
            createUseCaseGraphic(useCase);
        });

        diagram.getRelations().stream().forEach((relation)-> {
            createRelationGraphic(relation);
        });
    }

    private Map<IEntityRelation, IEntityRelationGraphic> getEntitiesRelation() {
        Map<IEntityRelation, IEntityRelationGraphic> relations = new HashMap<>();
        relations.putAll(notes);
        relations.putAll(usecases);
        relations.putAll(actors);
        return relations;
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
        usecases.values().stream().forEach((dst) -> {
            dst.removeEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
        actors.values().stream().forEach((dst) -> {
            dst.removeEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
        editor.removeEventHandler(MouseEvent.MOUSE_CLICKED, event);
    }

	@Override
	public void removeNote(INote modelNote) {
		// Auto-generated method stub /*inutile*/
	}
}
