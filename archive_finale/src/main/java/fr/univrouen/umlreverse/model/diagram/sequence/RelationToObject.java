package fr.univrouen.umlreverse.model.diagram.sequence;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeListenerProxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.common.IDiagramCommon;
import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.Relation;
import fr.univrouen.umlreverse.model.project.Argument;
import fr.univrouen.umlreverse.model.project.IMethod;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.util.Contract;

public class RelationToObject extends Relation implements IRelationToObject {

	/**
	 *
	 */
	private static final long serialVersionUID = 8938189811880630386L;




	protected final Map<IEntityRelation, IRelation> relations = new HashMap<>();

	private IMethod method;
	private Map<Argument, String> arguments;
	private String garde;
	private String returnText;
	private boolean hasArguments;
	private ISegment segmentContainer;
	private IActivity activityContainer;
	private IActivity activityDstContainer;



	public RelationToObject(IDiagramCommon diagram, IEntityRelation source, IEntityRelation target) {
		super(diagram, source, target);

		arguments = new HashMap<>();
		garde = "";
		returnText = "";
		setType(RelationTypeEnum.SYNCHRONE);
	}

	@Override
	public IMethod getMethod() {
		return method;
	}



	@Override
	public void setMethod(IMethod method) {
		this.method = method;

		method.addVetoableChangeListeners(
				new VetoableChangeListenerProxy(IMethod.ARGUMENTS_CHANGE_PROPERTY_NAME, new VetoableChangeListener() {

					@Override
					public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
						setHasArguments(false);
					}
				}
			)
		);

		getArguments().clear();

		firePropertyChange(TEXT_CHANGE_PROPERTY_NAME, "", toString());
	}



	@Override
	public Map<Argument, String> getArguments() {
		return arguments;
	}



	@Override
	public void setArguments(Map<Argument, String> arguments) {
		this.arguments = arguments;

		firePropertyChange(TEXT_CHANGE_PROPERTY_NAME, "", toString());
	}



	@Override
	public String getGarde() {
		return garde;
	}



	@Override
	public void setGarde(String garde) {
		String old = toString();
		if (garde == null) {
			garde = "";
		}
		this.garde = garde;

		firePropertyChange(TEXT_CHANGE_PROPERTY_NAME, old, toString());
	}



	@Override
	public String getReturn() {
		return returnText;
	}



	@Override
	public void setReturn(String returnText) {
		if (returnText == null) {
			returnText = "";
		}

		this.returnText = returnText;

		firePropertyChange(TEXT_CHANGE_PROPERTY_NAME, "", toString());
	}




	@Override
	public boolean hasArguments() {
		return hasArguments;
	}



	@Override
	public void setHasArguments(boolean hasArguments) {
		this.hasArguments = hasArguments;

		firePropertyChange(TEXT_CHANGE_PROPERTY_NAME, "", toString());
	}

	public String toString() {
		String methodName;
		if (method == null) {
			methodName = "";
		} else {
			methodName =  method.getName();
		}
		return (!getGarde().equals("") ? "[" + getGarde() + "]" : "")
				+ methodName
				+ (hasArguments ? toStringOrderedArgument() : "")
				+ (getReturn().length() != 0 ? ":": "") + getReturn();
	}

	@Override
	public String toStringOrderedArgument() {
		String result = "(";
		int nbArg = 0;
		if (method != null) {
			for (Argument arg: method.getArguments()) {
				if (nbArg != 0) {
					result += ", ";
				}
				String argValue = getArguments().get(arg);
				if (argValue == null) {
					argValue = "null";
				}
				if (argValue.length() == 0) {
					argValue = "null";
				}
				result += argValue;
				nbArg++;
			}
		}


		return result + ')';
	}

	// REQUESTS

    @Override
    public IRelation getRelation(IEntityRelation entity) {
        Contract.check(entity != null, "entity must not be null.");
        return relations.get(entity);
    }

    @Override
    public Set<IRelation> getRelations() {
        Set<IRelation> res = new HashSet<>(relations.values());
        return res;
    }

    @Override
    public ISegment getSegmentContainer() {
    	return segmentContainer;
    }

    @Override
    public IActivity getActivityContainer() {
    	return activityContainer;
    }
// COMMANDS

    @Override
    public void setSegmentContainer(ISegment segment) {
    	segmentContainer = segment;
    }

    @Override
    public void setActivityContainer(IActivity activity) {
    	activityContainer = activity;
    	
    	firePropertyChange(ACTIVITY_SRC_CHANGE_PROPERTY_NAME, null, activityContainer);
    }

    @Override
    public IRelation addRelation(IEntityRelation entity) {
        Contract.check(entity != null, "entity must not be null.");
        IRelation relation = new Relation(getDiagram(), this, entity);
        relations.put(entity, relation);
        entity.addRelationDst(relation, entity);
        getDiagram().addRelation(relation);
        firePropertyChange(RELATION_ADDED_PROPERTY_NAME, null, relation);
        return relation;
    }

    /**
     * addRelationDst allows to add relation into the map of relations
     * @param relation
     * @param entity
     */
    public void addRelationDst(IRelation relation, IEntityRelation entity) {
        Contract.check(entity != null, "entity must not be null.");
        relations.put(entity, relation);
    }

    @Override
    public void removeRelation(IEntityRelation entity) {
        Contract.check(entity != null, "entity must not be null.");
        IRelation relation = relations.get(entity);
        relations.remove(entity);
        entity.removeRelationDst(entity);
        getDiagram().removeRelation(relation);
        firePropertyChange(RELATION_REMOVED_PROPERTY_NAME, relation, null);
    }

    @Override
    public void removeRelationDst(IEntityRelation entity) {
        Contract.check(entity != null, "entity must not be null.");
        relations.remove(entity);
    }

	@Override
	public IActivity getActivityDstContainer() {
		return activityDstContainer;
	}

	@Override
	public void setActivityDstContainer(IActivity activity) {
		activityDstContainer = activity;
		
		firePropertyChange(ACTIVITY_DST_CHANGE_PROPERTY_NAME, null, activityDstContainer);
	}

}
