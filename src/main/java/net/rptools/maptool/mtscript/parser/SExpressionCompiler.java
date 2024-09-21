package net.rptools.maptool.mtscript.parser;

import net.rptools.maptool.mtscript.vm.MapToolVMByteCodeBuilder;
import net.rptools.maptool.mtscript.vm.values.CodeType;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

/// A compiler for S-expressions.
public class SExpressionCompiler {

  /// Compiles an S-expression.
  /// @param source The source code of the S-expression.
  /// @param name The name to attribute to the program being compiled.
  /// @return The compiled program.
  public CodeType compile(String source, String name) {
    var lexer = new mtSexpressionLexer(CharStreams.fromString(source));
    var tokens = new CommonTokenStream(lexer);
    var parser = new mtSexpressionParser(tokens);
    var builder = new MapToolVMByteCodeBuilder(name);
    var visitor = new MTSExpressionVisitor(builder);
    var code = visitor.visit(parser.sexpr());
    return builder.build();
  }
}
