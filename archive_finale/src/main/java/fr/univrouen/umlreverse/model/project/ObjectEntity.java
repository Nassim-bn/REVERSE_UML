
package fr.univrouen.umlreverse.model.project;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ObjectEntity class represents classes, enumerations, interfaces & abstract classes
 */

public class ObjectEntity implements IObjectEntity {

	// ATTRIBUTES

	private String nameEntity;
	private TypeEntity type ;
	private Visibility visibility;
	private Set<IAttribute> listAttribute = new HashSet<>();
	private Set<IMethod> listMethod = new HashSet<>();
	private Set<IViewEntity> listViewer = new HashSet<>();
	private IPackage parent = null;
	private List<String> abstractTexts = new ArrayList<>();
	private List<String> enumFields = new LinkedList<>();
	/**
	 * Support for PropertyChangeListeners
	 */
	private PropertyChangeSupport support = new PropertyChangeSupport(this);

	// CONSTRUCTOR

	/**
	 * Constructor
	 *
	 * @param nameEntity
	 * 	the name of the entity
	 * @param type
	 * 	the type of entity
	 * @param visibility
	 * 	the visibility
	 */
	public ObjectEntity(String nameEntity, TypeEntity type, Visibility visibility) {
        Contract.check(nameEntity != null && nameEntity.length()>0);
        Contract.check(type != null);
        Contract.check(visibility != null);

		this.nameEntity = nameEntity;
		this.type = type;
		this.visibility = visibility;
	}

