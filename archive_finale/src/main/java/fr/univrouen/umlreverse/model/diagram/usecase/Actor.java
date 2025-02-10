package fr.univrouen.umlreverse.model.diagram.usecase;

import fr.univrouen.umlreverse.model.diagram.usecase.visitor.IUsecaseVisitor;

import static fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram.ACTOR_STYLE_ID;

/**
 *
 * Actor Class that represents the Actor in the Use case diagram
 *
 */
public class Actor extends AEntityUsecase implements IActor {
	
//CONSTRUCTORS

    public Actor(String name , System ParentGroup) {
	super(name, ParentGroup);
    }

//REQUESTS
        
    @Override
    public String getStyleId() {
        return ACTOR_STYLE_ID;
    }

    @Override
    public void accept(IUsecaseVisitor visitor) {
        visitor.visit(this);
    }
}
