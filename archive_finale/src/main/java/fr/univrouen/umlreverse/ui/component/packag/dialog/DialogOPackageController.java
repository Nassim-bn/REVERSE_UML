package fr.univrouen.umlreverse.ui.component.packag.dialog;

import java.util.ArrayList;
import java.util.List;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewPackage;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewPackage;
import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.model.project.ObjectEntity;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.project.Visibility;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.ui.component.clazz.dialog.DialogOEGNew;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.packag.IPackageController;
import fr.univrouen.umlreverse.util.Contract;
import fr.univrouen.umlreverse.util.ErrorDialog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * OEG's dialog controller. Uses resources/fxml/DialogOPackage.fxml
 */
public class DialogOPackageController {

    /** The package being created. */
    private IViewPackage entity;

    private IPackageController controller;

    @FXML
    private TextField entityName;
    @FXML
    private ColorPicker color;
    @FXML
    private ListView<String> listViewAdd;
    @FXML
    private ListView<String> listViewRemove;


    /**
     * The package being created.
     * @return the package being created
     */
    public IViewPackage getViewPackage() {
        return entity;
    }

    @FXML
    public void addParameter() {
    	if (listViewAdd.getSelectionModel().getSelectedItem() != null) {
	        String old = listViewAdd.getSelectionModel().getSelectedItem();

	        List<String> listEntities = new ArrayList<>(listViewAdd.getItems());
	        listEntities.remove(old);
	        listViewAdd.setItems(FXCollections.observableList(listEntities));

	        listEntities = new ArrayList<>(listViewRemove.getItems());
	        listEntities.add(old);
	        listViewRemove.setItems(FXCollections.observableList(listEntities));
    	}
    }

    @FXML
    public void removeParameter() {
    	if (listViewRemove.getSelectionModel().getSelectedItem() != null) {
	        String old = listViewRemove.getSelectionModel().getSelectedItem();

	        List<String> listEntities = new ArrayList<>(listViewRemove.getItems());
	        listEntities.remove(old);
	        listViewRemove.setItems(FXCollections.observableList(listEntities));

	        listEntities = new ArrayList<>(listViewAdd.getItems());
	        listEntities.add(old);
	        listViewAdd.setItems(FXCollections.observableList(listEntities));
    	}
    }

    /**
     * Initializes fields and events.
     * @param diagramController the diagram used.
     * @param typeEntity the type wanted.
     */
    public final void setBehaviors(IPackageController diagramController) {
        Contract.check(diagramController != null, "diagramController must not be null");
        Platform.runLater(() -> entityName.requestFocus());
        this.controller = diagramController;

        if (this.entity != null) {
			this.entityName.setText(entity.getName());
			this.entity.setParameterName(entity.getNameParameter());

			List<String> listEntities = new ArrayList<>();
	        List<String> listParameters = new ArrayList<>(entity.getParameters());

	        for (IObjectEntity oe : controller.getDiagram().getProject().getAllEntities()) {
	        	if (!listParameters.contains(oe.getName()) && !oe.getType().equals(TypeEntity.Packag)) {
	        		listEntities.add(oe.getName());
	        	}
	        }
			listViewAdd.setItems(FXCollections.observableList(listEntities));
			listViewRemove.setItems(FXCollections.observableList(listParameters));

			for (String s : listViewAdd.getItems()) {
				entity.removeParameter(s);
			}
			for (String s : listViewRemove.getItems()) {
				entity.addParameter(s);
			}

		} else {
			List<String> listEntities = new ArrayList<>();
	        for (IObjectEntity oe : controller.getDiagram().getProject().getAllEntities()) {
	        	if (!oe.getType().equals(TypeEntity.Packag)) {
	        		listEntities.add(oe.getName());
	        	}
	        }
			listViewAdd.setItems(FXCollections.observableList(listEntities));

			color.setValue(Color.web(IDiagramEditorController.TEXT_COLOR_DEFAULT));
		}
    }

    /**
     * Add the entity into the diagram from the user input.
     * @throws RefusedAction if there is conflicts between entities.
     */
    public final void addEntity() {
    	// Nouvelle entité
    	String name = entityName.getText();

    	TypeEntity typeEntity = TypeEntity.Packag;
        Visibility visibility = Visibility.Private;
        
         if (entity == null) {
        	 if (name.equals("")) {
        		 ErrorDialog.showError("Erreur", "Nom invalide");
	    	            new DialogOPackage(controller).showAndWait();
	    	         entity = null;
        	 } else {
        	    IObjectEntity objectEntity =
	    				new ObjectEntity(name, typeEntity, visibility);
	    		entity = new ViewPackage(objectEntity, name, controller.getDiagram());
	
	    		try {
	    			controller.getDiagram().getProject().addEntity(objectEntity);
	    		} catch (RefusedAction e) {
	    			 ErrorDialog.showError("Erreur", ErrorAbstraction.getErrorFromCode(
	    	            	e.getMessage()).getExplain());
	    	            new DialogOPackage(controller).showAndWait();
	    	         entity = null;
	    	         return;
	    		}
            }
		} else {
			try {
				entity.setName(name);
			} catch (RefusedAction e) {
				ErrorDialog.showError("Erreur", ErrorAbstraction.getErrorFromCode(
    	            	e.getMessage()).getExplain());
    	            new DialogOPackage(controller, entity).showAndWait();
			}
		}

    	for (String s : listViewRemove.getItems()) {
    		entity.addParameter(s);
		}
		for (String s : listViewAdd.getItems()) {
			entity.removeParameter(s);
		}

		String s = "";
		int cpt = entity.getParameters().size();
    	for (String sp : entity.getParameters()) {
    		if (cpt != 1 && cpt > 0) {
    			s += sp + ", ";
    		} else {
    			s += sp;
    		}
    		cpt--;
    	}
		entity.setParameterName(s);

        entity.addStyle(IDiagramEditorController.TEXT_COLOR_STYLE_ID,
        		toRGBCode(color.getValue()));
    }

    public void setPackage(IViewPackage pack) {
		this.entity = pack;
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
