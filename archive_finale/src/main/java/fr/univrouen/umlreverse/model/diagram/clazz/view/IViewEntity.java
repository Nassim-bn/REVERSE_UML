
package fr.univrouen.umlreverse.model.diagram.clazz.view;

import fr.univrouen.umlreverse.model.diagram.common.IStylizable;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.model.project.IAttribute;
import fr.univrouen.umlreverse.model.project.IMethod;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.project.Visibility;
import fr.univrouen.umlreverse.model.diagram.clazz.visitor.IClassVisitor;
import fr.univrouen.umlreverse.model.diagram.common.IObservable;
import fr.univrouen.umlreverse.model.util.RefusedAction;

/**
 * An ObjectEntityView for the class diagram.
 */
public interface IViewEntity extends Serializable, IStylizable, IObservable {

// REQUESTS
    /**
     * The original data
     */
    IObjectEntity getData();

    /**
     * The diagram
     */
    IClassDiagram getDiagram();

    /**
     * getter of abstractText
     * @return the list of abstract string
     */
    List<String> getAbstractTexts();

    /**
     * getter of enumField
     * @return the list of enum field
     */
    List<String> getEnumFields();

    /**
     * getter of type
     * @return the type
     */
    TypeEntity getType();

    /**
     * getter of visibility
     * @return the visibility
     */
    Visibility getVisibility();

    /**
     * getter of listAttribute
     * @return the listAttribute
     */
    Set<IAttribute> getListAttribute();

    /**
     * getter of listMethod
     * @return the listMethod
     */
    Set<IMethod> getListMethod();
    /**
     * getter of name
     * @return the name
     */
    String getName();

    /**
     * The parent folder
     */
    IViewPackage getPackage();

    /**
     * The absolute name
     */
    String getAbsoluteName();

    // METHODS

    /**
     * Clear the style concerning only this entity.
     */
    void clearStyle();

    /**
     * setter of parent
     */
    void setPackage(IViewPackage parent);

    /**
     * setter of name
     * @param namePackage the name to set
     */
    void setName(String namePackage) throws RefusedAction;

    /**
     * setter of type
     * @param type the type to set
     */
    void setType(TypeEntity type);

    /**
     * add an enumerate field.
     * @param field
     * 		field != null && field.length() > 0
     * 		the new enumerate field
     */
    void addEnumField(String field);

    /**
     * Remove an enum field
     */
    void removeEnumField(String field);
    /**
     * add a abstractTextfield
     */
    void addAbstractText(String text);

    /**
     * hide a abstractTextfield
     */
    void hideAbstractText(String text);

    /**
     * remove an abstract text
     */
    void removeAbstractText(String text);

    /**
     * setter of visibility
     * @param visibility the visibility to set
     */
    void setVisibility(Visibility visibility);

    /**
     * addAttribute allows to add attributs
     * @param attribute
     * 		the attribute to add
     */
    void addAttribute(IAttribute attribute) throws RefusedAction;

    /**
     * hideAttribute allows to hide attributs
     * @param attribute
     * 		the attribute to hide
     */
    void hideAttribute(IAttribute attribute);

    /**
     * remove an attribute
     * @pre
     *      attribut != null
     */
    void removeAttribute(IAttribute attribute);

    /**
     * addMethod allows to add methodes
     * @param method
     * 		the method to add
     */
    void addMethod(IMethod method) throws RefusedAction;

    /**
     * hideMethod allows to hide methodes
     * @param method
     * 		the method to hide
     */
    void hideMethod(IMethod method);

    /**
     * Remove a method.
     * @pre
     *      method != null
     */
    void removeMethod(IMethod method);

    void setDiagram(IClassDiagram diagram) throws RefusedAction;

    void accept(IClassVisitor visitor);
}
