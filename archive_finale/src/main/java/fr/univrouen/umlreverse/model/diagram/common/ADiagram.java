package fr.univrouen.umlreverse.model.diagram.common;


import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.diagram.util.IStyleBox;
import fr.univrouen.umlreverse.model.diagram.util.StyleBox;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;

import java.util.HashSet;
import java.util.Set;

/**
 * A class who factorise the code for all IDiagram.
 */
public abstract class ADiagram extends Observable implements IDiagram {

    // ATTRIBUTES

    /**
     * List of Id
     */
    private Set<String> listIds  = new HashSet<>() ;

    /**
     * The style.
     */
    private IStyleBox styles = new StyleBox();

    /**
     * The name.
     */
    private String name;

    /**
     * The title.
     */
    private String title;

    /**
     * The project.
     */
    private IProject project;

    // CONSTRUCTOR

    /**
     * CONSTRUCTOR
     * @param project
     * @param name
     * @throws RefusedAction
     */
    protected ADiagram(IProject project, String name) throws RefusedAction {
        Contract.check(project != null);
        Contract.check(name != null && name.length() > 0);
        for (IDiagram diagram : project.getDiagrams()) {
            if (diagram.getName().equals(name)) {
                throw new RefusedAction(ErrorAbstraction.ConflictOfNameInProject);
            }
        }
        this.project = project;
        this.name = name;
        project.addDiagram(this);
    }

    // REQUESTS
    @Override
    public Set<String> getIds(){
        return listIds;
    }

    @Override
    public boolean addId(String id){

        return listIds.add(id);
    }

    @Override
    public void removeId(String id){
        listIds.remove(id);
    }

    @Override
    public IProject getProject() {
        return project;
    }
    @Override
    public IStyle getStyle(String type, String id) {
        return styles.getStyle(type, id);
    }
    @Override
    public IStyleBox getStyle() {return styles;}
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return title;
    }
    
    @Override
    public String toString() {
        return getName();
    }

    // METHODS

    @Override
    public void applyStyle(IStyleBox styles) {
        this.styles.setStyle(styles);
    }
    
    public void addStyle(String selector, String id, String key, String value) {
        Contract.check(selector != null, "selector can be not null.");
        Contract.check(id != null, "is can be not null.");
        Contract.check(key != null, "value can be not null.");
        Contract.check(value != null, "value can be not null.");
        styles.addStyle(selector, id, key, value);
    }

    @Override
    public void setName(String name) throws RefusedAction {
        String old = getName();
        if (!old.equals(name)) {
            for (IDiagram d : project.getDiagrams()) {
                if (d.getName().equals(name)) {
                    throw new RefusedAction(ErrorAbstraction.ConflictOfNameInProject);
                }
            }
            this.name = name;
            firePropertyChange(NAME_CHANGED_PROPERTY_NAME, old, name);
        }
    }

    @Override
    public void setTitle(String title) {
        String old = getTitle();
        if (old == null ? title == null : old.equals(title)) {
            this.title = title;
            firePropertyChange(TITLE_CHANGED_PROPERTY_NAME, old, title);
        }
    }
}
