package fr.univrouen.umlreverse.model.diagram.clazz;

import fr.univrouen.umlreverse.model.project.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewRelation;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewRelation;

/**
 * Created by BELKHOUS on 11/03/2016.
 */
public class RelationTest {

    private Relation r;
    private ObjectEntity OE1,OE2;


    @Before
    public void setUp() throws Exception {
        OE1 = new ObjectEntity("OE1", TypeEntity.Enumeration, Visibility.Public);
        OE2 = new ObjectEntity("OE2",TypeEntity.Abstract,Visibility.Private);
        r = new Relation("relation1",OE1,OE2);

    }

    @Test
    public void testGetSetEntitySource() throws Exception {
        r.setEntitySource(OE2);
        assertEquals(r.getEntitySource(),OE2);

    }

    @Test
    public void testGetSetEntityTarget() throws Exception {
        r.setEntityTarget(OE1);
        assertEquals(r.getEntityTarget(),OE1);
    }

    @Test
    public void testGetSetNameRelation() throws Exception {
        r.setNameRelation("newName");
        assertEquals(r.getNameRelation(),"newName");
    }

    @Test
    public void testGetSetHeadCardinality() throws Exception {
        r.setHeadCardinality("headCard");
        assertEquals(r.getHeadCardinality(),"headCard");
    }

    @Test
    public void testGetSetTailCardinality() throws Exception {
        r.setTailCardinality("trailCard");
        assertEquals(r.getTailCardinality(),"trailCard");
    }

    @Test
    public void testGetSetHeadComment() throws Exception {
        r.setHeadComment("commentHead");
        assertEquals(r.getHeadComment(),"commentHead");
    }

    @Test
    public void testGetSetTailComment() throws Exception {
        r.setTailComment("commentTail");
        assertEquals(r.getTailComment(),"commentTail");
    }

    @Test
    public void testGetSetArrowHead() throws Exception {
        r.setArrowHead(TypeHeadArrow.EmptyHead);
        assertEquals(r.getArrowHead(),TypeHeadArrow.EmptyHead);
    }

    @Test
    public void testGetSetArrowTail() throws Exception {
        r.setArrowTail(TypeHeadArrow.EmptyHead);
        assertEquals(r.getArrowTail(),TypeHeadArrow.EmptyHead);
    }

    @Test
    public void testGetSetLineArrow() throws Exception {
        r.setLineArrow(TypeLineArrow.Plain);
        assertEquals(r.getLineArrow(),TypeLineArrow.Plain);
    }

    @Test
    public void testGetSetAssociationEntity() throws Exception {
        r.setAssociationEntity(OE1);
        assertEquals(r.getAssociationEntity(),OE1);
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(r.getId(),"relation1");
    }

    @Test
    public void testGetAddRemoveViews() throws Exception {
        Set<IViewRelation> testListViewer = new HashSet<>();
        IProject project = new Project("test");
        project.addRelation(r);
        IViewRelation Viewer1 = new ViewRelation(r,new ClassDiagram(project,"diagram1"));
        r.addView(Viewer1);

        testListViewer.add(Viewer1);
        assertEquals(r.getViews(),testListViewer);

        r.removeView(Viewer1);
        assertFalse(r.getViews().contains(Viewer1));

        r.addView(Viewer1);
        assertTrue(r.getViews().contains(Viewer1));
    }
}