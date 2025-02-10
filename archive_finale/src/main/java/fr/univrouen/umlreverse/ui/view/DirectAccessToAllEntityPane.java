package fr.univrouen.umlreverse.ui.view;

import fr.univrouen.umlreverse.UmlReverseBeans;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewEntity;
import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.diagram.packag.IPackageDiagram;
import fr.univrouen.umlreverse.model.diagram.packag.PackageDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.model.diagram.sequence.Object;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.SequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.visitor.SortedDiagramVisitor;
import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.project.ObjectEntity;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import static fr.univrouen.umlreverse.util.Util.toRGBCode;

import java.util.*;


public class DirectAccessToAllEntityPane extends StackPane {

    /**
     * Menu for right click.
     */
    private ContextMenu ctxMenuDiagram;
    /**
     * Map with name of entity as key and the entity as value.
     */
    private Map<String, IObjectEntity> entityMap;
    /**
     * variable that contain the project which is open in the application.
     */
    private IProject project;

    /**
     * Variable that give access to the different part of the view.
     */
    private UmlReverseBeans beans;

    /**
     * Constructor that create an empty scrollPane.
     *
     * @param beans the beans of the application
     */

    public DirectAccessToAllEntityPane(UmlReverseBeans beans) {
        this.beans = beans;
        project = beans.getProject();
        addListenerToBeans();
        entityMap = new HashMap<>();
    }

    //REQUEST


    /**
     * getter of project.
     *
     * @return IProject
     */
    public IProject getProject() {
        return project;
    }

    /**
     * setter of Project.
     *
     * @param p current project
     */
    private void setProject(IProject p) {
        Contract.check(p != null, "attempt to set a null project");
        project = p;
        addPropertyChangeListenerToProject();
        setId("directAccessPane");
    }

    //COMMANDS

    /**
     * getter of entityMap that contain all entity of the project.
     *
     * @return entityMap
     */
    public Map<String, IObjectEntity> getEntityMap() {
        return entityMap;
    }

    /**
     * Draw the leftBottomPane.
     */
    private void draw() {
        //destroy last view
        this.getChildren().clear();

        //Create a List with the value of the Map and then sort it.
        List<IObjectEntity> entityList = new ArrayList<>();

        entityMap.values()
        		.stream()
        		.filter(entity -> entity.getType() != TypeEntity.Packag)
        		.forEach(entity -> entityList.add(entity));

        Collections.sort(entityList, (p1, p2)
                -> p1.getAbsoluteName().compareTo(p2.getAbsoluteName()));


        //creer une liste qui contient des gridpane qui eux possède la representation d'une entité
        List<GridPane> li = new ArrayList<>();
        for (IObjectEntity oe : entityList) {
            GridPane gp = new GridPane();
            gp.getColumnConstraints().add(new ColumnConstraints(15));
            gp.getColumnConstraints().add(new ColumnConstraints(15));
            gp.getColumnConstraints().add(new ColumnConstraints(265));
            Text tmp = new Text(oe.getName());
            tmp.setId(oe.getAbsoluteName());
            gp.setId(oe.getAbsoluteName());
            addEventToGridPane(gp);
            Pane p = new Pane();
            {
                double circleSize = 5;
                Circle c = new Circle(circleSize, Color.web(oe.getType().getColor()));
                c.setCenterX(p.getLayoutX() + circleSize);
                c.setCenterY(p.getLayoutY() + circleSize + 3);
                c.setStroke(Color.BLACK);
                p.getChildren().add(c);
            }
            gp.add(p, 0, 0);
            gp.add(new Text("" + oe.getVisibility().getSymbol()), 1, 0);
            gp.add(tmp, 2, 0);
            li.add(gp);
        }
        ObservableList<GridPane> ol = FXCollections.observableArrayList(li);
        ListView<GridPane> lv = new ListView<>(ol);
        lv.setPrefSize(getPrefWidth(), getPrefHeight());
        getChildren().add(lv);
        this.setVisible(true);
    }

    /**
     * Action of the gridPane.
     * Verification of diagram and then create the viewEntity
     */
    private void gridPaneEvent(MouseEvent event) {
        //Test via a visitor pattern that the current diagram is a classDiagramEditor

        IObjectEntity oE = entityMap.get(((GridPane) event.getSource()).getId());
        SortedDiagramVisitor sdv = new SortedDiagramVisitor();
        if (beans.getDiagram() != null) {
            beans.getDiagram().accept(sdv);
            Set<IDiagram> se = sdv.getDiagrams(IClassDiagram.class);

            if (se.size() > 0) {

                Iterator<IDiagram> it = se.iterator();
                IClassDiagram icd = (IClassDiagram) it.next();

                try {
                    ViewEntity ve = new ViewEntity(oE, icd);
                } catch (RefusedAction refusedAction) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Entité déjà présente sur le diagramme");
                    alert.setHeaderText(null);
                    alert.setContentText(ErrorAbstraction.getErrorFromCode(refusedAction.getMessage()).getExplain());
                    alert.showAndWait();
                }

            }
            if (beans.getDiagram().getClass().equals(SequenceDiagram.class)) {
            	if (oE.getType().equals(TypeEntity.Clazz)) {
					IObject obj = new Object(oE, "", (SequenceDiagram) beans.getDiagram());
					obj.addStyle(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID, IDiagramEditorController.BACKGROUND_COLOR_DEFAULT_ENTITY);
			        obj.addStyle(IDiagramEditorController.TEXT_COLOR_STYLE_ID, IDiagramEditorController.TEXT_COLOR_DEFAULT);
			        int ox = 50 + ((SequenceDiagram) beans.getDiagram()).getObjects().size()*150;
		            obj.addStyle(IDiagramEditorController.POSITION_STYLE_ID, 0 + "|" + 0 + "|"+ ox +"|50");
			        try {
						((SequenceDiagram) beans.getDiagram()).addObject(obj);
					} catch (RefusedAction refusedAction) {
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
	                    alert.setTitle("Entité déjà présente sur le diagramme");
	                    alert.setHeaderText(null);
	                    alert.setContentText(ErrorAbstraction.getErrorFromCode(refusedAction.getMessage()).getExplain());
	                    alert.showAndWait();
					}
            	} else {
            		Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Entité non autorisée dans le diagramme de séquence");
                    alert.setHeaderText(null);
                    alert.setContentText("Les entités de type interface ou classe abstraite ne sont pas autorisés dans un "
                    		+ "diagramme de séquence");
                    alert.showAndWait();
            	}
            }
        }
    }

    /**
     * Add All entity of the project to the local Map.
     */
    private void addAllEntityToMap() {
        entityMap.clear();
        Set<IObjectEntity> enti = project.getAllEntities();
        for (IObjectEntity entity : enti) {
            addPropertyChangeListenerToEntity((ObjectEntity) entity);
            entityMap.put(entity.getAbsoluteName(), entity);
        }
    }

