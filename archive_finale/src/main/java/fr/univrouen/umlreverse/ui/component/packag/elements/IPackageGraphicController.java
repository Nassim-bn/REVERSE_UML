package fr.univrouen.umlreverse.ui.component.packag.elements;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewPackage;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphicController;
import fr.univrouen.umlreverse.ui.view.packag.IPackageController;

public interface IPackageGraphicController extends IEntityGraphicController {

	void refresh();

	IPackageController getDiagramController();

	IViewPackage getModel();

	IViewEntity getViewEntity();

	void setTextParameter(String s);

}
