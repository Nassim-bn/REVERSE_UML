/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.view.util;

import fr.univrouen.umlreverse.model.diagram.clazz.importXMI.ImportXMI;
import fr.univrouen.umlreverse.model.io.parser.Java8Loader;
import fr.univrouen.umlreverse.model.io.parser.PlantUMLLoader;
import fr.univrouen.umlreverse.model.io.parser.util.log.Logger;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;


public class DialogImport {
 
// ATTRIBUTES
    private final Dialog<Boolean> dialog;
    private final Button choiceFile;
    private final TextField fileTF;
    private final TextField name;
    private final Window window;
    private List<File> files;
    private File file;
    private final IProject project;
    private final Scene scene;
    private final ImportType type;
    
// CONSTRUCTORS
    public DialogImport(IProject project, Window window, Scene scene, ImportType type) {
        this.type = type;
        this.project = project;
        this.scene = scene;
        if (type == ImportType.CLASSES) {
            choiceFile = new Button("Choisir des fichiers");
        } else if (type == ImportType.PACKAGE) {
            choiceFile = new Button("Choisir un paquetage");
        } else {
            choiceFile = new Button("Choisir un fichier");
        }
        
        fileTF = new TextField();
        fileTF.setDisable(true);
        name = new TextField();
        Platform.runLater(name::requestFocus);
        dialog = new Dialog<>();
        dialog.setResultConverter(dialogButton -> dialogButton == ButtonType.OK);
        dialog.setResizable(true);
        
        this.window = window;
        
        createDialog();
        createController();
    }
    
    public void showAndWait() {
        scene.setCursor(Cursor.WAIT);
        Optional<Boolean> b = dialog.showAndWait();
        if (((files != null && type == ImportType.CLASSES) ||
             (file != null && (type == ImportType.PACKAGE || type == ImportType.PUMLFILE 
             || type == ImportType.XMI)))
                && name.getText() != null && b.get()) {
            try { 
                Java8Loader loader = null;
                PlantUMLLoader pumlLoader = null;
                ImportXMI builder = null;
                if (type == ImportType.CLASSES) {
                    loader = new Java8Loader(project, name.getText(), files);
                } else if (type == ImportType.PUMLFILE) {
                    pumlLoader = new PlantUMLLoader(project, name.getText(), file);
                } else if (type == ImportType.XMI) {
                	InputSource is = new InputSource(file.getAbsolutePath());
                	builder = new ImportXMI(is, name.getText());
					builder.build();
                } else {
                    loader = new Java8Loader(project, name.getText(), file);
                }
                scene.setCursor(Cursor.DEFAULT);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Informations des fichiers parsés");
                if (type.equals(ImportType.PUMLFILE)) {
                    alert.setHeaderText("Import d'un diagramme PlantUML");
                    alert.setContentText("Le diagramme PlantUML a été parsé");
                } else if (type.equals(ImportType.XMI)) {
                    alert.setHeaderText("Import d'un diagramme XMI");
                    alert.setContentText("Le diagramme XMI a été parsé");
                }
                else {
                    alert.setHeaderText("Import des fichiers java");
                    alert.setContentText("Les fichiers java ont été parsé");
                }

                Label label = new Label("Informations :");

                Logger logger;
                if (loader != null) {
                    logger = loader.getLog();
                } else if (pumlLoader != null) {
                    logger = pumlLoader.getLog();
                } else {
                	logger = builder.getLog();
                }
                TextArea textArea = new TextArea(logger.toString());
                textArea.setEditable(false);
                textArea.setWrapText(true);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);

                // Set expandable Exception into the dialog pane.
                alert.getDialogPane().setExpandableContent(expContent);
                alert.setResizable(true);
                alert.showAndWait();
            } catch (Exception refusedAction) {
                scene.setCursor(Cursor.DEFAULT);
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Exception");
                alert.setHeaderText("Le fichier n'a pas pu être parsé");
                alert.setContentText("Le fichier n'a pas pu être parsé.");

                // Create expandable Exception.
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                refusedAction.printStackTrace(pw);
                String exceptionText = sw.toString();

                Label label = new Label("L'exception reçu :");

                TextArea textArea = new TextArea(exceptionText);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);

                // Set expandable Exception into the dialog pane.
                alert.getDialogPane().setExpandableContent(expContent);

                alert.showAndWait();
            } 
        } else {
            scene.setCursor(Cursor.DEFAULT);
        }
    }

	private void createDialog() {
        dialog.setTitle("Importer");
        if (type.equals(ImportType.PUMLFILE)) {
            dialog.setHeaderText("Importer un diagramme PlantUML");
        } else {
            dialog.setHeaderText("Importer des fichiers java");
        }

        dialog.getDialogPane().getButtonTypes()
        		.addAll(ButtonType.OK, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
              
        grid.add(new Label("Nom du diagramme"), 0, 0);
        grid.add(name, 1, 0);      
        
        grid.add(choiceFile, 0, 1);
        grid.add(fileTF, 1, 1);

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                okButton.setDisable(name.getText().isEmpty() || fileTF.getText().isEmpty());
            }
        });
        fileTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                okButton.setDisable(name.getText().isEmpty() || fileTF.getText().isEmpty());
            }
        });
        dialog.getDialogPane().setContent(grid);
    }
    
    private void createController() {
        choiceFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                if (type == ImportType.CLASSES) {
                    fileChooser.getExtensionFilters().add(
                            new FileChooser.ExtensionFilter("Java files (*.java)", "*.java"));
                    files = fileChooser.showOpenMultipleDialog(window);
                    if (files != null) {
                        StringBuilder sb = new StringBuilder();
                        for (File f : files) {
                            sb.append(f.getName()).append(", ");
                        }
                        sb.delete(sb.length() - 2, sb.length());
                        fileTF.setText(sb.toString());
                    }
                } else if (type == ImportType.PUMLFILE || type == ImportType.XMI) {
                    file = fileChooser.showOpenDialog(window);
                    if (file != null) {
                        fileTF.setText(file.getName());
                    }
                } else {
                    DirectoryChooser directoryFile = new DirectoryChooser();
                    file = directoryFile.showDialog(window);
                    if (file != null) {
                        fileTF.setText(file.getName());
                    }
                }
            }
        });
    }
}

