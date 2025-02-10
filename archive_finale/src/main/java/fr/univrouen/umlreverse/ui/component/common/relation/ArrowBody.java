package fr.univrouen.umlreverse.ui.component.common.relation;

import fr.univrouen.umlreverse.ui.component.clazz.dialog.DialogRemovePoint;
import fr.univrouen.umlreverse.util.Contract;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 * Implements IArrowBody.
 */
public class ArrowBody implements IArrowBody {
// ATTRIBUTES
    private static final Color COLOR_DEFAULT = Color.BLACK;
    private static final double RADIUS = 5d;
    //***********************************************************//
	public static final String PROP_CIRCLE = "removePoint";
	public static final String PROP_LINE = "addPoint";
    //***********************************************************//
    private Color colorLines;  
    private final ObjectProperty<Point2D> startPointProperty;
    private final ObjectProperty<Point2D> endPointProperty;
    private final Line firstLine;
    private final List<Circle> circlesList;
    private final Map<Circle, ObjectProperty<Point2D>> circles;
    private final Map<ObjectProperty<Point2D>, ChangeListener<Point2D>> changeListerners;
    private final List<Line> lines;
    private final Map<Line, ObjectProperty<Point2D>> linesToPointsStart;
    private final Map<Line, ObjectProperty<Point2D>> linesToPointsEnd;
    private EventHandler<MouseEvent> pointDnd_Event;
    private double dash = -1;
    //private ContextMenu removeMenu;
    //private MenuItem removeMI;
    private final PropertyChangeSupport pcs;

    private Group group;

        
    
// CONSTRUCTORS
    /**
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param tailProperty
     * @param headProperty 
     * @pre
     *      tailProperty != null && headProperty != null
     */
    public ArrowBody(double startX, double startY, double endX, double endY, ObjectProperty<Point2D> tailProperty, ObjectProperty<Point2D> headProperty) {
        Contract.check(tailProperty != null, "tailProperty must not be null.");
        Contract.check(headProperty != null, "headProperty must not be null.");
        colorLines = COLOR_DEFAULT;
        startPointProperty = new SimpleObjectProperty<>();
        endPointProperty = new SimpleObjectProperty<>();
        firstLine = new Line();
        lines = new ArrayList<>();
        changeListerners = new HashMap<>();
        linesToPointsStart = new HashMap<>();
        linesToPointsEnd = new HashMap<>();
        circles = new HashMap<>();
        circlesList = new ArrayList<>();
        
        linesToPointsStart.put(firstLine, startPointProperty);
        linesToPointsEnd.put(firstLine, endPointProperty);    
        addChangeLOnFirstsPoints();
        startPointProperty.set(new Point2D(startX, startY));
        endPointProperty.set(new Point2D(endX, endY));  
        tailProperty.addListener(new ChangeListener<Point2D>() {
            @Override
            public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
                startPointProperty.set(newValue);
            }
        });  
        headProperty.addListener(new ChangeListener<Point2D>() {
            @Override
            public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
                endPointProperty.set(newValue);
            }
        });

        pcs = new PropertyChangeSupport(this);
    }
        
// REQUESTS


    @Override
    public Line getFisrtLine() {
        return firstLine;
    }
    
    @Override
    public List<Line> getLines() {
        List<Line> linesRes = new ArrayList<>(lines);
        linesRes.add(firstLine);
        return linesRes;
    }
    
    @Override
    public List<Shape> getShapes() {
        List<Shape> l = new ArrayList<>();
        l.addAll(getLines());
        l.add(firstLine);
        l.addAll(circles.keySet());  
        return l;
    }
    
    @Override
    public List<Circle> getCircles() {
        return circlesList;
    }
    
