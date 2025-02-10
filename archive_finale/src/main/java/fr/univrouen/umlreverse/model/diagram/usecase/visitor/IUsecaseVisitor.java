package fr.univrouen.umlreverse.model.diagram.usecase.visitor;

import fr.univrouen.umlreverse.model.diagram.usecase.*;

/**
 * IUsecaseVisitor Interface that represent the mechanise of visitor pattern
 */
public interface IUsecaseVisitor {
    void visit(IActor actor);
    void visit(IUsecase usecase);
    void visit(ISystem system);
    void visit(INoteUsecase note);
    void visit(IUsecaseDiagram diagram);
}
