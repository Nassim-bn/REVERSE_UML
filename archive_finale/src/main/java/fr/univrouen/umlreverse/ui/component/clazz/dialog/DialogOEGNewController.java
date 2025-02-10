package fr.univrouen.umlreverse.ui.component.clazz.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.model.project.ObjectEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewEntity;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.project.Visibility;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.ui.view.clazz.ClassController;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import fr.univrouen.umlreverse.util.ErrorDialog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * OEG's dialog controller. Uses resources/fxml/DialogOEGNew.fxml
 */
public class DialogOEGNewController {
	/** The diagram in use. */
    private ClassController diagramController;
    /** The entity being created. */
    private IViewEntity entity;

    // ObjectEntity common
    @FXML
    private TextField entityName;
    @FXML
    private ComboBox<TypeEntity> entityType;
    @FXML
    private ComboBox<Visibility> entityVisibility;
    @FXML
    private ColorPicker backgroundColor;
    @FXML
    private ColorPicker color;

    /**
     * The entity being created.
     * @return the entity being created
     */
    public IViewEntity getViewEntity() {
        return entity;
    }

    /**
     * Initializes fields and events.
     * @param diagramController the diagram used.
     * @param typeEntity the type wanted.
     */
    public final void setBehaviors(ClassController diagramController, TypeEntity typeEntity) {
        Contract.check(diagramController != null, "diagramController must not be null");
        Contract.check(typeEntity != null, "typeEntity must not be null");
        Platform.runLater(() -> entityName.requestFocus());
        this.diagramController = diagramController;
        // Fills the combo boxes
        List<TypeEntity> typeList = new ArrayList<>();
        Collections.addAll(typeList, TypeEntity.values());
        entityType.setItems(FXCollections.observableList(typeList));
        List<Visibility> visibilities = new ArrayList<>();
        Collections.addAll(visibilities, Visibility.values());
        entityVisibility.setItems(FXCollections.observableList(visibilities));
        // Fills default values
        entityType.setValue(typeEntity);
        entityVisibility.setValue(Visibility.Public);
        color.setValue(Color.web(IDiagramEditorController.TEXT_COLOR_DEFAULT));
        backgroundColor.setValue(Color.web(IDiagramEditorController.BACKGROUND_COLOR_DEFAULT_ENTITY));

        // Change représentation of ComboBox item
        entityType.setCellFactory(new Callback<ListView<TypeEntity>, ListCell<TypeEntity>>() {
            @Override
            public ListCell<TypeEntity> call(ListView<TypeEntity> param) {
                return new ListCell<TypeEntity>(){
                    @Override
                    protected void updateItem(TypeEntity item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null)
                        setText(item.getViewName());
                    }
                };
            }
        });
    }

    /**
     * Add the entity into the diagram from the user input.
     * @throws RefusedAction if there is conflicts between entities.
     */
    public final void addEntity() {
        // Nouvelle entité
        String name = entityName.getText();
        TypeEntity typeEntity = entityType.getValue();
        Visibility visibility = entityVisibility.getValue();

        try {
            IObjectEntity objectEntity =
            		new ObjectEntity(name, typeEntity, visibility);
            diagramController.getDiagram().getProject().addEntity(objectEntity);
            entity = new ViewEntity(objectEntity,
            		diagramController.getDiagram());
            entity.addStyle(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID,
            		toRGBCode(backgroundColor.getValue()));
            entity.addStyle(IDiagramEditorController.TEXT_COLOR_STYLE_ID,
            		toRGBCode(color.getValue()));
        } catch (RefusedAction ex) {
            ErrorDialog.showError("Erreur", ErrorAbstraction.getErrorFromCode(
            		ex.getMessage()).getExplain());
            new DialogOEGNew(diagramController, typeEntity).showAndWait();
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, "Nom d'entité invalide")
            		.showAndWait();
            new DialogOEGNew(diagramController, typeEntity).showAndWait();
        }
    }

    // STATIC PRIVATE TOOLS
    /**
     * @param color the color.
     * @return the rgb web code corresponding to this color.
     */
    private static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
