package fr.univrouen.umlreverse.model.diagram.usecase.visitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.usecase.AEntityUsecase;
import fr.univrouen.umlreverse.model.diagram.usecase.IActor;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecase;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.util.Contract;

public class ExportXMIUseCaseVisitor extends AUsecaseVisitor {

	// ATTRIBUTS
	private File file;
    private String error;
    private Map<AEntityUsecase, String> toId = new HashMap<>();
    private Map<IRelation, Integer> toIdRel = new HashMap<>();
    private int actorID;
	private int useCaseID;
	private int relationID;
	private String idA;
	private String idUC;

	// CONSTRUCTEURS
	public ExportXMIUseCaseVisitor(String fileName) {
		this(new File(fileName));
	}

	public ExportXMIUseCaseVisitor(File file) {
		Contract.check(file != null);

        this.file = file;
	}

	// REQUETES
	public String getError() {
        return error;
    }

	// COMMANDES
	@Override
    public void visit(IUsecaseDiagram diagram) {

        file.delete();

        try {
			analyse(diagram);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	private void analyse(IUsecaseDiagram diagram) throws IOException {

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

        for (AEntityUsecase actor : diagram.getActors()) {
	        if (!toId.containsKey(actor)){
	            toId.put(actor, "act_" + toId.size());
	            actorID++;
			}
        }

        for (IUsecase useCase : diagram.getUsecases()) {
	        if (!toId.containsKey(useCase)){
	            toId.put((AEntityUsecase) useCase, "useC_" + toId.size());
	            useCaseID++;
			}
        }

        try {
        	writeEntities(diagram, writer);
        } catch (IOException e) {
        	e.printStackTrace();
        }

        try {
			writeRelations(diagram, writer);
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

	private void writeEntities(IUsecaseDiagram diagram, BufferedWriter writer) throws IOException {

		for (IActor actor : diagram.getActors()) {
			writeActors(actor, writer);
		}

		for (IUsecase useCase : diagram.getUsecases()) {
			writeUseCases(useCase, writer);
		}
	}

	private void writeActors(IActor actor, BufferedWriter writer) throws IOException {


		actorID++;

		if (actorID == useCaseID || actorID == relationID) {
			actorID++;
		}

		//idA = "act_" + actorID;
		idA = toId.get(actor);


		writer.write("        <packagedElement xmi:type=\"uml:Actor\" name=\"" + actor.getName() + "\" xmi:id=\"" + idA + "\" visibility=\"package\">");
		writer.write("</packagedElement>\n");
	}

	// TODO faire une map pour les id.
	private void writeUseCases(IUsecase useCase, BufferedWriter writer) throws IOException {
		useCaseID++;

		if (actorID == useCaseID || useCaseID == relationID) {
			useCaseID++;
		}

		//idUC = "useC_" + useCaseID;
		idUC = toId.get(useCase);


		writer.write("        <packagedElement xmi:type=\"uml:UseCase\" xmi:id=\"" + idUC + "\" name=\"" + useCase.getName() + "\">\n");
		if (!useCase.getRelations().isEmpty()) {
			for (IRelation rel : useCase.getRelations()) {
				if (useCase.equals(rel.getEntitySource())) {
					String head = typeHeadArrowToXMI(rel.getArrowHead());
	    	        String tail = typeHeadArrowToXMI(rel.getArrowTail());
	    	        String line = String.valueOf(typeLineArrowToXMI(rel.getLineArrow()));
	            	if (line.equals("Plain") && head.equals("FullHead") && tail.equals("None")) {
	            		String sourceID = toId.get(rel.getEntitySource());
		    	        String targetID = toId.get(rel.getEntityTarget());
						writer.write("            <generalization xmi:type=\"uml:Generalization\" xmi:id=\"" + idUC + "_" + sourceID + "\" general=\"" + targetID + "\"/>\n");
					}
				}
			}
		}
		writer.write("        </packagedElement>\n");
	}

	private void writeRelations(IUsecaseDiagram diagram, BufferedWriter writer) throws IOException {
   	 	for (IRelation relation : diagram.getRelations()) {
   	 		toIdRel.put(relation, (int) Math.random());
   	 		relationToXMI(relation, writer);
        }
    }

	private String typeLineArrowToXMI (TypeLineArrow type) {
        switch (type) {
            case Plain:
                return "Plain";
            case Dashed:
                return "Dashed";
            default :
                return "";
        }
    }

	private String typeHeadArrowToXMI(TypeHeadArrow type) {
        switch (type) {
            case Aggregation:
                return "Aggregation";
            case Composition:
                return "Composition";
            case InternalClass:
                return "InternalClass";
            case FullHead:
                return "FullHead";
            case EmptyHead:
                return "EmptyHead";
            case None:
                return "None";
            default :
                return "?";
        }
    }

	private void relationToXMI(IRelation relation, BufferedWriter writer) throws IOException {

		String actorID = relation.getEntitySource().getId();
		String useCaseID = relation.getEntityTarget().getId();

    	relationID++;

    	if (this.actorID == relationID || this.useCaseID == relationID) {
    		relationID++;
		}

    	String head = typeHeadArrowToXMI(relation.getArrowHead());
        String tail = typeHeadArrowToXMI(relation.getArrowTail());
        String line = String.valueOf(typeLineArrowToXMI(relation.getLineArrow()));
    	if (line.equals("Dashed") && head.equals("FullHead") && tail.equals("None")) {
    		writer.write("        <packagedElement xmi:type=\"uml:Dependency\" xmi:id=\"BOUML_0x1f407_9\" client=\"BOUML_0x1f407_5\" supplier=\"BOUML_0x1f487_5\">");
    		writer.write("</packagedElement>\n");
    	} else if (line.equals("Plain") && head.equals("EmptyHead") && tail.equals("None") || line.equals("Plain") && head.equals("None") && tail.equals("None")) {
	    	writer.write("        <packagedElement xmi:type=\"uml:Association\" xmi:id=\"ASSOC_" + relationID + "\">\n");
	    	writer.write("            <ownedEnd xmi:type=\"uml:Property\" xmi:id=\"assoc_" + idA + "_" + relationID + "\" type=\"" + idA + "\" association=\"ASSOC_" + relationID + "\">");
	    	writer.write("</ownedEnd>\n");
	    	writer.write("            <ownedEnd xmi:type=\"uml:Property\" xmi:id=\"assoc_" + idUC + "_" + relationID + "\" type=\"" + idUC + "\" association=\"ASSOC_" + relationID + "\">");
	    	writer.write("</ownedEnd>\n");
	        writer.write("        </packagedElement>\n");
		}
    }
}
