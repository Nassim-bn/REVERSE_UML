package fr.univrouen.umlreverse.model.diagram.clazz.view;

import fr.univrouen.umlreverse.model.diagram.clazz.visitor.IClassVisitor;
import fr.univrouen.umlreverse.model.diagram.common.Observable;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.diagram.util.Style;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.ALL_STYLE_ID;
import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.NOTE_STYLE_ID;
import static fr.univrouen.umlreverse.model.diagram.common.INote.STYLE_CHANGED_PROPERTY_NAME;
import static fr.univrouen.umlreverse.model.diagram.common.INote.TEXT_CHANGED_PROPERTY_NAME;

/**
 * Basic implementation of INote.
 */
public class NoteClass extends Observable implements INoteClass {

    // ATTRIBUTES
    private final List<IViewEntity> entities = new LinkedList<>();
    private final List<IViewEntity> constraintEntities = new LinkedList<>();
    private final List<IViewPackage> packages = new LinkedList<>();
    private final List<IViewPackage> constraintPackages = new LinkedList<>();
    private final List<IViewRelation> relations = new LinkedList<>();
    private final List<IViewRelation> constraintRelations = new LinkedList<>();
    private final IClassDiagram diagram;
    private String text = "";
    private String id;
    
    // CONSTRUCTOR
    public NoteClass(String id, String text, IClassDiagram diagram) throws RefusedAction {
        super();
        Contract.check(diagram != null);
        this.diagram = diagram;
        this.text = text;
        this.id = id;
        diagram.addNote(this);
    }

    // REQUESTS
    @Override
    public String getId() {
        return id;
    }

    @Override
    public IStyle getStyle() {
        IStyle style = new Style();
        style.putAll(getDiagram().getStyle().getStyle(ALL_STYLE_ID, ALL_STYLE_ID));
        style.putAll(getDiagram().getStyle().getStyle(ALL_STYLE_ID, getStyleId()));
        style.putAll(getDiagram().getStyle().getStyle(getStyleId(), getId()));
        return style;
    }

    @Override
    public void addAllStyle(Map<String, String> keyValue) {
        Contract.check(keyValue != null);
        keyValue.keySet().stream().forEach((key) -> {
            getDiagram().getStyle().addStyle(getStyleId(), getId(), key, keyValue.get(key));
        });
    }

    @Override
    public void addStyle(String key, String value) {
        Contract.check(getDiagram() != null);
        IStyle old = getStyle();
        getDiagram().getStyle().addStyle(getStyleId(), getId(), key, value);
        firePropertyChange(STYLE_CHANGED_PROPERTY_NAME, old, getStyle());
    }

    @Override
    public void removeStyle(String key) {
        IStyle old = getStyle();
        getDiagram().getStyle().removeStyle(getStyleId(), getId(), key);
        firePropertyChange(STYLE_CHANGED_PROPERTY_NAME, old, getStyle());
    }

    @Override
    public void clearStyle() {
        IStyle old = getStyle();
        getDiagram().getStyle().removeStyle(getStyleId(), getId());
        firePropertyChange(STYLE_CHANGED_PROPERTY_NAME, old, getStyle());
    }

    @Override
    public IClassDiagram getDiagram() {
        return diagram;
    }

    @Override
    public List<IViewEntity> getEntities() {
        return entities;
    }

    @Override
    public List<IViewEntity> getConstraintEntities() {
        return constraintEntities;
    }

    @Override
    public List<IViewRelation> getViewRelations() {
        return relations;
    }

    @Override
    public List<IViewRelation> getConstraintRelations() {
        return constraintRelations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteClass)) return false;

        NoteClass note = (NoteClass) o;
        return getId() != null ? getId().equals(note.getId()) : note.getId() == null;

    }

    @Override
    public int hashCode() {
        int result = (getId() != null ? getId().hashCode() : 0);
        return result;
    }

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
    public void addConstraintEntity(IViewEntity entity) {
        if (!constraintEntities.contains(entity)) {
            constraintEntities.add(entity);
            firePropertyChange(CONSTRAINT_ENTITY_ADDED_PROPERTY_NAME, null, entity);
        }
    }

    @Override
    public void addEntity(IViewEntity entity) {
        if (!entities.contains(entity)) {
            entities.add(entity);
            firePropertyChange(ENTITY_ADDED_PROPERTY_NAME, null, entity);
        }
    }

    @Override
    public void addRelation(IViewRelation relation) {
        if (!relations.contains(relation)) {
            relations.add(relation);
            firePropertyChange(RELATION_ADDED_PROPERTY_NAME, null, relation);
        }
    }

    @Override
    public void removeEntity(IViewEntity entity) {
        Contract.check(diagram != null);
        if (entities.contains(entity)) {
            entities.remove(entity);
            firePropertyChange(ENTITY_REMOVED_PROPERTY_NAME, entity, null);
        }
    }

    @Override
    public void removeConstraintEntity(IViewEntity entity) {
        if (constraintEntities.contains(entity)) {
            constraintEntities.remove(entity);
            firePropertyChange(CONSTRAINT_ENTITY_REMOVED_PROPERTY_NAME, entity, null);
        }
    }

    @Override
    public void addConstraintRelation(IViewRelation relation) {
        if (!constraintRelations.contains(relation)) {
            constraintRelations.add(relation);
            firePropertyChange(CONSTRAINT_RELATION_ADDED_PROPERTY_NAME, null, relation);
        }
    }

    @Override
    public void removeConstraintRelation(IViewRelation relation) {
        if(constraintRelations.contains(relation)) {
            constraintRelations.remove(relation);
            firePropertyChange(CONSTRAINT_RELATION_REMOVED_PROPERTY_NAME, relation, null);
        }
    }

    @Override
    public void removeRelation(IViewRelation relation) {
        if (relations.contains(relation)) {
            relations.remove(relation);
            firePropertyChange(RELATION_REMOVED_PROPERTY_NAME, relation, null);
        }
    }

    @Override
    public void accept(IClassVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void setText(String text) {
        if (!this.text.equals(text)) {
            String oldText = this.text;
            this.text = text;
            firePropertyChange(TEXT_CHANGED_PROPERTY_NAME, oldText, text);
        }
    }
}
