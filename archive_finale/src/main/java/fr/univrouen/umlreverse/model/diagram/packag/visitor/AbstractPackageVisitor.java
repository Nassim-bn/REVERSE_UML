package fr.univrouen.umlreverse.model.diagram.packag.visitor;

import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.NoteClass;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewPackage;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewRelation;
import fr.univrouen.umlreverse.model.diagram.packag.PackageDiagram;


public abstract class AbstractPackageVisitor implements IPackageVisitor {
    @Override
    public void visit(PackageDiagram packageDiagram) {

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
