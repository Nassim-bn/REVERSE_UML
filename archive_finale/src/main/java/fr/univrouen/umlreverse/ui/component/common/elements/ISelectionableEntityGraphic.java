/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.common.elements;


public interface ISelectionableEntityGraphic {
    /**
     * Indicate if noteGraphic is selected.
     * @return isSelected
     */
    boolean isSelected();
    
    /**
     * Select EntityGraphic.
     * @param b : boolean
     */
    void setSelected(boolean b);
    
}
