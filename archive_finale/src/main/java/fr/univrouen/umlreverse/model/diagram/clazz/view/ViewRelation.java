package fr.univrouen.umlreverse.model.diagram.clazz.view;

import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import java.util.Map;

import fr.univrouen.umlreverse.model.project.IRelation;
import static fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram.ALL_STYLE_ID;
import static fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram.RELATION_STYLE_ID;
import fr.univrouen.umlreverse.model.diagram.clazz.visitor.IClassVisitor;
import fr.univrouen.umlreverse.model.diagram.common.Observable;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.diagram.util.Style;
import fr.univrouen.umlreverse.util.Contract;

/**
 * A view on a relation
 */
public class ViewRelation extends Observable implements IViewRelation {

    // ATTRIBUTES

    private final IRelation data;
    private final IClassDiagram diagram;

    // CONSTRUCTOR

    public ViewRelation(IRelation data, IClassDiagram diagram) {
        Contract.check(data != null);
        Contract.check(diagram != null);
        Contract.check(diagram.getProject().contains(data));
        this.data = data;
        this.diagram = diagram;
        data.addView(this);
        diagram.addRelation(this);
        initializeListenersOnData();
    }

    // REQUESTS


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewRelation)) return false;

        ViewRelation that = (ViewRelation) o;
        if (!getDiagram().equals(that.getDiagram())) return false;
        return data != null ? data.equals(that.data) : that.data == null;
    }

    @Override
    public int hashCode() {
        int result = getDiagram() != null ? getDiagram().hashCode() : 0;
        return 31 * result + (data != null ? data.hashCode() : 0);
    }

    @Override
    public TypeHeadArrow getArrowHead() {
        return data.getArrowHead();
    }

    @Override
    public TypeHeadArrow getArrowTail() {
        return data.getArrowTail();
    }

    @Override
    public IObjectEntity getAssociationEntity() {
        return data.getAssociationEntity();
    }

    @Override
    public IRelation getData() {
        return data;
    }

    @Override
    public IClassDiagram getDiagram() {
        return diagram;
    }

    @Override
    public IObjectEntity getEntitySource() {
        return data.getEntitySource();
    }

    @Override
    public IObjectEntity getEntityTarget() {
        return data.getEntityTarget();
    }

    @Override
    public String getHeadCardinality() {
        return data.getHeadCardinality();
    }

    @Override
    public String getHeadComment() {
        return data.getHeadComment();
    }

    @Override
    public String getId() {
        return data.getId();
    }

    @Override
    public TypeLineArrow getLineArrow() {
        return data.getLineArrow();
    }

    @Override
    public String getNameRelation() {
        return data.getNameRelation();
    }

    @Override
    public IStyle getStyle() {
        Contract.check(diagram != null);
        IStyle style = new Style();
        style.putAll(diagram.getStyle().getStyle(ALL_STYLE_ID, ALL_STYLE_ID));
        style.putAll(diagram.getStyle().getStyle(ALL_STYLE_ID, RELATION_STYLE_ID));
        style.putAll(diagram.getStyle().getStyle(RELATION_STYLE_ID, getId()));
        return style;
    }

    @Override
    public String getTailCardinality() {
        return data.getTailCardinality();
    }

    @Override
    public String getTailComment() {
        return data.getTailComment();
    }
    
    // COMMANDS

    @Override
    public void addStyle(String key, String value) {
        Contract.check(diagram != null);
        IStyle old = getStyle();
        diagram.getStyle().addStyle("relation", getId(), key, value);
        firePropertyChange("StyleChanged", old, getStyle());
    }

    @Override
    public void addAllStyle(Map<String, String> keyValue) {
        Contract.check(keyValue != null);
        IStyle old = getStyle();
        for (String key : keyValue.keySet()) {
            diagram.getStyle().addStyle("relation", getId(), key, keyValue.get(key));
        }
        firePropertyChange("StyleChanged", old, getStyle());
    }

    @Override
    public void removeStyle(String key) {
        Contract.check(diagram != null);
        diagram.getStyle().removeStyle("relation", getId(), key);
        firePropertyChange("StyleChanged", null, getStyle());
    }

    @Override
    public void clearStyle() {
        Contract.check(diagram != null);
        diagram.getStyle().removeStyle("relation", getId());
        firePropertyChange("StyleChanged", null, getStyle());
    }

    @Override
    public void setArrowHead(TypeHeadArrow arrowHead) {
        TypeHeadArrow old = data.getArrowHead();
        data.setArrowHead(arrowHead);
    }

    @Override
    public void setArrowTail(TypeHeadArrow arrowTail) {

        TypeHeadArrow old = data.getArrowTail();
        data.setArrowTail(arrowTail);
    }

    @Override
    public void setAssociationEntity(IObjectEntity associationEntity) {
        IObjectEntity old = data.getAssociationEntity();
        data.setAssociationEntity(associationEntity);
    }

    @Override
    public void setEntitySource(IObjectEntity entitySource) {
        Contract.check(entitySource != null);
        IObjectEntity old = data.getEntitySource();
        data.setEntitySource(entitySource);

    }

    @Override
    public void setEntityTarget(IObjectEntity entityTarget) {
        Contract.check(entityTarget != null);
        IObjectEntity old = data.getEntityTarget();
        data.setEntityTarget(entityTarget);
    }

    @Override
    public void setHeadCardinality(String headCardinality) {
        String old = data.getHeadCardinality();
        data.setHeadCardinality(headCardinality);
    }

    @Override
    public void setHeadComment(String headComment) {
        String old = data.getHeadComment();
        data.setHeadComment(headComment);
    }

    @Override
    public void setLineArrow(TypeLineArrow lineArrow) {
        TypeLineArrow old = data.getLineArrow();
        data.setLineArrow(lineArrow);
    }

    @Override
    public void setNameRelation(String nameRelation) {
        String old = data.getNameRelation();
        data.setNameRelation(nameRelation);
    }

    @Override
    public void setTailCardinality(String tailCardinality) {
        String old = data.getTailCardinality();
        data.setTailCardinality(tailCardinality);
    }

    @Override
    public void setTailComment(String tailComment) {
        String old = data.getTailComment();
        data.setTailComment(tailComment);
    }

    @Override
    public void accept(IClassVisitor visitor) {
        visitor.visit(this);
    }

    // TOOLS

    private void initializeListenersOnData() {
        data.addPropertyChangeListeners("SourceChanged", evt-> firePropertyChange("SourceChanged", evt.getOldValue(), evt.getNewValue()));
        data.addPropertyChangeListeners("TargetChanged", evt-> firePropertyChange("TargetChanged", evt.getOldValue(), evt.getNewValue()));
        data.addPropertyChangeListeners("NameChanged", evt-> firePropertyChange("NameChanged", evt.getOldValue(), evt.getNewValue()));
        data.addPropertyChangeListeners("TailCommentChanged", evt-> firePropertyChange("TailCommentChanged", evt.getOldValue(), evt.getNewValue()));
        data.addPropertyChangeListeners("HeadCommentChanged", evt-> firePropertyChange("HeadCommentChanged", evt.getOldValue(), evt.getNewValue()));
        data.addPropertyChangeListeners("TailCardinalityChanged", evt->firePropertyChange("TailCardinalityChanged", evt.getOldValue(), evt.getNewValue()));
        data.addPropertyChangeListeners("HeadCardinalityChanged", evt->firePropertyChange("HeadCardinalityChanged", evt.getOldValue(), evt.getNewValue()));
        data.addPropertyChangeListeners("ArrowHeadChanged", evt-> firePropertyChange("ArrowHeadChanged", evt.getOldValue(), evt.getNewValue()));
        data.addPropertyChangeListeners("ArrowTailChanged", evt-> firePropertyChange("ArrowTailChanged", evt.getOldValue(), evt.getNewValue()));
        data.addPropertyChangeListeners("LineArrowChanged", evt-> firePropertyChange("LineArrowChanged", evt.getOldValue(), evt.getNewValue()));
        data.addPropertyChangeListeners("AssociationEntityChanged", evt-> firePropertyChange("AssociationEntityChanged", evt.getOldValue(), evt.getNewValue()));
    }
}
