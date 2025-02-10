package fr.univrouen.umlreverse.model.io.parser.util.usecase;

import fr.univrouen.umlreverse.model.diagram.usecase.System;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseEntity;

import java.util.*;

/**
 * An usecase group / system.
 */
public class ParseUsecaseGroup extends ParseEntity<ParseUsecaseDiagram, ParseUsecaseGroup> {
    private final Map<String, ParseUsecaseEntity<?>> children;
    private System system;

    public ParseUsecaseGroup(ParseUsecaseDiagram diagram, ParseUsecaseGroup group, String name) {
        super(diagram, group, name);
        children = new HashMap<>();
    }

    public System getModel() {
        return system;
    }

    public void addIntoModel() {
        system = new System("" + UUID.randomUUID(), getName(), getDiagram().getModel());
        getDiagram().getModel().addSystem(system);
        children.values().forEach(ParseUsecaseEntity::addIntoModel);
    }

    public void add(ParseUsecaseEntity<?> entity) {
        children.put(entity.getName(), entity);
    }

    public ParseUsecaseEntity<?> get(String name) {
        return children.get(name);
    }
}