// COMMANDS
 
 // Méthode pour calculer la distance entre deux points
	public double distance(Point2D p1, Point2D p2) {
		return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
	}
	
	//***************************************************************************************//
	//Ici on retrouve les méthodes pour ajouter et supprimer des PropertyChangeListener
	
	public void addPropertyChangeListener(String name, PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(name, listener);
	}
	
	public void removePropertyChangeListener(String name, PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(name, listener);
	}
	public void firePropertyChange(String name, Object oldValue, Object newValue) {
		pcs.firePropertyChange(name, oldValue, newValue);
	}
    //**************************************************************************************//

		
    
    //ajouter un point
    @Override
    public void addPoint(Point2D p) {
        Contract.check(p != null, "p must not be null.");
        ObjectProperty<Point2D> newPoint = new SimpleObjectProperty<>(); 
        Circle newCircle = new Circle(RADIUS, COLOR_DEFAULT);
        addMouseEventOnCircle(newCircle);
        newCircle.setVisible(false);
        circles.put(newCircle, newPoint);
        circlesList.add(newCircle);
         
        Line newLine = new Line();
        newLine.setStroke(colorLines);
        if (dash > 0) {
            newLine.getStrokeDashArray().add(dash);
        }
        Line lastLine;
        if (lines.isEmpty()) {
            lastLine = firstLine;
        } else {
            lastLine = lines.get(lines.size() - 1);
        }
        newLine.setEndX(endPointProperty.getValue().getX());
        newLine.setEndY(endPointProperty.getValue().getY());
        lines.add(newLine);
        
        ChangeListener<Point2D> newPointChangeL = new ChangeListener<Point2D>() {
            @Override
            public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
                newLine.setStartX(newValue.getX());
                newLine.setStartY(newValue.getY());
                lastLine.setEndX(newValue.getX());
                lastLine.setEndY(newValue.getY());
                newCircle.setCenterX(newValue.getX());
                newCircle.setCenterY(newValue.getY());
            }
        };
        newPoint.addListener(newPointChangeL);
        changeListerners.put(newPoint, newPointChangeL);       
        endPointProperty.removeListener(changeListerners.get(endPointProperty));       
        ChangeListener<Point2D> lastPointChangeL = new ChangeListener<Point2D>() {
            @Override
            public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
                newLine.setEndX(newValue.getX());
                newLine.setEndY(newValue.getY());
            } 
        };
        endPointProperty.addListener(lastPointChangeL);
        changeListerners.put(endPointProperty, lastPointChangeL);      
        linesToPointsStart.put(newLine, newPoint);
        linesToPointsEnd.put(newLine, endPointProperty);
        linesToPointsEnd.put(lastLine, newPoint);       
        newPoint.setValue(p);
        
    }
    
    //*********************************************************************************************//
    //Autre implémentation de l'ajout d'un point en triant la liste (cette fonction n'est pas utilisée dans notre solution, idée à développer)
    
    //Ajouter un point à une position donnéesÒ
    
    @Override
    public void addPointInPosition(Point2D p, Point2D src, Point2D dst) {
        Contract.check(p != null, "p must not be null.");
        ObjectProperty<Point2D> newPoint = new SimpleObjectProperty<>();
        Circle newCircle = new Circle(RADIUS, COLOR_DEFAULT);
        addMouseEventOnCircle(newCircle);
        newCircle.setVisible(false);
        circles.put(newCircle, newPoint);

      //Recuperer les coordonnées des deux premiers points de la liste
      double x1 = src.getX();
      double y1 = src.getY();
//      double x1 = circles.get(circlesList.get(0)).getValue().getX();
//      double y1 = circles.get(circlesList.get(0)).getValue().getY();
      double x2 = circles.get(circlesList.get(0)).getValue().getX();
      double y2 = circles.get(circlesList.get(0)).getValue().getY();
		
		//calculer la distance entre le point a ajouter et les deux points les plus proches
		double min_dis1 = distance(new Point2D(x1, y1), p);
		double min_dis2 = distance(new Point2D(x2, y2), p);
		int index=1;
		
		int count = 0;
      for (Circle c : circlesList) {
      	
			if (count < circlesList.size()-1) {

			 x1 = circles.get(c).getValue().getX();
			 y1 = circles.get(c).getValue().getY();
			 x2 = circles.get(circlesList.get(circlesList.indexOf(c) + 1)).getValue().getX();	
			 y2 = circles.get(circlesList.get(circlesList.indexOf(c) + 1)).getValue().getY();
			//calculer la distance entre le point a ajouter et les deux points les plus proches
			 double d1 = distance(new Point2D(x1, y1), p);
			 double d2 = distance(new Point2D(x2, y2), p);
			 if (d1 < min_dis1) {
				 min_dis1 = d1;
      	}
			 if (d2 < min_dis2) {
				 min_dis2 = d2;
				 index++;
			 }
			 
			}
			
			count++;
      }
      
      x1= x2;
      y1 = y2;
      x2 = dst.getX();
      y2 = dst.getY();
    //calculer la distance entre le point a ajouter et les deux points les plus proches
		 double d1 = distance(new Point2D(x1, y1), p);
		 double d2 = distance(new Point2D(x2, y2), p);
		 if (d1 < min_dis1) {
			 min_dis1 = d1;
	   	}
		 if (d2 < min_dis2) {
			 min_dis2 = d2;
			 index++;
		 }
      
        //Ajouter le point a la liste des points
		circlesList.add(index, newCircle);
			
		//Creation d'une nouvelle map triée par ordre croissant des distances
		Map<Circle, ObjectProperty<Point2D>> newCircles = new HashMap<>();
			for (Circle c : circlesList) {
				newCircles.put(c, circles.get(c));
			}
			circles.clear();
			circles.putAll(newCircles);
         //Créer une ligne entre chaque point et le point suivant dans la liste
         
        for (int i = 0; i < circlesList.size() - 1; i++) {
            Circle startCircle = circlesList.get(i);
            Circle endCircle = circlesList.get(i + 1);
            Line line = createLineBetweenPoints(startCircle, endCircle);
            linesToPointsStart.put(line, circles.get(startCircle));
            linesToPointsEnd.put(line, circles.get(endCircle));
        }

        // Mettre à jour les coordonnées du nouveau point
        newPoint.setValue(p);
    }

    // Méthode pour créer une ligne entre deux points
    private Line createLineBetweenPoints(Circle startCircle, Circle endCircle) {
        Line line = new Line();
        line.setStroke(colorLines);
        if (dash > 0) {
            line.getStrokeDashArray().add(dash);
        }
        line.setStartX(startCircle.getCenterX());
        line.setStartY(startCircle.getCenterY());
        line.setEndX(endCircle.getCenterX());
        line.setEndY(endCircle.getCenterY());
        lines.add(line);
        return line;
    }
    
    //*********************************************************************************************//

	//supprimer un point
    
    public void removePoint (Circle c) {
    	
        Contract.check(c != null, "c must not be null.");
        ObjectProperty<Point2D> point = circles.get(c);
        ChangeListener<Point2D> changeL = changeListerners.get(point);
        point.removeListener(changeL);
        
        //L'index de la ligne à supprimer
        int index = circlesList.indexOf(c);

        //Suppression des lignes reliées aux points (non fontionnelle)
        /*
        //Relier les deux points qui étaient connectés à ce point
        int index1= index - 1;
        int index2 = index + 1;
        Line newLine = new Line();
        newLine.setStroke(colorLines);
		if (dash > 0) {
			newLine.getStrokeDashArray().add(dash);
		}
		Line lastLine;
		if (lines.isEmpty()) {
			lastLine = firstLine;
		} else {
			lastLine = lines.get(lines.size() - 1);
		}
		
		//Enlever les lignes qui sont connectées à ce point
		lines.remove(index);
		lines.remove(index - 1);
		
		//Ajouter la nouvelle ligne
		newLine.setEndX(endPointProperty.getValue().getX());
		newLine.setEndY(endPointProperty.getValue().getY());
		lines.add(index1, newLine);*/

        //Enlever le point de la liste des points
        circlesList.remove(c);
        c.setVisible(false);
        c.removeEventHandler(MouseEvent.MOUSE_ENTERED, null);
        c.removeEventHandler(MouseEvent.MOUSE_EXITED, null);
        c.removeEventFilter(MouseEvent.MOUSE_DRAGGED, null);
               
    }
    
   	        
    @Override
    public void clear() {
        clearAll();
        addChangeLOnFirstsPoints();      
        double startX = startPointProperty.get().getX();
        double startY = startPointProperty.get().getY();
        double endX = endPointProperty.get().getX();
        double endY = endPointProperty.get().getY();       
        startPointProperty.set(new Point2D(startX, startY));
        endPointProperty.set(new Point2D(endX, endY));
    }
    
     @Override
    public void clearAll() {
        circles.values().stream().forEach((point) -> {
            point.removeListener(changeListerners.get(point));
        });
        startPointProperty.removeListener(changeListerners.get(startPointProperty));
        endPointProperty.removeListener(changeListerners.get(endPointProperty));
        lines.clear();
        changeListerners.clear();
        linesToPointsStart.clear();
        linesToPointsEnd.clear();
        circles.clear();
        circlesList.clear();
    }
    
    @Override
    public void moveEndPoint(Point2D p) {
        Contract.check(p != null, "p must not be null.");
        endPointProperty.setValue(p);
    }
    
    @Override
    public void movePoint(Circle c, double x, double y) {
        Contract.check(c != null, "c must not be null.");
        ObjectProperty<Point2D> point = circles.get(c);
        point.set(new Point2D(x, y));
    }
    
    @Override
    public void moveStartPoint(Point2D p) {
        Contract.check(p != null, "p must not be null.");
        startPointProperty.setValue(p);
    }
    
     @Override
    public void setColor(Color c) {
        Contract.check(c != null, "c must not be null.");
        colorLines = c;
        firstLine.setStroke(c);
        getLines().stream().forEach((l) -> {
            l.setStroke(c);
        });
    }
    
    @Override
    public void setDashed(double dash) {
        this.dash = dash;
        getLines().stream().forEach((l) -> {
            if (dash > 0) {
                l.getStrokeDashArray().add(dash);
            } else {
                l.getStrokeDashArray().clear();
            }
        });
    }
       
    @Override
    public void setDNDPointEvent(EventHandler<MouseEvent> pointDnd_Event) {
        Contract.check(pointDnd_Event != null, "pointDnd_Event "
                + "must not be null.");
       this.pointDnd_Event = pointDnd_Event;
    }
    
