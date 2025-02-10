package fr.univrouen.umlreverse.model.io.parser.java;

import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import fr.univrouen.umlreverse.model.io.parser.util.clazz.ParseClassDiagram;
import fr.univrouen.umlreverse.model.io.parser.util.clazz.ParseClassObjectEntity;
import fr.univrouen.umlreverse.model.io.parser.util.clazz.ParseClassPackage;
import fr.univrouen.umlreverse.model.io.parser.util.clazz.ParseClassRelation;
import fr.univrouen.umlreverse.model.io.parser.util.clazz.ParseClassRelationType;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.project.Visibility;

/**
 * Java class visitor, fills a ParseClassDiagram with all the class inside a Java file.
 */
public class Java8ClassVisitor extends Java8BaseVisitor<Void> {
    private final ParseClassDiagram diagram;
    private ParseClassPackage pack;
    private ParseClassObjectEntity entity;

    public Java8ClassVisitor(ParseClassDiagram diagram) {
        super();
        this.diagram = diagram;
        this.pack = null;
        this.entity = null;
    }

    @Override public Void visitPackageDeclaration(Java8Parser.PackageDeclarationContext ctx) {
        List<TerminalNode> packages = ctx.Identifier();
        pack = diagram.getRoot();
        for (TerminalNode n : packages) {
            ParseClassPackage np = pack.getPackage(n.getText());
            if (np == null)
                np = new ParseClassPackage(diagram, pack, n.getText());
            pack = np;
        }
        return visitChildren(ctx);
    }

    @Override public Void visitNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
        ParseClassObjectEntity current = entity;
        if (ctx.typeParameters() != null && !ctx.typeParameters().isEmpty())
            addEntity(TypeEntity.Clazz, ctx.Identifier().getText(), ctx.typeParameters(), ctx.classModifier());
        else
            addEntity(TypeEntity.Clazz, ctx.Identifier().getText(), null, ctx.classModifier());
        visitChildren(ctx);
        entity = current;
        return null;
    }

    @Override public Void visitEnumDeclaration(Java8Parser.EnumDeclarationContext ctx) {
        ParseClassObjectEntity current = entity;
        addEntity(TypeEntity.Enumeration, ctx.Identifier().getText(), null, ctx.classModifier());
        visitChildren(ctx);
        entity = current;
        return null;
    }

    @Override public Void visitNormalInterfaceDeclaration(Java8Parser.NormalInterfaceDeclarationContext ctx) {
        ParseClassObjectEntity current = entity;
        if (ctx.typeParameters() != null && !ctx.typeParameters().isEmpty())
            addInterface(ctx.Identifier().getText(), ctx.typeParameters(), ctx.interfaceModifier());
        else
            addInterface(ctx.Identifier().getText(), null, ctx.interfaceModifier());
        visitChildren(ctx);
        entity = current;
        return null;
    }

    @Override public Void visitAnnotationTypeDeclaration(Java8Parser.AnnotationTypeDeclarationContext ctx) {
        ParseClassObjectEntity current = entity;
        addInterface(ctx.Identifier().getText(), null, ctx.interfaceModifier());
        visitChildren(ctx);
        entity = current;
        return null;
    }

    private void addEntity(TypeEntity type, String name, Java8Parser.TypeParametersContext generics,
                           List<Java8Parser.ClassModifierContext> list) {
        ParseClassObjectEntity current = entity;

        if (current != null)  {
            name = entity.getName() + "." + name;
        }

        entity = new ParseClassObjectEntity(diagram, pack, type, name);
        if (generics != null) {
            for (Java8Parser.TypeParameterContext arg : generics.typeParameterList().typeParameter()) {
                entity.addGenerics(arg.Identifier().getText());
            }
        }

        if (current != null) {
            diagram.getRelations().add(new ParseClassRelation(diagram, current, "",
                    ParseClassRelationType.INTERNAL_CLASS.getRelationType(), "", entity, ""));
        }

        Visibility visibility = Visibility.Packaged;
        for (int i = 0; i < list.size() && visibility == Visibility.Packaged; i++) {
            Java8Parser.ClassModifierContext mctx = list.get(i);
            if (mctx.getText().equals("abstract")) {
                entity.setType(TypeEntity.Abstract);
            } else {
                Visibility newVisibility = Visibility.getVisibility(mctx.getText());
                if (newVisibility != null) visibility = newVisibility;
            }
        }
        entity.setVisibility(visibility);
        pack.add(entity);
    }

    private void addInterface(String name, Java8Parser.TypeParametersContext generics,
                              List<Java8Parser.InterfaceModifierContext> list) {
        if (entity != null) name = entity.getName() + "." + name;

        entity = new ParseClassObjectEntity(diagram, pack, TypeEntity.Interface, name);
        if (generics != null) {
            for (Java8Parser.TypeParameterContext arg : generics.typeParameterList().typeParameter()) {
                entity.addGenerics(arg.Identifier().getText());
            }
        }

        Visibility visibility = Visibility.Packaged;
        for (int i = 0; i < list.size() && visibility == Visibility.Packaged; i++) {
            Java8Parser.InterfaceModifierContext mctx = list.get(i);
            Visibility current = Visibility.getVisibility(mctx.getText());
            if (current != null) visibility = current;
        }
        entity.setVisibility(visibility);
        pack.add(entity);
    }
}
