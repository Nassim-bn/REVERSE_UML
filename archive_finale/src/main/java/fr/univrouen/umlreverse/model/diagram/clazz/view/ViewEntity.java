package fr.univrouen.umlreverse.model.diagram.clazz.view;

import fr.univrouen.umlreverse.model.diagram.clazz.visitor.IClassVisitor;
import fr.univrouen.umlreverse.model.diagram.common.Observable;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.diagram.util.Style;
import fr.univrouen.umlreverse.model.project.*;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;

import java.util.*;

import static fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram.ALL_STYLE_ID;
import static fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram.ENTITY_STYLE_ID;

/**
 * A view on an objectEntity
 */
public class ViewEntity extends Observable implements IViewEntity {

    // ATTRIBUTES
    private IViewPackage parent = null;
    /**
     * The original data.
     */
    private IObjectEntity data;

    /**
     * the diagram
     */
    private IClassDiagram diagram;

    /**
     * the abstract texts
     */
    private List<String> abstractTexts = new LinkedList<>();

    /**
     * the attributes
     */
    private Set<IAttribute> attributes = new HashSet<>();

    /**
     * the methods
     */
    private Set<IMethod> methods = new HashSet<>();

// CONSTRUCTOR
    public ViewEntity(IObjectEntity data, IClassDiagram diagram) throws RefusedAction {
        Contract.check(data != null);
        Contract.check(diagram != null);
        boolean find = false;
        for (IObjectEntity entity : diagram.getProject().getAllEntities()) {
            if (entity == data) {
                find = true;
            }
        }
        if (!find) {
            throw new RefusedAction(ErrorAbstraction.ConflictNameEntitiesInPackage);
        }
        this.data = data;
        this.diagram = diagram;
        attributes.addAll(data.getListAttribute());
        abstractTexts.addAll(data.getAbstractTexts());
        methods.addAll(data.getListMethod());
        data.addViewer(this);
        diagram.addEntity(this);
        createTransferEventControllers();
    }

// REQUESTS

    @Override
    public IObjectEntity getData() {
        return data;
    }

    @Override
    public IClassDiagram getDiagram() {
        return diagram;
    }

    @Override
    public IStyle getStyle() {
        if (diagram == null) {
            return null;
        }
        IStyle result = new Style();
        result.putAll(diagram.getStyle().getStyle(ALL_STYLE_ID, ALL_STYLE_ID));
        result.putAll(diagram.getStyle().getStyle(ALL_STYLE_ID, getType().toString()));
        result.putAll(diagram.getStyle().getStyle(ENTITY_STYLE_ID, getAbsoluteName()));
        return result;
    }

    @Override
    public List<String> getAbstractTexts() {
        return abstractTexts;
    }

    @Override
    public List<String> getEnumFields() {
        return data.getEnumFields();
    }

    @Override
    public TypeEntity getType() {
        return data.getType();
    }

    @Override
    public Visibility getVisibility() {
        return data.getVisibility();
    }

    @Override
    public Set<IAttribute> getListAttribute() {
        return attributes;
    }

    @Override
    public Set<IMethod> getListMethod() {
        return methods;
    }

    @Override
    public String getName() {
        return data.getName();
    }

    @Override
    public IViewPackage getPackage() {
        return parent;
    }

    @Override
    public String getAbsoluteName() {
        return data.getAbsoluteName();
    }

    // COMMANDS
    @Override
    public void clearStyle() {
        IStyle old = getStyle();
        diagram.getStyle().removeStyle("entity", getAbsoluteName());
        firePropertyChange("StyleChanged", old, getStyle());
    }

    @Override
    public void setDiagram(IClassDiagram diagram) throws RefusedAction {
        Contract.check(diagram.getProject().contains(getData()));
        IClassDiagram old = this.diagram;
        if (old != diagram) {
            if (old != null) {
                old.removeEntity(this);
            }
            this.diagram = diagram;
            diagram.addEntity(this);
        }
        firePropertyChange("DiagramChanged", old, diagram);
    }

    @Override
    public void setPackage(IViewPackage parent) {
   /* 	if (parent == null && this.parent != null) {
            IViewPackage old = getPackage();

    		getPackage().removeEntity(this);
    		this.parent = null;

            firePropertyChange("PackageChanged", old, getPackage());
    	}
    	if (getPackage() != null && !getPackage().equals(parent)) {
            IViewPackage old = getPackage();
            this.parent = parent;
            if (!parent.getEntities().contains(this)) {
                parent.addEntity(this);
            }
            firePropertyChange("PackageChanged", old, getPackage());
    	}*/

    	if (parent == null && getPackage() != null) {
    		getPackage().removeEntity(this);
    		this.parent = null;
    		return;
    	}

    	if (parent != null && !parent.equals(getPackage())) {
    		this.parent = parent;
    		if (!parent.getEntities().contains(this)) {
                parent.addEntity(this);
            }
    	}



    }

    @Override
    public void setName(String name) throws RefusedAction {
        if (!getName().equals(name)) {
            data.setName(name);
        }
    }

