package fr.univrouen.umlreverse.ui.view;

import fr.univrouen.umlreverse.UmlReverseBeans;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.diagram.packag.IPackageDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.ui.view.clazz.ClassDiagramEditor;
import fr.univrouen.umlreverse.ui.view.clazz.ToolBarClassController;
import fr.univrouen.umlreverse.ui.view.packag.PackageDiagramEditor;
import fr.univrouen.umlreverse.ui.view.packag.ToolBarPackageController;
import fr.univrouen.umlreverse.ui.view.sequence.SequenceDiagramEditor;
import fr.univrouen.umlreverse.ui.view.sequence.ToolBarSequenceController;
import fr.univrouen.umlreverse.ui.view.usecase.UsecaseDiagramEditor;
import fr.univrouen.umlreverse.util.Contract;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;
import fr.univrouen.umlreverse.ui.view.usecase.ToolBarUseCaseController;

public class ToolBarSwapper extends Region {
	// ATTRIBUTES
	/**
	 * The beans of the application.
	 */
	private UmlReverseBeans beans;

	// CONSTRUCTORS

	/**
	 * Constructor of the tool bar swapper.
	 * @param b The beans of the application.
     */
	public ToolBarSwapper(UmlReverseBeans b) throws IOException {
		Contract.check(b != null, "b must not be null");
		beans = b;

		beans.diagramProperty().addListener(new ChangeListener<IDiagram>() {
			@Override
			public void changed(ObservableValue<? extends IDiagram> observable, IDiagram oldValue,
					IDiagram newValue) {
                        try {
                            updateToolBar(newValue);
                        } catch (IOException ex) {
                            Logger.getLogger(ToolBarSwapper.class.getName()).log(Level.SEVERE, null, ex);
                        }
			}
		});

		updateToolBar(beans.getDiagram());



	}

	// TOOLS

	/**
	 * Update the tool bar.
	 * @param diagram The model of diagram
     */
	private void updateToolBar(IDiagram diagram) throws IOException {
		getChildren().clear();
		if (diagram != null) {
			if (diagram instanceof IPackageDiagram) {
            	try{
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ToolBarPackage.fxml"));

					Node toolBarPackage = fxmlLoader.load();
		            ToolBarPackageController rightController = fxmlLoader.getController();
		            rightController.setDiagram((PackageDiagramEditor) beans.getEditor());
		            getChildren().add(toolBarPackage);
				} catch (IOException e) {
					e.printStackTrace();
					new Alert(AlertType.ERROR, "Impossible de charger la barre d’outils.").showAndWait();
				}
            } else if (diagram instanceof IUsecaseDiagram) {
               try{
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ToolBarUseCase.fxml"));

					Node toolBarUseCase = fxmlLoader.load();
		            ToolBarUseCaseController rightController = fxmlLoader.getController();
		            rightController.setDiagram((UsecaseDiagramEditor) beans.getEditor());
		            getChildren().add(toolBarUseCase);
				} catch (IOException e) {
					new Alert(AlertType.ERROR, "Impossible de charger la barre d’outils.").showAndWait();
				}
            } else if (diagram instanceof ISequenceDiagram) {
            	try{
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ToolBarSequence.fxml"));

					Node toolBarSequence = fxmlLoader.load();
		            ToolBarSequenceController rightController = fxmlLoader.getController();
		            rightController.setDiagram((SequenceDiagramEditor) beans.getEditor());
		            getChildren().add(toolBarSequence);
				} catch (IOException e) {
					e.printStackTrace();
					new Alert(AlertType.ERROR, "Impossible de charger la barre d’outils.").showAndWait();
				}
            } else if (diagram instanceof IClassDiagram) {
	            try {
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ToolBarClass.fxml"));

					Node toolBar = fxmlLoader.load();
		            ToolBarClassController rightController = fxmlLoader.getController();
		            rightController.setDiagram((ClassDiagramEditor) beans.getEditor());
		            getChildren().add(toolBar);
				} catch (IOException e) {
					new Alert(AlertType.ERROR, "Impossible de charger la barre d’outils.").showAndWait();
				}
			}
		}
	}
}






