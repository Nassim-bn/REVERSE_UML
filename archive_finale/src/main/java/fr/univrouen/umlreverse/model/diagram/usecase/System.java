package fr.univrouen.umlreverse.model.diagram.usecase;

import fr.univrouen.umlreverse.model.diagram.common.Observable;
import fr.univrouen.umlreverse.model.diagram.usecase.visitor.IUsecaseVisitor;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.diagram.util.Style;
import fr.univrouen.umlreverse.util.Contract;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.ALL_STYLE_ID;
import static fr.univrouen.umlreverse.model.diagram.common.INote.STYLE_CHANGED_PROPERTY_NAME;
import static fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram.USECASEGROUP_STYLE_ID;

/**
 *
 *System represents the use case group
 *
 */
public class System extends Observable implements ISystem {

// ATTRIBUTES
    private IUsecaseDiagram ParentDiagram;
	private final Set<ISystem> listUseCaseGroups = new HashSet<>() ;
	private final Set<Usecase> listUseCases = new HashSet<>() ;
	private final Set<INoteUsecase> listNotes = new HashSet<>() ;
	private String UseCaseGroupName;
    private System ParentGroup;
    private final String id;

// CONSTRUCTORS

    /**
     * Constructor
     * @param useCaseGroupName
     * @param parentDiagram
     */
    public System(String id, String useCaseGroupName, IUsecaseDiagram parentDiagram) {
        Contract.check(useCaseGroupName != null , "useCaseGroupName use case can't be null");
        Contract.check(parentDiagram != null , "parentDiagram use case can't be null");

        UseCaseGroupName = useCaseGroupName;
        ParentDiagram = parentDiagram;
        this.id = id;
    }

// REQUESTS
    @Override
    public String getAbsoluteName() {
        if (this.getParentGroup() != null) {
            return this.getParentGroup().getAbsoluteName() +"/"+this.getNameGroup();
        } else {
            return this.getNameGroup();
        }
    }

    @Override
    public IUsecaseDiagram getDiagram() {
        return ParentDiagram;
    }

    @Override
    public Set<ISystem> getGroups() {
	return listUseCaseGroups;
    }

    @Override
    public String getNameGroup() {
	return UseCaseGroupName;
    }

    @Override
    public Set<INoteUsecase> getNotes() {
	return listNotes;
    }

    @Override
    public System getParentGroup() {
        return ParentGroup;
    }

    @Override
    public IStyle getStyle() {
        IStyle style = new Style();
        style.putAll(ParentDiagram.getStyle().getStyle(ALL_STYLE_ID, ALL_STYLE_ID));
        style.putAll(ParentDiagram.getStyle().getStyle(ALL_STYLE_ID, USECASEGROUP_STYLE_ID));
        style.putAll(ParentDiagram.getStyle().getStyle(USECASEGROUP_STYLE_ID, id));
        return style;
    }

    @Override
    public Set<Usecase> getUseCases() {
	return listUseCases;
    }

// COMMANDS

    @Override
    public void addAllStyle(Map<String, String> keyValue) {
        Contract.check(keyValue != null);
        keyValue.keySet().stream().forEach((key) -> {
            ParentDiagram.getStyle().addStyle(USECASEGROUP_STYLE_ID, id, key, keyValue.get(key));
        });
    }

