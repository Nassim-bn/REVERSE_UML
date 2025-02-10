package fr.univrouen.umlreverse.ui.view.packag;

import static fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram.PACKAGE_STYLE_ID;
import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.ALL_STYLE_ID;
import java.util.Map;
import fr.univrouen.umlreverse.model.diagram.clazz.view.INoteClass;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewPackage;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewRelation;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.packag.IPackageDiagram;
import fr.univrouen.umlreverse.model.project.IRelation;
import fr.univrouen.umlreverse.model.project.Relation;
import fr.univrouen.umlreverse.ui.component.clazz.elements.NoteGraphic;
import fr.univrouen.umlreverse.ui.component.clazz.elements.ObjectEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.EnlargePoint;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.INoteGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.ui.component.packag.dialog.DialogOPackage;
import fr.univrouen.umlreverse.ui.component.packag.elements.IPackageGraphic;
import fr.univrouen.umlreverse.ui.component.packag.elements.PackageGraphic;
import fr.univrouen.umlreverse.ui.view.clazz.ClassController;
import fr.univrouen.umlreverse.ui.view.common.ADiagramEditor;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import fr.univrouen.umlreverse.util.ErrorDialog;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

public class PackageController extends ClassController implements IPackageController {

	// ATTRIBUTES
	/**
     * The menu item that will allow the creation of a class/interface/enum/abstract.
     */
    private MenuItem createPackageMI;

    /**
     * The package's relation handler.
     */
    private EventHandler<MouseEvent> addPackageRelationEvt;

    // CONSTRUCTORS
    /**
     * Constructor of the class controller.
     * @param editor the ADiagramEditor which will be correlated whith the controller
     * @param classDiagram the classDiagram which will be correlated with the controller
     */
    public PackageController(ADiagramEditor editor, IPackageDiagram packageDiagram) {
        super(editor, packageDiagram);
    }

    // REQUESTS
    @Override
    public IPackageDiagram getDiagram() {
        return (IPackageDiagram) diagram;
    }

    @Override
    public Map<IViewEntity, PackageGraphic> getPackages() {
    	return packages;
    }

    @Override
    public PackageGraphic getPackage(IViewPackage packag) {
        Contract.check(packag != null, "L'argument packag ne doit pas être nul.");
        return packages.get(packag);
    }

    // COMMANDS
    @Override
	public void createPackage(double x, double y) {
        DialogOPackage dialogOPackage = new DialogOPackage(PackageController.this);
        dialogOPackage.showAndWait();
        IViewPackage entity = dialogOPackage.getViewPackage();
        if (entity != null) {
        	int posX = 10;
        	int posY = 30;

        	if (getDiagram().getPackages().size() < 3) {
        		posY += getDiagram().getPackages().size() * 160;
        	} else {
        		posX = 150;
        		posY = 150;
        	}
	        entity.addStyle(IDiagramEditorController.POSITION_STYLE_ID, x + "|" + y + "|" + posX + "|" + posY);
	        getDiagram().addPackage(entity);
        }
    }

    @Override
    public void createPackageRelation(IViewEntity entity) {
        createPackageRelation.setValue(entity);
        addMouseEvent(addPackageRelationEvt);
    }

    @Override
    public void removePackage(IViewPackage packag) {
    	Contract.check(packag != null, "The packag must not be null.");
    	diagram.removePackage(packag);
	    packages.remove(packag);
    }

    // CONTROLLER
    /**
     * Create controller for the menu item of the context menu.
     */
    protected void createButtonController() {
    	super.createButtonController();

        createPackageMI.setOnAction(actionEvent -> {
            createPackage(miceX, miceY);
            deselectEntity();
        });
    }

    /**
     * Create listener that will listen to the model.
     */
    protected void createModelListeners() {
    	super.createModelListeners();

        diagram.addPropertyChangeListener(IPackageDiagram.PACKAGE_ADDED_PROPERTY_NAME, evt -> {
            IViewPackage entity = (IViewPackage) evt.getNewValue();
            createPackageGraphic(entity);
        });

        diagram.addPropertyChangeListener(IPackageDiagram.PACKAGE_REMOVED_PROPERTY_NAME, evt -> {
        	IViewPackage packag = (IViewPackage) evt.getOldValue();
            IPackageGraphic packageG = packages.get(packag);
            packages.remove(packag);
            editor.getCanvas().getChildren().remove(packageG);
        });
    }

