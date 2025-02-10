package fr.univrouen.umlreverse.model.project;

import fr.univrouen.umlreverse.model.diagram.common.Direction;

import fr.univrouen.umlreverse.util.Contract;

/**
 * Argument is a variable associated to a direction
 */
public class Argument extends Variable {

    // ATTRIBUTES

    /**

     * A direction
     */
    private Direction direction;

    // CONSTRUCTORS

    /**
     * An argument with the direction to None and no initialization
     * @param type
     *      the type
     * @param name
     *      the name
     */
    public Argument(final IType type, final String name) {
        this(Direction.None, type, name, null);
    }

    /**
     * An argument with the direction to None
     * @param type
     *      the type
     * @param name
     *      the name
     * @param initialization
     *      the initialization
     */
    public Argument(final IType type, final String name, final String initialization) {
        this(Direction.None, type, name, initialization);
    }

    /**
     * An argument
     * @param direction
     *      the direction
     * @param type
     *      the type
     * @param name
     *      the name
     * @param initialization
     *      the initialization
     */
    public Argument(Direction direction, final IType type, final String name, final String initialization) {
        super(type, name, initialization);
        Contract.check(direction != null, "direction can't be null");

        this.direction = direction;
    }
    /**
     * An argument with the initialization to null
     * @param direction
     *      the direction
     * @param type
     *      the type
     * @param name
     *      the name
     */
    public Argument(Direction direction, final IType type, final String name) {
        this(direction, type, name, null);
    }

    // REQUESTS

    public Direction getDirection() {
        return direction;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Argument)) return false;

        Argument argument = (Argument) o;

        return super.equals(o) && direction == argument.direction;

    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + (direction != null ? direction.hashCode() : 0);
    }

    // METHODS

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getDirection().toString()).append(' ');
        sb.append(getName());
        sb.append(':').append(getType().toString());
        if (!getInitialization().isEmpty())
            sb.append('=').append(getInitialization());
        return sb.toString();
    }

    public void setDirection(Direction direction) {
        Contract.check(direction != null, "direction can't be null");
        this.direction = direction;
    }

}
