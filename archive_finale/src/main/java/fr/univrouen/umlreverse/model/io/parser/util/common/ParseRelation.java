package fr.univrouen.umlreverse.model.io.parser.util.common;

/**
 * A parse relation between two parse entities.
 * @param <D> The diagram type.
 * @param <E> The super entity used.
 */
public abstract class ParseRelation<D extends ParseDiagram<?, ?, ?>, E extends ParseEntity<?, ?>> {
    protected final D diagram;
    protected final E src;
    protected final String headLabel;
    protected final ParseRelationType type;
    protected final String tailLabel;
    protected final E dst;
    protected final String bodyLabel;

    /**
     * @param diagram the diagram.
     * @param src source entity.
     * @param headLabel text besides the source entity.
     * @param type relation's type.
     * @param tailLabel text besides the destination entity.
     * @param dst destination entity.
     * @param bodyLabel text in the middle of the relation.
     */
    public ParseRelation(D diagram,
                              E src,
                              String headLabel,
                              ParseRelationType type,
                              String tailLabel,
                              E dst,
                              String bodyLabel) {
        this.diagram = diagram;
        this.src = src;
        this.headLabel = headLabel;
        this.type = type;
        this.tailLabel = tailLabel;
        this.dst = dst;
        this.bodyLabel = bodyLabel;
    }

    /**
     * Abstract method used to add this relation into the real model.
     */
    public abstract void addIntoModel();

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParseRelation<?, ?> that = (ParseRelation<?, ?>) o;

        return src != null ? src.equals(that.src) : that.src == null
                && (headLabel != null ? headLabel.equals(that.headLabel) : that.headLabel == null
                && (type != null ? type.equals(that.type) : that.type == null
                && (tailLabel != null ? tailLabel.equals(that.tailLabel) : that.tailLabel == null
                && (dst != null ? dst.equals(that.dst) : that.dst == null
                && (bodyLabel != null ? bodyLabel.equals(that.bodyLabel) : that.bodyLabel == null)))));
    }
}
