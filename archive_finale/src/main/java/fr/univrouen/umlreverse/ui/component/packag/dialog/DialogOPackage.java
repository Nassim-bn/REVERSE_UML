package fr.univrouen.umlreverse.ui.component.packag.dialog;

import java.io.IOException;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewPackage;
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
public class DialogOPackage {
    // ATTRIBUTES
	/** The diagram in use. */
    private IPackageController diagramController;
    /** This dialog. */
    private Dialog<ButtonType> dialog;
    /** The controller associated with this dialog. */
    private DialogOPackageController controller;

    private IViewPackage packag;

    // CONSTRUCTORS
    /**
     * OEG's dialog to create a new OEG.
     * @param diagramController the diagram in use.
     * @param typeEntity OEG's type wanted.
     */
    public DialogOPackage(IPackageController diagramController) {
        this.diagramController = diagramController;
        createDialog();
    }

    public DialogOPackage(IPackageController diagramController, IViewPackage pack) {
        this.diagramController = diagramController;
        this.packag = pack;
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
     * The package being created.
     * @return the package being created
     */
    public IViewPackage getViewPackage() {
        return controller.getViewPackage();
    }

    // TOOLS
    /**
     * Initializes and creates the dialog.
     */
    private void createDialog() {
        // Create the custom dialog.
        dialog = new Dialog<>();
        dialog.setTitle("Créer un paquetage");
        dialog.setHeaderText("Créer un paquetage");

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
        		.getResource("/fxml/DialogNewPackage.fxml"));
        try {
            Node root = fxmlLoader.load();
            controller = fxmlLoader.getController();
            dialog.getDialogPane().setContent(root);
            if (this.packag != null) {
            	controller.setPackage(this.packag);
            }
            controller.setBehaviors(diagramController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
