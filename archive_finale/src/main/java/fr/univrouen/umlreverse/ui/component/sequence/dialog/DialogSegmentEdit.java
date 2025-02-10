package fr.univrouen.umlreverse.ui.component.sequence.dialog;

import java.io.IOException;

import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegment;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegmentCommon;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;

public class DialogSegmentEdit {

	// ATTRIBUTS
	private ISequenceController diagramController;
	private Dialog<ButtonType> dialog;
	private DialogSegmentEditController controller;
	private ISegmentCommon seg;

	// CONSTRUCTEUR
	public DialogSegmentEdit(ISequenceController diagramController) {
		Contract.check(diagramController != null, "diagramController doesn't be null");
		this.diagramController = diagramController;
		createDialog();
	}

	public DialogSegmentEdit(ISequenceController diagramController, ISegmentCommon model) {
        this.diagramController = diagramController;
        this.seg = model;
        createDialog();
    }

	// REQUETES
	public ISegmentCommon getSegment() {
		return controller.getSegment();
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
		dialog.setTitle("Créer un segment");
		dialog.setHeaderText("Créer un segment");

		ImageView img = new ImageView(this.getClass().getResource("/img/entity_edit.png").toString());
		img.setFitHeight(50);
		img.setFitWidth(50);
		dialog.setGraphic(img);

		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/DialogNewSegmentSeq.fxml"));

		try {
			Node root = fxmlLoader.load();
			controller = fxmlLoader.getController();
			dialog.getDialogPane().setContent(root);
			if (seg != null) {
            	controller.setSegment(seg);
            }
			controller.setBehaviors(diagramController);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
