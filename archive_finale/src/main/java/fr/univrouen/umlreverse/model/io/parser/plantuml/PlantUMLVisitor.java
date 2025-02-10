// Generated from /home/tekiflo/dev/uml/projet/src/main/antlr4/PlantUML.g4 by ANTLR 4.5.1
package fr.univrouen.umlreverse.model.io.parser.plantuml;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PlantUMLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PlantUMLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#entryPoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntryPoint(PlantUMLParser.EntryPointContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#usecaseDiagram}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsecaseDiagram(PlantUMLParser.UsecaseDiagramContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#usecaseDiagramContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsecaseDiagramContent(PlantUMLParser.UsecaseDiagramContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#usecaseEntityDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsecaseEntityDef(PlantUMLParser.UsecaseEntityDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#usecaseActorDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsecaseActorDef(PlantUMLParser.UsecaseActorDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#usecaseCaseDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsecaseCaseDef(PlantUMLParser.UsecaseCaseDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#usecaseRectangleDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsecaseRectangleDef(PlantUMLParser.UsecaseRectangleDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#usecaseArrow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsecaseArrow(PlantUMLParser.UsecaseArrowContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#usecaseArrowType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsecaseArrowType(PlantUMLParser.UsecaseArrowTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#usecaseArrowID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsecaseArrowID(PlantUMLParser.UsecaseArrowIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#usecaseEntityID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsecaseEntityID(PlantUMLParser.UsecaseEntityIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#usecaseActorID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsecaseActorID(PlantUMLParser.UsecaseActorIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#usecaseCaseID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsecaseCaseID(PlantUMLParser.UsecaseCaseIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classDiagram}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDiagram(PlantUMLParser.ClassDiagramContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classDiagramContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDiagramContent(PlantUMLParser.ClassDiagramContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classPackageEntity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassPackageEntity(PlantUMLParser.ClassPackageEntityContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classPackageEntityType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassPackageEntityType(PlantUMLParser.ClassPackageEntityTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classPackageName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassPackageName(PlantUMLParser.ClassPackageNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classEntity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassEntity(PlantUMLParser.ClassEntityContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classEntityType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassEntityType(PlantUMLParser.ClassEntityTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classInheritance}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassInheritance(PlantUMLParser.ClassInheritanceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classInheritanceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassInheritanceType(PlantUMLParser.ClassInheritanceTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classExternalField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassExternalField(PlantUMLParser.ClassExternalFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassField(PlantUMLParser.ClassFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classMethod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassMethod(PlantUMLParser.ClassMethodContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassAttribute(PlantUMLParser.ClassAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classVisibility}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassVisibility(PlantUMLParser.ClassVisibilityContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classFieldNoVisibilityContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassFieldNoVisibilityContent(PlantUMLParser.ClassFieldNoVisibilityContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classFieldContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassFieldContent(PlantUMLParser.ClassFieldContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classRelation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassRelation(PlantUMLParser.ClassRelationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classAssociationClass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassAssociationClass(PlantUMLParser.ClassAssociationClassContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classRelationClass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassRelationClass(PlantUMLParser.ClassRelationClassContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classRelationType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassRelationType(PlantUMLParser.ClassRelationTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classLollipopHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassLollipopHead(PlantUMLParser.ClassLollipopHeadContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classLollipopTail}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassLollipopTail(PlantUMLParser.ClassLollipopTailContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classEntityID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassEntityID(PlantUMLParser.ClassEntityIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classEntityNameID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassEntityNameID(PlantUMLParser.ClassEntityNameIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#classPackageID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassPackageID(PlantUMLParser.ClassPackageIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#commonContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommonContent(PlantUMLParser.CommonContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#title}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTitle(PlantUMLParser.TitleContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#note}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNote(PlantUMLParser.NoteContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#noteEntityID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoteEntityID(PlantUMLParser.NoteEntityIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#noteDirection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoteDirection(PlantUMLParser.NoteDirectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#noteContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoteContent(PlantUMLParser.NoteContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#noteWord}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoteWord(PlantUMLParser.NoteWordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#endNote}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndNote(PlantUMLParser.EndNoteContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#legend}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLegend(PlantUMLParser.LegendContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#legendContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLegendContent(PlantUMLParser.LegendContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#legendWord}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLegendWord(PlantUMLParser.LegendWordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#endLegend}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndLegend(PlantUMLParser.EndLegendContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment(PlantUMLParser.CommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#generic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneric(PlantUMLParser.GenericContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#genericContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericContent(PlantUMLParser.GenericContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#genericPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericPart(PlantUMLParser.GenericPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#stereotype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStereotype(PlantUMLParser.StereotypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#stereotypeContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStereotypeContent(PlantUMLParser.StereotypeContentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrowFullHead}
	 * labeled alternative in {@link PlantUMLParser#arrowHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowFullHead(PlantUMLParser.ArrowFullHeadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrowBasicHead}
	 * labeled alternative in {@link PlantUMLParser#arrowHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowBasicHead(PlantUMLParser.ArrowBasicHeadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrowCircleHead}
	 * labeled alternative in {@link PlantUMLParser#arrowHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowCircleHead(PlantUMLParser.ArrowCircleHeadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrowStarHead}
	 * labeled alternative in {@link PlantUMLParser#arrowHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowStarHead(PlantUMLParser.ArrowStarHeadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrowPlusHead}
	 * labeled alternative in {@link PlantUMLParser#arrowHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowPlusHead(PlantUMLParser.ArrowPlusHeadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrowDotBody}
	 * labeled alternative in {@link PlantUMLParser#arrowBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowDotBody(PlantUMLParser.ArrowDotBodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrowDashBody}
	 * labeled alternative in {@link PlantUMLParser#arrowBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowDashBody(PlantUMLParser.ArrowDashBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#arrowDirection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowDirection(PlantUMLParser.ArrowDirectionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrowFullTail}
	 * labeled alternative in {@link PlantUMLParser#arrowTail}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowFullTail(PlantUMLParser.ArrowFullTailContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrowBasicTail}
	 * labeled alternative in {@link PlantUMLParser#arrowTail}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowBasicTail(PlantUMLParser.ArrowBasicTailContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrowCircleTail}
	 * labeled alternative in {@link PlantUMLParser#arrowTail}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowCircleTail(PlantUMLParser.ArrowCircleTailContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrowStarTail}
	 * labeled alternative in {@link PlantUMLParser#arrowTail}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowStarTail(PlantUMLParser.ArrowStarTailContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrowPlusTail}
	 * labeled alternative in {@link PlantUMLParser#arrowTail}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowPlusTail(PlantUMLParser.ArrowPlusTailContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#escapedString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEscapedString(PlantUMLParser.EscapedStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#escapedStringContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEscapedStringContent(PlantUMLParser.EscapedStringContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#escapedKeywords}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEscapedKeywords(PlantUMLParser.EscapedKeywordsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#escapedNoteKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEscapedNoteKeyword(PlantUMLParser.EscapedNoteKeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#escapedLegendKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEscapedLegendKeyword(PlantUMLParser.EscapedLegendKeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#escapedCommonKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEscapedCommonKeyword(PlantUMLParser.EscapedCommonKeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#color}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColor(PlantUMLParser.ColorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#endLine}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndLine(PlantUMLParser.EndLineContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#endLineNoDoubleDots}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndLineNoDoubleDots(PlantUMLParser.EndLineNoDoubleDotsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PlantUMLParser#ignoredCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIgnoredCommand(PlantUMLParser.IgnoredCommandContext ctx);
}