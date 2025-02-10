package fr.univrouen.umlreverse.ui.view.sequence;

import fr.univrouen.umlreverse.util.Contract;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class ToolBarSequenceController {

    // ATTRIBUTES
    /**
     * The class diagram that will correspond to this tool bar.
     */
    private SequenceDiagramEditor diagram;

    /**
     * Set the current diagram to the parameter.
     * @param sq the sequence diagram editor that will be set as current diagram
     */
    public void setDiagram(SequenceDiagramEditor sq) {
         Contract.check(sq != null, "sq must not be null.");
         diagram = sq;
    }


     /**
     * Create a new Object.
     */
    @FXML
    public void createObject(){

        Contract.check(diagram != null, "diagram must be instantiated.");
        diagram.createObject(0, 0);

    }

    /**
     * Create a new Segment.
    */
    @FXML
    public void createSegment(){

        Contract.check(diagram != null, "diagram must be instantiated.");
        if (((SequenceController) diagram.getController()).getObjects().size() == 0) {
        	new Alert(Alert.AlertType.ERROR, "Il faut au moins un objet pour créer un segment")
    		.showAndWait();
        } else {
        	diagram.createSegment(0, 0);
        }

    }

    /**
     * Create a new Activite.
    */
    @FXML
    public void createActivite(){

        Contract.check(diagram != null, "diagram must be instantiated.");
        if (((SequenceController) diagram.getController()).getObjects().size() == 0) {
        	new Alert(Alert.AlertType.ERROR, "Il faut au moins un objet pour créer une activité")
    		.showAndWait();
        } else {
        	diagram.createActivite(0, 0);
        }

    }


     /**
     * Create a new note.
     */
    @FXML
    public void createNote(){
         Contract.check(diagram != null, "diagram must be instantiated.");
         diagram.createNote(0, 0);
     }

}
