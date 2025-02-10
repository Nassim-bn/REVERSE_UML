package fr.univrouen.umlreverse.model.diagram.sequence.visitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.univrouen.umlreverse.model.diagram.common.INote;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.sequence.IActivity;
import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegment;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegmentCommon;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.RelationToObject;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.util.Contract;

/**
 * A visitor used to export a PlantUML file from a sequence diagram.
 */

public class ExportPlantUMLSequenceVisitor {

	// ATTRIBUTS
    private File file;
    private String error = null;
    private boolean canDrawRelation;
    private List<IActivity> listActDrawn = new ArrayList<>();
    private Map<IActivity, Boolean> mapActDrawn = new HashMap<>();

    // CONSTRUCTEURS
    public ExportPlantUMLSequenceVisitor(String filename) {
        this(new File(filename));
    }
    public ExportPlantUMLSequenceVisitor(File file) {
        Contract.check(file != null);
        this.file = file;
    }

    public void visit(ISequenceDiagram sequenceDiagram) {
        file.delete();
        analyse(sequenceDiagram);
    }

    public String getError() {
        return error;
    }

    private void analyse(ISequenceDiagram diagram) {
        error = null;
        BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            error = ErrorAbstraction.ErrorImpossibleToCreateFile.getCode();
            return;
        }

        try {
            writer.append("@startuml\n");
            if (diagram.getTitle() != null && diagram.getTitle().length() > 0) {
                writer.write("title " + diagram.getTitle() + "\n");
            }
            writeObject(diagram, writer);
            writeSegment(diagram, writer);
            writeActivity(diagram, writer);
            writeRelations(diagram, writer);
            writeNotes(diagram, writer);
            writer.append("@enduml\n");
        } catch (IOException e) {
            error = ErrorAbstraction.ErrorImpossibleToWriteInFile.getCode();
            try {
            	writer.close();
            } catch (IOException ignored) {

            }
            return;
        }

