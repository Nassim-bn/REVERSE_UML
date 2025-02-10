package fr.univrouen.umlreverse.model.diagram.clazz.view;

import fr.univrouen.umlreverse.model.diagram.common.IStylizable;
import java.io.Serializable;

import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.model.project.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.clazz.visitor.IClassVisitor;
import fr.univrouen.umlreverse.model.diagram.common.IObservable;

/**
 * A view on a relation
 */
public interface IViewRelation extends Serializable, IStylizable, IObservable {
    /**
     * the original data.
     */
    IRelation getData();

    /**
     * The id.
     */
    String getId();

    /**
     * The diagram
     */
    IClassDiagram getDiagram();

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
     * getter of arrowHear
     * @return the arrowHear
     */
    TypeHeadArrow getArrowHead();

    /**
     * setter of arrowHear
     * @param arrowHead the arrowHear to set
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

    void accept(IClassVisitor visitor);
}
