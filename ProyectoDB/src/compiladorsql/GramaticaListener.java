// Generated from C:\Users\u005Cuser\Desktop\proyecto_DB\Proyecto_SQL\CompiladorSQL\src\compiladorsql\Gramatica.g4 by ANTLR 4.4
package compiladorsql;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link GramaticaParser}.
 */
public interface GramaticaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#asignacion}.
	 * @param ctx the parse tree
	 */
	void enterAsignacion(@NotNull GramaticaParser.AsignacionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#asignacion}.
	 * @param ctx the parse tree
	 */
	void exitAsignacion(@NotNull GramaticaParser.AsignacionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#references}.
	 * @param ctx the parse tree
	 */
	void enterReferences(@NotNull GramaticaParser.ReferencesContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#references}.
	 * @param ctx the parse tree
	 */
	void exitReferences(@NotNull GramaticaParser.ReferencesContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#andExp}.
	 * @param ctx the parse tree
	 */
	void enterAndExp(@NotNull GramaticaParser.AndExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#andExp}.
	 * @param ctx the parse tree
	 */
	void exitAndExp(@NotNull GramaticaParser.AndExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tipoFloat}
	 * labeled alternative in {@link GramaticaParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTipoFloat(@NotNull GramaticaParser.TipoFloatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tipoFloat}
	 * labeled alternative in {@link GramaticaParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTipoFloat(@NotNull GramaticaParser.TipoFloatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterarDB}
	 * labeled alternative in {@link GramaticaParser#dbOperation}.
	 * @param ctx the parse tree
	 */
	void enterAlterarDB(@NotNull GramaticaParser.AlterarDBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterarDB}
	 * labeled alternative in {@link GramaticaParser#dbOperation}.
	 * @param ctx the parse tree
	 */
	void exitAlterarDB(@NotNull GramaticaParser.AlterarDBContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#entero}.
	 * @param ctx the parse tree
	 */
	void enterEntero(@NotNull GramaticaParser.EnteroContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#entero}.
	 * @param ctx the parse tree
	 */
	void exitEntero(@NotNull GramaticaParser.EnteroContext ctx);
	/**
	 * Enter a parse tree produced by the {@code crearDB}
	 * labeled alternative in {@link GramaticaParser#dbOperation}.
	 * @param ctx the parse tree
	 */
	void enterCrearDB(@NotNull GramaticaParser.CrearDBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code crearDB}
	 * labeled alternative in {@link GramaticaParser#dbOperation}.
	 * @param ctx the parse tree
	 */
	void exitCrearDB(@NotNull GramaticaParser.CrearDBContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#nullo}.
	 * @param ctx the parse tree
	 */
	void enterNullo(@NotNull GramaticaParser.NulloContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#nullo}.
	 * @param ctx the parse tree
	 */
	void exitNullo(@NotNull GramaticaParser.NulloContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCond(@NotNull GramaticaParser.CondContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCond(@NotNull GramaticaParser.CondContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#constraints}.
	 * @param ctx the parse tree
	 */
	void enterConstraints(@NotNull GramaticaParser.ConstraintsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#constraints}.
	 * @param ctx the parse tree
	 */
	void exitConstraints(@NotNull GramaticaParser.ConstraintsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#relExp}.
	 * @param ctx the parse tree
	 */
	void enterRelExp(@NotNull GramaticaParser.RelExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#relExp}.
	 * @param ctx the parse tree
	 */
	void exitRelExp(@NotNull GramaticaParser.RelExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#nUM_MONTH}.
	 * @param ctx the parse tree
	 */
	void enterNUM_MONTH(@NotNull GramaticaParser.NUM_MONTHContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#nUM_MONTH}.
	 * @param ctx the parse tree
	 */
	void exitNUM_MONTH(@NotNull GramaticaParser.NUM_MONTHContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#eqExp}.
	 * @param ctx the parse tree
	 */
	void enterEqExp(@NotNull GramaticaParser.EqExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#eqExp}.
	 * @param ctx the parse tree
	 */
	void exitEqExp(@NotNull GramaticaParser.EqExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#character}.
	 * @param ctx the parse tree
	 */
	void enterCharacter(@NotNull GramaticaParser.CharacterContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#character}.
	 * @param ctx the parse tree
	 */
	void exitCharacter(@NotNull GramaticaParser.CharacterContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#exp5}.
	 * @param ctx the parse tree
	 */
	void enterExp5(@NotNull GramaticaParser.Exp5Context ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#exp5}.
	 * @param ctx the parse tree
	 */
	void exitExp5(@NotNull GramaticaParser.Exp5Context ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#exp4}.
	 * @param ctx the parse tree
	 */
	void enterExp4(@NotNull GramaticaParser.Exp4Context ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#exp4}.
	 * @param ctx the parse tree
	 */
	void exitExp4(@NotNull GramaticaParser.Exp4Context ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#exp3}.
	 * @param ctx the parse tree
	 */
	void enterExp3(@NotNull GramaticaParser.Exp3Context ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#exp3}.
	 * @param ctx the parse tree
	 */
	void exitExp3(@NotNull GramaticaParser.Exp3Context ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#exp2}.
	 * @param ctx the parse tree
	 */
	void enterExp2(@NotNull GramaticaParser.Exp2Context ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#exp2}.
	 * @param ctx the parse tree
	 */
	void exitExp2(@NotNull GramaticaParser.Exp2Context ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#exp1}.
	 * @param ctx the parse tree
	 */
	void enterExp1(@NotNull GramaticaParser.Exp1Context ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#exp1}.
	 * @param ctx the parse tree
	 */
	void exitExp1(@NotNull GramaticaParser.Exp1Context ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExp(@NotNull GramaticaParser.ExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExp(@NotNull GramaticaParser.ExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#unarianFactorExp}.
	 * @param ctx the parse tree
	 */
	void enterUnarianFactorExp(@NotNull GramaticaParser.UnarianFactorExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#unarianFactorExp}.
	 * @param ctx the parse tree
	 */
	void exitUnarianFactorExp(@NotNull GramaticaParser.UnarianFactorExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintPK}
	 * labeled alternative in {@link GramaticaParser#constraint}.
	 * @param ctx the parse tree
	 */
	void enterConstraintPK(@NotNull GramaticaParser.ConstraintPKContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintPK}
	 * labeled alternative in {@link GramaticaParser#constraint}.
	 * @param ctx the parse tree
	 */
	void exitConstraintPK(@NotNull GramaticaParser.ConstraintPKContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#nUM_DAY}.
	 * @param ctx the parse tree
	 */
	void enterNUM_DAY(@NotNull GramaticaParser.NUM_DAYContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#nUM_DAY}.
	 * @param ctx the parse tree
	 */
	void exitNUM_DAY(@NotNull GramaticaParser.NUM_DAYContext ctx);
	/**
	 * Enter a parse tree produced by the {@code usarDB}
	 * labeled alternative in {@link GramaticaParser#dbOperation}.
	 * @param ctx the parse tree
	 */
	void enterUsarDB(@NotNull GramaticaParser.UsarDBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code usarDB}
	 * labeled alternative in {@link GramaticaParser#dbOperation}.
	 * @param ctx the parse tree
	 */
	void exitUsarDB(@NotNull GramaticaParser.UsarDBContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#glExp}.
	 * @param ctx the parse tree
	 */
	void enterGlExp(@NotNull GramaticaParser.GlExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#glExp}.
	 * @param ctx the parse tree
	 */
	void exitGlExp(@NotNull GramaticaParser.GlExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mostrarDB}
	 * labeled alternative in {@link GramaticaParser#dbOperation}.
	 * @param ctx the parse tree
	 */
	void enterMostrarDB(@NotNull GramaticaParser.MostrarDBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mostrarDB}
	 * labeled alternative in {@link GramaticaParser#dbOperation}.
	 * @param ctx the parse tree
	 */
	void exitMostrarDB(@NotNull GramaticaParser.MostrarDBContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(@NotNull GramaticaParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(@NotNull GramaticaParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#nUM_YEAR}.
	 * @param ctx the parse tree
	 */
	void enterNUM_YEAR(@NotNull GramaticaParser.NUM_YEARContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#nUM_YEAR}.
	 * @param ctx the parse tree
	 */
	void exitNUM_YEAR(@NotNull GramaticaParser.NUM_YEARContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropConstraintTB}
	 * labeled alternative in {@link GramaticaParser#tableAction}.
	 * @param ctx the parse tree
	 */
	void enterDropConstraintTB(@NotNull GramaticaParser.DropConstraintTBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropConstraintTB}
	 * labeled alternative in {@link GramaticaParser#tableAction}.
	 * @param ctx the parse tree
	 */
	void exitDropConstraintTB(@NotNull GramaticaParser.DropConstraintTBContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mostrarColumnasTB}
	 * labeled alternative in {@link GramaticaParser#tOperation}.
	 * @param ctx the parse tree
	 */
	void enterMostrarColumnasTB(@NotNull GramaticaParser.MostrarColumnasTBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mostrarColumnasTB}
	 * labeled alternative in {@link GramaticaParser#tOperation}.
	 * @param ctx the parse tree
	 */
	void exitMostrarColumnasTB(@NotNull GramaticaParser.MostrarColumnasTBContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#sqlProgram}.
	 * @param ctx the parse tree
	 */
	void enterSqlProgram(@NotNull GramaticaParser.SqlProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#sqlProgram}.
	 * @param ctx the parse tree
	 */
	void exitSqlProgram(@NotNull GramaticaParser.SqlProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#fecha}.
	 * @param ctx the parse tree
	 */
	void enterFecha(@NotNull GramaticaParser.FechaContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#fecha}.
	 * @param ctx the parse tree
	 */
	void exitFecha(@NotNull GramaticaParser.FechaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tipoDAte}
	 * labeled alternative in {@link GramaticaParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTipoDAte(@NotNull GramaticaParser.TipoDAteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tipoDAte}
	 * labeled alternative in {@link GramaticaParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTipoDAte(@NotNull GramaticaParser.TipoDAteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addConstraintTB}
	 * labeled alternative in {@link GramaticaParser#tableAction}.
	 * @param ctx the parse tree
	 */
	void enterAddConstraintTB(@NotNull GramaticaParser.AddConstraintTBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addConstraintTB}
	 * labeled alternative in {@link GramaticaParser#tableAction}.
	 * @param ctx the parse tree
	 */
	void exitAddConstraintTB(@NotNull GramaticaParser.AddConstraintTBContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#decimal}.
	 * @param ctx the parse tree
	 */
	void enterDecimal(@NotNull GramaticaParser.DecimalContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#decimal}.
	 * @param ctx the parse tree
	 */
	void exitDecimal(@NotNull GramaticaParser.DecimalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addColumnTB}
	 * labeled alternative in {@link GramaticaParser#tableAction}.
	 * @param ctx the parse tree
	 */
	void enterAddColumnTB(@NotNull GramaticaParser.AddColumnTBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addColumnTB}
	 * labeled alternative in {@link GramaticaParser#tableAction}.
	 * @param ctx the parse tree
	 */
	void exitAddColumnTB(@NotNull GramaticaParser.AddColumnTBContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#insert}.
	 * @param ctx the parse tree
	 */
	void enterInsert(@NotNull GramaticaParser.InsertContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#insert}.
	 * @param ctx the parse tree
	 */
	void exitInsert(@NotNull GramaticaParser.InsertContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#update}.
	 * @param ctx the parse tree
	 */
	void enterUpdate(@NotNull GramaticaParser.UpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#update}.
	 * @param ctx the parse tree
	 */
	void exitUpdate(@NotNull GramaticaParser.UpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#orderBy}.
	 * @param ctx the parse tree
	 */
	void enterOrderBy(@NotNull GramaticaParser.OrderByContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#orderBy}.
	 * @param ctx the parse tree
	 */
	void exitOrderBy(@NotNull GramaticaParser.OrderByContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#notExp}.
	 * @param ctx the parse tree
	 */
	void enterNotExp(@NotNull GramaticaParser.NotExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#notExp}.
	 * @param ctx the parse tree
	 */
	void exitNotExp(@NotNull GramaticaParser.NotExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#delete}.
	 * @param ctx the parse tree
	 */
	void enterDelete(@NotNull GramaticaParser.DeleteContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#delete}.
	 * @param ctx the parse tree
	 */
	void exitDelete(@NotNull GramaticaParser.DeleteContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(@NotNull GramaticaParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(@NotNull GramaticaParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#columna}.
	 * @param ctx the parse tree
	 */
	void enterColumna(@NotNull GramaticaParser.ColumnaContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#columna}.
	 * @param ctx the parse tree
	 */
	void exitColumna(@NotNull GramaticaParser.ColumnaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintCheck}
	 * labeled alternative in {@link GramaticaParser#constraint}.
	 * @param ctx the parse tree
	 */
	void enterConstraintCheck(@NotNull GramaticaParser.ConstraintCheckContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintCheck}
	 * labeled alternative in {@link GramaticaParser#constraint}.
	 * @param ctx the parse tree
	 */
	void exitConstraintCheck(@NotNull GramaticaParser.ConstraintCheckContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(@NotNull GramaticaParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(@NotNull GramaticaParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tipoEntero}
	 * labeled alternative in {@link GramaticaParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTipoEntero(@NotNull GramaticaParser.TipoEnteroContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tipoEntero}
	 * labeled alternative in {@link GramaticaParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTipoEntero(@NotNull GramaticaParser.TipoEnteroContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintFK}
	 * labeled alternative in {@link GramaticaParser#constraint}.
	 * @param ctx the parse tree
	 */
	void enterConstraintFK(@NotNull GramaticaParser.ConstraintFKContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintFK}
	 * labeled alternative in {@link GramaticaParser#constraint}.
	 * @param ctx the parse tree
	 */
	void exitConstraintFK(@NotNull GramaticaParser.ConstraintFKContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(@NotNull GramaticaParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(@NotNull GramaticaParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(@NotNull GramaticaParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(@NotNull GramaticaParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#dOperation}.
	 * @param ctx the parse tree
	 */
	void enterDOperation(@NotNull GramaticaParser.DOperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#dOperation}.
	 * @param ctx the parse tree
	 */
	void exitDOperation(@NotNull GramaticaParser.DOperationContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(@NotNull GramaticaParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(@NotNull GramaticaParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropColumnTB}
	 * labeled alternative in {@link GramaticaParser#tableAction}.
	 * @param ctx the parse tree
	 */
	void enterDropColumnTB(@NotNull GramaticaParser.DropColumnTBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropColumnTB}
	 * labeled alternative in {@link GramaticaParser#tableAction}.
	 * @param ctx the parse tree
	 */
	void exitDropColumnTB(@NotNull GramaticaParser.DropColumnTBContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mostrarTablasTB}
	 * labeled alternative in {@link GramaticaParser#tOperation}.
	 * @param ctx the parse tree
	 */
	void enterMostrarTablasTB(@NotNull GramaticaParser.MostrarTablasTBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mostrarTablasTB}
	 * labeled alternative in {@link GramaticaParser#tOperation}.
	 * @param ctx the parse tree
	 */
	void exitMostrarTablasTB(@NotNull GramaticaParser.MostrarTablasTBContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#column}.
	 * @param ctx the parse tree
	 */
	void enterColumn(@NotNull GramaticaParser.ColumnContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#column}.
	 * @param ctx the parse tree
	 */
	void exitColumn(@NotNull GramaticaParser.ColumnContext ctx);
	/**
	 * Enter a parse tree produced by {@link GramaticaParser#orExp}.
	 * @param ctx the parse tree
	 */
	void enterOrExp(@NotNull GramaticaParser.OrExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link GramaticaParser#orExp}.
	 * @param ctx the parse tree
	 */
	void exitOrExp(@NotNull GramaticaParser.OrExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code eliminarTB}
	 * labeled alternative in {@link GramaticaParser#tOperation}.
	 * @param ctx the parse tree
	 */
	void enterEliminarTB(@NotNull GramaticaParser.EliminarTBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code eliminarTB}
	 * labeled alternative in {@link GramaticaParser#tOperation}.
	 * @param ctx the parse tree
	 */
	void exitEliminarTB(@NotNull GramaticaParser.EliminarTBContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameTB}
	 * labeled alternative in {@link GramaticaParser#tableAction}.
	 * @param ctx the parse tree
	 */
	void enterRenameTB(@NotNull GramaticaParser.RenameTBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameTB}
	 * labeled alternative in {@link GramaticaParser#tableAction}.
	 * @param ctx the parse tree
	 */
	void exitRenameTB(@NotNull GramaticaParser.RenameTBContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterarTB}
	 * labeled alternative in {@link GramaticaParser#tOperation}.
	 * @param ctx the parse tree
	 */
	void enterAlterarTB(@NotNull GramaticaParser.AlterarTBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterarTB}
	 * labeled alternative in {@link GramaticaParser#tOperation}.
	 * @param ctx the parse tree
	 */
	void exitAlterarTB(@NotNull GramaticaParser.AlterarTBContext ctx);
	/**
	 * Enter a parse tree produced by the {@code crearTB}
	 * labeled alternative in {@link GramaticaParser#tOperation}.
	 * @param ctx the parse tree
	 */
	void enterCrearTB(@NotNull GramaticaParser.CrearTBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code crearTB}
	 * labeled alternative in {@link GramaticaParser#tOperation}.
	 * @param ctx the parse tree
	 */
	void exitCrearTB(@NotNull GramaticaParser.CrearTBContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tipoChar}
	 * labeled alternative in {@link GramaticaParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTipoChar(@NotNull GramaticaParser.TipoCharContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tipoChar}
	 * labeled alternative in {@link GramaticaParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTipoChar(@NotNull GramaticaParser.TipoCharContext ctx);
	/**
	 * Enter a parse tree produced by the {@code eliminarDB}
	 * labeled alternative in {@link GramaticaParser#dbOperation}.
	 * @param ctx the parse tree
	 */
	void enterEliminarDB(@NotNull GramaticaParser.EliminarDBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code eliminarDB}
	 * labeled alternative in {@link GramaticaParser#dbOperation}.
	 * @param ctx the parse tree
	 */
	void exitEliminarDB(@NotNull GramaticaParser.EliminarDBContext ctx);
}