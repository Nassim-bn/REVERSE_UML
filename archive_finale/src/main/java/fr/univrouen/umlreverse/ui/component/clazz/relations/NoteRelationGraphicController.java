package fr.univrouen.umlreverse.ui.component.clazz.relations;

import java.util.ArrayList;

import java.util.List;

import fr.univrouen.umlreverse.ui.component.clazz.dialog.DialogAddPoint;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphicController;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import fr.univrouen.umlreverse.model.diagram.clazz.view.INoteClass;


/**
 * A controller associated with a note-relation component.
 */
public class NoteRelationGraphicController implements IRelationGraphicController {
	// ATTRIBUTES
	/** The note-relation component associated with this controller. */
    private final NoteRelationGraphic relation;
    /** The note at one end of the relation. */
    private final INoteClass note;
    /** The entity at the other end of the relation. */
    private final IViewEntity entity;
    /** The component representing one end of the relation. */
    private final IEntityGraphic src;
    /** The component representing the other end of the relation. */
    private final IEntityGraphic dst;
    /** Updates the relation when the entity on either side is moved. */
    private ChangeListener<Object> moveEntityCL;
    
    // CONSTRUCTORS
    /**
     * 
     * @param relation
     * @param note
     * @param diagramController
     * @param src
     * @param dst
     * @pre
     *      relation != null
     *      && note != null
     *      && entity != null
     *      && diagramController != null
     *      && src != null
     *      && dst != null
     */
    public NoteRelationGraphicController(NoteRelationGraphic relation, 
            INoteClass note, IViewEntity entity,
            IDiagramEditorController diagramController,
            IEntityGraphic src, IEntityGraphic dst) {
        Contract.check(diagramController != null, 
                "diagramController must not be null.");
        Contract.check(relation != null, "relation must not be null.");
        Contract.check(note != null, "note must not be null.");
        Contract.check(entity != null, "entity must not be null.");
        Contract.check(src != null, "src must not be null.");
        Contract.check(dst != null, "dst must not be null.");
        this.relation = relation;
        this.src = src;
        this.dst = dst;
        this.note = note;
        this.entity = entity;
        relation.setColorShapes(Color.web(
        		IDiagramEditorController.COLOR_DEFAULT_RELATION));
        createController();
    }

    // QUERIES
    @Override
    public String getCardinalityEnd() {
        return "";
    }
    
    @Override
    public String getCardinalityStart() {
        return "";
    }
    
    @Override
    public Color getColorRelation() {
        return Color.web(IDiagramEditorController.COLOR_DEFAULT_RELATION);
    }

    @Override
    public Color getColorText() {
        return Color.web(IDiagramEditorController.TEXT_COLOR_DEFAULT);
    }
    
    @Override
    public IEntityGraphic getGEntitySrc() {
        return src;
    }

    @Override
    public IEntityGraphic getGEntityDst() {
        return dst;
    }
    
    @Override
    public String getName() {
        return "";
    }
    
    @Override
    public List<Point2D> getPoints() {
        return new ArrayList<>();
    }
    
    @Override
    public RelationTypeEnum getType() {
        return RelationTypeEnum.NORMAL;
    }

    @Override
    public TypeHeadArrow getTypeHead() {
        return TypeHeadArrow.None;
    } 
    
    @Override
    public TypeLineArrow getTypeLine() {
        return TypeLineArrow.Dashed;
    }
    
    @Override
    public TypeHeadArrow getTypeTail() {
        return TypeHeadArrow.None;
    }
    
    // COMMANDS
    @Override
    public void clear() {
        src.positionProperty().removeListener(moveEntityCL); 
        dst.positionProperty().removeListener(moveEntityCL);  
    }
    
    @Override
    public void setCardinalityEnd(String cardinalityEnd) {
        
    }

    @Override
    public void setCardinalityStart(String cardinalityStart) {
        
    }

    @Override
    public void setColorRelation(Color colorRelation) {
        
    }

    @Override
    public void setColorText(Color colorText) {
        
    }
    
    @Override
    public void setName(String name) {
       
    }
    
    @Override
    public void setType(RelationTypeEnum type) {
        
    }
    
//    //Affiche une boite de dialogue pour ajouter un point a la relation
//	private void afficherDialogAjoutPoint() {
//		DialogAddPoint dialogAddPoint = new DialogAddPoint();
//		dialogAddPoint.showAndWait();
//	}
	
    // TOOLS
    /**
     * Create and populate the context menu associated with the component, and
     * create the necessary event listeners.
     */
    private void createController() {
        ContextMenu ctxMenu = new ContextMenu();
        MenuItem removeMI = new MenuItem("Supprimer");
        ctxMenu.getItems().add(removeMI);
        
        
        // BUTTONS
        removeMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                note.removeEntity(entity);
            }
        });   
        
    
        // EVENT ON RELATION
        
        //quand on clique sur la relation, on la selectionne et on affiche le menu contextuel 
        relation.setContextMenuEventOnArrow(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
                new EventHandler<ContextMenuEvent>() {
                        @Override
                        public void handle(ContextMenuEvent event) {
                            relation.setSelected(true);
                            ctxMenu.show(relation.getFirstLine(), 
                                    event.getScreenX(), event.getScreenY());
                        }
                }
        );
        
        //cette partie permet de mettre en valeur la relation quand on passe la souris dessus
        relation.setMouseEventOnArrow(MouseEvent.MOUSE_ENTERED, 
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    relation.getShapes().stream().forEach((shape) -> {
                        shape.setStyle("-fx-stroke-width : 4");
                    });   
                }
            }
        );
         
        //cette partie permet de remettre la relation dans son etat initial quand on sort la souris
        relation.setMouseEventOnArrow(MouseEvent.MOUSE_EXITED, 
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    relation.getShapes().stream().forEach((shape) -> {
                        shape.setStyle("-fx-stroke-width : 1");
                    });   
                }
            }
        );
        

  
        // LISTENERS ON GRAPHIC ENTITIES
        moveEntityCL = (observable, oldValue, newValue) ->
				relation.calculPointRelation();
        src.positionProperty().addListener(moveEntityCL);
        dst.positionProperty().addListener(moveEntityCL);
    }
}