    @Override
    public void addStyle(String key, String value) {
        IStyle old = getStyle();
        diagram.getStyle().addStyle("entity", getAbsoluteName(), key, value);
        firePropertyChange("StyleChanged", old, getStyle());
    }

    @Override
    public void addAllStyle(Map<String, String> keyValue) {
        Contract.check(keyValue != null);
        IStyle old = getStyle();
        String id = getAbsoluteName();
        for (String key : keyValue.keySet()) {
            diagram.getStyle().addStyle("entity", id, key, keyValue.get(key));
        }
        firePropertyChange("StyleChanged", old, getStyle());
    }
    @Override
    public void removeStyle(String key) {
        IStyle old = getStyle();
        diagram.getStyle().removeStyle("entity", getAbsoluteName(), key);
        firePropertyChange("StyleChanged", old, getStyle());

    }

    @Override
    public void setType(TypeEntity type) {
        data.setType(type);
    }

    @Override
    public void addEnumField(String field) {
        if (!data.getEnumFields().contains(field)) {
            data.addEnumField(field);
        }
    }

    @Override
    public void removeEnumField(String field) {
        data.removeEnumField(field);
    }

    @Override
    public void addAbstractText(String text) {
        if (!data.getAbstractTexts().contains(text)) {
            data.addAbstractText(text);
        }
        if (!abstractTexts.contains(text)) {
            abstractTexts.add(text);
        }
    }

    @Override
    public void hideAbstractText(String text) {
        abstractTexts.remove(text);
    }

    @Override
    public void removeAbstractText(String text) {
        abstractTexts.remove(text);
        data.removeAbstractText(text);
    }

    @Override
    public void setVisibility(Visibility visibility) {
        if (getVisibility() != visibility) {
            data.setVisibility(visibility);
        }
    }

    @Override
    public void addAttribute(IAttribute attribute) throws RefusedAction {
        Set<IAttribute> old = getListAttribute();
        if (!data.getListAttribute().contains(attribute)) {
            data.addAttribute(attribute);
        }
        attributes.add(attribute);
        firePropertyChange("AttributesChanged", old, getListAttribute());
    }

    @Override
    public void hideAttribute(IAttribute attribute) {
        Set<IAttribute> old = getListAttribute();
        attributes.remove(attribute);
        firePropertyChange("AttributesChanged", old, getListAttribute());
    }

    @Override
    public void removeAttribute(IAttribute attribute) {
        Set<IAttribute> old = getListAttribute();
        attributes.remove(attribute);
        data.removeAttribute(attribute);
        firePropertyChange("AttributesChanged", old, getListAttribute());
    }

    @Override
    public void addMethod(IMethod method) throws RefusedAction {
        Set<IMethod> old = getListMethod();
        if (!data.getListMethod().contains(method)) {
            data.addMethod(method);
        }
        methods.add(method);
        firePropertyChange("MethodsChanged", old, getListMethod());
    }

    @Override
    public void hideMethod(IMethod method) {
        Set<IMethod> old = getListMethod();
        methods.remove(method);
        firePropertyChange("MethodsChanged", old, getListMethod());
    }

    @Override
    public void removeMethod(IMethod method) {
        Set<IMethod> old = getListMethod();
        methods.remove(method);
        data.removeMethod(method);
        firePropertyChange("MethodsChanged", old, getListMethod());
    }

    @Override
    public void accept(IClassVisitor visitor) {
        visitor.visit(this);
    }

    // TOOLS

    private void createTransferEventControllers() {
        data.addPropertyChangeListener("EnumFieldAdded", evt -> {
            List<String> newValue = data.getEnumFields();
            List<String> old = new LinkedList<>(newValue);
            old.remove(evt.getNewValue());
            firePropertyChange("EnumFieldsChanged", old, newValue);
        });

        data.addPropertyChangeListener("EnumFieldRemoved", evt -> {
            List<String> newValue = data.getEnumFields();
            List<String> old = new LinkedList<>(newValue);
            old.add((String) evt.getOldValue());
            firePropertyChange("EnumFieldsChanged", old, newValue);
        });

        data.addPropertyChangeListener("AbstractTextAdded", evt -> {
            List<String> newValue = data.getAbstractTexts();
            List<String> old = new LinkedList<>(newValue);
            old.remove(evt.getNewValue());
            firePropertyChange("AbstractTextsChanged", old, newValue);
        });

        data.addPropertyChangeListener("AbstractTextRemoved", evt -> {
            List<String> newValue = data.getAbstractTexts();
            List<String> old = new LinkedList<>(newValue);
            old.add((String) evt.getOldValue());
            firePropertyChange("AbstractTextsChanged", old, newValue);
        });
        data.addPropertyChangeListener("NameChanged", evt -> firePropertyChange("NameChanged", evt.getOldValue(), evt.getNewValue()));
        data.addPropertyChangeListener("VisibilityChanged", evt-> firePropertyChange("VisibilityChanged", evt.getOldValue(), evt.getNewValue()));
        data.addPropertyChangeListener("TypeChanged", evt-> firePropertyChange("TypeChanged", evt.getOldValue(), evt.getNewValue()));
    }
}
