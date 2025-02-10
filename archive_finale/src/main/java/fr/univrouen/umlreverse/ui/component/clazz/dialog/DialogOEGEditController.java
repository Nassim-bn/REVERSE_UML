/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.clazz.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.project.Argument;
import fr.univrouen.umlreverse.model.project.Attribute;
import fr.univrouen.umlreverse.model.project.IAttribute;
import fr.univrouen.umlreverse.model.project.IMethod;
import fr.univrouen.umlreverse.model.project.IType;
import fr.univrouen.umlreverse.model.project.Method;
import fr.univrouen.umlreverse.model.project.Modifier;
import fr.univrouen.umlreverse.model.project.Type;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.project.Variable;
import fr.univrouen.umlreverse.model.project.Visibility;
import fr.univrouen.umlreverse.model.diagram.util.CommaArrayList;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * OEG's dialog controller. Uses resources/fxml/DialogOEGEdit.fxml
 */
public class DialogOEGEditController {
    // CONSTANTS
	/** Label for a button that edits an element. */
    public static final String EDIT_TEXT = "Éditer";
    /** Label for a button that adds an element. */
    public static final String ADD_TEXT = "Ajouter";
    /** Label for a button that hides an element. */
    public static final String HIDE_TEXT = "Cacher";
    /** Label for a button that shows an element. */
    public static final String SHOW_TEXT = "Afficher";
    /** Label for a button that deletes an element. */
    public static final String DELETE_TEXT = "Supprimer";
    /** Label for a button that cancels an operation. */
    public static final String CANCEL_TEXT = "Annuler";

    // ATTRIBUTES
    /** The entity bing modified.*/
    private IViewEntity entity;
    /** The method being modified. */
    private IMethod currentMethod;
    /** The attribute being modified. */
    private IAttribute currentAttribute;
    /** The enumeration being modified. */
    private String currentEnum;
    /** The miscellaneous thing being modified. */
    private String currentOther;
    /** The arguments list being edited. */
    private List<Argument> argumentList;

    // ObjectEntity common
    @FXML
    private TextField entityName;
    @FXML
    private ComboBox<TypeEntity> entityType;
    @FXML
    private ComboBox<Visibility> entityVisibility;
    @FXML
    private ColorPicker backgroundColor;
    @FXML
    private ColorPicker color;
    // Lists
    @FXML
    private ListView<IMethod> methodList;
    @FXML
    private ListView<IAttribute> attributeList;
    @FXML
    private ListView<String> enumList;
    @FXML
    private ListView<String> otherList;
    // Methods
    @FXML
    private TextField methodName;
    @FXML
    private TextField methodType;
    @FXML
    private Button methodArgsButton;
    @FXML
    private TextField methodProperty;
    @FXML
    private ComboBox<Visibility> methodVisibility;
    @FXML
    private CheckBox staticMethod;
    @FXML
    private CheckBox abstractMethod;
    @FXML
    private Button newMethodButton;
    @FXML
    private Button addMethodButton;
    @FXML
    private Button hideMethodButton;
    @FXML
    private Button deleteMethodButton;
    // Attributes
    @FXML
    private TextField attributeName;
    @FXML
    private TextField attributeType;
    @FXML
    private TextField attributeValue;
    @FXML
    private TextField attributeProperty;
    @FXML
    private ComboBox<Visibility> attributeVisibility;
    @FXML
    private CheckBox staticAttribute;
    @FXML
    private CheckBox abstractAttribute;
    @FXML
    private Button newAttributeButton;
    @FXML
    private Button addAttributeButton;
    @FXML
    private Button hideAttributeButton;
    @FXML
    private Button deleteAttributeButton;
    // Enum fields
    @FXML
    private TextField enumField;
    @FXML
    private Button newEnumButton;
    @FXML
    private Button addEnumButton;
    @FXML
    private Button deleteEnumButton;
    // Other
    @FXML
    private TextField otherText;
    @FXML
    private Button newOtherButton;
    @FXML
    private Button addOtherButton;
    @FXML
    private Button hideOtherButton;
    @FXML
    private Button deleteOtherButton;
    
    private final BooleanProperty textsFieldAreEmptyProperty = new SimpleBooleanProperty(true);
    
    // QUERIES
    /**
     * @return true if all fields are empty. Else false.
     */
    public boolean elementFieldsIsEmpty() {
        return otherText.getText().isEmpty()
                & enumField.getText().isEmpty()
                & attributeName.getText().isEmpty()
                & methodName.getText().isEmpty();
    }

