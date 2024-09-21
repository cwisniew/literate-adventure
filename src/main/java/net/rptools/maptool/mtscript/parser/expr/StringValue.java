package net.rptools.maptool.mtscript.parser.expr;

/// Represents a string value in an S-expression.
/// @param value The string value.
public record StringValue(String value) implements SExpressionValue {
  /// Gets the string representation of the string value.
  @Override
  public String op() {
    return "string: " + value;
  }
}
