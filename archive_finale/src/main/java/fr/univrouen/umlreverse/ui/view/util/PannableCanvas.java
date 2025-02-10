package fr.univrouen.umlreverse.ui.view.util;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;

/**
 * A group that supports clamped zooming and panning gestures.
 */
public class PannableCanvas extends Group {
	/**
	 * The maximum scale factor reachable by a PannableCanvas.
	 */
    public static final double MAX_SCALE = 4.d;
    /**
     * The minimum scale factor reachable by a PannableCanvas.
     */
    public static final double MIN_SCALE = .1d;

    /**
     * This canvas’s current scale.
     */
    private DoubleProperty scale;

    /**
     * Create a new PannableCanvas with a default scale factor of 1.
     */
    public PannableCanvas() {
    	scale = new SimpleDoubleProperty(1.0);
    	
        scaleXProperty().bind(scale);
        scaleYProperty().bind(scale);
        
        /*
         * The Group component can resize itself if its children move.
         * The listener below accounts for this change.
         */
        boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				setTranslateX(getTranslateX() + (newValue.getMinX() - oldValue.getMinX() +
						newValue.getMaxX() - oldValue.getMaxX()) / 2 * (getScale() - 1));
				setTranslateY(getTranslateY() + (newValue.getMinY() - oldValue.getMinY() +
						newValue.getMaxY() - oldValue.getMaxY()) / 2 * (getScale() - 1));
			}
        });
    }

    /**
     * Get the scale.
     * @return the canvas’s current scale 
     */
    public double getScale() {
        return scale.get();
    }

    /**
     * Set the scale (clamped between MIN_SCALE and MAX_SCALE).
     * @param s the canvas’s new scale
     */
    public void setScale(double s) {
        scale.set(clamp(s, MIN_SCALE, MAX_SCALE));
    }

    /**
     * Move the scale transformation’s fixed point.
     * @param x the value to be removed from the pivot’s X coordinate
     * @param y the value to be removed from the pivot’s Y coordinate
     */
    public void setPivot(double x, double y) {
        setTranslateX(getTranslateX() - x);
        setTranslateY(getTranslateY() - y);
    }

    /**
     * Clamp a `value` to an interval [`min`, `max`].
     * @param value the value to clamp
     * @param min the lower bound of the interval
     * @param max the upper bound or the interval
     * @return the number closest to `value` within the [`min`, `max`] interval
     */
    private static double clamp(double value, double min, double max) {
    	return Math.min(Math.max(value, min), max);
    }
}