    // COMMANDS
    /**
     * Applies user input to the entity's model.
     * @throws RefusedAction when there are name conflicts.
     */
    public void apply() throws RefusedAction {
        entity.setName(entityName.getText());
        entity.setVisibility(entityVisibility.getValue());
        entity.setType(entityType.getValue());
        entity.addStyle(IDiagramEditorController.TEXT_COLOR_STYLE_ID, toRGBCode(color.getValue()));
        entity.addStyle(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID, toRGBCode(backgroundColor.getValue()));
    }
    
    public final BooleanProperty textsFieldAreEmptyProperty() {
        return textsFieldAreEmptyProperty;
    }

    /**
     * Load values from entity to the dialog.
     * @param entity the entity to load.
     */
    public final void loadValues(IViewEntity entity) {
        Contract.check(entity != null, "entity is null");
        this.entity = entity;
        this.argumentList = new CommaArrayList<>();
        // Fills the combo boxes
        List<TypeEntity> typeList = new ArrayList<>();
        Collections.addAll(typeList, TypeEntity.values());
        entityType.setItems(FXCollections.observableList(typeList));
        // Change représentation of ComboBox item
        entityType.setCellFactory(new Callback<ListView<TypeEntity>, ListCell<TypeEntity>>() {
            @Override
            public ListCell<TypeEntity> call(ListView<TypeEntity> param) {
                return new ListCell<TypeEntity>(){
                    @Override 
                    protected void updateItem(TypeEntity item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null)
                        setText(item.getViewName());
                    }
                };
            }
        });
        List<Visibility> visibilities = new ArrayList<>();
        Collections.addAll(visibilities, Visibility.values());
        entityVisibility.setItems(FXCollections.observableList(visibilities));
        methodVisibility.setItems(FXCollections.observableList(visibilities));
        attributeVisibility.setItems(FXCollections.observableList(visibilities));
        entityName.setText(entity.getName());
        entityType.setValue(entity.getType());
        entityVisibility.setValue(entity.getVisibility());
        color.setValue(Color.web(entity.getStyle().getValue(IDiagramEditorController.TEXT_COLOR_STYLE_ID)));
        backgroundColor.setValue(Color.web(entity.getStyle().getValue(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID)));
        refreshElements();
        setBehaviors();
        
