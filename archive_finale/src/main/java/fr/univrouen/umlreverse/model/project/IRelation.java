
package fr.univrouen.umlreverse.model.project;

import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewRelation;

/**
 *
 * IRelation interface is the interface of relation
 * version 1.0
 * Date 16/02/2016
 *  
 */
public interface IRelation extends Serializable {

	/**
	 * The id.
	 */
	String getId();

	/**
	 * getter of getEntitySource
	 * @return the entitySource
	 */
	IObjectEntity getEntitySource();
	
	/**
	 * setter of getEntitySource
	 * @param entitySource the entitySource to set
	 */
	void setEntitySource(IObjectEntity entitySource);
	
	/**
	 * getter of entityTarget
	 * @return the entityTarget
	 */
	IObjectEntity getEntityTarget();
	
	/**
	 * setter of entityTarget
	 * @param entityTarget the entityTarget to set
	 */
	void setEntityTarget(IObjectEntity entityTarget);
	
	/**
	 * getter of nameRelation
	 * @return the nameRelation
	 */
	String getNameRelation();
	
	/**
	 * setter of nameRelation
	 * @param nameRelation the nameRelation to set
	 */
	void setNameRelation(String nameRelation);
	
	/**
	 * getter of headCardinality
	 * @return the headCardinality
	 */
	String getHeadCardinality();
	
	/**
	 * setter of headCardinality
	 * @param headCardinality the headCardinality to set
	 */
	void setHeadCardinality(String headCardinality);
	
	/**
	 * getter of getTailCardinality
	 * @return the tailCardinality
	 */
	String getTailCardinality();
	
	/**
	 * setter of getTailCardinality
	 * @param tailCardinality the tailCardinality to set
	 */
	void setTailCardinality(String tailCardinality);
	
	/**
	 * getter of headComment
	 * @return the headComment
	 */
	String getHeadComment();
	
	/**
	 * setter of headComment
	 * @param headComment the headComment to set
	 */
	void setHeadComment(String headComment);
	
	/**
	 * getter of tailComment
	 * @return the tailComment
	 */
	String getTailComment();
	
	/**
	 * setter of tailComment
	 * @param tailComment the tailComment to set
	 */
	void setTailComment(String tailComment);
	
	/**
	 * getter of arrowHead
	 * @return the arrowHead
	 */
	TypeHeadArrow getArrowHead();
	
	/**
	 * setter of arrowHead
	 * @param arrowHead the arrowHead to set
	 */
	void setArrowHead(TypeHeadArrow arrowHead);
	
	/**
	 * getter of arrowTail
	 * @return the arrowTail
	 */
	TypeHeadArrow getArrowTail();
	
	/**
	 * setter of arrowTail
	 * @param arrowTail the arrowTail to set
	 */
	void setArrowTail(TypeHeadArrow arrowTail);
	
	/**
	 * getter of lineArrow
	 * @return the lineArrow
	 */
	TypeLineArrow getLineArrow();
	
	/**
	 * setter of lineArrow
	 * @param lineArrow the lineArrow to set
	 */
	void setLineArrow(TypeLineArrow lineArrow);
	
	/**
	 * getter of associationEntity
	 * @return the associationEntity
	 */
	IObjectEntity getAssociationEntity();
	
	/**
	 * setter of associationEntity
	 * @param associationEntity the associationEntity to set
	 */
	void setAssociationEntity(IObjectEntity associationEntity);

	/**
	 * the list of the view of this
     */
	Set<IViewRelation> getViews();

	/**
	 * Add a view on this
	 */
	void addView(IViewRelation relation);

	/**
	 * Remove a view
     */
	void removeView(IViewRelation relation);

	void addPropertyChangeListeners(String property, PropertyChangeListener listener);

	void addPropertyChangeListeners(PropertyChangeListener listener);

	void removePropertyChangeListeners(String property, PropertyChangeListener listener);

	void removePropertyChangeListeners(PropertyChangeListener listener);
}
