package fr.univrouen.umlreverse.model.diagram.clazz.view;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.common.ADiagram;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.diagram.clazz.visitor.IClassVisitor;
import fr.univrouen.umlreverse.model.diagram.visitor.IDiagramVisitor;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;

/**
 * Basic implementation of IClassDiagram
 */
public class ClassDiagram extends ADiagram implements IClassDiagram {

    // ATTRIBUTES

    private Set<INoteClass> notes = new HashSet<>();
    private Set<IViewEntity> entities = new HashSet<>();
    private Set<IViewPackage> packages = new HashSet<>();
    private Set<IViewRelation> relations = new HashSet<>();

    // CONSTRUCTOR

    public ClassDiagram(IProject project, String name) throws RefusedAction {
        super(project, name);
    }

    // REQUESTS

    @Override
    public Set<INoteClass> getNotes() {
        return notes;
    }

    @Override
    public Set<IViewEntity> getEntities() {
        return entities;
    }

    @Override
    public Set<IViewPackage> getPackages() {
        return packages;
    }

    @Override
    public Set<IViewRelation> getRelations() {
        return relations;
    }

    @Override
    public boolean containsIdNote(String id) {
        for (INoteClass note : notes) {
            if (note.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

	@Override
	public DiagramType getType() {
		return DiagramType.CLASS;
	}


// METHODS

    @Override
    public void addNote(INoteClass note) throws RefusedAction {
        if (!notes.contains(note)) {
            for (INoteClass n : notes) {
                if (n.getId().equals(note.getId())) {
                    throw new RefusedAction(ErrorAbstraction.ConflictNoteIdInDiagram);
                }
            }
            notes.add(note);
            firePropertyChange(NOTE_ADDED_PROPERTY_NAME, null, note);
        }
    }

    @Override
    public void removeNote(INoteClass note) {
        if (notes.contains(note)) {
            note.clearStyle();
            notes.remove(note);
            firePropertyChange(NOTE_REMOVED_PROPERTY_NAME, note, null);
        }
    }

    @Override
    public void addEntity(IViewEntity entity) throws RefusedAction {
        Contract.check(getProject().contains(entity.getData()));
        for (IViewEntity view : getEntities()) {
            if (view.getData().equals(entity.getData())) {
                throw new RefusedAction(ErrorAbstraction.ErrorEntityAlreadyInDiagram);
            }
        }
        entities.add(entity);
        firePropertyChange("EntityAdded", null, entity);
    }

    @Override
    public void removeEntity(IViewEntity entity) {
        boolean event = false;
        if (entities.contains(entity)) {
            event = true;
            entity.getData().getListViewer().remove(entity);
            entity.clearStyle();
            entities.remove(entity);
            removeNoteConcerning(entity);

            removeEntityFromPackages(entity);
        }
        getProject().removeEntity(entity.getData());
        if (event) {
            firePropertyChange("DiagramChanged", entity, null);
        }
    }

    private void removeEntityFromPackages(IViewEntity entity) {
    	packages.forEach(pack ->  {
    		pack.getEntities().remove(entity);
		});
	}

	@Override
    public void hideEntity(IViewEntity entity) {
        if (entities.contains(entity)) {
            removeRelationConcerning(entity);
            removeNoteConcerning(entity);
            entity.clearStyle();
            entities.remove(entity);

            removeEntityFromPackages(entity);

            entity.getData().getListViewer().remove(entity);
            firePropertyChange("DiagramChanged", entity, null);
        }
    }

    @Override
    public void addPackage(IViewPackage _package) {
        if (!packages.contains(_package)) {
            packages.add(_package);
            firePropertyChange(PACKAGE_ADDED_PROPERTY_NAME, null, _package);
        }
    }

    @Override
    public void removePackage(IViewPackage _package) {
        if (packages.contains(_package)) {
            removeRelationConcerning(_package);
            _package.removeStyle(PACKAGE_STYLE_ID);
            packages.remove(_package);
            getProject().removeEntity(_package.getData());
            firePropertyChange(PACKAGE_REMOVED_PROPERTY_NAME, _package, null);
        }
    }

    @Override
    public void addRelation(IViewRelation relation) {
        Contract.check(getProject().contains(relation.getData()));
        for (IViewRelation view : getRelations()) {
            if (view.getData().equals(relation.getData())) {
                return;
            }
        }
        relations.add(relation);
        firePropertyChange(RELATION_ADDED_PROPERTY_NAME, null, relation);
    }

    @Override
    public void removeRelation(IViewRelation relation) {
        boolean event = false;
        if (relations.contains(relation)) {
            event = true;
            relation.clearStyle();
            relations.remove(relation);
            relation.getData().removeView(relation);
            removeNoteConcerning(relation);
        }
        getProject().removeRelation(relation.getData());
        if (event) {
            firePropertyChange("RelationRemoved", relation, null);
        }
    }

    @Override
    public void hideRelation(IViewRelation relation) {
        if (relations.contains(relation)) {
            relation.clearStyle();
            relations.remove(relation);
            relation.getData().removeView(relation);
            removeNoteConcerning(relation);
            firePropertyChange("RelationRemoved", relation, null);
        }
    }

    @Override
    public void accept(IClassVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(IDiagramVisitor visitor) {
        visitor.visit(this);
    }


    // TOOLS

    private void removeNoteConcerning(IViewEntity entity) {
        for (INoteClass note : notes) {
            if (note.getConstraintEntities().contains(entity)) {
                note.removeConstraintEntity(entity);
            }
            if (note.getEntities().contains(entity)) {
                note.removeEntity(entity);
            }
        }
    }

    private void removeNoteConcerning(IViewRelation relation) {
        for (INoteClass note : notes) {
            if (note.getConstraintRelations().contains(relation)) {
                note.removeConstraintRelation(relation);
            }
            if (note.getViewRelations().contains(relation)) {
                note.removeRelation(relation);
            }
        }
    }

    private void removeRelationConcerning(IViewEntity entity) {
        List<IViewRelation> relationToDelete = new LinkedList<>();
        relations.stream().filter(relation -> relation.getEntitySource().equals(entity.getData()) || relation.getEntityTarget().equals((entity.getData()))).forEach(relation -> {
            relationToDelete.add(relation);
        });
        for (IViewRelation view : relationToDelete) {
            removeNoteConcerning(view);
            view.getData().removeView(view);
            view.clearStyle();
            relations.remove(view);
            firePropertyChange("RelationRemoved", view, null);
        }
    }
}