        initialiseTextProperty();
    }
    
    private void initialiseTextProperty() {
        attributeName.textProperty().addListener((observable, oldValue, newValue) -> {
            textsFieldAreEmptyProperty.setValue(elementFieldsIsEmpty());
            addAttributeButton.setDisable(newValue.isEmpty());
        });  
        otherText.textProperty().addListener((observable, oldValue, newValue) -> {
            textsFieldAreEmptyProperty.setValue(elementFieldsIsEmpty());
            addOtherButton.setDisable(newValue.isEmpty());
        });
        enumField.textProperty().addListener((observable, oldValue, newValue) -> {
            textsFieldAreEmptyProperty.setValue(elementFieldsIsEmpty());
            addEnumButton.setDisable(newValue.isEmpty());
        });
        attributeProperty.textProperty().addListener((observable, oldValue, newValue) -> {
            textsFieldAreEmptyProperty.setValue(elementFieldsIsEmpty());
        });
        attributeValue.textProperty().addListener((observable, oldValue, newValue) -> {
            textsFieldAreEmptyProperty.setValue(elementFieldsIsEmpty());
        });
        attributeType.textProperty().addListener((observable, oldValue, newValue) -> {
            textsFieldAreEmptyProperty.setValue(elementFieldsIsEmpty());
        });
        methodProperty.textProperty().addListener((observable, oldValue, newValue) -> {
            textsFieldAreEmptyProperty.setValue(elementFieldsIsEmpty());
        });
        methodType.textProperty().addListener((observable, oldValue, newValue) -> {
            textsFieldAreEmptyProperty.setValue(elementFieldsIsEmpty());
        });
        methodName.textProperty().addListener((observable, oldValue, newValue) -> {
            textsFieldAreEmptyProperty.setValue(elementFieldsIsEmpty());
            addMethodButton.setDisable(newValue.isEmpty());
        });
    }

    /**
     * Initializes all behaviors and controllers of this dialog.
     */
    private void setBehaviors() {
        // NEW
        newAttributeButton.setOnAction(event -> fillAttribute());
        newMethodButton.setOnAction(event -> fillMethod());
        newEnumButton.setOnAction(event -> fillEnum());
        newOtherButton.setOnAction(event -> fillOther());
        // ADD
        addAttributeButton.setOnAction(event -> {
            IType type = Type.getTypefromString(attributeType.getText());
            if (type == null) {
                showError("Erreur lors de l'édition de l'attribut", "Le type est mal formé.");
                attributeType.requestFocus();
            } else {
                if (currentAttribute != null) {
                    // Edit current attribute
                    try {
                        currentAttribute.setVisibility(attributeVisibility.getValue());
                        currentAttribute.setProperty(attributeProperty.getText());
                        currentAttribute.getVariable().setType(type);
                        currentAttribute.getVariable().setName(attributeName.getText());
                        currentAttribute.getVariable().setInitialization(attributeValue.getText());
                        if (staticAttribute.isSelected())
                            currentAttribute.addModifier(Modifier.Static);
                        else if (currentAttribute.getModifier().contains(Modifier.Static))
                            currentAttribute.removeModifier(Modifier.Static);
                        if (abstractAttribute.isSelected())
                            currentAttribute.addModifier(Modifier.Abstract);
                        else if (currentAttribute.getModifier().contains(Modifier.Abstract))
                            currentAttribute.removeModifier(Modifier.Abstract);
                        refreshElements();
                    } catch (IllegalArgumentException e) {
                        showError("Erreur lors de l'édition de l'attribut", "Le type et le nom de l'attribut sont obligatoires.");
                    } catch (Exception e) {
                        showError("Erreur lors de l'édition de l'attribut", "La signature de cet attribut est déjà existante.");
                    }
                } else {
                    // Add new attribute
                    IAttribute attribute = new Attribute(attributeVisibility.getValue(), attributeProperty.getText(),
                            new Variable(type, attributeName.getText(), attributeValue.getText()));
                    if (staticAttribute.isSelected())
                        attribute.addModifier(Modifier.Static);
                    if (abstractAttribute.isSelected())
                        attribute.addModifier(Modifier.Abstract);
                    try {
                        entity.addAttribute(attribute);
                        if (hideAttributeButton.getText().equals(SHOW_TEXT))
                            entity.hideAttribute(attribute);
                        refreshElements();
                    } catch (IllegalArgumentException e) {
                        showError("Erreur lors de l'ajout de l'attribut", "Le type et le nom de l'attribut sont obligatoires.");
                    } catch (Exception e) {
                        showError("Erreur lors de l'ajout de l'attribut", "La signature de cet attribut est déjà existante.");
                    }
                }
                attributeName.requestFocus();
            }
            textsFieldAreEmptyProperty.setValue(elementFieldsIsEmpty());
        });
        addMethodButton.setOnAction(event -> {
            IType type = Type.getTypefromString(methodType.getText());
            if (type == null) {
                showError("Erreur lors de l'édition de la méthode", "Le type est mal formé.");
                methodType.requestFocus();
            } else {
                if (currentMethod != null) {
                    // Edit current method
                    try {
                        currentMethod.setArguments(argumentList);
                        try {
                            currentMethod.setName(methodName.getText());
                            currentMethod.setProperty(methodProperty.getText());
                            currentMethod.setReturn(type);
                            currentMethod.setVisibility(methodVisibility.getValue());
                            if (staticMethod.isSelected())
                                currentMethod.addModifier(Modifier.Static);
                            else if (currentMethod.getModifier().contains(Modifier.Static))
                                currentMethod.removeModifier(Modifier.Static);
                            if (abstractMethod.isSelected())
                                currentMethod.addModifier(Modifier.Abstract);
                            else if (currentMethod.getModifier().contains(Modifier.Abstract))
                                currentMethod.removeModifier(Modifier.Abstract);
                            refreshElements();
                        } catch (IllegalArgumentException e) {
                            showError("Erreur lors de l'édition de la méthode", "Le type et le nom de la méthode sont obligatoires.");
                        } catch (Exception e) {
                            showError("Erreur lors de l'édition de la méthode", "La signature de cette méthode est déjà existante.");
                        }
                    } catch (Exception e) {
                        showError("Erreur lors de l'édition de la méthode", "Les arguments ne doivent pas avoir la même signature.");
                    }
                } else {
                    // Add new method
                    IMethod method;
                    try {
                        method = new Method(methodVisibility.getValue(), type, methodName.getText(), argumentList, methodProperty.getText());
                        if (staticMethod.isSelected())
                            method.addModifier(Modifier.Static);
                        if (abstractMethod.isSelected())
                            method.addModifier(Modifier.Abstract);
                        try {
                            entity.addMethod(method);
                            if (hideMethodButton.getText().equals(SHOW_TEXT))
                                entity.hideMethod(method);
                            refreshElements();
                        } catch (IllegalArgumentException e) {
                            showError("Erreur lors de l'ajout de la méthode", "Le type et le nom de la méthode sont obligatoires.");
                        } catch (Exception e) {
                            showError("Erreur lors de l'ajout de la méthode", "La signature de cette méthode est déjà existante.");
                        }
                    } catch (Exception e) {
                        showError("Erreur lors de l'ajout de la méthode", "Les arguments ne doivent pas avoir la même signature.");
                    }
                }
                methodName.requestFocus();
            }
            textsFieldAreEmptyProperty.setValue(elementFieldsIsEmpty());
        });
        addEnumButton.setOnAction(event -> {
            if (currentEnum != null)
                entity.getData().removeEnumField(currentEnum);
            try {
                entity.addEnumField(enumField.getText());
                refreshElements();
            } catch (IllegalArgumentException e) {
                showError("Erreur lors de l'ajout du champ", "Le champ est vide.");
            } catch (Exception e) {
                showError("Erreur lors de l'ajout du champ", "Le champ est déjà existant.");
            }
            enumField.requestFocus();
            textsFieldAreEmptyProperty.setValue(elementFieldsIsEmpty());
        });
        addOtherButton.setOnAction(event -> {
            if (currentOther != null)
                entity.getData().removeAbstractText(currentOther);
            try {
                entity.addAbstractText(otherText.getText());
                if (hideOtherButton.getText().equals(SHOW_TEXT))
                    entity.hideAbstractText(otherText.getText());
                refreshElements();
            } catch (IllegalArgumentException e) {
                showError("Erreur lors de l'ajout du texte", "Le texte est vide.");
            } catch (Exception e) {
                showError("Erreur lors de l'ajout du texte", "Le texte est déjà existant.");
            }
            otherText.requestFocus();
            textsFieldAreEmptyProperty.setValue(elementFieldsIsEmpty());
        });
        // HIDE
        hideAttributeButton.setOnAction(event -> {
            if (hideAttributeButton.getText().equals(HIDE_TEXT)) {
                // HIDE
                if (currentAttribute != null) {
                    entity.hideAttribute(currentAttribute);
                }
                hideAttributeButton.setText(SHOW_TEXT);
            } else {
                // SHOW
                if (currentAttribute != null) {
                    try {
                        entity.addAttribute(currentAttribute);
                    } catch (RefusedAction e) {
                        showError("Action Impossible", ErrorAbstraction.getErrorFromCode(e.getMessage()).getExplain());                    }
                }
                hideAttributeButton.setText(HIDE_TEXT);
            }
            textsFieldAreEmptyProperty.setValue(true);
        });
        hideMethodButton.setOnAction(event -> {
            if (hideMethodButton.getText().equals(HIDE_TEXT)) {
                // HIDE
                if (currentMethod != null) {
                    entity.hideMethod(currentMethod);
                }
                hideMethodButton.setText(SHOW_TEXT);
            } else {
                // SHOW
                if (currentMethod != null) {
                    try {
                        entity.addMethod(currentMethod);
                    } catch (RefusedAction e) {
                        showError("Action Impossible", ErrorAbstraction.getErrorFromCode(e.getMessage()).getExplain());
                    }
                }
                hideMethodButton.setText(HIDE_TEXT);
            }
            textsFieldAreEmptyProperty.setValue(true);
        });
        hideOtherButton.setOnAction(event -> {
            if (hideOtherButton.getText().equals(HIDE_TEXT)) {
                // HIDE
                if (currentOther != null) {
                    entity.hideAbstractText(currentOther);
                }
                hideOtherButton.setText(SHOW_TEXT);
            } else {
                // SHOW
                if (currentOther != null) {
                    entity.addAbstractText(currentOther);
                }
                hideOtherButton.setText(HIDE_TEXT);
            }
            textsFieldAreEmptyProperty.setValue(true);
        });
        // DELETE
        deleteAttributeButton.setOnAction(event -> {
            if (currentAttribute != null) {
                entity.getData().removeAttribute(currentAttribute);
                refreshElements();
            }
            fillAttribute();
            textsFieldAreEmptyProperty.setValue(true);
        });
        deleteMethodButton.setOnAction(event -> {
            if (currentMethod != null) {
                entity.getData().removeMethod(currentMethod);
                refreshElements();
            }
            fillMethod();
            textsFieldAreEmptyProperty.setValue(true);
        });
        deleteEnumButton.setOnAction(event -> {
            if (currentEnum != null) {
                entity.getData().removeEnumField(currentEnum);
                refreshElements();
            }
            fillEnum();
            textsFieldAreEmptyProperty.setValue(true);
        });
        deleteOtherButton.setOnAction(event -> {
            if (currentOther != null) {
                entity.getData().removeAbstractText(currentOther);
                refreshElements();
            }
            fillOther();
            textsFieldAreEmptyProperty.setValue(true);
        });
        // SELECTION
        attributeList.getSelectionModel().selectedItemProperty().addListener((ov, old_val, new_val) -> {
            fillAttribute(new_val);
        });
        methodList.getSelectionModel().selectedItemProperty().addListener((ov, old_val, new_val) -> {
            fillMethod(new_val);
        });
        enumList.getSelectionModel().selectedItemProperty().addListener((ov, old_val, new_val) -> {
            fillEnum(new_val);
        });
        otherList.getSelectionModel().selectedItemProperty().addListener((ov, old_val, new_val) -> {
            fillOther(new_val);
        });
        // ARGS BUTTON
        methodArgsButton.setOnAction(event -> {
            new DialogArgs(argumentList);
            methodArgsButton.setText("(" + argumentList.toString() + ")");
        });
        // On enter key, add field
        attributeName.setOnAction(event -> addAttributeButton.fire());
        attributeProperty.setOnAction(event -> addAttributeButton.fire());
        attributeType.setOnAction(event -> addAttributeButton.fire());
        attributeValue.setOnAction(event -> addAttributeButton.fire());
        //attributeVisibility.setOnAction(event -> addAttributeButton.fire());
        methodName.setOnAction(event -> addMethodButton.fire());
        methodProperty.setOnAction(event -> addMethodButton.fire());
        methodType.setOnAction(event -> addMethodButton.fire());
        //methodVisibility.setOnAction(event -> addMethodButton.fire());
        enumField.setOnAction(event -> addEnumButton.fire());
        otherText.setOnAction(event -> addOtherButton.fire());

        // Request focus for entity name
        Platform.runLater(entityName::requestFocus);
    }

    /**
     * Refresh the fields lists and the tabs contents.
     */
    private void refreshElements() {
        // Fills methods
        List<IMethod> methods = new ArrayList<>();
        methods.addAll(entity.getData().getListMethod());
        methodList.getSelectionModel().clearSelection();
        if (methodList.getItems() != null)
            methodList.getItems().clear();
        methodList.setItems(FXCollections.observableList(methods));
        fillMethod();
        // Fills attributes
        List<IAttribute> attributes = new ArrayList<>();
        attributes.addAll(entity.getData().getListAttribute());
        attributeList.getSelectionModel().clearSelection();
        if (attributeList.getItems() != null)
            attributeList.getItems().clear();
        attributeList.setItems(FXCollections.observableList(attributes));
        fillAttribute();
        // Fills enum fields
        List<String> fields = new ArrayList<>();
        fields.addAll(entity.getData().getEnumFields());
        enumList.getSelectionModel().clearSelection();
        if (enumList.getItems() != null)
            enumList.getItems().clear();
        enumList.setItems(FXCollections.observableList(fields));
        fillEnum();
        // Fills others
        List<String> others = new ArrayList<>();
        others.addAll(entity.getData().getAbstractTexts());
        otherList.getSelectionModel().clearSelection();
        if (otherList.getItems() != null)
            otherList.getItems().clear();
        otherList.setItems(FXCollections.observableList(others));
        fillOther();
    }

    /**
     * Clears the Attribute tab.
     */
    private void fillAttribute() {
        attributeVisibility.setValue(Visibility.Private);
        attributeName.clear();
        attributeType.setText("int");
        attributeValue.clear();
        attributeProperty.clear();
        staticAttribute.setSelected(false);
        abstractAttribute.setSelected(false);
        currentAttribute = null;
        addAttributeButton.setText(ADD_TEXT);
        deleteAttributeButton.setText(CANCEL_TEXT);
        hideAttributeButton.setText(HIDE_TEXT);
    }

    /**
     * Fills the Attribute tab.
     * @param attribute the selected attribute.
     */
    private void fillAttribute(IAttribute attribute) {
        if (attribute != null) {
            attributeVisibility.setValue(attribute.getVisibility());
            attributeName.setText(attribute.getVariable().getName());
            attributeType.setText(attribute.getVariable().getType().toString());
            attributeValue.setText(attribute.getVariable().getInitialization());
            attributeProperty.setText(attribute.getProperty());
            staticAttribute.setSelected(attribute.getModifier().contains(Modifier.Static));
            abstractAttribute.setSelected(attribute.getModifier().contains(Modifier.Abstract));
            currentAttribute = attribute;
            addAttributeButton.setText(EDIT_TEXT);
            deleteAttributeButton.setText(DELETE_TEXT);
            if (entity.getListAttribute().contains(attribute))
                hideAttributeButton.setText(HIDE_TEXT);
            else
                hideAttributeButton.setText(SHOW_TEXT);
        } else {
            fillAttribute();
        }
    }

    /**
     * Clears the Method tab.
     */
    private void fillMethod() {
        methodVisibility.setValue(Visibility.Public);
        methodName.clear();
        methodType.setText("void");
        methodArgsButton.setText("()");
        argumentList.clear();
        methodProperty.clear();
        staticMethod.setSelected(false);
        abstractMethod.setSelected(false);
        currentMethod = null;
        addMethodButton.setText(ADD_TEXT);
        deleteMethodButton.setText(CANCEL_TEXT);
        hideMethodButton.setText(HIDE_TEXT);
    }

    /**
     * Fills the method tab.
     * @param method the selected method.
     */
    private void fillMethod(IMethod method) {
        if (method != null) {
            methodVisibility.setValue(method.getVisibility());
            methodName.setText(method.getName());
            methodType.setText(method.getReturn().toString());
            methodArgsButton.setText("(" + method.getArguments().toString() + ")");
            argumentList.clear();
            argumentList.addAll(method.getArguments());
            methodProperty.setText(method.getProperty());
            staticMethod.setSelected(method.getModifier().contains(Modifier.Static));
            abstractMethod.setSelected(method.getModifier().contains(Modifier.Abstract));
            currentMethod = method;
            addMethodButton.setText(EDIT_TEXT);
            deleteMethodButton.setText(DELETE_TEXT);
            if (entity.getListMethod().contains(method))
                hideMethodButton.setText(HIDE_TEXT);
            else
                hideMethodButton.setText(SHOW_TEXT);
        } else {
            fillMethod();
        }
    }

    /**
     * Clears the Enum tab.
     */
    private void fillEnum() {
        enumField.clear();
        currentEnum = null;
        addEnumButton.setText(ADD_TEXT);
        deleteEnumButton.setText(CANCEL_TEXT);
    }

    /**
     * Fills the Enum tab.
     * @param field the selected enum field.
     */
    private void fillEnum(String field) {
        if (field != null) {
            enumField.setText(field);
            currentEnum = field;
            addEnumButton.setText(EDIT_TEXT);
            deleteEnumButton.setText(DELETE_TEXT);
        } else {
            fillEnum();
        }
    }

    /**
     * Clears the Other tab.
     */
    private void fillOther() {
        otherText.clear();
        currentOther = null;
        addOtherButton.setText(ADD_TEXT);
        deleteOtherButton.setText(CANCEL_TEXT);
        hideOtherButton.setText(HIDE_TEXT);
    }

    /**
     * Fills the Other tab.
     * @param other the selected value.
     */
    private void fillOther(String other) {
        if (other != null) {
            otherText.setText(other);
            currentOther = other;
            addOtherButton.setText(EDIT_TEXT);
            deleteOtherButton.setText(DELETE_TEXT);
            if (entity.getAbstractTexts().contains(other))
                hideOtherButton.setText(HIDE_TEXT);
            else
                hideOtherButton.setText(SHOW_TEXT);
        } else {
            fillOther();
        }
    }

    // STATIC PUBLIC TOOLS
    /**
     * Shows an error dialog.
     * @param header the dialog's header.
     * @param content the dialog's content.
     */
    public static void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // STATIC PRIVATE TOOLS
    /**
     * @param color the color.
     * @return the rgb web code corresponding to this color.
     */
    private static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }
}
