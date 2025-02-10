package fr.univrouen.umlreverse.ui.component.usecase.elements;


import fr.univrouen.umlreverse.ui.component.common.elements.IEntityRelationGraphic;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.List;

public interface IActorGraphic extends IEntityRelationGraphic {
    /**
     * Getter for the shape that composed the Actor.
     * @return List<Shape>
     */
    List<Shape> getAllActorShape();

    List<Shape> getNormalShape();

    /**
     * Getter for the Text below the Actor graphic.
     * @return Text
     */
    Text getText();
}
