package net.rptools.maptool.mtscript.vm.values;

/// Represents a string value in the MTScript VM.
/// @param value The string value.
public record StringType(String value) implements ValueRecord {
  @Override
  public ValueType valueType() {
    return ValueType.STRING;
  }

  @Override
  public StringType add(ValueRecord other) {
    if (other instanceof StringType otherString) {
      return new StringType(value + otherString.value());
    }
    throw new IllegalArgumentException("Cannot add a string to a non-string"); // TODO: CDW
  }

  @Override
  public BooleanType compareLessThan(ValueRecord other) {
    if (other instanceof StringType otherString) {
      return BooleanType.valueOf(value.compareTo(otherString.value) < 0);
    }
    throw new IllegalArgumentException("Cannot compare a string to a non-string"); // TODO: CDW
  }

  @Override
  public BooleanType compareLessThanOrEqual(ValueRecord other) {
    if (other instanceof StringType otherString) {
      return BooleanType.valueOf(value.compareTo(otherString.value) <= 0);
    }
    throw new IllegalArgumentException("Cannot compare a string to a non-string"); // TODO: CDW
  }

  @Override
  public BooleanType compareGreaterThan(ValueRecord other) {
    if (other instanceof StringType otherString) {
      return BooleanType.valueOf(value.compareTo(otherString.value) > 0);
    }
    throw new IllegalArgumentException("Cannot compare a string to a non-string"); // TODO: CDW
  }

  @Override
  public BooleanType compareGreaterThanOrEqual(ValueRecord other) {
    if (other instanceof StringType otherString) {
      return BooleanType.valueOf(value.compareTo(otherString.value) >= 0);
    }
    throw new IllegalArgumentException("Cannot compare a string to a non-string"); // TODO: CDW
  }


}
