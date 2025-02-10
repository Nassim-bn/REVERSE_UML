package fr.univrouen.umlreverse.ui.view.sequence;

import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.ALL_STYLE_ID;
import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.NOTE_ADDED_PROPERTY_NAME;
import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.NOTE_REMOVED_PROPERTY_NAME;
import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.NOTE_STYLE_ID;
import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.RELATION_STYLE_ID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.INote;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.sequence.IActivity;
import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.model.diagram.sequence.IRelationToObject;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegment;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegmentCommon;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.RelationToObject;
import fr.univrouen.umlreverse.model.diagram.sequence.SequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.INoteUsecase;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.ui.component.common.dialog.DialogNoteEdit;
import fr.univrouen.umlreverse.ui.component.common.elements.EnlargePoint;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityRelationGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.NoteGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.RelationGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.ui.component.sequence.dialog.DialogActivityEdit;
import fr.univrouen.umlreverse.ui.component.sequence.dialog.DialogObjectEdit;
import fr.univrouen.umlreverse.ui.component.sequence.dialog.DialogSegmentEdit;
import fr.univrouen.umlreverse.ui.component.sequence.elements.ActivityGraphic;
import fr.univrouen.umlreverse.ui.component.sequence.elements.IObjectGraphic;
import fr.univrouen.umlreverse.ui.component.sequence.elements.ObjectGraphic;
import fr.univrouen.umlreverse.ui.component.sequence.elements.SegmentGraphic;
import fr.univrouen.umlreverse.ui.component.sequence.relations.RelationToObjectGraphic;
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
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;

// TODO remplacer tout les getEntitySrc par getDiagram.get(model.getSource())
public class SequenceController extends ADiagramEditorController implements ISequenceController {
	public static final double INIT_MARG_RELATION_TO_OBJECT = 20;

	// ATTRIBUTES
    private final ISequenceDiagram diagram;
    private final Map<INote, NoteGraphic> notes;
    private final Map<IRelation, IRelationGraphic> relations;
    private final Map<IRelationToObject, RelationToObjectGraphic> relationsToObject;
    private final Map<IObject, ObjectGraphic> objects;
    private final Map<ISegmentCommon, SegmentGraphic> segments;
    private final Map<IActivity, ActivityGraphic> activities;


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
    private MenuItem createObjectMI;
    private MenuItem createSegmentMI;
    private MenuItem createActivityMI;

    // CONSTRUTORS
    public SequenceController(ADiagramEditor editor, ISequenceDiagram sequenceDiagram) {
        super(editor);
        diagram = sequenceDiagram;
        notes = new HashMap<>();
        objects = new HashMap<>();
        relations = new HashMap<>();
        segments = new HashMap<>();
        activities = new HashMap<>();
        relationsToObject = new HashMap<>();

        createRelation = new SimpleObjectProperty<>();
        createComposant();
        createController();
        drawDiagram();
    }

	@Override
	public void createNote(double x, double y) {
		DialogNoteEdit dialog = new DialogNoteEdit(diagram);
        dialog.showAndWait();
        INote note = dialog.getNote();
        if (note != null) {
            note.addStyle(POSITION_STYLE_ID, x + "|" + y + "|0|0");
        }
	}


	@Override
	public ISequenceDiagram getDiagram() {
		return diagram;
	}


	@Override
	public Map<IObject, ObjectGraphic> getObjects() {
		return objects;
	}


	@Override
	public Map<ISegmentCommon, SegmentGraphic> getSegments() {
		return segments;
	}

	@Override
	public Map<IActivity, ActivityGraphic> getActivities() {
		return activities;
	}

	@Override
	public Map<INote, NoteGraphic> getNotes() {
		return notes;
	}

	@Override
	public Map<IRelationToObject, RelationToObjectGraphic> getRelationsToObject() {
		return relationsToObject;
	}

	@Override
	public void createRelation(IEntityRelation entity) {
		createRelation.setValue(entity);
        addMouseEvent(addRelationEvt);
	}


