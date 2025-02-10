package fr.univrouen.umlreverse.model.diagram.usecase;

import fr.univrouen.umlreverse.model.diagram.usecase.visitor.IUsecaseVisitor;

import static fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram.USECASE_STYLE_ID;

/**
 * Usecase Class that allow to create a use case entity
 *
 */
public class Usecase extends AEntityUsecase implements IUsecase {

//ATTRIBUTS
	
//CONSTRUCTORS
    public Usecase(String name, System ParentGroup){
        super(name, ParentGroup);
    }
	
//REQUESTS
 
    @Override
    public String getStyleId() {
        return USECASE_STYLE_ID;
    }

    @Override
    public void accept(IUsecaseVisitor visitor) {
        visitor.visit(this);
    }
}
