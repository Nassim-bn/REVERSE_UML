package fr.univrouen.umlreverse.model.io.parser.plantuml;

import java.util.HashMap;
import java.util.Map;

import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.UsecaseDiagram;
import fr.univrouen.umlreverse.model.io.parser.util.clazz.ParseClassDiagram;
import fr.univrouen.umlreverse.model.io.parser.util.clazz.ParseClassNote;
import fr.univrouen.umlreverse.model.io.parser.util.clazz.ParseClassObjectEntity;
import fr.univrouen.umlreverse.model.io.parser.util.clazz.ParseClassPackage;
import fr.univrouen.umlreverse.model.io.parser.util.clazz.ParseClassRelation;
import fr.univrouen.umlreverse.model.io.parser.util.clazz.ParseClassRelationType;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseEntity;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseRelationType;
import fr.univrouen.umlreverse.model.io.parser.util.log.Logger;
import fr.univrouen.umlreverse.model.io.parser.util.usecase.ParseUsecaseActor;
import fr.univrouen.umlreverse.model.io.parser.util.usecase.ParseUsecaseCase;
import fr.univrouen.umlreverse.model.io.parser.util.usecase.ParseUsecaseDiagram;
import fr.univrouen.umlreverse.model.io.parser.util.usecase.ParseUsecaseEntity;
import fr.univrouen.umlreverse.model.io.parser.util.usecase.ParseUsecaseGroup;
import fr.univrouen.umlreverse.model.io.parser.util.usecase.ParseUsecaseNote;
import fr.univrouen.umlreverse.model.io.parser.util.usecase.ParseUsecaseRelation;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.util.RefusedAction;

/**
 * Visitor used to get a IDiagram from a PlantUML file.
 */
public class PlantUMLDiagramVisitor extends PlantUMLBaseVisitor<Void> {
    private static final String ERR_NEW_DIAG = "Impossible de créer le diagramme : ";
    private static final String UNK_RELATION = "Entité inconnue : ";
    private static final String ERR_RELATION = "Impossible d'ajouter la relation, entité inconnue.";
    private static final String ERR_CLASS_ASSOC = "Classes associations non supportées.";
    private static final String NOTE_ERR = "Note non reconnue.";
    private static final String UNKNOWN_TYPE = "Type d'entité non reconnu : ";

    private final IProject project;
    private final String diagramName;
    private final Logger logger;
    private final Map<String, ParseEntity<?, ?>> aliases;
    private ParseClassDiagram classDiagram;
    private ParseUsecaseDiagram usecaseDiagram;
    private ParseClassPackage pack;
    private ParseClassObjectEntity objectEntity;
    private ParseUsecaseGroup group;

    public PlantUMLDiagramVisitor(IProject project, String diagramName, Logger logger) {
        super();
        this.project = project;
        this.diagramName = diagramName;
        this.logger = logger;
        this.aliases = new HashMap<>();
        this.classDiagram = null;
        this.usecaseDiagram = null;
        this.pack = null;
        this.objectEntity = null;
        this.group = null;
    }

    /**
     * Return the log object containing parsing informations.
     * @return the log object containing parsing informations.
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Returns an IDiagram corresponding to the file's diagram.
     * @return an IDiagram corresponding to the file's diagram.
     */
    public IDiagram getModel() {
        if (usecaseDiagram != null)
            return usecaseDiagram.getModel();
        if (classDiagram != null)
            return classDiagram.getModel();
        return null;
    }

    @Override public Void visitEntryPoint(PlantUMLParser.EntryPointContext ctx) {
        return visitChildren(ctx);
    }

    @Override public Void visitUsecaseDiagram(PlantUMLParser.UsecaseDiagramContext ctx) {
        try {
            usecaseDiagram = new ParseUsecaseDiagram(new UsecaseDiagram(project, diagramName), logger);
            group = usecaseDiagram.getRoot();
            visitChildren(ctx);
            usecaseDiagram.addIntoModel();
        } catch (RefusedAction refusedAction) {
            logger.addError(ERR_NEW_DIAG + refusedAction.getMessage());
        }
        return null;
    }

    private String removeUsecaseChars(String name) {
        if (name.startsWith("\"") || name.startsWith(":") || name.startsWith("(")) {
            return name.substring(1, name.length() - 1);
        }
        return name;
    }

    @Override public Void visitUsecaseActorDef(PlantUMLParser.UsecaseActorDefContext ctx) {
        String name = getUsecaseID(ctx.name);
        name = removeUsecaseChars(name);
        String alias = name;
        if (ctx.label != null && !ctx.label.isEmpty()) {
            alias = getUsecaseID(ctx.label);
        }
        alias = removeUsecaseChars(alias);
        ParseUsecaseActor actor = new ParseUsecaseActor(usecaseDiagram, group, name);
        aliases.put(alias, actor);
        aliases.put(name, actor);
        return null;
    }

