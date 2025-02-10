package fr.univrouen.umlreverse.ui.view.packag;

import fr.univrouen.umlreverse.model.diagram.packag.IPackageDiagram;
import fr.univrouen.umlreverse.ui.view.clazz.ClassDiagramEditor;

public class PackageDiagramEditor extends ClassDiagramEditor {

	// CONSTRUCTOR
	public PackageDiagramEditor(IPackageDiagram packageDiagram) {
		super();
		controller = new PackageController(this, packageDiagram);
	}

	public IPackageController getController() {
		return (IPackageController) controller;
	}

	 /**
     * Create a package via the controller.
     * @param x the x position of the entity
     * @param y the y position of the entity
     */
    public void createPackage(double x, double y) {
       getController().createPackage(x, y);
    }
}
