package net.rptools.maptool.mtscript.vm.values;

/// Represents a variable in the MTScript VM.
/// Variables are used to store values that can be modified.
/// @param name The name of the variable.
/// @param value The value of the variable.
public record Symbol(String name, ValueRecord value) implements ValueRecord {
  @Override
  public ValueType valueType() {
    return value.valueType();
  }

  @Override
  public ValueRecord add(ValueRecord other) {
    return ValueRecord.super.add(other);
  }

  @Override
  public ValueRecord subtract(ValueRecord other) {
    return ValueRecord.super.subtract(other);
  }

  @Override
  public ValueRecord multiply(ValueRecord other) {
    return ValueRecord.super.multiply(other);
  }

  @Override
  public ValueRecord divide(ValueRecord other) {
    return ValueRecord.super.divide(other);
  }

  @Override
  public BooleanType compareEqual(ValueRecord other) {
    return ValueRecord.super.compareEqual(other);
  }

  @Override
  public BooleanType compareNotEqual(ValueRecord other) {
    return ValueRecord.super.compareNotEqual(other);
  }

  @Override
  public BooleanType compareLessThan(ValueRecord other) {
    return ValueRecord.super.compareLessThan(other);
  }

  @Override
  public BooleanType compareLessThanOrEqual(ValueRecord other) {
    return ValueRecord.super.compareLessThanOrEqual(other);
  }

  @Override
  public BooleanType compareGreaterThan(ValueRecord other) {
    return ValueRecord.super.compareGreaterThan(other);
  }

  @Override
  public BooleanType compareGreaterThanOrEqual(ValueRecord other) {
    return ValueRecord.super.compareGreaterThanOrEqual(other);
  }
}
