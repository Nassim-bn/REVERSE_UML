package fr.univrouen.umlreverse.ui.component.common.elements;

import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphic;
import java.util.List;

import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * A component representing an graphic entity.
 */
public abstract class AEntityGraphic extends BorderPane implements IEntityGraphic {
    
// ATTRIBUTES
    private boolean isSelected;
    private final IDiagramEditorController diagramEditorController;
    
// CONTRUCTORS
    protected AEntityGraphic(IDiagramEditorController diagramEditorController) {
        super();
        Contract.check(diagramEditorController != null,
                "L'argument diagramEditorController "
                + "ne doit pas être nul.");
        this.diagramEditorController = diagramEditorController;
        isSelected = false;
    }
    
// REQUESTS   
    
    @Override
    public Point2D getCenterPoint() {
        double x = getLayoutX() + getTranslateX();
        double y = getLayoutY() + getTranslateY();
        return new Point2D(x + getWidth() / 2,
        		y + getHeight() / 2);
    }
    
    @Override
    public double getMainHeight() {
    	return getHeight();
    }
    
    
    @Override
    public double getMainWidth() {
    	return getWidth();
    }
    
    
    @Override
    public boolean isSelected() {
        return isSelected;
    }


    @Override
    public ObjectProperty<Point2D> positionProperty() {
        return getController().positionProperty();      
    }
    
    @Override
    public Color getBackgroundColor() {
        return getController().getBackgroundColor();
    }
    
    @Override
    public List<IRelationGraphic> getRelations() {
        return getController().getRelations();
    }

// COMMANDS
    @Override
    public void addRelation(IRelationGraphic r) {
        getController().addRelation(r);
    }
    
    @Override
    public void removeRelation(IRelationGraphic r) {
        getController().removeRelation(r);
    }

    
    @Override
    public void setSelected(boolean b) {
        if (b) {
            diagramEditorController.setSelectedEntity(this);
        }
        isSelected = b;
    }   
    
    @Override
    public void setPosition(Point2D value) {
        Contract.check(value != null, "L'argument value ne doit pas être nul.");
        getController().setPosition(value);
    }
    
    @Override
    public void setTranslatePosition(Point2D value) {
        Contract.check(value != null, "L'argument value ne doit pas être nul.");
        getController().setTranslatePosition(value);
    }
    
    @Override
    public void setBackgroundColor(Color c) {
        Contract.check(c != null, "L'argument c ne doit pas être nul.");
        getController().setBackgroundColor(c);
    }
    
// PRIVATE
    
}
