package fr.univrouen.umlreverse.model.diagram.clazz.visitor;

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
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.project.Argument;
import fr.univrouen.umlreverse.model.project.IAttribute;
import fr.univrouen.umlreverse.model.project.IMethod;
import fr.univrouen.umlreverse.model.project.IType;
import fr.univrouen.umlreverse.model.project.Modifier;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.util.Contract;

public class ExportXMIClassVisitor extends AbstractClassVisitor {

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
    	

	    public ExportXMIClassVisitor(String filename) {
	        this(new File(filename));
	    }
	    public ExportXMIClassVisitor(File file) {
	        Contract.check(file != null);
	        this.file = file;
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

	    public String getError() {
	        return error;
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
           
	        writer.append("</uml:Model>\n");
	        writer.append("</xmi:XMI>\n");
	        
	        try {
	            writer.close();
	        } catch (IOException e) {
	            error = ErrorAbstraction.ErrorImpossibleToCloseTheFile.getCode();
	        }
	    }
	    
	    
	    //private void writeNotes(IClassDiagram diagram, BufferedWriter writer) throws IOException {}

	    protected void writeRelations(IClassDiagram diagram, BufferedWriter writer) throws IOException {
	    	 for (IViewRelation relation : diagram.getRelations()) {
	             relationToXMI(relation, writer);
	         }
	    }

	    protected void writePackagesAndEntities(IClassDiagram diagram, BufferedWriter writer) throws IOException {
	    	for (IViewPackage p : diagram.getPackages()) {
	            writePackage(diagram, writer, p);
	        }
	        for (IViewEntity entity : diagram.getEntities()) {
	            writeEntities(diagram, entity, writer);
	        }
	    }
	    
	    private void writeEntities(IClassDiagram diagram, IViewEntity entity, BufferedWriter writer) throws IOException {
	    		  
	    	// open entity
	        String type = "?(TypeEntity)";
	        switch (entity.getType()) {
	        	case Abstract:
	        		type = "Class";
	        		break;
	            case Clazz:
	                type = "Class";
	                break;
	            case Interface:
	                type = "Interface";
	                break;
	            case Enumeration:
	                type = "Enumeration";
	                break;
	        }
	    	
	        String name = entity.getAbsoluteName();
	        idClassEnumInterface++;
	        
	        writer.write("<packagedElement xmi:type=\"uml:"+type+"\" xmi:id=\"entity"+ toId.get(name) +"\" name=\""+entity.getAbsoluteName()+"\" visibility=\"" + entity.getVisibility() +"\" isAbstract=\""+classIsAbstract(entity)+"\" >\n");
	        
	        
	        // writing attributes
	        for (IAttribute attribute : entity.getListAttribute()) {
	            idAtt++;    
	            writer.write("<ownedAttribute xmi:type=\"uml:Property\" xmi:id=\"att" + idAtt + "\" name=\""+attribute.getVariable().getName()+"\" visibility=\""+attribute.getVisibility()+"\">\n");
     	        writer.write("<type xmi:type=\"uml:PrimitiveType\" href=\"http://schema.omg.org/spec/UML/2.3/uml.xml#"+typeConversion(attribute.getVariable().getType().toString())+"\"/>\n");
     	        writer.write("</ownedAttribute>\n");
	        }
	        
	        
	        // writing methods    
	        for (IMethod method : entity.getListMethod()) {
	            idMethod++;
	            
	            	writer.write("<ownedOperation xmi:type=\"uml:Operation\" name=\""+method.getName()+"\" xmi:id=\"method"+idMethod +"\" visibility=\""+ method.getVisibility() +"\" isAbstract=\""+methodIsAbstract(method.getModifier())+"\" isStatic=\""+methodIsStatic(method.getModifier())+"\" >\n");
	            	writer.write("<ownedParameter name=\"return\" xmi:id=\"return"+idMethod+"\" direction=\"return\">\n");
	            	writer.write("<type xmi:type=\"uml:PrimitiveType\" href=\"http://schema.omg.org/spec/UML/2.3/uml.xml#"+typeConversion(method.getReturn().toString())+"\"/>\n");
	            	writer.write("</ownedParameter>\n");
	            	
	            	for ( Argument argument : method.getArguments()){
	            		idMethodParam++;
	            		writer.write("<ownedParameter xmi:type=\"uml:Parameter\" name=\""+argument.getName()+"\" xmi:id=\"param"+idMethodParam+"\" direction=\"in\">\n");
	            		writer.write("<type xmi:type=\"uml:PrimitiveType\" href=\"http://schema.omg.org/spec/UML/2.3/uml.xml#"+typeConversion(argument.getType().toString())+"\"/>\n");
	            		writer.write("</ownedParameter>\n");
	            	}
	            	
	            writer.write("</ownedOperation>\n");
	            	
	          }
  
	        for (IViewRelation relation : diagram.getRelations()) {
	            if(entity.getAbsoluteName().equals(relation.getEntitySource().getAbsoluteName())){
	            	String head = typeHeadArrowToXMI(relation.getArrowHead());
	    	        String tail = typeHeadArrowToXMI(relation.getArrowTail());
	    	        String line = String.valueOf(typeLineArrowToXMI(relation.getLineArrow()));
	            	if(line.equals("Plain") && head.equals("FullHead") && tail.equals("None")){
		    	        String entityTarget = "entity" + toId.get(relation.getEntityTarget().getAbsoluteName());
		    	        String entitySource = "entity" + toId.get(relation.getEntitySource().getAbsoluteName());
		            	idRelationHerit++;
		            	toIdAgreg.put(relation, idRelationHerit);
		            	writer.write("<generalization xmi:type=\"uml:Generalization\" xmi:id=\"relation"+idRelationHerit+"\" general=\""+entityTarget+"\"/>\n");
		            	
	            	}else if(tail.equals("Aggregation")){ 
	            		idRelationAgreg++;
	            		toIdAgreg.put(relation, idRelationAgreg);
	            		idCard++;
       		
		            	String entityTarget = "entity" + toId.get(relation.getEntityTarget().getAbsoluteName());
		            	String entitySource = "entity" + toId.get(relation.getEntitySource().getAbsoluteName());
		            		
		            	String cardinalityTail = (relation.getHeadCardinality());
		            	String card1="";
		            	String card2="";

		            	String relation_normal = "relation_A" + idRelationAgreg;
		            	String relation_member = "relation_member_A" + idRelationAgreg;
		            	writer.write("<ownedAttribute xmi:type=\"uml:Property\" name=\"\" xmi:id=\""+relation_member+"\" visibility=\"private\" association=\""+relation_normal+"\" aggregation=\"none\">\n");
		            	writer.write("<type xmi:type=\"uml:Class\" xmi:idref=\""+entityTarget+"\"/>\n");
		            	if(cardinalityTail!=""){
		            		card1=cardinalityTail.substring(0, 1); 
		            		card2 = cardinalityTail.substring(cardinalityTail.length()-1, cardinalityTail.length()); 
		            		writer.write("<lowerValue xmi:type=\"uml:LiteralString\" xmi:id=\"multiplicity_lower_"+idCard+"\" value=\""+card1+"\"/>\n");
		            		writer.write("<upperValue xmi:type=\"uml:LiteralString\" xmi:id=\"multiplicity_upper_"+idCard+"\" value=\""+card2+"\"/>\n");
		            	}
		            	writer.write("</ownedAttribute>\n");
	            		
	            		
	            	}else if(tail.equals("Composition")){
	            		idRelationComposi++;
	    			    toIdCompo.put(relation, idRelationComposi);
	    			    idCard++;

	            		String entityTarget = "entity" + toId.get(relation.getEntityTarget().getAbsoluteName());
	            		String entitySource = "entity" + toId.get(relation.getEntitySource().getAbsoluteName());
	            		
	            		String cardinalityTail = (relation.getHeadCardinality());
	            		String card1="";
		            	String card2=""; 
	            		
	            		String relation_normal = "relation_C" + idRelationComposi;
	            		String relation_member = "relation_member_C" + idRelationComposi;
	            		writer.write("<ownedAttribute xmi:type=\"uml:Property\" name=\"\" xmi:id=\""+relation_member+"\" visibility=\"private\" association=\""+relation_normal+"\" aggregation=\"none\">\n");
	            		writer.write("<type xmi:type=\"uml:Class\" xmi:idref=\""+entityTarget+"\"/>\n");
	            		if(cardinalityTail!=""){
		            		card1=cardinalityTail.substring(0, 1); 
		            		card2 = cardinalityTail.substring(cardinalityTail.length()-1, cardinalityTail.length()); 
		            		writer.write("<lowerValue xmi:type=\"uml:LiteralString\" xmi:id=\"multiplicity_lower_"+idCard+"\" value=\""+card1+"\"/>\n");
		            		writer.write("<upperValue xmi:type=\"uml:LiteralString\" xmi:id=\"multiplicity_upper_"+idCard+"\" value=\""+card2+"\"/>\n");
	            		}	
	            		writer.write("</ownedAttribute>\n");
	            	}else if(line.equals("Plain") && head.equals("EmptyHead")){
	            		idRelationNone++;
	            		toIdNone.put(relation, idRelationNone);
	            		idCard++;
	            		
	            		String cardinalityTail = (relation.getHeadCardinality());
	            		String card1="";
		            	String card2="";  
	            		 
	            		String entityTarget = "entity" + toId.get(relation.getEntityTarget().getAbsoluteName());
	            		String entitySource = "entity" + toId.get(relation.getEntitySource().getAbsoluteName());
	            		String relation_normal = "relation_N" + idRelationNone;
	            		String relation_member = "relation_member_N" + idRelationNone;
	            		writer.write("<ownedAttribute xmi:type=\"uml:Property\" name=\"\" xmi:id=\""+relation_member+"\" visibility=\"private\" association=\""+relation_normal+"\" aggregation=\"none\">\n");
	            		writer.write("<type xmi:type=\"uml:Class\" xmi:idref=\""+entityTarget+"\"/>\n");
	            		if(cardinalityTail!=""){
		            		card1=cardinalityTail.substring(0, 1); 
		            		card2 = cardinalityTail.substring(cardinalityTail.length()-1, cardinalityTail.length()); 
		            		writer.write("<lowerValue xmi:type=\"uml:LiteralString\" xmi:id=\"multiplicity_lower_"+idCard+"\" value=\""+card1+"\"/>\n");
		            		writer.write("<upperValue xmi:type=\"uml:LiteralString\" xmi:id=\"multiplicity_upper_"+idCard+"\" value=\""+card2+"\"/>\n");
	            		}
	            		writer.write("</ownedAttribute>\n");
	            	}else if(line.equals("Plain") && head.equals("None") && tail.equals("None") && !tail.equals("Aggregation")  && !tail.equals("Composition")){
	            		idRelationLine++;
	            		toIdLine.put(relation, idRelationLine);
	            		
	            		idCard++;
	            		String cardinalityTail = (relation.getHeadCardinality());
	            		String card1="";
		            	String card2=""; 
	            		
	            		String entityTarget = "entity" + toId.get(relation.getEntityTarget().getAbsoluteName());
	            		String entitySource = "entity" + toId.get(relation.getEntitySource().getAbsoluteName());
	            		String relation_normal = "relation_normal" + idRelationLine;
	            		String relation_assoc = "relation_assoc" + idRelationLine;
	            		writer.write("<ownedAttribute xmi:type=\"uml:Property\" name=\"\" xmi:id=\""+relation_normal+"\" visibility=\"private\" association=\""+relation_assoc+"\" aggregation=\"none\">\n");
	            		writer.write("<type xmi:type=\"uml:Class\" xmi:idref=\""+entityTarget+"\"/>\n");
	            		if(cardinalityTail!=""){
		            		card1=cardinalityTail.substring(0, 1); 
		            		card2 = cardinalityTail.substring(cardinalityTail.length()-1, cardinalityTail.length()); 
		            		writer.write("<lowerValue xmi:type=\"uml:LiteralString\" xmi:id=\"multiplicity_lower_"+idCard+"\" value=\""+card1+"\"/>\n");
		            		writer.write("<upperValue xmi:type=\"uml:LiteralString\" xmi:id=\"multiplicity_upper_"+idCard+"\" value=\""+card2+"\"/>\n");
	            		}
	            		writer.write("</ownedAttribute>\n");
	            	}
	            	
	            }else if(entity.getAbsoluteName().equals(relation.getEntityTarget().getAbsoluteName())){
	            	String head = typeHeadArrowToXMI(relation.getArrowHead());
	    	        String tail = typeHeadArrowToXMI(relation.getArrowTail());
	    	        String line = String.valueOf(typeLineArrowToXMI(relation.getLineArrow()));
	    	      if(line.equals("Plain") && head.equals("None") && tail.equals("None") && !tail.equals("Aggregation")  && !tail.equals("Composition")){
	 		        	   	String entityTarget1 = "entity" + toId.get(relation.getEntityTarget().getAbsoluteName());
		 	            	String entitySource1 = "entity" + toId.get(relation.getEntitySource().getAbsoluteName());
		 	            		
		 	            	String cardinalityHead1 = (relation.getTailCardinality());
		 	            	String card11="";
		 		            String card21="";
		 		            	
		 		            if(cardinalityHead1!=""){
		 		            	idCard++;
		 	            		card11=cardinalityHead1.substring(0, 1); 
		 		            	card21 = cardinalityHead1.substring(cardinalityHead1.length()-1, cardinalityHead1.length()); 	
		 		            	String relation_normal = "relation_normal" + idRelationLine;
			            		String relation_assoc = "relation_assoc" + idRelationLine;
			            		writer.write("<ownedAttribute xmi:type=\"uml:Property\" name=\"\" xmi:id=\""+relation_normal+"\" visibility=\"private\" association=\""+relation_assoc+"\" aggregation=\"none\">\n");
			            		writer.write("<type xmi:type=\"uml:Class\" xmi:idref=\""+entitySource1+"\"/>\n");
		 		            	writer.write("<lowerValue xmi:type=\"uml:LiteralString\" xmi:id=\"multiplicity_lower_"+idCard+"\" value=\""+card11+"\"/>\n");
		 		            	writer.write("<upperValue xmi:type=\"uml:LiteralString\" xmi:id=\"multiplicity_upper_"+idCard+"\" value=\""+card21+"\"/>\n");
		 		            	writer.write("</ownedAttribute>\n");
		 		            }
		 		           
	            	 } 
	            		 
	            }
	         }
	        
	        writer.write("</packagedElement>\n");
	        
	    }
	    
	    public String typeConversion(String chaine){
	    	String chaine2 = "";
	    	if(chaine.equals("int")){
	    		chaine2= "Integer";
	    		return chaine2;
	    	}else if(chaine.equals("bool")){
	    		chaine2= "Boolean";
	    		return chaine2;
	    	}else if(chaine.equals("byte")){
	    		chaine2= "Byte";
	    		return chaine2;
	    	}else if(chaine.equals("char")){
	    		chaine2= "Char";
	    		return chaine2;
	    	}else if(chaine.equals("double")){
	    		chaine2= "Double";
	    		return chaine2;
	    	}else if(chaine.equals("float")){
	    		chaine2= "Float";
	    		return chaine2;
	    	}else if(chaine.equals("short")){
	    		chaine2= "Short";
	    		return chaine2;
	    	}else{
	    		return chaine ;
	    	}
	    }
	    
	    public boolean methodIsAbstract(Set<Modifier> property){
	    	if(property.contains(Modifier.Abstract)){
	    		return true;
	    	}else{
	    		return false;
	    	}	
		}
	    
	    public boolean methodIsStatic(Set<Modifier> property){
	    	if(property.contains(Modifier.Static)){
	    		return true;
	    	}else{
	    		return false;
	    	}	
		}
	    
	    public boolean classIsAbstract(IViewEntity entity){
	    	if (entity.getType().equals(TypeEntity.Abstract )) {
	    		return true;
	    	}
	    	else{
	    		return false;
	    	}
	    }
        
	    
	    private void writePackage(IClassDiagram diagram, BufferedWriter writer, IViewPackage p) throws IOException {
	        for (IViewEntity entity : p.getEntities()) {
	            writeEntities(diagram, entity, writer);
	        }
	       
	    }
	    
	    private String typeHeadArrowToXMI(TypeHeadArrow type) {
	        switch (type) {
	            case Aggregation:
	                return "Aggregation";
	            case Composition:
	                return "Composition";
	            case InternalClass:
	                return "InternalClass";
	            case FullHead:
	                return "FullHead";
	            case EmptyHead:
	                return "EmptyHead";
	            case None:
	                return "None";
	            default :
	                return "?";
	        }
	    }
	    
	    private String typeLineArrowToXMI (TypeLineArrow type) {
	        switch (type) {
	            case Plain:
	                return "Plain";
	            case Dashed:
	                return "Dashed";
	            default :
	                return "";
	        }
	    }
	    
	    private void relationToXMI(IViewRelation relation, BufferedWriter writer) throws IOException { 
	    	
	    	String entitySource = "entity" + toId.get(relation.getEntitySource().getAbsoluteName());
	        String entityTarget = "entity" + toId.get(relation.getEntityTarget().getAbsoluteName());
	    	
	    	
	    	String head = typeHeadArrowToXMI(relation.getArrowHead());
	        String tail = typeHeadArrowToXMI(relation.getArrowTail());
	        String line = String.valueOf(typeLineArrowToXMI(relation.getLineArrow()));
	        
	        if(tail.equals("Aggregation")){ 

            	int idrelationA= toIdAgreg.get(relation);
     	        writer.write("<packagedElement xmi:type=\"uml:Association\" xmi:id=\"relation_A"+idrelationA+"\" name=\""+relation.getNameRelation()+"\" visibility=\"private\">\n");
     	        writer.write("<memberEnd xmi:idref=\"relation_member_A"+idrelationA+"\"/>\n");
     	        writer.write("<ownedEnd xmi:type=\"uml:Property\" xmi:id=\"relation_reverse_A"+idrelationA+"\" association=\"relation_A"+idrelationA+"\" visibility=\"private\" type=\""+entitySource+"\" aggregation=\"shared\" isNavigable=\"false\"/>\n");
     	        writer.write("<memberEnd  xmi:idref=\"relation_reverse_A"+idrelationA+"\"/>\n");
     	        writer.write("</packagedElement>\n");
 	
	        }else if(tail.equals("Composition")){
	        	
		        int idrelationC= toIdCompo.get(relation);
		        writer.write("<packagedElement xmi:type=\"uml:Association\" xmi:id=\"relation_C"+idrelationC+"\" name=\""+relation.getNameRelation()+"\" visibility=\"private\">\n");
		        writer.write("<memberEnd xmi:idref=\"relation_member_C"+idrelationC+"\"/>\n");
		        writer.write("<ownedEnd xmi:type=\"uml:Property\" xmi:id=\"relation_reverse_C"+idrelationC+"\" association=\"relation_C"+idrelationC+"\" visibility=\"private\" type=\""+entitySource+"\" aggregation=\"composite\" isNavigable=\"false\"/>\n");
		        writer.write("<memberEnd  xmi:idref=\"relation_reverse_C"+idrelationC+"\"/>\n");
		        writer.write("</packagedElement>\n");
		        	
	        }else if(line.equals("Dashed") && head.equals("FullHead")){
	        	idRelation++;
	        	writer.write("<packagedElement xmi:type=\"uml:Realization\" xmi:id=\"relation"+idRelation+"\" client=\""+entitySource+"\" supplier=\""+entityTarget+"\" realizingClassifier=\""+entityTarget+"\">\n");
	        	writer.write("</packagedElement>\n");
	        }else if(line.equals("Dashed") && head.equals("EmptyHead")){
	        	idRelation++;
	        	writer.write("<packagedElement xmi:type=\"uml:Dependency\" xmi:id=\"relation"+idRelation+"\" client=\""+entitySource+"\" supplier=\""+entityTarget+"\">\n");
		        writer.write("</packagedElement>\n");
	        }
	        else if(line.equals("Plain") && head.equals("EmptyHead")){
	        	
            	int idrelationN= toIdNone.get(relation);
		        writer.write("<packagedElement xmi:type=\"uml:Association\" xmi:id=\"relation_N"+idrelationN+"\" name=\""+relation.getNameRelation()+"\" visibility=\"private\">\n");
		        writer.write("<memberEnd xmi:idref=\"relation_member_N"+idrelationN+"\"/>\n");
		        writer.write("<ownedEnd xmi:type=\"uml:Property\" xmi:id=\"relation_reverse_N"+idrelationN+"\" association=\"relation_N"+idrelationN+"\" visibility=\"private\" type=\""+entitySource+"\" aggregation=\"none\" isNavigable=\"false\"/>\n");
		        writer.write("<memberEnd  xmi:idref=\"relation_reverse_N"+idrelationN+"\"/>\n");
		        writer.write("</packagedElement>\n");
            	
	        }else if(line.equals("Plain") && head.equals("None") && tail.equals("None")  && !tail.equals("Aggregation")  && !tail.equals("Composition")){
	        	
	        	int idrelationL= toIdLine.get(relation);
	        	writer.write("<packagedElement xmi:type=\"uml:Association\" xmi:id=\"relation_assoc"+idrelationL+"\" name=\""+relation.getNameRelation()+"\" visibility=\"private\">\n");
	        	writer.write("<memberEnd xmi:idref=\"relation_normal"+idrelationL+"\"/>\n");
				writer.write("<memberEnd xmi:idref=\"relation_normal"+idrelationL+"\"/>\n");
				writer.write("</packagedElement>\n");
	        	
	        }
	    }
}


