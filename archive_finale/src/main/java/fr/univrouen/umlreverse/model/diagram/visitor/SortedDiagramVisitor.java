package fr.univrouen.umlreverse.model.diagram.visitor;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.diagram.packag.IPackageDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.SequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.UsecaseDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;

/**
 * Returns a set of diagrams sorted by type.
 */
public class SortedDiagramVisitor implements IDiagramVisitor {
	private Map<Class<? extends IDiagram>, Set<IDiagram>> diagrams;

	public SortedDiagramVisitor() {
		diagrams = new HashMap<Class<? extends IDiagram>, Set<IDiagram>>();
	}
	
	// REQUESTS
	public Set<IDiagram> getDiagrams(Class<? extends IDiagram> type) {
		return Collections.unmodifiableSet(diagrams.getOrDefault(type, new HashSet<>()));
	}
	
	public Map<Class<? extends IDiagram>, Set<IDiagram>> getAllDiagrams() {
		return Collections.unmodifiableMap(diagrams);
	}
	
	// COMMANDS
	@Override
	public void visit(IClassDiagram diagram) {
		put(IClassDiagram.class, diagram);
	}

	@Override
	public void visit(IUsecaseDiagram diagram) {
		put(IUsecaseDiagram.class, diagram);
	}
	
	@Override
	public void visit(ISequenceDiagram diagram) {
		put(ISequenceDiagram.class, diagram);	
	}
	
	@Override
	public void visit(IPackageDiagram diagram) {
		put(IPackageDiagram.class, diagram);	
	}
	
	// TOOLS
	private void put(Class<? extends IDiagram> key, IDiagram value) {
		if (!diagrams.containsKey(key)) {
			diagrams.put(key, new HashSet<IDiagram>());
		}
		diagrams.get(key).add(value);
	}

	

	
}
