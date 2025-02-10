package fr.univrouen.umlreverse.model.io.parser.java;

import fr.univrouen.umlreverse.model.project.IAttribute;
import fr.univrouen.umlreverse.model.project.Variable;
import fr.univrouen.umlreverse.model.project.Visibility;
import fr.univrouen.umlreverse.model.project.Argument;
import fr.univrouen.umlreverse.model.project.IMethod;
import fr.univrouen.umlreverse.model.project.Modifier;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.project.IType;
import fr.univrouen.umlreverse.model.project.Type;
import fr.univrouen.umlreverse.model.project.Method;
import fr.univrouen.umlreverse.model.project.Attribute;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseRelationType;
import fr.univrouen.umlreverse.model.io.parser.util.clazz.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Visit a Java class's fields to get elements and relations in this file.
 * Needs a ParseClassDiagram filled by Java8ClassVisitor.
 */
public class Java8FieldsVisitor extends Java8BaseVisitor<Void> implements Java8Visitor<Void> {
    private static final String ADD_ENTITY_MESSAGE = "Insertion d'une entité inconnue : ";
    private static final String GET_ENTITY_MESSAGE = "Recherche d'une entité inconnue : ";
    private static final String UNRECOGNIZED_TYPE = "Type non reconnu : ";
    private static final String ARGS_ERROR = "Problème d'arguments dans la méthode ";

    private final ParseClassDiagram diagram;
    private final SortedMap<String, ParseClassObjectEntity> imports;
    private ParseClassPackage pack;
    private ParseClassObjectEntity entity;

    public Java8FieldsVisitor(ParseClassDiagram diagram) {
        super();
        this.diagram = diagram;
        this.imports = new TreeMap<>();
        this.pack = null;
        this.entity = null;
    }

    // PACKAGE
    @Override public Void visitPackageDeclaration(Java8Parser.PackageDeclarationContext ctx) {
        List<TerminalNode> packages = ctx.Identifier();
        pack = diagram.getRoot();
        for (TerminalNode n : packages) {
            ParseClassPackage np = pack.getPackage(n.getText());
            if (np == null)
                np = new ParseClassPackage(diagram, pack, n.getText());
            pack = np;
        }
        loadPackageImports();
        return visitChildren(ctx);
    }

    // IMPORTS
    @Override public Void visitSingleTypeImportDeclaration(Java8Parser.SingleTypeImportDeclarationContext ctx) {
        loadImport(ctx.typeName().getText());
        return null;
    }
    @Override public Void visitTypeImportOnDemandDeclaration(Java8Parser.TypeImportOnDemandDeclarationContext ctx) {
        loadImport(ctx.packageOrTypeName().getText() + ".*");
        return null;
    }
    @Override public Void visitSingleStaticImportDeclaration(Java8Parser.SingleStaticImportDeclarationContext ctx) {
        loadImport(ctx.typeName().getText() + "." + ctx.Identifier().getText());
        return null;
    }
    @Override public Void visitStaticImportOnDemandDeclaration(Java8Parser.StaticImportOnDemandDeclarationContext ctx) {
        loadImport(ctx.typeName().getText() + ".*");
        return null;
    }

