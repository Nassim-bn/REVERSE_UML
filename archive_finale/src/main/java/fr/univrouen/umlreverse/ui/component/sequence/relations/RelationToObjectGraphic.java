/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.sequence.relations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.sequence.IActivity;
import fr.univrouen.umlreverse.model.diagram.sequence.IRelationToObject;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegment;
import fr.univrouen.umlreverse.model.diagram.sequence.Segment.SegmentType;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityRelationGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.type.Asynchrone;
import fr.univrouen.umlreverse.ui.component.common.relation.type.Owner;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationType;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.ui.component.sequence.elements.ActivityGraphic;
import fr.univrouen.umlreverse.ui.component.sequence.elements.IObjectGraphic;
import fr.univrouen.umlreverse.ui.component.sequence.elements.SegmentGraphic;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class RelationToObjectGraphic extends AEntityGraphic implements IEntityRelationGraphic, IRelationGraphic {

	public final static double OWNER_WIDTH_ = 40;
	// ATTRIBUTES
    /** The controller associated with this component. */
    private final RelationToObjectGraphicController controller;
	private RelationType arrow;
	/**
	 * Graphic Object containing the text on the relation
	 * */
    protected final Text nameText;

	private IObjectGraphic src;
	private IObjectGraphic dst;


	// CONSTRUCTORS
     public RelationToObjectGraphic(ISequenceController diagramEditorController,
            IRelationToObject relation,
            IObjectGraphic src, IObjectGraphic dst) {
        super(diagramEditorController);
        Contract.check(src != null, "src must not be null.");
        Contract.check(relation != null, "viewRelation must not be null.");
        Contract.check(dst != null, "dst must not be null.");
        Contract.check(diagramEditorController != null, "L'argument diagramController "
                + "ne doit pas être nul.");

        this.src = src;
        this.dst = dst;

        double width = 40;
        // In case where src == dst, the width of the relation is constant 
        if (getEntitySrc() != getEntityDst()) {
        	width = Math.abs(getEntitySrc().lifeLineStartPosition().getX()
            		- getEntityDst().lifeLineStartPosition().getX());
        }
        
        if (src == dst) {
        	arrow = new Owner(width);
        } else {
        	arrow = new Asynchrone(width);
        }
        setCenter(arrow);


        nameText = new Text();
        nameText.setText("");
        nameText.setTextOrigin(VPos.CENTER);
        nameText.setTextAlignment(TextAlignment.CENTER);
		nameText.setWrappingWidth(width);


        controller = new RelationToObjectGraphicController(this, diagramEditorController, relation);

        
        if (relation.getStyle().getValue(IDiagramEditorController.POSITION_STYLE_ID) != null) { 
	        //initialisation Position Y
	        double yPos = diagramEditorController.getNextYRelationToObjectPosition();
	        double xPos = 0;
	        //change la direction de la fleche
	        //initialisation position X, if src.getX == dst.getX no rotate
	        if (getEntitySrc().getCenterPoint().getX() > getEntityDst().getCenterPoint().getX()) {
	        	arrow.setRotate(180);
	            xPos = getEntityDst().lifeLineStartPosition().getX();
	        } else {
	            xPos = getEntitySrc().lifeLineStartPosition().getX();
	        }
	        
	        relation.addStyle(IDiagramEditorController.POSITION_STYLE_ID,
	                0 + "|" + 0 + "|"
	                + xPos + "|" + yPos);
	        positionProperty().set(new Point2D(xPos, yPos));
	        
        }

        /*When relation is moved, this listener insert if needed the relation in a segment
         * or put the relation out a the segment*/
        positionProperty().addListener(new ChangeListener<Point2D>() {

			@Override
			public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
				getController().getDiagramController().getObjects().values().forEach(obj -> obj.getController().refresh());

				// calculate the center the arrow's relation
				Point2D newValueCenter = new Point2D(newValue.getX(),
						newValue.getY() + getHeight() / 2
							+ (nameText.getText() != "" ? nameText.getLayoutY() / 2 :0));

				final Map<ISegment, SegmentGraphic> segAcc = new HashMap<>();
				getController().getDiagramController().getSegments().forEach((seg, segG) -> {
					if (newValueCenter.getY() > segG.getTranslateY() && newValueCenter.getY() < segG.getRectangle().getHeight() +
							segG.getTranslateY() && !seg.getSegmentType().equals(SegmentType.REF)) {
						segAcc.put((ISegment) seg, segG);
					}
				});
				ISegment result = null;
				double startYMax = 0;
				if (segAcc.size() != 0) {
					startYMax = segAcc.values().stream().findAny().get().getTranslateY();
				}
				for (Entry<ISegment, SegmentGraphic> entry : segAcc.entrySet()) {
					if (entry.getValue().getTranslateY() >= startYMax) {
						startYMax = entry.getValue().getTranslateY();
						result = entry.getKey();
					}
				}
				
				if (getController().getModel().getSegmentContainer() != null
						&& !(getController().getModel().getSegmentContainer().equals(result))) {
					getController().getModel().getSegmentContainer().removeRelation(getController().getModel());
				}
				
				if (result != null && (getController().getModel().getSegmentContainer() == null ||
						!(getController().getModel().getSegmentContainer().equals(result)))) {
					if (result.getSegmentType().equals(SegmentType.ALT)) {
						if (getController().getDiagramController().getSegments().get(result)
								.getElses().keySet().stream().filter(line -> (line.getStartY() <= newValueCenter.getY()))
	                        	.findFirst().isPresent()) {
							//((ISegmentWithElse) result).;
						} else {
							result.addRelation(getController().getModel());
						}
					} else {
						result.addRelation(getController().getModel());
					}
				}
				getController().getModel().setSegmentContainer(result);


				final Map<IActivity, ActivityGraphic> actAccSrc = new HashMap<>();
				final Map<IActivity, ActivityGraphic> actAccDst = new HashMap<>();
				getController().getDiagramController().getActivities().forEach((act, actG) -> {
					if (newValueCenter.getY() > actG.getTranslateY() && newValueCenter.getY() < actG.getRectangle().getHeight() +
							actG.getTranslateY()) {
						if (act.getObj().equals(src.getModel())) {
							actAccSrc.put((IActivity) act, actG);
						} else if (act.getObj().equals(dst.getModel())) {
							actAccDst.put((IActivity) act, actG);
						}
					}
				});

				IActivity res = null;
				double startYMaxi = 0;

				if (actAccSrc.size() != 0) {
					startYMaxi = actAccSrc.values().stream().findAny().get().getTranslateY();
				}
				for (Entry<IActivity, ActivityGraphic> entry : actAccSrc.entrySet()) {
					if (entry.getValue().getTranslateY() >= startYMaxi) {
						startYMaxi = entry.getValue().getTranslateY();
						res = entry.getKey();
					}
				}
				if (getController().getModel().getActivityContainer() != null
						&& !(getController().getModel().getActivityContainer().equals(res))) {
					getController().getModel().getActivityContainer().removeRelation(getController().getModel());
				}
				if (res != null && (getController().getModel().getActivityContainer() == null ||
						!(getController().getModel().getActivityContainer().equals(res)))) {
					res.addRelation(getController().getModel());
				}
				getController().getModel().setActivityContainer(res);

				IActivity resDst = null;
				startYMaxi = 0;

				if (actAccDst.size() != 0) {
					startYMaxi = actAccDst.values().stream().findAny().get().getTranslateY();
				}
				for (Entry<IActivity, ActivityGraphic> entry : actAccDst.entrySet()) {
					if (entry.getValue().getTranslateY() >= startYMaxi) {
						startYMaxi = entry.getValue().getTranslateY();
						resDst = entry.getKey();
					}
				}
				if (getController().getModel().getActivityDstContainer() != null
						&& !(getController().getModel().getActivityDstContainer().equals(resDst))) {
					getController().getModel().getActivityDstContainer().removeRelation(getController().getModel());
				}
				if (resDst != null && (getController().getModel().getActivityDstContainer() == null ||
						!(getController().getModel().getActivityDstContainer().equals(resDst)))) {
					resDst.addRelation(getController().getModel());
				}
				getController().getModel().setActivityDstContainer(resDst);
			}

        });


        autosize();

        //setSrcDstEventHandler();

    }

 	public RelationType getRelationType() {
 		return arrow;
 	}

 	public void RefreshRelationDirection() {
 		 if (getEntitySrc().getCenterPoint().getX() > getEntityDst().getCenterPoint().getX()) {
 			arrow.setRotate(180);
 		 } else {
 			 arrow.setRotate(0);
         }
 	}

 	/**
 	 * remove previous relation and add to graphic the new relation component
 	 * */
 	public void setRelationType(RelationType relation) {
 		getChildren().remove(arrow);
 		arrow = relation;
 		setCenter(arrow);
 	}

	public IObjectGraphic getEntitySrc() {
		return src;
	}

	public double getWidthRelation() {

		Point2D srcPoint = getEntitySrc().lifeLineStartPosition();
		Point2D dstPoint = getEntityDst().lifeLineStartPosition();
		double width = Math.abs(srcPoint.getX()
				- dstPoint.getX());
		return width;
	}
	public IObjectGraphic getEntityDst() {
		return dst;
	}

	public void clear() {
	}


	@Override
	public IEntityRelation getModel() {
		return controller.getModel();
	}

	public IRelation getRelationModel() {
		return controller.getModel();
	}

	@Override
	public RelationToObjectGraphicController getController() {
		return controller;
	}


	@Override
	@Deprecated
	public List<Circle> getArrowBodyCircles() {
		return null;
	}


	@Override
	@Deprecated
	public String getCardinalityEnd() {
		return null;
	}


	@Override
	@Deprecated
	public Text getCardinalityEndText() {
		return null;
	}


	@Override
	@Deprecated
	public String getCardinalityStart() {
		return null;
	}


	@Override
	@Deprecated
	public Text getCardinalityStartText() {
		return null;
	}


	@Override
	@Deprecated
	public Circle getCircleDst() {
		return null;
	}


	@Override
	@Deprecated
	public Circle getCircleSrc() {
		return null;
	}


	@Override
	public Color getColorRelation() {
		return null;
	}


	@Override
	public Color getColorText() {
		return null;
	}


	@Override
	@Deprecated
	public Line getFirstLine() {
		return null;
	}


	@Override
	@Deprecated
	public List<Line> getHeadLines() {
		return null;
	}


	@Override
	public String getName() {
		return controller.getName();
	}


	@Override
	public Text getNameText() {
        return nameText;
	}


	@Override
	@Deprecated
	public Set<Shape> getShapes() {
		return null;
	}


	@Override
	@Deprecated
	public List<Line> getTailLines() {
		return null;
	}


	@Override
	public RelationTypeEnum getType() {
		return getRelationModel().getType();
	}


	@Override
	@Deprecated
	public void calculPointRelation() {

	}


	@Override
	@Deprecated
	public void drawInGroup(Group g) {

	}


	@Override
	@Deprecated
	public void moveArrowBodyPoint(Circle c, double x, double y) {

	}


	@Override
	@Deprecated
	public void moveArrowHead(double x, double y) {

	}


	@Override
	@Deprecated
	public void moveArrowTail(double x, double y) {

	}


	@Override
	@Deprecated
	public void nameTextAutoSize() {

	}


	@Override
	@Deprecated
	public void setCardinalityEnd(String cardinalityEnd) {

	}


	@Override
	@Deprecated
	public void setCardinalityStart(String cardinalityStart) {

	}


	@Override
	public void setColorRelation(Color colorRelation) {
		// TODO Auto-generated method stub

	}


	@Override
	public void setColorShapes(Color c) {

	}


	@Override
	public void setColorText(Color colorText) {
		// TODO Auto-generated method stub

	}


	@Override
	@Deprecated
	public void setContextMenuEventOnArrow(EventType<ContextMenuEvent> eventType,
			EventHandler<? super ContextMenuEvent> eventHandler) {

	}


	@Override
	@Deprecated
	public void setDNDPointEvent(EventHandler<MouseEvent> pointDnd_Event) {

	}


	@Override
	public void setName(String name) {
		controller.setName(name);
	}


	@Override
	@Deprecated
	public void setMouseEventOnArrow(EventType<MouseEvent> eventType, EventHandler<? super MouseEvent> eventHandler) {

	}


	@Override
	@Deprecated
	public void setType(RelationTypeEnum type) {
		// TODO Auto-generated method stub

	}


	@Override
	@Deprecated
	public void setTypeArrowHead(TypeHeadArrow head) {
		// TODO Auto-generated method stub
	}


	@Override
	@Deprecated
	public void setTypeArrowTail(TypeHeadArrow tail) {
		// TODO Auto-generated method stub

	}


	@Override
	@Deprecated
	public void setTypeLineArrow(TypeLineArrow line) {
		// TODO Auto-generated method stub

	}


	public void setText(String newName) {
		nameText.setText(newName);
		if (newName == "") {
			getChildren().remove(nameText);
		} else {
			setTop(nameText);
		}
		
		if (getEntityDst() ==  getEntitySrc()) {
			double width = Math.max(newName.length() * 8, OWNER_WIDTH_); 
			nameText.setWrappingWidth(width);
			arrow.draw(width);
		}
	}
	
	
}
