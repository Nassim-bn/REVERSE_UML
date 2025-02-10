package fr.univrouen.umlreverse.ui.component.clazz.dialog;

import java.util.List;

import fr.univrouen.umlreverse.model.project.Argument;
import fr.univrouen.umlreverse.model.diagram.common.Direction;
import fr.univrouen.umlreverse.model.project.IType;
import fr.univrouen.umlreverse.model.project.Type;
import fr.univrouen.umlreverse.util.Contract;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Controller for the dialog described in `resources/fxml/DialogOEGEdit.fxml`.
 */
public class DialogArgsController {
    // CONSTANTS
	/** Label for a button that edits an argument. */
    public static final String EDIT_TEXT = "Éditer";
    /** Label for a button that adds an argument. */
    public static final String ADD_TEXT = "Ajouter";
    /** Label for a button that deletes an argument. */
    public static final String DELETE_TEXT = "Supprimer";
    /** Label for a button that cancels an operation. */
    public static final String CANCEL_TEXT = "Annuler";

    // ATTRIBUTES
    /** The argument list that this dialog allows to edit. */
    private List<Argument> argumentList;
    /** The argument currently being edited, or null if none is. */
    private Argument currentArgument;

    @FXML
    private ListView<Argument> argumentListView;
    @FXML
    private TextField nameText;
    @FXML
    private TextField typeText;
    @FXML
    private TextField valueText;
    @FXML
    private CheckBox inBox;
    @FXML
    private CheckBox outBox;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button newButton;

    // COMMANDS
    /**
     * Load a new list to edit in the dialog associated with this controller.
     * @param arguments the new list to be edited, must not be null
     */
    public void loadValues(List<Argument> arguments) {
        Contract.check(arguments != null,
        		"La liste des arguments ne doit pas être nulle.");
        this.argumentList = arguments;
        currentArgument = null;
        // TODO None of the behaviors seemingly need to be reset on each update.
        setBehaviors(); 
        argumentListView.setItems(FXCollections.observableList(arguments));
        fill();
        Platform.runLater(nameText::requestFocus);
    }

    // TOOLS
    /**
     * Set the dialog’s controls’ behavior.
     */
    private void setBehaviors() {
        newButton.setOnAction(evt -> fill());
        addButton.setOnAction(evt -> {
            try {
                String name = nameText.getText();
                IType type = Type.getTypefromString(typeText.getText());
                String value = valueText.getText();
                Direction direction = Direction.None;
                if (inBox.isSelected() && outBox.isSelected()) {
                    direction = Direction.InOut;
                } else if (inBox.isSelected()) {
                    direction = Direction.In;
                } else if (outBox.isSelected()) {
                    direction = Direction.Out;
                }
                if (currentArgument != null) {
                    // REMOVE CURRENT ARGUMENT
                    argumentListView.getItems().remove(currentArgument);
                }
                // NEW ARGUMENT
                argumentListView.getItems().add(
                		new Argument(direction, type, name, value));
                fill();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Problème lors de l'ajout de l'argument");
                alert.setContentText("Le type de l'argument est incorrect.");
                alert.showAndWait();
            }
        });
        deleteButton.setOnAction(evt -> {
            if (currentArgument != null) {
            	// TODO Choose one of these two lines, delete the other.
                argumentListView.getItems().remove(currentArgument); 
                argumentListView.setItems(FXCollections.observableList(argumentList));
            }
            fill();
        });
        argumentListView.getSelectionModel().selectedItemProperty()
        		.addListener((ov, old_val, new_val) -> {
            fill(new_val);
        });
        // On enter key, add
        nameText.setOnAction(evt -> addButton.fire());
        typeText.setOnAction(evt -> addButton.fire());
        valueText.setOnAction(evt -> addButton.fire());
    }

    /**
     * Update the current argument to `argument`, and the UI accordingly.
     * @param argument the new current argument
     */
    private void fill(Argument argument) {
        if (argument != null) {
            currentArgument = argument;
            nameText.setText(argument.getName());
            typeText.setText(argument.getType().toString());
            valueText.setText(argument.getInitialization());
            inBox.setSelected(argument.getDirection().equals(Direction.In)
                || argument.getDirection().equals(Direction.InOut));
            outBox.setSelected(argument.getDirection().equals(Direction.Out)
                    || argument.getDirection().equals(Direction.InOut));
            addButton.setText(EDIT_TEXT);
            deleteButton.setText(DELETE_TEXT);
        } else {
            fill();
        }
    }

    /**
     * Update the state and UI to indicate that no argument is being edited.
     */
    private void fill() {
        currentArgument = null;
        nameText.clear();
        typeText.clear();
        valueText.clear();
        inBox.setSelected(false);
        outBox.setSelected(false);
        addButton.setText(ADD_TEXT);
        deleteButton.setText(CANCEL_TEXT);
        nameText.requestFocus();
    }
}
