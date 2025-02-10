package fr.univrouen.umlreverse.ui.view.packag;

import java.util.Collection;
import java.util.Map;

import fr.univrouen.umlreverse.model.diagram.clazz.view.INoteClass;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewPackage;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewRelation;
import fr.univrouen.umlreverse.model.diagram.packag.IPackageDiagram;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.ui.component.clazz.elements.NoteGraphic;
import fr.univrouen.umlreverse.ui.component.clazz.elements.ObjectEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.elements.EnlargePoint;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityGraphic;
import fr.univrouen.umlreverse.ui.component.common.relation.IRelationGraphic;
import fr.univrouen.umlreverse.ui.component.packag.elements.PackageGraphic;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import javafx.scene.shape.Circle;

public interface IPackageController extends IDiagramEditorController {

	IPackageDiagram getDiagram();

	PackageGraphic getPackage(IViewPackage packag);

	Map<IViewEntity, PackageGraphic> getPackages();

	Collection<NoteGraphic> getNotes();

	Collection<ObjectEntityGraphic> getObjectEntityGraphics();

	ObjectEntityGraphic getOEG(IViewEntity entity);

	void createEntity(double x, double y, TypeEntity t);

	void createPackage(double x, double y);

	void createPackageRelation(IViewEntity entity);

	void createEntityRelation(IViewEntity entity);

	void createNoteRelation(INoteClass note);

	void hideObjectEntity(ObjectEntityGraphic oEG);

	void removeNote(INoteClass note);

	void removeObjectEntity(ObjectEntityGraphic oEG);

	void removePackage(IViewPackage packag);

	void removeRelation(IViewRelation relation);

	void removeRelationGraphic(IRelationGraphic r);

	EnlargePoint bindToEnlargePackagePoint(IEntityGraphic bindedObject);
}
