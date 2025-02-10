
package fr.univrouen.umlreverse.model.project;

import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewRelation;
import fr.univrouen.umlreverse.util.Contract;

/**
 * Relation class represents the relations between components or notes
 */
public class Relation implements IRelation {

	// ATTRIBUTES

	/**
	 * The id.
	 */
	private String idRelation;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Relation)) return false;

		Relation relation = (Relation) o;

		return (idRelation != null ? idRelation.equals(relation.idRelation) : relation.idRelation == null);
	}

	@Override
	public int hashCode() {
		return idRelation != null ? idRelation.hashCode() : 0;
	}

	/**
	 * The source.
	 */
	private IObjectEntity entitySource;
	/**
	 * The target.
	 */
	private IObjectEntity entityTarget;
	/**
	 * The central comment.
	 */
	private String nameRelation = "";
	/**
	 * The head cardinality.
	 */
	private String headCardinality = "";
	/**
	 * The tail cardinality.
	 */
	private String tailCardinality = "";
	/**
	 * The head comment.
	 */
	private String headComment = "";
	/**
	 * The tail comment.
	 */
	private String tailComment = "";
	/**
	 * The head symbol.
	 */
	private TypeHeadArrow arrowHead = TypeHeadArrow.FullHead;
	/**
	 * The tail symbol.
	 */
	private TypeHeadArrow arrowTail = TypeHeadArrow.None;
	/**
	 * The style line.
	 */
	private TypeLineArrow lineArrow = TypeLineArrow.Plain;
	/**
	 * The association class.
	 */
	private IObjectEntity associationEntity = null;

	/**
	 * the set of view.
	 */
	private Set<IViewRelation> views = new HashSet<>();

	/**
	 * The support of listeners.
	 */
	private PropertyChangeSupport support = new PropertyChangeSupport(this);

	// CONSTRUCTOR

	public Relation(String idRelation, IObjectEntity source, IObjectEntity target) {
		Contract.check(idRelation != null && idRelation.length() > 0);
		Contract.check(source != null);
		Contract.check(target != null);

		this.idRelation = idRelation;
		this.entitySource = source;
		this.entityTarget = target;
	}

	public Relation(String idRelation, IObjectEntity source, IObjectEntity target, TypeHeadArrow head, TypeHeadArrow tail, TypeLineArrow line) {
		this(idRelation, source, target, head, tail, line, null);
	}
	public Relation(String idRelation, IObjectEntity source,
					IObjectEntity target, TypeHeadArrow head,
					TypeHeadArrow tail, TypeLineArrow line,
					ObjectEntity classAssociation) {

        Contract.check(idRelation != null && idRelation.length() > 0);
        Contract.check(source != null);
        Contract.check(target != null);
        Contract.check(head != null);
        Contract.check(tail != null);
        Contract.check(line != null);

		this.idRelation = idRelation;
		this.entitySource = source;
		this.entityTarget = target;
		this.arrowHead = head;
		this.arrowTail = tail;
		this.associationEntity = classAssociation;
		this.lineArrow = line;
	}

	// REQUESTS

	/**
	 * getter of getEntitySource
	 * @return the entitySource
	 */
	public IObjectEntity getEntitySource() {
		return entitySource;
	}

	/**
	 * getter of entityTarget
	 * @return the entityTarget
	 */
	public IObjectEntity getEntityTarget() {
		return entityTarget;
	}

	/**
	 * getter of nameRelation
	 * @return the nameRelation
	 */
	public String getNameRelation() {
		return nameRelation;
	}

	/**
	 * getter of headCardinality
	 * @return the headCardinality
	 */
	public String getHeadCardinality() {
		return headCardinality;
	}

	/**
	 * getter of getTailCardinality
	 * @return the tailCardinality
	 */
	public String getTailCardinality() {
		return tailCardinality;
	}

	/**
	 * getter of headComment
	 * @return the headComment
	 */
	public String getHeadComment() {
		return headComment;
	}

	/**
	 * getter of tailComment
	 * @return the tailComment
	 */
	public String getTailComment() {
		return tailComment;
	}

	/**
	 * getter of arrowHear
	 * @return the arrowHear
	 */
	public TypeHeadArrow getArrowHead() {
		return arrowHead;
	}

	/**
	 * getter of arrowTail
	 * @return the arrowTail
	 */
	public TypeHeadArrow getArrowTail() {
		return arrowTail;
	}

	/**
	 * getter of lineArrow
	 * @return the lineArrow
	 */
	public TypeLineArrow getLineArrow() {
		return lineArrow;
	}

	/**
	 * getter of associationEntity
	 * @return the associationEntity
	 */
	public IObjectEntity getAssociationEntity() {
		return associationEntity;
	}

	/**
	 * getter of associationEntity
	 * @return the idRelation
	 */
	public String getId() {
		return idRelation;
	}

    @Override
    public Set<IViewRelation> getViews() {
        return views;
    }

	// METHODS

	/**
	 * setter of getEntitySource
	 * @param entitySource the entitySource to set
	 */
	public void setEntitySource(IObjectEntity entitySource) {
        Contract.check(entitySource != null);
        if (!getEntitySource().equals(entitySource)) {
            IObjectEntity old = this.entitySource;
            this.entitySource = entitySource;
            firePropertyChanged("SourceChanged", old, entitySource);
        }
	}

	/**
	 * setter of entityTarget
	 * @param entityTarget the entityTarget to set
	 */
	public void setEntityTarget(IObjectEntity entityTarget) {
        Contract.check(entityTarget != null);
        IObjectEntity old = this.entityTarget;
        if (!old.equals(entityTarget)) {
            this.entityTarget = entityTarget;
            firePropertyChanged("TargetChanged", old, entityTarget);
        }
	}
	
	/**
	 * setter of nameRelation
	 * @param nameRelation the nameRelation to set
	 */
	public void setNameRelation(String nameRelation) {
		if (getNameRelation() == null && nameRelation == null) {
			return;
		}
		if (getNameRelation() == null  || !getNameRelation().equals(nameRelation)) {
            String old = getNameRelation();
            this.nameRelation = nameRelation;
            firePropertyChanged("NameChanged", old, getNameRelation());
        }
	}

	/**
	 * setter of headCardinality
	 * @param headCardinality the headCardinality to set
	 */
	public void setHeadCardinality(String headCardinality) {
        String old = getHeadCardinality();
        if (headCardinality == null && old == null) {
            return;
        }
        if (headCardinality == null || (!headCardinality.equals(old))) {
            this.headCardinality = headCardinality;
            firePropertyChanged("HeadCardinalityChanged", old, headCardinality);
        }
	}
	
	/**
	 * setter of getTailCardinality
	 * @param tailCardinality the tailCardinality to set
	 */
	public void setTailCardinality(String tailCardinality){
        String old = getTailCardinality();
        if (tailCardinality == null && old == null) {
            return;
        }
        if (tailCardinality == null || (!tailCardinality.equals(old))) {
            this.tailCardinality = tailCardinality;
            firePropertyChanged("TailCardinalityChanged", old, tailCardinality);
        }
	}
	
	/**
	 * setter of headComment
	 * @param headComment the headComment to set
	 */
	public void setHeadComment(String headComment) {
        String old = getHeadComment();
        if (headComment == null && old == null) {
            return;
        }
        if (headComment == null || (!headComment.equals(old))) {
            this.headComment = headComment;
            firePropertyChanged("HeadCommentChanged", old, headComment);
        }
	}
	
	/**
	 * setter of tailComment
	 * @param tailComment the tailComment to set
	 */
	public void setTailComment(String tailComment) {
        String old = getTailComment();
        if (tailComment == null && old == null) {
            return;
        }
        if (tailComment == null || (!tailComment.equals(old))) {
            this.tailComment = tailComment;
            firePropertyChanged("TailCommentChanged", old, tailComment);
        }
	}

	/**
	 * setter of arrowHead
	 * @param arrowHead the arrowHead to set
	 */
	public void setArrowHead(TypeHeadArrow arrowHead) {
        Contract.check(arrowHead != null);
        TypeHeadArrow old = getArrowHead();
        if (!arrowHead.equals(old)) {
            this.arrowHead = arrowHead;
            firePropertyChanged("ArrowHeadChanged", old, arrowHead);
        }
	}
	
	/**
	 * setter of arrowTail
	 * @param arrowTail the arrowTail to set
	 */
	public void setArrowTail(TypeHeadArrow arrowTail) {
        Contract.check(arrowTail != null);
        TypeHeadArrow old = getArrowTail();
        if (!old.equals(arrowTail)) {
            this.arrowTail = arrowTail;
            firePropertyChanged("ArrowTailChanged", old, getArrowTail());
        }
	}
	
	/**
	 * setter of lineArrow
	 * @param lineArrow the lineArrow to set
	 */
	public void setLineArrow(TypeLineArrow lineArrow) {
        Contract.check(lineArrow != null);
        TypeLineArrow old = getLineArrow();
        if (!old.equals(lineArrow)) {
            this.lineArrow = lineArrow;
            firePropertyChanged("LineArrowChanged", old, lineArrow);
        }
	}

	/**
	 * setter of associationEntity
	 * @param associationEntity the associationEntity to set
	 */
	public void setAssociationEntity(IObjectEntity associationEntity) {
        IObjectEntity old = getAssociationEntity();
        if (old == null && associationEntity == null) {
            return;
        }
        if (associationEntity == null || !associationEntity.equals(old)) {
            this.associationEntity = associationEntity;
            firePropertyChanged("AssociationEntityChanged", old, associationEntity);
        }
	}

	@Override
	public void addView(IViewRelation relation) {
		views.add(relation);
	}

	@Override
	public void removeView(IViewRelation relation) {
		views.remove(relation);
	}


    @Override
    public void addPropertyChangeListeners(String property, PropertyChangeListener listener) {
        support.addPropertyChangeListener(property, listener);
    }

    @Override
    public void addPropertyChangeListeners(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListeners(String property, PropertyChangeListener listener) {
        support.removePropertyChangeListener(property, listener);
    }

    @Override
    public void removePropertyChangeListeners(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    protected void firePropertyChanged(String property, Object oldValue, Object newValue) {
        support.firePropertyChange(property, oldValue, newValue);
    }
}
