
package fr.univrouen.umlreverse.model.project;


import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewPackage;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.model.util.ErrorAbstraction;
import fr.univrouen.umlreverse.model.util.RefusedAction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Package class represents the packages
 */
public class Package implements IPackage {
// ATTRIBUTES
	/**
	 * The list of viewer.
	 */
	private List<IViewPackage> viewers = new LinkedList<>();
	/**
	 * The name.
	 */
	private String namePackage;

	/**
	 * The packages.
	 */
	private Set<IPackage> packages = new HashSet<>();

	/**
	 * The entities.
	 */
	private Set<IObjectEntity> listEntities = new HashSet<>();

	/**
	 * The parent package.
	 */
	private IPackage parent = null;

	// CONSTRUCTOR

	/**
	 * Constructor
	 *
	 * @param namePackage the name of package
	 */
	public Package(String namePackage) throws RefusedAction {
		this(namePackage, null);
	}

	/**
	 * constructor
	 *
	 * @param name   the name
	 * @param parent the folder
	 */
	public Package(String name, Package parent) throws RefusedAction {
		if (parent != null) {
			for (IPackage _package : parent.getPackages()) {
				if (_package.getName().equals(name) && _package != this) {
					throw new RefusedAction(ErrorAbstraction.ConflictNamePackageInPackage);
				}
			}
		}

		this.parent = parent;
		this.namePackage = name;
		if (parent != null) {
			parent.addPackage(this);
		}
	}

	// REQUESTS

	@Override
	public IPackage getPackage() {
		return parent;
	}

	@Override
	public String getAbsoluteName() {
		if (namePackage == null) {
			return "";
		}
		if (parent == null) {
			return namePackage;
		}
		String parentStr = parent.getAbsoluteName();
		if (parentStr.length() > 0) {
			return parentStr + "." + namePackage;
		} else {
			return namePackage;
		}
	}

	@Override
	public String getName() {
		return namePackage;
	}

	@Override
	public Set<IPackage> getPackages() {
		return packages;
	}

    @Override
    public Set<IObjectEntity> getAllEntitiesIncluded() {
        Set<IObjectEntity> allEntities = new HashSet<>();
		getAllEntitiesOfAPackage(allEntities, this);
        return allEntities;
    }
    /**
     * Private function to get all entities in a list
     * @param allEntities list that will contain all entity
     * @param pack the current package
     */
    private void getAllEntitiesOfAPackage(Set<IObjectEntity> allEntities, IPackage pack) {
		allEntities.addAll(pack.getListEntities());
		Set<IPackage> packages = pack.getPackages();
        for (IPackage p : packages) {
			getAllEntitiesOfAPackage(allEntities, p);
        }
    }

    @Override
	public Set<IObjectEntity> getEntities(TypeEntity filter) {
		return listEntities.stream().filter(entity -> entity.getType().equals(filter)).collect(Collectors.toSet());
	}

	@Override
	public Set<IObjectEntity> getEntities(String filterByName) {
		return listEntities.stream().filter(entity -> entity.getName().equals(filterByName)).collect(Collectors.toSet());
	}

	@Override
	public Set<IObjectEntity> getListEntities() {
		return listEntities;
	}

	// METHODS

	@Override
	public void setPackage(IPackage parent) throws RefusedAction {
		if (parent != null) {
			for (IPackage _package : parent.getPackages()) {
				if (_package.getName().equals(namePackage) && _package != this) {
					throw new RefusedAction(ErrorAbstraction.ConflictNamePackageInPackage);
				}
			}
		}
        Map<IViewEntity, IStyle> viewToStyle = new HashMap<>();
        Set<IObjectEntity> entities = this.getAllEntitiesIncluded();
        for (IObjectEntity entity : entities) {
            for (IViewEntity view : entity.getListViewer()) {
                String selecteur = entity.getType().toString();
                String id = entity.getAbsoluteName();
                viewToStyle.put(view, view.getDiagram().getStyle().getStyle(selecteur, id));
                view.getDiagram().getStyle().removeStyle(selecteur, id);
            }
        }
        this.parent = parent;
        for (IViewEntity view : viewToStyle.keySet()) {
            String selecteur = view.getData().getType().toString();
            String id = view.getData().getAbsoluteName();
            view.getDiagram().getStyle().addStyle(selecteur, id, viewToStyle.get(view));
        }
	}

	@Override
	public void setName(String namePackage) throws RefusedAction {
		if (parent != null) {
			for (IPackage _package : parent.getPackages()) {
				if (_package.getName().equals(namePackage) && _package != this) {
					throw new RefusedAction(ErrorAbstraction.ConflictNamePackageInPackage);
				}
			}
		}
		this.namePackage = namePackage;
	}

	@Override
	public void addPackage(IPackage _package) throws RefusedAction {
		_package.setPackage(this);
		packages.add(_package);
	}

	@Override
	public void removePackage(IPackage _package) {
		packages.remove(_package);
	}

	@Override
	public void addEntity(IObjectEntity entity) throws RefusedAction {
		entity.setPackage(this);
		listEntities.add(entity);
	}

	@Override
	public void removeEntity(IObjectEntity entity) {
		listEntities.remove(entity);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Package)) return false;

		Package aPackage = (Package) o;
		String fullnameThis = getAbsoluteName();
		String fullnameO = aPackage.getAbsoluteName();

		return fullnameThis != null ? fullnameThis.equals(fullnameO) : fullnameO == null;
	}

	@Override
	public int hashCode() {
		int result = getAbsoluteName() != null ? getAbsoluteName().hashCode() : 0;
		return result;
	}
}
