package fr.univrouen.umlreverse.ui.component.common.util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.VLineTo;

public class NoteShape extends Path {
	/**
	 * Maximum size of the fold in the upper right corner, in pixels.
	 */
	private static final double FOLD_MAX_SIZE = 15.;
	
	/**
	 * Size of the fold for smaller notes, in units of min(width, height).
	 */
	private static final double FOLD_PROPORTION = .25;
	
	private final DoubleProperty foldSize = new SimpleDoubleProperty();
	private final DoubleProperty width = new SimpleDoubleProperty();
	private final DoubleProperty height = new SimpleDoubleProperty();
	private final DoubleProperty x = new SimpleDoubleProperty();
	private final DoubleProperty y = new SimpleDoubleProperty();
	
	public NoteShape(double w, double h) {
		width.set(w);
		height.set(h);
		setFoldSize();
		setFillRule(FillRule.NON_ZERO);
		setStrokeLineJoin(StrokeLineJoin.ROUND);
		
		MoveTo moveTo = new MoveTo();
			moveTo.xProperty().bind(x.add(width));
			moveTo.yProperty().bind(y.add(foldSize));
		
		VLineTo rightSide = new VLineTo();
			rightSide.yProperty().bind(y.add(height));
		
		HLineTo bottomSide = new HLineTo();
			bottomSide.xProperty().bind(x);
		
		VLineTo leftSide = new VLineTo();
			leftSide.yProperty().bind(y);
		
		HLineTo topSide = new HLineTo();
			topSide.xProperty().bind(x.add(width).subtract(foldSize));
		
		LineTo lineTo = new LineTo();
			lineTo.xProperty().bind(x.add(width));
			lineTo.yProperty().bind(y.add(foldSize));
		
		HLineTo hFold = new HLineTo();
			hFold.xProperty().bind(x.add(width).subtract(foldSize));
		
		VLineTo vFold = new VLineTo();
			vFold.yProperty().bind(y);
		
		getElements().addAll(moveTo, rightSide, bottomSide, leftSide,
				topSide, lineTo, hFold, vFold, new ClosePath());
	}
	
	public void setHeight(double h) {
		height.set(h);
		setFoldSize();
	}
	
	public void setWidth(double w) {
		width.set(w);
		setFoldSize();
	}
	
	public void setX(double value) {
		x.set(value);
	}
	
	public void setY(double value) {
		y.set(value);
	}
	
	private void setFoldSize() {
		foldSize.set(Math.min(FOLD_MAX_SIZE,
				FOLD_PROPORTION * Math.min(height.get(), width.get())));
	}
}
