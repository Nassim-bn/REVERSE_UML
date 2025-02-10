package fr.univrouen.umlreverse.ui.component.clazz.dialog;

import static fr.univrouen.umlreverse.util.Util.toRGBCode;

import java.util.UUID;

import fr.univrouen.umlreverse.model.diagram.clazz.view.NoteClass;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import fr.univrouen.umlreverse.util.ErrorDialog;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import fr.univrouen.umlreverse.model.diagram.clazz.view.INoteClass;

/**
 * A dialog that allows the user to edit a note.
 */
public class DialogNoteEdit {
	// ATTRIBUTES
	/** The color picker used to set a new background color. */
    private ColorPicker backgroundColorCP;
    /** The color picker used to set a new text color. */
    private ColorPicker textColorCP;
    /** This dialog. */
    private Dialog<String> dialog;
    /** The note being edited. */
    private INoteClass note;
    /** The diagram where a note is being created. */
    private IClassDiagram diagram;

    // CONSTRUCTORS
    /**
     * Creates a dialog to create a new note in the given diagram.
     * @param diagram the diagram where a note should be created
     */
    public DialogNoteEdit(IClassDiagram diagram) {
        createDialog();
        this.diagram = diagram;
    }

    /**
     * Creates a dialog to edit the given note.
     * @param note the note to be edited
     */
    public DialogNoteEdit(INoteClass note) {
        Contract.check(note != null, "L'argument note ne doit pas être nul.");
        this.note = note;
        createDialog();
    }

    // QUERIES
    /**
     * Shows the dialog and waits for the user response (in other words, brings
     * up a blocking dialog, with the returned value the users input).
     * Build NoteGraphic based on result of the returned value the users input.
     */
    public void showAndWait() {
        dialog.showAndWait().ifPresent(text -> {
            if (note == null) {
                try {
                    note = new NoteClass(UUID.randomUUID().toString().replace("-", ""),
                    		text, diagram);
                } catch (RefusedAction ex) {
                    ErrorDialog.showError("Erreur", ex.getMessage());
                }
            } else {
                note.setText(text);
            }
            note.addStyle(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID,
            		toRGBCode(backgroundColorCP.getValue()));
            note.addStyle(IDiagramEditorController.TEXT_COLOR_STYLE_ID,
            		toRGBCode(textColorCP.getValue()));
        });
    }

    /**
     * Get INote to edit.
     * @return note
     */
    public INoteClass getNote() {
        return note;
    }

    // TOOLS
    /**
     * Generates the dialog to show.
     */
    private void createDialog() {
        DialogNoteEditMode mode = DialogNoteEditMode.CREATE;
        final TextArea noteText = new TextArea();
         if (note != null) {
            if (note.getText() == null) {
                throw new NullPointerException("note.getText() is null");
            }
            noteText.setText(note.getText());
            IStyle style = note.getStyle();
            String bgColorS = style.getValue(
            		IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID);
            backgroundColorCP = new ColorPicker(Color.web(bgColorS));
            String textColor = style.getValue(
            		IDiagramEditorController.TEXT_COLOR_STYLE_ID);
            textColorCP = new ColorPicker(Color.web(textColor));
            mode = DialogNoteEditMode.EDIT;
        } else {
            backgroundColorCP = new ColorPicker(Color.web(
            		IDiagramEditorController.BACKGROUND_COLOR_DEFAULT_NOTE));
            textColorCP = new ColorPicker(Color.web(
            		IDiagramEditorController.TEXT_COLOR_DEFAULT));
        }

        // Create the custom dialog.
        dialog = new Dialog<>();
        dialog.setTitle(mode.getTitle());
        dialog.setHeaderText(mode.getTitle());

        // Set the icon (must be included in the project).
        ImageView img = new ImageView(this.getClass()
        		.getResource("/img/note_edit.png").toString());
        img.setFitHeight(50);
        img.setFitWidth(50);
        dialog.setGraphic(img);

        // Set the button types.
        dialog.getDialogPane().getButtonTypes()
        		.addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the noteText label and field.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        noteText.setPromptText("Texte");
        noteText.setPrefSize(300, 100);
        grid.add(new Label("Texte :"), 0, 0);
        grid.add(noteText, 1, 0);


        grid.add(new Label("Couleur d'arrière plan :"), 0, 1);
        grid.add(backgroundColorCP, 1, 1);


        grid.add(new Label("Couleur du texte :"), 0, 2);
        grid.add(textColorCP, 1, 2);

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);

        if (mode == DialogNoteEditMode.CREATE) {
            okButton.setDisable(true);
        }
        // Do some validation (using the Java 8 lambda syntax).
        noteText.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the noteText field by default.
        Platform.runLater(() -> noteText.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return noteText.getText();
            }
            return null;
        });
    }

    // NESTED TYPES
    /**
     * A mode of note edition.
     */
    public enum DialogNoteEditMode {
    	/** Note creation. */
        CREATE("Créer une note"),
        /** Note editing. */
        EDIT("Modifier une note");

    	/** The title associated with this edition mode. */
        private String title;

        /**
         * Creates a mode with the specified title.
         * @param s the title of the mode
         */
        DialogNoteEditMode(String s) {
            title = s;
        }

        /**
         * This mode’s title.
         * @return this mode’s title
         */
        public String getTitle() {
            return title;
        }
    }
}
