package fr.univrouen.umlreverse.model.diagram.usecase;

import fr.univrouen.umlreverse.model.diagram.usecase.visitor.IUsecaseVisitor;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.diagram.common.ADiagramCommon;
import fr.univrouen.umlreverse.model.diagram.common.INote;
import fr.univrouen.umlreverse.model.diagram.visitor.IDiagramVisitor;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 *
 * DiagramUsecase represents the use case Diagram
 * 
 */
public class UsecaseDiagram extends ADiagramCommon implements IUsecaseDiagram {

// ATTRIBUTES	
	private final Set<ISystem> listUseCaseGroups = new HashSet<>() ;
	private final Set<Actor> listActors = new HashSet<>() ;
	private final Set<IUsecase> listUseCases = new HashSet<>() ;
    private final Set<INoteUsecase> listNotes = new HashSet<>() ;
    private final System DefaultGroup = new System("root", "DefaultGroup", this);
        
// CONSTRUCTORS

    /**
     * Constructor
     * @param project
     * @param name
     * @throws fr.univrouen.umlreverse.model.util.RefusedAction
     */
    public UsecaseDiagram(IProject project, String name) throws RefusedAction {
        super(project, name);
    }
	
// REQUESTS
        @Override
	public Actor getActor(String name) {
        Contract.check(name != null , "Name actor can't be null");
		Iterator<Actor> it = listActors.iterator();
		while(it.hasNext()){
			if (it.next().getName().equals(name)){
				return it.next();
			}
                }
        return null;
	}
        
    @Override
	public Set<Actor> getActors() {
            return listActors;
	}

    @Override
    public Set<INoteUsecase> getNotes() {
        return listNotes;
    }

    @Override
    public System getRootSystem() {
            return DefaultGroup;
        }
        
	@Override
	public Set<ISystem> getSystems() {
		return listUseCaseGroups;
	}
        
    @Override
	public IUsecase getUsecase(String name){
        Contract.check(name != null , "Name use case can't be null");
            Iterator<IUsecase> it = listUseCases.iterator();
            while(it.hasNext()){
                    if (it.next().getName().equals(name)){
                            return it.next();
                    }
            }
		return null;
	}
	
	@Override
	public Set<IUsecase> getUsecases() {
		return listUseCases;
	}
	
	@Override
	public DiagramType getType() {
		return DiagramType.USECASE;
	}

// COMMANDS
    
    @Override
	public void addActor(Actor actor) {
        Contract.check(actor != null , "actor can't be null");
        boolean b = listActors.add(actor);
        if (b) {
            firePropertyChange(ACTOR_ADDED_PROPERTY_NAME, null, actor);
        }
	}
        
    @Override
    public void addNote(INoteUsecase note) {
        Contract.check(note != null , "note can't be null");
        boolean b = listNotes.add(note);
        if (b) {
            firePropertyChange(NOTE_ADDED_PROPERTY_NAME, null, note);
        }
    }
	
    @Override
	public void addSystem(ISystem group) {
        Contract.check(group != null , "group can't be null");
        boolean b = false;
        if(!listUseCaseGroups.contains(group)){
            Iterator<ISystem> it = listUseCaseGroups.iterator();
            while(it.hasNext() && b==false){
                if (it.next().getNameGroup().equals(group.getNameGroup())){
                    b=true;
                }
            }
            if(b==false){
                listUseCaseGroups.add(group);
                group.setDiagram(this);
            }
        }
    }
        
	@Override
	public void addUsecase(IUsecase useCase) {
        Contract.check(useCase != null , "useCase can't be null");
        boolean b = listUseCases.add(useCase);
        if (b) {
            firePropertyChange(USECASE_ADDED_PROPERTY_NAME, null, useCase);
        }
	}
        
    @Override
	public void removeActor(Actor actor) {
        boolean b = listActors.remove(actor);
        if (b) {
            actor.getRelations().stream().forEach((r) -> {
                removeRelation(r);
            });
            firePropertyChange(ACTOR_REMOVED_PROPERTY_NAME, actor, null);
        }
	}
 
    @Override
    public void removeNote(INoteUsecase note){
        Contract.check(note != null , "note can't be null");
        boolean b = listNotes.remove(note);
        if (b) {
            note.getRelations().stream().forEach((r) -> {
                removeRelation(r);
            });
            firePropertyChange(NOTE_REMOVED_PROPERTY_NAME, note, null);
        }
    }

	@Override
	public void removeSystem(ISystem group) {
        Contract.check(group != null , "group can't be null");
        if(listUseCaseGroups.contains(group)){
            group.getParentGroup().getGroups().remove(group);

            listNotes.removeAll(group.getNotes());
            listUseCases.removeAll(group.getUseCases());
            listUseCaseGroups.remove(group);
        }
	}
	
	
	@Override
	public void removeUsecase(IUsecase usecase) {
        Contract.check(usecase != null , "useCase can't be null");
        boolean b = listUseCases.remove(usecase);
        if (b) {
            usecase.getRelations().stream().forEach((r) -> {
                removeRelation(r);
            });
            firePropertyChange(USECASE_REMOVED_PROPERTY_NAME, usecase, null);
        }
	}

    @Override
    public void accept(IUsecaseVisitor visitor) {
        visitor.visit(this);
    }
    @Override
    public void accept(IDiagramVisitor visitor) {
        visitor.visit(this);
    }

	@Override
	public void addNote(INote note) {
		// TODO Auto-generated method stub
		
	}
}