        try {
            writer.close();
        } catch (IOException e) {
            error = ErrorAbstraction.ErrorImpossibleToCloseTheFile.getCode();
        }
    }

    private void writeNotes(ISequenceDiagram diagram, BufferedWriter writer) throws IOException {
        for (INote note : diagram.getNotes()) {
            String id = "note" + note.getId();
            writer.write("note as " + id + "\n");
            writer.write(note.getText() + "\n");
            writer.write("end note\n");
        }
    }

    private void writeObject(ISequenceDiagram diagram, BufferedWriter writer) throws IOException {
    	Comparator<IObject> compare = Comparator.comparing(IObject::getX);
    	diagram.getObjects().stream().sorted(compare).forEach(obj -> {
    		try {
				writer.append(objectToPlantUml(obj) + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	});
    }

    private void writeSegment(ISequenceDiagram diagram, BufferedWriter writer) throws IOException {
    	Comparator<ISegmentCommon> compare = Comparator.comparing(ISegmentCommon::getY);
    	diagram.getSegments().stream().filter(seg -> seg.getParent() == null).sorted(compare).forEach(seg -> {
			try {
				writer.append(segmentToPlantUml(seg) + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
    }

    private void writeActivity(ISequenceDiagram diagram, BufferedWriter writer) throws IOException {
    	Comparator<IActivity> compare = Comparator.comparing(IActivity::getY);
    	diagram.getActivities().stream().filter(act -> act.getParent() == null).sorted(compare).forEach(act -> {
    		try {
				writer.append(activityToPlantUml(act) + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	});
    }

    private void writeRelations(ISequenceDiagram diagram, BufferedWriter writer) throws IOException {
    	Comparator<IRelation> compare = Comparator.comparing(IRelation::getY);
    	diagram.getRelations().stream().filter(rel -> ((RelationToObject) rel).getSegmentContainer() == null
				   											   && ((RelationToObject) rel).getActivityContainer() == null).sorted(compare).forEach(rel -> {
			try {
				writer.append(relationToPlantUml(rel) + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	});
    }

    private String relationToPlantUml(IRelation relation) {
    	String result = "";
    	if (relation.getType().equals(RelationTypeEnum.ASYNCHRONE)) {
    		result += "->> ";
    	} else if (relation.getType().equals(RelationTypeEnum.SYNCHRONE)) {
    		result += "-> ";
    	} else {
    		result += "--> ";
    	}

        String commentCentral = prepareCommentToPlantUml(relation.toString());
        if (commentCentral.length() > 0) {
            commentCentral = " : " + commentCentral;
        }

        IObject objectSource = (IObject) relation.getEntitySource();
        IObject objectTarget = (IObject) relation.getEntityTarget();
        return objectSource.getName() + " " + result + objectTarget.getName() + " " + commentCentral + "\n";
    }

    private String objectToPlantUml(IObject obj) {
    	String result = "participant " + obj.getName();
    	return result;
    }

    private String segmentToPlantUml(ISegmentCommon seg) {
    	String result = seg.getSegmentType().getType() + " " + seg.getCondition() + "\n";

    	for (IRelation rel : ((ISegment) seg).getRelations()) {
    		result += relationToPlantUml(rel);
    	}

    	for (ISegmentCommon segment : ((ISegment) seg).getSegments()) {
    		result += segmentToPlantUml(segment);
    	}

    	return result + "\nend " + seg.getSegmentType().getType();
    }

    private String activityToPlantUml(IActivity act) {
    	String res = "";
    	if (!listActDrawn.contains(act)) {
    		mapActDrawn.put(act, false);
    		if (!act.getRelation().isEmpty()) {
    			res = "activate " + act.getObj().getName() + "\n";
    		}

	    	for (IActivity activity : act.getActivity()) {
	    		res += activityToPlantUml(activity);
	    	}

	    	Comparator<IRelation> compare = Comparator.comparing(IRelation::getY);
	    	List<IRelation> s = new ArrayList<>();
	    	act.getRelation().stream().sorted(compare).forEach(tmp -> s.add(tmp));


			IActivity other;
			boolean isDst = false;

			if (s.size() != 0) {
				if (((RelationToObject) s.get(0)).getActivityContainer().equals(act)) {
					 other = ((RelationToObject) s.get(0)).getActivityDstContainer();
					 isDst = true;
				} else {
					 other = ((RelationToObject) s.get(0)).getActivityContainer();
				}
		    	for (IRelation r : s) {
		    		if (canDrawRelation && (isDst && !((RelationToObject) r).getActivityDstContainer().equals(other) ||
		    				!isDst && !((RelationToObject) r).getActivityContainer().equals(other))) {
		    			canDrawRelation = false;
		    			res += "deactivate " + other.getObj().getName() + "\n";
		    			mapActDrawn.put(other, true);
		    		}
		    		if (canDrawRelation) {
		    			res += relationToPlantUml(r);
		    		} else {
			    		if (((RelationToObject) r).getActivityContainer() == null || ((RelationToObject) r).getActivityDstContainer() == null) {
			    			res += relationToPlantUml(r);
			    		} else {
			    			canDrawRelation = true;
			    			if (((RelationToObject) r).getActivityContainer().equals(act)) {
			    				res += activityToPlantUml(((RelationToObject) r).getActivityDstContainer());
			    			} else {
			    				res += activityToPlantUml(((RelationToObject) r).getActivityContainer());
			    			}
			    		}
		    		}
		    	}
		    	if (!mapActDrawn.get(act)) {
		    		res += "deactivate " + act.getObj().getName() + "\n";
		    	}
		    	canDrawRelation = false;
		    	listActDrawn.add(act);
			}
    	}
    	return res;

    }

    private String prepareCommentToPlantUml(String comment) {
        if (comment == null || comment.length() == 0) {
            return "";
        } else {
            return "\"" + comment + "\"";
        }
    }
}
