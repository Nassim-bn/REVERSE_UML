package fr.univrouen.umlreverse.model.project;

import fr.univrouen.umlreverse.util.Contract;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.HashSet;
import java.util.Set;

/**
 * The basic implementation of IField
 */
class AField implements IField {

    // ATTRIBUTES

    /**
     * The set of modifier
     */
    private Set<Modifier> modifiers = new HashSet<>();

    /**
     * The vetoableListener manager
     */
    private VetoableChangeSupport support = new VetoableChangeSupport(this);

    // CONSTRUCTOR

    AField() {
    }

    // REQUESTS
    
    @Override
    public VetoableChangeListener[] getVetoableChangeListeners() {
        return support.getVetoableChangeListeners();
    }

    // METHODS

    @Override
    public Set<Modifier> getModifier() {
        return modifiers;
    }

    @Override
    public void addVetoableChangeListeners(VetoableChangeListener listener) {
        support.addVetoableChangeListener(listener);
    }

    @Override
    public void removeVetoableChangeListeners(VetoableChangeListener listener) {
        support.removeVetoableChangeListener(listener);
    }

    @Override
    public void addModifier(Modifier modifier) {
        Contract.check(modifier != null);
        modifiers.add(modifier);
    }

    @Override
    public void removeModifier(Modifier modifier) {
        Contract.check(modifier != null);
        modifiers.remove(modifier);
    }

    @Override
    public void clearModifier() {
        modifiers.clear();
    }

// TOOLS

    protected void fireVetoableChange(String name, Object oldValue, Object newValue) throws PropertyVetoException {
        for(VetoableChangeListener listener : support.getVetoableChangeListeners()) {
            listener.vetoableChange(new PropertyChangeEvent(this, name, oldValue, newValue));
        }
    }
}
