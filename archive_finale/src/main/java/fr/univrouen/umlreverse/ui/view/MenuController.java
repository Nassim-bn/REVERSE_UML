package fr.univrouen.umlreverse.ui.view;

import fr.univrouen.umlreverse.UmlReverseBeans;




import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.diagram.packag.IPackageDiagram;
import fr.univrouen.umlreverse.model.diagram.packag.PackageDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.SequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.UsecaseDiagram;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.project.Project;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.ui.view.util.DialogExportPuml;
import fr.univrouen.umlreverse.ui.view.util.DialogExportXMI;
import fr.univrouen.umlreverse.ui.view.util.DialogImport;
import fr.univrouen.umlreverse.ui.view.util.ImportType;
import fr.univrouen.umlreverse.ui.view.util.PannableCanvas;
import fr.univrouen.umlreverse.util.Contract;
//import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.*;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;


public class MenuController {

    // ATRIBUTES
    /**
     * Menu bar that will correspond to this controller.
     */
    @FXML private MenuBar bar;
    /**
     * The border pane that will correspond to this controller.
     */
    @FXML private BorderPane borderpane;
    /**
     * Menu item that will allow to save project.
     */
    @FXML private MenuItem saveProject;
    /**
     * Menu item that will allow to save as project.
     */
    @FXML private MenuItem saveAsProject;
    /**
     * Menu item that allow to create a new use case.
     */
    @FXML private MenuItem newUseCase;
    /**
     * Menu item that allow to create a new class diagram.
     */
    @FXML private MenuItem newClass;
    /**
     * Menu item that allow to create a new sequence diagram.
     */
    @FXML private MenuItem newSequence;
    /**
     * Menu item that allow to create a new package diagram.
     */
    @FXML private MenuItem newPackage;
    /**
     * Menu item that allow to export a diagram to plant uml.
     */
    @FXML private MenuItem exportToPuml;
    /**
     * Menu item that allow to export a diagram to plant uml.
     */
    @FXML private MenuItem pdf;
    /**
     * Menu item that allow to export a diagram to plant uml.
     */
    @FXML private MenuItem exportToXMI;
    /**
     * Menu item that allow to export a diagram to picture.
     */
    @FXML private MenuItem exportToPicture;
    /**
     * Menu item that allow to print a diagram.
     */
    @FXML private MenuItem print;
    /**
     * Menu item that allow to import a plant uml file.
     */
    @FXML private MenuItem importPuml;
    /**
     * Menu item that allow to import a Java classes.
     */
    @FXML private MenuItem importJava;
    /**
     * Menu item that allow to import a Java project.
     */
    @FXML private MenuItem importJavaPackage;
    /**
     * Menu item that allow to zoom in.
     */
    @FXML private MenuItem zoomIn;
    /**
     * Menu item that allow to zoom out.
     */
    @FXML private MenuItem zoomOut;
    /**
     * A toolbar.
     */
    @FXML private CheckMenuItem toolbar;
    /**
     * An explorer.
     */
    @FXML private CheckMenuItem explorer;
    /**
     * The stage of the application.
     */
    private Window stage;
    /**
     * The beans of the application.
     */
    private UmlReverseBeans beans;
    /**
     * The size of the explorer pane (left).
     */
    private double explorerSize;
    /**
     * The size of the toolbar pane (right).
     */
    private double toolbarSize;

    private Scene scene;
// COMMANDES

    /**
     * Set UmlReverseBeans.
     *
     * @param beans the beans of the application
     */
    public void setUmlReverseBeans(UmlReverseBeans beans) {
        Contract.check(beans != null);
        this.beans = beans;
        createController(beans);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Create a new Project.
     */
    public void newProjet() {
        if (saveControlAndSave()) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nouveau projet");
            dialog.setHeaderText("Ajouter un nouveau projet");
            dialog.setContentText("Entrer un nom :");
            beans.setFile(null);
            beans.setSaved(false);
            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();

            result.ifPresent(name -> {
                try {
                    IProject project = Project.newProject(name);
                    beans.setProject(project);
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setContentText("Nom de projet invalide");
                    alert.showAndWait();
                    newProjet();
                }
            });
        }
    }

