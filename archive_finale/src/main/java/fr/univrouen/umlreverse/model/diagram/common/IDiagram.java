
package fr.univrouen.umlreverse.model.diagram.common;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.diagram.packag.IPackageDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.usecase.IUsecaseDiagram;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.diagram.util.IStyleBox;
import fr.univrouen.umlreverse.model.diagram.visitor.IDiagramVisitor;
import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.util.RefusedAction;

/**
 * Super interface to all diagram.
 */
public interface IDiagram extends Serializable, IObservable {

// ATTRIBUTES
String ALL_STYLE_ID = "*";
    String NOTE_STYLE_ID = "note";
    String RELATION_STYLE_ID = "relation";

    String STYLE_CHANGED_PROPERTY_NAME = "StyleChanged";
    String NOTE_ADDED_PROPERTY_NAME = "NoteAdded";
    String NOTE_REMOVED_PROPERTY_NAME = "NoteDeleted";
    String NAME_CHANGED_PROPERTY_NAME = "NameChanged";
    String TITLE_CHANGED_PROPERTY_NAME = "TitleChanged";

    String SEGMENT_ADDED_PROPERTY_NAME = "SegmentAdded";
    String SEGMENT_REMOVED_PROPERTY_NAME = "SegmentRemoved";

    String ACTIVITY_ADDED_PROPERTY_NAME = "ActivityAdded";
    String ACTIVITY_REMOVED_PROPERTY_NAME = "ActivityRemoved";

    String RELATION_ADDED_PROPERTY_NAME = "RelationAdded";
    String RELATION_REMOVED_PROPERTY_NAME = "RelationRemoved";

    String PACKAGE_ADDED_PROPERTY_NAME = "PackageAdded";
    String PACKAGE_REMOVED_PROPERTY_NAME = "PackageRemoved";


// REQUESTS
	/**
	 * The project.
	 */
	IProject getProject();
	/**
	 * The style of the element "id" of type "type"
	 * @pre
	 * 		type != null
	 * 		id != null
     * @return
	 * 		the reference of the IStyle associated to "id" of type "type"
     */
	IStyle getStyle(String type, String id);

	IStyleBox getStyle();

	/**
	 * The name of the diagram.
	 * @return
	 * 		getName() != null
     */
	String getName();

	/**
	 * The title of the diagram
	 * @return
	 * 		getTitle() != null
     */
	String getTitle();

	/**
	 * The type of the diagram
	 * @return
	 *      getType() != null
	 */
	DiagramType getType();

	/**
	 * Erase the old style and replace with styles.
     */
	void applyStyle(IStyleBox styles);

	/**
	 * Add style in StyleBox.
	 * @param selector
	 * @param id
	 * @param key
	 * @param value
	 * @pre
	 *      selector != null
	 *      && i != null
	 *      && key != null
	 *      && value != null
	 */
	void addStyle(String selector, String id, String key, String value);

	/**
	 * Set the name.
	 * @pre
	 * 		name != null
	 * 		name.length() > 0
     */
	void setName(String name) throws RefusedAction;

	/**
	 * Set the title.
     */
	void setTitle(String title);

	/**
	 * Accept a visitor.
	 * @pre
	 * 		visitor != null
     */
	void accept(IDiagramVisitor visitor);

	/**
	 * getIds gives all the ids that are contained in a diagram
	 * @return List<String>
     */
	Set<String> getIds();

	/**
	 * addId allows to add an id
	 * @param id
     */
    boolean addId(String id);

	/**
	 * removeId allows to remove an id
	 * @param id
     */
	void removeId(String id);

	// NESTED TYPES
	enum DiagramType {
		CLASS("Classe", IClassDiagram.class),
		USECASE("Cas d’utilisation", IUsecaseDiagram.class),
		SEQUENCE("Séquence", ISequenceDiagram.class),
		PACKAGE("Paquetage", IPackageDiagram.class);

		private static final Map<Class<? extends IDiagram>, DiagramType> classes = new HashMap<>();

		static {
			for (DiagramType dt: EnumSet.allOf(DiagramType.class)) {
				classes.put(dt.actualType, dt);
			}
		}

		private final String displayName;
		private final Class<? extends IDiagram> actualType;

		DiagramType(String name, Class<? extends IDiagram> c) {
			displayName = name;
			actualType = c;
		}

		/**
		 * getDisplayName GETTER that allows to get the display name of the diagram
		 * @return
         */
		public String getDisplayName() {
			return displayName;
		}

        /**
         * fromClass function that allows to build the tree view manager of the application
         * @param c
         * @return DiagramType
         */
		public static DiagramType fromClass(Class<? extends IDiagram> c) {
			return classes.get(c);
		}
	}

}
