package net.rptools.maptool.mtscript.vm.values;

/// Represents a number value in the MTScript VM.
/// @param value The value value.
public record NumberType(double value) implements ValueRecord {
  @Override
  public ValueType valueType() {
    return ValueType.NUMBER;
  }

  @Override
  public NumberType add(ValueRecord other) {
    if (other instanceof NumberType otherNumber) {
      return new NumberType(value + otherNumber.value());
    }
    throw new IllegalArgumentException("Cannot add a value to a non-value"); // TODO: CDW
  }

  @Override
  public ValueRecord subtract(ValueRecord other) {
    if (other instanceof NumberType otherNumber) {
      return new NumberType(value - otherNumber.value());
    }
    throw new IllegalArgumentException("Cannot subtract a value from a non-value"); // TODO: CDW
  }

  @Override
  public ValueRecord multiply(ValueRecord other) {
    if (other instanceof NumberType otherNumber) {
      return new NumberType(value * otherNumber.value());
    }
    throw new IllegalArgumentException("Cannot multiply a value by a non-value"); // TODO: CDW
  }

  @Override
  public ValueRecord divide(ValueRecord other) {
    if (other instanceof NumberType otherNumber) {
      return new NumberType(value / otherNumber.value());
    }
    throw new IllegalArgumentException("Cannot divide a value by a non-value"); // TODO: CDW
  }
}