    @Override public Void visitUsecaseCaseDef(PlantUMLParser.UsecaseCaseDefContext ctx) {
        String name = getUsecaseID(ctx.name);
        name = removeUsecaseChars(name);
        String alias = name;
        if (ctx.label != null && !ctx.label.isEmpty()) {
            alias = getUsecaseID(ctx.label);
        }
        alias = removeUsecaseChars(alias);
        ParseUsecaseCase usecase = new ParseUsecaseCase(usecaseDiagram, group, name);
        aliases.put(alias, usecase);
        aliases.put(name, usecase);
        return null;
    }

    @Override public Void visitUsecaseRectangleDef(PlantUMLParser.UsecaseRectangleDefContext ctx) {
        /*String name = getUsecaseID(ctx.name);
        String alias = name;
        if (ctx.label != null && !ctx.label.isEmpty()) {
            alias = getUsecaseID(ctx.label);
        }
        ParseUsecaseGroup current = group;
        group = new ParseUsecaseGroup(usecaseDiagram, current, name);
        aliases.put(alias, group);
        aliases.put(name, group);*/
        visitChildren(ctx);
        //group = current;
        return null;
    }

    private ParseUsecaseEntity<?> addUsecaseEntity(String name) {
        ParseUsecaseEntity<?> entity;
        if (name.startsWith(":")) {
            entity = new ParseUsecaseActor(usecaseDiagram, group, name.substring(1, name.length() - 1));
        } else if (name.startsWith("(")) {
            entity = new ParseUsecaseCase(usecaseDiagram, group, name.substring(1, name.length() - 1));
        } else if (name.startsWith("\"")) {
            getLogger().addWarning(UNKNOWN_TYPE + name);
            entity = new ParseUsecaseCase(usecaseDiagram, group, name.substring(1, name.length() - 1));
        } else {
            getLogger().addWarning(UNKNOWN_TYPE + name);
            entity = new ParseUsecaseCase(usecaseDiagram, group, name);
        }
        aliases.put(entity.getName(), entity);
        return entity;
    }

    @Override public Void visitUsecaseArrow(PlantUMLParser.UsecaseArrowContext ctx) {
        ParseUsecaseEntity<?> dst = getUsecaseEntity(ctx.headID.getText());
        if (dst == null) {
            dst = addUsecaseEntity(ctx.headID.getText());
        }
        ParseUsecaseEntity<?> src = getUsecaseEntity(ctx.tailID.getText());
        if (src == null) {
            src = addUsecaseEntity(ctx.tailID.getText());
        }
        if (src ==  null || dst == null) {
            getLogger().addError(ERR_RELATION);
            return null;
        }
        String headLabel = "";
        if (ctx.headLabel != null)
            headLabel = ctx.headLabel.getText();
        String bodyLabel = "";
        if (ctx.bodyLabel != null)
            bodyLabel = ctx.bodyLabel.getText();
        String tailLabel = "";
        if (ctx.tailLabel != null)
            tailLabel = ctx.tailLabel.getText();
        String headArr = "";
        if (ctx.usecaseArrowType().arrowHead() != null)
            headArr = ctx.usecaseArrowType().arrowHead().getText();
        String bodyArr = ctx.usecaseArrowType().arrowBody().getText();
        String tailArr = "";
        if (ctx.usecaseArrowType().arrowTail() != null)
            tailArr = ctx.usecaseArrowType().arrowTail().getText();
        ParseRelationType type = ParseRelationType.fromString(headArr, bodyArr, tailArr);
        ParseUsecaseRelation relation = new ParseUsecaseRelation(usecaseDiagram, src, headLabel,
                type, tailLabel, dst, bodyLabel);
        usecaseDiagram.getRelations().add(relation);
        return null;
    }

    private String getUsecaseID(PlantUMLParser.UsecaseEntityIDContext ctx) {
        String id = ctx.getText();
        if ((ctx.escapedString() != null && !ctx.escapedString().isEmpty())
            || id.startsWith("(") || id.startsWith(":"))
            id = id.substring(1, id.length() - 1);
        return id;
    }

    // CLASS
    @Override public Void visitClassDiagram(PlantUMLParser.ClassDiagramContext ctx) {
        try {
            classDiagram = new ParseClassDiagram(new ClassDiagram(project, diagramName), logger);
            pack = classDiagram.getRoot();
            visitChildren(ctx);
            classDiagram.addIntoModel();
        } catch (RefusedAction refusedAction) {
            logger.addError(ERR_NEW_DIAG + refusedAction.getMessage());
        }
        return null;
    }

