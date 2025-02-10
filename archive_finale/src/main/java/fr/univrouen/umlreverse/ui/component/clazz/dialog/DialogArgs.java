package fr.univrouen.umlreverse.ui.component.clazz.dialog;

import java.util.List;

import fr.univrouen.umlreverse.model.project.Argument;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;


/**
 * A dialog that allows the user to edit an entity’s arguments.
 */
public class DialogArgs {
	/**
	 * Creates and displays a dialog that lets the user edit a given list.
	 * @param arguments the list to be edited
	 */
    public DialogArgs(List<Argument> arguments) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Édition des arguments");
        dialog.setHeaderText("Édition des arguments");

        // Set the icon (must be included in the project).
        dialog.setGraphic(new ImageView(getClass()
        		.getResource("/img/note_edit.png").toString()));

        // Set the button types.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        // FXML Loading
        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
        		.getResource("/fxml/DialogArgs.fxml"));
        try {
            Node root = fxmlLoader.load();
            DialogArgsController controller = fxmlLoader.getController();
            controller.loadValues(arguments);
            dialog.getDialogPane().setContent(root);
            dialog.showAndWait();
        } catch (Exception e) {
            // TODO gérer l'exception
            e.printStackTrace();
        }
    }
}
