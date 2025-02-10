package fr.univrouen.umlreverse.model.diagram.visitor;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.visitor.ExportXMIClassVisitor;
import fr.univrouen.umlreverse.model.diagram.packag.IPackageDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;

import java.io.File;

public class ExportXMIVisitor implements IDiagramVisitor {

	 String error = null;
	 File file;

	 public ExportXMIVisitor(File file) {
	     this.file = file;
	 }

	 public String getError() {
	     return error;
	 }

	 @Override
	 public void visit(IClassDiagram diagram) {
	     ExportXMIClassVisitor visitor = new ExportXMIClassVisitor(file);
	     diagram.accept(visitor);
	     error = visitor.getError();
	 }
	
	 @Override
	 public void visit(IUsecaseDiagram diagram) {
	 }

	 @Override
	 public void visit(ISequenceDiagram diagram) {	
	 }

	@Override
	public void visit(IPackageDiagram diagram) {
		// TODO Auto-generated method stub
		
	}

}
