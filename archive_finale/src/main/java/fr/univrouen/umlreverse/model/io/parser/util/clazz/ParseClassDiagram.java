package fr.univrouen.umlreverse.model.io.parser.util.clazz;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseDiagram;
import fr.univrouen.umlreverse.model.io.parser.util.log.Logger;

/**
 * A parse class diagram.
 */
public class ParseClassDiagram extends ParseDiagram<IClassDiagram, ParseClassPackage, ParseClassRelation> {
    public ParseClassDiagram(IClassDiagram model, Logger logger) {
        super(model, logger);
        root = new ParseClassPackage(this, null, "unknown");
    }
}
