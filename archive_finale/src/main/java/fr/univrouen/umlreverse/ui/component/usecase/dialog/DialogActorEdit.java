package fr.univrouen.umlreverse.ui.component.usecase.dialog;


import static fr.univrouen.umlreverse.util.Util.toRGBCode;

import java.util.Optional;

import fr.univrouen.umlreverse.model.diagram.usecase.Actor;
import fr.univrouen.umlreverse.model.diagram.usecase.IActor;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
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

public class DialogActorEdit {
    // ATTRIBUTES

    private String text;
    private TextArea nameText;
    private ColorPicker backgroundColorCP;
    private ColorPicker textColorCP;
    private Dialog<String> dialog;
    private IActor act;
    private IUsecaseDiagram diagram;
    // CONSTRUTORS
    public DialogActorEdit(IUsecaseDiagram diagram) {
        createDialog();
        this.diagram = diagram;
    }

    public DialogActorEdit(IActor act) {
        Contract.check(act != null, "L'argument note ne doit pas être nul.");
        this.act = act;
        createDialog();
    }

// REQUEST

    /**
     * Shows the dialog and waits for the user response (in other words, brings
     * up a blocking dialog, with the returned value the users input).
     * Build Actor based on result of the returned value the users input.
     */
    public void showAndWait() {
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(noteEdit -> {
            text = noteEdit;
            if (act == null) {

                    act = new Actor(text, diagram.getRootSystem());
                    diagram.addActor((Actor) act);
            } else {
                act.setName(text);
            }
            act.addStyle(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID, toRGBCode(backgroundColorCP.getValue()));
            act.addStyle(IDiagramEditorController.TEXT_COLOR_STYLE_ID, toRGBCode(textColorCP.getValue()));
        });
    }

    /**
     * Get IActor to edit.
     * @return IActor
     */
    public IActor getActor() {
        return act;
    }

    // PRIVATE

    /**
     * Create the dialog.
     */
    private void createDialog() {
        DialogActorEditMode mode = DialogActorEditMode.CREATE;
        if (act != null) {
            if (act.getName() == null) {
                throw new NullPointerException("Name null");
            }
            nameText = new TextArea(act.getName());

            IStyle style = act.getStyle();
            String bgColorS = style.getValue(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID);
            backgroundColorCP = new ColorPicker(Color.web(bgColorS));
            String textColor = style.getValue(IDiagramEditorController.TEXT_COLOR_STYLE_ID);
            textColorCP = new ColorPicker(Color.web(textColor));
            mode = DialogActorEditMode.EDIT;
        } else {
            nameText = new TextArea();
            backgroundColorCP = new ColorPicker(Color.web(IDiagramEditorController.BACKGROUND_COLOR_DEFAULT_ACTOR));
            textColorCP = new ColorPicker(Color.web(IDiagramEditorController.TEXT_COLOR_DEFAULT));
        }

        // Create the custom dialog.
        dialog = new Dialog<>();
        dialog.setTitle(mode.getTitle());
        dialog.setHeaderText(mode.getTitle());

        // Set the icon (must be included in the project).
        ImageView img = new ImageView(this.getClass().getResource("/img/entity_edit.png").toString());
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

        nameText.setPromptText("Nom");
        nameText.setPrefSize(300, 50);
        grid.add(new Label("Nom de l'acteur :"), 0, 0);
        grid.add(nameText, 1, 0);


        grid.add(new Label("Couleur de l'acteur :"), 0, 1);
        grid.add(backgroundColorCP, 1, 1);


        grid.add(new Label("Couleur du texte :"), 0, 2);
        grid.add(textColorCP, 1, 2);

        // Enable/Disable login button depending on whether a username was entered.
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);

        if (mode == DialogActorEditMode.CREATE) {
            okButton.setDisable(true);
        }
        // Do some validation (using the Java 8 lambda syntax).
        nameText.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the noteText field by default.
        Platform.runLater(() -> nameText.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {

                return nameText.getText();
            }
            return null;
        });
    }

// ENUM

    public enum DialogActorEditMode {
        CREATE("Créer un acteur"),
        EDIT("Modifier un acteur");

        String title;

        DialogActorEditMode(String s) {
            title = s;
        }

        public String getTitle() {
            return title;
        }
    }
}
