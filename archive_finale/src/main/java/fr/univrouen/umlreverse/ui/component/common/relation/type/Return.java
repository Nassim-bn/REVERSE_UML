package fr.univrouen.umlreverse.ui.component.common.relation.type;

import javafx.scene.shape.Line;

public class Return extends RelationType {

	public Return(double width) {
		super();
		
		draw(width);

    }
    
    @Override
    public RelationTypeEnum getType() {
        return RelationTypeEnum.RETURN;
    }
    
    
    
    /**
     * draw the arrow with src and dst point2D 
     * */
    public void draw(double width) {

    	// clear before redraw lines
    	getChildren().clear();
    	
    	Line l1 = new Line(0, 0, width, 0);
        Line l2 = new Line(width, 0, width-10, -8);
        Line l3 = new Line(width, 0, width-10, +8);
        

        l1.getStrokeDashArray().add(5d);
        
        getChildren().add(l1);
        getChildren().add(l2);
        getChildren().add(l3);

    }


}
