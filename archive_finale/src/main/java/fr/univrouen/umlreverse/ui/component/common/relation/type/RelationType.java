package fr.univrouen.umlreverse.ui.component.common.relation.type;

import javafx.scene.Group;


public abstract class RelationType extends Group {
        public abstract RelationTypeEnum getType();
        
        @Override
        public String toString() {
            return getType().toString();
        }
        
        public void draw(double width) {
        	
        }
    }
