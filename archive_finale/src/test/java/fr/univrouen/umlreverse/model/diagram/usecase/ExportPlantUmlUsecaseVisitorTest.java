package fr.univrouen.umlreverse.model.diagram.usecase;

import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.Relation;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.usecase.visitor.ExportPlantUmlUsecaseVisitor;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.project.Project;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class ExportPlantUmlUsecaseVisitorTest {

    private IProject prj;
    private UsecaseDiagram diagram;
    private System usecaseSystem;
    private IRelation relation1,relation2,relation3,relation4,relation5,relation6,relation7,relation8,relation9;
    private Usecase usecase1,usecase2,usecase3,usecase4, usecase5;
    private Actor actor1,actor2,actor3,actor4,actor5;

    private INoteUsecase note1,note2,note3,note4;

    @Before
    public void setUp() throws Exception {
        prj = new Project("prjTest");
        diagram = new UsecaseDiagram(prj, "name diagram");
        diagram.setTitle("Test du Diagramme de cas d'utilisation");

        note1 = new NoteUsecase("texte de la note 1 avec actor1",diagram);
        note2 = new NoteUsecase("texte de la note 2 avec usecas1",diagram);
        note3 = new NoteUsecase("texte de la note 3 avec usecase4",diagram);
        note4 = new NoteUsecase("texte de la note 4 avec diagram",diagram);

        usecaseSystem = new System("1","system1",diagram);
        usecaseSystem.addNote(note3);
        diagram.addNote(note1);
        diagram.addNote(note2);

        actor1 = new Actor("Actor1", diagram.getRootSystem()); note1.addUsecaseEntity(actor1);
        actor2 = new Actor("Actor2", diagram.getRootSystem());
        actor3 = new Actor("Actor3", diagram.getRootSystem());
        actor4 = new Actor("Actor4", usecaseSystem);
        actor5 = new Actor("Actor5", usecaseSystem);

        usecase1 = new Usecase("Case1", diagram.getRootSystem()); note2.addUsecaseEntity(usecase1);
        usecase2 = new Usecase("Case2", diagram.getRootSystem());
        usecase3 = new Usecase("Case3", diagram.getRootSystem());
        usecase4 = new Usecase("Case4", usecaseSystem);note3.addUsecaseEntity(usecase4);
        usecase5 = new Usecase("Case5", usecaseSystem);

        relation1 = new Relation(diagram, actor1, usecase1, TypeHeadArrow.None, TypeHeadArrow.None, TypeLineArrow.Plain);
        relation2 = new Relation(diagram, actor2, usecase2, TypeHeadArrow.Aggregation, TypeHeadArrow.None, TypeLineArrow.Dashed);
        relation3 = new Relation(diagram, actor2, actor3, TypeHeadArrow.FullHead, TypeHeadArrow.None, TypeLineArrow.Plain);
        relation4 = new Relation(diagram, actor3, usecase3, TypeHeadArrow.None, TypeHeadArrow.None, TypeLineArrow.Plain);
        relation5 = new Relation(diagram, actor4, usecase4, TypeHeadArrow.Composition, TypeHeadArrow.None, TypeLineArrow.Plain);
        relation6 = new Relation(diagram, actor5, usecase5, TypeHeadArrow.None, TypeHeadArrow.EmptyHead, TypeLineArrow.Plain);
        relation7 = new Relation(diagram,usecase1,note1, TypeHeadArrow.None, TypeHeadArrow.EmptyHead, TypeLineArrow.Plain);

        usecaseSystem.addUsecase(usecase4);
        usecaseSystem.addUsecase(usecase5);

        diagram.addSystem(usecaseSystem);

        diagram.getRootSystem().addUsecase(usecase1);
        diagram.getRootSystem().addUsecase(usecase2);
        diagram.getRootSystem().addUsecase(usecase3);

        diagram.addUsecase(usecase1);
        diagram.addUsecase(usecase2);
        diagram.addUsecase(usecase3);

        diagram.addActor(actor1);
        diagram.addActor(actor2);
        diagram.addActor(actor3);
        diagram.addActor(actor4);
        diagram.addActor(actor5);

        diagram.addRelation(relation1); diagram.addRelation(relation2);
        diagram.addRelation(relation3); diagram.addRelation(relation4);
        diagram.addRelation(relation5); diagram.addRelation(relation6);

        diagram.addNote(note4);

    }

    @Test
    public void testanalyse() throws Exception {

        ExportPlantUmlUsecaseVisitor test = new ExportPlantUmlUsecaseVisitor("test");
        test.visit(diagram);
        new File("test").delete();
    }
}