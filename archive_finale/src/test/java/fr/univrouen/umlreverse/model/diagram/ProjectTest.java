package fr.univrouen.umlreverse.model.diagram;



import fr.univrouen.umlreverse.model.project.Relation;
import fr.univrouen.umlreverse.model.project.Package;
import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.model.project.ObjectEntity;
import fr.univrouen.umlreverse.model.project.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.project.Visibility;
import fr.univrouen.umlreverse.model.project.IPackage;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.project.Project;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class ProjectTest {
    IProject p;

    @Before
    public void setUp() {
        p = Project.newProject("test");
    }


    @Test
    public void testNewProject() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        assertThat(p).isNotNull();
    }

    @Test
    public void testSaveLoadProject() throws Exception {
        String saveFile = "nomFichier.txt";
        List<IObjectEntity> listEntitiesToCompare = new LinkedList<>();
        //GIVEN
        IProject p = new Project("test");
        IObjectEntity tmpEntity = new ObjectEntity("class1", TypeEntity.Abstract, Visibility.Private);
        p.addEntity(tmpEntity);
        listEntitiesToCompare.add(tmpEntity);
        //WHEN
        p.serialize(new File(saveFile));
        IProject pr = Project.deserialize(new File(saveFile));
        //THEN
        assertThat(pr).isNotNull();
        assertThat(listEntitiesToCompare.size() == pr.getAllEntities().size()).isTrue();
        File f = new File(saveFile);
        f.delete();
    }

    @Test
    public void testGetListDiagrams() throws Exception {
        //GIVEN
        //WHEN
        Set<IDiagram> se = p.getDiagrams();
        //THEN
        assertThat(se.size()).isEqualTo(0);
    }

    @Test
    public void testGetListRelations() throws Exception {
        //GIVEN
        //WHEN
        Set<IRelation> se = p.getRelations();
        //THEN
        assertThat(se.size()).isEqualTo(0);
    }

    @Test
    public void testGetRootPackage() throws Exception {
        //GIVEN
        //WHEN
        IPackage pack = p.getRootPackage();
        //THEN
        assertThat(pack.getName()).isNull();
        assertThat(pack.getPackage()).isNull();
    }

    @Test
    public void testGetAllEntities() throws RefusedAction {
        //GIVEN

        Package pack1 = new Package("pack1", (Package)p.getRootPackage());
        Package pack2 = new Package("pack2", pack1);
        Package pack3 = new Package("pack3", pack2);
        Package pack4 = new Package("pack4", pack2);
        ObjectEntity oE = new ObjectEntity("name", TypeEntity.Abstract, Visibility.Protected);
        ObjectEntity oE2 = new ObjectEntity("name2", TypeEntity.Clazz, Visibility.Private);
        ObjectEntity oe3 = new ObjectEntity("name3", TypeEntity.Enumeration, Visibility.Packaged);
        ObjectEntity oe4 = new ObjectEntity("name4", TypeEntity.Interface, Visibility.Public);
        ObjectEntity oe5 = new ObjectEntity("name5", TypeEntity.Interface, Visibility.Public);
        pack1.addEntity(oE);
        pack2.addEntity(oE2);
        pack3.addEntity(oe3);
        pack4.addEntity(oe4);
        pack4.addEntity(oe5);

        //WHEN
        Set<IObjectEntity> li = p.getAllEntities();
        //THEN
        assertThat(li.size()).isEqualTo(5);
        assertThat(li.contains(oE)).isTrue();
        assertThat(li.contains(oE2)).isTrue();
        assertThat(li.contains(oe3)).isTrue();
        assertThat(li.contains(oe4)).isTrue();
        assertThat(li.contains(oe5)).isTrue();
    }
    @Test
    public void testAddRelation() throws Exception {
        //GIVEN
        String nameObjectEntityA = "nameObjectEntityA";
        String nameObjectEntityB = "nameObjectEntityB";
        IObjectEntity a = new ObjectEntity(nameObjectEntityA, TypeEntity.Clazz, Visibility.Public);
        IObjectEntity b = new ObjectEntity(nameObjectEntityB, TypeEntity.Enumeration, Visibility.Public);
        String nameRelation = "nameRelation";
        IRelation re = new Relation(nameRelation, a, b);
        //WHEN
        p.addRelation(re);
        //THEN
        assertThat(p.getRelations().size()).isEqualTo(1);
        assertThat(p.getRelations().contains(re)).isTrue();
    }

    @Test
    public void testRemoveEntity() throws Exception {
        //GIVEN
        String nameObjectEntityA = "nameObjectEntityA";
        String nameObjectEntityB = "nameObjectEntityB";
        IObjectEntity a = new ObjectEntity(nameObjectEntityA, TypeEntity.Clazz, Visibility.Public);
        IObjectEntity b = new ObjectEntity(nameObjectEntityB, TypeEntity.Enumeration, Visibility.Public);
        String nameRelation = "nameRelation";
        IRelation re = new Relation(nameRelation, a, b);
        p.addRelation(re);
        //WHEN
        p.removeEntity(a);
        //THEN
        assertThat(p.getRelations().contains(a)).isFalse();
    }

    @Test
    public void testRemoveDiagram() throws Exception {
        //GIVEN

        Set<IDiagram> listDiagrams = p.getDiagrams();
        IDiagram diag = new ClassDiagram(p, "name");
        listDiagrams.add(diag);
        //WHEN
        p.removeDiagram(diag);
        //THEN
        assertThat(p.getDiagrams().size()).isEqualTo(0);
    }

    @Test
    public void testRemoveRelation() throws Exception {
        //GIVEN
        String nameObjectEntityA = "nameObjectEntityA";
        String nameObjectEntityB = "nameObjectEntityB";
        IObjectEntity a = new ObjectEntity(nameObjectEntityA, TypeEntity.Clazz, Visibility.Public);
        IObjectEntity b = new ObjectEntity(nameObjectEntityB, TypeEntity.Enumeration, Visibility.Public);
        String nameRelation = "nameRelation";
        IRelation re = new Relation(nameRelation, a, b);
        p.addRelation(re);
        //WHEN
        p.removeRelation(re);
        //THEN
        assertThat(p.getRelations().size()).isEqualTo(0);
    }

    @Test
    public void testSaveAll() throws Exception {
        //GIVEN
        String filename = "newFile.txt";
        //WHEN
        p.serialize(new File(filename));
        //THEN

        assertThat(new File(filename).exists()).isTrue();
        File f = new File(filename);
        f.delete();
    }
}