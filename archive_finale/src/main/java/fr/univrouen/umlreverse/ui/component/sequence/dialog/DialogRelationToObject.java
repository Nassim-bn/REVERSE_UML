/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.sequence.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.model.diagram.sequence.IRelationToObject;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.project.Argument;
import fr.univrouen.umlreverse.model.project.IMethod;
import fr.univrouen.umlreverse.model.project.Method;
import fr.univrouen.umlreverse.model.project.Type;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.util.Contract;
import fr.univrouen.umlreverse.util.Util;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class DialogRelationToObject {
// ATTRIBUTES
	
	
	/** The available relation types. */
    private ComboBox<RelationTypeEnum> types;
    /** The name of the relation. */
    
    private GridPane grid;
    
    private ComboBox<IMethod> methodsComboBox; 
    private CheckBox hasArguments;
    private Map<Argument, TextField> argumentsTextField;
    private TextField garde;
    private TextField returnText;
    
    
    /** The stroke color of the relation. */
    private ColorPicker relationColor; // TODO Not implemented yet
    /** The text color of the relation. */
    private ColorPicker textColor; // TODO Not implemented yet
    /** This dialog. */
    private Dialog<ButtonType> dialog;
    /** The relation being edited. */
    private final IRelationToObject relation;
    
    private Dialog<ButtonType> dialogArguments;
    private Button editArguments;
    
    /**
     * Methode choisis lorsqu'on veut utilisé un methode qui n'existe pas dans la classe
     * */
    private IMethod newMethod;
    /**
     * TextField for the newMethode input
     * */
    private TextField newMethodTF;
    
    private IObject targetObj;
    
    // CONSTRUCTORS
    /**
     * Create a dialog to edit a given relation.
     * @param relation the relation to edit, must not be null
     */
    public DialogRelationToObject(IRelationToObject relation) {
        Contract.check(relation != null, 
                "L'argument relation ne doit pas être nul.");
        this.relation = relation;
        
        targetObj = (IObject) relation.getEntityTarget();
        
        argumentsTextField = new HashMap<>();
        createDialog();

        createController();
        
        // select previous method 
 		if (relation.getMethod() != null) {
 			methodsComboBox.getSelectionModel().select(relation.getMethod());
 		} else {
 			methodsComboBox.getSelectionModel().select(newMethod);	
 		}
 		
 		garde.setText(relation.getGarde());
 		returnText.setText(relation.getReturn());
 		hasArguments.selectedProperty().set(relation.hasArguments());
	    editArguments.setDisable(!relation.hasArguments()); 
     		
    }
    
    

	// QUERIES
    /**
     * Shows the dialog and waits for the user response (in other words, brings 
     * up a blocking dialog, with the returned value the users input).
     * Build NoteGraphic based on result of the returned value the users input.
     */
    public void showAndWait() {
        if (dialog.showAndWait().get() == ButtonType.OK) {
            relation.addStyle("color", Util.toRGBCode(relationColor.getValue()));
            relation.addStyle("text-color", Util.toRGBCode(textColor.getValue()));
            RelationTypeEnum t = types.getValue();
            if (t != null) {
                relation.setType(t);
            }
            relation.setGarde(garde.getText());
            if (methodsComboBox.getValue() == newMethod) {
            	
            	try {
					newMethod.setName(newMethodTF.getText());
				} catch (RefusedAction e) {
					// TODO
					System.out.println("erreur");
				}
            	
            	try {
					targetObj.getEntity().addMethod(newMethod);
				} catch (RefusedAction e1) {
					e1.printStackTrace();
					System.out.println("erreur");
					//TODO
				}
            	
            }
            
            relation.setHasArguments(hasArguments.selectedProperty().get());
            relation.setMethod(methodsComboBox.getValue());
            
            Map<Argument, String> argumentsModel = new HashMap<>();
            for (Entry<Argument, TextField> argument: argumentsTextField.entrySet()) {
            	argumentsModel.put(argument.getKey(), argument.getValue().getText());
            }
			relation.setArguments(argumentsModel);
            
            if (hasArguments.selectedProperty().get() 
            		&& methodsComboBox.getValue() != newMethod) {
            	//TODO ???
            }
            relation.setReturn(returnText.getText());
        }
    }
    
    // TOOLS
    /**
     * Generates the dialog.
     */
    private void createDialog() {
    	List<RelationTypeEnum> typeset = RelationTypeEnum.getSequenceTypes();
        types = new ComboBox<>();
        
        for (RelationTypeEnum t: typeset) {
            if (t != RelationTypeEnum.CUSTOM) {
                types.getItems().add(t);
            }
        }
        
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
        
        if (relation.getType() == RelationTypeEnum.OWNER) {
        	types.setDisable(true);
        }
                
        IStyle style = relation.getStyle();
        relationColor = new ColorPicker(Color.web(style.getValue("color")));
        textColor = new ColorPicker(Color.web(style.getValue("text-color")));
 
        // Create the custom dialog.
        dialog = new Dialog<>();
        dialog.setTitle("Éditer une relation");
        dialog.setHeaderText("Éditer une relation");
           
        // Set the icon (must be included in the project).
       // dialog.setGraphic(new ImageView(this.getClass()
        //		.getResource("/img/note_edit.png").toString()));
        // TODO icon plus petite
        
        // Set the button types.
        dialog.getDialogPane().getButtonTypes()
        		.addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the noteText label and field.
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        
        grid.add(new Label("Type de relation :"), 0, 0);
        grid.add(types, 1, 0);
        types.setValue(relation.getType());
        
        List<IMethod> modelMethods = new ArrayList<IMethod>(targetObj.getEntity().getListMethod());
        newMethod = new Method(new Type("void"), "Methode non existante");
        modelMethods.add(0, newMethod);
        ObservableList<IMethod> listMethods = FXCollections.observableList(modelMethods);
		methodsComboBox = new ComboBox<>(listMethods);
		
		
        grid.add(new Label("Méthode"), 0, 1);
        grid.add(methodsComboBox, 1, 1);
        
        grid.add(new Label("Nouvelle méthode"), 0, 2);
        newMethodTF = new TextField();
        grid.add(newMethodTF, 1, 2);
        
        garde = new TextField();
        grid.add(new Label("Garde"), 0, 3);
        grid.add(garde, 1, 3);
        

        hasArguments = new CheckBox();
        hasArguments.setDisable(true);
        grid.add(new Label("Afficher les arguments"), 0, 4);
        grid.add(hasArguments, 1, 4);
        

        grid.add(new Label("Modifier les arguments"), 0, 5);
        editArguments = new Button("Modifier...");
        grid.add(editArguments, 1, 5);
   

        returnText = new TextField();
        grid.add(new Label("Return"), 0, 6);
        grid.add(returnText, 1, 6);
        
        
        grid.add(new Label("Couleur de la relation :"), 0, 7);
        grid.add(relationColor, 1, 7);
              
        grid.add(new Label("Couleur du texte :"), 0, 8);
        grid.add(textColor, 1, 8);
        
        
        
        dialog.getDialogPane().setContent(grid);  
        
    }
    
    
    private void createController() {
    	
    	hasArguments.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				editArguments.setDisable(!newValue);
	
			}
		});
    	
    	editArguments.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				createArgumentDialog(methodsComboBox.getValue());	
				showAndWaitArgumentDialog();
			}
		});
    	
    	
    	methodsComboBox.valueProperty().addListener(new ChangeListener<IMethod>() {

			@Override
			public void changed(ObservableValue<? extends IMethod> observable, IMethod oldValue, IMethod newValue) {
				if (newValue == newMethod) {
					hasArguments.setDisable(true);
					newMethodTF.setDisable(false);
				} else {
					hasArguments.setDisable(false);
					newMethodTF.setDisable(true);	
				}
			}
		});
    	
	}
    
    
    
    private void createArgumentDialog(IMethod method) {
    	
    	// Create the custom dialog.
        dialogArguments = new Dialog<>();
        dialogArguments.setTitle("Éditer les arguments");
        dialogArguments.setHeaderText("Éditer les arguments");
           

        // Set the button types.
        dialogArguments.getDialogPane().getButtonTypes()
        		.addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the noteText label and field.
        GridPane gridArguments = new GridPane();
        gridArguments.setHgap(10);
        gridArguments.setVgap(10);
        gridArguments.setPadding(new Insets(10, 10, 10, 10));
        
        
    	List<Argument> arguments = method.getArguments();
        int sizeArguments = arguments.size();
        
        argumentsTextField.clear();
       
        for (int i = 0; i < sizeArguments; i++) {
        	Label label = new Label(arguments.get(i).getName());
			TextField argumentTextField = new TextField();
			argumentsTextField.put(arguments.get(i),argumentTextField);
			gridArguments.addRow(i, label, argumentTextField);
		}

        dialogArguments.getDialogPane().setContent(gridArguments);
    }
    
    
    private void showAndWaitArgumentDialog() {
        if (dialogArguments.showAndWait().get() == ButtonType.OK) {
            
        }
    }
}