	@Override
	/**Handler of createObject action : show dialog box to edit an object*/
	public void createObject(double x, double y) {
		DialogObjectEdit doe = new DialogObjectEdit(this);
        doe.showAndWait();
        IObject obj = (IObject) doe.getObject();
        if (obj != null) {
        	int ox = 50 + diagram.getObjects().size()*150;

        	obj.addStyle(IDiagramEditorController.POSITION_STYLE_ID, x + "|" + y + "|"+ ox +"|50");
        	try {
    			getDiagram().addObject(obj);
    		} catch (RefusedAction refusedAction) {
    			Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Entité déjà présente sur le diagramme");
                alert.setHeaderText(null);
                alert.setContentText(ErrorAbstraction.getErrorFromCode(refusedAction.getMessage()).getExplain());
                alert.showAndWait();
                createObject(0,0);
    		}
        }
	}

	@Override
	public void createSegment(double x, double y) {
		DialogSegmentEdit dse = new DialogSegmentEdit(this);
		dse.showAndWait();

		ISegmentCommon segment = dse.getSegment();
		getDiagram().addSegment(segment);
	}

	@Override
	public void createActivite(double x, double y) {
		DialogActivityEdit dse = new DialogActivityEdit(this);
		dse.showAndWait();

		IActivity activity = dse.getActivity();
		if (activity != null) {
			double ymax = 0;
			for (ObjectGraphic objG : objects.values()) {
				if (objG.getRectangle().getHeight() > ymax) {
					ymax = objG.getRectangle().getHeight();
				}
			}
			if (activity.getParent() != null) {

			} else {
				ymax = getNextYRelationToObjectPosition();
			}
			// TODO créer une constante width
			double xmax = objects.get(activity.getObj()).lifeLineStartPosition().getX() - 5; //activities.get(activity.getObj()).getRectActWidth();
			activity.addStyle(IDiagramEditorController.POSITION_STYLE_ID, x + "|" + y + "|" + xmax + "|" + ymax);

		}
		getDiagram().addActivity(activity);

	}


	@Override
	public void removeNote(INote note) {
		 Contract.check(note != null, "note must not be null.");
	        diagram.removeNote(note);
	        notes.remove(note);
	}


	@Override
	public void removeRelation(IRelation relation) {
		Contract.check(relation != null,
                "L'argument relation ne doit pas être nul.");
        diagram.removeRelation(relation);
        relations.remove(relation);
	}


	@Override
	public void removeObject(IObject object) {
		Contract.check(object != null, "The object must not be null.");
	    diagram.removeObject((IObject)object);
	    objects.remove(object);

	}

	@Override
	public void removeSegment(ISegmentCommon segment) {
		Contract.check(segment != null, "The segment must not be null.");
	    diagram.removeSegment((ISegment)segment);
	    objects.remove(segment);
	}

	@Override
	public double getNextYRelationToObjectPosition() {
		if (objects.size() == 0) {
			return 0;
		}
		if (relationsToObject.size() == 0 && segments.size() == 0 && activities.size() == 0) {
			// return Y position of the lifeLine of a random object
			ObjectGraphic og = objects.values().toArray(new ObjectGraphic[objects.size()])[0];
			return og.getLifeLine().getStartY() + og.getRectangle().getHeight() + INIT_MARG_RELATION_TO_OBJECT;
		}
		double maxYPos = 0;
		// search for the relation with the highest Y position
		// TODO only to the last relation in the model if relations are ordered
		for (Entry<IRelationToObject, RelationToObjectGraphic> relation: relationsToObject.entrySet()) {
			double yPos = relation.getValue().getTranslateY();
			if (yPos > maxYPos) {
				maxYPos = yPos;
			}
		}
		for (Entry<ISegmentCommon, SegmentGraphic> segment: segments.entrySet()) {
			double yPos = segment.getValue().getRectangle().getHeight() + segment.getValue().getTranslateY();
			if (yPos > maxYPos) {
				maxYPos = yPos;
			}
		}

		for (Entry<IActivity, ActivityGraphic> activity: activities.entrySet()) {
			double yPos = activity.getValue().getRectangle().getHeight() + activity.getValue().getTranslateY();
			if (yPos > maxYPos) {
				maxYPos = yPos;
			}
		}
		return maxYPos + INIT_MARG_RELATION_TO_OBJECT;
	}

