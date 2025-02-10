package fr.univrouen.umlreverse.model.diagram.clazz.view;

import fr.univrouen.umlreverse.model.diagram.clazz.visitor.ExportPlantUmlClassVisitor;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.util.IStyleBox;
import fr.univrouen.umlreverse.model.diagram.util.StyleBox;
import fr.univrouen.umlreverse.model.project.*;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class ClassDiagramTest {
    IClassDiagram cd;
    IProject project;
    INoteClass note;
    @Before
    public void setUp() throws RefusedAction, PropertyVetoException {
        project = Project.newProject("test");
        cd = new ClassDiagram(project, "testClassDiagram");
        String id = "idNote";
        String text = "text of Note";
        note = new NoteClass(id, text, cd);
    }

    @Test
    public void testConstructor() {
        //GIVEN
        //WHEN
        //THEN
        assertThat(cd.getName()).isEqualTo("testClassDiagram");
        assertThat(cd.getProject()).isEqualTo(project);

    }

    @Test
    public void testAddNote() throws Exception {
        //GIVEN
        //WHEN
        cd.addNote(note);
        //THEN
        assertThat(cd.getNotes().size()).isEqualTo(1);
        assertThat(cd.getNotes().contains(note)).isTrue();
    }

    @Test
    public void testRemoveNote() throws Exception {
        //GIVEN
        cd.addNote(note);
        //WHEN
        cd.removeNote(note);
        //THEN
        assertThat(cd.getNotes()).isEmpty();
    }

    @Test
    public void testAddEntity() throws Exception {
        //GIVEN
        IProject project = Project.newProject("project");
        IClassDiagram cd = new ClassDiagram(project, "cd");
        IObjectEntity data  = new ObjectEntity("nameEntity", TypeEntity.Clazz, Visibility.Public);
        project.addEntity(data);
        IViewEntity ve = new ViewEntity(data, cd);
        //THEN
        assertThat(cd.getEntities().contains(ve)).isTrue();

    }

    @Test
    public void testRemoveEntity() throws Exception {
        IProject project = Project.newProject("test");
        IClassDiagram cd = new ClassDiagram(project, "testClassDiagram");
        //GIVEN
        IObjectEntity data  = new ObjectEntity("name1", TypeEntity.Clazz, Visibility.Public);
        IObjectEntity data2  = new ObjectEntity("name2", TypeEntity.Clazz, Visibility.Public);
        project.addEntity(data);
        project.addEntity(data2);
        IViewEntity ve = new ViewEntity(data, cd);
        IViewEntity ve2 = new ViewEntity(data2, cd);
        IRelation relation = new Relation("4", data, data2);
        IRelation relation2 = new Relation("5", data2, data);
        project.addRelation(relation);
        project.addRelation(relation2);
        new ViewRelation(relation, cd);
        new ViewRelation(relation2, cd);
        //WHEN
        cd.removeEntity(ve2);
        //THEN
        assertThat(cd.getEntities().size()).isEqualTo(1);
        assertThat(cd.getRelations().size()).isEqualTo(0);
    }

    @Test
    public void testAddPackage() throws Exception {
/*        //GIVEN
        String namePackage = "namePackage";
        IViewPackage pa = new ViewPackage(namePackage, cd);
        //WHEN
        cd.addPackage(pa);
        //THEN
        assertThat(cd.getPackages().contains(pa)).isTrue();*/
    }

    @Test
    public void testRemovePackage() throws Exception {
  /*      //GIVEN
        IStyleBox sb = new StyleBox();
        String namePackage = "namePackage";
        IViewPackage pa = new ViewPackage(namePackage, cd);
        //WHEN
        cd.removePackage(pa);
        //THEN
        assertFalse(cd.getPackages().contains(pa));*/
    }

    @Test
    public void testAddRelation() throws Exception {
        //GIVEN
        IObjectEntity a = new ObjectEntity("a", TypeEntity.Clazz, Visibility.Public);
        IObjectEntity b = new ObjectEntity("b",TypeEntity.Clazz, Visibility.Public);
        IRelation ir = new Relation("idRelation", a, b);
        project.addRelation(ir);
        IViewRelation vR = new ViewRelation(ir, cd);

        //WHEN
        cd.addRelation(vR);
        //THEN
        assertThat(cd.getRelations().size()).isEqualTo(1);
        assertThat(cd.getRelations().contains(vR));
    }

    @Test
    public void testRemoveRelation() throws Exception {
        //GIVEN
        IObjectEntity a = new ObjectEntity("a", TypeEntity.Clazz, Visibility.Public);
        IObjectEntity b = new ObjectEntity("b",TypeEntity.Clazz, Visibility.Public);
        IRelation ir = new Relation("idRelation", a, b);
        project.addRelation(ir);
        IViewRelation vR = new ViewRelation(ir, cd);
        cd.addRelation(vR);
        //WHEN
        cd.removeRelation(vR);
        //THEN
        assertThat(cd.getRelations()).isEmpty();
    }



    @Test
    public void vetoExceptionIfRedundancyNameInDiagram() {
        IObjectEntity a = new ObjectEntity("a", TypeEntity.Clazz, Visibility.Public);
        IObjectEntity b = new ObjectEntity("a",TypeEntity.Clazz, Visibility.Public);
        IProject project = new Project("test");
        try {
            project.getRootPackage().addEntity(a);
            project.getRootPackage().addEntity(b);
            fail();
        } catch (RefusedAction ignore) {
        }
    }

    @Test
    public void testExportPuml() throws RefusedAction {
        IObjectEntity data = new ObjectEntity("data1", TypeEntity.Clazz, Visibility.Public);
        IProject project = new Project("test");
        project.addEntity(data);
        IClassDiagram diag = new ClassDiagram(project, "d");
        diag.setTitle("Cacahuete power");

        IViewEntity a = new ViewEntity(data, diag);
        a.addEnumField("enum1");
        a.addEnumField("enum2");
        a.addEnumField("enum3");
        a.addAbstractText("abstract1");
        a.addAbstractText("abstract2");
        a.addAbstractText("abstract3");
        a.addAttribute(new Attribute(Visibility.Private, "prop",
                new Variable(Type.getTypefromString("int<a,b>"),"attr1")));
        a.addAttribute(new Attribute(Visibility.Public, "prop",
                new Variable(Type.getTypefromString("int<a,b>"),"attr2")));
        a.addAttribute(new Attribute(Visibility.Protected, "", new Variable(Type.getTypefromString("int<a,b>"),"attr3")));
        a.addMethod(new Method(Type.getTypefromString("int"), "method1"));
        a.addMethod(new Method(Type.getTypefromString("int"), "method2"));
        a.addMethod(new Method(Type.getTypefromString("int"), "method3"));

        data = new ObjectEntity("data22", TypeEntity.Clazz, Visibility.Public);
        project.addEntity(data);
        IViewEntity b = new ViewEntity(data, diag);
        b.addEnumField("enum1");
        b.addEnumField("enum2");
        b.addEnumField("enum3");
        b.addAbstractText("abstract1");
        b.addAbstractText("abstract2");
        b.addAbstractText("abstract3");
        b.addAttribute(new Attribute(Visibility.Private, "prop",
                new Variable(Type.getTypefromString("int<a,b>"),"attr1")));
        b.addAttribute(new Attribute(Visibility.Public, "prop",
                new Variable(Type.getTypefromString("int<a,b>"),"attr2")));
        b.addAttribute(new Attribute(Visibility.Protected, "", new Variable(Type.getTypefromString("int<a,b>"),"attr3")));
        b.addMethod(new Method(Type.getTypefromString("int"), "method1"));
        b.addMethod(new Method(Type.getTypefromString("int"), "method2"));
        b.addMethod(new Method(Type.getTypefromString("int"), "method3"));


        IRelation relation2 = new Relation("4", a.getData(), b.getData());
        project.addRelation(relation2);
        new ViewRelation(relation2, diag);
        relation2.setArrowHead(TypeHeadArrow.EmptyHead);
        relation2.setArrowTail(TypeHeadArrow.InternalClass);
        relation2.setAssociationEntity(a.getData());
        relation2.setLineArrow(TypeLineArrow.Dashed);
        relation2.setHeadCardinality("4..2");
        relation2.setTailCardinality("cardinality");
        relation2.setHeadComment("commentHead");
        relation2.setTailComment("commentTail");
        relation2.setNameRelation("name");
        INoteClass n1 = new NoteClass("5", "blabla1", diag);
        INoteClass n2 = new NoteClass("6", "blabla2", diag);
        n1.addEntity(a);
        n1.addEntity(b);
        n2.addEntity(a);

        ExportPlantUmlClassVisitor visitor = new ExportPlantUmlClassVisitor("testExport.puml");
        diag.accept(visitor);
        new File("testExport.puml").delete();
    }
}