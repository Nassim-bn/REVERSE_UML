package fr.univrouen.umlreverse.model.diagram.sequence.visitor;

import fr.univrouen.umlreverse.model.diagram.usecase.*;

/**
 * IUsecaseVisitor Interface that represent the mechanise of visitor pattern
 */
public interface ISequenceVisitor {
	//TODO les visit de cette classe
    void visit(IActor actor);
    void visit(IUsecase usecase);
    void visit(ISystem system);
    void visit(INoteUsecase note);
    void visit(IUsecaseDiagram diagram);
}
