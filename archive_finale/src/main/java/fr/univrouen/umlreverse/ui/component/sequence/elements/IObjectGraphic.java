package fr.univrouen.umlreverse.ui.component.sequence.elements;

import fr.univrouen.umlreverse.ui.component.common.elements.IEntityRelationGraphic;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public interface IObjectGraphic extends IEntityRelationGraphic {

	Rectangle getRectangle();
	
	Point2D lifeLineStartPosition();



}