	//OUTILS
	/**
     * To instancie ContextMenu and MenuItems: shown on right clic on the editor
     */
    private void createComposant() {
        ctxMenuDiagram = new ContextMenu();
        createObjectMI = new MenuItem("Créer un objet");
        createSegmentMI = new MenuItem("Créer un segment");
        createActivityMI = new MenuItem("Créer une activité");

        // TODO add here item button to create other componants like segment and activities

        ctxMenuDiagram.getItems().add(getCreateNoteMI());
        ctxMenuDiagram.getItems().add(createObjectMI);
        ctxMenuDiagram.getItems().add(getZoomInMI());
        ctxMenuDiagram.getItems().add(getZoomOutMI());
    }

    /**
     * Add mouse listener to Entity.
     *
     * @param event the Event Handler that will be add
     */
    private void addMouseEvent(EventHandler<MouseEvent> event) {
        editor.setCursor(Cursor.OPEN_HAND);
        notes.values().stream().forEach((noteG) -> {
            noteG.addEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
        objects.values().stream().forEach((objectG) -> {
        	objectG.addEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
        segments.values().stream().forEach((segmentG) -> {
        	segmentG.addEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
        activities.values().stream().forEach((activityG) -> {
        	activityG.addEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });

        relationsToObject.values().stream().forEach((relationG) -> {
    		relationG.addEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });

        editor.addEventHandler(MouseEvent.MOUSE_CLICKED, event);
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
        createObjectMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createObject(miceX, miceY);
                deselectEntity();
            }

        });
    }

    /**
     * Listener on model:
     * listen on create and remove property to update the view
     */
    private void createModelListeners() {


    	// NOTE
        diagram.addPropertyChangeListener(NOTE_ADDED_PROPERTY_NAME, evt -> {
        	INote note = (INote) evt.getNewValue();
            createNoteGraphic(note);
        });

        diagram.addPropertyChangeListener(NOTE_REMOVED_PROPERTY_NAME, evt -> {
            INote note = (INote) evt.getOldValue();
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

        // OBJECT LISTENERS
        diagram.addPropertyChangeListener(ISequenceDiagram.OBJECT_ADDED_PROPERTY_NAME, evt -> {
            IObject Object = (IObject) evt.getNewValue();
            createObjectGraphic(Object);
        });

        diagram.addPropertyChangeListener(ISequenceDiagram.OBJECT_REMOVED_PROPERTY_NAME, evt -> {
            IObject object = (IObject) evt.getOldValue();
            IObjectGraphic ObjectG = objects.get(object);
            objects.remove(object);
            editor.getCanvas().getChildren().remove(ObjectG);
        });

        // RELATION
        diagram.addPropertyChangeListener(IDiagram.RELATION_ADDED_PROPERTY_NAME, evt -> {
            IRelation relation = (IRelation) evt.getNewValue();
            if (relation.getEntitySource() instanceof IObject
            		&& relation.getEntityTarget() instanceof IObject) {
            	createRelationToObjectGraphic((IRelationToObject) relation);
            	objects.values().forEach(obj -> obj.getController().refresh());
            } else {
            	createRelationGraphic(relation);
            }
        });

        diagram.getSegments().forEach(seg ->  {
        	seg.addPropertyChangeListener(ISegment.RELATION_ADDED_TO_SEGMENT_PROPERTY_NAME, evt -> {
        		IRelation relation = (IRelation) evt.getNewValue();
        		segments.get(((IRelationToObject) relation)
        				.getSegmentContainer())
        				.getController()
        				.refresh();

        	});
        });

        diagram.addPropertyChangeListener(IDiagram.RELATION_REMOVED_PROPERTY_NAME, evt -> {
            IRelation vr = (IRelation) evt.getOldValue();

            if (vr.getEntitySource() instanceof IObject
            		&& vr.getEntityTarget() instanceof IObject) {
            	RelationToObjectGraphic relationG = relationsToObject.get(vr);
	            IObjectGraphic src = relationG.getEntitySrc();
	            IObjectGraphic dst = relationG.getEntityDst();

	            src.removeRelation(relationG);
	            if (dst != src) {
	                dst.removeRelation(relationG);
	            }

	            relationsToObject.remove(vr);
	            editor.getCanvas().getChildren().remove(relationG);

            } else {
	            IRelationGraphic relation = relations.get(vr);
	            IEntityGraphic src = relation.getEntitySrc();
	            IEntityGraphic dst = relation.getEntityDst();

                src.removeRelation(relation);
	            if (dst != src) {
	                dst.removeRelation(relation);
	            }
	            editor.getCanvas().getChildren().removeAll(relation.getShapes());
	            relation.clear();
            }
        });

        // SEGMENT
        diagram.addPropertyChangeListener(IDiagram.SEGMENT_ADDED_PROPERTY_NAME, evt -> {
            ISegmentCommon segment = (ISegmentCommon) evt.getNewValue();
            createSegmentGraphic(segment);
            ISegment parent = segment.getParent();
            while (parent != null) {
            	segments.get(parent).getController().refresh();
            	parent = parent.getParent();
            }
            if (segment.getParent() != null) {
            	segments.keySet().stream().filter(seg ->
            	segments.get(seg).getTranslateY() >
            	segments.get(segment.getParent()).getTranslateY()
            	&& seg.getParent() == null).forEach(seg -> {
            		segments.get(seg).setTranslateY(segments.get(seg).getTranslateY() + segments.get(seg).getHeight());
            	});
            }
            objects.values().forEach(obj -> obj.getController().refresh());
            relationsToObject.values().forEach(relation -> relation.toFront());

        });

        diagram.addPropertyChangeListener(IDiagram.SEGMENT_REMOVED_PROPERTY_NAME, evt -> {
        	ISegment segment = (ISegment) evt.getOldValue();
            SegmentGraphic segmentG = segments.get(segment);
            segment.getSegments().forEach(segmentChildren -> {
            	getDiagram().removeSegment(segmentChildren);
            });
            segment.getRelations().forEach(relationsContained -> {
            	getDiagram().removeRelation(relationsContained);
            });
            segments.remove(segment);
            editor.getCanvas().getChildren().remove(segmentG);
        });

        // ACTIVITY
        diagram.addPropertyChangeListener(IDiagram.ACTIVITY_ADDED_PROPERTY_NAME, evt -> {
            IActivity activity = (IActivity) evt.getNewValue();
            createActivityGraphic(activity);
            IActivity parent = activity.getParent();
            while (parent != null) {
            	activities.get(parent).getController().refresh();
            	parent = parent.getParent();
            }

            objects.values().forEach(obj -> obj.getController().refresh());
        });

        diagram.addPropertyChangeListener(IDiagram.ACTIVITY_REMOVED_PROPERTY_NAME, evt -> {
        	IActivity activity = (IActivity) evt.getOldValue();
            ActivityGraphic activityG = activities.get(activity);
            activity.getActivity().forEach(activityChildren -> {
            	getDiagram().removeActivity(activityChildren);
            });
            activity.getRelation().forEach(relationsContained -> {
            	getDiagram().removeRelation(relationsContained);
            });
            activities.remove(activity);
            editor.getCanvas().getChildren().remove(activityG);
        });
    }

    /**
     * Listener on change value of graphics components : note, object
     * */
    private void createPropertyListeners() {
        createRelation.addListener(new ChangeListener<IEntityRelation>() {
            @Override
            public void changed(ObservableValue<? extends IEntityRelation> observable,
                                IEntityRelation oldValue, IEntityRelation newValue) {
                addRelationEvt = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getSource() instanceof IEntityRelationGraphic
                        		|| event.getSource() instanceof RelationToObjectGraphic) {
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
     * Remove mouse listener of all Entities.
     *
     * @param event the event handler that will be remove
     */
    private void removeMouseEvent(EventHandler<MouseEvent> event) {
        editor.setCursor(Cursor.DEFAULT);
        notes.values().stream().forEach((dst) -> {
            dst.removeEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
        objects.values().stream().forEach((dst) -> {
            dst.removeEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });

        segments.values().stream().forEach((dst) -> {
            dst.removeEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
        activities.values().stream().forEach((dst) -> {
            dst.removeEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });

        relationsToObject.values().stream().forEach((relationG) -> {
    		relationG.removeEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });

        editor.removeEventHandler(MouseEvent.MOUSE_CLICKED, event);
    }



    private void createObjectGraphic(IObject o) {
        ObjectGraphic objectG = new ObjectGraphic(this, o);
        objects.put(o, objectG);
        objectG.addEventHandler(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        objectG.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());
        editor.getCanvas().getChildren().add(objectG);
    }

    private void createNoteGraphic(INote note) {
        NoteGraphic noteG = new NoteGraphic(this, note);
        notes.put(note, noteG);
        noteG.addEventHandler(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        noteG.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());
        editor.getCanvas().getChildren().add(noteG);
    }

    private void createSegmentGraphic(ISegmentCommon segments2) {
        SegmentGraphic segmentG = new SegmentGraphic(this, segments2);
        segments.put(segments2, segmentG);
        segmentG.addEventHandler(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        segmentG.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());
        editor.getCanvas().getChildren().add(segmentG);
    }

    private void createActivityGraphic(IActivity activity) {
    	System.out.println("add acitivty");
    	diagram.getActivities().forEach(act -> {
    		System.out.println("act :" + act);
    	});
    	ActivityGraphic activityG = new ActivityGraphic(this, activity);
        activities.put(activity, activityG);
        activityG.addEventHandler(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        activityG.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());
        editor.getCanvas().getChildren().add(activityG);

        activityG.toFront();
    }

    private Map<IEntityRelation, IEntityRelationGraphic> getEntitiesRelation() {
        Map<IEntityRelation, IEntityRelationGraphic> relations = new HashMap<>();
        relations.putAll(notes);
        relations.putAll(objects);
        relations.putAll(relationsToObject);
        return relations;
    }


    /**
     * Create a graphic relation that will correspond to the IRelation given in parameter.
     *
     * @param vr the model of the graphic relation that will be created
     */
    private void createRelationGraphic(IRelation vr) {
        IEntityRelationGraphic src = getEntitiesRelation().get(vr.getEntitySource());
        IEntityRelationGraphic dst = getEntitiesRelation().get(vr.getEntityTarget());
        IRelationGraphic rg  = new RelationGraphic(this, vr, src, dst);
    	rg.drawInGroup(editor.getCanvas());
        relations.put(vr, rg);
    }

    private void createRelationToObjectGraphic(IRelationToObject vr) {
		ObjectGraphic src = objects.get(vr.getEntitySource());
    	ObjectGraphic dst = objects.get(vr.getEntityTarget());

    	vr.addStyle(IDiagramEditorController.POSITION_STYLE_ID, 0 + "|" + 0 + "|"+ 0 +"|50");

    	RelationToObjectGraphic relationG = new RelationToObjectGraphic(this, vr, src, dst);
    	relationsToObject.put(vr, relationG);
        relationG.addEventHandler(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        relationG.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());
        editor.getCanvas().getChildren().add(relationG);
    }

    @Override
	public EnlargePoint bindToEnlargeActivityPoint(IEntityGraphic bindedObject) {
		EnlargePoint enlargePoint = new EnlargePoint(this, bindedObject);
		enlargePoint.addEventHandler(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
		enlargePoint.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());
        editor.getCanvas().getChildren().add(enlargePoint);

		return enlargePoint;
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
        // RELATIONS
        diagram.addStyle(ALL_STYLE_ID, RELATION_STYLE_ID, COLOR_STYLE_ID, COLOR_DEFAULT_RELATION);


        diagram.getObjects().stream().forEach((objects) -> {
            createObjectGraphic(objects);
        });

        diagram.getRelations().stream().forEach((relation) -> {
        	if (relation instanceof IRelationToObject) {
        		createRelationToObjectGraphic((IRelationToObject) relation);
        	} else {
            	createRelationGraphic(relation);
        	}
        });

        diagram.getSegments().stream().forEach((segments) -> {
            createSegmentGraphic(segments);
        });



        diagram.getActivities().stream().forEach((activities) -> {
            createActivityGraphic(activities);
        });


        segments.values().stream().forEach(segmentG -> segmentG.toBack());
    }

	@Override
	public void removeActivity(IActivity activity) {
		// TODO Auto-generated method stub
		Contract.check(activity != null, "The activity must not be null.");
	    diagram.removeActivity((IActivity) activity);
	    objects.remove(activity);
	}
}
