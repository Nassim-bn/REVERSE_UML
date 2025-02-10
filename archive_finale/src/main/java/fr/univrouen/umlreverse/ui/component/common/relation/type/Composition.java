/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.ui.component.common.relation.type;

import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;


public class Composition extends RelationType {
        public Composition() {
            super();
            Polygon p = new Polygon(0, 0, 5, -4, 10, 0, 5, 4);
            Line l1 = new Line(10, 0, 40, 0);
            Line l2 = new Line(40, 0, 30, -8);
            Line l3 = new Line(40, 0, 30, 8);

            getChildren().add(p);
            getChildren().add(l1);
            getChildren().add(l2);
            getChildren().add(l3);
        }
        
        @Override
        public RelationTypeEnum getType() {
            return RelationTypeEnum.COMPOSITION;
        }
    } 
