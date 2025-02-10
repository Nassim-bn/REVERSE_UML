package fr.univrouen.umlreverse.model.diagram.clazz.visitor;

import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.NoteClass;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewPackage;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewRelation;

/**
 * Class visitor.
 */
public interface IClassVisitor {
    void visit(ClassDiagram classDiagram);
    void visit(ViewEntity viewEntity);
    void visit(ViewPackage viewPackage);
    void visit(NoteClass note);
    void visit(ViewRelation viewRelation);
}
