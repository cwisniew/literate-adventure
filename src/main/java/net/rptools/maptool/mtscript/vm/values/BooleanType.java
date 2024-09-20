package net.rptools.maptool.mtscript.vm.values;

/// Represents a boolean value in the MTScript VM.
/// @param value The value of the boolean.
public record BooleanType(boolean value) implements ValueRecord {

  @Override
  public ValueType valueType() {
    return ValueType.BOOLEAN;
  }
}
