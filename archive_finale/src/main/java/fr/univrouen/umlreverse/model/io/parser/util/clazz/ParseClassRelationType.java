package fr.univrouen.umlreverse.model.io.parser.util.clazz;

import fr.univrouen.umlreverse.model.io.parser.util.common.ParseRelationType;

/**
 * Enumeration containing basic relations, like implementation.
 */
public enum ParseClassRelationType {
    EXTENDS(new ParseRelationType(
            ParseRelationType.Head.FULL_ARROW,
            ParseRelationType.Body.DASHES,
            ParseRelationType.Tail.NONE
    )),
    IMPLEMENTS(new ParseRelationType(
            ParseRelationType.Head.FULL_ARROW,
            ParseRelationType.Body.DOTS,
            ParseRelationType.Tail.NONE
    )),
    INTERNAL_CLASS(new ParseRelationType(
            ParseRelationType.Head.NONE,
            ParseRelationType.Body.DASHES,
            ParseRelationType.Tail.ARROW
    )),
    NOTE_LINK(new ParseRelationType(
            ParseRelationType.Head.NONE,
            ParseRelationType.Body.DOTS,
            ParseRelationType.Tail.NONE
    ));

    private final ParseRelationType type;

    ParseClassRelationType(ParseRelationType type) {
        this.type = type;
    }

    public ParseRelationType getRelationType() {
        return this.type;
    }
}