    /**
     * Create a new  use case diagram.
     */
    public void newUseCase() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nouveau diagramme");
        dialog.setHeaderText("Ajouter un nouveau diagramme de cas d'utilisation");
        dialog.setContentText("Entrer un nom :");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            try {
                IUsecaseDiagram diagram = new UsecaseDiagram(beans.getProject(), name);
                beans.setDiagram(diagram);
                beans.setSaved(false);
            } catch (RefusedAction ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText(ErrorAbstraction.getErrorFromCode(ex.getMessage()).getExplain());
                alert.showAndWait();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Nom de diagramme invalide");
                alert.showAndWait();
                newClass();
            }
        });
    }

    /**
     * Create a new class diagram.
     */
    public void newClass() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nouveau diagramme");
        dialog.setHeaderText("Ajouter un nouveau diagramme de classe");
        dialog.setContentText("Entrer un nom :");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            try {
                IClassDiagram diagram = new ClassDiagram(beans.getProject(), name);
                beans.setDiagram(diagram);
                beans.setSaved(false);
            } catch (RefusedAction ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText(ErrorAbstraction.getErrorFromCode(ex.getMessage()).getExplain());
                alert.showAndWait();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Nom de diagramme invalide");
                alert.showAndWait();
                newClass();
            }
        });
    }

    /**
     * Create a new class diagram.
     */
    public void newSequence() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nouveau diagramme");
        dialog.setHeaderText("Ajouter un nouveau diagramme de séquence");
        dialog.setContentText("Entrer un nom :");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            try {
                ISequenceDiagram diagram = new SequenceDiagram(beans.getProject(), name);
                beans.setDiagram(diagram);
                beans.setSaved(false);
            } catch (RefusedAction ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText(ErrorAbstraction.getErrorFromCode(ex.getMessage()).getExplain());
                alert.showAndWait();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Nom de diagramme invalide");
                alert.showAndWait();
                newSequence();
            }
        });
    }

    /**
     * Create a new diagram package.
     */
    public void newPackage() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nouveau paquetage");
        dialog.setHeaderText("Ajouter un diagramme de paquetage");
        dialog.setContentText("Entrer un nom :");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            try {
            	IPackageDiagram diagram = new PackageDiagram(beans.getProject(), name);
                beans.setDiagram(diagram);
                beans.setSaved(false);
            } catch (RefusedAction ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText(ErrorAbstraction.getErrorFromCode(ex.getMessage()).getExplain());
                alert.showAndWait();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Nom de diagramme invalide");
                alert.showAndWait();
                newPackage();
            }
        });
    }


    /**
     * Open a new Project.
     */
    public void openProjet() {
        boolean bool = saveControlAndSave();
        if (bool) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Ser files (*.ser)", "*.ser"),
                    new ExtensionFilter("All Files", "*.*"));
            fileChooser.setTitle("Ouvrir Projet");
            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                if (file.exists()) {
                    try {
                        if (beans.getFile() == null || !beans.getFile().equals(file)) {

                            beans.setProject(Project.deserialize(file));
                            beans.setFile(file);
                            beans.setSaved(true);

                        } else {

                        }
                    } catch (IOException e) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Erreur fichier invalide");
                        alert.setContentText("Fichier ne représentant pas un projet valide");
                        alert.showAndWait();
                        openProjet();
                    } catch (ClassNotFoundException e) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Erreur fichier invalide");
                        alert.setContentText("Fichier invalide");
                        alert.showAndWait();
                        openProjet();
                    }
                }
            }
        }
    }

    /**
     * Save the project.
     */
    public void save() {
        if (beans.getFile() != null) {
            beans.save();
        } else {
            beans.saveAs();
        }
    }

    /**
     * Create a pop up for saving project.
     */
    public void saveAs() {
        beans.saveAs();
    }

    /**
     * Export File PLANTUML.
     */
    public void exportPUML() {
        DialogExportPuml dialog = new DialogExportPuml(beans.getProject().getDiagrams(), stage);
        dialog.showAndWait();
    }

    /**
     * Export XMI File.
     */
    public void exportXMI() {
        DialogExportXMI dialog = new DialogExportXMI(beans.getProject().getDiagrams(), stage);
        dialog.showAndWait();
    }


    /**
     * Export to image PNG.
     *
     * @throws IOException
     */
    public void exportImg() throws IOException {
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        BufferedImage img = SwingFXUtils.fromFXImage(
                beans.getEditor().getCanvas().snapshot(sp, null),
                null);
        FileChooser fc = new FileChooser();
        fc.setTitle("Exporter le diagramme en image");
        fc.getExtensionFilters().add(new ExtensionFilter("PNG", "*.png"));
        File f = fc.showSaveDialog(bar.getScene().getWindow());
        if (f != null) {
            ImageWriter writer = ImageIO.getImageWritersBySuffix(
                    fc.getSelectedExtensionFilter().getDescription()).next();
            writer.setOutput(ImageIO.createImageOutputStream(f));
            writer.write(img);
        }
    }
    
    
    /**
     * Exporte l'image en PDF.
     *
     */
    public void exportPdf() throws IOException {
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        BufferedImage img = SwingFXUtils.fromFXImage(
                beans.getEditor().getCanvas().snapshot(sp, null),
                null);

        // Dimensions de l'image
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();

        // Ajout d'éspaces supplémentaire pour bien couvrir tous les elements
        float extraSpace = 100; // Adjust as needed
        float pageWidth = imgWidth + 2 * extraSpace;
        float pageHeight = imgHeight + 2 * extraSpace;

        // Création d'un document PDF
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(new PDRectangle(pageWidth, pageHeight));
        doc.addPage(page);

        // Calcul de la position de l'image
        float imageX = extraSpace;
        float imageY = extraSpace;

        // Intégrer l'image dans le PDF.
        PDImageXObject pdImage = LosslessFactory.createFromImage(doc, img);
        try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
            contentStream.drawImage(pdImage, imageX, imageY, imgWidth, imgHeight);
        }

        // Enregistrer le PDF
        FileChooser fc = new FileChooser();
        fc.setTitle("Exporter le diagramme en PDF");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        File f = fc.showSaveDialog(bar.getScene().getWindow());
        if (f != null) {
            doc.save(f);
        }

  
        doc.close();
    }
    
    

    /**
     * Print a diagram.
     */
    public void print() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            if (job.showPrintDialog(stage) && job.showPageSetupDialog(stage)) {
                if (job.printPage(beans.getEditor())) {
                    job.endJob();
                }
            } else {
                new Alert(AlertType.INFORMATION, "Impression annulée.").show();
            }
        } else {
            new Alert(AlertType.ERROR, "Aucune imprimante disponible.").show();
        }
    }

    /**
     * Import a xmi file.
     *
     * @throws IOException
     */
    public void importXMI() throws IOException {
        DialogImport dialog = new DialogImport(beans.getProject(), stage, scene, ImportType.XMI);
        dialog.showAndWait();
    }

    /**
     * Import a plantuml file.
     *
     * @throws IOException
     */
    public void importPuml() throws IOException {
        DialogImport dialog = new DialogImport(beans.getProject(), stage, scene, ImportType.PUMLFILE);
        dialog.showAndWait();
    }


    /**
     * Import a Java project.
     *
     * @throws IOException
     */
    public void importJava() throws IOException {
       DialogImport dialog = new DialogImport(beans.getProject(), stage, scene, ImportType.CLASSES);
       dialog.showAndWait();
    }

    public void importJavaPackage() throws IOException {
       DialogImport dialog = new DialogImport(beans.getProject(), stage, scene, ImportType.PACKAGE);
       dialog.showAndWait();
    }

    /**
     * Close Application Menu Alert.
     */
    public void close() {
        Alert dialogW = new Alert(AlertType.WARNING
                , "Voulez vous sauvegarder avant de quitter l'application ?"
                , ButtonType.CANCEL, ButtonType.NO, ButtonType.YES);
        dialogW.setTitle("Attention");
        dialogW.setHeaderText("Attention, votre travail n'est pas sauvegardé.");
        dialogW.showAndWait();
        if (dialogW.getResult() != ButtonType.CANCEL) {
            if (dialogW.getResult() == ButtonType.YES) {
                save();
            }
            System.exit(0);
        }
    }

    /**
     * ZOOM ++.
     */
    public void zoomIn() {
        zoom(2);
    }

    /**
     * ZOOM --.
     */
    public void zoomOut() {
        zoom(.5);
    }

    /**
     * Set the zoom to the attribute factor.
     *
     * @param factor The factor of zoom.
     */
    private void zoom(double factor) {
    	PannableCanvas cvs = beans.getEditor().getCanvas();
        cvs.setScale(factor * cvs.getScale());
    }


    /**
     * Show and hide the tool bar.
     * @throws Exception
     */
    public void showHide() throws Exception {
        toolbar.setOnAction(e -> {
            if (toolbar.isSelected()) {
                borderpane.getRight().setVisible(false);
            }
        });
    }

    /**
     * Show the explorer.
     */
    public void showExplorer() {
    	SplitPane sp = beans.getSplitPane();
    	if (explorer.isSelected()) {
    		sp.getItems().add(0, beans.getLeftPane());
    		sp.setDividerPosition(1, sp.getDividerPositions()[0]);
    		sp.setDividerPosition(0, explorerSize);
    	} else {
    		double[] positions = sp.getDividerPositions();
    		sp.getItems().remove(beans.getLeftPane());
    		explorerSize = positions[0];
    		if (positions.length > 1) {
    			sp.setDividerPosition(0, positions[1]);
    		}
    	}
    }

    /**
     * Show the tool bar.
     */
    public void showToolbar() {
    	SplitPane sp = beans.getSplitPane();
    	double[] positions = sp.getDividerPositions();
    	if (toolbar.isSelected()) {
    		sp.getItems().add(beans.getRightPane());
    		sp.setDividerPosition(positions.length, toolbarSize);
    	} else {
    		sp.getItems().remove(beans.getRightPane());
    		toolbarSize = positions[positions.length - 1];
    	}
    }


