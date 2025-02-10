package fr.univrouen.umlreverse.ui.component.common.relation.type;

import javafx.scene.shape.Line;

public class Note extends RelationType {
	public Note() {
		super();
        Line l1 = new Line(0, 0, 40, 0);
        l1.getStrokeDashArray().add(5d);
        getChildren().add(l1);
	}
	
	@Override
	public RelationTypeEnum getType() {
		return RelationTypeEnum.NOTE;
	}
}
