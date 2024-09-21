package net.rptools.maptool.mtscript.vm.values;

/// Represents a boolean value in the MTScript VM.
/// @param value The value of the boolean.
public class BooleanType implements ValueRecord {

  /// Represents the true boolean value.
  public static final BooleanType TRUE = new BooleanType(true);

  /// Represents the false boolean value.
  public static final BooleanType FALSE = new BooleanType(false);

  /// The value of the boolean.
  private final boolean value;

  /// Creates a new boolean value.
  /// @param value The value of the boolean.
  private BooleanType(boolean value) {
    this.value = value;
  }

  /// Returns the boolean value of the given boolean.
  /// @param value The boolean value.
  public static BooleanType valueOf(boolean value) {
    return value ? TRUE : FALSE;
  }

  /// Returns the value of the boolean.
  public boolean value() {
    return value;
  }

  @Override
  public ValueType valueType() {
    return ValueType.BOOLEAN;
  }

  /// Returns the result of the logical AND operation between this boolean and another boolean.
  /// @param other The other boolean.
  public BooleanType not() {
    return value ? FALSE : TRUE;
  }
}