    @Override public Void visitClassPackageEntity(PlantUMLParser.ClassPackageEntityContext ctx) {
        ParseClassPackage current = pack;
        String name = ctx.classPackageName().getText();
        String alias = name;
        if (ctx.classPackageName().escapedString() != null && !ctx.classPackageName().escapedString().getText().isEmpty()) {
            alias = ctx.classPackageName().escapedString().getText();
            alias = alias.substring(1, alias.length() - 1);
            StringBuilder sb = new StringBuilder();
            String append = "";
            for (PlantUMLParser.ClassPackageIDContext currentCtx : ctx.classPackageName().classPackageID()) {
                sb.append(append);
                sb.append(currentCtx.getText());
            }
            name = sb.toString();
        }
        pack = new ParseClassPackage(classDiagram, current, name);
        aliases.put(alias, pack);
        aliases.put(name, pack);
        visitChildren(ctx);
        pack = current;
        return null;
    }

    @Override public Void visitClassEntity(PlantUMLParser.ClassEntityContext ctx) {
        String alias = ctx.classEntityID().getText();
        String name = alias;
        if (ctx.escapedString() != null && !ctx.escapedString().getText().isEmpty()) {
            name = ctx.escapedString().getText();
            name = name.substring(1, name.length() - 1);
        }
        String typeStr = ctx.classEntityType().getText();
        TypeEntity type = TypeEntity.Abstract;
        switch (typeStr) {
            case "class":
                type = TypeEntity.Clazz;
                break;
            case "interface":
                type = TypeEntity.Interface;
                break;
            case "enum":
                type = TypeEntity.Enumeration;
                break;
        }
        ParseClassObjectEntity entity = getObjectEntity(alias);
        if (entity != null) {
            objectEntity = entity;
            objectEntity.setType(type);
            objectEntity.setName(name);
            aliases.put(name, objectEntity);
        } else {
            objectEntity = new ParseClassObjectEntity(classDiagram, pack, type, name);
            aliases.put(alias, objectEntity);
            aliases.put(name, objectEntity);
        }
        visitChildren(ctx);
        return null;
    }

    @Override public Void visitClassInheritance(PlantUMLParser.ClassInheritanceContext ctx) {
        ParseClassObjectEntity dst = getObjectEntity(ctx.classEntityID().getText());
        ParseClassRelationType type = ParseClassRelationType.IMPLEMENTS;
        if (ctx.classInheritanceType().getText().equals("extends")) {
            type = ParseClassRelationType.EXTENDS;
        }
        ParseClassRelation relation = new ParseClassRelation(classDiagram, objectEntity, "",
                type.getRelationType(), "", dst, "");
        classDiagram.getRelations().add(relation);
        return null;
    }

    @Override public Void visitClassExternalField(PlantUMLParser.ClassExternalFieldContext ctx) {
        String name = ctx.classEntityID().getText();
        objectEntity = getObjectEntity(name);
        if (objectEntity == null) {
            objectEntity = new ParseClassObjectEntity(classDiagram, pack, name);
            aliases.put(name, objectEntity);
        }
        visitChildren(ctx);
        return null;
    }

    @Override public Void visitClassMethod(PlantUMLParser.ClassMethodContext ctx) {
        objectEntity.addField(ctx.getText().trim());
        return null;
    }

    @Override public Void visitClassAttribute(PlantUMLParser.ClassAttributeContext ctx) {
        objectEntity.addField(ctx.getText().trim());
        return null;
    }

