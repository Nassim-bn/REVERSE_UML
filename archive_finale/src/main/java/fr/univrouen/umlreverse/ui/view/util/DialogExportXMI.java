/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.view.util;

import java.io.File;
import java.util.Optional;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.visitor.ExportXMIClassVisitor;
import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.diagram.packag.IPackageDiagram;
import fr.univrouen.umlreverse.model.diagram.packag.PackageDiagram;
import fr.univrouen.umlreverse.model.diagram.packag.visitor.ExportPlantUMLPackageVisitor;
import fr.univrouen.umlreverse.model.diagram.packag.visitor.ExportXMIPackageVisitor;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.SequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.visitor.ExportXMISequenceVisitor;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.UsecaseDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.visitor.ExportXMIUseCaseVisitor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;


public class DialogExportXMI {

// ATTRIBUTES
    private final Dialog<Boolean> dialog;
    private final Button choiceFile;
    private final TextField fileTF;
    private final ComboBox<IDiagram> diagramsBox;
    private final Window window;
    private File file;

// CONSTRUCTORS
    public DialogExportXMI(Set<IDiagram> diagrams, Window window) {
        choiceFile = new Button("Choisir un fichier");
        fileTF = new TextField();
        fileTF.setDisable(true);
        diagramsBox = new ComboBox<>();
        diagramsBox.getItems().addAll(diagrams);
        diagramsBox.setValue(diagrams.iterator().next());
        dialog = new Dialog<>();
        dialog.setResultConverter(dialogButton -> {
            return dialogButton == ButtonType.OK;
        });
        this.window = window;

        createDialog();
        createController();
    }

    public void showAndWait() {
        Optional<Boolean> b = dialog.showAndWait();
        IDiagram diagram = diagramsBox.getValue();
        if (diagram != null && file != null && b.get()) {
            if (diagram.getClass() == ClassDiagram.class) {
                ExportXMIClassVisitor visitor = new ExportXMIClassVisitor(file);
                IClassDiagram classDiagram = (IClassDiagram) diagram;
                classDiagram.accept(visitor);
            } else if (diagram.getClass() == UsecaseDiagram.class) {
                ExportXMIUseCaseVisitor visitor = new ExportXMIUseCaseVisitor(file);
                IUsecaseDiagram usecaseDiagram = (IUsecaseDiagram) diagram;
                visitor.visit(usecaseDiagram);
            } else if (diagram.getClass() == SequenceDiagram.class) {
                ExportXMISequenceVisitor visitor = new ExportXMISequenceVisitor(file);
                ISequenceDiagram sequenceDiagram = (ISequenceDiagram) diagram;
                visitor.visit(sequenceDiagram);
            }else if (diagram.getClass() == PackageDiagram.class) {
                ExportXMIPackageVisitor visitor = new ExportXMIPackageVisitor(file);
                IPackageDiagram packageDiagram = (IPackageDiagram) diagram;
                packageDiagram.accept(visitor);
            }/*
            else if (diagram.getClass() == PackageDiagram.class) {
                ExportXMIPackageVisitor visitor = new ExportXMIPackageVisitor(file);
                IPackageDiagram packageDiagram = (IPackageDiagram) diagram;
                visitor.visit(packageDiagram);
            }*/
        }
    }

    private void createDialog() {
        dialog.setTitle("Exporter en XMI");
        dialog.setHeaderText("Exporter un diagramme en XMI");

        dialog.getDialogPane().getButtonTypes()
        		.addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        grid.add(new Label("Choisir un diagramme"), 0, 0);
        grid.add(diagramsBox, 1, 0);

        grid.add(choiceFile, 0, 1);
        grid.add(fileTF, 1, 1);

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        fileTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                okButton.setDisable(fileTF.getText().isEmpty());
            }
        });
        fileTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                okButton.setDisable(fileTF.getText().isEmpty());
            }
        });
        dialog.getDialogPane().setContent(grid);
    }

    private void createController() {
        choiceFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("XMI files (*xmi)", "*.xmi"));
                file = fileChooser.showSaveDialog(window);
                if (file != null) {
                    if (!file.getAbsolutePath().matches(".*.xmi")) {
                        File newFile = new File(file.getAbsolutePath()+ ".xmi");
                        file = newFile;
                    }
                    fileTF.setText(file.getName());
                }
            }
        });

    }
}
