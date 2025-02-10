package fr.univrouen.umlreverse.ui.component.sequence.dialog;

import java.io.IOException;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;

/**
 * OEG's creation dialog.
 */
public class DialogObjectEdit {
    // ATTRIBUTES
	/** The diagram in use. */
    private ISequenceController diagramController;
    /** This dialog. */
    private Dialog<ButtonType> dialog;
    /** The controller associated with this dialog. */
    private DialogObjectEditController controller;
    
    private IObject obj;

    // CONSTRUCTORS
    /**
     * OEG's dialog to create a new OEG.
     * @param diagramController the diagram in use.
     * @param typeEntity OEG's type wanted.
     */
    public DialogObjectEdit(ISequenceController diagramController) {
        this.diagramController = diagramController;
        createDialog();
    }
    
    public DialogObjectEdit(ISequenceController diagramController, IObject obj) {
        this.diagramController = diagramController;
        this.obj = obj;
        createDialog();
    }

    // QUERIES
    /**
     * Shows dialog and wait for user input, then adds entity if input is valid.
     */
    public final void showAndWait() {
        if (dialog.showAndWait().get().equals(ButtonType.OK)) {
            controller.addEntity();
        }
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
        		.getResource("/fxml/DialogNewObjectSequence.fxml"));
        try {
            Node root = fxmlLoader.load();
            controller = fxmlLoader.getController();
            dialog.getDialogPane().setContent(root);
            if (obj != null) {
            	controller.setObject(obj);
            }
            controller.setBehaviors(diagramController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public IObject getObject() {
		return controller.getObject();
	}
}
