package fr.univrouen.umlreverse.ui.view.common;

import fr.univrouen.umlreverse.model.diagram.common.IEntityRelation;
import fr.univrouen.umlreverse.model.diagram.common.INote;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.ui.component.common.elements.ISelectionableEntityGraphic;
import javafx.scene.control.MenuItem;


public interface IDiagramEditorController {
//  ATTRIBUTES
String POSITION_STYLE_ID = "position";
    String POSITIONS_RELATION_STYLE_ID = "positions";
    String POSITION_DEFAULT = "0|0|0|0";
    String TEXT_COLOR_STYLE_ID = "text-color";
    String TEXT_COLOR_DEFAULT = "#000000";
    String BACKGROUND_COLOR_STYLE_ID = "background-color";
    String COLOR_STYLE_ID = "color";
    String BACKGROUND_COLOR_DEFAULT_NOTE = "#FBFB77";
    String BACKGROUND_COLOR_DEFAULT_ENTITY = "#FFFECE";
    String BACKGROUND_COLOR_DEFAULT_ACTOR = "#000000";
    String COLOR_DEFAULT_RELATION = "#A80036";
    String COLOR_STROK_SHAPE = "#A80036";
	String SIZE_STYLE_ID = "size";

    /**
     * Get DiagramEditor.
     * @return DiagramEditor
     */
    ADiagramEditor getEditor();

    /**
     * Get MenuItem to create note.
     * @return MenuItem
     */
    MenuItem getCreateNoteMI();
    
    /**
     * Get MenuItem to zoom in.
     * @return MenuItem
     */
    MenuItem getZoomInMI();
    
    /**
     * Get MenuItem to zoom out.
     * @return MenuItem
     */
    MenuItem getZoomOutMI();

    /**
     * Get the entity that was selected.
     * @return  ISelectionableEntityGraphic
     */
    ISelectionableEntityGraphic getSelectedEntity();

    /**
     * Get the x position of the cursor.
     * @return double
     */
    double getxCursor();

    /**
     * Get the y position of the cursor.
     * @return double
     */
    double getyCursor();

// COMMANDS    

    /**
     * Create a note to the position given in parameter.
     * @param x the x position of the note
     * @param y the y position of the note
     */
    void createNote(double x, double y);
    
    /**
     * Set the attribute selectedEntity to the param selectedEntity.
     * @param selectedEntity the entity that will be set as selected
     *
     */
    void setSelectedEntity(ISelectionableEntityGraphic selectedEntity);

    /**
     * Set the attribute selectedEntity to null.
     */
    void deselectEntity();

    /**
     * Set the attribute xCursor to the param xCursor.
     * @param xCursor the x position of the cursor
     */
    void setxCursor(double xCursor);
    
    /**
     * Set the attribute yCursor to the param yCursor.
     * @param yCursor the y position of the cursor
     */
    void setyCursor(double yCursor);

	void removeNote(INote modelNote);

	void createRelation(IEntityRelation entity);

	void removeRelation(IRelation relationModel);


}
