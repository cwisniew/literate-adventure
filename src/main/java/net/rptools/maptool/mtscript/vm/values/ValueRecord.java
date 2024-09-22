package net.rptools.maptool.mtscript.vm.values;

import net.rptools.maptool.mtscript.parser.expr.BooleanValue;

/// Represents a value and its type in the MTScript VM.
public interface ValueRecord {
  /// Returns the type of this value.
  /// @return The type of this value.
  ValueType valueType();

  /// Adds another value to this value.
  /// @param other The value to add to this value.
  default ValueRecord add(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException("Addition not supported for " + valueType()); //
  }

  /// Subtracts another value from this value.
  /// @param other The value to subtract from this value.
  default ValueRecord subtract(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException("Subtraction not supported for " + valueType());
  }

  /// Multiplies this value by another value.
  /// @param other The value to multiply this value by.
  default ValueRecord multiply(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException("Multiplication not supported for " + valueType());
  }

  /// Divides this value by another value.
  /// @param other The value to divide this value by.
  default ValueRecord divide(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException("Division not supported for " + valueType());
  }

  /// Compares this value to another value for equality.
  /// @param other The value to compare to this value.
  default BooleanType compareEqual(ValueRecord other) {
    // TODO: CDW
    if (valueType() != other.valueType()) {
      throw new UnsupportedOperationException("Cannot compare " + valueType() + " to " + other.valueType());
    }
    return BooleanType.valueOf(equals(other));
  }


  /// Compares this value to another value for inequality.
  /// @param other The value to compare to this value.
  default BooleanType compareNotEqual(ValueRecord other) {
    // TODO: CDW
    return compareEqual(other).not();
  }

  /// Compares this value to another value for less than.
  /// @param other The value to compare to this value.
  default BooleanType compareLessThan(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException("Cannot compare " + valueType() + " to " + other.valueType());
  }

  /// Compares this value to another value for less than or equal.
  /// @param other The value to compare to this value.
  default BooleanType compareLessThanOrEqual(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException("Cannot compare " + valueType() + " to " + other.valueType());
  }

  /// Compares this value to another value for greater than.
  /// @param other The value to compare to this value.
  default BooleanType compareGreaterThan(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException("Cannot compare " + valueType() + " to " + other.valueType());
  }

  /// Compares this value to another value for greater than or equal.
  /// @param other The value to compare to this value.
  default BooleanType compareGreaterThanOrEqual(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException("Cannot compare " + valueType() + " to " + other.valueType());
  }

}
