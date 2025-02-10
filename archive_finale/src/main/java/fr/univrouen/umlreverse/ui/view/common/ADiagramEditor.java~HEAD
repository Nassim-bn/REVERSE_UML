package fr.univrouen.umlreverse.ui.view.common;

import fr.univrouen.umlreverse.ui.view.util.PannableCanvas;
import fr.univrouen.umlreverse.ui.view.util.SceneGestures;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public abstract class ADiagramEditor extends Pane implements IDiagramEditor {
// ATTRIBUTES
    /**
     * The pannable canvas of the diagram.
     */
	private final PannableCanvas canvas;
  
// CONSTRUCTORS

    /**
     * Constructor of the diagram editor.
     */
    public ADiagramEditor() {
        super();
        canvas = new PannableCanvas();
        SceneGestures sg = new SceneGestures(canvas);
        addEventHandler(MouseEvent.MOUSE_PRESSED, sg.getOnMousePressedEventHandler());
        addEventHandler(MouseEvent.MOUSE_DRAGGED, sg.getOnMouseDraggedEventHandler());
        addEventHandler(ScrollEvent.ANY, sg.getOnScrollEventHandler());
        getChildren().add(new Pane(canvas));
        setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        Rectangle clipRectangle = new Rectangle();
        setClip(clipRectangle);
        layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
                clipRectangle.setWidth(newValue.getWidth());
                clipRectangle.setHeight(newValue.getHeight());
            }
        });  

    }
    
// REQUESTS 
    @Override
    public abstract IDiagramEditorController getController();
 
    @Override
    public PannableCanvas getCanvas() {
    	return canvas;
    }
    
// COMMANDES
    @Override
    public void createNote(double x, double y) {
        getController().createNote(x, y);
    }
}
