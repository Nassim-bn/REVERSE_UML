package fr.univrouen.umlreverse.model.diagram.packag.visitor;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewPackage;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewRelation;
import fr.univrouen.umlreverse.model.diagram.clazz.visitor.AbstractClassVisitor;
import fr.univrouen.umlreverse.model.diagram.clazz.visitor.ExportXMIClassVisitor;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.packag.PackageDiagram;
import fr.univrouen.umlreverse.model.project.Argument;
import fr.univrouen.umlreverse.model.project.IAttribute;
import fr.univrouen.umlreverse.model.project.IMethod;
import fr.univrouen.umlreverse.model.project.IType;
import fr.univrouen.umlreverse.model.project.Modifier;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.util.Contract;

public class ExportXMIPackageVisitor extends ExportXMIClassVisitor {

	 	private File file;
	    private String error = null;
	    private Map<String, Integer> toId = new HashMap<>();
	    private Map<IViewRelation, Integer> toIdCompo = new HashMap<>();
	    private Map<IViewRelation, Integer> toIdAgreg = new HashMap<>();
	    private Map<IViewRelation, Integer> toIdNone = new HashMap<>();
	    private Map<IViewRelation, Integer> toIdLine = new HashMap<>();
	    private Map<IViewRelation, Integer> toIdHerit = new HashMap<>();
	    int idAtt = 0;
    	int idClassEnumInterface=0;
    	int idRelationHerit=0;
    	int idMethod=0;
    	int idMethodParam=0;
    	int idRelation=0;
    	int idRelationAgreg=0;
    	int idRelationComposi=0;
    	int idRelationNone=0;
    	int idRelationLine=0;
    	int idCard=0;
    	int idPackage=0;
    	

	    public ExportXMIPackageVisitor(String filename) {
	       super(filename);
	    }
	    public ExportXMIPackageVisitor(File file) {
	      super(file);
	    }

	    @Override
	    public void visit(ClassDiagram diagram) {
	    	 file.delete();
		        try {
					analyse(diagram);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    }

	    private void analyse(IClassDiagram diagram) throws IOException {
	    	
	    	
	        error = null;
	        BufferedWriter writer;
	        try {
	            writer = new BufferedWriter(new FileWriter(file));
	        } catch (IOException e) {
	            error = ErrorAbstraction.ErrorImpossibleToCreateFile.getCode();
	            return;
	        }

	        /******************************
	         * Code de l'écriture du fichier XMI
	         ******************************/
	        
	        writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	        writer.append("<xmi:XMI xmi:version=\"2.1\" xmlns:uml=\"http://schema.omg.org/spec/UML/2.3\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\">\n");
	        writer.append("<xmi:Documentation exporter=\"UmlReverse\" exporterVersion=\"1\"/>\n");
	        writer.append("<uml:Model xmi:type=\"uml:Model\" xmi:id=\"model\" name=\"xmiFile\">\n"); 
	        writer.append("<packagedElement xmi:type=\"uml:Package\" xmi:id=\"Package"+idPackage+"\" name =\""+diagram.getName()+"\">");
	        
	        for (IViewEntity entity : diagram.getEntities()) {
		        String name = entity.getAbsoluteName();
		        
		        if (!toId.containsKey(name)){
		            toId.put(name, toId.size());
				}
	        }
	        
	        
	        
	        
	        try {
				writePackagesAndEntities(diagram, writer);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	        
            try {
				writeRelations(diagram, writer);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
           
            writer.append("</packagedElement>\n");
	        writer.append("</uml:Model>\n");
	        writer.append("</xmi:XMI>\n");
	        
	        try {
	            writer.close();
	        } catch (IOException e) {
	            error = ErrorAbstraction.ErrorImpossibleToCloseTheFile.getCode();
	        }
	    }
	    
	   
}
	    
	    