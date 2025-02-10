package fr.univrouen.umlreverse.model.diagram.sequence.visitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.sequence.IActivity;
import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegmentCommon;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.util.Contract;

public class ExportXMISequenceVisitor {

	// ATTRIBUTS
	private static final String LIFELINE = "LIFELINE";
	private static final String COLLATTR = "COLLATTR";
	private static final String INTERACTION = "INTERACTION";
	private static final String ID = "0x505";
	private static final String ID_CLASS = "0x444719";
	private static final String ID_INSTANCE = "0x127563";

	private File file;
    private String error;
    private Map<IRelation, Integer> toId = new HashMap<>();
    private int activityID;
	private int objectID;
	private int relationID;
	private int segmentID;
	private int compteurLifeLine;


	// CONSTRUCTEURS
	public ExportXMISequenceVisitor(String fileName) {
		this(new File(fileName));
	}

	public ExportXMISequenceVisitor(File file) {
		Contract.check(file != null);

        this.file = file;
	}

	// REQUETES
	public String getError() {
        return error;
    }

	// COMMANDES
	public void visit(ISequenceDiagram diagram) {

        file.delete();

        try {
			analyse(diagram);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	private void analyse(ISequenceDiagram diagram) throws IOException {

        error = null;
        BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            error = ErrorAbstraction.ErrorImpossibleToCreateFile.getCode();
            return;
        }

        writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.append("<xmi:XMI xmi:version=\"2.1\" xmlns:uml=\"http://schema.omg.org/spec/UML/2.3\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\">\n");
        writer.append("<xmi:Documentation exporter=\"UmlReverse\" exporterVersion=\"1\"/>\n");
        writer.append("    <uml:Model xmi:type=\"uml:Model\" xmi:id=\"model\" name=\"xmiFile\">\n");

        try {
        	writeEntities(diagram, writer);
        } catch (IOException e) {
        	e.printStackTrace();
        }

        writer.append("    </uml:Model>\n");
        writer.append("</xmi:XMI>\n");

        try {
            writer.close();
        } catch (IOException e) {
            error = ErrorAbstraction.ErrorImpossibleToCloseTheFile.getCode();
        }
    }

	private void writeEntities(ISequenceDiagram diagram, BufferedWriter writer) throws IOException {

		for (IObject object : diagram.getObjects()) {
			writeObjects(object, writer);
		}

		for (IActivity activity : diagram.getActivities()) {
			writeActivities(activity, writer);
		}

		for (ISegmentCommon segment : diagram.getSegments()) {
			writeSegments(segment, writer);
		}

		for (IRelation relation : diagram.getRelations()) {
			writeRelations(relation, writer);
		}
	}

	public void writeObjects(IObject object, BufferedWriter writer) throws IOException {
		objectID++;

		writer.write("    <packagedElement xmi:type=\"uml:Collaboration\" xmi:id=\"BOUML_COLLABORATION_0x1f407_12\" name=\"o\">\"\n");

		writer.write("        <ownedBehavior xmi:type=\"uml:Interaction\" xmi:id=\"" + INTERACTION + "_" + ID + "\" name=\"sequence\">\n");
		writer.write("			  <lifeline xmi:type=\"uml:Lifeline\" xmi:id=\"" + LIFELINE + objectID + "_" + ID + "\" name=\"" + object.getName() + "\" represents=\"OBJECT" + objectID + "_" + ID + "\"/>\n");
		writer.write("		  </ownedBehavior>\n");

		writer.write("		  <ownedAttribute xmi:type=\"uml:Property\" xmi:id=\"OBJECT" + objectID + "_" + ID + "\" name=\"" + object.getName() + "\" type=\"" + ID + "\"/>\n");

		writer.write("	  </packagedElement>\n");

		writer.write("	  <packagedElement xmi:type=\"uml:Class\" name=\"" + object.getEntity().getName() + "\" xmi:id=\"CLASS_" + ID_CLASS + objectID + "\" visibility=\"package\">\n     </packagedElement>\n");
		writer.write("	  <packagedElement xmi:type=\"uml:InstanceSpecification\" xmi:id=\"INSTANCE_" + ID_INSTANCE + "_" + objectID + "\" name=\"" + object.getName() +"\">\n");
		writer.write("		  <classifier  xmi:idref=\"CLASS_" + ID_CLASS + objectID + "\"/>\n");
		writer.write("	  </packagedElement>\n");
	}

	// TODO récupérer id relation
	private void writeActivities(IActivity activity, BufferedWriter writer) throws IOException {
		activityID++;

		if (activityID == segmentID || activityID == relationID) {
			activityID++;
		}

		writer.write("        <ownedBehavior xmi:type=\"uml:Interaction\" xmi:id=\"" + INTERACTION + "_" + ID + "\" name=\"sequence\">\n");
		writer.write("			  <fragment xmi:type=\"uml:BehaviorExecutionSpecification\" xmi:id=\"ACTIVITY" + activityID + "_" + ID + "\" covered=\"" + LIFELINE + objectID + "_" + ID + "\" start=\"START" + objectID + "_" + ID + "\" finish=\"FINISH" + "_idRel" + "_" + ID +"\"/>\n");
		writer.write("		  </ownedBehavior>\n");
	}

	private void writeSegments(ISegmentCommon segment, BufferedWriter writer) throws IOException {
		segmentID++;

		if (activityID == segmentID || segmentID == relationID) {
			segmentID++;
		}

		writer.write("");
		writer.write("");
	}

	private void writeRelations(IRelation relation, BufferedWriter writer) throws IOException {

    	relationID++;

    	if (this.objectID == relationID || this.segmentID == relationID) {
    		relationID++;
		}

    	writer.write("");
    	writer.write("");
    	writer.write("");
        writer.write("");
    }
}
