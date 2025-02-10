package fr.univrouen.umlreverse.model.diagram.clazz;

import fr.univrouen.umlreverse.model.project.Package;
import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.model.project.ObjectEntity;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.project.Visibility;
import fr.univrouen.umlreverse.model.project.IPackage;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;


public class PackageTest {

    private IPackage pa;
    @Before
    public void setUp() throws Exception {
        pa = new Package("ThisIsPACKAGE");
    }

    @Test
    public void testConstructeur1arg() {
        //GIVEN
        //WHEN
        //THEN
        assertThat(pa.getName()).isEqualTo("ThisIsPACKAGE");
        assertThat(pa.getPackage()).isEqualTo(null);
    }

    @Test
    public void testGetPackage() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        assertThat(pa.getPackage()).isEqualTo(null);
    }

    @Test
    public void testGetAbsoluteName() throws Exception {
        //GIVEN
        String pack = "ThisISTheParentPACKAGE";
        pa.setPackage(new Package(pack));
        //WHEN
        String s = pa.getAbsoluteName();
        //THEN
        //System.out.println(s);
        assertThat(s).isEqualTo(pack + "." + pa.getName());
    }

    @Test
    public void testGetName() throws Exception {
        //GIVEN
        //WHEN
        //THEN
        assertThat(pa.getName()).isEqualTo("ThisIsPACKAGE");
    }

    @Test
    public void testGetPackages() throws Exception {
        //GIVEN
        String pack = "ThisISTheParentPACKAGE";
        IPackage paa = new Package(pack);
        pa.addPackage(paa);
        //WHEN
        Set<IPackage> se = pa.getPackages();
        //THEN
        assertThat(se.size()).isEqualTo(1);
        assertThat(se.contains(paa)).isTrue();
    }

    @Test
    public void testGetEntities() throws Exception {
        //GIVEN
        Set<IObjectEntity> se = pa.getListEntities();
        ObjectEntity objEnt = new ObjectEntity("datTest", TypeEntity.Clazz,Visibility.Public);
        se.add(objEnt);
        //WHEN
        Set<IObjectEntity> res = pa.getEntities(TypeEntity.Clazz);
        //THEN
        assertThat(res.size()).isEqualTo(1);
        assertThat(res.contains(objEnt));
        assertThat(pa.getEntities(TypeEntity.Abstract)).isEmpty();
        assertThat(pa.getEntities(TypeEntity.Enumeration)).isEmpty();
        assertThat(pa.getEntities(TypeEntity.Interface)).isEmpty();
    }

    @Test
    public void testGetEntities1() throws Exception {
        //GIVEN
        Set<IObjectEntity> se = pa.getListEntities();
        String name = "datTest";
        ObjectEntity objEnt = new ObjectEntity(name, TypeEntity.Clazz,Visibility.Public);
        se.add(objEnt);
        //WHEN
        Set<IObjectEntity> res = pa.getEntities(name);
        //THEN
        assertThat(res.size()).isEqualTo(1);
        assertThat(res.contains(objEnt));
        assertThat(pa.getEntities("salut")).isEmpty();
    }

    @Test
    public void testGetListEntities() throws Exception {
        //GIVEN
        //WHEN
        Set<IObjectEntity> se = pa.getListEntities();
        //THEN
        assertThat(se.size()).isEqualTo(0);
    }

    @Test
    public void testSetPackage() throws Exception {
        //GIVEN
        String pack = "ThisISTheParentPACKAGE";
        IPackage parent = new Package(pack);
        //WHEN
        pa.setPackage(parent);
        //THEN
        assertThat(pa.getPackage()).isEqualTo(parent);
    }

    @Test
    public void testSetName() throws Exception {
        //GIVEN
        String name = "nom";
        //WHEN
        pa.setName(name);
        //THEN
        assertThat(pa.getName()).isEqualTo(name);
    }

    @Test
    public void testAddPackage() throws Exception {
        //GIVEN
        String pack = "namePackage";
        IPackage parent = new Package(pack);
        //WHEN
        pa.addPackage(parent);
        //THEN
        assertThat(pa.getPackages().size()).isEqualTo(1);
        assertThat(pa.getPackages().contains(parent)).isTrue();
    }

    @Test
    public void testRemovePackage() throws Exception {
        //GIVEN
        String pack = "namePackage";
        IPackage parent = new Package(pack);
        pa.addPackage(parent);
        //WHEN
        pa.removePackage(parent);
        //THEN
        assertThat(pa.getPackages().size()).isEqualTo(0);
    }

    @Test
    public void testAddEntity() throws Exception {
        //GIVEN
        String s = "nameEntity";
        IObjectEntity objEnt = new ObjectEntity(s,TypeEntity.Clazz, Visibility.Public);
        //WHEN
        pa.addEntity(objEnt);
        //THEN
        assertThat(pa.getListEntities().size()).isEqualTo(1);
        assertThat(pa.getListEntities().contains(objEnt));

    }

    @Test
    public void testRemoveEntity() throws Exception {
        //GIVEN
        String s = "nameEntity";
        IObjectEntity objEnt = new ObjectEntity(s,TypeEntity.Clazz, Visibility.Public);
        pa.addEntity(objEnt);
        //WHEN
        pa.removeEntity(objEnt);
        //THEN
        assertThat(pa.getListEntities().size()).isEqualTo(0);
    }
}