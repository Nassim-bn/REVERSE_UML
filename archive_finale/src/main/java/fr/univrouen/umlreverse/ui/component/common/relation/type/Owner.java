package fr.univrouen.umlreverse.ui.component.common.relation.type;

import javafx.scene.shape.Line;

public class Owner extends RelationType {

	public Owner(double width, double height) {
		super();
		
		draw(width, height);

    }
	
	public Owner(double width) {
		this(width, 10);
	}
    
    @Override
    public RelationTypeEnum getType() {
        return RelationTypeEnum.OWNER;
    }
    
    
    public void draw(double width) {
    	draw(width, 10);
    }
    
    /**
     * draw the arrow with src and dst point2D 
     * */
    public void draw(double width, double height) {

    	// clear before redraw lines
    	getChildren().clear();
    	
    	//First Line
    	Line l1 = new Line(0, 0, width, 0);
    	
    	//Vertical line 
    	Line l2 = new Line(width, 0, width, height);
    	
    	//Third horizontal line
    	Line l3 = new Line(0, height, width, height);
    	
    	// head of the arrow
    	Line h1 = new Line(0, height, 10, height - 8);
    	Line h2 = new Line(0, height, 10, height + 8);
        

        getChildren().add(l1);
        getChildren().add(l2);
        getChildren().add(l3);
        getChildren().add(h1);
        getChildren().add(h2);

    }


}
