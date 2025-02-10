/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.usecase.elements;

import fr.univrouen.umlreverse.ui.component.common.elements.IEntityRelationGraphic;
import javafx.scene.shape.Ellipse;

public interface IUsecaseGraphic extends IEntityRelationGraphic {
    Ellipse getEllipse();
}
