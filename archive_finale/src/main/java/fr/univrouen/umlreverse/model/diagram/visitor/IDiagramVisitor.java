package fr.univrouen.umlreverse.model.diagram.visitor;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.diagram.packag.IPackageDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;

/**
 * A diagram visitor.
 */
public interface IDiagramVisitor {
    void visit(IClassDiagram diagram);
    void visit(IUsecaseDiagram diagram);
    void visit(ISequenceDiagram diagram);
    void visit(IPackageDiagram diagram);
}
