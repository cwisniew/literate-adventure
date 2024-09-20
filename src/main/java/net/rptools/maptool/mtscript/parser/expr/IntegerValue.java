package net.rptools.maptool.mtscript.parser.expr;

/// Represents an integer value in an S-expression.
/// @param value The integer value.
public record IntegerValue(int value) implements SExpressionExpr {
}
