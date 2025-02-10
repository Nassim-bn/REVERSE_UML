package fr.univrouen.umlreverse.model.diagram.packag.visitor;

import fr.univrouen.umlreverse.model.diagram.clazz.view.NoteClass;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewPackage;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewRelation;
import fr.univrouen.umlreverse.model.diagram.packag.PackageDiagram;

/**
 * Package visitor
 */
public interface IPackageVisitor {
	void visit(PackageDiagram packageDiagram);
	void visit(ViewEntity viewEntity);
	void visit(ViewPackage viewPackage);
	void visit(NoteClass note);
	void visit(ViewRelation viewRelation);
}
