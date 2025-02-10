package fr.univrouen.umlreverse.model.io.parser.util.clazz;


import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewEntity;
import fr.univrouen.umlreverse.model.io.parser.util.common.ParseDiagram;
import fr.univrouen.umlreverse.model.project.*;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A parse class object entity / class / interface / enum.
 */
public class ParseClassObjectEntity extends ParseClassEntity {
    // CONSTANTES
    private static final String ALREADY_EXISTS_ERR = "Entité déjà existante : ";
    private static final String MERGE_INFO = "Entité récupérée, fusion en cours.";
    private static final String INTERNAL_ERROR = "Impossible d'ajouter : ";
    private static final String ADD_ERR = "Impossible d'ajouter le champ ";

    // ATTRIBUTES
    private TypeEntity type;
    private Visibility visibility;
    private List<String> generics;
    private final Set<String> fields;
    private final Set<IMethod> methods;
    private final Set<IAttribute> attributes;
    private final Set<String> enums;
    private ObjectEntity model;

    // CONSTRUCTORS
    public ParseClassObjectEntity(ParseClassDiagram diagram, ParseClassPackage parent, TypeEntity type, String name) {
        super(diagram, parent, name);
        if (parent == null) throw new IllegalArgumentException("parent == null");
        this.type = type;
        this.generics = new ArrayList<>();
        this.fields = new HashSet<>();
        this.methods = new HashSet<>();
        this.attributes = new HashSet<>();
        this.enums = new HashSet<>();
        this.visibility = Visibility.Public;
        parent.add(this);
    }
    public ParseClassObjectEntity(ParseClassDiagram diagram, ParseClassPackage parent, String name) {
        this(diagram, parent, TypeEntity.Clazz, name);
    }

    // REQUESTS
    public ObjectEntity getModel() {
        return model;
    }

    public TypeEntity getType() {
        return type;
    }

    public List<String> getGenerics() {
        return generics;
    }

    public Visibility getVisibility() { return visibility; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParseClassObjectEntity that = (ParseClassObjectEntity) o;

        return getName().equals(that.getName());
    }

    // COMMANDS
    public void setType(TypeEntity type) {
        this.type = type;
    }

    public void addGenerics(String element) {
        this.generics.add(element);
    }

    public void setVisibility(Visibility visibility) { this.visibility = visibility; }

    public boolean addField(String field) {
        return fields.add(field);
    }
    public boolean addMethod(IMethod method) {
        return methods.add(method);
    }
    public boolean addAttribute(IAttribute attribute) {
        return attributes.add(attribute);
    }
    public boolean addEnum(String enumStr) {
        return enums.add(enumStr);
    }

    @Override
    public void addIntoModel() {
        model = new ObjectEntity(getName(), type, visibility);

        IViewEntity viewEntity = null;

        try {
            getDiagram().getModel().getProject().addEntity(model);
            viewEntity = new ViewEntity(model, getDiagram().getModel());
            viewEntity.addStyle(IDiagramEditorController.POSITION_STYLE_ID, (ParseDiagram.count_generation % 5) * ParseDiagram.SCALE_GENERATION_X + "|" + (ParseDiagram.count_generation / 5)  * ParseDiagram.SCALE_GENERATION_Y + "|0|0");
            ++ParseDiagram.count_generation;
            getDiagram().getModel().addEntity(viewEntity);
        } catch (RefusedAction refusedAction) {
            getDiagram().getLogger().addError(ALREADY_EXISTS_ERR + getName());
            for (IViewEntity currentViewEntity : getDiagram().getModel().getEntities()) {
                if (currentViewEntity.getName().equals(getName())) {
                    viewEntity = currentViewEntity;
                    getDiagram().getLogger().addInfo(MERGE_INFO);
                }
            }
        }

        if (viewEntity != null) {
            for (String field : fields) {
                viewEntity.addAbstractText(field);
            }
            for (IMethod method : methods) {
                try {
                    viewEntity.addMethod(method);
                } catch (RefusedAction refusedAction) {
                    getDiagram().getLogger().addError(ADD_ERR + getName() + "." + method.getName());
                }
            }
            for (IAttribute attribute : attributes) {
                try {
                    viewEntity.addAttribute(attribute);
                } catch (RefusedAction refusedAction) {
                    getDiagram().getLogger().addError(ADD_ERR + getName() + "." + attribute.getVariable().getName());
                }
            }
            for (String enumStr : enums) {
                viewEntity.addEnumField(enumStr);
            }
        } else {
            getDiagram().getLogger().addError(INTERNAL_ERROR + getName());
        }
    }
}
