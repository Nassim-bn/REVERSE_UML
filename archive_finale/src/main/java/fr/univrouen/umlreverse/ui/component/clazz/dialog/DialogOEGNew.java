package fr.univrouen.umlreverse.ui.component.clazz.dialog;

import java.io.IOException;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.ui.view.clazz.ClassController;
import fr.univrouen.umlreverse.ui.view.packag.IPackageController;
import fr.univrouen.umlreverse.ui.view.packag.PackageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;

/**
 * OEG's creation dialog.
 */
public class DialogOEGNew {
    // ATTRIBUTES
	/** The diagram in use. */
    private ClassController diagramController;
    /** OEG's type wanted. */
    private TypeEntity typeEntity;
    /** This dialog. */
    private Dialog<ButtonType> dialog;
    /** The controller associated with this dialog. */
    private DialogOEGNewController controller;

    // CONSTRUCTORS
    /**
     * OEG's dialog to create a new OEG.
     * @param diagramController the diagram in use.
     * @param typeEntity OEG's type wanted.
     */
    public DialogOEGNew(ClassController diagramController, TypeEntity typeEntity) {
        this.diagramController = diagramController;
        this.typeEntity = typeEntity;
        createDialog();
    }

    // QUERIES
    /**
     * Shows dialog and wait for user input, then adds entity if input is valid.
     */
    public final void showAndWait() {
        if (dialog.showAndWait().get().equals(ButtonType.OK)) {
            // L'utilisateur a cliqué sur OK
            // Nouvelle entité
            controller.addEntity();
        }
    }

    /**
     * The entity being created.
     * @return the entity being created
     */
    public IViewEntity getViewEntity() {
        return controller.getViewEntity();
    }

    // TOOLS
    /**
     * Initializes and creates the dialog.
     */
    private void createDialog() {
        // Create the custom dialog.
        dialog = new Dialog<>();
        dialog.setTitle("Créer un objet");
        dialog.setHeaderText("Créer un objet");

        // Set the icon (must be included in the project).
        ImageView img = new ImageView(this.getClass()
        		.getResource("/img/entity_edit.png").toString());
        img.setFitHeight(50);
        img.setFitWidth(50);
        dialog.setGraphic(img);

        // Set the button types.
        dialog.getDialogPane().getButtonTypes()
        		.addAll(ButtonType.OK, ButtonType.CANCEL);

        // FXML Loading
        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
        		.getResource("/fxml/DialogOEGNew.fxml"));
        try {
            Node root = fxmlLoader.load();
            controller = fxmlLoader.getController();
            dialog.getDialogPane().setContent(root);
            controller.setBehaviors(diagramController, typeEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
