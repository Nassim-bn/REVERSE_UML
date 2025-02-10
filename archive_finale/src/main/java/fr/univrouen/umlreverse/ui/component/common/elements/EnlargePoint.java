package fr.univrouen.umlreverse.ui.component.common.elements;




import fr.univrouen.umlreverse.ui.component.packag.elements.PackageGraphic;
import fr.univrouen.umlreverse.ui.component.packag.elements.PackageGraphicController;
import fr.univrouen.umlreverse.ui.component.sequence.elements.ActivityGraphic;
import fr.univrouen.umlreverse.ui.component.sequence.elements.ActivityGraphicController;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;

public class EnlargePoint extends AEntityGraphic {

	/**
	 * The Object bind to this point, so when this point is moved the sive of the binded changed with the point
	 * */
	private IEntityGraphic bindObject;
	private Pane point;

	public static final double SIZE = 10;

	public static final double SIZE_INIT = 3 + SIZE;
	public EnlargePoint(IDiagramEditorController diagramEditorController, IEntityGraphic bindObject) {
		super(diagramEditorController);

		point = new Pane();
		/*                     SIZE,0
		 *                  /    |
		 *              /        |
		 *          /            |
		 *      0,SIZE--------SIZE,SIZE
		 * */
		Polyline triangle = new Polyline(0, SIZE, SIZE, SIZE, SIZE, 0,0,SIZE);
		for (int i = (int) SIZE-3; i > 2; i -=3) {
			Line l = new Line(i, SIZE - 2, SIZE-2, i);
			point.getChildren().add(l);
		}

		point.getChildren().add(triangle);
		setCenter(point);
		point.setVisible(false);

		this.bindObject = bindObject;
		createController();
		refreshPosition();
	}

	public void refreshPosition() {
		setTranslateX(bindObject.positionProperty().get().getX()+bindObject.getWidth() - SIZE_INIT);
		setTranslateY(bindObject.positionProperty().get().getY()+bindObject.getHeight() - SIZE_INIT);
	}

	private void createController() {
		bindObject.positionProperty().addListener(new ChangeListener<Point2D>() {

			@Override
			public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
				refreshPosition();
			}
		});


		EventHandler<Event> inEvent = new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				point.setVisible(true);
			}
		};

		EventHandler<Event> outEvent = new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				point.setVisible(false);
			}
		};

		addEventFilter(MouseEvent.MOUSE_ENTERED, inEvent);
		addEventFilter(MouseEvent.MOUSE_EXITED, outEvent);



		if (bindObject instanceof Node) {
			((Node) bindObject).addEventFilter(MouseEvent.MOUSE_ENTERED, inEvent);

			((Node) bindObject).addEventFilter(MouseEvent.MOUSE_EXITED, outEvent);
		}

        setCursor(Cursor.SE_RESIZE);
	}

	public void setTranslatePosition(Point2D value) {
		if (value.getX() > bindObject.positionProperty().get().getX()
				&& value.getY() > bindObject.positionProperty().get().getY()) {
			setTranslateX(value.getX());
			setTranslateY(value.getY());
			double width = value.getX() + SIZE_INIT-1 - bindObject.positionProperty().get().getX();
			double height = value.getY() + SIZE_INIT-1 - bindObject.positionProperty().get().getY();
			if (bindObject instanceof PackageGraphic) {
				((PackageGraphicController) bindObject.getController()).getModel()
					.addStyle(IDiagramEditorController.SIZE_STYLE_ID, width + "|" + height);
			} else if (bindObject instanceof ActivityGraphic) {
				((ActivityGraphicController) bindObject.getController()).getModel()
				.addStyle(IDiagramEditorController.SIZE_STYLE_ID, "15|" + height);
			}
		}
	}

	@Override
	public IEntityGraphicController getController() {
		return null;
	}

}
