package fr.univrouen.umlreverse.model.io.parser.util.clazz;

import fr.univrouen.umlreverse.model.io.parser.util.common.ParseEntity;

/**
 * A class entity used in a parse class diagram. Extend this to use new entities.
 */
abstract class ParseClassEntity extends ParseEntity<ParseClassDiagram, ParseClassPackage> {
    ParseClassEntity(ParseClassDiagram diagram, ParseClassPackage parent, String name) {
        super(diagram, parent, name);
    }
}
