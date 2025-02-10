package fr.univrouen.umlreverse.model.diagram.usecase;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.project.Project;

public class UsecaseTest {
    private IProject prj;
    private UsecaseDiagram diagram;
    private System UseGrp;
    private Actor actor;
    private Usecase Ucase;

    @Before
    public void setUp() throws Exception {
        prj = new Project("a");
        diagram=new UsecaseDiagram(prj,"Diagramme de cas d'utilisation");
        UseGrp = new System(UUID.randomUUID().toString(), "Group Use Case",diagram);
        actor = new Actor("actor1",UseGrp);
        Ucase = new Usecase("case1",UseGrp);
    }

    @Test
    public void testGetAddRemoveGeneralization() throws Exception {
        /*Generalization gener = new Generalization("generalization","",Ucase,actor);
        Ucase.addGeneralization(gener);

        assertEquals(gener.getNameRelation(),Ucase.getGeneralization("generalization").getNameRelation());
        assertEquals(gener.getComment(),Ucase.getGeneralization("generalization").getComment());
        assertEquals(gener.getDestination(),Ucase.getGeneralization("generalization").getDestination());
        assertEquals(gener.getSource(),Ucase.getGeneralization("generalization").getSource());

        //Test des niveau sup
        assertTrue(Ucase.getParentGroup().getListRelation().contains(gener));
        assertTrue(Ucase.getParentGroup().getParentDiagram().getListRelation().contains(gener));


        Ucase.RemoveGeneralization(gener);
        assertFalse(Ucase.getListGeneralization().contains(gener));
        assertFalse(Ucase.getParentGroup().getListRelation().contains(gener));
        assertFalse(Ucase.getParentGroup().getParentDiagram().getListRelation().contains(gener));*/
    }

    @Test
    public void testGetAddRemoveExtention() throws Exception {/*
        Extention extend = new Extention("Extends","",Ucase,actor);
        Ucase.addExtension(extend);

        assertEquals(extend.getNameRelation(),Ucase.getExtention("Extends").getNameRelation());
        assertEquals(extend.getComment(),Ucase.getExtention("Extends").getComment());
        assertEquals(extend.getDestination(),Ucase.getExtention("Extends").getDestination());
        assertEquals(extend.getSource(),Ucase.getExtention("Extends").getSource());

        //Test des niveau sup
        assertTrue(Ucase.getParentGroup().getListRelation().contains(extend));
        assertTrue(Ucase.getParentGroup().getParentDiagram().getListRelation().contains(extend));


        Ucase.RemoveExtention(extend);
        assertFalse(Ucase.getListExtension().contains(extend));
        assertFalse(Ucase.getParentGroup().getListRelation().contains(extend));
        assertFalse(Ucase.getParentGroup().getParentDiagram().getListRelation().contains(extend));*/
    }

    @Test
    public void testGetAddRemoveInclude() throws Exception {/*
        Include include = new Include("Include","",Ucase,actor);
        Ucase.addInclude(include);

        assertEquals(include.getNameRelation(),Ucase.getInclude("Include").getNameRelation());
        assertEquals(include.getComment(),Ucase.getInclude("Include").getComment());
        assertEquals(include.getDestination(),Ucase.getInclude("Include").getDestination());
        assertEquals(include.getSource(),Ucase.getInclude("Include").getSource());

        //Test des niveau sup
        assertTrue(Ucase.getParentGroup().getListRelation().contains(include));
        assertTrue(Ucase.getParentGroup().getParentDiagram().getListRelation().contains(include));


        Ucase.RemoveInclude(include);
        assertFalse(Ucase.getListExtension().contains(include));
        assertFalse(Ucase.getParentGroup().getListRelation().contains(include));
        assertFalse(Ucase.getParentGroup().getParentDiagram().getListRelation().contains(include));*/
    }
}