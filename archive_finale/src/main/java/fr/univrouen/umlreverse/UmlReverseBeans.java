package fr.univrouen.umlreverse;

import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.diagram.common.IObservable;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.ui.view.MenuController;
import fr.univrouen.umlreverse.ui.view.ToolBarSwapper;
import fr.univrouen.umlreverse.ui.view.TreeFileManagerView;
import fr.univrouen.umlreverse.ui.view.common.ADiagramEditor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Diverse elements used by the project.
 */
public class UmlReverseBeans {
	private static UmlReverseBeans INSTANCE;

// ATTRIBUTES

    /**
     * Object property on project to detect a project change.
     */
    private final ObjectProperty<IProject> projectProperty;
    /**
     * Object property on editor to detect an editor change.
     */
    private final ObjectProperty<ADiagramEditor> editorProperty;
    /**
     * Object property on diagram to detect a diagram change.
     */
    private final ObjectProperty<IDiagram> diagramProperty;
    /**
     * Object property on file to detect a file change.
     */
    private final ObjectProperty<File> fileProperty;
    /**
     * The menu controller of the application.
     */
    private MenuController menuController;
    /**
     * The tree view of the current project.
     */
    private TreeFileManagerView treeView;
    /**
     * Split pane that contain all pane except menu.
     */
    private SplitPane splitPane;
    /**
     * Split pane that contain the left pane with the tree view and the entity list.
     */
    private SplitPane leftPane;
    /**
     * Tab pane for center of the splitPane.
     */
    private TabPane centerPane;
    /**
     * Toolbar for the right of the splitpane.
     */
    private ToolBarSwapper rightPane;
    /**
     * The Stage that will contain all pane.
     */
    private Window stage;
    /**
     * Boolean to know if the current project is saved or not.
     */
    private boolean isSaved;

// CONSTRUCTORS

    /**
     * Constructor of the umlReverseBeans.
     * It will contain all data that the project deserve to work correctly
     */
    private UmlReverseBeans() {
        projectProperty = new SimpleObjectProperty<>();
        editorProperty = new SimpleObjectProperty<>();
        diagramProperty = new SimpleObjectProperty<>();
        fileProperty = new SimpleObjectProperty<>();
        isSaved = true;
        createObsForSave();
    }

// REQUESTS

    /**
     * Getter of Menu Controller.
     * @return MenuController
     */
    public MenuController getMenuController() {
    	return menuController;
    }

    /**
     * Getter of the Project model.
     * @return IProject
     */
    public IProject getProject() {
        return projectProperty.get();
    }

    // COMMANDS

    /**
     * Setter of the Project.
     * @param project the new project
     */
    public void setProject(IProject project) {
        this.projectProperty.setValue(project);
    }

    /**
     * Getter of the current editor.
     * @return ADiagramEditor
     */
    public ADiagramEditor getEditor() {
        return editorProperty.get();
    }

    /**
     * Setter of the editor.
     * @param editor the new editor
     */
    public void setEditor(ADiagramEditor editor) {
        editorProperty.set(editor);
    }

    /**
     * Getter of the project property.
     * @return ObjectProperty<IProject>
     */
    public ObjectProperty<IProject> getProjectProperty() {
        return projectProperty;
    }

    /**
     * Getter of the editor property.
     * @return ObjectProperty<ADiagramEditor>
     */
    public ObjectProperty<ADiagramEditor> getEditorProperty() {
        return editorProperty;
    }

    /**
     * Getter of the tree view of the project.
     * @return TreeFileManagerView
     */
    public TreeFileManagerView getTreeView() {
        return treeView;
    }

    /**
     * Setter of the tree view.
     * @param treeView the tree view of the project
     */
    public void setTreeView(TreeFileManagerView treeView) {
        this.treeView = treeView;
    }

    /**
     * Getter of the split pane.
     * @return SplitPane
     */
    public SplitPane getSplitPane() {
        return splitPane;
    }

    /**
     * Setter of the split pane.
     * @param split The split pane of the project
     */
    public void setSplitPane(SplitPane split) {
        splitPane = split;
    }

    /**
     * Getter of the split pane that contain left elements.
     * @return SplitPane
     */
    public SplitPane getLeftPane() {
        return leftPane;
    }

    /**
     * Setter of the left split pane.
     * @param left the splitpane that contain the treeview and the list entities
     */
    public void setLeftPane(SplitPane left) {
        leftPane = left;
    }

