package fr.univrouen.umlreverse.model.project;

import fr.univrouen.umlreverse.model.diagram.util.CommaArrayList;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;

import java.beans.PropertyVetoException;
import java.util.*;

/**
 * A basic implementation of IMethod
 */
public class Method extends AField implements IMethod  {

    // ATTRIBUTES


    /**
     * The type of return
     */
    private IType returnType;

    /**
     * The arguments
     */
    private List<Argument> arguments = new LinkedList<>();

    /**
     * The name
     */
    private String name;

    /**
     * The property
     */
    private String property;

    /**
     * The visibility
     */
    private Visibility visibility = Visibility.Public;

    // CONSTRUCTOR

    public Method(IType returnType, String name) {
        this(Visibility.Public, returnType, name, null, null);
    }

    public Method(IType returnType, String name, List<Argument> arguments) {
        this(Visibility.Public, returnType, name, arguments, null);
    }

    public Method(Visibility visibility, IType returnType, String name, List<Argument> arguments) {
        this(visibility, returnType, name, arguments, null);
    }

    public Method(Visibility visibility, IType returnType, String name) {
        this(visibility, returnType, name, null, null);
    }

    public Method(Visibility visibility, IType returnType, String name, List<Argument> arguments, String property) {
        super();
        this.visibility = visibility;
        this.returnType = returnType;
        this.name = name;
        if (arguments != null) {
            this.arguments = new LinkedList<>(arguments);
        } else {
            this.arguments = new LinkedList<>();
        }
        this.property = property;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getVisibility().getSymbol());
        if (!getModifier().isEmpty())
            sb.append('{').append(getModifier()).append('}');
        sb.append(getName());
        sb.append('(').append(getArguments().toString()).append(')');
        sb.append(" : ").append(getReturn().toString());
        if (!getProperty().isEmpty())
            sb.append('{').append(getProperty()).append('}');
        return sb.toString();
    }

    @Override
    public IType getReturn() {
        return returnType;
    }

    @Override
    public String getName() {
    	if (name == null) {
    		return "";
    	}
        return name;
    }

    @Override
    public List<Argument> getArguments() {
        return new CommaArrayList<>(arguments);
    }

    @Override
    public String getProperty() {
    	if (property == null) {
    		return "";
    	}
        return property;
    }

    @Override
    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    public void setVisibility(Visibility visibility) {
        Contract.check(visibility != null, "visibility can't be null");
        this.visibility = visibility;
    }

    @Override
    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public void setReturn(final IType type) {
        Contract.check(type != null);
        this.returnType = type;
    }

    @Override
    public void setName(String name) throws RefusedAction {
        if (!name.equals(getName())) {
            try {
                fireVetoableChange(NAME_CHANGED_PROPERTY_NAME, getName(), name);
                this.name = name;
            } catch (PropertyVetoException e) {
                throw new RefusedAction(ErrorAbstraction.getErrorFromCode(e.getMessage()));
            }
        }
    }

    @Override
    public void setArguments(final List<Argument> arguments) throws RefusedAction {
        Contract.check(testArgument(arguments), "All the argument in arguments need to have an unique name.");
        try {
            fireVetoableChange(ARGUMENTS_CHANGE_PROPERTY_NAME, getArguments(), arguments);
            this.arguments.clear();
            for (Argument arg : arguments) {
                this.arguments.add(arg);
                arg.addVetoableChangeListener(evt -> {
                    Argument arg1 = (Argument) evt.getSource();
                    List<Argument> list = new ArrayList<>(arguments);
                    int indice = list.indexOf(arg1);
                    if (indice > 0) {
                        switch (evt.getPropertyName()) {
                            case TYPE_CHANGE_PROPERTY_NAME :
                                list.set(indice, new Argument(arg1.getDirection(), (IType) evt.getNewValue(), arg1.getName(), arg1.getInitialization()));
                                break;
                            case NAME_CHANGED_PROPERTY_NAME :
                                list.set(indice, new Argument(arg1.getDirection(), arg1.getType(), (String) evt.getNewValue(), arg1.getInitialization()));
                                break;
                            default:
                                //ok
                                break;
                        }
                    }
                    if (!testArgument(list)) {
                        throw new PropertyVetoException(ErrorAbstraction.ConflictArgumentInMethod.getCode(), evt);
                    }
                    fireVetoableChange(ARGUMENTS_CHANGE_PROPERTY_NAME, getArguments(), list);
                });
            }
        } catch (PropertyVetoException e) {
            throw new RefusedAction(ErrorAbstraction.ConflictArgumentInMethod);
        }
    }

    // TOOLS

    private static boolean testArgument(List<Argument> arguments) {
        Set<String> names = new HashSet<>();
        for (Argument arg : arguments) {
            if (names.contains(arg.getName())) {
                return false;
            }
            names.add(arg.getName());
        }
        return true;
    }
}
