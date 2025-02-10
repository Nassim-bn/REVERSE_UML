/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.common.elements;

import javafx.scene.paint.Color;

public interface IEntityTextGraphicController extends IEntityGraphicController {
    
    /**
     * Get text color of text.
     * @return textColor
     */
    Color getTextColor();

// COMMANDS
    
    void loadRelations();
    
    /**
     * Set text on note.
     * @param s 
     */
    void setText(String s);
    
    
    /**
     * Set text color of note.
     * @param c 
     */
    void setTextColor(Color c);

}
