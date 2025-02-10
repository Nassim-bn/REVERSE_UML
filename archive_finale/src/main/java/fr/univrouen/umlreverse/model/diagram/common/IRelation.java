package fr.univrouen.umlreverse.model.diagram.common;

import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;

/**
 * An interface for all relations.
 */
public interface IRelation extends IEntity {

// ATTRIBUTES
	String SOURCE_CHANGED_PROPERTY_NAME = "SourceChanged";
    String TARGET_CHANGED_PROPERTY_NAME = "TargetChanged";
    String NAME_CHANGED_PROPERTY_NAME = "NameChanged";

    String HEAD_CARDI_CHANGED_PROPERTY_NAME = "HeadCardinalityChanged";
    String TAIL_CARDI_CHANGED_PROPERTY_NAME = "TailCardinalityChanged";
    String HEAD_COM_CHANGED_PROPERTY_NAME = "HeadCommentChanged";
    String TAIL_COM_CHANGED_PROPERTY_NAME = "TailCommentChanged";

    String HEAD_CHANGED_PROPERTY_NAME = "ArrowHeadChanged";
    String TAIL_CHANGED_PROPERTY_NAME = "ArrowTailChanged";
    String LINE_CHANGED_PROPERTY_NAME = "LineArrowChanged";
    String TYPE_CHANGE_PROPERTY_NAME = "TypeChanged";

    // REQUESTS
	/**
	 * getter of arrowHead
	 * @return the arrowHead
	 */
	TypeHeadArrow getArrowHead();

	/**
	 * getter of arrowTail
	 * @return the arrowTail
	 */
	TypeHeadArrow getArrowTail();

	/**
	 * getter of getEntitySource
	 * @return the entitySource
	 */
	IEntityRelation getEntitySource();

	/**
	 * getter of entityTarget
	 * @return the entityTarget
	 */
	IEntityRelation getEntityTarget();

	/**
	 * getter of headCardinality
	 * @return the headCardinality
	 */
	String getHeadCardinality();

	/**
	 * getter of headComment
	 * @return the headComment
	 */
	String getHeadComment();

	/**
	 * The id.
	 */
	String getId();

	/**
	 * getter of lineArrow
	 * @return the lineArrow
	 */
	TypeLineArrow getLineArrow();

	/**
	 * getter of nameRelation
	 * @return the nameRelation
	 */
	String getNameRelation();

	/**
	 * getter of getTailCardinality
	 * @return the tailCardinality
	 */
	String getTailCardinality();

	/**
	 * getter of tailComment
	 * @return the tailComment
	 */
	String getTailComment();


	RelationTypeEnum getType();

	double getY();

// COMMANDS
	/**
	 * setter of arrowHead
	 * @param arrowHead the arrowHead to set
	 */
	void setArrowHead(TypeHeadArrow arrowHead);

	/**
	 * setter of arrowTail
	 * @param arrowTail the arrowTail to set
	 */
	void setArrowTail(TypeHeadArrow arrowTail);

	/**
	 * setter of getEntitySource
	 * @param entitySource the entitySource to set
	 */
	void setEntitySource(IEntityRelation entitySource);

	/**
	 * setter of entityTarget
	 * @param entityTarget the entityTarget to set
	 */
	void setEntityTarget(IEntityRelation entityTarget);

	/**
	 * setter of lineArrow
	 * @param lineArrow the lineArrow to set
	 */
	void setLineArrow(TypeLineArrow lineArrow);

	/**
	 * setter of headCardinality
	 * @param headCardinality the headCardinality to set
	 */
	void setHeadCardinality(String headCardinality);

	/**
	 * setter of headComment
	 * @param headComment the headComment to set
	 */
	void setHeadComment(String headComment);

	/**
	 * setter of nameRelation
	 * @param nameRelation the nameRelation to set
	 */
	void setNameRelation(String nameRelation);

	/**
	 * setter of getTailCardinality
	 * @param tailCardinality the tailCardinality to set
	 */
	void setTailCardinality(String tailCardinality);

	/**
	 * setter of tailComment
	 * @param tailComment the tailComment to set
	 */
	void setTailComment(String tailComment);

	void setType(RelationTypeEnum t);
}
