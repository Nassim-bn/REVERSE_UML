package fr.univrouen.umlreverse.ui.component.common.relation.type;

import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class Synchrone extends RelationType {


	public Synchrone(double width) {
		super();
		
		draw(width);

    }
    
    @Override
    public RelationTypeEnum getType() {
        return RelationTypeEnum.ASYNCHRONE;
    }
    
    
    
    /**
     * draw the arrow with src and dst point2D 
     * */
    public void draw(double width) {
    	
    	// clear before redraw lines
    	getChildren().clear();
    	
    	Line l1 = new Line(0, 0, width, 0);
        
        Polygon poly = new Polygon(width-10,-8, width, 0, width-10, +8);
        
        getChildren().add(l1);
        getChildren().add(poly);
    	
    	    }

}
