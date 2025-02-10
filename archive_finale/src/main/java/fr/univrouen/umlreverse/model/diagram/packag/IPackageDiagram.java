package fr.univrouen.umlreverse.model.diagram.packag;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.diagram.packag.visitor.IPackageVisitor;

/**
 * A package diagram.
 */
public interface IPackageDiagram extends IClassDiagram {

	String PACKAGE_STYLE_ID = "package";

	void accept(IPackageVisitor visitor);
}
