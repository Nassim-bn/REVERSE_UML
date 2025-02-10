package fr.univrouen.umlreverse.model.io.parser.util.usecase;

import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseDiagram;
import fr.univrouen.umlreverse.model.io.parser.util.log.Logger;

/**
 * An usecase diagram.
 */
public class ParseUsecaseDiagram extends ParseDiagram<IUsecaseDiagram, ParseUsecaseGroup, ParseUsecaseRelation> {
    public ParseUsecaseDiagram(IUsecaseDiagram model, Logger logger) {
        super(model, logger);
        root = new ParseUsecaseGroup(this, null, "");
    }
}