    /**
     * Create Property Change listener that will observe the creation of new Entity.
     */
    protected void createPropertyListeners() {
    	super.createPropertyListeners();
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
                            } else if (event.getSource().getClass() == PackageGraphic.class) {
                            	 PackageGraphic pg = (PackageGraphic) event.getSource();
                                 newValue.addEntity(pg.getViewEntity());
                            } else {
                    			ErrorDialog.showError("Erreur",
                    					"Vous ne pouvez relier une note qu'à "
                    							+ "une entité (Interface/Classe/"
                    							+ "Enumeration/Paquetage)");
                            }
                            removeMouseEvent(addNoteRelationEvt);
                            event.consume();
                        }
                };
            }
        });

        createPackageRelation.addListener(new ChangeListener<IViewEntity>() {
        	@Override
    		public void changed(ObservableValue<? extends IViewEntity> observable,
            		IViewEntity oldValue, IViewEntity newValue) {
    			addPackageRelationEvt = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
					    if (event.getSource().getClass() == NoteGraphic.class) {
					        INoteGraphic noteG = (INoteGraphic) event.getSource();
					        getNote(noteG).addEntity((IViewEntity) newValue);
					    } else if (event.getSource().getClass() == PackageGraphic.class) {
					        PackageGraphic pg = (PackageGraphic) event.getSource();

					        IRelation r = new Relation(
					                Math.random() + "", ((IViewEntity) newValue).getData(),
					            pg.getViewEntity().getData(),
					            TypeHeadArrow.None,
					            TypeHeadArrow.None,
					            TypeLineArrow.Dashed);
						    diagram.getProject().addRelation(r);
                            ViewRelation viewRelation = new ViewRelation(r, diagram);

                            viewRelation.setArrowHead(RelationTypeEnum.DEPENDENCY.getHead());
                		    viewRelation.setLineArrow(RelationTypeEnum.DEPENDENCY.getLine());
						} else if (event.getSource().getClass() == ObjectEntityGraphic.class) {
							ObjectEntityGraphic pg = (ObjectEntityGraphic) event.getSource();

					        IRelation r = new Relation(
					                Math.random() + "", ((IViewEntity) newValue).getData(),
					            pg.getViewEntity().getData(),
					            TypeHeadArrow.None,
					            TypeHeadArrow.None,
					            TypeLineArrow.Dashed);
						    diagram.getProject().addRelation(r);
                            ViewRelation viewRelation = new ViewRelation(r, diagram);

                		    viewRelation.setArrowHead(RelationTypeEnum.DEPENDENCY.getHead());
                		    viewRelation.setLineArrow(RelationTypeEnum.DEPENDENCY.getLine());
						} else {
						    ErrorDialog.showError("Erreur",
								"Vous ne pouvez relier une un paquetage qu'à "
								+ "une note ou un autre paquetage");
						}
					    removeMouseEvent(addPackageRelationEvt);
					    event.consume();
					}
                };
            }
		});
    }

    /**
     * To instancie new ContextMenu and MenuItems
     */
    protected void createComposant() {
    	super.createComposant();

        createPackageMI = new MenuItem("Créer un paquetage");
        ctxMenuDiagram.getItems().add(createPackageMI);
    }


    // PRIVATES
    /**
     * Add mouse listener to Entity.
     * @param event the Event Handler that will be add
     */
    protected void addMouseEvent(EventHandler<MouseEvent> event) {
    	super.addMouseEvent(event);

        packages.values().stream().forEach((packageG) -> {
        	packageG.addEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
    }

    /**
     * Create a graphic relation that will correspond to the IViewRelation given in parameter.
     * @param vr the model of the graphic relation that will be created
     */
	private void createPackageGraphic(IViewPackage packag) {
		PackageGraphic packageG = new PackageGraphic(this, packag);

        packages.put(packag, packageG);
        objectEntities.put(packag.getData(), packag);
        packageG.addEventHandler(MouseEvent.MOUSE_PRESSED,
                nodeGestures.getOnMousePressedEventHandler());
        packageG.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                nodeGestures.getOnMouseDraggedEventHandler());
        editor.getCanvas().getChildren().add(packageG);

        packageG.toBack();
	}

	@Override
	public EnlargePoint bindToEnlargePackagePoint(IEntityGraphic bindedObject) {
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
    protected void drawDiagram() {
    	// PAQUETAGES
        diagram.addStyle(ALL_STYLE_ID, PACKAGE_STYLE_ID, COLOR_STYLE_ID, BACKGROUND_COLOR_DEFAULT_ENTITY);

        diagram.getPackages().stream().forEach((packag) -> {
        	createPackageGraphic(packag);
        });

    	super.drawDiagram();
    }

    /**
     * Remove mouse listener of all Entities.
     * @param event the event handler that will be remove
     */
    protected void removeMouseEvent(EventHandler<MouseEvent> event) {
    	super.removeMouseEvent(event);

        packages.values().stream().forEach((packageG) -> {
        	packageG.removeEventFilter(MouseEvent.MOUSE_CLICKED, event);
        });
    }
}
