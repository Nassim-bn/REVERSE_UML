package fr.univrouen.umlreverse.model.diagram.common;
import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.RELATION_STYLE_ID;

import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;

/**
 * A relation.
 */
public class Relation extends AEntity implements IRelation {
		// ATTRIBUTES
        /**
	 * The source.
	 */
	private IEntityRelation entitySource;
	/**
	 * The target.
	 */
	private IEntityRelation entityTarget;
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
	private TypeHeadArrow arrowHead = TypeHeadArrow.None;
	/**
	 * The tail symbol.
	 */
	private TypeHeadArrow arrowTail = TypeHeadArrow.None;
	/**
	 * The style line.
	 */
	private TypeLineArrow lineArrow = TypeLineArrow.Plain;

	private IDiagramCommon diagram;

    private RelationTypeEnum type = RelationTypeEnum.NORMAL;
// CONSTRUCTORS
	public Relation(IDiagramCommon diagram, IEntityRelation source, IEntityRelation target) {
		super(diagram);
		Contract.check(source != null);
		Contract.check(target != null);
		Contract.check(diagram != null);
		this.entitySource = source;
		this.entityTarget = target;
		this.diagram = diagram;
	}

	public Relation(IDiagramCommon diagram,
		IEntityRelation source, IEntityRelation target,
		TypeHeadArrow head, TypeHeadArrow tail, TypeLineArrow line){

		this(diagram, source, target);
		Contract.check(head != null);
		Contract.check(tail != null);
		Contract.check(line != null);
		this.arrowHead = head;
		this.arrowTail = tail;
		this.lineArrow = line;
	}

// REQUESTS
	@Override
	public TypeHeadArrow getArrowHead() {
		return arrowHead;
	}

	@Override
	public TypeHeadArrow getArrowTail() {
		return arrowTail;
	}

	@Override
	public IDiagramCommon getDiagram() {
            return diagram;
        }

	@Override
	public String getHeadCardinality() {
		return headCardinality;
	}

	@Override
	public String getHeadComment() {
		return headComment;
	}

	@Override
	public IEntityRelation getEntitySource() {
            return entitySource;
	}

	@Override
	public IEntityRelation getEntityTarget() {
		return entityTarget;
	}

	@Override
	public TypeLineArrow getLineArrow() {
		return lineArrow;
	}

	@Override
	public String getNameRelation() {
		return nameRelation;
	}

	@Override
	public String getStyleId() {
            return RELATION_STYLE_ID;
        }

	@Override
	public String getTailCardinality() {
		return tailCardinality;
	}

	@Override
	public String getTailComment() {
		return tailComment;
	}

	@Override
	public double getY() {
		String position = getStyle().getValue(IDiagramEditorController.POSITION_STYLE_ID);
		String[] positionTab = position.split("\\|");
		return Double.valueOf(positionTab[3]);
	}

// COMMANDS
	@Override
	public void setArrowHead(TypeHeadArrow arrowHead) {
        Contract.check(arrowHead != null);
            TypeHeadArrow old = getArrowHead();
            if (!arrowHead.equals(old)) {
                this.arrowHead = arrowHead;
                firePropertyChange(HEAD_CHANGED_PROPERTY_NAME, old, arrowHead);
            }
	}


	@Override
	public void setArrowTail(TypeHeadArrow arrowTail) {
		Contract.check(arrowTail != null);
		TypeHeadArrow old = getArrowTail();
		if (!old.equals(arrowTail)) {
			this.arrowTail = arrowTail;
			firePropertyChange(TAIL_CHANGED_PROPERTY_NAME, old, getArrowTail());
		}
	}

	@Override
	public void setEntitySource(IEntityRelation entitySource) {
		Contract.check(entitySource != null);
		if (!getEntitySource().equals(entitySource)) {
			IEntityRelation old = this.entitySource;
			this.entitySource = entitySource;
			firePropertyChange(SOURCE_CHANGED_PROPERTY_NAME, old, entitySource);
		}
	}

	@Override
	public void setEntityTarget(IEntityRelation entityTarget) {
		Contract.check(entityTarget != null);
		IEntityRelation old = this.entityTarget;
		if (!old.equals(entityTarget)) {
			this.entityTarget = entityTarget;
			firePropertyChange(TARGET_CHANGED_PROPERTY_NAME, old, entityTarget);
		}
	}

	@Override
	public void setHeadCardinality(String headCardinality) {
		String old = getHeadCardinality();
		if (headCardinality == null && old == null) {
			return;
		}
		if (headCardinality == null || (!headCardinality.equals(old))) {
			this.headCardinality = headCardinality;
			firePropertyChange(HEAD_CARDI_CHANGED_PROPERTY_NAME, old, headCardinality);
		}
	}

	@Override
	public void setHeadComment(String headComment) {
		String old = getHeadComment();
		if (headComment == null && old == null) {
			return;
		}
		if (headComment == null || (!headComment.equals(old))) {
			this.headComment = headComment;
			firePropertyChange(HEAD_COM_CHANGED_PROPERTY_NAME, old, headComment);
		}
	}

	@Override
	public void setLineArrow(TypeLineArrow lineArrow) {
		Contract.check(lineArrow != null);
		TypeLineArrow old = getLineArrow();
		if (!old.equals(lineArrow)) {
			this.lineArrow = lineArrow;
			firePropertyChange(LINE_CHANGED_PROPERTY_NAME, old, lineArrow);
		}
	}

	@Override
	public void setNameRelation(String nameRelation) {
		if (getNameRelation() == null && nameRelation == null) {
			return;
		}
		if (getNameRelation() == null  || !getNameRelation().equals(nameRelation)) {
			String old = getNameRelation();
			this.nameRelation = nameRelation;
			firePropertyChange(NAME_CHANGED_PROPERTY_NAME, old, getNameRelation());
		}
	}

	@Override
	public void setTailCardinality(String tailCardinality){
		String old = getTailCardinality();
		if (tailCardinality == null && old == null) {
			return;
		}
		if (tailCardinality == null || (!tailCardinality.equals(old))) {
			this.tailCardinality = tailCardinality;
			firePropertyChange(TAIL_CARDI_CHANGED_PROPERTY_NAME, old, tailCardinality);
		}
	}

	@Override
	public void setTailComment(String tailComment) {
		String old = getTailComment();
		if (tailComment == null && old == null) {
			return;
		}
		if (tailComment == null || (!tailComment.equals(old))) {
			this.tailComment = tailComment;
			firePropertyChange(TAIL_COM_CHANGED_PROPERTY_NAME, old, tailComment);
		}
	}

	@Override
	public RelationTypeEnum getType() {
		return type;
	}



	@Override
	public void setType(RelationTypeEnum t) {
		RelationTypeEnum old = type;
		type = t;
		firePropertyChange(TYPE_CHANGE_PROPERTY_NAME, old, t);
	}
}
