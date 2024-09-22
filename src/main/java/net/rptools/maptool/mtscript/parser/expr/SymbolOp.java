package net.rptools.maptool.mtscript.parser.expr;

/// Represents a symbol operation in an S-expression for an unknown symbol that will have
/// to be resolved as a variable or constant.
/// @param op The operation.
/// @param defined Whether the symbol is defined.
public record SymbolOp(String op, boolean defined) implements SExpressionExpr {
}
