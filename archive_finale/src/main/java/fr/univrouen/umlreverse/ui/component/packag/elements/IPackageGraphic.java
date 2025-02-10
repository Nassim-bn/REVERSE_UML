package fr.univrouen.umlreverse.ui.component.packag.elements;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewPackage;
import fr.univrouen.umlreverse.ui.component.common.elements.EnlargePoint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public interface IPackageGraphic {

	static final int POSITION_TITLE = -20;
	static final int POSITION_RECT_TITLE = -22;
	static final int POSITION_RECT_PARAMETER = -11;
	static final int POSITION_TEXT_PARAMETER = 14;

	IViewPackage getViewPackage();

	Text getTextParam();

	Rectangle getRectangleParameter();

	Rectangle getRectangle();

	Rectangle getRectangleTitle();

	EnlargePoint getEnlargePoint();
}
