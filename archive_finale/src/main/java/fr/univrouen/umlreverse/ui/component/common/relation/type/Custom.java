package fr.univrouen.umlreverse.ui.component.common.relation.type;

import javafx.scene.text.Text;

public class Custom extends RelationType {
        public Custom() {
            super();
            Text t = new Text("Personnalisé");
            getChildren().add(t);
        }
        
        @Override
        public RelationTypeEnum getType() {
            return RelationTypeEnum.CUSTOM;
        }
}
