package fr.univrouen.umlreverse.ui.view.common;

import fr.univrouen.umlreverse.ui.view.util.NodeGestures;
import fr.univrouen.umlreverse.ui.component.common.elements.ISelectionableEntityGraphic;
import fr.univrouen.umlreverse.util.Contract;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;


public abstract class ADiagramEditorController implements IDiagramEditorController {
    
// ATTRIBUTES
    /**
     * The diagram editor that will correspond to this controller.
     */
    protected final ADiagramEditor editor;
    /**
     * The node gesture of the diagram editor.
     */
    protected NodeGestures nodeGestures;
    /**
     * Menu item that allow to zoom in.
     */
    private MenuItem zoomInMI;
    /**
     * Menu item that allow to zoom out.
     */
    private MenuItem zoomOutMI;
    /**
     * Menu item that allow to create a note.
     */
    private MenuItem createNoteMI;
    /**
     * The selected entity.
     */
    private ISelectionableEntityGraphic selectedEntity;
    /**
     * The x position of the cursor.
     */
    private double xCursor;
    /**
     * The y position of the cursor.
     */
    private double yCursor;

// CONSTRUCTORS

    /**
     * Constructor of the controller of an editor.
     * @param editor the editor that will correspond to this controller.
     */
    public ADiagramEditorController(ADiagramEditor editor) {
        Contract.check(editor != null , 
                "L'argument editor ne doit pas être nul.");
        this.editor = editor;
        nodeGestures = new NodeGestures(editor.getCanvas());
        createComposant();
        createController();
    }

// REQUESTS
      
    @Override
    public ADiagramEditor getEditor() {
        return editor;
    }
    
    @Override
    public MenuItem getCreateNoteMI() {
        return createNoteMI;
    }
    
    @Override
    public MenuItem getZoomInMI() {
        return zoomInMI;
    }
    
    @Override
    public MenuItem getZoomOutMI() {
        return zoomOutMI;
    }
    
    @Override
    public ISelectionableEntityGraphic getSelectedEntity() {
        return selectedEntity;
    }
    
    @Override
    public double getxCursor() {
        return xCursor;
    }

    @Override
    public double getyCursor() {
        return yCursor;
    }
    
// COMMANDS 

    @Override
    public void setSelectedEntity(ISelectionableEntityGraphic selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    @Override
    public void deselectEntity() {
        if (selectedEntity != null) {
            selectedEntity.setSelected(false);
            selectedEntity = null;
        }
    }

    @Override
    public void setxCursor(double xCursor) {
        this.xCursor = xCursor;
    }

    @Override
    public void setyCursor(double yCursor) {
        this.yCursor = yCursor;
    }
    
// CONTROLLERS
    
    /**
     * To instancie ContextMenu and MenuItems
     */
    private void createComposant() {
        zoomInMI = new MenuItem("Zoom avant");
        zoomOutMI = new MenuItem("Zoom arrière");
        createNoteMI = new MenuItem("Créer une note");
    }
    
    /**
     * Add controllers on DiagramEditor
     */
    private void createController() {
        zoomInMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                editor.getCanvas().setScale(editor.getCanvas().getScale() * 2.);
            }
        });
        
        zoomOutMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                editor.getCanvas().setScale(editor.getCanvas().getScale() / 2.);
            }
        });
        
        // Add action on createNoteItem 
        createNoteMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createNote(xCursor, yCursor);
            }
        });
        
        editor.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                
            }
        });
    }
}
