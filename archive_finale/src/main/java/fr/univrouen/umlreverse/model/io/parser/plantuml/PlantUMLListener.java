// Generated from /home/tekiflo/dev/uml/projet/src/main/antlr4/PlantUML.g4 by ANTLR 4.5.1
package fr.univrouen.umlreverse.model.io.parser.plantuml;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PlantUMLParser}.
 */
public interface PlantUMLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#entryPoint}.
	 * @param ctx the parse tree
	 */
	void enterEntryPoint(PlantUMLParser.EntryPointContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#entryPoint}.
	 * @param ctx the parse tree
	 */
	void exitEntryPoint(PlantUMLParser.EntryPointContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#usecaseDiagram}.
	 * @param ctx the parse tree
	 */
	void enterUsecaseDiagram(PlantUMLParser.UsecaseDiagramContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#usecaseDiagram}.
	 * @param ctx the parse tree
	 */
	void exitUsecaseDiagram(PlantUMLParser.UsecaseDiagramContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#usecaseDiagramContent}.
	 * @param ctx the parse tree
	 */
	void enterUsecaseDiagramContent(PlantUMLParser.UsecaseDiagramContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#usecaseDiagramContent}.
	 * @param ctx the parse tree
	 */
	void exitUsecaseDiagramContent(PlantUMLParser.UsecaseDiagramContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#usecaseEntityDef}.
	 * @param ctx the parse tree
	 */
	void enterUsecaseEntityDef(PlantUMLParser.UsecaseEntityDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#usecaseEntityDef}.
	 * @param ctx the parse tree
	 */
	void exitUsecaseEntityDef(PlantUMLParser.UsecaseEntityDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#usecaseActorDef}.
	 * @param ctx the parse tree
	 */
	void enterUsecaseActorDef(PlantUMLParser.UsecaseActorDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#usecaseActorDef}.
	 * @param ctx the parse tree
	 */
	void exitUsecaseActorDef(PlantUMLParser.UsecaseActorDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#usecaseCaseDef}.
	 * @param ctx the parse tree
	 */
	void enterUsecaseCaseDef(PlantUMLParser.UsecaseCaseDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#usecaseCaseDef}.
	 * @param ctx the parse tree
	 */
	void exitUsecaseCaseDef(PlantUMLParser.UsecaseCaseDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#usecaseRectangleDef}.
	 * @param ctx the parse tree
	 */
	void enterUsecaseRectangleDef(PlantUMLParser.UsecaseRectangleDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#usecaseRectangleDef}.
	 * @param ctx the parse tree
	 */
	void exitUsecaseRectangleDef(PlantUMLParser.UsecaseRectangleDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#usecaseArrow}.
	 * @param ctx the parse tree
	 */
	void enterUsecaseArrow(PlantUMLParser.UsecaseArrowContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#usecaseArrow}.
	 * @param ctx the parse tree
	 */
	void exitUsecaseArrow(PlantUMLParser.UsecaseArrowContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#usecaseArrowType}.
	 * @param ctx the parse tree
	 */
	void enterUsecaseArrowType(PlantUMLParser.UsecaseArrowTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#usecaseArrowType}.
	 * @param ctx the parse tree
	 */
	void exitUsecaseArrowType(PlantUMLParser.UsecaseArrowTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#usecaseArrowID}.
	 * @param ctx the parse tree
	 */
	void enterUsecaseArrowID(PlantUMLParser.UsecaseArrowIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#usecaseArrowID}.
	 * @param ctx the parse tree
	 */
	void exitUsecaseArrowID(PlantUMLParser.UsecaseArrowIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#usecaseEntityID}.
	 * @param ctx the parse tree
	 */
	void enterUsecaseEntityID(PlantUMLParser.UsecaseEntityIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#usecaseEntityID}.
	 * @param ctx the parse tree
	 */
	void exitUsecaseEntityID(PlantUMLParser.UsecaseEntityIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#usecaseActorID}.
	 * @param ctx the parse tree
	 */
	void enterUsecaseActorID(PlantUMLParser.UsecaseActorIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#usecaseActorID}.
	 * @param ctx the parse tree
	 */
	void exitUsecaseActorID(PlantUMLParser.UsecaseActorIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#usecaseCaseID}.
	 * @param ctx the parse tree
	 */
	void enterUsecaseCaseID(PlantUMLParser.UsecaseCaseIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#usecaseCaseID}.
	 * @param ctx the parse tree
	 */
	void exitUsecaseCaseID(PlantUMLParser.UsecaseCaseIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classDiagram}.
	 * @param ctx the parse tree
	 */
	void enterClassDiagram(PlantUMLParser.ClassDiagramContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classDiagram}.
	 * @param ctx the parse tree
	 */
	void exitClassDiagram(PlantUMLParser.ClassDiagramContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classDiagramContent}.
	 * @param ctx the parse tree
	 */
	void enterClassDiagramContent(PlantUMLParser.ClassDiagramContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classDiagramContent}.
	 * @param ctx the parse tree
	 */
	void exitClassDiagramContent(PlantUMLParser.ClassDiagramContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classPackageEntity}.
	 * @param ctx the parse tree
	 */
	void enterClassPackageEntity(PlantUMLParser.ClassPackageEntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classPackageEntity}.
	 * @param ctx the parse tree
	 */
	void exitClassPackageEntity(PlantUMLParser.ClassPackageEntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classPackageEntityType}.
	 * @param ctx the parse tree
	 */
	void enterClassPackageEntityType(PlantUMLParser.ClassPackageEntityTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classPackageEntityType}.
	 * @param ctx the parse tree
	 */
	void exitClassPackageEntityType(PlantUMLParser.ClassPackageEntityTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classPackageName}.
	 * @param ctx the parse tree
	 */
	void enterClassPackageName(PlantUMLParser.ClassPackageNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classPackageName}.
	 * @param ctx the parse tree
	 */
	void exitClassPackageName(PlantUMLParser.ClassPackageNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classEntity}.
	 * @param ctx the parse tree
	 */
	void enterClassEntity(PlantUMLParser.ClassEntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classEntity}.
	 * @param ctx the parse tree
	 */
	void exitClassEntity(PlantUMLParser.ClassEntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classEntityType}.
	 * @param ctx the parse tree
	 */
	void enterClassEntityType(PlantUMLParser.ClassEntityTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classEntityType}.
	 * @param ctx the parse tree
	 */
	void exitClassEntityType(PlantUMLParser.ClassEntityTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classInheritance}.
	 * @param ctx the parse tree
	 */
	void enterClassInheritance(PlantUMLParser.ClassInheritanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classInheritance}.
	 * @param ctx the parse tree
	 */
	void exitClassInheritance(PlantUMLParser.ClassInheritanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classInheritanceType}.
	 * @param ctx the parse tree
	 */
	void enterClassInheritanceType(PlantUMLParser.ClassInheritanceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classInheritanceType}.
	 * @param ctx the parse tree
	 */
	void exitClassInheritanceType(PlantUMLParser.ClassInheritanceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classExternalField}.
	 * @param ctx the parse tree
	 */
	void enterClassExternalField(PlantUMLParser.ClassExternalFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classExternalField}.
	 * @param ctx the parse tree
	 */
	void exitClassExternalField(PlantUMLParser.ClassExternalFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classField}.
	 * @param ctx the parse tree
	 */
	void enterClassField(PlantUMLParser.ClassFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classField}.
	 * @param ctx the parse tree
	 */
	void exitClassField(PlantUMLParser.ClassFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classMethod}.
	 * @param ctx the parse tree
	 */
	void enterClassMethod(PlantUMLParser.ClassMethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classMethod}.
	 * @param ctx the parse tree
	 */
	void exitClassMethod(PlantUMLParser.ClassMethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classAttribute}.
	 * @param ctx the parse tree
	 */
	void enterClassAttribute(PlantUMLParser.ClassAttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classAttribute}.
	 * @param ctx the parse tree
	 */
	void exitClassAttribute(PlantUMLParser.ClassAttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classVisibility}.
	 * @param ctx the parse tree
	 */
	void enterClassVisibility(PlantUMLParser.ClassVisibilityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classVisibility}.
	 * @param ctx the parse tree
	 */
	void exitClassVisibility(PlantUMLParser.ClassVisibilityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classFieldNoVisibilityContent}.
	 * @param ctx the parse tree
	 */
	void enterClassFieldNoVisibilityContent(PlantUMLParser.ClassFieldNoVisibilityContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classFieldNoVisibilityContent}.
	 * @param ctx the parse tree
	 */
	void exitClassFieldNoVisibilityContent(PlantUMLParser.ClassFieldNoVisibilityContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classFieldContent}.
	 * @param ctx the parse tree
	 */
	void enterClassFieldContent(PlantUMLParser.ClassFieldContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classFieldContent}.
	 * @param ctx the parse tree
	 */
	void exitClassFieldContent(PlantUMLParser.ClassFieldContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classRelation}.
	 * @param ctx the parse tree
	 */
	void enterClassRelation(PlantUMLParser.ClassRelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classRelation}.
	 * @param ctx the parse tree
	 */
	void exitClassRelation(PlantUMLParser.ClassRelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classAssociationClass}.
	 * @param ctx the parse tree
	 */
	void enterClassAssociationClass(PlantUMLParser.ClassAssociationClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classAssociationClass}.
	 * @param ctx the parse tree
	 */
	void exitClassAssociationClass(PlantUMLParser.ClassAssociationClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classRelationClass}.
	 * @param ctx the parse tree
	 */
	void enterClassRelationClass(PlantUMLParser.ClassRelationClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classRelationClass}.
	 * @param ctx the parse tree
	 */
	void exitClassRelationClass(PlantUMLParser.ClassRelationClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classRelationType}.
	 * @param ctx the parse tree
	 */
	void enterClassRelationType(PlantUMLParser.ClassRelationTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classRelationType}.
	 * @param ctx the parse tree
	 */
	void exitClassRelationType(PlantUMLParser.ClassRelationTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classLollipopHead}.
	 * @param ctx the parse tree
	 */
	void enterClassLollipopHead(PlantUMLParser.ClassLollipopHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classLollipopHead}.
	 * @param ctx the parse tree
	 */
	void exitClassLollipopHead(PlantUMLParser.ClassLollipopHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classLollipopTail}.
	 * @param ctx the parse tree
	 */
	void enterClassLollipopTail(PlantUMLParser.ClassLollipopTailContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classLollipopTail}.
	 * @param ctx the parse tree
	 */
	void exitClassLollipopTail(PlantUMLParser.ClassLollipopTailContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classEntityID}.
	 * @param ctx the parse tree
	 */
	void enterClassEntityID(PlantUMLParser.ClassEntityIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classEntityID}.
	 * @param ctx the parse tree
	 */
	void exitClassEntityID(PlantUMLParser.ClassEntityIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classEntityNameID}.
	 * @param ctx the parse tree
	 */
	void enterClassEntityNameID(PlantUMLParser.ClassEntityNameIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classEntityNameID}.
	 * @param ctx the parse tree
	 */
	void exitClassEntityNameID(PlantUMLParser.ClassEntityNameIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#classPackageID}.
	 * @param ctx the parse tree
	 */
	void enterClassPackageID(PlantUMLParser.ClassPackageIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#classPackageID}.
	 * @param ctx the parse tree
	 */
	void exitClassPackageID(PlantUMLParser.ClassPackageIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#commonContent}.
	 * @param ctx the parse tree
	 */
	void enterCommonContent(PlantUMLParser.CommonContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#commonContent}.
	 * @param ctx the parse tree
	 */
	void exitCommonContent(PlantUMLParser.CommonContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#title}.
	 * @param ctx the parse tree
	 */
	void enterTitle(PlantUMLParser.TitleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#title}.
	 * @param ctx the parse tree
	 */
	void exitTitle(PlantUMLParser.TitleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#note}.
	 * @param ctx the parse tree
	 */
	void enterNote(PlantUMLParser.NoteContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#note}.
	 * @param ctx the parse tree
	 */
	void exitNote(PlantUMLParser.NoteContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#noteEntityID}.
	 * @param ctx the parse tree
	 */
	void enterNoteEntityID(PlantUMLParser.NoteEntityIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#noteEntityID}.
	 * @param ctx the parse tree
	 */
	void exitNoteEntityID(PlantUMLParser.NoteEntityIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#noteDirection}.
	 * @param ctx the parse tree
	 */
	void enterNoteDirection(PlantUMLParser.NoteDirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#noteDirection}.
	 * @param ctx the parse tree
	 */
	void exitNoteDirection(PlantUMLParser.NoteDirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#noteContent}.
	 * @param ctx the parse tree
	 */
	void enterNoteContent(PlantUMLParser.NoteContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#noteContent}.
	 * @param ctx the parse tree
	 */
	void exitNoteContent(PlantUMLParser.NoteContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#noteWord}.
	 * @param ctx the parse tree
	 */
	void enterNoteWord(PlantUMLParser.NoteWordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#noteWord}.
	 * @param ctx the parse tree
	 */
	void exitNoteWord(PlantUMLParser.NoteWordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#endNote}.
	 * @param ctx the parse tree
	 */
	void enterEndNote(PlantUMLParser.EndNoteContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#endNote}.
	 * @param ctx the parse tree
	 */
	void exitEndNote(PlantUMLParser.EndNoteContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#legend}.
	 * @param ctx the parse tree
	 */
	void enterLegend(PlantUMLParser.LegendContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#legend}.
	 * @param ctx the parse tree
	 */
	void exitLegend(PlantUMLParser.LegendContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#legendContent}.
	 * @param ctx the parse tree
	 */
	void enterLegendContent(PlantUMLParser.LegendContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#legendContent}.
	 * @param ctx the parse tree
	 */
	void exitLegendContent(PlantUMLParser.LegendContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#legendWord}.
	 * @param ctx the parse tree
	 */
	void enterLegendWord(PlantUMLParser.LegendWordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#legendWord}.
	 * @param ctx the parse tree
	 */
	void exitLegendWord(PlantUMLParser.LegendWordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#endLegend}.
	 * @param ctx the parse tree
	 */
	void enterEndLegend(PlantUMLParser.EndLegendContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#endLegend}.
	 * @param ctx the parse tree
	 */
	void exitEndLegend(PlantUMLParser.EndLegendContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#comment}.
	 * @param ctx the parse tree
	 */
	void enterComment(PlantUMLParser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#comment}.
	 * @param ctx the parse tree
	 */
	void exitComment(PlantUMLParser.CommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#generic}.
	 * @param ctx the parse tree
	 */
	void enterGeneric(PlantUMLParser.GenericContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#generic}.
	 * @param ctx the parse tree
	 */
	void exitGeneric(PlantUMLParser.GenericContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#genericContent}.
	 * @param ctx the parse tree
	 */
	void enterGenericContent(PlantUMLParser.GenericContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#genericContent}.
	 * @param ctx the parse tree
	 */
	void exitGenericContent(PlantUMLParser.GenericContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#genericPart}.
	 * @param ctx the parse tree
	 */
	void enterGenericPart(PlantUMLParser.GenericPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#genericPart}.
	 * @param ctx the parse tree
	 */
	void exitGenericPart(PlantUMLParser.GenericPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#stereotype}.
	 * @param ctx the parse tree
	 */
	void enterStereotype(PlantUMLParser.StereotypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#stereotype}.
	 * @param ctx the parse tree
	 */
	void exitStereotype(PlantUMLParser.StereotypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#stereotypeContent}.
	 * @param ctx the parse tree
	 */
	void enterStereotypeContent(PlantUMLParser.StereotypeContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#stereotypeContent}.
	 * @param ctx the parse tree
	 */
	void exitStereotypeContent(PlantUMLParser.StereotypeContentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrowFullHead}
	 * labeled alternative in {@link PlantUMLParser#arrowHead}.
	 * @param ctx the parse tree
	 */
	void enterArrowFullHead(PlantUMLParser.ArrowFullHeadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrowFullHead}
	 * labeled alternative in {@link PlantUMLParser#arrowHead}.
	 * @param ctx the parse tree
	 */
	void exitArrowFullHead(PlantUMLParser.ArrowFullHeadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrowBasicHead}
	 * labeled alternative in {@link PlantUMLParser#arrowHead}.
	 * @param ctx the parse tree
	 */
	void enterArrowBasicHead(PlantUMLParser.ArrowBasicHeadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrowBasicHead}
	 * labeled alternative in {@link PlantUMLParser#arrowHead}.
	 * @param ctx the parse tree
	 */
	void exitArrowBasicHead(PlantUMLParser.ArrowBasicHeadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrowCircleHead}
	 * labeled alternative in {@link PlantUMLParser#arrowHead}.
	 * @param ctx the parse tree
	 */
	void enterArrowCircleHead(PlantUMLParser.ArrowCircleHeadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrowCircleHead}
	 * labeled alternative in {@link PlantUMLParser#arrowHead}.
	 * @param ctx the parse tree
	 */
	void exitArrowCircleHead(PlantUMLParser.ArrowCircleHeadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrowStarHead}
	 * labeled alternative in {@link PlantUMLParser#arrowHead}.
	 * @param ctx the parse tree
	 */
	void enterArrowStarHead(PlantUMLParser.ArrowStarHeadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrowStarHead}
	 * labeled alternative in {@link PlantUMLParser#arrowHead}.
	 * @param ctx the parse tree
	 */
	void exitArrowStarHead(PlantUMLParser.ArrowStarHeadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrowPlusHead}
	 * labeled alternative in {@link PlantUMLParser#arrowHead}.
	 * @param ctx the parse tree
	 */
	void enterArrowPlusHead(PlantUMLParser.ArrowPlusHeadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrowPlusHead}
	 * labeled alternative in {@link PlantUMLParser#arrowHead}.
	 * @param ctx the parse tree
	 */
	void exitArrowPlusHead(PlantUMLParser.ArrowPlusHeadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrowDotBody}
	 * labeled alternative in {@link PlantUMLParser#arrowBody}.
	 * @param ctx the parse tree
	 */
	void enterArrowDotBody(PlantUMLParser.ArrowDotBodyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrowDotBody}
	 * labeled alternative in {@link PlantUMLParser#arrowBody}.
	 * @param ctx the parse tree
	 */
	void exitArrowDotBody(PlantUMLParser.ArrowDotBodyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrowDashBody}
	 * labeled alternative in {@link PlantUMLParser#arrowBody}.
	 * @param ctx the parse tree
	 */
	void enterArrowDashBody(PlantUMLParser.ArrowDashBodyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrowDashBody}
	 * labeled alternative in {@link PlantUMLParser#arrowBody}.
	 * @param ctx the parse tree
	 */
	void exitArrowDashBody(PlantUMLParser.ArrowDashBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#arrowDirection}.
	 * @param ctx the parse tree
	 */
	void enterArrowDirection(PlantUMLParser.ArrowDirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#arrowDirection}.
	 * @param ctx the parse tree
	 */
	void exitArrowDirection(PlantUMLParser.ArrowDirectionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrowFullTail}
	 * labeled alternative in {@link PlantUMLParser#arrowTail}.
	 * @param ctx the parse tree
	 */
	void enterArrowFullTail(PlantUMLParser.ArrowFullTailContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrowFullTail}
	 * labeled alternative in {@link PlantUMLParser#arrowTail}.
	 * @param ctx the parse tree
	 */
	void exitArrowFullTail(PlantUMLParser.ArrowFullTailContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrowBasicTail}
	 * labeled alternative in {@link PlantUMLParser#arrowTail}.
	 * @param ctx the parse tree
	 */
	void enterArrowBasicTail(PlantUMLParser.ArrowBasicTailContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrowBasicTail}
	 * labeled alternative in {@link PlantUMLParser#arrowTail}.
	 * @param ctx the parse tree
	 */
	void exitArrowBasicTail(PlantUMLParser.ArrowBasicTailContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrowCircleTail}
	 * labeled alternative in {@link PlantUMLParser#arrowTail}.
	 * @param ctx the parse tree
	 */
	void enterArrowCircleTail(PlantUMLParser.ArrowCircleTailContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrowCircleTail}
	 * labeled alternative in {@link PlantUMLParser#arrowTail}.
	 * @param ctx the parse tree
	 */
	void exitArrowCircleTail(PlantUMLParser.ArrowCircleTailContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrowStarTail}
	 * labeled alternative in {@link PlantUMLParser#arrowTail}.
	 * @param ctx the parse tree
	 */
	void enterArrowStarTail(PlantUMLParser.ArrowStarTailContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrowStarTail}
	 * labeled alternative in {@link PlantUMLParser#arrowTail}.
	 * @param ctx the parse tree
	 */
	void exitArrowStarTail(PlantUMLParser.ArrowStarTailContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrowPlusTail}
	 * labeled alternative in {@link PlantUMLParser#arrowTail}.
	 * @param ctx the parse tree
	 */
	void enterArrowPlusTail(PlantUMLParser.ArrowPlusTailContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrowPlusTail}
	 * labeled alternative in {@link PlantUMLParser#arrowTail}.
	 * @param ctx the parse tree
	 */
	void exitArrowPlusTail(PlantUMLParser.ArrowPlusTailContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#escapedString}.
	 * @param ctx the parse tree
	 */
	void enterEscapedString(PlantUMLParser.EscapedStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#escapedString}.
	 * @param ctx the parse tree
	 */
	void exitEscapedString(PlantUMLParser.EscapedStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#escapedStringContent}.
	 * @param ctx the parse tree
	 */
	void enterEscapedStringContent(PlantUMLParser.EscapedStringContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#escapedStringContent}.
	 * @param ctx the parse tree
	 */
	void exitEscapedStringContent(PlantUMLParser.EscapedStringContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#escapedKeywords}.
	 * @param ctx the parse tree
	 */
	void enterEscapedKeywords(PlantUMLParser.EscapedKeywordsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#escapedKeywords}.
	 * @param ctx the parse tree
	 */
	void exitEscapedKeywords(PlantUMLParser.EscapedKeywordsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#escapedNoteKeyword}.
	 * @param ctx the parse tree
	 */
	void enterEscapedNoteKeyword(PlantUMLParser.EscapedNoteKeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#escapedNoteKeyword}.
	 * @param ctx the parse tree
	 */
	void exitEscapedNoteKeyword(PlantUMLParser.EscapedNoteKeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#escapedLegendKeyword}.
	 * @param ctx the parse tree
	 */
	void enterEscapedLegendKeyword(PlantUMLParser.EscapedLegendKeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#escapedLegendKeyword}.
	 * @param ctx the parse tree
	 */
	void exitEscapedLegendKeyword(PlantUMLParser.EscapedLegendKeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#escapedCommonKeyword}.
	 * @param ctx the parse tree
	 */
	void enterEscapedCommonKeyword(PlantUMLParser.EscapedCommonKeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#escapedCommonKeyword}.
	 * @param ctx the parse tree
	 */
	void exitEscapedCommonKeyword(PlantUMLParser.EscapedCommonKeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#color}.
	 * @param ctx the parse tree
	 */
	void enterColor(PlantUMLParser.ColorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#color}.
	 * @param ctx the parse tree
	 */
	void exitColor(PlantUMLParser.ColorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#endLine}.
	 * @param ctx the parse tree
	 */
	void enterEndLine(PlantUMLParser.EndLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#endLine}.
	 * @param ctx the parse tree
	 */
	void exitEndLine(PlantUMLParser.EndLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#endLineNoDoubleDots}.
	 * @param ctx the parse tree
	 */
	void enterEndLineNoDoubleDots(PlantUMLParser.EndLineNoDoubleDotsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#endLineNoDoubleDots}.
	 * @param ctx the parse tree
	 */
	void exitEndLineNoDoubleDots(PlantUMLParser.EndLineNoDoubleDotsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUMLParser#ignoredCommand}.
	 * @param ctx the parse tree
	 */
	void enterIgnoredCommand(PlantUMLParser.IgnoredCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUMLParser#ignoredCommand}.
	 * @param ctx the parse tree
	 */
	void exitIgnoredCommand(PlantUMLParser.IgnoredCommandContext ctx);
}