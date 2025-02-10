package fr.univrouen.umlreverse.ui.component.sequence.dialog;

import static fr.univrouen.umlreverse.util.Util.toRGBCode;

import java.util.ArrayList;
import java.util.List;

import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.model.diagram.sequence.Object;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.model.project.ObjectEntity;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.project.Visibility;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.ui.component.clazz.dialog.DialogOEGNew;
import fr.univrouen.umlreverse.ui.component.usecase.dialog.DialogActorEdit.DialogActorEditMode;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class DialogObjectEditController {

	// ATTRIBUTES
	@FXML
    private ComboBox<String> stereotypeName;
	@FXML
    private TextField objectName;
	@FXML
    private TextField entityName;
	@FXML
    private ColorPicker backgroundColor;
	@FXML
    private ColorPicker color;
    private ISequenceController controller;
    private IObject obj;
    private IObjectEntity entity;

	/**
     * Get IActor to edit.
     * @return IActor
     */
    public IObject getObject() {
        return obj;
    }

    public void setObject(IObject o) {
    	obj = o;
    }

    /**
     * Initializes fields and events.
     * @param diagramController the diagram used.
     * @param typeEntity the type wanted.
     */
    public final void setBehaviors(ISequenceController diagramController) {
    	Contract.check(diagramController != null, "diagramController must not be null");
    	Platform.runLater(() -> entityName.requestFocus());
    	controller = diagramController;
        DialogActorEditMode mode = DialogActorEditMode.CREATE;

        List<String> list = new ArrayList<>();
    	list.add("Acteur");
    	list.add("Système");
    	list.add("Base de Données");
    	list.add("Thread");
    	stereotypeName.setItems(FXCollections.observableList(list));

        if (obj != null) {
            if (obj.getName() == null) {
                throw new NullPointerException("Name null");
            }
            objectName.setText(obj.getName());
            stereotypeName.setValue(obj.getStereotype());
            entityName.setText(obj.getEntity().getName());
            entityName.setDisable(true);
            IStyle style = obj.getStyle();
            String bgColorS = style.getValue(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID);
            backgroundColor.setValue(Color.web(bgColorS));
            String textColor = style.getValue(IDiagramEditorController.TEXT_COLOR_STYLE_ID);
            color.setValue(Color.web(textColor));
            mode = DialogActorEditMode.EDIT;
        } else {
        	color.setValue(Color.web(IDiagramEditorController.TEXT_COLOR_DEFAULT));
            backgroundColor.setValue(Color.web(IDiagramEditorController.BACKGROUND_COLOR_DEFAULT_ENTITY));
       }
    }

    public final void addEntity() {
    	String type = stereotypeName.getValue();
    	String name = objectName.getText();
    	if (entityName.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Nom de classe invalide")
            		.showAndWait();
            new DialogObjectEdit(controller).showAndWait();
    	} else {
	    	if (obj == null) {
		    	entity = new ObjectEntity(entityName.getText(), TypeEntity.Clazz, Visibility.Public);
		    		if (! controller.getDiagram().getProject().contains(entity)) {
						try {
							controller.getDiagram().getProject().addEntity(entity);
						} catch (RefusedAction e) {
						}
		    		}
		    	obj = new Object(entity, name, controller.getDiagram());
	    	} else {
	    		obj.setName(name);
	    	}
	    	obj.setStereotype(type);
	    	obj.addStyle(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID, toRGBCode(backgroundColor.getValue()));
	        obj.addStyle(IDiagramEditorController.TEXT_COLOR_STYLE_ID, toRGBCode(color.getValue()));
    	}
    }
}

