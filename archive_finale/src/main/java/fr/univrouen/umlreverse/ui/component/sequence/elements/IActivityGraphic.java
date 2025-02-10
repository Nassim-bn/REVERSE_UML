package fr.univrouen.umlreverse.ui.component.sequence.elements;

import fr.univrouen.umlreverse.ui.component.common.elements.EnlargePoint;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityRelationGraphic;
import javafx.scene.shape.Rectangle;

public interface IActivityGraphic extends IEntityRelationGraphic {

	static final int WIDTH = 10;
	static final int HEIGHT = 30;

	EnlargePoint getEnlargePoint();

	int getRectActHeight();

	int getRectActWidth();

	Rectangle getRectangle();
}
