package fr.univrouen.umlreverse.model.diagram.usecase;

import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.project.Project;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AEntityUsecaseTest {

    private IProject prj;
    private UsecaseDiagram diagram;
    private System UseGrp;
    private AEntityUsecase actor;

    @Before
    public void setUp() throws Exception {
        prj = new Project("a");
        diagram=new UsecaseDiagram(prj,"Diagramme de cas d'utilisation");
        UseGrp = new System(UUID.randomUUID().toString(), "Group Use Case",diagram);
        actor = new Actor("actor1",UseGrp);
    }

    @Test
    public void testGetSetName() throws Exception {
        assertEquals("actor1",actor.getName());

        actor.setName("NewNameActor");
        assertEquals("NewNameActor",actor.getName());
    }

    @Test
    public void testGetSetParentGroup() throws Exception {
        assertEquals(UseGrp,actor.getParentGroup());
        System grp1 = new System(UUID.randomUUID().toString(), "newGrp",diagram);
        actor.setParentGroup(grp1);
        assertEquals(grp1,actor.getParentGroup());
    }

    @Test
    public void testGetAddRemoveListNotes() throws Exception {
        INoteUsecase note = new NoteUsecase("newNote", diagram);
        actor.addNote(note);

        assertTrue(actor.getNotes().contains(note));

        actor.removeNote(note);
        assertFalse(actor.getNotes().contains(note));
    }
/*
    @Test
    public void testGetSetRemoveStyle() throws Exception {

    }
*/
}