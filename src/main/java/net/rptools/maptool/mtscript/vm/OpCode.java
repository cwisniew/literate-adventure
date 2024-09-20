package net.rptools.maptool.mtscript.vm;

public class OpCode {
  /// Halts the program
  public static final byte HALT = (byte) 0x20;

  // Constants
  /// Pushes a constant value onto the stack
  public static final byte CONST = (byte) 0x21;

  // Mathematical Operations
  /// Adds the top two values on the stack
  public static final byte ADD = (byte) 0x22;

  /// Subtracts the top two values on the stack
  public static final byte SUB = (byte) 0x23;

  /// Multiplies the top two values on the stack
  public static final byte MUL = (byte) 0x24;

  /// Divides the top two values on the stack
  public static final byte DIV = (byte) 0x25;


}
