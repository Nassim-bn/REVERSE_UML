package fr.univrouen.umlreverse.model.project;

import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.List;

/**
 * Implementation basic of IVariable
 */
public class Variable implements IVariable {

    /**
     * The name.
     */
    private String name;

    /**
     * The initialization.
     */
    private String initialization;

    /**
     * The type.
     */
    private IType type;

    /**
     * The vetoableSupport
     */
    private VetoableChangeSupport support;

    // CONSTRUCTOR

    /**
     * A variable
     * @param type
     *      the type.
     * @param name
     *      the name.
     * @param initialization
     *      the initialization
     */
    public Variable(final IType type, final String name, final String initialization) {
        Contract.check(type != null, "type can't be null");
        Contract.check(name != null, "name can't be null");
        Contract.check(name.length() > 0, "name can't be empty");

        this.support = new VetoableChangeSupport(this);
        this.type = type;
        this.name = name;
        this.initialization = initialization;

        this.type.addVetoableChangeListener(evt -> {
            IType oldType = getType();
            IType newType;
            switch (evt.getPropertyName()) {
                case "TypeChanged" :
                    newType = new Type((String) evt.getNewValue(), oldType.isConstant(), oldType.getGenericArguments());
                    break;
                case "ConstChanged" :
                    newType = new Type(oldType.getType(), (boolean) evt.getNewValue(), oldType.getGenericArguments());
                    break;
                case "GenericArgumentsChanged" :
                    newType = new Type(oldType.getType(), oldType.isConstant(), (List<IType>)evt.getNewValue());
                    break;
                default :
                    throw new AssertionError("ImpossibleEvent");
            }
            fireVetoableChange("TypeChanged", oldType, newType);
        });
    }
    public Variable(IType type, String name) {
        this(type, name, null);
    }

    // REQUESTS
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append(" : ").append(getType().toString());
        if (getInitialization() != null && getInitialization().length() > 0)
            sb.append(" = ").append(getInitialization());
        return sb.toString();
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public IType getType() {
        return type;
    }

    @Override
    public String getInitialization() {

        return initialization;
    }

    @Override
    public VetoableChangeListener[] getVetoableChangeListeners() {
        return support.getVetoableChangeListeners();
    }

    @Override
    public void setInitialization(String init) {
        try {
            fireVetoableChange("InitChanged", getInitialization(), init);
            initialization = init;
        } catch (PropertyVetoException ignore) {
        }
    }
    @Override
    public void setType(final IType type) throws RefusedAction {
        Contract.check(type != null, "type can't be null");
        try {
            fireVetoableChange("TypeChanged", getType(), type);
            this.type = type;
            this.type.addVetoableChangeListener(evt -> {
                IType oldType = getType();
                switch (evt.getPropertyName()) {
                    case "TypeChanged" :
                        IType newType = new Type((String) evt.getNewValue(), oldType.isConstant(), oldType.getGenericArguments());
                        fireVetoableChange("TypeChanged", oldType, newType);
                        break;
                    case "GenericArgumentsChanged" :
                        newType = new Type(oldType.getType(), oldType.isConstant(), (List<IType>) evt.getNewValue());
                        fireVetoableChange("TypeChanged", oldType, newType);
                        break;
                    default :
                        //ok
                        break;
                }
            });
        } catch (PropertyVetoException e) {
            throw new RefusedAction(ErrorAbstraction.getErrorFromCode(e.getMessage()));
        }
    }

    @Override
    public void setName(String name) throws RefusedAction {
        Contract.check(name != null, "name can't be null");
        Contract.check(name.length() > 0, "name can't be empty");
        try {
            fireVetoableChange("NameChanged", getName(), name);
            this.name = name;
        } catch (PropertyVetoException e) {
            throw new RefusedAction(ErrorAbstraction.getErrorFromCode(e.getMessage()));
        }
    }

    @Override
    public void addVetoableChangeListener(VetoableChangeListener listener) {
        if (listener != null)
        support.addVetoableChangeListener(listener);
    }

    @Override
    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        if (listener != null)
        support.removeVetoableChangeListener(listener);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variable)) return false;

        Variable variable = (Variable) o;

        if (name != null ? !name.equals(variable.name) : variable.name != null) return false;
        if (initialization != null ? !initialization.equals(variable.initialization) : variable.initialization != null)
            return false;
        return type != null ? type.equals(variable.type) : variable.type == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (initialization != null ? initialization.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    // TOOLS

    protected void fireVetoableChange(String name, Object oldValue, Object newValue) throws PropertyVetoException {
        for(VetoableChangeListener listener : support.getVetoableChangeListeners()) {
            listener.vetoableChange(new PropertyChangeEvent(this, name, oldValue, newValue));
        }
    }
}