    // CLASSES
    @Override public Void visitNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
        return setCurrentEntity(ctx.Identifier().getText(), ctx);
    }
    @Override public Void visitEnumDeclaration(Java8Parser.EnumDeclarationContext ctx) {
        return setCurrentEntity(ctx.Identifier().getText(), ctx);
    }
    @Override public Void visitNormalInterfaceDeclaration(Java8Parser.NormalInterfaceDeclarationContext ctx) {
        return setCurrentEntity(ctx.Identifier().getText(), ctx);
    }
    @Override public Void visitAnnotationTypeDeclaration(Java8Parser.AnnotationTypeDeclarationContext ctx) {
        return setCurrentEntity(ctx.Identifier().getText(), ctx);
    }

    // EXTENDS/IMPLEMENTS
    @Override public Void visitSuperclass(Java8Parser.SuperclassContext ctx) {
        ParseClassObjectEntity dst = getEntity(ctx.classType().Identifier().getText());
        diagram.getRelations().add(new ParseClassRelation(diagram, entity, "",
                ParseClassRelationType.EXTENDS.getRelationType(), "", dst, ""));
        loadExtendsImports(dst);
        return null;
    }
    @Override public Void visitInterfaceTypeList(Java8Parser.InterfaceTypeListContext ctx) {
        ParseRelationType type = ParseClassRelationType.IMPLEMENTS.getRelationType();
        if (entity.getType().equals(TypeEntity.Interface))
            type = ParseClassRelationType.EXTENDS.getRelationType();
        for (Java8Parser.InterfaceTypeContext c : ctx.interfaceType()) {
            ParseClassObjectEntity dst = getEntity(c.getText());
            dst.setType(TypeEntity.Interface);
            diagram.getRelations().add(new ParseClassRelation(diagram, entity, "",
                    type, "", dst, ""));
            loadExtendsImports(dst);
        }
        return null;
    }

    // FIELDS
    // Attribute declaration
    @Override public Void visitFieldDeclaration(Java8Parser.FieldDeclarationContext ctx) {
        Visibility vis = Visibility.Packaged;
        boolean isStatic = false;
        String isFinal = "";
        for (Java8Parser.FieldModifierContext fctx : ctx.fieldModifier()) {
            String mod = fctx.getText();
            switch (mod) {
                case "static":
                    isStatic = true;
                    break;
                case "final":
                    isFinal = "const";
                    break;
                default:
                    Visibility nvis = Visibility.getVisibility(mod);
                    if (nvis != null)
                        vis = nvis;
                    break;
            }
        }
        IType type = Type.getTypefromString(ctx.unannType().getText());
        if (type == null) {
            diagram.getLogger().addError(UNRECOGNIZED_TYPE + ctx.unannType().getText());
            return null;
        }
        for (Java8Parser.VariableDeclaratorContext vctx : ctx.variableDeclaratorList().variableDeclarator()) {
            String name = vctx.variableDeclaratorId().getText();
            IAttribute attr;
            if (vctx.variableInitializer() != null && !vctx.variableInitializer().getText().isEmpty()) {
                attr = new Attribute(vis, isFinal, new Variable(type, name, vctx.variableInitializer().getText()));
            } else {
                attr = new Attribute(vis, isFinal, new Variable(type, name, ""));
            }
            if (isStatic)
                attr.addModifier(Modifier.Static);
            entity.addAttribute(attr);
        }
        // ADD USES RELATION
        if (!entity.getGenerics().contains(type.getType())) {
            diagram.getRelations().add(new ParseClassRelation(
                    diagram,
                    entity,
                    "",
                    new ParseRelationType(
                            ParseRelationType.Head.ARROW,
                            ParseRelationType.Body.DASHES,
                            ParseRelationType.Tail.NONE),
                    "",
                    getEntity(type.getType()),
                    ""
            ));
        }
        return null;
    }

    // Method declaration
    @Override public Void visitMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        Visibility vis = Visibility.Packaged;
        boolean isStatic = false;
        boolean isAbstract = false;
        String isFinal = "";
        for (Java8Parser.MethodModifierContext mctx : ctx.methodModifier()) {
            String mod = mctx.getText();
            switch (mod) {
                case "static":
                    isStatic = true;
                    break;
                case "final":
                    isFinal = "const";
                    break;
                case "abstract":
                    isAbstract = true;
                    break;
                default:
                    Visibility nvis = Visibility.getVisibility(mod);
                    if (nvis != null)
                        vis = nvis;
                    break;
            }
        }
        Java8Parser.MethodHeaderContext headerCtx = ctx.methodHeader();
        IType type = Type.getTypefromString(headerCtx.result().getText());
        if (type == null) {
            diagram.getLogger().addError(UNRECOGNIZED_TYPE + headerCtx.result().getText());
            return null;
        }
        String name = headerCtx.methodDeclarator().Identifier().getText();
        IMethod method = new Method(vis, type, name);
        if (isStatic)
            method.addModifier(Modifier.Static);
        if (isAbstract)
            method.addModifier(Modifier.Abstract);
        method.setProperty(isFinal);

        // ARGS
        try {
            List<Argument> argList = new ArrayList<>();
            Java8Parser.FormalParameterListContext paramsCtx = headerCtx.methodDeclarator().formalParameterList();
            if (paramsCtx != null) {
                if (paramsCtx.formalParameters() != null) {
                    argList.addAll(paramsCtx.formalParameters().formalParameter().stream()
                            .map(param -> new Argument(Type.getTypefromString(param.unannType().getText()),
                            param.variableDeclaratorId().getText(), "")).collect(Collectors.toList()));
                }
                Java8Parser.FormalParameterContext lastParam = paramsCtx.lastFormalParameter().formalParameter();
                if (lastParam != null) {
                    argList.add(new Argument(Type.getTypefromString(lastParam.unannType().getText()),
                            lastParam.variableDeclaratorId().getText(), ""));
                } else {
                    Java8Parser.LastFormalParameterContext formalParam = paramsCtx.lastFormalParameter();
                    argList.add(new Argument(Type.getTypefromString(formalParam.unannType().getText() + "..."),
                            formalParam.variableDeclaratorId().getText(), ""));
                }
            }
            if (!argList.isEmpty())
                method.setArguments(argList);
        } catch (Exception e) {
            diagram.getLogger().addError(ARGS_ERROR + method.getName());
        }
        entity.addMethod(method);
        return null;
    }

    // Enum constant
    @Override public Void visitEnumConstant(Java8Parser.EnumConstantContext ctx) {
        entity.addEnum(ctx.Identifier().getText());
        return null;
    }

    // TOOLS
    private ParseClassObjectEntity getEntity(String name) {
        if (imports.containsKey(name))
            return imports.get(name);
        if (entity != null) {
            for (ParseClassObjectEntity objectEntity : pack.getEntities()) {
                if (objectEntity.getName().equals(entity.getName() + "." + name)) {
                    return objectEntity;
                }
            }
        }
        diagram.getLogger().addInfo(GET_ENTITY_MESSAGE + name);
        ParseClassObjectEntity e = diagram.getDefaultPackage().getOrAddObjectEntity(name);
        imports.put(name, e);
        return e;
    }

    private Void setCurrentEntity(String name, ParserRuleContext ctx) {
        ParseClassObjectEntity current = entity;
        entity = pack.getObjectEntity(name);
        if (entity == null)  {
            entity = pack.getObjectEntity(current.getName() + "." + name);
            if (entity == null) {
                entity = new ParseClassObjectEntity(diagram, pack, name);
                diagram.getLogger().addError(ADD_ENTITY_MESSAGE + entity.getFullName());
                pack.add(entity);
            }
        }
        if (current == null)
            loadEntityImports();
        visitChildren(ctx);
        entity = current;
        return null;
    }

    private void loadImport(String importName) {
        String[] names = importName.split("\\.");
        ParseClassPackage p = diagram.getRoot();
        for (int i = 0; i < names.length - 1; i++) {
            ParseClassPackage np = p.getPackage(names[i]);
            if (np == null)
                np = new ParseClassPackage(diagram, p, names[i]);
            p = np;
        }
        String name = names[names.length - 1];
        if (name.equals("*")) {
            for (ParseClassObjectEntity e : p.getEntities()) {
                if (e.getVisibility().equals(Visibility.Public)) {
                    imports.put(e.getName(), e);
                    //addAssocRelation(entity, e);
                }
            }
        } else {
            ParseClassObjectEntity e = p.getObjectEntity(name);
            if (e == null) {
                e = new ParseClassObjectEntity(diagram, p, name);
                diagram.getLogger().addInfo(ADD_ENTITY_MESSAGE + p.getFullName());
            } else {
                for (ParseClassObjectEntity inner : e.getParent().getEntities()) {
                    if (inner.getName().startsWith(e.getName() + ".")
                            && inner.getVisibility().equals(Visibility.Public)) {
                        imports.put(inner.getName(), inner);
                        //addAssocRelation(entity, inner);
                    }
                }
            }
            imports.put(e.getName(), e);
            //addAssocRelation(entity, e);
        }
    }

    private void loadPackageImports() {
        for (ParseClassObjectEntity e : pack.getEntities()) {
            if (!e.getVisibility().equals(Visibility.Private))
                imports.put(e.getName(), e);
        }
    }

    private void loadEntityImports() {
        for (ParseClassObjectEntity e : pack.getEntities()) {
            if (e.getName().startsWith(entity.getName() + "."))
                imports.put(e.getName(), e);
        }
    }

    private void loadExtendsImports(ParseClassObjectEntity extended) {
        for (ParseClassObjectEntity e : extended.getParent().getEntities()) {
            if (e.getName().startsWith(extended.getName() + ".")
                    && (e.getVisibility().equals(Visibility.Protected)
                    || e.getVisibility().equals(Visibility.Public)))
                imports.put(e.getName(), e);
        }
    }

    private void addAssocRelation(ParseClassObjectEntity src, ParseClassObjectEntity dst) {
        diagram.getRelations().add(new ParseClassRelation(
                diagram,
                src,
                "",
                new ParseRelationType(
                        ParseRelationType.Head.ARROW,
                        ParseRelationType.Body.DASHES,
                        ParseRelationType.Tail.NONE),
                "",
                dst,
                ""
        ));
    }
}
