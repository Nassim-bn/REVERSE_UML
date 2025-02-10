package fr.univrouen.umlreverse.model.diagram.clazz.visitor;

import fr.univrouen.umlreverse.model.diagram.clazz.view.*;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.project.Argument;
import fr.univrouen.umlreverse.model.project.IAttribute;
import fr.univrouen.umlreverse.model.project.IMethod;
import fr.univrouen.umlreverse.model.project.Modifier;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.util.Contract;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A visitor used to export a PlantUML file from a diagram.
 */
public class ExportPlantUmlClassVisitor extends AbstractClassVisitor {

    /**
     * The file.
     */
    private File file;
    private String error = null;
    private Map<String, Integer> toId = new HashMap<>();

    public ExportPlantUmlClassVisitor(String filename) {
        this(new File(filename));
    }
    public ExportPlantUmlClassVisitor(File file) {
        Contract.check(file != null);
        this.file = file;
    }

    @Override
    public void visit(ClassDiagram diagram) {
        file.delete();
        analyse(diagram);
    }

    public String getError() {
        return error;
    }

    private void analyse(IClassDiagram diagram) {
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
            writePackagesAndEntities(diagram, writer);
            writeRelations(diagram, writer);
            writeNotes(diagram, writer);
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

    private void writeNotes(IClassDiagram diagram, BufferedWriter writer) throws IOException {
        for (INoteClass note : diagram.getNotes()) {
            String id = "note" + note.getId();
            writer.write("note as " + id + "\n");
            writer.write(note.getText() + "\n");
            writer.write("end note\n");
            for (IViewEntity entity : note.getEntities()) {
                writer.write(id + " .. entity" + toId.get(entity.getAbsoluteName()) + "\n");
            }
        }
    }

    private void writeRelations(IClassDiagram diagram, BufferedWriter writer) throws IOException {
        for (IViewRelation relation : diagram.getRelations()) {
            writer.append(relationToPlantUml(relation) + "\n");
        }
    }

    private void writePackagesAndEntities(IClassDiagram diagram, BufferedWriter writer) throws IOException {
        for (IViewPackage p : diagram.getPackages()) {
            writePackage(writer, p);
        }
        for (IViewEntity entity : diagram.getEntities()) {
            writeEntities(entity, writer);
        }
    }

    private void writeEntities(IViewEntity entity, BufferedWriter writer) throws IOException {

        // open entity
        String type = "?(TypeEntity)";
        switch (entity.getType()) {
            case Abstract:
                type = "abstract class";
                break;
            case Clazz:
                type = "class";
                break;
            case Interface:
                type = "interface";
                break;
            case Enumeration:
                type = "enum";
                break;
        }
        String name = entity.getAbsoluteName();
        if (!toId.containsKey(name))
            toId.put(name, toId.size());
        writer.write(type + " \"" + entity.getAbsoluteName() + "\" as entity" + toId.get(name) + " {\n");

        // writing attributes
        for (IAttribute attribute : entity.getListAttribute()) {
            StringBuilder modifiers = new StringBuilder();
            for (Modifier m : attribute.getModifier()) {
                modifiers.append("{" + transformModifier(m) + "} ");
            }
            writer.write("\t" + attribute.getVisibility().getSymbol() + " "
                + modifiers
                + attribute.getVariable().getType() + " "
                + attribute.getVariable().getName() + " "
                + (attribute.getVariable().getInitialization() == null ? "" : attribute.getVariable().getInitialization()+ " ")
                + (attribute.getProperty() == null ? "" : attribute.getProperty()) + "\n"
            );
        }
        writer.write("\t--\n");

        // writing methods
        for (IMethod method : entity.getListMethod()) {
            StringBuilder modifiers = new StringBuilder();
            for (Modifier m : method.getModifier()) {
                modifiers.append("{" + transformModifier(m) + "} ");
            }
            StringBuilder args = new StringBuilder();
            Iterator<Argument> it = method.getArguments().iterator();
            if (it.hasNext()) {
                args.append(it.next());
            }
            while (it.hasNext()) {
                args.append(", " + it.next());
            }
            writer.write("\t" + method.getVisibility().getSymbol() + " "
                + modifiers
                + method.getReturn() + " "
                + method.getName() + " "
                + "(" + args + ") "
                + (method.getProperty() == null ? "" : method.getProperty()) + "\n"
            );
        }

        // writing enumFields
        if (entity.getEnumFields().size() > 0) {
            writer.write("\t--\n");
            for (String s : entity.getEnumFields()) {
                writer.write("\t" + s + "\n");
            }
        }

        // writing abstractText
        if (entity.getAbstractTexts().size() > 0) {
            writer.write("\t--\n");
            for (String s : entity.getAbstractTexts()) {
                writer.write("\t" + s + "\n");
            }
        }

        // close entity
        writer.write("}\n");
    }

    private String transformModifier(Modifier m) {
        switch (m) {
            case Abstract:
                return "abstract";
            case Static:
                return "static";
        }
        return "";
    }

    private void writePackage(BufferedWriter writer, IViewPackage p) throws IOException {
        writer.write("package " + p.getAbsoluteName() + " {\n");
        for (IViewEntity entity : p.getEntities()) {
            writeEntities(entity, writer);
        }
        writer.write("}\n");
    }
    
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

    private String relationToPlantUml(IViewRelation relation) {
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
        String entitySource = "entity" + toId.get(relation.getEntitySource().getAbsoluteName());
        String entityTarget = "entity" + toId.get(relation.getEntityTarget().getAbsoluteName());
        return entitySource + " " + commentTail + tail + line + line + head + commentHead + " " + entityTarget + commentCentral ;

    }

    private String prepareCommentToPlantUml(String comment) {
        if (comment == null || comment.length() == 0) {
            return "";
        } else {
            return "\"" + comment + "\"";
        }
    }
}