    @Override
    public void addGroup(ISystem group) throws Exception {
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
                group.setParentGroup(this);
                group.setDiagram(this.getDiagram());
                group.getDiagram().getSystems().add(group);
                firePropertyChange("addGroup", null, listUseCaseGroups);
            }
            else {
                throw new Exception("This name of group is already taken, please choose an other one");
            }
        }
        else {
            throw new Exception("This group is already exist");
        }
    }

    @Override
    public void addNote(INoteUsecase note){
        Contract.check(note != null , "note can't be null");
		listNotes.add(note);
        this.getDiagram().getNotes().add(note);
        firePropertyChange("addNote", null, listNotes);
    }

    @Override
    public void addStyle(String key, String value) {
        Contract.check(ParentDiagram != null);
        IStyle old = getStyle();
        ParentDiagram.getStyle().addStyle(USECASEGROUP_STYLE_ID, id, key, value);
        firePropertyChange(STYLE_CHANGED_PROPERTY_NAME, old, getStyle());
    }

    @Override
    public void addUsecase(Usecase useCase) throws Exception {
        Contract.check(useCase != null , "useCase can't be null");

        if(!listUseCases.contains(useCase)){
            boolean b = false;
            Iterator<Usecase> it = listUseCases.iterator();
            while(it.hasNext() && b==false){
                if (it.next().getName().equals(useCase.getName())){
                    b=true;
                }
            }
            if(b==false){
                listUseCases.add(useCase);
                useCase.setParentGroup(this);
                this.getDiagram().getUsecases().add(useCase);
                firePropertyChange("addEntity", null, listUseCases);
            }
            else {
                throw new Exception("This name of group is already taken, please choose an other one");
            }
        }
        else{
            throw new Exception("This Use case is already exist");
        }
    }

    @Override
    public void clearStyle() {
        IStyle old = getStyle();
        ParentDiagram.getStyle().removeStyle(USECASEGROUP_STYLE_ID, id);
        firePropertyChange(STYLE_CHANGED_PROPERTY_NAME, old, getStyle());
    }

    @Override
    public void removeStyle(String key) {
        IStyle old = getStyle();
        ParentDiagram.getStyle().removeStyle(USECASEGROUP_STYLE_ID, id, key);
        firePropertyChange(STYLE_CHANGED_PROPERTY_NAME, old, getStyle());
    }

    @Override
    public void setDiagram(IUsecaseDiagram parentDiagram) {
        Contract.check(parentDiagram != null , "parentDiagram can't be null");
        IUsecaseDiagram old = this.ParentDiagram;
        IUsecaseDiagram news = parentDiagram;
        ParentDiagram = parentDiagram;
        firePropertyChange("setParentGroup", old, news);
    }

    @Override
    public void removeGroup(ISystem group) throws Exception {
        Contract.check(group != null , "group can't be null");
        if(listUseCaseGroups.contains(group)){
            if(this.getParentGroup()==null){
                group.getDiagram().getSystems().remove(group);
                listUseCaseGroups.remove(group);
                firePropertyChange("removeGroup", null, listUseCaseGroups);
            }
            else{
                group.getParentGroup().getGroups().remove(group);
                group.getDiagram().getSystems().remove(group);
                listUseCaseGroups.remove(group);
                firePropertyChange("removeGroup", null, listUseCaseGroups);
            }
        }
        else{
            throw new Exception("This group does't exist");
        }
    }

    @Override
    public void removeNote(INoteUsecase note) throws Exception {
        Contract.check(note != null , "note can't be null");
        if(listNotes.contains(note)){
            this.getDiagram().getNotes().remove(note);
            listNotes.remove(note);
            firePropertyChange("removeNote", null, listNotes);
        } else{
            throw new Exception("This note is already exist");
        }
    }

    @Override
    public void removeUsecase(Usecase useCase) throws Exception {
        Contract.check(useCase != null , "useCase can't be null");
        if(listUseCases.contains(useCase)){
            this.getDiagram().getUsecases().remove(useCase);
            listUseCases.remove(useCase);
            firePropertyChange("removeEntity", null, listUseCases);
        }
        else{
            throw new Exception("This Use case does't exist");
        }
    }

    @Override
    public void setParentGroup(System parentGroup) {
        Contract.check(parentGroup != null , "parentGroup can't be null");
        System old = this.ParentGroup;
        System news = parentGroup;
        ParentGroup = parentGroup;
        firePropertyChange("setParentGroup", old, news);
    }

    @Override
    public void setNameGroup(String useCaseGroupName) {
    	Contract.check(useCaseGroupName != null , "name can't be null");
        firePropertyChange("useCaseGroupName", this.UseCaseGroupName, useCaseGroupName);
	UseCaseGroupName = useCaseGroupName;
    }
    @Override
    public void accept(IUsecaseVisitor visitor) {
        visitor.visit(this);
    }
}
