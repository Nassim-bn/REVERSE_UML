package fr.univrouen.umlreverse.model.project;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import fr.univrouen.umlreverse.model.diagram.util.CommaArrayList;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;

/**
 * Basic implement of IType.
 */
public class Type implements IType {

    public static IType getTypefromString(String str) {
        str = str.trim();
        if (str.length() <= 0) {
            return null;
        }

        boolean isconst = str.startsWith("const ");
        String name;
        boolean isGeneric = str.contains("<");
        List<IType> generic = new LinkedList<>();


        // const
        if (isconst) {
            str = str.substring(6).trim();
        }


        // name
        if (!isGeneric) {
            name = str;
        } else {
            int beginGenericity = str.indexOf("<");
            int endGenericity = str.lastIndexOf(">");
            if (endGenericity != str.length() - 1) {
                return null;
            }
            name = str.substring(0, beginGenericity).trim();
            str = str.substring(beginGenericity + 1, endGenericity);
            // generic

            StringTokenizer tokenizer = new StringTokenizer(str, ",");
            while (tokenizer.hasMoreTokens()) {
                String next = tokenizer.nextToken().trim();
                while (next.contains("<") && !next.contains(">")) {
                    if (!tokenizer.hasMoreTokens()) return null;
                    next += "," + tokenizer.nextToken();
                }
                IType arg = getTypefromString(next.trim());
                if (arg == null) {
                    return null;
                }
                generic.add(arg);
            }
        }
        if (name.equals("const") || name.length() <= 0 || (isGeneric && generic.isEmpty())) {
            return null;
        }
        return new Type(name, isconst, generic);
    }

    // ATTRIBUTES

    /**
     * The basic type.
     */
    private String type;

    /**
     * The generic arguments.
     */
    private List<IType> genericType;

    /**
     * Flag to determine if this is constant.
     */
    private boolean isConstant;

    /**
     * The listeners register.
     */
    private VetoableChangeSupport support;


    // CONSTRUCTOR

    public Type(String type, boolean isConst, List<IType> genericArguments) {
        Contract.check(type != null, "type can't be null");
        Contract.check(type.length() > 0, "type can't be empty");

        this.support = new VetoableChangeSupport(this);
        this.genericType = new CommaArrayList<>();
        this.type = type;
        this.isConstant = isConst;
        if (genericArguments != null) {
            for (IType t : genericArguments) {
                genericType.add(t);
                t.addVetoableChangeListener(new VetoableChangeListener() {
                    @Override
                    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
                        IType newType;
                        switch (evt.getPropertyName()) {
                            case "TypeChanged" :
                                newType = new Type((String) evt.getNewValue(), isConstant(), getGenericArguments());
                                break;
                            case "ConstChanged" :
                                newType = new Type(getType(), (boolean) evt.getNewValue(), getGenericArguments());
                                break;
                            case "GenericArgumentsChanged" :
                                newType = new Type(getType(), isConstant(), (List<IType>)evt.getNewValue());
                                break;
                            default :
                                throw new AssertionError("ImpossibleEvent");
                        }
                        fireVetoableChange("TypeChanged", this, newType);

                    }
                });
            }
        }
    }

    public Type(String type, boolean isConst) {
        this(type, isConst, null);
    }

    public Type(String type) {this(type, false, null);    }


    // REQUESTS

    @Override
    public boolean isConstant() {
        return isConstant;
    }

    @Override
    public List<IType> getGenericArguments() {
        return new CommaArrayList<>(genericType);
    }

    @Override
    public boolean isGeneric() {
        return getGenericArguments().size() > 0;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public VetoableChangeListener[] getVetoableChangeListeners() {
        return support.getVetoableChangeListeners();
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(isConstant() ? "const " : "");
        builder.append(type);
        List<IType> arguments = getGenericArguments();
        if (!arguments.isEmpty()) {
            builder.append('<').append(arguments.toString()).append('>');
        }
        /*if (arguments.size() != 0) {
            Iterator<IType> iterator = arguments.iterator();
            builder.append(" <").append(iterator.next().toString());
            while(iterator.hasNext()) {
                builder.append(", ").append(iterator.next().toString());
            }
            builder.append(">");
        }*/
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Type)) return false;

        Type type1 = (Type) o;

        return (isConstant() == type1.isConstant())
                && (getType().equals(type1.getType()))
                && genericType.equals(type1.genericType);
    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + genericType.hashCode();
        result = 31 * result + (isConstant() ? 1 : 0);
        return result;
    }

    // METHODS

    @Override
    public void setGenericArguments(List<IType> arguments) throws RefusedAction {
        if (arguments == null) {
            arguments = new LinkedList<>();
        }
        if (!arguments.equals(getGenericArguments())) {
            try {
                fireVetoableChange("GenericArgumentsChanged", this.getGenericArguments(), arguments);
                genericType.clear();
                for (IType type : arguments) {
                    genericType.add(type);
                    type.addVetoableChangeListener(new VetoableChangeListener() {
                        @Override
                        public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
                            IType newType;
                            switch (evt.getPropertyName()) {
                                case "TypeChanged" :
                                    newType = new Type((String) evt.getNewValue(), isConstant(), getGenericArguments());
                                    break;
                                case "ConstChanged" :
                                    newType = new Type(getType(), (boolean) evt.getNewValue(), getGenericArguments());
                                    break;
                                case "GenericArgumentsChanged" :
                                    newType = new Type(getType(), isConstant(), (List<IType>)evt.getNewValue());
                                    break;
                                default :
                                    throw new AssertionError("ImpossibleEvent");
                            }
                            fireVetoableChange("TypeChanged", this, newType);

                        }
                    });
                }
            } catch (PropertyVetoException e) {
                throw new RefusedAction(ErrorAbstraction.getErrorFromCode(e.getMessage()));
            }
        }
    }

    @Override
    public void setType(String type) throws RefusedAction {
        Contract.check(type != null, "type can't be null");
        Contract.check(type.length() > 0, "type can't be empty");
        if (!this.type.equals(type)) {
            try {
                fireVetoableChange("TypeChanged", getType(), type);
                this.type = type;
            } catch (PropertyVetoException e) {
                throw new RefusedAction(ErrorAbstraction.getErrorFromCode(e.getMessage()));
            }
        }
    }

    @Override
    public void setConstant(boolean isConst) {
        if (isConst != this.isConstant) {
            this.isConstant = isConst;
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


    // TOOLS

    protected void fireVetoableChange(String name, Object oldValue, Object newValue) throws PropertyVetoException {
        for(VetoableChangeListener listener : support.getVetoableChangeListeners()) {
            listener.vetoableChange(new PropertyChangeEvent(this, name, oldValue, newValue));
        }
    }


}
