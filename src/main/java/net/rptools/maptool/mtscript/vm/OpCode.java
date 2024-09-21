package net.rptools.maptool.mtscript.vm;

public class OpCode {
  /// Halts the program
  public static final byte HALT = (byte) 0x20;

  // Constants
  /// Pushes a constant value onto the stack
  /// LOAD_CONST <constant index>
  public static final byte LOAD_CONST = (byte) 0x21;

  // Mathematical Operations
  /// Adds the top two values on the stack
  /// ADD
  /// Pops top two values from the stack adds them together and places the result on the stack.
  public static final byte ADD = (byte) 0x22;

  /// Subtracts the top two values on the stack
  /// SUB
  /// Pops top two values from the stack subtracts the first from the second and places the result on the stack.
  public static final byte SUB = (byte) 0x23;

  /// Multiplies the top two values on the stack
  /// MUL
  /// Pops top two values from the stack multiplies them together and places the result on the stack.
  public static final byte MUL = (byte) 0x24;

  /// Divides the top two values on the stack
  /// DIV
  /// Pops top two values from the stack divides the second by the first and places the result on the stack.
  public static final byte DIV = (byte) 0x25;

  // Comparison Operations
  /// Compares the top two values on the stack for equality
  /// EQ
  /// Pops top two values from the stack compares them for equality and places the boolean result on the stack.
  public static final byte EQ = (byte) 0x26;

  /// Compares the top two values on the stack for inequality
  /// NEQ
  /// Pops top two values from the stack compares them for inequality and places the boolean result on the stack.
  public static final byte NEQ = (byte) 0x27;

  /// Compares the top two values on the stack for less than
  /// LT
  /// Pops top two values from the stack checks if the second is less than the first and places the boolean result on the stack.
  public static final byte LT = (byte) 0x28;

  /// Compares the top two values on the stack for less than or equal
  /// LTE
  /// Pops top two values from the stack checks if the second is less than or equal to the first and places the boolean result on the stack.
  public static final byte LTE = (byte) 0x29;

  /// Compares the top two values on the stack for greater than
  /// GT
  /// Pops top two values from the stack checks if the second is greater than the first and places the boolean result on the stack.
  public static final byte GT = (byte) 0x2A;


  /// Compares the top two values on the stack for greater than or equal
  /// GTE
  /// Pops top two values from the stack checks if the second is greater than or equal to the first and places the boolean result on the stack.
  public static final byte GTE = (byte) 0x2B;


}
