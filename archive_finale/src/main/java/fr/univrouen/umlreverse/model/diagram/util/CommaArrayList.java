package fr.univrouen.umlreverse.model.diagram.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An ArrayList where toString return a comme separated list.
 * @param <E> the elements' type.
 */
public class CommaArrayList<E> extends ArrayList<E> {
    public CommaArrayList() {
        super();
    }

    public CommaArrayList(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public String toString() {
        if (this.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder();
        for (E obj : this) {
            sb.append(obj.toString()).append(", ");
        }
        return sb.replace(sb.length() - 2, sb.length(), "").toString().replaceAll("(^\\[|\\]$)", "");
    }
}
