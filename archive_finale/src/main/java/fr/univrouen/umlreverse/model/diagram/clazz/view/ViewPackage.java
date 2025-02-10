package fr.univrouen.umlreverse.model.diagram.clazz.view;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import fr.univrouen.umlreverse.model.diagram.clazz.visitor.IClassVisitor;
import fr.univrouen.umlreverse.model.diagram.common.Observable;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.diagram.util.Style;
import fr.univrouen.umlreverse.model.project.IAttribute;
import fr.univrouen.umlreverse.model.project.IMethod;
import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.project.Visibility;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;

/**
 * Basic implement to IViewPackage
 */
public class ViewPackage extends Observable implements IViewPackage, IViewEntity {

    // ATTRIBUTES
	/**
     * The original data.
     */
    private IObjectEntity data;

    private final IClassDiagram diagram;
    private IViewPackage parent = null;
    private String name;
    private String nameParameter;
    private final Set<IViewPackage> packages = new HashSet<>();
    private final Set<IViewEntity> entities = new HashSet<>();
    private final Set<String> parameters;
    private String id;

    // CONSTRUCTORS

    public ViewPackage(IObjectEntity data, String name, IClassDiagram diagram) {
        this.diagram = diagram;
        this.name = name;
        this.data = data;
        this.nameParameter = "";
        this.parameters = new HashSet<String>();

        id = UUID.randomUUID().toString();
        boolean b = diagram.addId(id);
        while(!b) {
           id = UUID.randomUUID().toString();
        }
    }

    // REQUESTS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewPackage)) return false;

        ViewPackage that = (ViewPackage) o;
        return getAbsoluteName() != null ? getAbsoluteName().equals(that.getAbsoluteName()) : that.getAbsoluteName() == null;

    }
/*
    @Override
    public int hashCode() {
        return getAbsoluteName() == null ? 0 : getAbsoluteName().hashCode();
    }*/

    @Override
    public IClassDiagram getDiagram() {
        return diagram;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
	public String getStyleId() {
		return PACKAGE_STYLE_ID;
	}

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNameParameter() {
        return nameParameter;
    }

    @Override
    public IViewPackage getPackage() {
        return parent;
    }

    @Override
	public IObjectEntity getData() {
		return data;
	}

    @Override
    public String getAbsoluteName() {
        if (parent != null) {
            return parent.getAbsoluteName() + "." + getName();
        }
        return getName();
    }

    @Override
    public Set<IViewPackage> getPackages() {
        return packages;
    }

    @Override
    public Set<IViewEntity> getEntities() {
        return entities;
    }

    @Override
    public Set<String> getParameters() {
        return parameters;
    }

    // METHODS


    @Override
    public void addEntity(IViewEntity entity) {
        if (!entities.contains(entity)) {
            Set<IViewEntity> old = new HashSet<>(entities);
            entities.add(entity);
            entity.setPackage(this);
            firePropertyChange("ENTITY_ADDED_TO_PACKAGE_PROPERTY_NAME", old, entities);
        }
    }

    @Override
    public void removeEntity(IViewEntity entity) {
    	if (entities.contains(entity)) {
            Set<IViewEntity> old = new HashSet<>(entities);
            entities.remove(entity);
            entity.setPackage(null);
            firePropertyChange("ENTITY_REMOVED_TO_PACKAGE_PROPERTY_NAME", old, entities);
        }
    }

    @Override
    public void addParameter(String parameter) {
        Contract.check(parameter != null);

        if (!parameters.contains(parameter)) {
	        this.parameters.add(parameter);
        }
    }

    @Override
    public void removeParameter(String parameter) {
        Contract.check(parameter != null);

        if (this.parameters.contains(parameter)) {
            this.parameters.remove(parameter);
        }
    }

    @Override
    public void setPackage(IViewPackage parent) {
        if (this.parent != parent) {
            IViewPackage old = this.parent;
            if (parent != null && !parent.getPackages().contains(this)) {
                parent.addEntity(this);
            }
            this.parent = parent;
            firePropertyChange(PARENT_CHANGED_PROPERTY_NAME, old, parent);
        }
    }

    @Override
    public void setName(String namePackage) throws RefusedAction {
        Contract.check(namePackage != null);
        IViewPackage  _pack = getDiagram().getPackages()
        	.stream()
        	.filter(pack -> pack != this && pack.getName() == namePackage)
        	.findFirst().orElse(null);
       if (_pack != null || namePackage == "") {
    	   throw new RefusedAction(ErrorAbstraction.ConflictNamePackageInPackage);
       }
        
        String old = name;
        if (!name.equals(namePackage)) {
            if (parent != null) {
                for (IViewPackage p : parent.getPackages()) {
                    if (p.getName().equals(namePackage)) {
                    // TODO    throw new PropertyVetoException("0x0001", null);
                    }
                }
            }
            this.name = namePackage;
            firePropertyChange(NAME_CHANGED_PROPERTY_NAME, old, name);
        }
    }

    @Override
    public void setParameterName(String name) {
        Contract.check(name != null);
        String old = nameParameter;
        this.nameParameter = name;
        firePropertyChange(PARAMETER_CHANGED_PROPERTY_NAME, old, nameParameter);
    }


    @Override
    public IStyle getStyle() {
        IStyle style = new Style();
        style.putAll(diagram.getStyle("*", "*"));
        style.putAll(diagram.getStyle(PACKAGE_STYLE_ID, "*"));
        style.putAll(diagram.getStyle(PACKAGE_STYLE_ID, getId()));
        return style;
    }

    @Override
    public void addAllStyle(Map<String, String> keyValue) {
        Contract.check(keyValue != null);
        for (String key : keyValue.keySet()) {
            diagram.getStyle().addStyle(STYLE_CHANGED_PROPERTY_NAME, getId(), key, keyValue.get(key));
        }
    }

    @Override
    public void addStyle(String key, String value) {
        IStyle old = getStyle();
        diagram.getStyle().addStyle(PACKAGE_STYLE_ID, getId(), key, value);
        firePropertyChange(STYLE_CHANGED_PROPERTY_NAME, old, getStyle());
    }

    @Override
    public void removeStyle(String key) {
        IStyle old = getStyle();
        firePropertyChange(STYLE_CHANGED_PROPERTY_NAME, old, getStyle());
    }

    @Override
    public void clearStyle() {
        IStyle old = getStyle();
        diagram.getStyle().removeStyle(PACKAGE_STYLE_ID, getId());
        firePropertyChange(STYLE_CHANGED_PROPERTY_NAME, old, getStyle());
    }

    // TOOLS

    @Override
    public void accept(IClassVisitor visitor) {
        visitor.visit(this);
    }

	@Override
	public List<String> getAbstractTexts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getEnumFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeEntity getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Visibility getVisibility() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IAttribute> getListAttribute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IMethod> getListMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setType(TypeEntity type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addEnumField(String field) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeEnumField(String field) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAbstractText(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideAbstractText(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAbstractText(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVisibility(Visibility visibility) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAttribute(IAttribute attribute) throws RefusedAction {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideAttribute(IAttribute attribute) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAttribute(IAttribute attribute) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addMethod(IMethod method) throws RefusedAction {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideMethod(IMethod method) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeMethod(IMethod method) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDiagram(IClassDiagram diagram) throws RefusedAction {
		// TODO Auto-generated method stub

	}
}
