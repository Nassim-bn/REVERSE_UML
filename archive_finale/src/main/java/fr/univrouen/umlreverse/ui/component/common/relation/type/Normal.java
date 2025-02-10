/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.common.relation.type;

import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationType;
import javafx.scene.shape.Line;


public class Normal extends RelationType {
        public Normal() {
            super();
            Line l1 = new Line(0, 0, 40, 0);
            getChildren().add(l1);
        }
        
        @Override
        public RelationTypeEnum getType() {
            return RelationTypeEnum.NORMAL;
        }
}
