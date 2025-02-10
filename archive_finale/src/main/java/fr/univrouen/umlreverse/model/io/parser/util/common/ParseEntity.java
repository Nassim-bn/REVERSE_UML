package fr.univrouen.umlreverse.model.io.parser.util.common;

/**
 * The entity superclass.
 * @param <D> The parse diagram used.
 * @param <P> The associated group (ex: a package for a class entity).
 */
public abstract class ParseEntity<D extends ParseDiagram<?, ?, ?>, P extends ParseEntity<?, ?>> {
    private final D diagram;
    private final P parent;
    private String name;

    /**
     * @param diagram the parse diagram.
     * @param parent the parent entity.
     * @param name the name of this entity.
     */
    public ParseEntity(D diagram, P parent, String name) {
        this.diagram = diagram;
        this.parent = parent;
        this.name = name;
    }

    /**
     * @return the diagram associated.
     */
    public D getDiagram() {
        return diagram;
    }

    /**
     * @return the parent of this entity.
     */
    public P getParent() {
        return parent;
    }

    /**
     * @return the name with the name of all its parents separated by a dot.
     */
    public String getFullName() {
        if (getParent() == null)
            return "";
        return getParent().getFullName().equals("") ? getName() : getParent().getFullName() + "." + getName();
    }

    /**
     * @return the entity's name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Abstract method used to add the entity into the real model.
     */
    public abstract void addIntoModel();
}
