package net.rptools.maptool.mtscript.parser.expr;

/// Represents an integer value in an S-expression.
/// @param value The integer value.
public record IntegerValue(int value) implements SExpressionValue {
  /// Gets the string representation of the integer value.
  @Override
  public String op() {
    return "integer: " + value;
  }
}
