/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.common.elements;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 *
 * @author antho
 */
public interface IEntityTextGraphic extends IEntityGraphic {
    
    int TEXT_SIZE_DEFAULT = 12;
// REQUESTS
    /**
     * Get Text of Note.
     * @return textProperty.get().getText()
     */
    Text getText();
    
    /**
     * Get text color of text.
     * @return textColor
     */
    Color getTextColor();
    
    
// COMMANDS
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
