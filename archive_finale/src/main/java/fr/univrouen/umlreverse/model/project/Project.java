
package fr.univrouen.umlreverse.model.project;

import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewRelation;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.util.Contract;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.*;

/**
 * Package class represents the projects
 */
public class Project implements Serializable, IProject {

	// ATTRIBUTES

	private PropertyChangeSupport support = new PropertyChangeSupport(this);
	private Set<IRelation> relations = new HashSet<>();
	private Set<IDiagram> diagrams = new HashSet<>();
	private String name;
	private IPackage root;

	// CONSTRUCTOR

	/**
	 * Default constructor
	 */
	public Project(String name) {
		Contract.check(name != null && name.length() > 0);
		try {
			this.name = name;
			root = new Package(null);
		} catch (RefusedAction ignored) {
		}
	}

	// STATIC FUNCTIONS

	/**
	 * Create an empty project.
	 */
	public static IProject newProject(String name){
		return new Project(name);
	}

	/**
	 * Load a project from a file.
	 */
	public static IProject deserialize(File f) throws IOException, ClassNotFoundException {
		IProject result;
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
		result = (IProject) ois.readObject();
		ois.close();
		return result;
	}

	// REQUESTS

	@Override
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return support.getPropertyChangeListeners();
	}

	@Override
	public Set<IDiagram> getDiagrams() {
		return diagrams;
	}

	@Override
	public Set<IRelation> getRelations() {
		return relations;
	}

	@Override
	public IPackage getRootPackage(){
		return root;
	}

	@Override
	public boolean contains(IObjectEntity entity) {
		return getAllEntities().contains(entity);
	}

	@Override
	public boolean contains(IRelation relation) {
		return relations.contains(relation);
	}

    @Override
    public boolean contains(IPackage _package) {
        return existingPackageInProject(_package);
    }

	@Override
	public IDiagram getDiagramByName(String name) {
		for (IDiagram diagram : diagrams) {
			if (diagram.getName().equals(name)) {
				return diagram;
			}
		}
		return null;
	}
    // COMMANDS
	public Set<IObjectEntity> getAllEntities() {
		return getRootPackage().getAllEntitiesIncluded();
	}

	/**
	 * Private function to get all entities in a list
	 * @param allEntities list that will contain all entity
	 * @param pack the current package
     */
	private void getAllEntitiesOfAPackage(List<IObjectEntity> allEntities, IPackage pack) {
		allEntities.addAll(pack.getListEntities());
		Set<IPackage> packages = pack.getPackages();
		for (IPackage p : packages) {
			getAllEntitiesOfAPackage(allEntities, p);
		}
	}
	public String getName() {
		return name;
	}
	// COMMANDS

	public void setName(String name) {
		Contract.check(name != null && name.length() > 0);
		String old = getName();
		if (!old.equals(name)) {
			this.name = name;
			firePropertyChanged("NameChanged", old, name);
		}
	}

	@Override
	public void addRelation(IRelation relation){
		if (!relations.contains(relation)) {
			relations.add(relation);
			firePropertyChanged("RelationAdded", null, relation);
		}
	}

	@Override
	public void removeEntity(IObjectEntity entity) {
		if (contains(entity)) {
			
			removeEntityFromData(entity);
			firePropertyChanged("EntityRemoved", entity, null);
		}
	}

	@Override
	public void removeDiagram(IDiagram diagram) {
		if (diagrams.contains(diagram)) {
			diagrams.remove(diagram);
			firePropertyChanged("DiagramRemoved", diagram, null);

		}
	}

	@Override
	public void removeRelation(IRelation relation){
		if (relations.contains(relation)) {
			relations.remove(relation);
            for (IViewRelation view : relation.getViews()) {
                view.getDiagram().hideRelation(view);
            }
			firePropertyChanged("RelationRemoved", relation, null);
		}
	}
	
	@Override
	public void serialize(File file) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(this);
		oos.close();
	}

    @Override
    public void addEntity(IObjectEntity entity) throws RefusedAction {
		if (!contains(entity)) {
			if (entity.getPackage() == null) {
				getRootPackage().addEntity(entity);
			} else {
				createPackage(entity.getPackage());
			}
			firePropertyChanged("EntityAdded", null, entity);
		}
    }

    @Override
    public void addPackage(IPackage _package) throws RefusedAction {
		if (!contains(_package)) {
			createPackage(_package);
			firePropertyChanged("PackageAdded", null, _package);
		}
    }

	@Override
	public void removePackage(IPackage aPackage) {
		if (contains(aPackage)) {
			removePackageFromData(aPackage);
			firePropertyChanged("RemovePackage", aPackage, null);
		}

	}

	@Override
	public void addDiagram(IDiagram diagram) throws RefusedAction {
		if (!diagrams.contains(diagram)) {
			Set<IDiagram> old = new HashSet<>(getDiagrams());
			for (IDiagram d : diagrams) {
				if (d.getName().equals(diagram.getName())) {
					throw new RefusedAction(ErrorAbstraction.ConflictOfNameInProject);
				}
			}
			diagrams.add(diagram);
			firePropertyChanged("DiagramsChanged", old, getDiagrams());
		}
	}

	@Override
	public void addPropertyChangeListener(String name, PropertyChangeListener listener) {
		support.addPropertyChangeListener(name, listener);
	}

	@Override
	public void removePropertyChangeListener(String name, PropertyChangeListener listener) {
		support.removePropertyChangeListener(name, listener);
	}

	protected void firePropertyChanged(String property, Object oldValue, Object newValue) {
		support.firePropertyChange(property, oldValue, newValue);
	}

	// TOOLS

	private void getComponents(IPackage aPackage, List<IPackage> packages, List<IObjectEntity> entities) {
		packages.add(aPackage);
		recursiveGetComponents(aPackage, packages, entities);
	}

	private void recursiveGetComponents(IPackage aPackage, List<IPackage> packages, List<IObjectEntity> entities) {
		entities.addAll(aPackage.getListEntities());
		for (IPackage p : aPackage.getPackages()) {
			recursiveGetComponents(p, packages, entities);
			packages.add(p);
		}
	}

    private boolean existingPackageInProject(IPackage toFind) {
        if (toFind == null) {
            return true;
        }
        LinkedList<IPackage> packages = new LinkedList<>();
        IPackage current = toFind;
        while (current != null) {
            packages.push(current);
            current = current.getPackage();
        }
        IPackage folder = getRootPackage();
        while (packages.size() > 0) {
            IPackage tmp = packages.pop();
            if (!folder.getPackages().contains(tmp)) {
                return false;
            }
            folder = tmp;
        }
        return true;
    }

	private void removeEntityFromData(IObjectEntity entity) {
		Contract.check(entity != null);

		List<IRelation> relations = new LinkedList<>(getRelations());
		for (IRelation r : relations) {
			if (r.getEntitySource().equals(entity) || r.getEntityTarget().equals(entity)) {
				this.relations.remove(r);
				for (IViewRelation view : r.getViews()) {
					view.getDiagram().removeRelation(view);
				}
			}
            if (r.getAssociationEntity() != null && r.getAssociationEntity().equals(entity)) {
				r.setAssociationEntity(null);
			}
		}


        if (entity.getPackage() != null) {
            entity.getPackage().removeEntity(entity);
            try { entity.setPackage(null); } catch (RefusedAction ignored) {}
        } else {
            getRootPackage().removeEntity(entity);
        }

		List<IViewEntity> toDelete = new ArrayList<>();
		for (IViewEntity view : entity.getListViewer()) {
			toDelete.add(view);
		}
		for (IViewEntity view : toDelete) {
			view.getDiagram().hideEntity(view);
		}
	}

	private void removePackageFromData(IPackage _package) {
		if (existingPackageInProject(_package)) {
			List<IPackage> resultPackage = new LinkedList<>();
			List<IObjectEntity> resultEntity = new LinkedList<>();
			getComponents(_package, resultPackage, resultEntity);
			resultEntity.forEach(this::removeEntityFromData);
			if (_package.getPackage() != null) {
                _package.getPackage().removePackage(_package);
            } else {
                getRootPackage().removePackage(_package);
            }
		}
	}

    private void createPackage(IPackage _package) throws RefusedAction {
        LinkedList<IPackage> packages = new LinkedList<>();
        IPackage current = _package;
        while (current != null) {
            packages.push(current);
            current = current.getPackage();
        }

        current = getRootPackage();
        while (packages.size() > 0) {
            IPackage toLookingFor = packages.pop();
            if (!current.getPackages().contains(toLookingFor)) {
                current.addPackage(toLookingFor);
            }
            current = toLookingFor;
        }
    }
}
