package fr.univrouen.umlreverse.ui.component.common.relation.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;

public enum RelationTypeEnum {
    NORMAL("Normal", TypeHeadArrow.None, TypeLineArrow.Plain, TypeHeadArrow.None, new Normal()),
    NOTE("Note", TypeHeadArrow.None, TypeLineArrow.Dashed, TypeHeadArrow.None, new Note()),
    ASSOCIATION("Association", TypeHeadArrow.None, TypeLineArrow.Plain, TypeHeadArrow.EmptyHead, new Association()), 
    DEPENDENCY("Dépendance", TypeHeadArrow.None, TypeLineArrow.Dashed, TypeHeadArrow.EmptyHead, new Dependency()), 
    AGGREGATION("Agrégation", TypeHeadArrow.Aggregation, TypeLineArrow.Plain, TypeHeadArrow.EmptyHead, new Aggregation()), 
    COMPOSITION("Composition", TypeHeadArrow.Composition, TypeLineArrow.Plain, TypeHeadArrow.EmptyHead, new Composition()), 
    INHERITANCE("Héritage", TypeHeadArrow.None, TypeLineArrow.Plain, TypeHeadArrow.FullHead, new Inheritance()), 
    IMPLEMENTATION("Implémentation", TypeHeadArrow.None, TypeLineArrow.Dashed, TypeHeadArrow.FullHead, new Implementation()),
    //SEQUENCE type
    SYNCHRONE("Synchrone", TypeHeadArrow.None, TypeLineArrow.Plain, TypeHeadArrow.FullHead, new Synchrone(40)),
    ASYNCHRONE("Asynchrone", TypeHeadArrow.None, TypeLineArrow.Plain, TypeHeadArrow.EmptyHead, new Asynchrone(40)),
    RETURN("Return", TypeHeadArrow.None, TypeLineArrow.Dashed, TypeHeadArrow.EmptyHead, new Return(40)),
    OWNER("Return", TypeHeadArrow.None, TypeLineArrow.Plain, TypeHeadArrow.EmptyHead, new Owner(40, 20)),
    CUSTOM("Personnalisé", null, null, null, new Custom());
    
    private final String toString;
    private final TypeHeadArrow tail;
    private final TypeLineArrow line;
    private final TypeHeadArrow head;
    private final RelationType group;
    
    RelationTypeEnum(String s, TypeHeadArrow tail, TypeLineArrow line, TypeHeadArrow head, RelationType group) {
        toString = s;
        this.tail = tail;
        this.line = line;
        this.head = head;
        this.group = group;
    }
        
    @Override
    public String toString() {
        return toString;
    }
    
    public TypeHeadArrow getTail() {
        return tail;
    }
    
    public TypeHeadArrow getHead() {
        return head;
    }
    
    public TypeLineArrow getLine() {
        return line;
    }
    
    public RelationType getGroup() {
        return group;
    }
    
    public static List<RelationTypeEnum> getClassTypes() {
        List<RelationTypeEnum> result = new ArrayList<>();
        Collections.addAll(result,
        		NORMAL, ASSOCIATION, DEPENDENCY, INHERITANCE,
        		IMPLEMENTATION, AGGREGATION, COMPOSITION);
        return result;
    }
    
    public static List<RelationTypeEnum> getUsecaseTypes() {
    	List<RelationTypeEnum> result = new ArrayList<>();
    	Collections.addAll(result,
    			NORMAL, ASSOCIATION, INHERITANCE, IMPLEMENTATION);
    	return result;
    }
    
    public static List<RelationTypeEnum> getSequenceTypes() {
    	List<RelationTypeEnum> result = new ArrayList<>();
    	Collections.addAll(result, SYNCHRONE, ASYNCHRONE, RETURN, OWNER);
    	return result;
    }
}
