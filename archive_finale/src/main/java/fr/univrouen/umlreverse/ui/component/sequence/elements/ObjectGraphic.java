package fr.univrouen.umlreverse.ui.component.sequence.elements;


import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityTextGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphicController;
import fr.univrouen.umlreverse.ui.component.sequence.relations.RelationToObjectGraphic;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ObjectGraphic extends AEntityTextGraphic implements IObjectGraphic {
	
	// ATTRIBUTES
	private final Rectangle rect;
	private final Line lifeLine;
    private final ObjectGraphicController controller;

	public ObjectGraphic(ISequenceController diagramEditorController, IObject object) {
		super(diagramEditorController);
		Contract.check(diagramEditorController != null, "L'argument diagramController "
                + "ne doit pas être nul.");
		Text text = getText();
		text.setTextOrigin(VPos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);
		rect = new Rectangle();
		rect.setStroke(Color.web(IDiagramEditorController.COLOR_STROK_SHAPE));
		lifeLine = new Line();

        lifeLine.getStrokeDashArray().add(5d);
		Pane p = new Pane();
        p.getChildren().add(rect);
        p.getChildren().add(text);
        p.getChildren().add(lifeLine);
        setCenter(p);
        controller = new ObjectGraphicController(this, diagramEditorController, object);
        autosize();
	}

	
	@Override
	public IEntityRelation getModel() {
		return controller.getModel();
	}

	@Override
	public Point2D lifeLineStartPosition() {
		return new Point2D(getTranslateX() + getLifeLine().getStartX()
				, getTranslateX() + getLifeLine().getStartY());
	}
	
	@Override
	public IObjectGraphicController getController() {
		return controller;
	}

	@Override
	public Rectangle getRectangle() {
		return rect;

	}


	public Line getLifeLine() {
		return lifeLine;
	}
	
	

	/**
	 * Return center point of the object graphic without considering the lifeLine
	 * */
	@Override
    public Point2D getCenterPoint() {
        double x = getRectangle().getLayoutX() + getTranslateX();
        double y = getRectangle().getLayoutY() + getTranslateY();
        return new Point2D(x + getRectangle().getWidth() / 2,
        		y + getRectangle().getHeight() / 2);
    }
	
	/**
	 * Return the height of the object graphic without considering the lifeLine
	 * */
	@Override
    public double getMainHeight() {
    	return getRectangle().getHeight();
    }
    
	/**
	 * Return the width of the point the object graphic without considering the lifeLine
	 * */
    @Override
    public double getMainWidth() {
    	return getRectangle().getWidth();
    }



	
}
