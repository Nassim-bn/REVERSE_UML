package fr.univrouen.umlreverse.model.diagram.clazz.view;

import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.model.project.IRelation;
import fr.univrouen.umlreverse.model.project.ObjectEntity;
import fr.univrouen.umlreverse.model.project.Relation;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.project.Visibility;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.diagram.util.IStyleBox;
import fr.univrouen.umlreverse.model.diagram.util.StyleBox;
import fr.univrouen.umlreverse.model.project.Project;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;


public class ViewRelationTest {

    private IViewRelation vr;
    private IRelation r;
    private IClassDiagram cd;
    private IStyleBox sb;

    @Before
    public void setUp() throws Exception {
        String idRelation = "idRelation";
        IObjectEntity a = new ObjectEntity("a", TypeEntity.Clazz, Visibility.Public);
        IObjectEntity b = new ObjectEntity("b", TypeEntity.Clazz, Visibility.Public);
        r = new Relation(idRelation, a, b, TypeHeadArrow.EmptyHead,
                TypeHeadArrow.EmptyHead, TypeLineArrow.Plain);
        IProject p = new Project("test");
        r.setTailCardinality("aabefjze");
        r.setHeadComment("fzoeifnze");
        r.setHeadCardinality("fzejngekrjng");

        cd = new ClassDiagram(p, "nameProject");
        sb = new StyleBox();
        p.addRelation(r);
        vr = new ViewRelation(r, cd);
    }

    @Test
    public void testConstructor() {
        //GIVEN
        //WHEN
        //THEN
        assertThat(vr.getStyle().toMap()).isEmpty();
        assertThat(vr.getDiagram()).isEqualTo(cd);
        assertThat(vr.getData()).isEqualTo(r);
    }
    @Test
    public void testGetArrowHead() throws Exception {
        //GIVEN
        //WHEN
        TypeHeadArrow te = vr.getArrowHead();
        //THEN
        assertThat(te).isEqualTo(TypeHeadArrow.EmptyHead);
    }

    @Test
    public void testGetArrowTail() throws Exception {
        //GIVEN
        //WHEN
        TypeHeadArrow te = vr.getArrowTail();
        //THEN
        assertThat(te).isEqualTo(TypeHeadArrow.EmptyHead);
    }

    @Test
    public void testGetAssociationEntity() throws Exception {
        //GIVEN
        //WHEN
        IObjectEntity oE = vr.getAssociationEntity();
        //THEN
        assertThat(oE).isEqualTo(r.getAssociationEntity());
    }

    @Test
    public void testGetData() throws Exception {
        //GIVEN
        //WHEN
        IRelation iR = vr.getData();
        //THEN
        assertThat(iR).isEqualTo(r);
    }

    @Test
    public void testGetDiagram() throws Exception {
        //GIVEN
        //WHEN
        IClassDiagram cdr = vr.getDiagram();
        //THEN
        assertThat(cdr).isEqualTo(cd);
    }

    @Test
    public void testGetEntitySource() throws Exception {
        //GIVEN
        //WHEN
        IObjectEntity oE = vr.getEntitySource();
        //THEN
        assertThat(oE).isEqualTo(r.getEntitySource());
    }

    @Test
    public void testGetEntityTarget() throws Exception {
        //GIVEN
        //WHEN
        IObjectEntity oE = vr.getEntityTarget();
        //THEN
        assertThat(oE).isEqualTo(r.getEntityTarget());
    }

    @Test
    public void testGetHeadCardinality() throws Exception {
        //GIVEN
        //WHEN
        String s  = vr.getHeadCardinality();
        //THEN
        assertThat(s).isEqualTo(r.getHeadCardinality());
    }

    @Test
    public void testGetHeadComment() throws Exception {
        //GIVEN
        //WHEN
        String s  = vr.getHeadComment();
        //THEN
        assertThat(s).isEqualTo(r.getHeadComment());
    }

    @Test
    public void testGetId() throws Exception {
        //GIVEN
        //WHEN
        String s = vr.getId();
        //THEN
        assertThat(s).isEqualTo(r.getId());
    }

    @Test
    public void testGetLineArrow() throws Exception {
        //GIVEN
        //WHEN
        TypeLineArrow tla = vr.getLineArrow();
        //THEN
        assertThat(tla).isEqualTo(r.getLineArrow());
    }

    @Test
    public void testGetNameRelation() throws Exception {
        //GIVEN
        //WHEN
        String s = vr.getNameRelation();
        //THEN
        assertThat(s).isEqualTo(r.getNameRelation());
    }

    @Test
    public void testGetStyle() throws Exception {
        //GIVEN
        //WHEN
        IStyle ma = vr.getStyle();
        //THEN
        assertThat(ma.toMap()).isEmpty();
    }

    @Test
    public void testGetTailCardinality() throws Exception {
        //GIVEN
        //WHEN
        String s = vr.getTailCardinality();
        //THEN
        assertThat(s).isEqualTo(r.getTailCardinality());
    }

    @Test
    public void testGetTailComment() throws Exception {
        //GIVEN
        //WHEN
        String s = vr.getTailComment();
        //THEN
        assertThat(s).isEqualTo(r.getTailComment());
    }