// PRIVATE
    
    private void addChangeLOnFirstsPoints() {
         ChangeListener<Point2D> startPointChangeL = 
                new ChangeListener<Point2D>() {
                @Override
                public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
                    firstLine.setStartX(newValue.getX());
                    firstLine.setStartY(newValue.getY());
                }
        };
        ChangeListener<Point2D> endPointChangeL = new ChangeListener<Point2D>() {
            @Override
            public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
                firstLine.setEndX(newValue.getX());
                firstLine.setEndY(newValue.getY());
            }
        };
        startPointProperty.addListener(startPointChangeL);
        endPointProperty.addListener(endPointChangeL);
        changeListerners.put(startPointProperty, startPointChangeL);
        changeListerners.put(endPointProperty, endPointChangeL);
    }
    
    private void addMouseEventOnCircle(Circle c) {
    
        c.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                c.setVisible(true);
            }
        });  

		  c.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			  if (event.getButton() == MouseButton.PRIMARY  && event.getClickCount() == 2) {
				  //afficher le menu de suppression		
				  DialogRemovePoint dialog = new DialogRemovePoint();
				  ButtonType result = dialog.showAndWait();
				  if (result ==
						  ButtonType.YES) {
					  removePoint(c);
					  //Envoi de l'événement
					  firePropertyChange(PROP_CIRCLE, null, c);
				  }
			  } 
	            
	        });

        c.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                c.setVisible(false);
            }
        }); 
        
        if (pointDnd_Event != null) {
            c.addEventFilter(MouseEvent.MOUSE_DRAGGED, pointDnd_Event);
        }
    }


}
