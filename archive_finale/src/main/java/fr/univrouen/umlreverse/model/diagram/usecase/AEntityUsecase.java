package fr.univrouen.umlreverse.model.diagram.usecase;

import fr.univrouen.umlreverse.model.diagram.common.AEntityRelation;
import java.util.HashSet;
import java.util.Set;
import fr.univrouen.umlreverse.util.Contract;

/**
 *
 * AEntityUsecase abstract class that regroup the Actor and the use case elements
 *
 */
public abstract class AEntityUsecase extends AEntityRelation implements IEntityUsecase {

//ATTRIBUTES
	private String Name;
	private System parentGroup;
	private final Set<INoteUsecase> ListNotes = new HashSet<>();

	
//CONSTRUCTORS

    /**
     * Constructor
     * @param name
     * @param ParentGroup
     */
	protected AEntityUsecase(String name, System ParentGroup) {
        super(ParentGroup.getDiagram());
        Contract.check(name != null , "name can't be null");
        Name = name;
        this.parentGroup = ParentGroup;
	}
	
//REQUESTS  

    @Override
    public String getName() {
        return Name;
    }

    @Override
	public Set<INoteUsecase> getNotes() {
		return ListNotes;
	}

    @Override
	public System getParentGroup() {
		return parentGroup;
	}
    
//COMMANDS
    @Override
	public void addNote(INoteUsecase note){
		Contract.check(note != null , "note can't be null");
		ListNotes.add(note);
		this.getParentGroup().getDiagram().getNotes().add(note);
        firePropertyChange(NOTE_ADDED_PROPERTY_NAME, null, ListNotes);
	}
        
    @Override
	public void removeNote(INoteUsecase note){
        if(ListNotes.contains(note)){
            this.getParentGroup().getDiagram().getNotes().remove(note);
            ListNotes.remove(note);
            firePropertyChange(NOTE_REMOVED_PROPERTY_NAME, null, ListNotes);
        }
	}

	@Override
	public void setName(String name) {
		Contract.check(name != null , "name can't be null");
        String old = this.Name;
        Name = name;
        firePropertyChange(NAME_CHANGED_PROPERTY_NAME, old , Name);
	}
	
	@Override
	public void setParentGroup(System parentGroup) {
		Contract.check(parentGroup != null , "parentGroup can't be null");
        System old = this.parentGroup;
        System news = parentGroup;
        this.parentGroup = parentGroup;
        firePropertyChange(PARENT_CHANGED_PROPERTY_NAME, old, news);
	}
        

}
