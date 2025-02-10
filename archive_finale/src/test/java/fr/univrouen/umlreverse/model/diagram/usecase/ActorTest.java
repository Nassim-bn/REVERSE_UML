package fr.univrouen.umlreverse.model.diagram.usecase;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.project.Project;


public class ActorTest {

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
        /*
        Generalization gener = new Generalization("generalization","",actor,Ucase);
        actor.addGeneralization(gener);

        assertEquals(gener.getNameRelation(),actor.getGeneralization("generalization").getNameRelation());
        assertEquals(gener.getComment(),actor.getGeneralization("generalization").getComment());
        assertEquals(gener.getDestination(),actor.getGeneralization("generalization").getDestination());
        assertEquals(gener.getSource(),actor.getGeneralization("generalization").getSource());

        //Test des niveau sup
        assertTrue(actor.getParentGroup().getListRelation().contains(gener));
        assertTrue(actor.getParentGroup().getParentDiagram().getListRelation().contains(gener));


        actor.RemoveGeneralization(gener);
        assertFalse(actor.getListGeneralization().contains(gener));
        assertFalse(actor.getParentGroup().getListRelation().contains(gener));
        assertFalse(actor.getParentGroup().getParentDiagram().getListRelation().contains(gener));*/
    }

    @Test
    public void testGetAddRemoveAssociation() throws Exception {/*
        Association associ = new Association("Association","",actor,Ucase);
        actor.addAssociation(associ);

        assertEquals(associ.getNameRelation(),actor.getAssociation("Association").getNameRelation());
        assertEquals(associ.getComment(),actor.getAssociation("Association").getComment());
        assertEquals(associ.getDestination(),actor.getAssociation("Association").getDestination());
        assertEquals(associ.getSource(),actor.getAssociation("Association").getSource());

        //Test des niveau sup
        assertTrue(actor.getParentGroup().getListRelation().contains(associ));
        assertTrue(actor.getParentGroup().getParentDiagram().getListRelation().contains(associ));


        actor.RemoveAssociation(associ);
        assertFalse(actor.getListAssociation().contains(associ));
        assertFalse(actor.getParentGroup().getListRelation().contains(associ));
        assertFalse(actor.getParentGroup().getParentDiagram().getListRelation().contains(associ));*/
    }


}