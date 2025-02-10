package fr.univrouen.umlreverse.ui.component.sequence.dialog;

import java.io.IOException;

import fr.univrouen.umlreverse.model.diagram.sequence.IActivity;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegment;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;

public class DialogActivityEdit {

	// ATTRIBUTS
	private ISequenceController diagramController;
	private Dialog<ButtonType> dialog;
	private DialogActivityEditController controller;
	private IActivity act;

	// CONSTRUCTEUR
	public DialogActivityEdit(ISequenceController diagramController) {
		Contract.check(diagramController != null, "diagramController doesn't be null");
		this.diagramController = diagramController;
		createDialog();
	}

	public DialogActivityEdit(ISequenceController diagramController, IActivity act) {
        this.diagramController = diagramController;
        this.act = act;
        createDialog();
    }

	// REQUETES
	public IActivity getActivity() {
		return controller.getActivity();
	}

	// COMMANDES
	 /**
     * Shows dialog and wait for user input, then adds entity if input is valid.
     */
    public final void showAndWait() {
        if (dialog.showAndWait().get().equals(ButtonType.OK)) {
            controller.addEntity();
        }
    }

	public void createDialog() {
		dialog = new Dialog<>();
		dialog.setTitle("Créer une activité");
		dialog.setHeaderText("Créer une activité");

		ImageView img = new ImageView(this.getClass().getResource("/img/entity_edit.png").toString());
		img.setFitHeight(50);
		img.setFitWidth(50);
		dialog.setGraphic(img);

		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/DialogNewActivity.fxml"));

		try {
			Node root = fxmlLoader.load();
			controller = fxmlLoader.getController();
			dialog.getDialogPane().setContent(root);
			if (act != null) {
				controller.setActivity(act);
			}
			controller.setBehaviors(diagramController);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
