/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package net.rptools.maptool.mtscript.parser;

import net.rptools.maptool.mtscript.vm.MapToolVMByteCodeBuilder;
import net.rptools.maptool.mtscript.vm.VMGlobals;
import net.rptools.maptool.mtscript.vm.values.CodeType;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

/// A compiler for S-expressions.
public class SExpressionCompiler {

  /// The global symbol table.
  private final VMGlobals globals;

  /// Creates a new S-expression compiler.
  /// @param globals The global symbol table.
  public SExpressionCompiler(VMGlobals globals) {
    this.globals = globals;
  }

  /// Compiles an S-expression.
  /// @param source The source code of the S-expression.
  /// @param name The name to attribute to the program being compiled.
  /// @return The compiled program.
  public CodeType compile(String source, String name) {
    var lexer = new mtSexpressionLexer(CharStreams.fromString("(block " + source + ")"));
    var tokens = new CommonTokenStream(lexer);
    var parser = new mtSexpressionParser(tokens);
    var builder = new MapToolVMByteCodeBuilder(name, globals);
    var visitor = new MTSExpressionVisitor(builder, globals);
    var code = visitor.visit(parser.sexpr());
    return builder.buildProgram();
  }
}
