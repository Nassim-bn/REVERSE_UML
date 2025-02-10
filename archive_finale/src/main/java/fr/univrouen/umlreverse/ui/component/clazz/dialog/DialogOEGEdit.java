package fr.univrouen.umlreverse.ui.component.clazz.dialog;

import java.io.IOException;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;

/**
 * OEG's edition / creation dialog.
 */
public class DialogOEGEdit {
    // ATTRIBUTES
	/** The entity being edited. */
    private IViewEntity entity;
    /** This dialog. */
    private Dialog<ButtonType> dialog;
    /** The controller associated with this dialog. */
    private DialogOEGEditController controller;

    // CONTROLLERS
    /**
     * OEG's dialog to edit an existing OEG.
     * @param entityGraphic the entity to edit.
     */
    public DialogOEGEdit(IViewEntity entity) {
        Contract.check(entity != null, "entity must not be null.");
        this.entity = entity;
        createDialog();
    }

    // QUERIES
    /**
     * Shows dialog and wait for user input.
     */
    final public void showAndWait() {
        dialog.showAndWait().ifPresent(response -> validate());
    }

    // TOOLS
    private void validate() {
        try {
            controller.apply();
        } catch (RefusedAction e) {
            DialogOEGEditController.showError("Édition impossible",
            		ErrorAbstraction.getErrorFromCode(e.getMessage()).getExplain());
            showAndWait();
        }
    }
            
            
    /**
     * Initializes and creates the dialog.
     */
    private void createDialog() {
        // Create the custom dialog.
        dialog = new Dialog<>();
        dialog.setTitle("Modifier un objet");
        dialog.setHeaderText("Modifier un objet");

        // Set the icon (must be included in the project).
        ImageView img = new ImageView(this.getClass()
        		.getResource("/img/entity_edit.png").toString());
        img.setFitHeight(50);
        img.setFitWidth(50);
        dialog.setGraphic(img);

        // Set the button types.
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        // FXML Loading
        
        String adrDialogEdit = "/fxml/DialogOEGEdit.fxml";
        if (entity.getType() == TypeEntity.Enumeration) {
            adrDialogEdit = "/fxml/DialogOEGEditEnum.fxml";
        }
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
        		.getResource(adrDialogEdit));
        try {
            Node root = fxmlLoader.load();
            controller = fxmlLoader.getController();
            controller.loadValues(entity);
            dialog.getDialogPane().setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
