package fr.univrouen.umlreverse.ui.component.sequence.elements;

import java.util.Map;
import java.util.Set;
import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.sequence.IActivity;
import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityTextGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.EnlargePoint;
import fr.univrouen.umlreverse.ui.component.sequence.relations.RelationToObjectGraphic;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ActivityGraphic extends AEntityTextGraphic implements IActivityGraphic {

	// ATTRIBUTS
	private final ActivityGraphicController controller;
	private final Rectangle rect;

	//private IObjectGraphic src;
	//private IObjectGraphic dst;

	/**
	 * this circle allows to enlarge manually the graphic package
	 * 	posX == rect.getTransaleX + rect.getWidth
	 * 	posY == rect.getTransaleY + rect.getHeight
	 * */
	private final EnlargePoint enlargePoint;

	// CONSTRUCTOR
	public ActivityGraphic(ISequenceController diagram, IActivity activity) {
		super(diagram);

		rect = new Rectangle();
		rect.setStroke(Color.web(IDiagramEditorController.COLOR_STROK_SHAPE));

		Pane p = new Pane();
		p.getChildren().add(rect);
		setCenter(p);

		controller = new ActivityGraphicController(this, diagram, activity);

		// Must be done after controller is initialize
		enlargePoint = diagram.bindToEnlargeActivityPoint(this);

		positionProperty().addListener(new ChangeListener<Point2D>() {
			@Override
			public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
				getController().getDiagramController().getObjects().values().forEach(obj -> obj.getController().refresh());

				(getController().getModel()).getRelation().forEach(rel -> {
					RelationToObjectGraphic relG = getController().getDiagramController().getRelationsToObject().get(rel);
					relG.getController().setTranslatePosition(new Point2D(relG.getTranslateX(), newValue.getY() +
							relG.getTranslateY() - oldValue.getY()));
				});
				(getController().getModel()).getActivity().forEach(act -> {

					double valueX = getController().getDiagramController().getActivities().get(act).getTranslateX();
					double valueY = newValue.getY() - oldValue.getY()
							+ getController().getDiagramController().getActivities().get(act).getTranslateY();
					getController().getDiagramController().getActivities().get(act).getController().setTranslatePosition(new Point2D(valueX, valueY));
				});
				/*
				Set<IRelation> relationSrc = src.getModel().getRelations();
				Set<IRelation> relationDst = dst.getModel().getRelations();
				for (IRelation relSrc : relationSrc) {
					for (IRelation relDst : relationDst) {
						if (relSrc.equals(relDst)) {
							(getController().getModel()).getActivity().forEach(act -> {
								double valueX = getController().getDiagramController().getActivities().get(dst).getTranslateX();
								double valueY = newValue.getY() + getController().getDiagramController().getActivities().get(dst).getTranslateY() - oldValue.getY();
								getController().getDiagramController().getActivities().get(dst).getController().setTranslatePosition(new Point2D(valueX, valueY));
							});
						}
					}
				}
				*/

				Set<IRelation> setRelationAct = activity.getRelation();
				Map<IObject, ObjectGraphic> mapObj = getController().getDiagramController().getObjects();

				for (IObject objD : mapObj.keySet()) {
					if (!objD.equals(activity.getObj())) {
						Set<IRelation> setRelationObj = objD.getRelations();
						for (IRelation relAct : setRelationAct) {
							for (IRelation relObj : setRelationObj) {
								if (relObj.equals(relAct)) {
									activity.addMapRelation(objD, relAct);
								}
							}
						}
					}
				}
			}
        });
        autosize();
	}

	// REQUESTS
	@Override
	public IActivityGraphicController getController() {
		return controller;
	}

	@Override
	public Rectangle getRectangle() {
		return rect;
	}

	@Override
	public int getRectActWidth() {
		return WIDTH;
	}

	@Override
	public int getRectActHeight() {
		return HEIGHT;
	}

	@Override
	public EnlargePoint getEnlargePoint() {
		return enlargePoint;
	}

	@Override
	public IEntityRelation getModel() {
		// TODO Auto-generated method stub
		return null;
	}
}
