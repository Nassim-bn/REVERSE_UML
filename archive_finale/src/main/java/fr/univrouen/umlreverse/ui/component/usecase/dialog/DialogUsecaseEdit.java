package fr.univrouen.umlreverse.ui.component.usecase.dialog;

import static fr.univrouen.umlreverse.util.Util.toRGBCode;

import java.util.Optional;

import fr.univrouen.umlreverse.model.diagram.usecase.IUsecase;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.Usecase;
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


public class DialogUsecaseEdit {
    
// ATTRIBUTES
    
    private String text;
    private TextArea usecaseText;
    private ColorPicker backgroundColorCP;
    private ColorPicker textColorCP;    
    private Dialog<String> dialog;
    private IUsecase usecase;
    private IUsecaseDiagram diagram;
// CONSTRUTORS
    public DialogUsecaseEdit(IUsecaseDiagram diagram) {
        createDialog();
        this.diagram = diagram;
    }

    public DialogUsecaseEdit(IUsecase usecase) {
        Contract.check(usecase != null, "L'argument usecase ne doit pas être nul.");
        this.usecase = usecase;
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
        
        result.ifPresent(usecaseEdit -> {
            text = usecaseEdit;
            if (usecase == null) {
                usecase = new Usecase(text, diagram.getRootSystem());
                diagram.addUsecase(usecase);
             
            } else {
                usecase.setName(text);
            }
            usecase.addStyle(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID, toRGBCode(backgroundColorCP.getValue()));
            usecase.addStyle(IDiagramEditorController.TEXT_COLOR_STYLE_ID, toRGBCode(textColorCP.getValue()));
        });  
    }
    
    /**
     * Get INote to edit.
     * @return usecase
     */
    public IUsecase getUsecase() {
        return usecase;
    }
    
// PRIVATE 
    private void createDialog() {
        DialogUseCaseEditMode mode = DialogUseCaseEditMode.CREATE;
         if (usecase != null) {
            if (usecase.getName()== null) {
                throw new NullPointerException("usecase.getText() is null");
            }
            usecaseText = new TextArea(usecase.getName());
            IStyle style = usecase.getStyle();
            String bgColorS = style.getValue(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID);
            backgroundColorCP = new ColorPicker(Color.web(bgColorS));
            String textColor = style.getValue(IDiagramEditorController.TEXT_COLOR_STYLE_ID);
            textColorCP = new ColorPicker(Color.web(textColor));
            mode = DialogUseCaseEditMode.EDIT;
        } else {
            usecaseText = new TextArea();
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

        // Create the usecaseText label and field.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        
        usecaseText.setPromptText("Texte");
        usecaseText.setPrefSize(300, 100);
        grid.add(new Label("Texte :"), 0, 0);
        grid.add(usecaseText, 1, 0);
        
        
        grid.add(new Label("Couleur d'arrière plan :"), 0, 1);
        grid.add(backgroundColorCP, 1, 1);
        
        
        grid.add(new Label("Couleur du texte :"), 0, 2);
        grid.add(textColorCP, 1, 2);

        // Enable/Disable login button depending on whether a username was entered.
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        
        if (mode == DialogUseCaseEditMode.CREATE) {
            okButton.setDisable(true);
        }
        // Do some validation (using the Java 8 lambda syntax).
        usecaseText.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the usecaseText field by default.
        Platform.runLater(() -> usecaseText.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return usecaseText.getText();
            }
            return null; 
        });
    }
    
// ENUM
    
    public enum DialogUseCaseEditMode {
        CREATE("Créer un cas d’utilisation"),
        EDIT("Modifier un cas d’utilisation");
        
        String title;
        
        DialogUseCaseEditMode(String s) {
            title = s;
        }
        
        public String getTitle() {
            return title;
        }
    }
}
