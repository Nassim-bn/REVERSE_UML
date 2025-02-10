package fr.univrouen.umlreverse.model.io.parser.util.common;

import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;

/**
 * A relation type.
 */
public class ParseRelationType {
    private Head head;
    private Body body;
    private Tail tail;

    public ParseRelationType(Head head, Body body, Tail tail) {
        this.head = head;
        this.body = body;
        this.tail = tail;
    }

    public Head getHead() {
        return head;
    }

    public Body getBody() {
        return body;
    }

    public Tail getTail() {
        return tail;
    }

    public ParseRelationType reverse() {
        Head h = Head.NONE;
        switch (this.tail) {
            case STAR:
                h = Head.STAR;
                break;
            case CIRCLE:
                h = Head.CIRCLE;
                break;
            case ARROW:
                h = Head.ARROW;
                break;
            case FULL_ARROW:
                h = Head.FULL_ARROW;
                break;
            case PLUS:
                h = Head.PLUS;
                break;
            default:
            	break;
        }

        Tail t = Tail.NONE;
        switch (this.head) {
            case STAR:
                t = Tail.STAR;
                break;
            case CIRCLE:
                t = Tail.CIRCLE;
                break;
            case ARROW:
                t = Tail.ARROW;
                break;
            case FULL_ARROW:
                t = Tail.FULL_ARROW;
                break;
            case PLUS:
                t = Tail.PLUS;
                break;
            default:
            	break;
        }

        return new ParseRelationType(h, this.body, t);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParseRelationType that = (ParseRelationType) o;

        return head == that.head && body == that.body && tail == that.tail;

    }

    // STATIC TOOLS
    public static ParseRelationType fromString(String head, String body, String tail) {
        return new ParseRelationType(Head.fromString(head), Body.fromString(body), Tail.fromString(tail));
    }

    // ENUMS
    /**
     * A relation head.
     */
    public enum Head {
        STAR("*", TypeHeadArrow.Composition),
        CIRCLE("o", TypeHeadArrow.Aggregation),
        ARROW("<", TypeHeadArrow.EmptyHead),
        FULL_ARROW("<|", TypeHeadArrow.FullHead),
        PLUS("+", TypeHeadArrow.InternalClass),
        NONE("", TypeHeadArrow.None);

        private final String str;
        private final TypeHeadArrow type;

        Head(String str, TypeHeadArrow type) {
            this.str = str;
            this.type = type;
        }

        public TypeHeadArrow getType() {
            return type;
        }

        public static Head fromString(String str) {
            if (str.isEmpty()) return Head.NONE;
            for (Head o : Head.values()) {
                if (o.str.equals(str))
                    return o;
            }
            throw new IllegalArgumentException(str + " is not a valid head");
        }
    }

    /**
     * A relation body.
     */
    public enum Body {
        DOTS('.', TypeLineArrow.Dashed),
        DASHES('-', TypeLineArrow.Plain);

        private final char c;
        private final TypeLineArrow type;

        Body(char c, TypeLineArrow type) {
            this.c = c;
            this.type = type;
        }

        public TypeLineArrow getType() {
            return type;
        }

        public static Body fromString(String str) {
            char c = str.charAt(0);
            for (Body o : Body.values()) {
                if (o.c == c)
                    return o;
            }
            throw new IllegalArgumentException(str + " is not a valid body");
        }
    }

    /**
     * A relation tail.
     */
    public enum Tail {
        STAR("*", TypeHeadArrow.Composition),
        CIRCLE("o", TypeHeadArrow.Aggregation),
        ARROW(">", TypeHeadArrow.EmptyHead),
        FULL_ARROW("|>", TypeHeadArrow.FullHead),
        PLUS("+", TypeHeadArrow.InternalClass),
        NONE("", TypeHeadArrow.None);

        private final String str;
        private final TypeHeadArrow type;

        Tail(String str, TypeHeadArrow type) {
            this.str = str;
            this.type = type;
        }

        public TypeHeadArrow getType() {
            return type;
        }

        public static Tail fromString(String str) {
            if (str.isEmpty()) return Tail.NONE;
            for (Tail o : Tail.values()) {
                if (o.str.equals(str))
                    return o;
            }
            throw new IllegalArgumentException(str + " is not a valid tail");
        }
    }
}
