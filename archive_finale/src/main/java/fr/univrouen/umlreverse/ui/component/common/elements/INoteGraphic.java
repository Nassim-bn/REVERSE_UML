/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.common.elements;

import fr.univrouen.umlreverse.ui.component.common.util.NoteShape;

public interface INoteGraphic extends IEntityGraphic {
    
// ATTRIBUTES
int WIDTH_MARGE_NOTE = 30;
    int HEIGHT_MARGE_NOTE = 30;
// REQUESTS
    /**
     * Get Rectangle of entity graphic.
     * @return 
     */
    NoteShape getRectangle();
}
