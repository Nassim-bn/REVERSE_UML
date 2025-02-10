package fr.univrouen.umlreverse.model.diagram.usecase.visitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fr.univrouen.umlreverse.model.diagram.clazz.visitor.AbstractClassVisitor;
import fr.univrouen.umlreverse.model.diagram.common.AEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.Note;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.usecase.AEntityUsecase;
import fr.univrouen.umlreverse.model.diagram.usecase.Actor;
import fr.univrouen.umlreverse.model.diagram.usecase.IEntityUsecase;
import fr.univrouen.umlreverse.model.diagram.usecase.INoteUsecase;
import fr.univrouen.umlreverse.model.diagram.usecase.ISystem;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecase;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.util.Contract;

/**
 * ExportPlantUmlUsecaseVisitor class that permits to export a use case diagram into plant uml file
 */
public class ExportPlantUmlUsecaseVisitor extends AbstractClassVisitor {

    /**
     * The file.
     */
    File file;
    String error = null;

    /**
     * Constructor
     * @param filename
     */
    public ExportPlantUmlUsecaseVisitor(String filename) {
        this(new File(filename));
    }

    /**
     * Constructor
     * @param file
     */
    public ExportPlantUmlUsecaseVisitor(File file) {
        Contract.check(file != null);
        this.file = file;
    }

    /**
     * visit allow to visit the diagram
     * @param diagram
     */
    public void visit(IUsecaseDiagram diagram) {
        file.delete();
        analyse(diagram);
    }

    /**
     * analyse permits to translate all the commencement of the use case diagram into their plant uml corespondent
     * @param diagram
     */
    private void analyse(IUsecaseDiagram diagram) {
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
            writer.append("\n");
            if(diagram.getTitle()!=null){
                writer.append("title "+diagram.getTitle()+"\n");
                writer.append("\n");
            }

            //Ajout de notes
            writeNotes(diagram,writer);
            writer.append("\n");

            //Ajoute des acteurs
            writeActors(diagram,writer);
            writer.append("\n");
            //Ajout du rectangle par defaut
            writeRootUsecase(diagram,writer);
            writer.append("\n");

            //Ajout de tout les rectangles
            writeSystemUsecase(diagram,writer);
            writer.append("\n");

            //Ajout des relations
            writeRelations(diagram,writer);
            writer.append("\n");

            writer.append("@enduml\n");
        } catch (IOException e) {
            error = ErrorAbstraction.ErrorImpossibleToWriteInFile.getCode();
            try { writer.close();} catch (IOException ignored) {}
            return;
        }