	// REQUESTS

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return support.getPropertyChangeListeners();
    }

	@Override
	public IPackage getPackage() {
		return parent;
	}

	@Override
	public String getAbsoluteName() {
		String parent = "";
		if (getPackage() != null) {
			parent = getPackage().getAbsoluteName();
		}
		if (parent.length() > 0) {
			return parent + "." + getName();
		} else {
			return getName();
		}
	}

	@Override
	public String getName() {
		return nameEntity;
	}

	@Override
	public List<String> getAbstractTexts() {
		return abstractTexts;
	}

    @Override
    public Set<IViewEntity> getListViewer() {
        return listViewer;
    }

	@Override
	public List<String> getEnumFields() {
		return enumFields;
	}

	@Override
	public TypeEntity getType() {
		return type;
	}

	@Override
	public Visibility getVisibility() {
		return visibility;
	}

	@Override
	public Set<IAttribute> getListAttribute() {
		return listAttribute;
	}

	@Override
	public Set<IMethod> getListMethod() {
		return listMethod;
	}

	// METHODS

	@Override
	public void addEnumField(String field) {
		Contract.check(field != null && field.length() > 0, "field can't be null or empty");
		Contract.check(!enumFields.contains(field) , "field is already in getEnumFields()");
        if (!enumFields.contains(field)) {
            enumFields.add(field);
            firePropertyChanged("EnumFieldAdded", null, field);
        }
	}

	@Override
	public void removeEnumField(String field) {
		if (enumFields.contains(field)) {
			enumFields.remove(field);
			for (IViewEntity view : getListViewer()) {
				view.removeEnumField(field);
			}
            firePropertyChanged("EnumFieldRemoved", field, null);
		}
	}

	@Override
	public void addAbstractText(String text) {
		Contract.check(text != null && text.length() > 0, "text can't be null or empty");
		abstractTexts.add(text);
        firePropertyChanged("AbstractTextAdded", null, text);
	}

	@Override
	public void removeAbstractText(String text) {
		if (abstractTexts.contains(text)) {
			abstractTexts.remove(text);
			getListViewer().stream().filter(view -> view.getAbstractTexts().contains(text)).forEach(view -> view.removeAbstractText(text));
		    firePropertyChanged("AbstractTextRemoved", text, null);
        }
	}

	@Override
	public void setPackage(IPackage parent) throws RefusedAction {
		Map<IViewEntity, IStyle> viewToStyle = new HashMap<>();
        if ((this.parent != null && !this.parent.equals(parent)) || (this.parent == null && parent != null)) {
            IPackage old = getPackage();
            for (IViewEntity view : getListViewer()) {
                viewToStyle.put(view, view.getDiagram().getStyle().getStyle(getType().toString(), getAbsoluteName()));
            }
            if(parent == null) {
                this.parent = null;
            } else {
                if (parent != getPackage()) {
                    for (IObjectEntity entity : parent.getListEntities()) {
                        if (entity.getName().equals(getName()) && entity != this) {
                            throw new RefusedAction(ErrorAbstraction.ConflictNameEntitiesInPackage);
                        }
                    }
                    this.parent = parent;
                }
            }
            for (IViewEntity view : viewToStyle.keySet()) {
                view.getDiagram().getStyle().addStyle("entity", getAbsoluteName(), viewToStyle.get(view));
            }
            firePropertyChanged("ParentChanged", old, parent);
        }
	}

	@Override
	public void setName(String nameEntity) throws RefusedAction {
		Contract.check(nameEntity != null && nameEntity.length() > 0);

		if (!nameEntity.equals(getName())) {
			if (parent != null) {
				for (IObjectEntity entity : parent.getListEntities()) {
					if (entity.getName().equals(nameEntity) && entity != this) {
						throw new RefusedAction(ErrorAbstraction.ConflictNameEntitiesInPackage);
					}
				}
			}

			// extraction of view's styles
			Map<IViewEntity, IStyle> styleFromView = new HashMap<>();
			for (IViewEntity view : listViewer) {
				styleFromView.put(view,view.getDiagram().getStyle().getStyle("entity", getAbsoluteName()));

			}

			// update
            String old = getName();
            this.nameEntity = nameEntity;

			// set of old style
			for (IViewEntity view : styleFromView.keySet()) {
				if (styleFromView.get(view) != null) {
					view.getDiagram().getStyle().addStyle("entity", getAbsoluteName(), styleFromView.get(view));
				}
			}

			// notify
            firePropertyChanged("NameChanged", old, nameEntity);
		}
	}

	@Override
	public void setVisibility(Visibility visibility) {
		Contract.check(visibility != null);
        if (this.visibility != visibility) {
            Visibility old = this.visibility;
            this.visibility = visibility;
            firePropertyChanged("VisibilityChanged", old, visibility);
        }
	}

	@Override
	public void setType(TypeEntity type) {
		Contract.check(type != null);
		if (type != getType()) {
            TypeEntity old = this.type;
			this.type = type;
            firePropertyChanged("TypeChanged", old, type);
		}
	}

	@Override
	public void addAttribute(IAttribute attribute) throws RefusedAction {
		Contract.check(attribute != null);
		if (!listAttribute.contains(attribute)) {
            for (IAttribute attr : listAttribute) {
                if (attr.getVariable().getName().equals(attribute.getVariable().getName())) {
                    throw new RefusedAction(ErrorAbstraction.ConflictAttributeInEntity);
                }
            }
			listAttribute.add(attribute);
			attribute.addVetoableChangeListeners(evt -> {
                switch(evt.getPropertyName()) {
                    case "VariableChanged" :
						IVariable oldVariable = ((IVariable) evt.getOldValue());
						IVariable newVariable = ((IVariable) evt.getNewValue());
                        if (!newVariable.getName().equals(oldVariable.getName())) {
							for (IAttribute tmp : getListAttribute()) {
								if (tmp != evt.getSource() && tmp.getVariable().getName().equals(newVariable.getName())) {
									throw new PropertyVetoException(ErrorAbstraction.ConflictAttributeInEntity.getCode(), evt);
								}
							}
                        }
                        break;
                    default :
                }
            });
            firePropertyChanged("AttributeAdded", null, attribute);
		}
	}

	@Override
	public void removeAttribute(IAttribute attribute){
		if (listAttribute.contains(attribute)) {
			listAttribute.remove(attribute);
			for (IViewEntity entity : listViewer) {
				entity.removeAttribute(attribute);
			}
            firePropertyChanged("AttributeRemoved", attribute, null);
		}
	}

	@Override
	public void addMethod(IMethod method) throws RefusedAction {
        Contract.check(method != null);

        if (!listMethod.contains(method)) {
			for (IMethod met : listMethod) {
                if (equalsSignature(method.getName(), method.getArguments(), met.getName(), met.getArguments())) {
					throw new RefusedAction(ErrorAbstraction.ConflictSignatureMethodInEntity);
                }

            }
            listMethod.add(method);
            method.addVetoableChangeListeners(evt -> {
                IMethod oldMethod = (IMethod) evt.getSource();
                switch (evt.getPropertyName()) {
                    case "ArgumentsChanged" :
                        for (IMethod method1 : listMethod) {
                            if (method1 != oldMethod) {
								if (equalsSignature(oldMethod.getName(), (List<Argument>)evt.getNewValue(), method1.getName(), method1.getArguments())) {
									throw new PropertyVetoException(
											ErrorAbstraction.ConflictSignatureMethodInEntity.getCode()
											, new PropertyChangeEvent(this, "MethodChanged", method,
													new Method(method.getVisibility(), method.getReturn(), method.getName(), (List<Argument>) evt.getNewValue(), method.getProperty())
											)
									);
								}
							}
                        }
                        break;
                    case "NameChanged" :
                        for (IMethod method1 : listMethod) {
                            if (method1 != evt.getSource()) {
                                if (equalsSignature((String) evt.getNewValue(), oldMethod.getArguments(), method1.getName(), method1.getArguments())) {
									throw new PropertyVetoException(ErrorAbstraction.ConflictSignatureMethodInEntity.getCode(),new PropertyChangeEvent(this, "MethodChanged", method, new Method(method.getVisibility(), method.getReturn(), (String) evt.getNewValue(), method.getArguments(), method.getProperty())));
                                }
                            }
                        }
                        break;
                    default :
                        // ok
                        break;
                }
            });
            firePropertyChanged("MethodAdded", null, method);
        }
	}

	@Override
	public void removeMethod(IMethod method){
		if (listMethod.contains(method)) {
			listMethod.remove(method);
			for (IViewEntity entity : listViewer) {
				entity.removeMethod(method);
			}
            firePropertyChanged("MethodRemoved", method, null);
		}
	}

	@Override
	public void addViewer(IViewEntity view){
		listViewer.add(view);
		abstractTexts.addAll(view.getAbstractTexts().stream().filter(abstractText -> !getAbstractTexts().contains(abstractText)).collect(Collectors.toList()));
		listAttribute.addAll(view.getListAttribute().stream().filter(attribute -> !getListAttribute().contains(attribute)).collect(Collectors.toList()));
		listMethod.addAll(view.getListMethod().stream().filter(method -> !getListMethod().contains(method)).collect(Collectors.toList()));
	    firePropertyChanged("ViewerAdded", null, view);
    }

	@Override
	public void removeViewer(IViewEntity view){
		listViewer.remove(view);
	}

    protected void firePropertyChanged(String property, Object oldValue, Object newValue) {
        support.firePropertyChange(property, oldValue, newValue);
    }

	// TOOLS

	private static boolean equalsSignature(String name1, List<Argument> arg1, String name2, List<Argument> arg2) {
		if (name1.equals(name2)) {
			if (arg1.size() == arg2.size()) {
				Iterator<Argument> it1 = arg1.iterator();
				Iterator<Argument> it2 = arg2.iterator();
				while(it1.hasNext()) {
					if (!it1.next().getType().getType().equals(it2.next().getType().getType())) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void addPropertyChangeListener(String name, PropertyChangeListener l) {
		support.addPropertyChangeListener(name, l);
	}
	@Override
	public void removePropertyChangeListener(String name, PropertyChangeListener l) {
		support.removePropertyChangeListener(name, l);
	}
	@Override
	public void addPropertyChangeListener(PropertyChangeListener l) {
		support.addPropertyChangeListener(l);
	}
	@Override
	public void removePropertyChangeListener(PropertyChangeListener l) {
		support.removePropertyChangeListener(l);
	}

}
