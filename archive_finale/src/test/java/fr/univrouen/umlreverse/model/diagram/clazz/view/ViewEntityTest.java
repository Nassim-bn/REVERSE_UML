package fr.univrouen.umlreverse.model.diagram.clazz.view;

import static org.assertj.core.api.Assertions.assertThat;

import fr.univrouen.umlreverse.model.util.RefusedAction;
import org.junit.Before;
import org.junit.Test;

import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.project.Project;
import fr.univrouen.umlreverse.model.project.Attribute;
import fr.univrouen.umlreverse.model.project.IAttribute;
import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.model.project.IType;
import fr.univrouen.umlreverse.model.project.Method;
import fr.univrouen.umlreverse.model.project.ObjectEntity;
import fr.univrouen.umlreverse.model.project.Type;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.project.Variable;
import fr.univrouen.umlreverse.model.project.Visibility;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;


public class ViewEntityTest {
    private IObjectEntity data;
    private IClassDiagram diagram;
    private ViewEntity ve;


    @Before
    public void setUp() throws RefusedAction {
        //GIVEN
        IProject p = Project.newProject("test");
        data = new ObjectEntity("test", TypeEntity.Clazz, Visibility.Public);
        diagram = new ClassDiagram(p, "testDiagram");
        p.addEntity(data);
        ve = new ViewEntity(data, diagram);
        ve.setDiagram(diagram);
    }

    @Test
    public void constructor() {
        //GIVEN
        //WHEN
        //THEN
        assertThat(ve.getData()).isEqualTo(data);
        assertThat(ve.getDiagram()).isEqualTo(diagram);
        assertThat(ve.getStyle().toMap().isEmpty()).isTrue();
    }


    @Test
    public void testClearStyle() {
        //GIVEN
        IStyle s = ve.getStyle();
        s.add("key", "value");
        //WHEN
        ve.clearStyle();
        //THEN
        assertThat(ve.getStyle().toMap().size()).isEqualTo(0);
    }

    @Test
    public void testSetName() throws Exception {
        //GIVEN
        String name = "testSetName";
        //WHEN
        ve.setName(name);
        //THEN
        assertThat(ve.getName()).isEqualTo(name);
    }

    @Test
    public void testAddStyle() throws Exception {
        //GIVEN
        String nameStyle = "nameStyle";
        String valueStyle = "valueStyle";
        //WHEN
        ve.addStyle(nameStyle, valueStyle);
        //THEN
        assertThat(ve.getStyle().toMap().size()).isEqualTo(1);
        assertThat(ve.getStyle().toMap().get(nameStyle));
    }

    @Test
    public void testRemoveStyle() throws Exception {
        //GIVEN
        String nameStyle = "nameStyle";
        String valueStyle = "valueStyle";
        ve.addStyle(nameStyle, valueStyle);
        //WHEN
        ve.removeStyle(nameStyle);
        //THEN
        assertThat(ve.getStyle().toMap().size()).isEqualTo(0);

    }

    @Test
    public void testSetType() throws Exception {
        //GIVEN
        //WHEN
        ve.setType(TypeEntity.Enumeration);
        //THEN
        assertThat(ve.getType()).isEqualTo(TypeEntity.Enumeration);
    }

    @Test
    public void testAddEnumField() throws Exception {
        //GIVEN
        String field = "testEnumField";
        //WHEN
        ve.addEnumField(field);
        //THEN
        assertThat(ve.getEnumFields().size()).isEqualTo(1);
        assertThat(ve.getEnumFields().get(0)).isEqualTo(field);
    }

    @Test
    public void testRemoveEnumField() throws Exception {
        //GIVEN
        String field = "testRemoveEnumField";
        ve.addEnumField(field);
        //WHEN
        ve.removeEnumField(field);
        //THEN
        assertThat(ve.getData().getEnumFields().size()).isEqualTo(0);
        assertThat(ve.getEnumFields().size()).isEqualTo(0);
    }

