package fr.univrouen.umlreverse.ui.component.common.elements;

import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphic;
import java.util.ArrayList;
import java.util.List;
import fr.univrouen.umlreverse.util.Contract;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;


public abstract class AEntityGraphicController implements IEntityGraphicController {
// ATTRIBUTES   
    private final ContextMenu ctxMenu;
    private final MenuItem editMI;
    private final MenuItem removeMI;
    private final MenuItem addRelationMI;
    private final List<IRelationGraphic> relations;
    private final ObjectProperty<Point2D> positionProperty;
    
// CONSTRUCTORS
    protected AEntityGraphicController() {
        ctxMenu = new ContextMenu();
        editMI = new MenuItem("Modifier");
        removeMI = new MenuItem("Supprimer");
        addRelationMI = new MenuItem("Relier à...");
        ctxMenu.getItems().add(editMI);
        ctxMenu.getItems().add(removeMI);
        ctxMenu.getItems().add(addRelationMI);
        relations = new ArrayList<>();
        positionProperty = new SimpleObjectProperty<>();
    }

    
// REQUEST
    
    @Override
    public ContextMenu getContextMenu() {
        return ctxMenu;
    }
    
    @Override
    public MenuItem getEditMI() {
        return editMI;
    }
    
    @Override
    public MenuItem getRemoveMI() {
        return removeMI;
    }
    
    @Override
    public MenuItem getAddRelationMI() {
        return addRelationMI;
    }

    @Override
    public Point2D getPosition() {
        return positionProperty.get();
    }

    @Override
    public ObjectProperty<Point2D> positionProperty() {
        return positionProperty;
    }
    
    @Override
    public List<IRelationGraphic> getRelations() {
        return relations;
    }

// COMMANDS
    
    @Override
    public void addRelation(IRelationGraphic r) {
        Contract.check(r != null, "Le paramètre r ne doit pas être nul.");
        relations.add(r);
    }
    
    @Override
    public void removeRelation(IRelationGraphic r) {
        Contract.check(r != null, "Le paramètre r ne doit pas être nul.");
        relations.remove(r);
    }

    
// CONTROLLER
   
}
