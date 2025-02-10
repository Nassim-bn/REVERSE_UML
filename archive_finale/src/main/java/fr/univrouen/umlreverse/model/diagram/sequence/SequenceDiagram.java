
package fr.univrouen.umlreverse.model.diagram.sequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.common.ADiagram;
import fr.univrouen.umlreverse.model.diagram.common.ADiagramCommon;
import fr.univrouen.umlreverse.model.diagram.common.INote;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.sequence.Segment.SegmentType;
import fr.univrouen.umlreverse.model.diagram.sequence.visitor.ISequenceVisitor;
import fr.univrouen.umlreverse.model.diagram.usecase.Actor;
import fr.univrouen.umlreverse.model.diagram.usecase.INoteUsecase;
import fr.univrouen.umlreverse.model.diagram.usecase.ISystem;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecase;
import fr.univrouen.umlreverse.model.diagram.usecase.visitor.IUsecaseVisitor;
import fr.univrouen.umlreverse.model.diagram.visitor.IDiagramVisitor;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;

public class SequenceDiagram extends ADiagramCommon implements ISequenceDiagram {


	/**
	 *
	 */
	private static final long serialVersionUID = -5257607374410334507L;
	// ATTRIBUTES
	private final List<IObject> listObjects = new ArrayList<>() ;
    private final Set<INote> listNotes = new HashSet<>() ;
    private final Set<ISegmentCommon> listSegments = new HashSet<>();
    private final Set<IActivity> listActivities = new HashSet<>();

    //CONSTRUCTOR
	public SequenceDiagram(IProject project, String name) throws RefusedAction {
		super(project, name);
	}

	//REQUESTS

	@Override
	public IObject getObject(String name) {
		Contract.check(name != null , "Name object can't be null");
		for (IObject o : listObjects) {
			if (o.getName().equals(name)) {
				return o;
			}
		}
        return null;
	}

	@Override
	public List<IObject> getObjects() {
		return listObjects;
	}

	@Override
	public Set<INote> getNotes() {
		return listNotes;
	}

	@Override
	public Set<ISegmentCommon> getSegments() {
		return listSegments;
	}


	@Override
	public DiagramType getType() {
		return DiagramType.SEQUENCE;
	}

	@Override
	public Set<ISegmentCommon> getSegmentsChilds() {
		Set<ISegmentCommon> result = new HashSet<>();
		result.addAll(getSegments());
		getSegments().stream().filter(seg -> !seg.getSegmentType().equals(SegmentType.REF)).forEach(seg -> {
			
			((ISegment) seg).getSegmentsChilds(result);
		});
		return result;
	}

	// COMMANDS

	@Override
	public void addObject(IObject object) throws RefusedAction {
		Contract.check(object != null , "actor can't be null");
		for (IObject obj : getObjects()) {
            if (!obj.getName().equals("") && obj.getName().equals(object.getName())) {
                throw new RefusedAction(ErrorAbstraction.ErrorObjectAlreadyInDiagram);
            }
        }
        boolean b = listObjects.add(object);
        if (b) {
            firePropertyChange(OBJECT_ADDED_PROPERTY_NAME, null, object);
        }
	}

	@Override
	public void addNote(INote note) {
		 Contract.check(note != null , "note can't be null");
	     boolean b = listNotes.add(note);
	     if (b) {
	         firePropertyChange(NOTE_ADDED_PROPERTY_NAME, null, note);
	     }
	}

	@Override
	public void addSegment(ISegmentCommon segment) {
		Contract.check(segment != null , "segment can't be null");
		boolean b = listSegments.add(segment);
		if (b) {
		    firePropertyChange(SEGMENT_ADDED_PROPERTY_NAME, null, segment);
		}
	}

	public void removeSegment(ISegmentCommon segment) {
		Contract.check(segment != null , "note can't be null");
		boolean b = listSegments.remove(segment);
		if (segment.getParent() != null) {
			try {
				segment.getParent().removeSegment(segment);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (b) {
		    firePropertyChange(SEGMENT_REMOVED_PROPERTY_NAME, segment, null);
		}
	}

	@Override
	public void addActivity(IActivity activity) {
		Contract.check(activity != null , "note can't be null");
		boolean b = listActivities.add(activity);
		if (b) {
		    firePropertyChange(ACTIVITY_ADDED_PROPERTY_NAME, null, activity);
		}
	}

	public void removeActivity(IActivity activity) {
		Contract.check(activity != null , "note can't be null");
		boolean b = listActivities.remove(activity);
		if (activity.getParent() != null) {
			try {
				activity.getParent().removeActivity(activity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		getRelations().forEach(relation -> {
			if (relation instanceof IRelationToObject) {
				IRelationToObject relationToObject = ((IRelationToObject) relation);
				if (relationToObject.getActivityContainer() == activity) {
					relationToObject.setActivityContainer(null);
				}
				if (relationToObject.getActivityDstContainer() == activity) {
					relationToObject.setActivityDstContainer(null);
				}
			}
		});
		if (b) {
		    firePropertyChange(ACTIVITY_REMOVED_PROPERTY_NAME, activity, null);
		}
	}

	@Override
	public void addSystem(ISystem group) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeObject(IObject object) {
		boolean b = listObjects.remove(object);
        if (b) {

        	//remove each segment related to this object
        	List<ISegmentCommon> removeSegmentList = new ArrayList<>();
        	getSegments().forEach(segment -> {
        		if (segment.getStart().equals(object) 
        				|| segment.getEnd().equals(object)) {
        			removeSegmentList.add(segment);
        		}
        	});
        	removeSegmentList.forEach(segment -> {
        		removeSegment(segment);
        	});
        	
        	// remove each relation related to this object
        	List<IRelation> removeRelationList = new ArrayList<>();
        	getRelations().forEach(relation -> {
        		if (relation.getEntitySource().equals(object) 
        				|| relation.getEntityTarget().equals(object)) {
        			removeRelationList.add(relation);
        		}
        	});
        	removeRelationList.forEach(relation -> {
    			removeRelation(relation);
        	});
        	
        	//remove each activity which has as parent this object
        	List<IActivity> removeActivityList = new ArrayList<>();
        	getActivities().forEach(activity -> {
        		if (activity.getObj().equals(object)) {
        			removeActivityList.add(activity);
        		}
        	});
        	
        	removeActivityList.forEach(activity -> {
    			removeActivity(activity);
        	});
            firePropertyChange(OBJECT_REMOVED_PROPERTY_NAME, object, null);
        }
	}

	@Override
	public void removeNote(INote note) {
		Contract.check(note != null , "note can't be null");
        boolean b = listNotes.remove(note);
        if (b) {
            note.getRelations().stream().forEach((r) -> {
                removeRelation(r);
            });
            firePropertyChange(NOTE_REMOVED_PROPERTY_NAME, note, null);
        }
	}

	@Override
	public void removeSystem(ISystem group) {
		// TODO Auto-generated method stub

	}

	@Override
	public void accept(ISequenceVisitor visitor) {
		//visitor.visit(this);

	}

	public void accept(IDiagramVisitor visitor) {
		visitor.visit(this);

	}

	@Override
	public Set<IActivity> getActivities() {
		return listActivities;
	}


}

