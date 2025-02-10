package fr.univrouen.umlreverse.ui.component.common.relation.type;

import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class Asynchrone extends RelationType {

	
	public Asynchrone(double width) {
		super();
		
		draw(width);

    }
    
    @Override
    public RelationTypeEnum getType() {
        return RelationTypeEnum.ASYNCHRONE;
    }
    
    
    public void draw(double width) {
    	
    	// clear before redraw lines
    	getChildren().clear();
    	
    	Line l1 = new Line(0, 0, width, 0);
        Line l2 = new Line(width, 0, width-10, -8);
        Line l3 = new Line(width, 0, width-10, +8);
        
        
        getChildren().add(l1);
        getChildren().add(l2);
        getChildren().add(l3);
    }
    
}