    @Test
    public void testAddPropertyChangeListener() throws Exception {
        //GIVEN
        //WHEN
        vr.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

            }
        });
        //THEN
        assertThat(vr.getPropertyChangeListeners().length).isEqualTo(1);
        //THEN
    }

    @Test
    public void testAddStyle() throws Exception {
        //GIVEN
        String key = "key";
        String value = "value";
        //WHEN
        vr.addStyle(key, value);
        //THEN
        assertThat(vr.getStyle().toMap().containsKey(key)).isTrue();
        assertThat(vr.getStyle().toMap().containsValue(value)).isTrue();
        assertThat(vr.getStyle().toMap().size()).isEqualTo(1);
        // See at the end of test the method for fireEvent
    }

    @Test
    public void testRemovePropertyChangeListener() throws Exception {
        //GIVEN
        PropertyChangeListener pcl = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

            }
        };
        vr.addPropertyChangeListener(pcl);
        //WHEN
        vr.removePropertyChangeListener(pcl);
        //THEN
        assertThat(vr.getPropertyChangeListeners().length).isEqualTo(0);
    }

    @Test
    public void testRemoveStyle() throws Exception {
        //GIVEN
        String key = "key";
        String value = "value";
        vr.addStyle(key, value);
        //WHEN
        vr.removeStyle(key);
        //THEN
        assertThat(vr.getStyle().toMap()).isEmpty();
        // See at the end of test the method for fireEvent
    }

    @Test
    public void testSetArrowHead() throws Exception {
        //GIVEN
        TypeHeadArrow ta = TypeHeadArrow.Composition;
        //WHEN
        vr.setArrowHead(ta);
        //THEN
        assertThat(vr.getArrowHead()).isEqualTo(ta);
    }

    @Test
    public void testSetArrowTail() throws Exception {
        //GIVEN
        TypeHeadArrow ta = TypeHeadArrow.Composition;
        //WHEN
        vr.setArrowTail(ta);
        //THEN
        assertThat(vr.getArrowTail()).isEqualTo(ta);
    }

    @Test
    public void testSetAssociationEntity() throws Exception {
        //GIVEN
        IObjectEntity assoc = new ObjectEntity("test", TypeEntity.Clazz, Visibility.Public);
        //WHEN
        vr.setAssociationEntity(assoc);
        //THEN
        assertThat(vr.getAssociationEntity()).isEqualTo(assoc);
    }

    @Test
    public void testGetPropertyChangeListeners() throws Exception {
        //GIVEN
        PropertyChangeListener pcl1 = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

            }
        };
        PropertyChangeListener pcl2 = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

            }
        };
        vr.addPropertyChangeListener(pcl1);
        vr.addPropertyChangeListener(pcl2);
        //WHEN
        PropertyChangeListener[] pcls = vr.getPropertyChangeListeners();
        //THEN
        assertThat(pcls.length).isEqualTo(2);
    }

    @Test
    public void testSetEntitySource() throws Exception {
        //GIVEN
        IObjectEntity obj = new ObjectEntity("test", TypeEntity.Enumeration, Visibility.Private);
        //WHEN
        vr.setEntitySource(obj);
        //THEN
        assertThat(vr.getEntitySource()).isEqualTo(obj);
    }

    @Test
    public void testSetEntityTarget() throws Exception {
        //GIVEN
        IObjectEntity obj = new ObjectEntity("test", TypeEntity.Enumeration, Visibility.Private);
        //WHEN
        vr.setEntityTarget(obj);
        //THEN
        assertThat(vr.getEntityTarget()).isEqualTo(obj);
    }

    @Test
    public void testSetHeadCardinality() throws Exception {
        //GIVEN
        String card = "cardinalité";
        //WHEN
        vr.setHeadCardinality(card);
        //THEN
        assertThat(vr.getHeadCardinality()).isEqualTo(card);
    }

    @Test
    public void testSetHeadComment() throws Exception {
        //GIVEN
        String headComment = "headComment";
        //WHEN
        vr.setHeadComment(headComment);
        //THEN
        assertThat(vr.getHeadComment()).isEqualTo(headComment);
    }

    @Test
    public void testSetLineArrow() throws Exception {
        //GIVEN
        TypeLineArrow tla = TypeLineArrow.Dashed;
        //WHEN
        vr.setLineArrow(tla);
        //THEN
        assertThat(vr.getLineArrow()).isEqualTo(tla);
    }

    @Test
    public void testSetNameRelation() throws Exception {
        //GIVEN
        String nameRelation = "nameRelation";
        //WHEN
        vr.setNameRelation(nameRelation);
        //THEN
        assertThat(vr.getNameRelation()).isEqualTo(nameRelation);
    }

    @Test
    public void testSetTailCardinality() throws Exception {
        //GIVEN
        String tailCard = "tailCardinality";
        //WHEN
        vr.setTailCardinality(tailCard);
        //THEN
        assertThat(vr.getTailCardinality()).isEqualTo(tailCard);
    }

    @Test
    public void testSetTailComment() throws Exception {
        //GIVEN
        String tailComment = "tailComment";
        //WHEN
        vr.setTailComment(tailComment);
        //THEN
        assertThat(vr.getTailComment()).isEqualTo(tailComment);
    }
    @Test
    public void testFirePropertyChangeTestWhenStyleIsAdd() throws Exception {
        //GIVEN
        String key = "key";
        String value = "value";
        vr.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                //THEN
                assertThat(evt.getPropertyName()).isEqualTo("StyleChanged");
                assertThat(evt.getNewValue()).isEqualToComparingFieldByField(vr.getStyle());
                throw new IllegalArgumentException("testPropertyStyleIsAdd");
            }
        });
        //WHEN
        try {
            vr.addStyle(key, value);
            fail();
        } catch (IllegalArgumentException e) {

        }

    }

    @Test
    public void testFirePropertyChangeTestWhenStyleIsRemove() {
        //GIVEN
        String key = "key";
        String value = "value";
        vr.addStyle(key, value);
        vr.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                //THEN
                assertThat(evt.getPropertyName()).isEqualTo("StyleChanged");
                assertThat(evt.getNewValue()).isEqualToComparingFieldByField(vr.getStyle());
                throw new IllegalArgumentException("testPropertyChangeStyleIsRemove");
            }
        });
        try {
            vr.removeStyle(key);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }
}