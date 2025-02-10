package fr.univrouen.umlreverse.model.io.parser.util.clazz;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewRelation;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewRelation;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseRelation;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseRelationType;
import fr.univrouen.umlreverse.model.project.Relation;

import java.util.UUID;

/**
 * A parse class relation.
 */
public class ParseClassRelation extends ParseRelation<ParseClassDiagram, ParseClassObjectEntity> {
    // CONSTANTS
    private static final String NULL_ERR = "Relation impossible à ajouter, entité inconnue.";

    // ATTRIBUTES
    ParseClassNote note;

    // CONSTRUCTORS
    public ParseClassRelation(ParseClassDiagram diagram,
                              ParseClassObjectEntity src,
                              String headLabel,
                              ParseRelationType type,
                              String tailLabel,
                              ParseClassObjectEntity dst,
                              String bodyLabel) {
        super(diagram, src, headLabel, type, tailLabel, dst, bodyLabel);
        note = null;
    }

    public ParseClassRelation(ParseClassDiagram diagram,
                              ParseClassObjectEntity src,
                              String headLabel,
                              ParseRelationType type,
                              String tailLabel,
                              ParseClassNote note,
                              String bodyLabel) {
        super(diagram, src, headLabel, type, tailLabel, null, bodyLabel);
        this.note = note;
    }

    // COMMANDS
    @Override public void addIntoModel() {
        try {
            if (note != null) {
                IViewEntity entity = null;
                for (IViewEntity viewEntity : diagram.getModel().getEntities()) {
                    if (viewEntity.getData().equals(src.getModel())) {
                        entity = viewEntity;
                        break;
                    }
                }
                if (entity == null) {
                    diagram.getLogger().addError(NULL_ERR);
                } else {
                    note.getModel().addEntity(entity);
                }
            } else {
                if (dst.getModel() == null || src.getModel() == null) return;
                Relation relation = new Relation("" + UUID.randomUUID(), src.getModel(), dst.getModel(),
                        type.getHead().getType(), type.getTail().getType(), type.getBody().getType());
                relation.setHeadComment(headLabel);
                relation.setTailComment(tailLabel);
                relation.setNameRelation(bodyLabel);
                diagram.getModel().getProject().addRelation(relation);
                IViewRelation viewRelation = new ViewRelation(relation, diagram.getModel());
                diagram.getModel().addRelation(viewRelation);
            }
        } catch (NullPointerException ex) {
            diagram.getLogger().addError(NULL_ERR);
        }
    }

    public ParseClassRelation reverse() {
        return new ParseClassRelation(diagram, dst, tailLabel, type.reverse(), headLabel, src, bodyLabel);
    }
}
