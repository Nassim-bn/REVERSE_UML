package fr.univrouen.umlreverse.model.diagram.sequence;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.common.IObservable;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.IStylizable;
import fr.univrouen.umlreverse.model.diagram.sequence.visitor.ISequenceVisitor;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityGraphic;
import fr.univrouen.umlreverse.ui.component.sequence.elements.IActivityGraphicController;

public interface IActivity extends IObservable, IStylizable, Serializable {

	String RELATION_ADDED_PROPERTY_NAME = "RelationAdded";
    String RELATION_REMOVED_PROPERTY_NAME = "RelationRemoved";
    String CREATED_ACTIVITY_PROPERTY_NAME = "CreatedActivity";

    String PARENT_CHANGED_PROPERTY_NAME = "ParentChanged";
    String ACTIVITY_ADDED_PROPERTY_NAME = "ActivityAdded";
    String ACTIVITY_REMOVED_PROPERTY_NAME = "ActivityRemoved";

    String RELATION_ADDED_TO_ACTIVITY_PROPERTY_NAME = "RelationAddedActivity";
    String ACTIVITY_STYLE_ID = "activity";

	/**
	 * Return the object of the activity.
	 */
	IObject getObj();

	boolean isRelSrcDest();

	Map<IObject, IRelation> getMapRelation();

	/**
	 * Return the relation.
	 */
	Set<IRelation> getRelation();

	/**
	 * Return the list of activities of the activity.
	 */
	List<IActivity> getActivity();

	/**
	 * Add a new relation to the list of activity.
	 *
	 * @param
	 *     IRelation relation
	 */
	void addRelation(IRelation relation);

	void setRelSrcDest(boolean rel);

	void addMapRelation(IObject obj, IRelation rel);

	/**
	 * Remove the relation to the list of activity.
	 *
	 * @param
	 *     IRelation relation
	 */
	void removeRelation(IRelation relation);

	/**
	 * Change the object of the activity.
	 *
	 * @param
	 *     IObject obj
	 */
	void setObj(IObject obj);

	/**
	 * Add a new activity into activity.
	 *
	 * @param
	 *     IActivity activity
	 */
	void addActivity(IActivity activity);

	/**
	 * Remove an activity of the activity.
	 *
	 * @param
	 *     IActivity activity
	 */
	void removeActivity(IActivity activity) throws Exception;

	/**
     * accept function that permits to use the visitor.
     *
     * @param
     *     ISequenceVisitor visitor
     */
	void accept(ISequenceVisitor visitor);

	ISequenceDiagram getDiagram();

	Set<IActivity> getActivitiesChilds(Set<IActivity> result);

	IActivity getParent();
	int getLevel();

	void setParent(IActivity father);

	boolean haveChild();

	double getY();


}
