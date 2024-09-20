package net.rptools.maptool.mtscript.vm.values;

/// Represents a value and its type in the MTScript VM.
public interface ValueRecord {
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

}
