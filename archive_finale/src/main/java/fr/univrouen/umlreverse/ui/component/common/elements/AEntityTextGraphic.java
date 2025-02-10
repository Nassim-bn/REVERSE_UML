/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.common.elements;

import static fr.univrouen.umlreverse.ui.component.common.elements.INoteGraphic.HEIGHT_MARGE_NOTE;
import static fr.univrouen.umlreverse.ui.component.common.elements.INoteGraphic.WIDTH_MARGE_NOTE;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public abstract class AEntityTextGraphic extends AEntityGraphic implements IEntityTextGraphic {
    // ATTRIBUTES
    private final Text text;
    IDiagramEditorController diagramController;
    
// CONSTRUCTORS
    protected AEntityTextGraphic(IDiagramEditorController diagramController) {
        super(diagramController);
        Contract.check(diagramController != null, "L'argument diagramController "
                + "ne doit pas être nul.");
        text = new Text(WIDTH_MARGE_NOTE / 2,
        		HEIGHT_MARGE_NOTE / 2 + TEXT_SIZE_DEFAULT, "");
        
        text.setFont(new Font(TEXT_SIZE_DEFAULT));  
        this.diagramController = diagramController;
    }
    
// REQUEST
    
    @Override
    public final Text getText() {
        return text;
    }
    
    @Override
    public Color getTextColor() {
        return ((IEntityTextGraphicController) getController()).getTextColor();
    }
    
    @Override
    public String toString() {
        return text.getText().trim();
    }

// COMMANDS 
    @Override
    public final void setText(String s) {
        Contract.check(s != null, "L'argument s ne doit pas être nul.");
        ((IEntityTextGraphicController) getController()).setText(s);
    }
    
    @Override
    public void setTextColor(Color c) {
        Contract.check(c != null, "L'argument c ne doit pas être nul.");
        ((IEntityTextGraphicController) getController()).setTextColor(c);
    }
}
