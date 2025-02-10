package fr.univrouen.umlreverse.ui.view;

import java.beans.PropertyChangeListener;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.univrouen.umlreverse.UmlReverseBeans;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.diagram.common.IDiagram.DiagramType;
import fr.univrouen.umlreverse.model.diagram.packag.PackageDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.SequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.UsecaseDiagram;
import fr.univrouen.umlreverse.model.diagram.visitor.SortedDiagramVisitor;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class TreeFileManagerView extends TreeView<TreeFileManagerView.ITreeData<?>> {
	// ATTRIBUTES
	/**
	 * The beans of the application.
	 */
	private UmlReverseBeans beans;
	/**
	 * The displayed diagram types and the corresponding tree items.
	 */
	private Map<DiagramType, TreeItem<ITreeData<?>>> typeItems;
	/**
	 * The displayed diagrams and the corresponding tree items.
	 */
	private Map<IDiagram, TreeItem<ITreeData<?>>> diagramItems;
	/**
	 * Change listener on the project.
	 */
	private ChangeListener<IProject> projectListener;
	/**
	 * Property change listener that rebuilds the tree.
	 */
	private PropertyChangeListener rebuildListener;
	/**
	 * Property change listener that removes a diagram from the tree.
	 */
	private PropertyChangeListener removeListener;
	
	// CONSTRUCTORS

	/**
	 * Constructor of the tree file manager.
	 * @param b the beans of the application.
     */
	public TreeFileManagerView(UmlReverseBeans b) {
		Contract.check(b != null, "b must not be null");
		beans = b;
		typeItems = new EnumMap<>(DiagramType.class);
		diagramItems = new HashMap<>();
		setCellFactory(tree -> new TreeFileManagerCell());
		createListeners();
		setProjectListeners(beans.getProject());
		beans.getProjectProperty().addListener(projectListener);
		buildTree();
	}
	
	// QUERIES

	/**
	 * Getter of the beans.
	 * @return
     */
	public UmlReverseBeans getUmlReverseBeans() {
		return beans;
	}
	
	// COMMANDS

	/**
	 * Setter of the beans.
	 * @param b the beans of the application
     */
	public void setUmlReverseBeans(UmlReverseBeans b) {
		Contract.check(b != null, "b must not be null");
		unsetProjectListeners(beans.getProject());
		beans.getProjectProperty().removeListener(projectListener);
		beans = b;
		setProjectListeners(beans.getProject());
		beans.getProjectProperty().addListener(projectListener);
		buildTree();
	}
	
	// TOOLS

	/**
	 * Build the tree representing the current state of the project.
	 */
	private void buildTree() {
		assert beans != null;
		IProject project = beans.getProject();
		
		if (project == null) {
			setRoot(null);
			return;
		}
		
		TreeItem<ITreeData<?>> root = new TreeItem<ITreeData<?>>(
				new ProjectTreeData(project));
		
		SortedDiagramVisitor visitor = new SortedDiagramVisitor();
		for (IDiagram diagram: project.getDiagrams()) {
			diagram.accept(visitor);
		}

		root.setExpanded(true);
		setRoot(root);
		
		Map<Class<? extends IDiagram>, Set<IDiagram>> diagrams =
				visitor.getAllDiagrams();
		for (Class<? extends IDiagram> type: diagrams.keySet()) {
			DiagramType diagType = DiagramType.fromClass(type);
			TreeItem<ITreeData<?>> typeItem = new TreeItem<ITreeData<?>>(
					new StringTreeData(diagType.getDisplayName()));
			for (IDiagram diagram: diagrams.get(type)) {
				TreeItem<ITreeData<?>> diagItem =
						new TreeItem<ITreeData<?>>(new DiagramTreeData(diagram));
				typeItem.getChildren().add(diagItem);
				diagramItems.put(diagram, diagItem);
			}
			root.getChildren().add(typeItem);
			typeItems.put(diagType, typeItem);
		}
	}

	/**
	 * Create listener to manage variation of project and rebuild.
	 */
	private void createListeners() {
		projectListener = (observable, oldValue, newValue) -> {
			unsetProjectListeners(oldValue);
			setProjectListeners(newValue);
			buildTree();
		};
		
		rebuildListener = e -> buildTree();
		
		removeListener = e -> {
			IDiagram d = (IDiagram) e.getOldValue();
			TreeItem<ITreeData<?>> diagItem = diagramItems.remove(d);
			TreeItem<ITreeData<?>> typeItem = typeItems.get(d.getType());
			typeItem.getChildren().remove(diagItem);
			if (typeItem.getChildren().isEmpty()) {
				typeItems.remove(d.getType());
				getRoot().getChildren().remove(typeItem);
			}
		};
	}

	/**
	 * Open a diagram
	 * @param diagram A diagram to be open
     */
	private void openDiagram(IDiagram diagram) {
        beans.setDiagram(diagram);
	}

	/**
	 * Set the project listener.
	 * @param p The project to observe
     */
	private void setProjectListeners(IProject p) {
		if (p != null) {
			p.addPropertyChangeListener("DiagramsChanged", rebuildListener);
			p.addPropertyChangeListener("DiagramRemoved", removeListener);
		}
	}

	/**
	 * Remove the project listener.
	 * @param p The project that contain propertyChangeListener to remove
     */
	private void unsetProjectListeners(IProject p) {
		if (p != null) {
			p.removePropertyChangeListener("DiagramsChanged", rebuildListener);
			p.removePropertyChangeListener("DiagramRemoved", removeListener);
		}
	}
	
	// NESTED TYPES
	public interface ITreeData<T> {
		/**
		 * The context menu.
		 * @return ContextMenu
         */
		ContextMenu getContextMenu();

		/**
		 * Getter of data.
		 * @return T
         */
		T getData();

		/**
		 * Add a listener to update the tree.
		 * @param listener Property Change listener
         */
		void addUpdateListener(PropertyChangeListener listener);

		/**
		 * Remove a listener.
		 * @param listener The listener that has to be removed.
         */
		void removeUpdateListener(PropertyChangeListener listener);
		void trigger();
	}
	
	public abstract class ATreeData<T> implements ITreeData<T> {
		// ATTRIBUTES
		/**
		 * The context menu for root of tree.
		 */
		protected ContextMenu menu;
		
		// CONSTRUCTORS
		public ATreeData() {
			/**
			 * Menu item to create a new Class diagram.
			 */
			MenuItem newClassDiagMI = new MenuItem("Nouveau diagramme de classe");
			newClassDiagMI.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
			        TextInputDialog dialog = new TextInputDialog();
			        dialog.setTitle("Nouveau diagramme de classe");
			        dialog.setHeaderText("Ajouter un nouveau diagramme de classe");
			        dialog.setContentText("Entrer un nom :");

			        dialog.showAndWait().ifPresent(name -> {
			            try {
			                beans.setDiagram(new ClassDiagram(beans.getProject(), name));
			            } catch (RefusedAction ex) {
			            	new Alert(AlertType.ERROR, ErrorAbstraction.getErrorFromCode(
			            			ex.getMessage()).getExplain()).showAndWait();
			                handle(event);
			            } catch (IllegalArgumentException e) {
			                new Alert(AlertType.ERROR, "Nom de diagramme invalide").showAndWait();
			                handle(event);
			            }
			        });
				}
			});
			/**
			 * Menu item to create a new use case diagram.
			 */
			MenuItem newUseCaseDiagMI = new MenuItem("Nouveau diagramme de cas d’utilisation");
			newUseCaseDiagMI.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
			        TextInputDialog dialog = new TextInputDialog();
			        dialog.setTitle("Nouveau diagramme de cas d’utilisation");
			        dialog.setHeaderText("Ajouter un nouveau diagramme de cas d’utilisation");
			        dialog.setContentText("Entrer un nom :");

			        dialog.showAndWait().ifPresent(name -> {
			            try {
			                beans.setDiagram(new UsecaseDiagram(beans.getProject(), name));
			            } catch (RefusedAction ex) {
			            	new Alert(AlertType.ERROR, ErrorAbstraction.getErrorFromCode(
			            			ex.getMessage()).getExplain()).showAndWait();
			                handle(event);
			            } catch (IllegalArgumentException e) {
			                new Alert(AlertType.ERROR, "Nom de diagramme invalide").showAndWait();
			                handle(event);
			            }
			        });
				}
			});
			
			
			/**
			 * Menu item to create a new sequence diagram.
			 */
			MenuItem newSequenceDiagMI = new MenuItem("Nouveau diagramme de séquence");
			newSequenceDiagMI.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
			        TextInputDialog dialog = new TextInputDialog();
			        dialog.setTitle("Nouveau diagramme de séquence");
			        dialog.setHeaderText("Ajouter un nouveau diagramme séquence");
			        dialog.setContentText("Entrer un nom :");

			        dialog.showAndWait().ifPresent(name -> {
			            try {
			                beans.setDiagram(new SequenceDiagram(beans.getProject(), name));
			            } catch (RefusedAction ex) {
			            	new Alert(AlertType.ERROR, ErrorAbstraction.getErrorFromCode(
			            			ex.getMessage()).getExplain()).showAndWait();
			                handle(event);
			            } catch (IllegalArgumentException e) {
			                new Alert(AlertType.ERROR, "Nom de diagramme invalide").showAndWait();
			                handle(event);
			            }
			        });
				}
			});
			
			
			/**
			 * Menu item to create a new sequence diagram.
			 */
			MenuItem newPackageDiagMI = new MenuItem("Nouveau diagramme de paquetage");
			newPackageDiagMI.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
			        TextInputDialog dialog = new TextInputDialog();
			        dialog.setTitle("Nouveau diagramme de paquetage");
			        dialog.setHeaderText("Ajouter un nouveau diagramme paquetage");
			        dialog.setContentText("Entrer un nom :");

			        dialog.showAndWait().ifPresent(name -> {
			            try {
			                beans.setDiagram(new PackageDiagram(beans.getProject(), name));
			            } catch (RefusedAction ex) {
			            	new Alert(AlertType.ERROR, ErrorAbstraction.getErrorFromCode(
			            			ex.getMessage()).getExplain()).showAndWait();
			                handle(event);
			            } catch (IllegalArgumentException e) {
			                new Alert(AlertType.ERROR, "Nom de diagramme invalide").showAndWait();
			                handle(event);
			            }
			        });
				}
			});

			
			menu = new ContextMenu(newClassDiagMI, newUseCaseDiagMI, newSequenceDiagMI, newPackageDiagMI);
		}
		
		// QUERIES

		/**
		 * Getter of the context menu.
		 * @return ContextMenu
         */
		public ContextMenu getContextMenu() {
			return menu;
		}
	}
	
	public class DiagramTreeData extends ATreeData<IDiagram> {
		// ATTRIBUTES
		/**
		 * Diagram.
		 */
		private IDiagram data;
		
		// CONSTRUCTORS

		/**
		 * Create a diagram tree data.
		 * @param d The corresponding diagram
         */
		public DiagramTreeData(IDiagram d) {
			Contract.check(d != null, "d must not be null");
			data = d;
			/**
			 * Menu item to rename a diagram.
			 */
			MenuItem renameMI = new MenuItem("Renommer");
			renameMI.setOnAction(e -> {
				TextInputDialog dialog = new TextInputDialog(data.getName());
				dialog.showAndWait().ifPresent(name -> {
					try {
						data.setName(name);
					} catch (RefusedAction ex) {
						new Alert(AlertType.ERROR,
								ErrorAbstraction.getErrorFromCode(
								ex.getMessage()).getExplain()).show();
					}
				});
			});
			/**
			 * Menu item to delete a diagram.
			 */
			MenuItem deleteMI = new MenuItem("Supprimer");
			deleteMI.setOnAction(e -> {
				new Alert(AlertType.CONFIRMATION,
						"Voulez-vous vraiment supprimer " +
						data.getName() + " ?").showAndWait()
						.filter(response -> response == ButtonType.OK)
						.ifPresent(response -> beans.getProject().removeDiagram(data));
			});
			
			/**
			 * Menu item to export a diagram to PlantUML.
			 */
			MenuItem exportMI = new MenuItem("Exporter en PlantUML");
			exportMI.setOnAction(e -> beans.getMenuController().exportPUML());
			
			menu.getItems().addAll(renameMI, deleteMI, exportMI);
		}
		
		// QUERIES
		@Override
		public IDiagram getData() {
			return data;
		}

		@Override
		public String toString() {
			return data.getName();
		}

		// COMMANDS
		@Override
		public void addUpdateListener(PropertyChangeListener listener) {
			data.addPropertyChangeListener("NameChanged", listener);
		}
		
		@Override
		public void removeUpdateListener(PropertyChangeListener listener) {
			data.removePropertyChangeListener("NameChanged", listener);
		}
		
		@Override
		public void trigger() {
			openDiagram(data);
		}
	}
	
	public class ProjectTreeData extends ATreeData<IProject> {
		// ATTRIBUTES
		/**
		 * Current project.
		 */
		private IProject data;
		
		// CONSTRUCTORS

		/**
		 * Constructor of a tree project.
		 * @param d The project
         */
		public ProjectTreeData(IProject d) {
			Contract.check(d != null, "d must not be null");
			data = d;
			/**
			 * Menu item to rename the project.
			 */
			MenuItem renameMI = new MenuItem("Renommer");
			renameMI.setOnAction(e -> {
				TextInputDialog dialog = new TextInputDialog(data.getName());
				dialog.showAndWait().ifPresent(name -> {
					try {
						data.setName(name);
					} catch (Exception ex) {
						new Alert(AlertType.ERROR, ex.getMessage()).show();
					}
				});
			});
			/**
			 * Menu item to delete project. (Removed on client’s request.)
			 */
//			MenuItem deleteMI = new MenuItem("Supprimer");
//			deleteMI.setOnAction(e -> {
//				new Alert(AlertType.CONFIRMATION,
//						"Voulez vous vraiment supprimer le projet ?")
//						.showAndWait()
//						.filter(response -> response == ButtonType.OK)
//						.ifPresent(response -> {
//					if (beans.getFile() != null) {
//						beans.getFile().delete();
//						beans.setFile(null);
//					}
//					beans.setDiagram(null);
//					beans.setProject(null);
//				});
//			});
			
			menu.getItems().addAll(renameMI);
		}
		
		// QUERIES
		@Override
		public IProject getData() {
			return data;
		}
		
		@Override
		public String toString() {
			return data.getName();
		}
		
		// COMMANDS
		@Override
		public void addUpdateListener(PropertyChangeListener listener) {
			data.addPropertyChangeListener("NameChanged", listener);
		}
		
		@Override
		public void removeUpdateListener(PropertyChangeListener listener) {
			data.removePropertyChangeListener("NameChanged", listener);
		}
		
		@Override
		public void trigger() {}
	}
	
	public class StringTreeData extends ATreeData<String> {
		// ATTRIBUTES
		/**
		 * String that represent the data.
		 */
		private String data;
		
		// CONSTRUCTORS

		/**
		 * Constructor of string tree data.
		 * @param d The string that represent the data
         */
		public StringTreeData(String d) {
			Contract.check(d != null, "d must not be null");
			data = d;
		}
		
		// QUERIES
		@Override
		public String getData() {
			return data;
		}
		
		@Override
		public String toString() {
			return data;
		}

		// COMMANDS
		@Override
		public void addUpdateListener(PropertyChangeListener listener) {}
		
		@Override
		public void removeUpdateListener(PropertyChangeListener listener) {}
		
		@Override
		public void trigger() {}
	}
	
	public class TreeFileManagerCell extends TreeCell<ITreeData<?>> {
		// ATTRIBUTES
		/**
		 * Property change listener that allow to update the tree cell.
		 */
		private PropertyChangeListener updateListener;
		
		// CONSTRUCTORS

		/**
		 * Constructor of tree file manager cell.
		 */
		public TreeFileManagerCell() {
			updateListener = e -> setText((String) e.getNewValue());
			
			addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
				if (!isEmpty() && e.getButton().equals(MouseButton.PRIMARY) &&
						e.getClickCount() == 2) {
					getItem().trigger();
				}
			});
		}
		
		@Override
		protected void updateItem(ITreeData<?> item, boolean empty) {
			if (!isEmpty()) {
				getItem().removeUpdateListener(updateListener);
			}
			
			super.updateItem(item, empty);
			
			if (isEmpty()) {
				setText("");
			} else {
				getItem().addUpdateListener(updateListener);
				setContextMenu(item.getContextMenu());
				setText(item.toString());
			}
		}
	}
}
