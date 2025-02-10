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

/**
 * A dialog that allows the user to edit a relation.
 */
public class DialogOEGRelation {
// ATTRIBUTES
	/** The available relation types. */
    private ComboBox<RelationTypeEnum> types;
    /** The name of the relation. */
    private TextField name;
    /** The source cardinality of the relation. */
    private TextField srcCardi;
    /** The target cardinality of the relation. */
    private TextField dstCardi;
    /** The stroke color of the relation. */
    private ColorPicker relationColor;
    /** The text color of the relation. */
    private ColorPicker textColor;
    /** This dialog. */
    private Dialog<ButtonType> dialog;
    /** The relation being edited. */
    private final IViewRelation relation;
    
    // CONSTRUCTORS
    /**
     * Create a dialog to edit a given relation.
     * @param relation the relation to edit, must not be null
     */
    public DialogOEGRelation(IViewRelation relation) {
        Contract.check(relation != null, 
                "L'argument relation ne doit pas être nul.");
        this.relation = relation;
        createDialog();
    }
    
    // QUERIES
    /**
     * Shows the dialog and waits for the user response (in other words, brings 
     * up a blocking dialog, with the returned value the users input).
     * Build NoteGraphic based on result of the returned value the users input.
     */
    public void showAndWait() {
        if (dialog.showAndWait().get() == ButtonType.OK) {
            relation.setNameRelation(name.getText());
            relation.setTailCardinality(srcCardi.getText());
            relation.setHeadCardinality(dstCardi.getText());
            relation.addStyle("color", Util.toRGBCode(relationColor.getValue()));
            relation.addStyle("text-color", Util.toRGBCode(textColor.getValue()));
            RelationTypeEnum t = types.getValue();
            if (t != null) {
                relation.setArrowHead(t.getHead());
                relation.setArrowTail(t.getTail());
                relation.setLineArrow(t.getLine());
            }
        }
    }
    
    // TOOLS
    /**
     * Generates the dialog.
     */
    private void createDialog() {
        types = new ComboBox<>();
        
        for (RelationTypeEnum t : RelationTypeEnum.getClassTypes()) {
            if (t != RelationTypeEnum.CUSTOM) {
                types.getItems().add(t);
            }
        }
        
        TypeHeadArrow head = relation.getArrowHead();    
        TypeHeadArrow tail = relation.getArrowTail();
        TypeLineArrow line = relation.getLineArrow();
        
        RelationTypeEnum type = RelationTypeEnum.CUSTOM;
        for (RelationTypeEnum t : RelationTypeEnum.getClassTypes()) {
            if (t.getHead() == head &&
            		t.getLine() == line &&
            		t.getTail() == tail) {
                type = t;
            }
        }
        types.setValue(type);
        
        types.setCellFactory(new Callback<ListView<RelationTypeEnum>, ListCell<RelationTypeEnum>>() {
            @Override
            public ListCell<RelationTypeEnum> call(ListView<RelationTypeEnum> param) {
                return new ListCell<RelationTypeEnum>(){
                    { 
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY); 
                    }

                    @Override 
                    protected void updateItem(RelationTypeEnum item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(item.getGroup());
                        }
                   }
                };
            }
        });
            
        name = new TextField(relation.getNameRelation());
        srcCardi = new TextField(relation.getTailCardinality());
        dstCardi = new TextField(relation.getHeadCardinality());
                
        IStyle style = relation.getStyle();
        relationColor = new ColorPicker(Color.web(style.getValue("color")));
        textColor = new ColorPicker(Color.web(style.getValue("text-color")));
 
        // Create the custom dialog.
        dialog = new Dialog<>();
        dialog.setTitle("Éditer une relation");
        dialog.setHeaderText("Éditer une relation");
            
        // Set the icon (must be included in the project).
        dialog.setGraphic(new ImageView(this.getClass()
        		.getResource("/img/note_edit.png").toString()));

        // Set the button types.
        dialog.getDialogPane().getButtonTypes()
        		.addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the noteText label and field.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        
        grid.add(new Label("Type de relation :"), 0, 0);
        grid.add(types, 1, 0);
        
        grid.add(new Label("Nom :"), 0, 1);
        grid.add(name, 1, 1);
        
        grid.add(new Label("Cardinalité source :"), 0, 2);
        grid.add(srcCardi, 1, 2);
        
        grid.add(new Label("Cardinalité destination :"), 0, 3);
        grid.add(dstCardi, 1, 3);
              
        grid.add(new Label("Couleur de la relation :"), 0, 4);
        grid.add(relationColor, 1, 4);
              
        grid.add(new Label("Couleur du texte :"), 0, 5);
        grid.add(textColor, 1, 5);
        
        dialog.getDialogPane().setContent(grid);   
    }
}
