package fr.univrouen.umlreverse.ui.view.util;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityGraphic;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Listeners for making the nodes draggable via left mouse button. 
 * Considers if parent is zoomed.
 */
public class NodeGestures extends Gestures {
// ATTRIBUTS

    /**
     * Drag and drop feature.
     * Set the mice pressed controller .
     */
    private final EventHandler<MouseEvent> onMousePressedEventHandler = 
            new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (!event.isPrimaryButtonDown()) {
                return;
            }
            Node node = (Node) event.getSource();
            runningDrag = new DragContext(
            		event.getSceneX(),
            		event.getSceneY(),
            		node.getTranslateX(),
            		node.getTranslateY());
        }
    };

    /**
     * drag and drop feature.
     * Set the dragged mice controller.
     */
    private final EventHandler<MouseEvent> onMouseDraggedEventHandler = 
            new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (!event.isPrimaryButtonDown()) {
                return;
            }
            double scale = canvas.getScale();
            AEntityGraphic entity = ((AEntityGraphic) event.getSource());
            double x = runningDrag.getTranslateX() + (event.getSceneX() 
                            - runningDrag.getMouseX()) / scale;
            double y = runningDrag.getTranslateY() + (event.getSceneY() 
                            - runningDrag.getMouseY()) / scale;
            
            entity.setTranslatePosition(new Point2D(x, y));
            event.consume();
        }
    };
    
// CONSTRUCTORS

    /**
     * Constructor of the node gesture.
     * @param c the canvas that will had this gesture
     */
    public NodeGestures(PannableCanvas c) {
        canvas = c;
    }

// REQUESTS

    /**
     * getter for pressed mice handler.
     * @return pressed mice handler
     */
    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    /**
     * getter for dragged mice handler.
     * @return dragged mice handler
     */
    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }
    
// COMMANDS
   
}