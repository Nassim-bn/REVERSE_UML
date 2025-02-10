package fr.univrouen.umlreverse.ui.component.clazz.dialog;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewRelation;


import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.util.Contract;
import fr.univrouen.umlreverse.util.Util;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;


// A dialog that allows the user to add a point to a relation.
public class DialogRemovePoint {
	//ATTRIBUTES
	
	/** The dialog. */
	private Dialog<ButtonType> dialog;

	// CONSTRUCTORS
	/**
	 * Create a dialog to remove a point to a relation.
	 */
	public DialogRemovePoint() {
		createDialog();
	}

	// QUERIES
	/**
	 * Show the dialog and wait for the user to close it.
	 * 
	 * @return the result of the dialog
	 */
	public ButtonType showAndWait() {
		return dialog.showAndWait().orElse(null);
	}

	// COMMANDS
	/**
	 * Create the dialog.
	 */
	private void createDialog() {
		dialog = new Dialog<>();
		dialog.setTitle("Retirer un point");
		dialog.setHeaderText("Voulez-vous vraiment retirer ce point de la relation?");
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
	}
	}

