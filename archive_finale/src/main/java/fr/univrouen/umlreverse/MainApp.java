package fr.univrouen.umlreverse;

import fr.univrouen.umlreverse.ui.view.DiagramTabPane;
import fr.univrouen.umlreverse.ui.view.DirectAccessToAllEntityPane;
import fr.univrouen.umlreverse.ui.view.MenuController;
import fr.univrouen.umlreverse.ui.view.ToolBarSwapper;
import fr.univrouen.umlreverse.ui.view.TreeFileManagerView;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The entry point of this project.
 */
public class MainApp extends Application {
    
// ATTRIBUTES
    private static final int LEFT_PANEL_WIDTH_DEFAULT = 180;
    private static final int WIDTH_DEFAULT = 600;
    private static final int HEIGHT_DEFAULT = 500;
    private UmlReverseBeans beans;
    
// COMMANDS

    @Override
    public void start(Stage stage) throws Exception {
        beans = UmlReverseBeans.getInstance();
        beans.setWindow(stage);

        BorderPane borderpane = new BorderPane();
        Scene scene = new Scene(borderpane); {
            FXMLLoader fxmlLoader;
            
            // CENTER
            TreeFileManagerView treeView = new TreeFileManagerView(beans);
            DirectAccessToAllEntityPane dataep = new DirectAccessToAllEntityPane(beans);
            beans.setTreeView(treeView);
            SplitPane leftPane = new SplitPane(treeView, dataep);
            leftPane.setOrientation(Orientation.VERTICAL); 
            leftPane.setPrefWidth(LEFT_PANEL_WIDTH_DEFAULT);
            leftPane.setPrefHeight(HEIGHT_DEFAULT);
            beans.setLeftPane(leftPane);
            SplitPane.setResizableWithParent(leftPane, false);
            
            //Represente les differents diagrammes
            DiagramTabPane tabPane = new DiagramTabPane(WIDTH_DEFAULT, HEIGHT_DEFAULT, beans);
            beans.setCenterPane(tabPane.getTabPane());
            
            //Represente les differents outils
            ToolBarSwapper rightPane = new ToolBarSwapper(beans);
            beans.setRightPane(rightPane);
            SplitPane.setResizableWithParent(rightPane, false);
            
            //Represente la fenetre principale
            SplitPane splitPane = new SplitPane(leftPane, tabPane.getTabPane(), rightPane);
            splitPane.setDividerPositions(.25, .75);
            beans.setSplitPane(splitPane);
            borderpane.setCenter(splitPane);
            
            // TOP
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Menu.fxml"));
            Node menu = fxmlLoader.load();
            MenuController menuController = fxmlLoader.getController();
            menuController.setUmlReverseBeans(beans);
            menuController.setScene(scene);
            beans.setMenuController(menuController);
            borderpane.setTop(menu);
        }
        
        scene.getStylesheets().add("/styles/Styles.css");    
        setActionToQuitApplication(stage);     
        stage.setTitle("UML Reverse");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(true);
        //stage.setFullScreen(true);

    }
    
    private void setActionToQuitApplication(Stage stage) {
        //Show alert when you click in the red boutton close
        stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent event) {
                if (!beans.isSaved()) {
                    Alert dialogW = new Alert(AlertType.WARNING,
                            "Voulez vous sauvegarder avant de quitter l'application ?",
                            ButtonType.CANCEL, ButtonType.NO, ButtonType.YES);
                    dialogW.setTitle("Attention");
                    dialogW.setHeaderText("Attention, votre travail n'est pas sauvegardé.");
                    dialogW.showAndWait();

                    if (dialogW.getResult() == ButtonType.YES) {
                        if (beans.getFile() != null) {
                            beans.save();
                        } else {
                            beans.saveAs();
                        }

                    }
                    if (dialogW.getResult() == ButtonType.NO) {
                        System.exit(0);
                    }
                    if (dialogW.getResult() == ButtonType.CANCEL) {
                        //stage.initOwner(stage);
                        event.consume();
                    }

                } else {
                    System.exit(0);
                }
            }
        });
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
