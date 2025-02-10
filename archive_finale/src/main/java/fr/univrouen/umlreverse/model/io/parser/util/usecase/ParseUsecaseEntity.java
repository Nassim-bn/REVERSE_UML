package fr.univrouen.umlreverse.model.io.parser.util.usecase;

import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseEntity;

/**
 * An entity which can be used in an parse usecase diagram.
 * @param <E> The corresponding entity in the model.
 */
public abstract class ParseUsecaseEntity<E extends IEntityRelation> extends ParseEntity<ParseUsecaseDiagram, ParseUsecaseGroup> {
    public ParseUsecaseEntity(ParseUsecaseDiagram diagram, ParseUsecaseGroup group, String name) {
        super(diagram, group, name);
        group.add(this);
    }

    public abstract E getModel();
}
