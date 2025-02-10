package fr.univrouen.umlreverse.ui.component.packag.elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewPackage;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewPackage;
import fr.univrouen.umlreverse.ui.component.clazz.elements.IObjectEntityGraphic;
import fr.univrouen.umlreverse.ui.component.clazz.elements.ObjectEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityTextGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.EnlargePoint;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.packag.PackageController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class PackageGraphic extends AEntityTextGraphic implements IPackageGraphic, IObjectEntityGraphic {

	// ATTRIBUTS
	private PackageGraphicController controller;
	private final Rectangle rect;
	private final Text textParameter;
	private final Rectangle rectTitle;
	private final Rectangle rectParameter;

	/**
	 * this circle allows to enlarge manualy the graphic package
	 * 	posX == rect.getTransaleX + rect.getWidth
	 * 	posY == rect.getTransaleY + rect.getHeight
	 * */
	private final EnlargePoint enlargePoint;

	// CONSTRUCTOR
	public PackageGraphic(PackageController diagram, IViewPackage pack) {
		super(diagram);

		Text text = getText();
		text.setTextOrigin(VPos.BOTTOM);
		text.setTextAlignment(TextAlignment.LEFT);

		textParameter = new Text();

		rectTitle = new Rectangle();
		rect = new Rectangle();

		rectTitle.setStroke(Color.web(IDiagramEditorController.COLOR_STROK_SHAPE));
		rect.setStroke(Color.web(IDiagramEditorController.COLOR_STROK_SHAPE));

		rectTitle.setTranslateY(POSITION_RECT_TITLE);
        text.setTranslateY(POSITION_TITLE);

		rectParameter = new Rectangle();
		rectParameter.setStroke(Color.web(IDiagramEditorController.COLOR_STROK_SHAPE));

        rectParameter.getStrokeDashArray().add(5d);
        rectParameter.getStrokeDashArray().add(5d);
        rectParameter.getStrokeDashArray().add(5d);
        rectParameter.getStrokeDashArray().add(5d);
        rectParameter.setStrokeLineCap(StrokeLineCap.SQUARE);


        Pane p2 = new Pane();
        p2.getChildren().add(rectParameter);
        textParameter.setTranslateY(POSITION_TEXT_PARAMETER);
        textParameter.setTranslateX(-POSITION_RECT_PARAMETER);

        p2.getChildren().add(textParameter);

        p2.setTranslateX(40);
		p2.setTranslateY(POSITION_RECT_PARAMETER);

		Pane p = new Pane();
		p.getChildren().add(rect);
        p.getChildren().add(rectTitle);
        p.getChildren().add(text);
        p.getChildren().add(p2);
        p.toBack();
        setCenter(p);

		controller = new PackageGraphicController(this, diagram, pack);

		//Must be done after controller is initialize
        enlargePoint = diagram.bindToEnlargePackagePoint(this);

		positionProperty().addListener(new ChangeListener<Point2D>() {
			@Override
			public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
				//move all entities which are in this package
				((IViewPackage) getController().getModel()).getEntities()
				.forEach(ent -> {
					// getPackages.get(ent) doesn't work
					// here we use filter by key to get the value
					if (ent.getClass().equals(ViewPackage.class)) {
						PackageGraphic packG = getController().getDiagramController().getPackages()
								.entrySet()
								.stream()
								.filter(map -> map.getKey().equals(ent))
								.findFirst()
								.get()
								.getValue();

						packG.getController()
							.setTranslatePosition(new Point2D(
								newValue.getX() + packG.getTranslateX() - oldValue.getX(),
								newValue.getY() + packG.getTranslateY() - oldValue.getY()));
					} else {
						ObjectEntityGraphic relG = getController().getDiagramController().getOEG(ent);
						relG.getController().setTranslatePosition(new Point2D(
								newValue.getX() +relG.getTranslateX() - oldValue.getX(),
								newValue.getY() + relG.getTranslateY() - oldValue.getY()));
					}

				});


				//add, if needed, this packages into an other package
				final Map<IViewPackage, PackageGraphic> packMap = new HashMap<>();

				Point2D topLeftObj = positionProperty().get();
				Point2D bottomRightObj = new Point2D(topLeftObj.getX() + getWidth(),
						topLeftObj.getY() + getHeight());

				controller.getDiagramController().getPackages().forEach((pack, packG) -> {
					Point2D topLeftPack = packG.positionProperty().get();
					Point2D bottomRightPack = new Point2D(topLeftPack.getX() + packG.getWidth(),
							topLeftPack.getY() + packG.getHeight());

					if (topLeftObj.getY() > topLeftPack.getY() && topLeftObj.getX() > topLeftPack.getX()
							&& bottomRightObj.getY() < bottomRightPack.getY()
							&& bottomRightObj.getX() < bottomRightPack.getX()) {
						packMap.put((IViewPackage) pack, packG);
					}
				});


				IViewPackage result = null;
				double startXMax = 0;
				if (packMap.size() != 0) {
					startXMax = packMap.values().stream().findAny().get().getTranslateX();
				}
				for (Entry<IViewPackage, PackageGraphic> entry : packMap.entrySet()) {
					if (entry.getValue().getTranslateX() >= startXMax) {
						startXMax = entry.getValue().getTranslateX();
						result = entry.getKey();
					}
				}
				if (controller.getViewEntity().getPackage() != null
						&& !(controller.getViewEntity().getPackage().equals(result))) {
					controller.getViewEntity().getPackage().removeEntity(controller.getViewEntity());
				}

				if (result != null && (controller.getViewEntity().getPackage() == null ||
						!(controller.getViewEntity().getPackage().equals(result)))) {
					result.addEntity(controller.getViewEntity());
				}
				controller.getViewEntity().setPackage(result);

				if (result != null) {
					toBack();
				}
			}
        });

        autosize();
	}

	// REQUESTS
	@Override
	public IPackageGraphicController getController() {
		return controller;
	}

	@Override
    public IViewPackage getViewPackage() {
        return controller.getModel();
    }

	@Override
	public EnlargePoint getEnlargePoint() {
		return enlargePoint;
	}

	@Override
	public Rectangle getRectangleTitle() {
		return rectTitle;
	}

	@Override
	public Rectangle getRectangle() {
		return rect;
	}

	@Override
	public Rectangle getRectangleParameter() {
		return rectParameter;
	}

	@Override
	public Text getTextParam() {
		return textParameter;
	}

	@Override
	public int getTextSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IViewEntity getViewEntity() {
		return (IViewEntity) controller.getModel();
	}

	// COMMANDS
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void toBack() {
		super.toBack();

		IViewPackage parent = getController().getModel().getPackage();

		if (parent != null) {
			getController().getDiagramController().getPackage(parent).toBack();
		}
	}
}
