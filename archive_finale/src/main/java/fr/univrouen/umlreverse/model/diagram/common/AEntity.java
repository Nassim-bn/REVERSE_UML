/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.model.diagram.common;

import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.ALL_STYLE_ID;
import static fr.univrouen.umlreverse.model.diagram.common.INote.STYLE_CHANGED_PROPERTY_NAME;

import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.diagram.util.Style;
import fr.univrouen.umlreverse.util.Contract;
import java.util.Map;
import java.util.UUID;

/**
 * AEntity abstract class that permits to represent an Entity that can be used by all of the Diagrams (Common code)
 */
public abstract class AEntity extends Observable implements IEntity {
    
// ATTRIBUTES
    private final IDiagramCommon diagram;
    private String id;

// CONSTRUCTORS
    protected AEntity(IDiagramCommon diagram) {
        Contract.check(diagram != null);
        this.diagram = diagram;
        id = UUID.randomUUID().toString();
        boolean b = diagram.addId(id);
        while(!b) {
           id = UUID.randomUUID().toString();
        }
    }

// REQUESTS

    @Override
    public IDiagramCommon getDiagram() {
        return diagram;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public IStyle getStyle() {
        IStyle style = new Style();
        style.putAll(getDiagram().getStyle().getStyle(ALL_STYLE_ID, ALL_STYLE_ID));
        style.putAll(getDiagram().getStyle().getStyle(ALL_STYLE_ID, getStyleId()));
        style.putAll(getDiagram().getStyle().getStyle(getStyleId(), getId()));
        return style;
    }
    
// COMMANDS
    @Override
    public void addAllStyle(Map<String, String> keyValue) {
        Contract.check(keyValue != null);
        keyValue.keySet().stream().forEach(key -> 
            getDiagram().getStyle().addStyle(getStyleId(), getId(), key, keyValue.get(key))
        );
    }
        
    @Override
    public void addStyle(String key, String value) {
        Contract.check(getDiagram() != null);
        IStyle old = getStyle();
        getDiagram().getStyle().addStyle(getStyleId(), getId(), key, value);
        firePropertyChange(STYLE_CHANGED_PROPERTY_NAME, old, getStyle());
    }
    
    @Override
    public void removeStyle(String key) {
        IStyle old = getStyle();
        getDiagram().getStyle().removeStyle(getStyleId(), getId(), key);
        firePropertyChange(STYLE_CHANGED_PROPERTY_NAME, old, getStyle());
    }

    @Override
    public void clearStyle() {
        IStyle old = getStyle();
        getDiagram().getStyle().removeStyle(getStyleId(), getId());
        firePropertyChange(STYLE_CHANGED_PROPERTY_NAME, old, getStyle());
    }
}