        try {
            writer.close();
        } catch (IOException e) {
            error = ErrorAbstraction.ErrorImpossibleToCloseTheFile.getCode();
        }
    }

    /**
     * writeActors allows to convert an actor into its plant uml representation
     * @param diagram
     * @param writer
     * @throws IOException
     */
    private void writeActors(IUsecaseDiagram diagram, BufferedWriter writer) throws IOException {
        for (Actor a : diagram.getActors()){
            String id =  a.getId().substring(0,4);
            String txt = a.getName().replaceAll("(\\r\\n|\\n)","\\\\n");
            writer.append("actor :"+ txt+": as "+id+"\n");
        }
    }

    /**
     * writeRootUsecase allows to convert the default use case diagram group into its plant uml representation
     * @param diagram
     * @param writer
     * @throws IOException
     */
    private void writeRootUsecase(IUsecaseDiagram diagram, BufferedWriter writer) throws IOException {
        for (IUsecase u : diagram.getUsecases()){
            String id = u.getId().substring(0,4);
            String txt = u.getName().replaceAll("(\\r\\n|\\n)","\\\\n");
            writer.append("usecase (" + txt + " ) as "+id+" \n");
        }
    }

    /**
     * writeSystemUsecase allows to convert the use case diagram groups into their plant uml representations
     * @param diagram
     * @param writer
     * @throws IOException
     */
    private void writeSystemUsecase(IUsecaseDiagram diagram, BufferedWriter writer) throws IOException {
        for (ISystem system : diagram.getSystems()){
            writer.append("rectangle "+system.getNameGroup()+"{\n");
            for (IUsecase u : system.getUseCases()){
                    String id = u.getId().substring(0,4);
                    String txt = u.getName().replaceAll("(\\r\\n|\\n)","\\\\n");
                    writer.append("usecase (" + txt + " ) as "+id+" \n");
            }
            writer.append("}\n");
        }
    }

    /**
     * writeNotes allows to convert the use case diagram notes into their plant uml representation
     * @param diagram
     * @param writer
     * @throws IOException
     */
    private void writeNotes(IUsecaseDiagram diagram, BufferedWriter writer) throws IOException {
        for (INoteUsecase note : diagram.getNotes()) {
            String id = 'n' + note.getId().substring(0,4);
            String txt = note.getText().replaceAll("(\\r\\n|\\n)","\\\\n");
            writer.write("note \"" + txt + "\" as " + id + "\n");
            for (IEntityUsecase entity : note.getUsecaseEntities()) {
                writer.write(id + " .. " + entity.getName() + "\n");
            }
        }
    }

    /**
     * writeRelations allows to convert the use case diagram relations into their plant uml representation
     * @param diagram
     * @param writer
     * @throws IOException
     */
    private void writeRelations(IUsecaseDiagram diagram, BufferedWriter writer) throws IOException {
        for (IRelation relation : diagram.getRelations()) {
            writer.append(relationToPlantUml(relation) + "\n");
        }
    }

    /**
     * typeHeadArrowToPlantUml allows to give the right head and tail relation
     * @param type
     * @param isTail
     * @return String
     */
    private String typeHeadArrowToPlantUml(TypeHeadArrow type, boolean isTail) {
        switch (type) {
            case Aggregation:
                return "o";
            case Composition:
                return "*";
            case InternalClass:
                return "+";
            case FullHead:
                return isTail ? "<|" : "|>";
            case EmptyHead:
                return isTail ? "<" : ">";
            case None:
                return "";
            default :
                return "?";
        }
    }

    /**
     * typeLineArrowToPlantUml allows to give the right line relation
     * @param type
     * @return String
     */
    private char typeLineArrowToPlantUml (TypeLineArrow type) {
        switch (type) {
            case Plain:
                return '-';
            case Dashed:
                return '.';
            default :
                return '?';
        }
    }

    /**
     * relationToPlantUml allows to concat the different parts of the relation
     * @param relation
     * @return String
     */
    private String relationToPlantUml(IRelation relation) {
        String commentHead = prepareCommentToPlantUml(relation.getHeadCardinality());
        if (commentHead.length() > 0) {
            commentHead = " " + commentHead;
        }
        String commentTail = prepareCommentToPlantUml(relation.getTailCardinality());
        if (commentTail.length() > 0) {
            commentTail = commentTail + " ";
        }
        String commentCentral = prepareCommentToPlantUml(relation.getNameRelation());
        if (commentCentral.length() > 0) {
            commentCentral = " : " + commentCentral;
        }
        String head = typeHeadArrowToPlantUml(relation.getArrowHead(), false);
        String tail = typeHeadArrowToPlantUml(relation.getArrowTail(), true);
        String line = String.valueOf(typeLineArrowToPlantUml(relation.getLineArrow()));


        if((relation.getEntitySource() instanceof AEntityUsecase) && (relation.getEntityTarget() instanceof AEntityUsecase)){
            AEntityUsecase  source = (AEntityUsecase) relation.getEntitySource();
            AEntityUsecase destination = (AEntityUsecase) relation.getEntityTarget();

            String entitySource=source.getId().substring(0,4);
            String entityTarget=destination.getId().substring(0,4);

            return entitySource + " " + commentTail + tail + line + line + head + commentHead + " " + entityTarget + commentCentral ;
        }

        if((relation.getEntitySource() instanceof AEntityUsecase) && (relation.getEntityTarget() instanceof Note)){
            AEntityUsecase  source = (AEntityUsecase) relation.getEntitySource();
            AEntityRelation destination = (Note) relation.getEntityTarget();

            String entitySource=source.getId().substring(0,4);
            String entityTarget="n"+destination.getId().substring(0,4);

            return entitySource + " " + commentTail + tail + line + line + head + commentHead + " " + entityTarget + commentCentral ;
        }

        if((relation.getEntityTarget() instanceof AEntityUsecase) && (relation.getEntitySource() instanceof Note)){
            AEntityRelation  source = (Note) relation.getEntitySource();
            AEntityUsecase destination = (AEntityUsecase) relation.getEntityTarget();

            String entitySource="n"+source.getId().substring(0,4);
            String entityTarget=destination.getId().substring(0,4);

            return entitySource + " " + commentTail + tail + line + line + head + commentHead + " " + entityTarget + commentCentral ;
        }

        if((relation.getEntitySource() instanceof Note) && (relation.getEntityTarget() instanceof Note)){
            AEntityRelation  source = (Note) relation.getEntitySource();
            AEntityRelation destination = (Note) relation.getEntityTarget();

            String entitySource="n"+source.getId().substring(0,4);
            String entityTarget="n"+destination.getId().substring(0,4);

            return entitySource + " " + commentTail + tail + line + line + head + commentHead + " " + entityTarget + commentCentral ;
        }
         return "?";
    }

    private String prepareCommentToPlantUml(String comment) {
        if (comment == null || comment.length() == 0) {
            return "";
        } else {
            return "\"" + comment + "\"";
        }
    }

}
