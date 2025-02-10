
package fr.univrouen.umlreverse.model.diagram.usecase;


import fr.univrouen.umlreverse.model.diagram.common.Note;
import fr.univrouen.umlreverse.model.diagram.common.IDiagramCommon;
import fr.univrouen.umlreverse.model.diagram.usecase.visitor.IUsecaseVisitor;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * Note class represents the notes (comments) on a diagram
 * 
 */
public class NoteUsecase extends Note implements INoteUsecase {

// ATTRIBUTES
    

    //Default serialVersionID to avoid incompatible deserialization
    private static final long serialVersionUID = 1L;
    private final Set<IEntityUsecase> UsecaseEntities = new HashSet<>();

// CONSTRUCTOR

    /**
     * Constructor
     * @param text
     * @param diagram
     * @throws RefusedAction
     */
    public NoteUsecase(String text, IDiagramCommon diagram) throws RefusedAction {
            super(text, diagram);
    }

// REQUESTS
    @Override
	public Set<IEntityUsecase> getUsecaseEntities() {
		return UsecaseEntities;
	}

// COMMANDS
        
    @Override
	public void addUsecaseEntity(IEntityUsecase entity){
		Contract.check(entity != null);
        UsecaseEntities.add(entity);
        firePropertyChange(ENTITY_ADDED_PROPERTY_NAME, null, entity);
	}

	@Override
	public void removeUsecaseEntity(IEntityUsecase entity){
		Contract.check(entity != null);
        UsecaseEntities.remove(entity);
        firePropertyChange(ENTITY_REMOVED_PROPERTY_NAME, entity, null);
	}

    @Override
    public void accept(IUsecaseVisitor visitor) {
        visitor.visit(this);
    }
}