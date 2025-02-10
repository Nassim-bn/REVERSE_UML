package fr.univrouen.umlreverse.model.diagram.sequence;

import java.util.HashSet;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.common.AEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.sequence.visitor.ISequenceVisitor;
import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;

public class Object extends AEntityRelation implements IObject {

	//ATTRIBUTES
	private String name;
	private final Set<INoteSequence> listNotes = new HashSet<>();
	private final IObjectEntity entity;
	private String stereotype;
	private ISequenceDiagram diagram;

	//CONSTRUCTORS

    /**
     * Constructor
     * @param name
     * @param ParentGroup
     */
	public Object(IObjectEntity entity, String name, ISequenceDiagram diagram) {
        super(diagram);
        Contract.check(name != null , "name can't be null");
        this.entity = entity;
        this.name = name;
	}


	@Override
	public String getStyleId() {
		return "object";
	}


	public String getFullName() {
		String keep;
		if (getStereotype() == null || getStereotype().equals(new String(""))) {
			keep = "";
		} else {
			keep = "<<" + getStereotype() + ">>\n";
		}
		return keep + name + " : " + getEntity().getName();
	}


	@Override
	public String getStereotype() {
		return stereotype;
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Set<INoteSequence> getNotes() {
		return listNotes;
	}

	@Override
	public IObjectEntity getEntity() {
		return entity;
	}

	@Override
	public double getX() {
		String position = getStyle().getValue(IDiagramEditorController.POSITION_STYLE_ID);
		String[] positionTab = position.split("\\|");
		return Double.valueOf(positionTab[2]);
	}

	@Override
	public void addNote(INoteSequence note) {
		Contract.check(note != null , "note can't be null");
		listNotes.add(note);
		//this.getParentGroup().getDiagram().getNotes().add(note);
		diagram.getNotes().add(note);
        firePropertyChange(NOTE_ADDED_PROPERTY_NAME, null, listNotes);

	}

	@Override
	public void removeNote(INoteSequence note) {
		if(listNotes.contains(note)){
            //this.getParentGroup().getDiagram().getNotes().remove(note);
			diagram.getNotes().remove(note);
            listNotes.remove(note);
            firePropertyChange(NOTE_REMOVED_PROPERTY_NAME, null, listNotes);
        }

	}

	@Override
	public void setName(String name) {
		Contract.check(name != null , "name can't be null");
        String old = this.name;
        this.name = name;
        firePropertyChange(NAME_CHANGED_PROPERTY_NAME, old , this.name);
	}


	@Override
    public IRelation addRelation(IEntityRelation entity) {
        Contract.check(entity != null, "entity must not be null.");
        IRelationToObject relation = new RelationToObject(getDiagram(), this, entity);
        relations.put(entity, relation);
        entity.addRelationDst(relation, entity);
        getDiagram().addRelation(relation);
        firePropertyChange(RELATION_ADDED_PROPERTY_NAME, null, relation);
        return relation;
    }

	@Override
	public void setStereotype(String stereotype) {
        String old = this.stereotype;
        this.stereotype = stereotype;
        firePropertyChange(STEREOTYPE_CHANGED_PROPERTY_NAME, old , stereotype);
	}

	@Override
	public void accept(ISequenceVisitor visitor) {
		// TODO Auto-generated method stub

	}

}
