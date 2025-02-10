package fr.univrouen.umlreverse.model.diagram.usecase;

import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.project.Project;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

import static org.junit.Assert.*;

public class SystemTest {

    private IProject prj;
    private UsecaseDiagram diagram;
    private System UseGrp;

    @Before
    public void setUp() throws Exception {
        prj = new Project("a");
        diagram=new UsecaseDiagram(prj,"Diagramme de cas d'utilisation");
        UseGrp = new System(UUID.randomUUID().toString(), "Group Use Case",diagram);
    }

    @Test
    public void testGetAbsoluteName() throws Exception {
        assertEquals("Group Use Case",UseGrp.getAbsoluteName());
        System UseGrpParent = new System(UUID.randomUUID().toString(), "Racine",diagram);

        UseGrp.setParentGroup(UseGrpParent);
        assertEquals("Racine/Group Use Case",UseGrp.getAbsoluteName());
    }

    @Test
    public void testGetSetParentGroup() throws Exception {
        assertNull(UseGrp.getParentGroup());

        System UseGrpParent = new System(UUID.randomUUID().toString(), "Racine",diagram);
        UseGrp.setParentGroup(UseGrpParent);
        assertEquals(UseGrpParent,UseGrp.getParentGroup());
    }

    @Test
    public void testGetSetParentDiagram() throws Exception {
        assertEquals(diagram,UseGrp.getDiagram());

        UsecaseDiagram diagram2=new UsecaseDiagram(prj,"Diagramme2");
        UseGrp.setDiagram(diagram2);
        assertEquals(diagram2,UseGrp.getDiagram());
    }

    @Test
    public void testGetSetUseCaseGroupname() throws Exception {
        assertEquals("Group Use Case",UseGrp.getNameGroup());

        UseGrp.setNameGroup("NewName");
        assertEquals("NewName",UseGrp.getNameGroup());
    }

    @Test
    public void testGetAddRemoveListUseCaseGroups() throws Exception {
        System UseGrp1 = new System(UUID.randomUUID().toString(), "GRP1",diagram);
        System UseGrp2 = new System(UUID.randomUUID().toString(), "GRP2",diagram);
        System UseGrp3 = new System(UUID.randomUUID().toString(), "GRP3",diagram);

        assertTrue(UseGrp.getGroups().isEmpty());

        UseGrp.addGroup(UseGrp1);
        UseGrp.addGroup(UseGrp2);
        UseGrp.addGroup(UseGrp3);
        assertTrue(UseGrp.getGroups().contains(UseGrp1)); assertTrue(UseGrp.getDiagram().getSystems().contains(UseGrp1));
        assertTrue(UseGrp.getGroups().contains(UseGrp2)); assertTrue(UseGrp.getDiagram().getSystems().contains(UseGrp2));
        assertTrue(UseGrp.getGroups().contains(UseGrp3)); assertTrue(UseGrp.getDiagram().getSystems().contains(UseGrp3));

        UseGrp.removeGroup(UseGrp1);
        assertFalse(UseGrp.getGroups().contains(UseGrp1)); assertFalse(UseGrp.getDiagram().getSystems().contains(UseGrp1));
    }

    @Test
    public void testGetAddRemoveListUseCases() throws Exception {
        Usecase Ucase1,Ucase2,Ucase3;
        Ucase1 = new Usecase("case1",UseGrp);
        Ucase2 = new Usecase("case2",UseGrp);
        Ucase3 = new Usecase("case3",UseGrp);

        assertTrue(UseGrp.getUseCases().isEmpty());

        UseGrp.addUsecase(Ucase1);
        UseGrp.addUsecase(Ucase2);
        UseGrp.addUsecase(Ucase3);
        assertTrue(UseGrp.getUseCases().contains(Ucase1)); assertTrue(UseGrp.getDiagram().getUsecases().contains(Ucase1));
        assertTrue(UseGrp.getUseCases().contains(Ucase2)); assertTrue(UseGrp.getDiagram().getUsecases().contains(Ucase2));
        assertTrue(UseGrp.getUseCases().contains(Ucase3)); assertTrue(UseGrp.getDiagram().getUsecases().contains(Ucase3));

        UseGrp.removeUsecase(Ucase1);
        assertFalse(UseGrp.getUseCases().contains(Ucase1)); assertFalse(UseGrp.getDiagram().getUsecases().contains(Ucase1));
    }

    @Test
    public void testGetAddRemoveListNotes() throws Exception {
        INoteUsecase note1 = new NoteUsecase("note1", diagram);
        INoteUsecase note2 = new NoteUsecase("note2", diagram);

        assertTrue(UseGrp.getNotes().isEmpty());

        UseGrp.addNote(note1);
        UseGrp.addNote(note2);
        assertTrue(UseGrp.getNotes().contains(note1)); assertTrue(UseGrp.getDiagram().getNotes().contains(note1));
        assertTrue(UseGrp.getNotes().contains(note2)); assertTrue(UseGrp.getDiagram().getNotes().contains(note2));

        UseGrp.removeNote(note1);
        assertFalse(UseGrp.getNotes().contains(note1)); assertFalse(UseGrp.getDiagram().getNotes().contains(note1));
    }

    @Test
    public void testGetAddRemoveListRelation() throws Exception {
        String nameRelation="Relation binaire",comment="rel";
        AEntityUsecase source = new Actor("Actor1",UseGrp),
                destination = new Actor("Actor2",UseGrp),
                destination2 = new Usecase("useCase1",UseGrp);
/*
        ARelation relation = new Generalization(nameRelation,comment,source,destination);
        ARelation relation2 = new Association(nameRelation,comment,source,destination2);

        assertTrue(UseGrp.getListRelation().isEmpty());

        UseGrp.addRelation(relation);
        UseGrp.addRelation(relation2);
        assertTrue(UseGrp.getListRelation().contains(relation)); assertTrue(UseGrp.getParentDiagram().getListRelation().contains(relation));
        assertTrue(UseGrp.getListRelation().contains(relation2)); assertTrue(UseGrp.getParentDiagram().getListRelation().contains(relation2));

        UseGrp.removeRelation(relation);
        assertFalse(UseGrp.getListRelation().contains(relation)); assertFalse(UseGrp.getParentDiagram().getListRelation().contains(relation));
  */  }
/*
    @Test
    public void testGetAddRemoveStyle() throws Exception {

    }
*/
}