// PRIVATE

    /**
     * add Controller to manage the menu items.
     *
     * @param beans the beans that contain all information needed to manage items
     */
    private void createController(UmlReverseBeans beans) {
        beans.getEditorProperty().addListener((observable, oldValue, newValue) -> {
        	boolean disable = newValue == null;
            exportToPuml.setDisable(disable);
            exportToXMI.setDisable(disable);
            pdf.setDisable(disable);
            exportToPicture.setDisable(disable);
            print.setDisable(disable);
            zoomIn.setDisable(disable);
            zoomOut.setDisable(disable);
        });

        beans.getProjectProperty().addListener((observable, oldValue, newValue) -> {
        	boolean disable = newValue == null;
            saveProject.setDisable(disable);
            saveAsProject.setDisable(disable);
            newUseCase.setDisable(disable);
            newClass.setDisable(disable);
            newSequence.setDisable(disable);
            newPackage.setDisable(disable);
            importPuml.setDisable(disable);
            importJava.setDisable(disable);
            importJavaPackage.setDisable(disable);
        });

        /*beans.getFileProperty().addListener((observable, oldValue, newValue) -> {
            saveProject.setDisable(newValue == null);
        });*/
    }

    private boolean saveControlAndSave() {
        if (beans.getProject() != null && !beans.isSaved()) {
            Alert a = new Alert(AlertType.WARNING,
                    "Voulez vous sauvegarder votre travail?",
                    ButtonType.CANCEL, ButtonType.NO, ButtonType.YES);

            a.setTitle("Attention");
            a.setHeaderText("Attention, votre travail n'est pas sauvegardé.");

            a.showAndWait();
            if (a.getResult() == ButtonType.YES) {

                if (beans.getFile() != null) {
                    beans.save();
                } else {
                    beans.saveAs();
                }
            }
            if (a.getResult() == ButtonType.CANCEL) {
                return false;
            }
        }
        return true;
    }
}
