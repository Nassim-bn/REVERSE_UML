package fr.univrouen.umlreverse.ui.component.common.dialog;

import static fr.univrouen.umlreverse.util.Util.toRGBCode;

import java.util.Optional;

import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.diagram.common.IDiagramCommon;
import fr.univrouen.umlreverse.model.diagram.common.INote;
import fr.univrouen.umlreverse.model.diagram.common.Note;
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


public class DialogNoteEdit {
    
// ATTRIBUTES
    
    private String text;
    private TextArea noteText;
    private ColorPicker backgroundColorCP;
    private ColorPicker textColorCP;    
    private Dialog<String> dialog;
    private INote note;
    private IDiagramCommon diagram;
// CONSTRUTORS
    public DialogNoteEdit(IDiagramCommon diagram) {
        createDialog();
        this.diagram = diagram;
    }

    public DialogNoteEdit(INote note) {
        Contract.check(note != null, "L'argument note ne doit pas être nul.");
        this.note = note;
        createDialog();
    }
    
    
    
// REQUEST
    
    

	/**
     * Shows the dialog and waits for the user response (in other words, brings 
     * up a blocking dialog, with the returned value the users input).
     * Build NoteGraphic based on result of the returned value the users input.
     */
    public void showAndWait() {
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(noteEdit -> {
            text = noteEdit;
            if (note == null) {
                note = new Note(text, diagram);
                diagram.addNote(note);
            } else {
                note.setText(text);
            }
            note.addStyle(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID, toRGBCode(backgroundColorCP.getValue()));
            note.addStyle(IDiagramEditorController.TEXT_COLOR_STYLE_ID, toRGBCode(textColorCP.getValue()));
        });  
    }
    
    /**
     * Get INote to edit.
     * @return note
     */
    public INote getNote() {
        return note;
    }
    
// PRIVATE 
    private void createDialog() {
        DialogNoteEditMode mode = DialogNoteEditMode.CREATE;
         if (note != null) {
            if (note.getText() == null) {
                throw new NullPointerException("note.getText() is null");
            }
            noteText = new TextArea(note.getText());
            IStyle style = note.getStyle();
            String bgColorS = style.getValue(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID);
            backgroundColorCP = new ColorPicker(Color.web(bgColorS));
            String textColor = style.getValue(IDiagramEditorController.TEXT_COLOR_STYLE_ID);
            textColorCP = new ColorPicker(Color.web(textColor));
            mode = DialogNoteEditMode.EDIT;
        } else {
            noteText = new TextArea();
            backgroundColorCP = new ColorPicker(Color.web(IDiagramEditorController.BACKGROUND_COLOR_DEFAULT_NOTE));
            textColorCP = new ColorPicker(Color.web(IDiagramEditorController.TEXT_COLOR_DEFAULT));
        }
 
        // Create the custom dialog.
        dialog = new Dialog<>();
        dialog.setTitle(mode.getTitle());
        dialog.setHeaderText(mode.getTitle());
            
        // Set the icon (must be included in the project).
        ImageView img = new ImageView(this.getClass().getResource("/img/note_edit.png").toString());
        img.setFitHeight(50);
        img.setFitWidth(50);
        dialog.setGraphic(img);

        // Set the button types.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

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

        // Enable/Disable login button depending on whether a username was entered.
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
    
// ENUM
    
    public enum DialogNoteEditMode {
        CREATE("Créer une note"),
        EDIT("Modifier une note");
        
        String title;
        
        DialogNoteEditMode(String s) {
            title = s;
        }
        
        public String getTitle() {
            return title;
        }
    }
}
