package fr.univrouen.umlreverse.model.diagram.common;

import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.NOTE_STYLE_ID;

import java.util.HashSet;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.visitor.IDiagramVisitor;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.util.Contract;

/**
 * ANote abstract class that permits to represent a note that can be used by all of the Diagrams (Common code)
 */
public class Note extends AEntityRelation implements INote {
/**
	 * 
	 */
	private static final long serialVersionUID = 3026948385477503397L;
	// ATTRIBUTES  
    private String text = "";
    private final Set<IEntityRelation> entities = new HashSet<>();

// CONSTRUCTOR

    public Note(String text, IDiagramCommon d) {
        super(d);
        this.text = text;
    }

    // REQUESTS
    
    @Override
    public String getStyleId() {
        return NOTE_STYLE_ID;
    }

    @Override
    public String getText() {
        return text;
    }

// COMMANDS
    @Override
    public IRelation addRelation(IEntityRelation entity) {
    	super.addRelation(entity);
    	IRelation relation = relations.get(entity);
    	relation.setArrowHead(RelationTypeEnum.NOTE.getHead());
    	relation.setLineArrow(RelationTypeEnum.NOTE.getLine());
    	relation.setArrowTail(RelationTypeEnum.NOTE.getTail());
    	return relation;
    }
    
    @Override
    public void addRelationDst(IRelation relation, IEntityRelation entity) {
    	super.addRelationDst(relation, entity);
    	relation.setArrowHead(RelationTypeEnum.NOTE.getHead());
    	relation.setLineArrow(RelationTypeEnum.NOTE.getLine());
    	relation.setArrowTail(RelationTypeEnum.NOTE.getTail());
    }
    
    @Override
    public void setText(String text) {
        if (!this.text.equals(text)) {
            String oldText = this.text;
            this.text = text;
            firePropertyChange(TEXT_CHANGED_PROPERTY_NAME, oldText, text);
        }
    }

	@Override
	public Set<IEntityRelation> getEntityRelation() {
		return entities;
	}

	@Override
	public void addEntityRelation(IEntityRelation entity) {
		Contract.check(entity != null);
		entities.add(entity);
        firePropertyChange(ENTITY_ADDED_PROPERTY_NAME, null, entity);
	}

	@Override
	public void removeEntityRelation(IEntityRelation entity) {
		Contract.check(entity != null);
		entities.remove(entity);
        firePropertyChange(ENTITY_REMOVED_PROPERTY_NAME, entity, null);
	}

	@Override
	public void accept(IDiagramVisitor visitor) {
		//visitor.visit(this);
	}
}
