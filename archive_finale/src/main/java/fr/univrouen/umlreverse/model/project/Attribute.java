package fr.univrouen.umlreverse.model.project;

import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;
import java.beans.PropertyVetoException;

/**
 * A basic implementation of IAttribute
 */
public class Attribute extends AField implements IAttribute {

    // ATTRIBUTS

    /**
     * The visibility
     */
    private Visibility visibility = Visibility.Public;

    /**
     * The property
     */
    private String property;

    /**
     * The variable
     */
    private Variable variable;

    // CONSTRUCTOR

    public Attribute(Visibility v, String p, Variable var){
        super();
        this.visibility = v;
        this.property = p;
        this.variable = var;
        this.variable.addVetoableChangeListener(evt -> {
            Variable var2 = (Variable) evt.getSource();
            switch (evt.getPropertyName()) {
                case "TypeChanged" :
                    fireVetoableChange("VariableChanged", getVariable(), new Variable((IType) evt.getNewValue(), var2.getName(),var2.getInitialization()));
                    break;
                case "NameChanged" :
                    fireVetoableChange("VariableChanged", getVariable(), new Variable(var2.getType(), (String) evt.getNewValue() ,var2.getInitialization()));
                    break;
            }
        });
    }

    // REQUESTS

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getVisibility().getSymbol());
        if (!getModifier().isEmpty())
            sb.append('{').append(getModifier()).append('}');
        sb.append(getVariable().toString());
        if (!getProperty().isEmpty())
            sb.append('{').append(getProperty()).append("}");
        return sb.toString();
    }

    @Override
    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    public String getProperty() {
        return property;
    }

    @Override
    public Variable getVariable() {
        return variable;
    }

    // METHODS

    @Override
    public void setVisibility(Visibility visibility) {
        Contract.check(visibility != null);
        this.visibility = visibility;
    }

    @Override
    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public void setVariable(Variable variable) throws RefusedAction {
        try {
            fireVetoableChange("VariableChanged", getVariable(), variable);
            this.variable = variable;
            variable.addVetoableChangeListener(evt -> {
                Variable var = (Variable) evt.getSource();
                switch (evt.getPropertyName()) {
                    case "TypeChanged" :
                        fireVetoableChange("VariableChanged", getVariable(), new Variable((IType) evt.getNewValue(), var.getName(),var.getInitialization()));
                        break;
                    case "NameChanged" :
                        fireVetoableChange("VariableChanged", getVariable(), new Variable(var.getType(), (String) evt.getNewValue() ,var.getInitialization()));
                        break;
                }
            });
        } catch (PropertyVetoException e) {
            throw new RefusedAction(ErrorAbstraction.getErrorFromCode(e.getMessage()));
        }
    }
}