    /**
     * Getter of the center Pane.
     * @return TabPane
     */
    public TabPane getCenterPane() {
        return centerPane;
    }

    /**
     * Setter of the center pane.
     * @param center The tab pane that contain center
     */
    public void setCenterPane(TabPane center) {
        centerPane = center;
    }

    /**
     * Getter of the tool bar.
     * @return ToolBarSwapper
     */
    public ToolBarSwapper getRightPane() {
        return rightPane;
    }

    /**
     * Setter of the right Pane with the toolbar.
     * @param right The toolbar for the current diagram.
     */
    public void setRightPane(ToolBarSwapper right) {
        rightPane = right;
    }

    /**
     * Getter of the diagram property.
     * @return ObjectProperty<IDiagram>
     */
    public ObjectProperty<IDiagram> diagramProperty() {
        return diagramProperty;
    }

    /**
     * Getter of the current model of diagram.
     * @return IDiagram
     */
    public IDiagram getDiagram() {
        return diagramProperty.get();
    }

    /**
     * Setter of the diagram model.
     * @param diagram Current diagram (usecase or classDiagram)
     */
    public void setDiagram(IDiagram diagram) {
        diagramProperty.setValue(diagram);
    }

    /**
     * Getter of the save file.
     * @return File
     */
    public File getFile() {
        return fileProperty.get();
    }

    /**
     * Setter of the save file.
     * @param f The file that will serve for the save
     */
    public void setFile(File f) {
        fileProperty.setValue(f);
    }

    /**
     * Getter of the file property.
     * @return ObjectProperty<File>
     */
    public ObjectProperty<File> getFileProperty() {
        return fileProperty;
    }

    /**
     * Getter of the stage of the application.
     * @return Window
     */
    public Window getStage() {
        return stage;
    }

    /**
     * Getter to know if project is saved or not.
     * @return boolean
     */
    public boolean isSaved() {
        return isSaved;
    }

    /**
     * Setter of the window.
     * @param stage The stage of the application.
     */
    public void setWindow(Window stage) {
        this.stage = stage;
    }

    /**
     * Setter of the menu controller.
     * @param mc The menu controller
     */
    public void setMenuController(MenuController mc) {
    	menuController = mc;
    }

    /**
     * Save to current file if it exist or call a fileChooser.
     */
    public void save() {
            try {
                if (getFile().exists()) {
                    getProject().serialize(getFile());
                    isSaved = true;
                } else {
                    setFile(null);
                    saveAs();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * Call a file chooser and wait for user's answer and then save in file the user choose.
     */
    public void saveAs() {
        FileChooser fileChooser = new FileChooser(); 
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("Ser files (*.ser)", "*.ser"));
        File f = fileChooser.showSaveDialog(stage);
        setFile(f);
        if (getFile() != null) {
            try {
                getProject().serialize(getFile());
                isSaved = true;
                String filename = f.getAbsolutePath();
                if (!filename.matches(".*.ser")) {
                    File newFile = new File(f.getAbsolutePath() + ".ser");
                    Files.move(f.toPath(), newFile.toPath());
                    setFile(newFile);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Setter of the boolean isSaved.
     * @param b The boolean value, if the project is saved b equals true, else false
     */
    public void setSaved(boolean b) {
        isSaved = b;
    }

    /**
     * Create observers on project and diagram to determine if a project is saved.
     */
    private void createObsForSave() {
    	final PropertyChangeListener unsave = evt -> setSaved(false);
    	projectProperty.addListener(new ChangeListener<IProject>() {
            @Override
            public void changed(ObservableValue<? extends IProject> observable, IProject oldValue, IProject newValue) {
                getProject().addPropertyChangeListener("NameChanged", unsave);
            }
        });
        diagramProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.addPropertyChangeListener(evt -> {
                    setSaved(false);
                    Object newVal = evt.getNewValue();
                    if (newVal != null && newVal instanceof IObservable) {
                        ((IObservable) newVal).addPropertyChangeListener(unsave);
                    }
                });
                
                getDiagram().getStyle().addPropertyChangeListener(unsave);
            }
        });
    }
    
    // STATIC METHODS

    /**
     * Singleton of UmlReverseBeans.
     * @return UmlReverseBeans
     */
    public static UmlReverseBeans getInstance() {
    	if (INSTANCE == null) {
    		INSTANCE = new UmlReverseBeans();
    	}
    	return INSTANCE;
    }
}
