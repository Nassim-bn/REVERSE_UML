package fr.univrouen.umlreverse.model.diagram.clazz.visitor;

import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.NoteClass;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewPackage;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewRelation;

/**
 * Created by zeddycus on 28/03/16.
 */
public abstract class AbstractClassVisitor implements IClassVisitor {
    @Override
    public void visit(ClassDiagram classDiagram) {

    }

    @Override
    public void visit(ViewEntity viewEntity) {

    }

    @Override
    public void visit(ViewPackage viewPackage) {

    }

    @Override
    public void visit(NoteClass note) {

    }

    @Override
    public void visit(ViewRelation viewRelation) {

    }

}
