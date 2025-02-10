package fr.univrouen.umlreverse.ui.component.clazz.elements;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import javafx.scene.paint.Color;

/**
 * A component representing an graphic object entity : class, interface, enum 
 * etc...
 */
public interface IObjectEntityGraphic {
	// CONSTANTS
    int X_MARGIN = 20;
    int Y_MARGIN = 15;
    int MIN_RECT_HEIGHT = 15;
    int CIRCLE_MARGIN = 5;
    int DEFAULT_TEXT_SIZE = 12;
    double CIRCLE_SIZE = 15;
    
    // QUERIES
    /**
     * Returns the text color.
     * @return the text color.
     */
    Color getTextColor();

    /**
     * Returns the text size.
     * @return the text size.
     */
    int getTextSize();
    
    /**
     * @return the model of entity.
     */
    IViewEntity getViewEntity();

    // COMMANDS
    /**
     * Refreshes the entity.
     */
    void refresh();
}
