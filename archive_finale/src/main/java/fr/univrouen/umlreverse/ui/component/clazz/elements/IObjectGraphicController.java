package fr.univrouen.umlreverse.ui.component.clazz.elements;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphicController;
import fr.univrouen.umlreverse.ui.view.clazz.ClassController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * A controller associated with an graphic object entity component.
 */
public interface IObjectGraphicController extends IEntityGraphicController {
	// QUERIES
    /**
     * Returns the ViewEntity associated to this OEG.
     * @return the ViewEntity associated to this OEG.
     */
    IViewEntity getViewEntity();

	ClassController getDiagrController();

    /**
     * Returns the text color.
     * @return the text color.
     */
    Color getTextColor();

    // COMMANDS
    @Override
    void setBackgroundColor(Color c);

    /**
     * Sets the text color.
     * @param c the color to apply.
     */
    void setTextColor(Color c);

    @Override
    void setPosition(Point2D value);

    @Override
    void setTranslatePosition(Point2D value);
}
