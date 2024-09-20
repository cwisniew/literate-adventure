package net.rptools.maptool.mtscript.vm.values;

/// Represents the type that the value is in the MTScript VM.
public enum ValueType {
  /// Represents a boolean value.
  BOOLEAN,
  /// Represents a numerical value.
  NUMBER,
  /// Represents a string value.
  STRING,
  /// Represents a boolean value.
  OBJECT,
  /// Represents a function value.
  CODE
}