    @Override public Void visitClassRelation(PlantUMLParser.ClassRelationContext ctx) {
        if (ctx.classAssociationClass() != null) {
            getLogger().addError(ERR_CLASS_ASSOC);
            return null;
        }
        PlantUMLParser.ArrowBodyContext bodyCtx = ctx.arrowBody();
        if (bodyCtx == null && ctx.classRelationType() != null && ctx.classRelationType().arrowBody() != null) {
            bodyCtx = ctx.classRelationType().arrowBody();
        }
        if (bodyCtx == null)
            return null;
        ParseClassObjectEntity dst = getObjectEntity(ctx.headID.getText());
        if (dst == null) {
            getLogger().addWarning(UNK_RELATION + ctx.headID.getText());
            dst = new ParseClassObjectEntity(classDiagram, pack, ctx.headID.getText());
            aliases.put(ctx.headID.getText(), dst);
        }
        String headLabel = "";
        if (ctx.headLabel != null)
            headLabel = ctx.headLabel.getText();
        String bodyLabel = "";
        if (ctx.bodyLabel != null)
            bodyLabel = ctx.bodyLabel.getText();
        String tailLabel = "";
        if (ctx.tailLabel != null)
            tailLabel = ctx.tailLabel.getText();
        String arrBody = bodyCtx.getText();
        String arrHead = "";
        String arrTail = "";
        ParseClassObjectEntity src = getObjectEntity(ctx.tailID.getText());
        if (src == null) {
            ParseClassNote note = getClassNote(ctx.tailID.getText());
            if (note != null) {
                ParseClassRelation relation = new ParseClassRelation(
                        classDiagram,
                        dst,
                        headLabel,
                        ParseClassRelationType.NOTE_LINK.getRelationType(),
                        tailLabel,
                        note,
                        bodyLabel
                );
                classDiagram.getRelations().add(relation);
                return null;
            } else {
                getLogger().addWarning(UNK_RELATION + ctx.tailID.getText());
                src = new ParseClassObjectEntity(classDiagram, pack, ctx.tailID.getText());
                aliases.put(ctx.tailID.getText(), src);
            }
        }
        if (ctx.classRelationType() != null) {
            if (ctx.classRelationType().arrowHead() != null)
                arrHead = ctx.classRelationType().arrowHead().getText();
            if (ctx.classRelationType().arrowTail() != null)
                arrTail = ctx.classRelationType().arrowTail().getText();
        }
        ParseRelationType type = new ParseRelationType(
                ParseRelationType.Head.fromString(arrHead),
                ParseRelationType.Body.fromString(arrBody),
                ParseRelationType.Tail.fromString(arrTail)
        );
        ParseClassRelation relation = new ParseClassRelation(
                classDiagram,
                src,
                headLabel,
                type,
                tailLabel,
                dst,
                bodyLabel
        );
        classDiagram.getRelations().add(relation);
        return null;
    }

    // COMMON
    @Override public Void visitNote(PlantUMLParser.NoteContext ctx) {
        String text;
        if (ctx.escapedString() != null && !ctx.escapedString().getText().isEmpty()) {
            text = ctx.escapedString().getText();
            text = text.substring(1, text.length() - 1);
        } else {
            if (ctx.endLineNoDoubleDots() != null)
                text = ctx.endLineNoDoubleDots().getText();
            else if (ctx.endLine() != null)
                text = ctx.endLine().getText();
            else if (ctx.noteContent() != null)
                text = ctx.noteContent().getText();
            else {
                getLogger().addError(NOTE_ERR);
                return null;
            }
        }
        String alias = "";
        if (ctx.ID() != null && !ctx.ID().getText().isEmpty())
            alias = ctx.ID().getText();
        if (classDiagram != null) {
            ParseClassNote note = new ParseClassNote(classDiagram, pack, text);
            if (!alias.isEmpty())
                aliases.put(alias, note);
            if (ctx.noteEntityID() != null) {
                ParseClassObjectEntity src = getObjectEntity(ctx.noteEntityID().getText());
                ParseClassRelation relation = new ParseClassRelation(classDiagram, src, "",
                        ParseClassRelationType.NOTE_LINK.getRelationType(), "", note, "");
                classDiagram.getRelations().add(relation);
            }
        } else {
            ParseUsecaseNote note = new ParseUsecaseNote(usecaseDiagram, group, text);
            if (ctx.noteEntityID() != null) {
                ParseUsecaseEntity<?> src = getUsecaseEntity(ctx.noteEntityID().getText());
                ParseUsecaseRelation relation = new ParseUsecaseRelation(usecaseDiagram, src, "",
                        ParseClassRelationType.NOTE_LINK.getRelationType(), "", note, "");
                usecaseDiagram.getRelations().add(relation);
            }
            if (!alias.isEmpty())
                aliases.put(alias, note);
        }
        return null;
    }

    @Override public Void visitLegend(PlantUMLParser.LegendContext ctx) {
        return null;
    }

    // TOOLS
    private ParseClassObjectEntity getObjectEntity(String name) {
        if (aliases.containsKey(name)) {
            ParseEntity<?, ?> entity = aliases.get(name);
            if (entity instanceof ParseClassObjectEntity)
                return (ParseClassObjectEntity) entity;
        }
        return pack.getObjectEntity(name);
    }

    private ParseClassNote getClassNote(String name) {
        if (aliases.containsKey(name)) {
            ParseEntity<?, ?> entity = aliases.get(name);
            if (entity instanceof ParseClassNote)
                return (ParseClassNote) entity;
        }
        return pack.getNote(name);
    }

    private ParseUsecaseEntity<?> getUsecaseEntity(String name) {
        if (aliases.containsKey(name)) {
            ParseEntity<?, ?> entity = aliases.get(name);
            if (entity instanceof ParseUsecaseEntity)
                return (ParseUsecaseEntity<?>) entity;
        }
        return group.get(name);
    }
}
