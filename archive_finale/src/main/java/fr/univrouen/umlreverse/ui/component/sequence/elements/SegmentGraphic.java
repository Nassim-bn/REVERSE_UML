package fr.univrouen.umlreverse.ui.component.sequence.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.univrouen.umlreverse.model.diagram.sequence.Activity;
import fr.univrouen.umlreverse.model.diagram.sequence.IActivity;
import fr.univrouen.umlreverse.model.diagram.sequence.IRelationToObject;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegment;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegmentCommon;
import fr.univrouen.umlreverse.model.diagram.sequence.Segment.SegmentType;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityTextGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphicController;
import fr.univrouen.umlreverse.ui.component.sequence.relations.RelationToObjectGraphic;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class SegmentGraphic extends AEntityTextGraphic {

	// ATTRIBUTS
	private SegmentGraphicController controller;
	private final Rectangle rect;
	private final Rectangle rectTitle;
	private final Text sequenceName;
	private final Pane p;
	private final Map<Line, ISegment> elses = new HashMap<>();

	// CONSTRUCTEUR
	public SegmentGraphic(ISequenceController diagram, ISegmentCommon segments2) {
		super(diagram);

		Text text = getText();
		text.setTextOrigin(VPos.BOTTOM);
		text.setTextAlignment(TextAlignment.LEFT);

		sequenceName = new Text();
		sequenceName.setTextOrigin(VPos.BOTTOM);
		sequenceName.setTextAlignment(TextAlignment.LEFT);
		rectTitle = new Rectangle();
		rect = new Rectangle();

		rectTitle.setStroke(Color.web(IDiagramEditorController.COLOR_STROK_SHAPE));
		rect.setStroke(Color.web(IDiagramEditorController.COLOR_STROK_SHAPE));
		p = new Pane();
		p.getChildren().add(rect);
		p.getChildren().add(sequenceName);
        p.getChildren().add(rectTitle);
        p.getChildren().add(text);
        p.toBack();
        setCenter(p);
		controller = new SegmentGraphicController(this, diagram, segments2);
		positionProperty().addListener(new ChangeListener<Point2D>() {

			@Override
			public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
				getController().getDiagramController().getObjects().values().forEach(obj -> obj.getController().refresh());
				
				if (!getController().getModel().getSegmentType().equals(SegmentType.REF)) {
					
					// Move each relations contained in this segment when this segment move
					((ISegment) getController().getModel()).getRelations().forEach(rel -> {
						RelationToObjectGraphic relG = getController().getDiagramController().getRelationsToObject().get(rel);
						relG.getController().setTranslatePosition(new Point2D(relG.getTranslateX(), newValue.getY() +
								relG.getTranslateY() - oldValue.getY()));
						
					});
					
					
					((ISegment) getController().getModel()).getSegments().forEach(seg -> {
						double valueX = getController().getDiagramController().getSegments().get(seg).getTranslateX();
						double valueY = newValue.getY() + getController().getDiagramController().getSegments().get(seg).getTranslateY() - oldValue.getY();
						getController().getDiagramController().getSegments().get(seg).getController().setTranslatePosition(new Point2D(valueX, valueY));
					});
				}

			}

        });
        autosize();
	}

	public Pane getPane() {
		return p;
	}

	@Override
	public ISegmentGraphicController getController() {
		return controller;
	}

	public Rectangle getRectangleTitle() {
		return rectTitle;
	}

	public Rectangle getRectangle() {
		return rect;
	}

	public Text getSequenceName() {
		return sequenceName;
	}

	public Map<Line, ISegment> getElses() {
		return elses;
	}
}
