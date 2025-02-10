package fr.univrouen.umlreverse.model.diagram.sequence;

import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.ALL_STYLE_ID;
import static fr.univrouen.umlreverse.model.diagram.common.INote.STYLE_CHANGED_PROPERTY_NAME;

import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.Observable;
import fr.univrouen.umlreverse.model.diagram.sequence.Segment.SegmentType;
import fr.univrouen.umlreverse.model.diagram.sequence.visitor.ISequenceVisitor;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.diagram.util.Style;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;

public abstract class SegmentCommon  extends Observable implements ISegmentCommon {

	// ATTRIBUTS
		private ISequenceDiagram diagram;
	    private ISegment parent;
	    private String id;

	    private String condition;
	    private final SegmentType type;
	    private IObject start;
	    private IObject end;

		// CONSTRUCTEURS
		public SegmentCommon(SegmentType type, ISequenceDiagram diagram, String condition) {
			Contract.check(diagram != null , "diagram can't be null");
			Contract.check(type != null , "type can't be null");
			Contract.check(condition != null , "condition can't be null");
			id = UUID.randomUUID().toString();
			boolean b = diagram.addId(id);
		    while(!b) {
		    	id = UUID.randomUUID().toString();
		    }
			this.type = type;
			this.diagram = diagram;
			this.condition = condition;
		}

		public SegmentCommon(SegmentType type, ISequenceDiagram diagram) {
			Contract.check(diagram != null , "diagram can't be null");
			Contract.check(type != null , "type can't be null");
			id = UUID.randomUUID().toString();
			boolean b = diagram.addId(id);
		    while(!b) {
		    	id = UUID.randomUUID().toString();
		    }
			this.type = type;
			this.diagram = diagram;
		}

		// REQUETES
		@Override
		public SegmentType getSegmentType() {
			return type;
		}

		@Override
		public String getCondition() {
			return condition;
		}

		@Override
		public ISequenceDiagram getDiagram() {
			return diagram;
		}

		@Override
		public ISegment getParent() {
			return parent;
		}

		@Override
		public IStyle getStyle() {
			IStyle style = new Style();
	        style.putAll(diagram.getStyle().getStyle(ALL_STYLE_ID, ALL_STYLE_ID));
	        style.putAll(diagram.getStyle().getStyle(ALL_STYLE_ID, SEGMENT_STYLE_ID));
	        style.putAll(diagram.getStyle().getStyle(SEGMENT_STYLE_ID, id));
	        return style;
		}


		@Override
		public int getLevel() {
			ISegment seg = getParent();
			int res = 0;
			while (seg != null) {
				seg = seg.getParent();
				res++;
			}
			return res;
		}

		@Override
		public double getY() {
			String position = getStyle().getValue(IDiagramEditorController.POSITION_STYLE_ID);
			String[] positionTab = position.split("\\|");
			return Double.valueOf(positionTab[3]);
		}

		// COMMANDES
		@Override
		public void setCondition(String condition) {
			Contract.check(condition != null , "type can't be null");

			this.condition = condition;
			firePropertyChange(CONDITION_CHANGED_PROPERTY_NAME, null, condition);
		}


		@Override
		public void setParent(ISegment parent) {
			ISegment old = this.parent;
			ISegment news = parent;
			this.parent = parent;
			// TODO a ne pas oublier
			parent.getSegments().add(this);
			firePropertyChange(PARENT_CHANGED_PROPERTY_NAME, old, news);
		}

		@Override
		public void addStyle(String key, String value) {
			Contract.check(diagram != null);
	        IStyle old = getStyle();
	        diagram.getStyle().addStyle(SEGMENT_STYLE_ID, id, key, value);
	        firePropertyChange(STYLE_CHANGED_PROPERTY_NAME, old, getStyle());

		}

		@Override
		public void addAllStyle(Map<String, String> keyValue) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeStyle(String key) {
			// TODO Auto-generated method stub

		}

		@Override
		public void clearStyle() {
			// TODO Auto-generated method stub

		}

		@Override
		public void accept(ISequenceVisitor visitor) {
			// TODO à faire
			//visitor.visit(this);
		}

		public IObject getStart() {
			return start;
		}

		public void setStart(IObject start) {
			this.start = start;
		}

		public IObject getEnd() {
			return end;
		}

		public void setEnd(IObject end) {
			this.end = end;
		}



}
