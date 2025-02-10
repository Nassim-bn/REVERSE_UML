package fr.univrouen.umlreverse.model.diagram.packag;

import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.packag.visitor.IPackageVisitor;
import fr.univrouen.umlreverse.model.diagram.visitor.IDiagramVisitor;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.util.RefusedAction;

public class PackageDiagram extends ClassDiagram implements IPackageDiagram {

	public PackageDiagram(IProject project, String name) throws RefusedAction {
		super(project, name);
	}

	@Override
	public void accept(IPackageVisitor visitor) {
        visitor.visit(this);
    }
	
	@Override
    public void accept(IDiagramVisitor visitor) {
        visitor.visit(this);
    }
	
}
