// Generated from C:\Users\u005Cuser\Desktop\proyecto_DB\Proyecto_SQL\CompiladorSQL\src\compiladorsql\Gramatica.g4 by ANTLR 4.4
package compiladorsql;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GramaticaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GramaticaVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#asignacion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsignacion(@NotNull GramaticaParser.AsignacionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#references}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferences(@NotNull GramaticaParser.ReferencesContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#andExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExp(@NotNull GramaticaParser.AndExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tipoFloat}
	 * labeled alternative in {@link GramaticaParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipoFloat(@NotNull GramaticaParser.TipoFloatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterarDB}
	 * labeled alternative in {@link GramaticaParser#dbOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterarDB(@NotNull GramaticaParser.AlterarDBContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#entero}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntero(@NotNull GramaticaParser.EnteroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code crearDB}
	 * labeled alternative in {@link GramaticaParser#dbOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCrearDB(@NotNull GramaticaParser.CrearDBContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#nullo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullo(@NotNull GramaticaParser.NulloContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCond(@NotNull GramaticaParser.CondContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#constraints}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraints(@NotNull GramaticaParser.ConstraintsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#relExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelExp(@NotNull GramaticaParser.RelExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#nUM_MONTH}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNUM_MONTH(@NotNull GramaticaParser.NUM_MONTHContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#eqExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExp(@NotNull GramaticaParser.EqExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#character}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharacter(@NotNull GramaticaParser.CharacterContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#exp5}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp5(@NotNull GramaticaParser.Exp5Context ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#exp4}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp4(@NotNull GramaticaParser.Exp4Context ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#exp3}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp3(@NotNull GramaticaParser.Exp3Context ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#exp2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp2(@NotNull GramaticaParser.Exp2Context ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#exp1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp1(@NotNull GramaticaParser.Exp1Context ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp(@NotNull GramaticaParser.ExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#unarianFactorExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnarianFactorExp(@NotNull GramaticaParser.UnarianFactorExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintPK}
	 * labeled alternative in {@link GramaticaParser#constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintPK(@NotNull GramaticaParser.ConstraintPKContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#nUM_DAY}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNUM_DAY(@NotNull GramaticaParser.NUM_DAYContext ctx);
	/**
	 * Visit a parse tree produced by the {@code usarDB}
	 * labeled alternative in {@link GramaticaParser#dbOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsarDB(@NotNull GramaticaParser.UsarDBContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#glExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlExp(@NotNull GramaticaParser.GlExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mostrarDB}
	 * labeled alternative in {@link GramaticaParser#dbOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMostrarDB(@NotNull GramaticaParser.MostrarDBContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(@NotNull GramaticaParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#nUM_YEAR}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNUM_YEAR(@NotNull GramaticaParser.NUM_YEARContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropConstraintTB}
	 * labeled alternative in {@link GramaticaParser#tableAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropConstraintTB(@NotNull GramaticaParser.DropConstraintTBContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mostrarColumnasTB}
	 * labeled alternative in {@link GramaticaParser#tOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMostrarColumnasTB(@NotNull GramaticaParser.MostrarColumnasTBContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#sqlProgram}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlProgram(@NotNull GramaticaParser.SqlProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#fecha}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFecha(@NotNull GramaticaParser.FechaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tipoDAte}
	 * labeled alternative in {@link GramaticaParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipoDAte(@NotNull GramaticaParser.TipoDAteContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addConstraintTB}
	 * labeled alternative in {@link GramaticaParser#tableAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddConstraintTB(@NotNull GramaticaParser.AddConstraintTBContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#decimal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimal(@NotNull GramaticaParser.DecimalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addColumnTB}
	 * labeled alternative in {@link GramaticaParser#tableAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddColumnTB(@NotNull GramaticaParser.AddColumnTBContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#insert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert(@NotNull GramaticaParser.InsertContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#update}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate(@NotNull GramaticaParser.UpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#orderBy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderBy(@NotNull GramaticaParser.OrderByContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#notExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExp(@NotNull GramaticaParser.NotExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#delete}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete(@NotNull GramaticaParser.DeleteContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(@NotNull GramaticaParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#columna}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumna(@NotNull GramaticaParser.ColumnaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintCheck}
	 * labeled alternative in {@link GramaticaParser#constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintCheck(@NotNull GramaticaParser.ConstraintCheckContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(@NotNull GramaticaParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tipoEntero}
	 * labeled alternative in {@link GramaticaParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipoEntero(@NotNull GramaticaParser.TipoEnteroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintFK}
	 * labeled alternative in {@link GramaticaParser#constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintFK(@NotNull GramaticaParser.ConstraintFKContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(@NotNull GramaticaParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(@NotNull GramaticaParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#dOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDOperation(@NotNull GramaticaParser.DOperationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(@NotNull GramaticaParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dropColumnTB}
	 * labeled alternative in {@link GramaticaParser#tableAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropColumnTB(@NotNull GramaticaParser.DropColumnTBContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mostrarTablasTB}
	 * labeled alternative in {@link GramaticaParser#tOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMostrarTablasTB(@NotNull GramaticaParser.MostrarTablasTBContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn(@NotNull GramaticaParser.ColumnContext ctx);
	/**
	 * Visit a parse tree produced by {@link GramaticaParser#orExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExp(@NotNull GramaticaParser.OrExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code eliminarTB}
	 * labeled alternative in {@link GramaticaParser#tOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEliminarTB(@NotNull GramaticaParser.EliminarTBContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameTB}
	 * labeled alternative in {@link GramaticaParser#tableAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameTB(@NotNull GramaticaParser.RenameTBContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterarTB}
	 * labeled alternative in {@link GramaticaParser#tOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterarTB(@NotNull GramaticaParser.AlterarTBContext ctx);
	/**
	 * Visit a parse tree produced by the {@code crearTB}
	 * labeled alternative in {@link GramaticaParser#tOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCrearTB(@NotNull GramaticaParser.CrearTBContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tipoChar}
	 * labeled alternative in {@link GramaticaParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipoChar(@NotNull GramaticaParser.TipoCharContext ctx);
	/**
	 * Visit a parse tree produced by the {@code eliminarDB}
	 * labeled alternative in {@link GramaticaParser#dbOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEliminarDB(@NotNull GramaticaParser.EliminarDBContext ctx);
}