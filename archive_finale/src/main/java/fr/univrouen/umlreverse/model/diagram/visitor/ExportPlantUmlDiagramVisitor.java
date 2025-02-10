package fr.univrouen.umlreverse.model.diagram.visitor;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.visitor.ExportPlantUmlClassVisitor;
import fr.univrouen.umlreverse.model.diagram.packag.IPackageDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;
import java.io.File;

/**
 * A diagram visitor used to export to PlantUML
 */
public class ExportPlantUmlDiagramVisitor implements IDiagramVisitor {
    String error = null;
    File file;

    public ExportPlantUmlDiagramVisitor(File file) {
        this.file = file;
    }

    public String getError() {
        return error;
    }

    @Override
    public void visit(IClassDiagram diagram) {
        ExportPlantUmlClassVisitor visitor = new ExportPlantUmlClassVisitor(file);
        diagram.accept(visitor);
        error = visitor.getError();
    }

    @Override
    public void visit(IUsecaseDiagram diagram) {
        /**ExportPlantUmlUsecaseVisitor visitor = new ExportPlantUmlUsecaseVisitor(file);
        diagram.accept(visitor);
        error = visitor.getError();*/
    }

	@Override
	public void visit(ISequenceDiagram diagram) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IPackageDiagram diagram) {
		// TODO Auto-generated method stub
		
	}
}
