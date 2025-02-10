package fr.univrouen.umlreverse.ui.view.util;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Listeners for making the scene's canvas draggable and zoomable
 */
public class SceneGestures extends Gestures {
    /**
     * The scale factor of this scene gesture.
     */
	private static final double SCALE_FACTOR = 1.25d;

    /**
     * Constructor of the scene gesture.
     * @param c the canvas that had this gesture
     */
    public SceneGestures(PannableCanvas c) {
        canvas = c;
    }

    /**
     * Getter of mice pressed event handler.
     * @return EventHandler<MouseEvent>
     */
    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    /**
     * Getter of mice dragged event handler.
     * @return EventHandler<MouseEvent>
     */
    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    /**
     * Getter of mice scroll event handler.
     * @return EventHandler<ScrollEvent>
     */
    public EventHandler<ScrollEvent> getOnScrollEventHandler() {
        return onScrollEventHandler;
    }

    /**
     * Attribute that contain the mice pressed event handler.
     */
    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        public void handle(MouseEvent event) {
            if (!event.isPrimaryButtonDown())
                return;

            runningDrag = new DragContext(
            		event.getSceneX(),
            		event.getSceneY(),
            		canvas.getTranslateX(),
            		canvas.getTranslateY());
        }
    };
    /**
     * Attribute that contain the dragged mice event handler.
     */
    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            if (!event.isPrimaryButtonDown())
                return;

            canvas.setTranslateX(runningDrag.getTranslateX() + event.getSceneX() - runningDrag.getMouseX());
            canvas.setTranslateY(runningDrag.getTranslateY() + event.getSceneY() - runningDrag.getMouseY());

            event.consume();
        }
    };

    /**
     * Mouse wheel handler: zoom to pivot point.
     */
    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

        @Override
        public void handle(ScrollEvent event) {
        	if (event.isControlDown()) {
        		// Ctrl + Scroll: zoom in/out on the pointer
	            double oldScale = canvas.getScale();
	
	            if (event.getDeltaY() < 0) {
	                canvas.setScale(oldScale / SCALE_FACTOR);
	            } else {
	                canvas.setScale(oldScale * SCALE_FACTOR);
	            }
	            
	            Bounds bip = canvas.getBoundsInParent();
	            double dx = event.getX() - bip.getWidth() / 2 - bip.getMinX();
	            double dy = event.getY() - bip.getHeight() / 2 - bip.getMinY();
	            double f = (canvas.getScale() / oldScale) - 1;
	            
	            canvas.setPivot(f * dx, f * dy);
        	} else {
        		// Scroll: move viewport vertically
        		// Shift + Scroll: move viewport horizontally
        		double mainAxis = event.isShiftDown() ? event.getDeltaX() : event.getDeltaY();
        		double secondAxis = event.isShiftDown() ? event.getDeltaY() : event.getDeltaX();
        		canvas.setTranslateX(canvas.getTranslateX() + secondAxis);
        		canvas.setTranslateY(canvas.getTranslateY() + mainAxis);
        	}
            event.consume();
        }

    };
}