//LISTENERS

    /**
     * Manage event on ObjectEntity like modification of type visibility or name.
     *
     * @param entity the entity that will be observed
     */
    private void addPropertyChangeListenerToEntity(ObjectEntity entity) {
        entity.addPropertyChangeListener("NameChanged", evt -> {
            entityMap.remove(evt.getOldValue());
            entityMap.put((String) evt.getNewValue(), (IObjectEntity) evt.getSource());
            draw();
        });
        entity.addPropertyChangeListener("TypeChanged", evt -> {
            draw();
        });

        entity.addPropertyChangeListener("VisibilityChanged", evt -> {
            draw();
        });
    }

    /**
     * Add Listener to current Project to observed if entity has been add or remove.
     */
    private void addPropertyChangeListenerToProject() {
        project.addPropertyChangeListener("EntityAdded", evt -> {
            if (!entityMap.containsValue(evt.getNewValue())) {
                addPropertyChangeListenerToEntity((ObjectEntity) evt.getNewValue());
                entityMap.put(((IObjectEntity) evt.getNewValue()).getAbsoluteName(), ((IObjectEntity) evt.getNewValue()));
            }
            draw();
        });
        project.addPropertyChangeListener("EntityRemoved", evt -> {
            entityMap.remove(((IObjectEntity) evt.getOldValue()).getAbsoluteName());
            draw();
        });

    }

    /**
     * Listener to manage modification of current Project.
     */
    private void addListenerToBeans() {
        beans.getProjectProperty().addListener(new ChangeListener<IProject>() {
            @Override
            public void changed(ObservableValue<? extends IProject> observable, IProject oldValue, IProject newValue) {
                if (newValue != null) {
                    setProject(newValue);
                    addAllEntityToMap();
                    draw();
                } else {
                    project = null;
                    getChildren().clear();

                }
            }
        });
        beans.diagramProperty().addListener(new ChangeListener<IDiagram>() {
            @Override
            public void changed(ObservableValue<? extends IDiagram> observable,
                                IDiagram oldValue, IDiagram newValue) {
                /* test if the current diagram is a class diagram or use case
                    and if it is use case disable the DirectAccess
                    if not set it able.
                    */
                SortedDiagramVisitor sdv = new SortedDiagramVisitor();
                if (beans.getDiagram() != null) {
                    beans.getDiagram().accept(sdv);
                    Set<IDiagram> se = new HashSet<>(sdv.getDiagrams(IClassDiagram.class));
                    se.addAll(sdv.getDiagrams(ISequenceDiagram.class));
                    se.addAll(sdv.getDiagrams(IPackageDiagram.class));
                    if (se.size() > 0) {
                        setDisable(false);
                    } else {
                        setDisable(true);
                    }
                }
            }
        });
    }

    /**
     * Add Event to the gridPane to manage click on grid.
     *
     * @param gp the gridPane
     */
    private void addEventToGridPane(GridPane gp) {
        //put a mouse controller on a grid that will manage double click and right click
        gp.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        gridPaneEvent(event);
                    }
                }
            }
            //manage the right click
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                //create element of the contextMenu then the controller and then add it
                ctxMenuDiagram = new ContextMenu();
                MenuItem addMi = new MenuItem("Ajouter l'entité au diagramme");
                addMi.setOnAction(e -> gridPaneEvent(event));
                MenuItem suppMi = new MenuItem("Supprimer l'entité du projet");
                suppMi.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Attention");
                    alert.setHeaderText("Attention la suppression de l'élement du projet entraine \n" +
                            "la suppression de toutes les entités présentes \ndans les différents diagramme du projet");
                    alert.setContentText("Êtes-vous certain de vouloir supprimer l'entité du projet?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        beans.getProject().removeEntity(entityMap.get(((GridPane) event.getSource()).getId()));
                    }
                    alert.close();
                });

                ctxMenuDiagram.getItems().add(addMi);
                ctxMenuDiagram.getItems().add(suppMi);
                ctxMenuDiagram.show(this, event.getScreenX(), event.getScreenY());
            }


        });

    }

}

