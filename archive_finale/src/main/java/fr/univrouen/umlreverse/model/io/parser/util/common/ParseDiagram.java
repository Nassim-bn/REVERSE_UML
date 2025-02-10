package fr.univrouen.umlreverse.model.io.parser.util.common;

import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.io.parser.util.log.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * A parse diagram used to store the diagram before adding it into the model.
 * Is more permissive than the model.
 * @param <D> The output diagram in the model.
 * @param <P> Parse entities' type.
 * @param <R> Parse relations' type.
 */
public class ParseDiagram<D extends IDiagram, P extends ParseEntity<?, ?>, R extends ParseRelation<?, ?>> {
    private final D model;
    protected P root;
    private final List<R> relations;
    private final Logger logger;
    public static final int SCALE_GENERATION_X = 200;
    public static final int SCALE_GENERATION_Y = 100;
    public static int count_generation = 0;

    /**
     * @param model the diagram to fill with addIntoModel.
     * @param logger the log used.
     */
    public ParseDiagram(D model, Logger logger) {
        this.logger = logger;
        this.model = model;
        relations = new ArrayList<>();
        count_generation = 0;
    }

    /**
     * @return the root package.
     */
    public P getRoot() {
        return root;
    }

    /**
     * @return the default package (root by default).
     */
    public P getDefaultPackage() {
        return root;
    }

    /**
     * @return all the relations.
     */
    public List<R> getRelations() {
        return relations;
    }

    /**
     * @return The log associated.
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * @return The output diagram.
     */
    public D getModel() {
        return model;
    }

    /**
     * Adds every relation and entity into the model.
     */
    public void addIntoModel() {
        getRoot().addIntoModel();
        relations.forEach(R::addIntoModel);
    }
}
