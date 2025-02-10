package fr.univrouen.umlreverse.ui.component.sequence.dialog;

import static fr.univrouen.umlreverse.util.Util.toRGBCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.univrouen.umlreverse.model.diagram.sequence.Segment.SegmentType;
import fr.univrouen.umlreverse.model.diagram.sequence.SegmentRef;
import fr.univrouen.umlreverse.model.diagram.sequence.SegmentWithElse;
import fr.univrouen.umlreverse.model.diagram.common.IDiagram.DiagramType;
import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegment;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegmentCommon;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegmentRef;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.Segment;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.sequence.ISequenceController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class DialogSegmentEditController {
	// TODO changer les noms dans le fxml
	@FXML
	private ComboBox<SegmentType> segmentType;
	@FXML
	private TextField condition;
	
	@FXML 
	private ComboBox<String> sequence;
	
	@FXML
	private Label sequenceLabel;

	@FXML
	private ComboBox<String> fatherName;
	@FXML
	private ComboBox<String> entityNameStart;
	@FXML
	private ComboBox<String> entityNameEnd;
	@FXML
    private ColorPicker color;

    private ISequenceController controller;

	private ISegmentCommon segment;

	private Map<String, IObject> objectOfFullname = new HashMap<>();
	private Map<String, ISegmentCommon> segmentOfFullname = new HashMap<>();
	private Map<String, ISequenceDiagram> diagramOfFullname = new HashMap<>();
	public ISegmentCommon getSegment() {
		return segment;
	}

	/**
     * Initializes fields and events.
     * @param diagramController the diagram used.
     * @param typeEntity the type wanted.
     */
	public void setBehaviors(ISequenceController diagramController) {
		Contract.check(diagramController != null, "diagramController must don't be null");

		this.controller = diagramController;
		List<String> listDiagram = new ArrayList<>();
		controller.getDiagram().getProject().getDiagrams().stream().filter(diag ->
		diag.getType().equals(DiagramType.SEQUENCE) && !diag.equals(controller.getDiagram()))
		.forEach(diag -> {
			listDiagram.add(diag.getName());
			diagramOfFullname.put(diag.getName(), (ISequenceDiagram) diag);
		});
		sequence.setItems(FXCollections.observableList(listDiagram));
		if (listDiagram.size() != 0) {
			sequence.setValue(listDiagram.get(0));
		}
		sequence.setVisible(false);
		sequenceLabel.setVisible(false);
		List<SegmentType> list = new ArrayList<>();
		for (SegmentType name : SegmentType.values()) {
			list.add(name);
		}
		this.segmentType.setItems(FXCollections.observableList(list));
		segmentType.setValue(list.get(0));
		List<String> listObjects = new ArrayList<>();
		controller.getDiagram().getObjects().forEach(obj -> {
			listObjects.add(obj.getFullName());
			objectOfFullname.put(obj.getFullName(), obj);
		});
		List<String> listSegments = new ArrayList<>();
		controller.getDiagram().getSegmentsChilds().forEach(seg -> {
			String descr = seg.getSegmentType().toString() + "    [" + seg.getCondition() + "]";
			listSegments.add(descr);
			segmentOfFullname.put(descr, seg);
		});
		fatherName.setItems(FXCollections.observableList(listSegments));
		entityNameEnd.setItems(FXCollections.observableList(listObjects));
		entityNameStart.setItems(FXCollections.observableList(listObjects));
		entityNameStart.setValue(listObjects.get(0));
		entityNameEnd.setValue(listObjects.get(listObjects.size() - 1));
		if (segment != null) {
			if (segment.getSegmentType() == null) {
                throw new NullPointerException("Type null");
            }
			if (segment.getSegmentType().equals(SegmentType.REF)) {
                sequence.setValue(((ISegmentRef) segment).getDiagramContained().getName());
                sequence.setVisible(true);
        		sequenceLabel.setVisible(true);
            }
			this.condition.setText(segment.getCondition());
			this.segmentType.setValue(segment.getSegmentType());
			entityNameStart.setValue(segment.getStart().getFullName());
			entityNameEnd.setValue(segment.getEnd().getFullName());
			segmentType.setDisable(true);
			fatherName.setDisable(true);
		} else {
			color.setValue(Color.web(IDiagramEditorController.TEXT_COLOR_DEFAULT));
		}
	}

	@FXML 
	public void refreshWindow() {
		if (segmentType.getValue().equals(SegmentType.REF)) {
			sequence.setVisible(true);
			sequenceLabel.setVisible(true);
		} else {
			sequence.setVisible(false);
			sequenceLabel.setVisible(false);
		}
	}
	
	public final void addEntity() {
		SegmentType type = segmentType.getValue();
		String condition = this.condition.getText();
		IObject start = objectOfFullname.get(entityNameStart.getValue());

		IObject end = objectOfFullname.get(entityNameEnd.getValue());
		ISegment father = null;
		if (fatherName.getValue() != null) {
			father = (ISegment) segmentOfFullname.get(fatherName.getValue());
		}
		if (segment == null) {
			if (type.equals(SegmentType.REF)) {
				segment = new SegmentRef(type, controller.getDiagram(), condition);
				((ISegmentRef) segment).setDiagramContained(diagramOfFullname.get(sequence.getValue()));
			} else if (type.equals(SegmentType.ALT)) {
				segment = new SegmentWithElse(type, controller.getDiagram(), condition);
			} else {
				segment = new Segment(type, controller.getDiagram(), condition);
			}
			if (father != null) {
				segment.setParent(father);
			}
		} else {
			if (type.equals(SegmentType.REF)) {
				((ISegmentRef) segment).setDiagramContained(diagramOfFullname.get(sequence.getValue()));
			}
			segment.setCondition(condition);
		}
		segment.setStart(start);
		segment.setEnd(end);
		segment.addStyle(IDiagramEditorController.TEXT_COLOR_STYLE_ID, toRGBCode(color.getValue()));
	}

	public void setSegment(ISegmentCommon seg) {
		this.segment = seg;
	}
}
