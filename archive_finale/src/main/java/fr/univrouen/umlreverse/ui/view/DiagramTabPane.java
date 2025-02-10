package fr.univrouen.umlreverse.ui.view;

import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import fr.univrouen.umlreverse.UmlReverseBeans;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.diagram.packag.IPackageDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.SequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.UsecaseDiagram;
import fr.univrouen.umlreverse.ui.view.clazz.ClassDiagramEditor;
import fr.univrouen.umlreverse.ui.view.common.ADiagramEditor;
import fr.univrouen.umlreverse.ui.view.packag.PackageDiagramEditor;
import fr.univrouen.umlreverse.ui.view.sequence.SequenceDiagramEditor;
import fr.univrouen.umlreverse.ui.view.usecase.UsecaseDiagramEditor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;


public class DiagramTabPane {
	/**
	 * The spacing between a tab’s title and its Close button.
	 */
	private static final String TAB_TITLE_PADDING = "   ";

// ATTRIBUTES
    /**
     * A map that has the diagram model as key and the corresponding tab as value.
     */
    private final Map<IDiagram, Tab> diagrams;
    /**
     * The intern tab pane.
     */
    private final TabPane tabPane;
    /**
     * The beans of the application.
     */
    private final UmlReverseBeans beans;
    /**
     * A property change listener for diagram removed.
     */
    private PropertyChangeListener diagramRemovedListener;
    
// CONSTRUCTORS

    /**
     * Constructor of the diagram tab pane.
     * @param x The width of this diagram tab pane
     * @param y The height of this diagram tab pane
     * @param beans The beans of the application
     */
    public DiagramTabPane(int x, int y, UmlReverseBeans beans) {
        this.beans = beans;
        diagrams = new HashMap<>();
        tabPane = new TabPane();
        tabPane.setPrefSize(x, y);
        createControllerForTabPane();
        createControllerBeans();
    }

    // REQUESTS

    /**
     * Getter for the tab pane.
     * @return TabPane
     */
    public TabPane getTabPane() {
        return tabPane;
    }

    // PRIVATE

    /**
     * Create controller on beans property.
     */
    private void createControllerBeans() {
    	diagramRemovedListener = event -> {
			if (diagrams.containsKey(event.getOldValue())) {
				Tab t = diagrams.get(event.getOldValue());
				tabPane.getTabs().remove(t);
				t.getOnClosed().handle(null);
			}
		};
		
        beans.getProjectProperty().addListener((observable, oldValue, newValue) -> {
            Iterator<Tab> it = tabPane.getTabs().iterator();
            while (it.hasNext()) {
            	Tab t = it.next();
            	it.remove();
            	t.getOnClosed().handle(null);
            }
            
            if (oldValue != null) {
            	oldValue.removePropertyChangeListener("DiagramRemoved",
            			diagramRemovedListener);
            }
            if (newValue != null) {
            	newValue.addPropertyChangeListener("DiagramRemoved",
            			diagramRemovedListener);
            }
        });
        
        beans.diagramProperty().addListener(new ChangeListener<IDiagram>() {
            @Override
            public void changed(ObservableValue<? extends IDiagram> observable,
                                IDiagram oldValue, IDiagram newValue) {
                if (newValue != null) {
                    Tab t = diagrams.containsKey(newValue)
                            ? diagrams.get(newValue)
                            : createTab(newValue);

                    tabPane.getSelectionModel().select(t);
                    beans.setEditor((ADiagramEditor) t.getContent());

                }
            }
        });
    }

    /**
     * Create controller for the tab pane.
     */
    private void createControllerForTabPane() {
    	tabPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
    	
        // Change listener for tabPane when another tab is selected
        tabPane.getSelectionModel().selectedItemProperty().addListener(
        		(observable, oldValue, newValue) -> {
            // check that the diagram is a class diagram to add it to the beans
            if (newValue != null) {
                // create a null Diagram then seek the diagram that correspond to the tab
                IDiagram tabDiagram = null;
                for (Entry<IDiagram, Tab> entry: diagrams.entrySet()) {
                    if (entry.getValue().equals(newValue)) {
                        tabDiagram = entry.getKey();
                    }
                }
                
                if (tabDiagram != null) {
                	beans.setDiagram(tabDiagram);
                }
            }
        });
    }

    /**
     * Create tab for the tab pane.
     * @param diagram The diagram that will correspond to the futur tab
     * @return Tab
     */
    private Tab createTab(IDiagram diagram) {
    	Tab newTab;
        if (diagram instanceof IPackageDiagram){
            newTab = new Tab(diagram.getName() + TAB_TITLE_PADDING,
            		new PackageDiagramEditor((IPackageDiagram) diagram));
        } else if (diagram instanceof IUsecaseDiagram) {
            newTab = new Tab(diagram.getName() + TAB_TITLE_PADDING,
            		new UsecaseDiagramEditor((UsecaseDiagram) diagram));
        } else if (diagram instanceof ISequenceDiagram){
            newTab = new Tab(diagram.getName() + TAB_TITLE_PADDING,
            		new SequenceDiagramEditor((SequenceDiagram) diagram));
        } else if (diagram instanceof IClassDiagram) {
            newTab = new Tab(diagram.getName() + TAB_TITLE_PADDING,
            		new ClassDiagramEditor((ClassDiagram) diagram));
        } else {
        	// If no diagram matchs
        	newTab = new Tab(diagram.getName() + TAB_TITLE_PADDING,
            		new ClassDiagramEditor((ClassDiagram) diagram));
        }
        
        newTab.setId(diagram.getName());

        newTab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                diagrams.remove(diagram);
                Tab t = tabPane.getSelectionModel().getSelectedItem();

                beans.setDiagram(t == null ? null :
                		beans.getProject().getDiagramByName(t.getId()));
                beans.setEditor(t == null ? null : (ADiagramEditor) t.getContent());
            }
        });
    	
    	diagram.addPropertyChangeListener("NameChanged",
    			event -> newTab.setText(event.getNewValue() + TAB_TITLE_PADDING));
    	
    	diagrams.put(diagram, newTab);
    	tabPane.getTabs().add(newTab);
    	return newTab;
    }
}
