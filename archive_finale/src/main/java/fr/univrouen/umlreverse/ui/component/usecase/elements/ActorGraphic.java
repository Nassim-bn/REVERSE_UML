package fr.univrouen.umlreverse.ui.component.usecase.elements;


import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.usecase.Actor;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphicController;
import fr.univrouen.umlreverse.ui.view.usecase.IUsecaseController;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class ActorGraphic extends AEntityGraphic implements IActorGraphic {
    /**
     * Size of the circle.
     */
    private final double CIRCLE_SIZE = 10;
    /**
     * size of the body.
     */
    private final double BODY_SIZE = 15;
    /**
     * size of the arm.
     */
    private final double SIZE_ARM = 20;
    /**
     * size of leg.
     */
    private final double SIZE_LEG = 12;
    /**
     * size of space between graphic and name.
     */
    private final double MARGE = 15;
    /**
     * Size of a TextLine.
     */
    private final double TEXT_LINE_MARGE = 15;
    /**
     * Marge for borderPane and Actor's head.
     */
    private final double MARGE_HEAD = 5;

    /**
     * List of shape that composed this actor.
     */
    private final List<Shape> li;
    /**
     * Text of the actor.
     */
    private Text text;
    /**
     * Line that represent the arm of the Actor.
     */
    private Line arms;
    /**
     * Circle that represent the head of the Actor.
     */
    private Circle c;
    /**
     * The controller of this actor.
     */
    private final ActorGraphicController controller;



    /**
     * Constructor of the actor graphic.
     *
     * @param diagramEditorController the diagram controller
     * @param act                     the model of the actor graphic
     */
    public ActorGraphic(IUsecaseController diagramEditorController, Actor act) {
        super(diagramEditorController);
        li = new ArrayList<>();
        controller = new ActorGraphicController(diagramEditorController, this, act);
        draw();
        autosize();
    }


    @Override
    public List<Shape> getAllActorShape() {
        List<Shape> list = new ArrayList<>();
        list.addAll(li);
        list.add(c);
        list.add(arms);
        return list;
    }

    @Override
    public List<Shape> getNormalShape() {
        return li;
    }

    @Override
    public Text getText() {
        return text;
    }

    @Override
    public IEntityRelation getModel() {
        return controller.getModel();
    }

    @Override
    public IEntityGraphicController getController() {
        return controller;
    }


    /**
     * Draw the actor.
     */
    private void draw() {
        getChildren().clear();
        //create Pane for text
        Pane q = new Pane();
        text = new Text(controller.getName());
        text.setFill(controller.getTextColor());
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutY(text.getLayoutY() + TEXT_LINE_MARGE);
        q.getChildren().add(text);
        q.setMinHeight(text.getLayoutBounds().getHeight() + TEXT_LINE_MARGE);
        q.setMaxHeight(text.getLayoutBounds().getHeight() + TEXT_LINE_MARGE);
        setBottom(q);


        Pane p = new Pane();
        double widthAct = SIZE_ARM + 4;
        double heightAct = CIRCLE_SIZE + BODY_SIZE + SIZE_LEG + MARGE * 2;
        p.setPrefSize(widthAct, heightAct);
        p.setMinSize(widthAct, heightAct);

        c = new Circle(CIRCLE_SIZE, Color.WHITE);
        c.setLayoutX(Math.max(p.getPrefWidth() / 2, text.getLayoutBounds().getWidth() / 2) + p.getLayoutX());
        c.setLayoutY(p.getLayoutY() + CIRCLE_SIZE + MARGE_HEAD);

        // test if width of text is lower than the min size of Actor and if it is then center text
        if (widthAct > text.getLayoutBounds().getWidth() / 2) {
            text.setLayoutX(c.getLayoutX() - text.getLayoutBounds().getWidth() / 2);
        }


        arms = new Line(c.getLayoutX() - SIZE_ARM / 2, c.getLayoutY() + CIRCLE_SIZE + BODY_SIZE / 4,
                c.getLayoutX() + SIZE_ARM / 2, c.getLayoutY() + CIRCLE_SIZE + BODY_SIZE / 4);
        double legx = c.getLayoutX();
        double legy = c.getLayoutY() + CIRCLE_SIZE + BODY_SIZE;
        Line body = new Line(c.getLayoutX(), c.getLayoutY() + CIRCLE_SIZE, c.getLayoutX(), legy);
        Line legOne = new Line(legx, legy, legx + SIZE_ARM / 2, legy + SIZE_LEG);
        Line legTwo = new Line(legx, legy, legx - SIZE_ARM / 2, legy + SIZE_LEG);
        //add normal shape to list.
        li.add(body);
        li.add(legOne);
        li.add(legTwo);

        //add all shape to different pane.
        p.getChildren().add(c);
        p.getChildren().add(arms);
        p.getChildren().add(body);
        p.getChildren().add(legOne);
        p.getChildren().add(legTwo);
        setCenter(p);
        autosize();
        setStyleToActor();
        setPosition(controller.getPosition());


    }

    /**
     * Set the text to the model's name and then manage position.
     */
    public void setName() {
        getText().setText(controller.getName());
        actualizePosition();
    }

    /**
     * Set style to all shape that composed ActorGraphic.
     */
    public void setStyleToActor() {
        for (Shape s : getAllActorShape()) {
            s.setStroke(controller.getBackgroundColor());
        }
        getText().setFill(controller.getTextColor());
    }

    /**
     * Actualize all the shape's position.
     */
    private void actualizePosition() {
        double d = text.getLayoutBounds().getWidth() / 2;
        Pane p = (Pane)c.getParent();
        if (d + c.getParent().getLayoutX() > p.getPrefWidth() / 2) {
            c.setLayoutX(Math.max(d + c.getParent().getLayoutX(), ((Pane) c.getParent()).getPrefWidth() / 2));

            arms.setLayoutX( d + arms.getParent().getLayoutX() - arms.getStartX() - SIZE_ARM / 2);

            for (Shape s : li) {

                s.setLayoutX(d + s.getParent().getLayoutX() - ((Line) s).getStartX());
            }
            text.setLayoutX(0);
        } else {
            c.setLayoutX(p.getPrefWidth() / 2);
            arms.setLayoutX(c.getLayoutX() -  arms.getStartX() - (SIZE_ARM / 2));
            for (Shape s : li) {

                s.setLayoutX(c.getLayoutX() - ((Line) s).getStartX());
            }
            text.setLayoutX(c.getLayoutX() - text.getLayoutBounds().getWidth() / 2);
        }
        autosize();
    }


}
