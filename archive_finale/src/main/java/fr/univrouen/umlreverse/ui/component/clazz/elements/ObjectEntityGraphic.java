package fr.univrouen.umlreverse.ui.component.clazz.elements;

import fr.univrouen.umlreverse.model.project.IField;
import fr.univrouen.umlreverse.model.project.Modifier;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewPackage;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityGraphic;
import fr.univrouen.umlreverse.ui.component.packag.elements.PackageGraphic;
import fr.univrouen.umlreverse.ui.view.clazz.ClassController;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.packag.PackageController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Used to draw an ObjectEntity : class, interface, enum etc...
 */
public class ObjectEntityGraphic extends AEntityGraphic implements IObjectEntityGraphic {
	/** The stroke color used for entities. */
    private static final Color strokeColor =
    		Color.web(IDiagramEditorController.COLOR_STROK_SHAPE);

	// ATTRIBUTES
	/** The controller associated with this component. */
    private final ObjectEntityGraphicController controller;
    /** The header of this component. */
    private Rectangle head;
    /** The name of this component. */
    private final Text name;
    /** The with of this component. */
    private double width;
    /** The body of this component. */
    private final Pane pane;

    // CONSTRUCTORS
    /**
     * The view representing an ObjectEntity.
     * @param diagramEditorController the current diagram controller.
     * @param entity the ViewEntity to represent.
     */
    public ObjectEntityGraphic(ClassController diagramEditorController, IViewEntity entity) {
        super(diagramEditorController);
        pane = new BorderPane();
        name = new Text();
        setCenter(pane);
        this.controller = new ObjectEntityGraphicController(this, diagramEditorController, entity);
        refresh();

        if (controller.getDiagrController() instanceof PackageController) {
        	PackageController packageController = (PackageController) controller.getDiagrController();

	        positionProperty().addListener(new ChangeListener<Point2D>() {
				@Override
				public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
					final Map<IViewPackage, PackageGraphic> packMap = new HashMap<>();

					Point2D topLeftObj = positionProperty().get();
					Point2D bottomRightObj = new Point2D(topLeftObj.getX() + getWidth(),
							topLeftObj.getY() + getHeight());

					packageController.getPackages().forEach((pack, packG) -> {
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
				}
			});
        }

    }

    // QUERIES
    @Override
    public ObjectEntityGraphicController getController() {
        return controller;
    }

    @Override
    public String toString() {
        return name.getText();
    }

    @Override
    public Color getTextColor() {
        return controller.getTextColor();
    }

    @Override
    public int getTextSize() {
        return DEFAULT_TEXT_SIZE;
    }

    @Override
    public IViewEntity getViewEntity() {
        return controller.getViewEntity();
    }

    // COMMANDS
    @Override
    public final void refresh() {
        // Remove everything
        pane.getChildren().clear();
        List<Text> attributes = new ArrayList<>();
        List<Text> methods = new ArrayList<>();
        List<Text> enums = new ArrayList<>();
        List<Text> others = new ArrayList<>();

        // Get style values
        Font font = new Font(getTextSize());
        Color color = getTextColor();
        Color bg = getBackgroundColor();

        // Set name
        name.setText(controller.getViewEntity().getName());
        name.setFont(font);
        name.setFill(color);

        // Fill text lists and sets the entity's width
        width = name.getLayoutBounds().getWidth() + CIRCLE_SIZE + 2 * X_MARGIN;
        setList(controller.getViewEntity().getListAttribute(),
        		attributes, font, color);
        setList(controller.getViewEntity().getListMethod(),
        		methods, font, color);
        setStrList(controller.getViewEntity().getEnumFields(),
        		enums, font, color);
        setStrList(controller.getViewEntity().getAbstractTexts(),
        		others, font, color);
        pane.setMinWidth(width);

        // Draw rectangles
        drawHead(bg);
        Rectangle attrRect = drawField(attributes, head, bg);
        Rectangle methRect = drawField(methods, attrRect, bg);
        if (!enums.isEmpty()) {
            Rectangle enumRect = drawField(enums, methRect, bg);
            if (!others.isEmpty())
                drawField(others, enumRect, bg);
        } else if (!others.isEmpty()) {
            drawField(others, methRect, bg);
        }
        autosize();
    }

    // TOOLS
    /**
     * Sets a text list from an iterable. Uses toString() to get text contents
     * from fields.
     * @param objSet the iterable used.
     * @param textList the list to fill.
     * @param font the font used.
     * @param color the color used.
     */
    private void setList(Iterable<? extends IField> objSet, List<Text> textList,
    		Font font, Color color) {
        for (IField obj : objSet) {
            String s = obj.toString();
            Text text = new Text();
            text.setText(s);
            text.setFont(font);
            text.setFill(color);

            // TODO Couldn’t we just test for Abstract and Static independently?
            if (obj.getModifier().contains(Modifier.Abstract) &&
            		obj.getModifier().contains(Modifier.Static))
                text.setStyle("-fx-font-style: italic; -fx-underline: true");
            else if (obj.getModifier().contains(Modifier.Abstract))
                text.setStyle("-fx-font-style: italic");
            else if (obj.getModifier().contains(Modifier.Static))
                text.setStyle("-fx-underline: true");
            //textList.add(text);
            double currentWidth = text.getLayoutBounds().getWidth() + 2 * Y_MARGIN;
            if (currentWidth > width) {
                width = currentWidth;
            }
            textList.add(text);

        }
    }

    /**
     * Sets a text list from a String iterable.
     * @param objSet the iterable used.
     * @param textList the list to fill.
     * @param font the font used.
     * @param color the color used.
     */
    private void setStrList(Iterable<String> objSet, List<Text> textList,
    		Font font, Color color) {
        for (String s : objSet) {
            Text text = new Text();
            text.setText(s);
            text.setFont(font);
            text.setFill(color);
            textList.add(text);
            double currentWidth = text.getLayoutBounds().getWidth() + 2 * Y_MARGIN;
            if (currentWidth > width) {
                width = currentWidth;
            }
        }
    }

    /**
     * Draws the head of the entity.
     * @param bg the background color.
     */
    private void drawHead(Color bg) {
        double height = Y_MARGIN * 2;
        head = new Rectangle(width, CIRCLE_SIZE + height, bg);
        head.setStroke(strokeColor);
        name.setLayoutX((head.getX() + X_MARGIN * 2) + CIRCLE_MARGIN);
        name.setLayoutY(head.getY() + Y_MARGIN + name.getFont().getSize());
        pane.getChildren().add(head);
        pane.setMinHeight(head.getHeight());

        // Draw the circle representing the entity's type
        Color circleColor = Color.web(controller.getViewEntity()
        		.getType().getColor());
        Circle c = new Circle(CIRCLE_SIZE, circleColor);
        c.setStroke(strokeColor);
        Text text = new Text("" + controller.getViewEntity()
        		.getType().toString().charAt(0));
        text.setBoundsType(TextBoundsType.VISUAL);
        Pane stack = new Pane();
        stack.getChildren().add(c);
        c.setCenterX(head.getX() + CIRCLE_SIZE);
        c.setCenterY(head.getY() + CIRCLE_SIZE);
        text.setX(c.getCenterX() - text.getLayoutBounds().getWidth() / 2);
        text.setY(c.getCenterY() + text.getLayoutBounds().getHeight() / 2);
        stack.getChildren().add(text);
        stack.setLayoutX(head.getX() + stack.getWidth() + CIRCLE_MARGIN);
        stack.setLayoutY(head.getY() + stack.getHeight() + CIRCLE_MARGIN);

        // Adds name and circle
        this.pane.getChildren().add(stack);
        this.pane.getChildren().add(name);
    }

    /**
     * Draws a field rectangle and returns it.
     * @param texts the texts to put in this rectangle.
     * @param prev the previous rectangle in which you attach the new rectangle.
     * @param bg the background color.
     * @return the corresponding rectangle.
     */
    private Rectangle drawField(List<Text> texts, Rectangle prev, Color bg) {
        Rectangle rect;
        if (texts != null && texts.size() > 0) {
            double lineHeight = texts.get(0).getLayoutBounds().getHeight();

            rect = new Rectangle(width, texts.size() * lineHeight + 5, bg);
            rect.setLayoutX(prev.getLayoutX());
            rect.setLayoutY(prev.getLayoutY() + prev.getHeight());
            rect.setStroke(Color.BLACK);
            pane.getChildren().add(rect);
            int i = 1;

            for (Text t : texts) {
                t.setLayoutX(rect.getLayoutX() + X_MARGIN);

                t.setLayoutY(rect.getLayoutY() + (lineHeight * i));
                pane.getChildren().add(t);
                i++;
            }
        } else {
            rect = new Rectangle(width, MIN_RECT_HEIGHT, bg);
            rect.setLayoutX(prev.getLayoutX());
            rect.setLayoutY(prev.getLayoutY() + prev.getHeight());
            rect.setStroke(Color.BLACK);
            pane.getChildren().add(rect);
        }
        pane.setMinHeight(pane.getMinHeight() + rect.getHeight());
        rect.setStroke(strokeColor);
        return rect;
    }
}