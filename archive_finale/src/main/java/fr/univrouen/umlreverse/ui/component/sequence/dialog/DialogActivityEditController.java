package fr.univrouen.umlreverse.ui.component.sequence.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.univrouen.umlreverse.model.diagram.sequence.Activity;
import fr.univrouen.umlreverse.model.diagram.sequence.IActivity;
import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegment;
import fr.univrouen.umlreverse.ui.component.usecase.dialog.DialogActorEdit.DialogActorEditMode;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class DialogActivityEditController {

	// ATTRIBUTES
	@FXML
    private ComboBox<String> entityName1;
    private ISequenceController controller;
    private IObject obj;
    private IActivity activity;
    private Map<String, IObject> objectOfFullname = new HashMap<>();
    private Map<String, IActivity> activityOfFullName = new HashMap<>();

	/**
     * Get IObject to edit.
     * @return IObject
     */
    public IObject getObject() {
        return obj;
    }

    public IActivity getActivity() {
    	return activity;
    }

    public void setObject(IObject o) {
    	obj = o;
    }

    /**
     * Initializes fields and events.
     *
     * @param diagramController the diagram used.
     */
    public final void setBehaviors(ISequenceController diagramController) {
    	Contract.check(diagramController != null, "diagramController must not be null");

    	Platform.runLater(() -> entityName1.requestFocus());
    	controller = diagramController;
        DialogActorEditMode mode = DialogActorEditMode.CREATE;

        List<String> list1 = new ArrayList<String>();
        for (IObject obj : controller.getDiagram().getObjects()) {
        	list1.add(obj.getFullName());
        	objectOfFullname.put(obj.getFullName(), obj);
        }

    	this.entityName1.setItems(FXCollections.observableList(list1));
    	if (list1.size() == 0) {
    		entityName1.setDisable(true);
    	} else {
    		entityName1.setValue(FXCollections.observableList(list1).get(0));    		
    	}

        if (obj != null) {
            if (obj.getName() == null) {
                throw new NullPointerException("Name null");
            }
            mode = DialogActorEditMode.EDIT;
        }
    }

    public final void addEntity() {
		IObject obj = objectOfFullname.get(entityName1.getValue());
		IActivity father = null;

		if (entityName1.getValue() != null) {
			father = activityOfFullName.get(entityName1.getValue());
		}

		activity = new Activity(obj, controller.getDiagram());
		if (father != null) {
			activity.setParent(father);
		}
	}

    public void setActivity(IActivity act) {
		this.activity = act;
	}
}
