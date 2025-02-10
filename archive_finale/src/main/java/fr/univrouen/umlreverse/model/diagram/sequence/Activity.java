package fr.univrouen.umlreverse.model.diagram.sequence;

import static fr.univrouen.umlreverse.model.diagram.common.IDiagram.ALL_STYLE_ID;
import static fr.univrouen.umlreverse.model.diagram.common.INote.STYLE_CHANGED_PROPERTY_NAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.Observable;
import fr.univrouen.umlreverse.model.diagram.sequence.visitor.ISequenceVisitor;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.diagram.util.Style;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;

public class Activity extends Observable implements IActivity {

	// ATTRIBUTS
	private IObject object;
	private Set<IRelation> listRelation;
	private List<IActivity> listActivity;
	private Map<IObject, IRelation> mapRelation;
	private ISequenceDiagram diagram;
	private String id;
	private IActivity parent;
	private boolean haveChild;
	private boolean haveRelSrcDest;

	// CONSTRUCTOR
	public Activity(IObject obj, ISequenceDiagram diagram) {
		Contract.check(obj != null, "obj can't be null");
		Contract.check(diagram != null, "diagram can't be null");

		id = UUID.randomUUID().toString();
		boolean b = diagram.addId(id);
		while (!b) {
			id = UUID.randomUUID().toString();
		}

		object = obj;
		listRelation = new HashSet<IRelation>();
		listActivity = new ArrayList<IActivity>();

		mapRelation = new HashMap<IObject, IRelation>();

		this.diagram = diagram;
	}

	public boolean isRelSrcDest() {
		return haveRelSrcDest;
	}

	public Map<IObject, IRelation> getMapRelation() {
		return mapRelation;
	}

	@Override
	public IObject getObj() {
		return object;
	}

	@Override
	public Set<IRelation> getRelation() {
		return listRelation;
	}

	@Override
	public ISequenceDiagram getDiagram() {
		return diagram;
	}

	public IActivity getParent() {
		return parent;
	}

	@Override
	public List<IActivity> getActivity() {
		return listActivity;
	}

	public boolean haveChild() {
		return haveChild;
	}

	@Override
	public Set<IActivity> getActivitiesChilds(Set<IActivity> result) {
		result.addAll(getActivity());
		getActivity().forEach(act -> act.getActivitiesChilds(result));
		if (result.isEmpty()) {
			haveChild = true;
		}
		return result;
	}
	
	@Override
	public int getLevel() {
		IActivity act = getParent();
		int res = 0;

		while (act != null) {
			act = act.getParent();
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

	public void setRelSrcDest(boolean rel) {
		haveRelSrcDest = rel;
	}

	public void addMapRelation(IObject obj, IRelation rel) {
		mapRelation.put(obj, rel);
	}

	@Override
	public void addRelation(IRelation relation) {
		Contract.check(relation != null, "relation must not be null.");
		boolean b = listRelation.add(relation);

		if (b) {
			firePropertyChange(RELATION_ADDED_TO_ACTIVITY_PROPERTY_NAME, null, relation);
		}
	}

	@Override
	public void removeRelation(IRelation relation) {
		Contract.check(relation != null, "relation must not be null.");
		boolean b = listRelation.remove(relation);

		if (b) {
			firePropertyChange(RELATION_REMOVED_PROPERTY_NAME, relation, null);
		}
	}

	@Override
	public void setObj(IObject obj) {
		Contract.check(obj != null, "obj can't be null");

		object = obj;
	}

	@Override
	public void addActivity(IActivity activity) {
		Contract.check(activity != null, "activity can't be null");

		if (!listActivity.contains(activity)) {
			listActivity.add(activity);
		}
		firePropertyChange(ACTIVITY_ADDED_PROPERTY_NAME, null, listActivity);
	}

	@Override
	public void removeActivity(IActivity activity) throws Exception {
		Contract.check(activity != null, "activity can't be null");

		if (listActivity.contains(activity)) {
			listActivity.remove(activity);
			firePropertyChange(ACTIVITY_REMOVED_PROPERTY_NAME, null, listActivity);
		} else {
			throw new Exception("This activity doesn't exist");
		}
	}

	@Override
	public IStyle getStyle() {
		IStyle style = new Style();
		style.putAll(diagram.getStyle().getStyle(ALL_STYLE_ID, ALL_STYLE_ID));
		style.putAll(diagram.getStyle().getStyle(ALL_STYLE_ID, ACTIVITY_STYLE_ID));
		style.putAll(diagram.getStyle().getStyle(ACTIVITY_STYLE_ID, id));
		return style;
	}

	@Override
	public void addStyle(String key, String value) {
		Contract.check(diagram != null);
		IStyle old = getStyle();
		diagram.getStyle().addStyle(ACTIVITY_STYLE_ID, id, key, value);
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

	public void setParent(IActivity parent) {
		IActivity old = this.parent;
		IActivity news = parent;
		this.parent = parent;
		parent.getActivity().add(this);
		firePropertyChange(PARENT_CHANGED_PROPERTY_NAME, old, news);
	}

	@Override
	public void accept(ISequenceVisitor visitor) {
		// TODO à faire
		// visitor.visit(this);
	}
}
