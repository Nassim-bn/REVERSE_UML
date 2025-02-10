package fr.univrouen.umlreverse.model.diagram.sequence;


import java.util.Map;

import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.project.Argument;
import fr.univrouen.umlreverse.model.project.IMethod;

/**
 * A relation between to object
 * looks like [garde]message[(arguments...)][:return]
 * */
public interface IRelationToObject extends IRelation, IEntityRelation {


	String TEXT_CHANGE_PROPERTY_NAME = "TextChanged";


	String ACTIVITY_SRC_CHANGE_PROPERTY_NAME = "AcitivitySrcChanged";
	String ACTIVITY_DST_CHANGE_PROPERTY_NAME = "AcitivityDstChanged";
	
	/**
	 * return the method associate to the relation
	 * */
	 IMethod getMethod();
	 void setMethod(IMethod method);

	 String getGarde();
	 void setGarde(String garde);

	 String getReturn();
	 void setReturn(String returnText);

	 Map<Argument, String> getArguments();
	 void setArguments(Map<Argument, String> arguments);

	 boolean hasArguments();
	 void setHasArguments(boolean hasArguments);


	/**
	 * return [garde]message[(arguments...)][:return] according to hasGarde, hasArguments, hasReturn
	 * */
	String toString();

	/**
	 * return arguments from getArguments according to the order in Method.getArguments
	 * */
	String toStringOrderedArgument();
	ISegment getSegmentContainer();
	void setSegmentContainer(ISegment segment);
	IActivity getActivityContainer();
	void setActivityContainer(IActivity activity);
	IActivity getActivityDstContainer();
	void setActivityDstContainer(IActivity activity);
}
