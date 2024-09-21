package net.rptools.maptool.mtscript.parser.expr;

/// Represents a boolean value in an S-expression.
/// @param value The boolean value.
public record BooleanValue(boolean value) implements SExpressionValue {
  @Override
  public String op() {
    return value ? "true" : "false";
  }
}