    @Test
    public void testAddAbstractText() throws Exception {
        //GIVEN
        String testAbstractText = "abstractText";
        //WHEN
        ve.addAbstractText(testAbstractText);
        //THEN
        assertThat(ve.getAbstractTexts().size()).isEqualTo(1);
        assertThat(ve.getAbstractTexts().get(0)).isEqualTo(testAbstractText);
    }

    @Test
    public void testHideAbstractText() throws Exception {
        //GIVEN
        String s = "testHideAbstractText";
        ve.addAbstractText(s);
        //WHEN
        ve.hideAbstractText(s);
        //THEN
        assertThat(ve.getAbstractTexts().size()).isEqualTo(0);
    }

    @Test
    public void testRemoveAbstractText() throws Exception {
        //GIVEN
        String s = "testRemoveAbstractText";
        ve.addAbstractText(s);
        //WHEN
        ve.removeAbstractText(s);
        //THEN
        assertThat(ve.getAbstractTexts().size()).isEqualTo(0);
        assertThat(ve.getData().getAbstractTexts().size()).isEqualTo(0);
    }

    @Test
    public void testSetVisibility() throws Exception {
        //GIVEN
        //WHEN
        ve.setVisibility(Visibility.Private);
        //THEN
        assertThat(ve.getVisibility()).isEqualTo(Visibility.Private);
    }

    @Test
    public void testAddAttribute() throws Exception {
        //GIVEN
        IType t = new Type("type");
        Variable v = new Variable(t, "varName", "varInit");
        IAttribute att = new Attribute(Visibility.Packaged, "attProperty", v);
        //WHEN
        ve.addAttribute(att);
        //THEN
        assertThat(ve.getListAttribute().size()).isEqualTo(1);
        assertThat(ve.getListAttribute().contains(att)).isTrue();

    }

    @Test
    public void testHideAttribute() throws Exception {
        //GIVEN
        IType t = new Type("type");
        Variable v = new Variable(t, "varName", "varInit");
        IAttribute att = new Attribute(Visibility.Packaged, "attProperty", v);
        ve.addAttribute(att);
        //WHEN
        ve.hideAttribute(att);
        //THEN
        assertThat(ve.getListAttribute().size()).isEqualTo(0);

    }

    @Test
    public void testRemoveAttribute() throws Exception {
        //GIVEN
        IType t = new Type("type");
        Variable v = new Variable(t, "varName", "varInit");
        IAttribute att = new Attribute(Visibility.Packaged, "attProperty", v);
        ve.addAttribute(att);
        //WHEN
        ve.removeAttribute(att);
        //THEN
        assertThat(ve.getListAttribute().size()).isEqualTo(0);
        assertThat(ve.getData().getListAttribute().size()).isEqualTo(0);
    }

    @Test
    public void testAddMethod() throws Exception {
        //GIVEN
        Method m = new Method(new Type("void"), "f");
        //WHEN
        ve.addMethod(m);
        //THEN
        assertThat(ve.getListMethod().size()).isEqualTo(1);
        assertThat(ve.getListMethod().contains(m));
    }

    @Test
    public void testHideMethod() throws Exception {
        //GIVEN
        Method m = new Method(new Type("void"), "f2");
        ve.addMethod(m);
        //WHEN
        ve.hideMethod(m);
        //THEN
        assertThat(ve.getListMethod().size()).isEqualTo(0);
    }

    @Test
    public void testRemoveMethod() throws Exception {
        //GIVEN
        Method m = new Method(new Type("void"), "f3");
        ve.addMethod(m);
        //WHEN
        assertThat(ve.getListMethod().contains(m)).isTrue();
        assertThat(ve.getData().getListMethod().contains(m)).isTrue();
        ve.removeMethod(m);
        //THEN
        assertThat(ve.getData().getListMethod().contains(m)).isFalse();
        assertThat(ve.getListMethod().contains(m)).isFalse();
    }
}