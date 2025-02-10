package fr.univrouen.umlreverse.model.io.parser.util.clazz;

import java.util.HashSet;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewPackage;

/**
 * A parse class package.
 */
public class ParseClassPackage extends ParseClassEntity {
    private final Set<ParseClassPackage> packages;
    private final Set<ParseClassObjectEntity> entities;
    private final Set<ParseClassNote> notes;
    private ViewPackage model;

    public ParseClassPackage(ParseClassDiagram diagram, ParseClassPackage parent, String name) {
        super(diagram, parent, name);
        packages = new HashSet<>();
        entities = new HashSet<>();
        notes = new HashSet<>();
        if (parent != null) {
            parent.add(this);
        }
    }

    public boolean contains(ParseClassObjectEntity e) {
        if (e.getParent().equals(this))
            return entities.contains(e);
        for (ParseClassPackage p : packages) {
            if (p.contains(e)) return true;
        }
        return false;
    }
    public boolean contains(ParseClassNote e) {
        if (e.getParent().equals(this))
            return notes.contains(e);
        for (ParseClassPackage p : packages) {
            if (p.contains(e)) return true;
        }
        return false;
    }
    public boolean contains(ParseClassPackage e) {
        if (e.getParent().equals(this))
            return packages.contains(e);
        for (ParseClassPackage p : packages) {
            if (p.contains(e)) return true;
        }
        return false;
    }

    public ParseClassObjectEntity getObjectEntity(String name) {
        for (ParseClassObjectEntity e : entities) {
            if (e.getName().equals(name))
                return e;
        }
        return null;
    }

    public ParseClassObjectEntity getOrAddObjectEntity(String name) {
        String[] names = name.split("\\.");
        ParseClassPackage p = this;
        for (int i = 0; i < names.length - 1; i++) {
            ParseClassPackage np = p.getPackage(names[i]);
            if (np == null)
                np = new ParseClassPackage(getDiagram(), p, names[i]);
            p = np;
        }
        ParseClassObjectEntity e = p.getObjectEntity(names[names.length - 1]);
        if (e == null)
            e = new ParseClassObjectEntity(getDiagram(), p, names[names.length - 1]);
        return e;
    }

    public ParseClassPackage getPackage(String name) {
        for (ParseClassPackage e : packages) {
            if (e.getName().equals(name))
                return e;
        }
        return null;
    }
    public ParseClassNote getNote(String name) {
        for (ParseClassNote e : notes) {
            if (e.getName().equals(name))
                return e;
        }
        return null;
    }
    public Iterable<ParseClassPackage> getPackages() {
        return packages;
    }
    public Iterable<ParseClassObjectEntity> getEntities() {
        return entities;
    }
    public Iterable<ParseClassNote> getNotes() {
        return notes;
    }

    public void add(ParseClassPackage e) {
        packages.add(e);
    }
    public void add(ParseClassObjectEntity e) {
        entities.add(e);
    }
    public void add(ParseClassNote e) {
        notes.add(e);
    }

    public ViewPackage getModel() {
        return model;
    }

    @Override
    public void addIntoModel() {
        /*IClassDiagram diagram = getDiagram().getModel();
        model = new ViewPackage(getName(), diagram);
        if (getParent() == null || getParent().getModel() == null) {
            diagram.addPackage(model);
        } else {
            try {
                getParent().getModel().addPackage(model);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                diagram.addPackage(model);
            }
        }*/
        // Add children
        packages.forEach(ParseClassPackage::addIntoModel);
        notes.forEach(ParseClassNote::addIntoModel);
        entities.forEach(ParseClassObjectEntity::addIntoModel);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParseClassPackage that = (ParseClassPackage) o;

        return getFullName().equals(that.getFullName());
    }
}
