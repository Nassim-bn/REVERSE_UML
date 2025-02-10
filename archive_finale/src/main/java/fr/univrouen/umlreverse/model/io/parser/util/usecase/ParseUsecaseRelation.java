package fr.univrouen.umlreverse.model.io.parser.util.usecase;

import fr.univrouen.umlreverse.model.diagram.common.Relation;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseRelation;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseRelationType;

/**
 * An usecase relation.
 */
public class ParseUsecaseRelation extends ParseRelation<ParseUsecaseDiagram, ParseUsecaseEntity<?>> {
    private static final String MODEL_ERR = "Impossible d'ajouter la relation.";

    public ParseUsecaseRelation(ParseUsecaseDiagram diagram,
                                ParseUsecaseEntity<?> src,
                                String headLabel,
                                ParseRelationType type,
                                String tailLabel,
                                ParseUsecaseEntity<?> dst,
                                String bodyLabel) {
        super(diagram, src, headLabel, type, tailLabel, dst, bodyLabel);
    }

    @Override public void addIntoModel() {
        try {
            Relation relation = new Relation(diagram.getModel(), src.getModel(), dst.getModel(),
                    type.getHead().getType(), type.getTail().getType(), type.getBody().getType());
            relation.setHeadComment(headLabel);
            relation.setTailComment(tailLabel);
            relation.setNameRelation(bodyLabel);
            diagram.getModel().addRelation(relation);
        } catch (Exception e) {
            diagram.getLogger().addError(MODEL_ERR);
        }
    }

    public ParseUsecaseRelation reverse() {
        return new ParseUsecaseRelation(diagram, dst, tailLabel, type.reverse(), headLabel, src, bodyLabel);
    }
}
