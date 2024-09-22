package net.rptools.maptool.mtscript.vm;

import java.util.Arrays;

/// Enumerates the opcodes for the MapToolVM.
public enum OpCode {


  /// Halts the program
  HALT((byte) 0x20, "halt"),

  // Constants
  /// Pushes a constant value onto the stack
  /// LOAD_CONST <constant index>
  LOAD_CONST((byte) 0x21, "const_load"),

  // Mathematical Operations
  /// Adds the top two values on the stack
  /// ADD
  /// Pops top two values from the stack adds them together and places the result on the stack.
  ADD((byte) 0x22, "add"),

  /// Subtracts the top two values on the stack
  /// SUB
  /// Pops top two values from the stack subtracts the first from the second and places the result on the stack.
  SUB((byte) 0x23, "sub"),

  /// Multiplies the top two values on the stack
  /// MUL
  /// Pops top two values from the stack multiplies them together and places the result on the stack.
  MULT((byte) 0x24, "mult"),

  /// Divides the top two values on the stack
  /// DIV
  /// Pops top two values from the stack divides the second by the first and places the result on the stack.
  DIV((byte) 0x25, "div"),

  // Comparison Operations
  /// Compares the top two values on the stack for equality
  /// EQ
  /// Pops top two values from the stack compares them for equality and places the boolean result on the stack.
  EQ((byte) 0x26, "eq"),

  /// Compares the top two values on the stack for inequality
  /// NEQ
  /// Pops top two values from the stack compares them for inequality and places the boolean result on the stack.
  NEQ((byte) 0x27, "not_eq"),

  /// Compares the top two values on the stack for less than
  /// LT
  /// Pops top two values from the stack checks if the second is less than the first and places the boolean result on the stack.
  LT((byte) 0x28, "lt"),

  /// Compares the top two values on the stack for less than or equal
  /// LTE
  /// Pops top two values from the stack checks if the second is less than or equal to the first and places the boolean result on the stack.
  LTE((byte) 0x29, "lt_eq"),

  /// Compares the top two values on the stack for greater than
  /// GT
  /// Pops top two values from the stack checks if the second is greater than the first and places the boolean result on the stack.
  GT((byte) 0x2A, "gt"),


  /// Compares the top two values on the stack for greater than or equal
  /// GTE
  /// Pops top two values from the stack checks if the second is greater than or equal to the first and places the boolean result on the stack.
  GTE((byte) 0x2B, "gt_eq"),

  // Jump Operations
  /// Loads a label onto the stack
  /// LOAD_LABEL <label index>
  LOAD_LABEL((byte) 0x2C, "label_load"),


  /// Jumps to the given label if the top value on the stack is false
  /// JUMP_IF_FALSE <label index>
  /// If the top value on the stack is false, this instruction jumps to the label.
  JUMP_IF_FALSE((byte) 0x2D, "jmp_false"),

  /// Jumps to the given label
  /// JUMP <label index>
  /// Unconditionally jumps to the label.
  JUMP((byte) 0x2E, "jump"),


  // Symbol Operations
  /// Loads a symbol from the global variable table onto the stack
  /// LOAD_GLOBAL <global symbol index>
  LOAD_GLOBAL((byte) 0x2F, "global_load"),

  /// Sets a symbol in the global variable table from the top of the stack.
  /// The value is not popped from the stack.
  /// SET_GLOBAL <global symbol index>
  SET_GLOBAL((byte) 0x30, "global_set"),




  /// No operation
  /// NOOP
  NOOP((byte) 0xFD, "noop"),

  /// Invalid opcode
  /// INVALID
  /// This opcode is used to indicate an invalid opcode.
  INVALID((byte) 0xFE, "* invalid *");


  /// A class to map from byte code to OpCode.
  private static class ByteCodeMap {
    /// The maximum opcode value.
    private static final int MAX_OPCODE = 255;
    /// The size of the opcode map.
    private static final int MAX_OPCODE_MAP_SIZE = MAX_OPCODE + 1;

    /// The opcode map.
    private final OpCode[] byteCodeMap = new OpCode[MAX_OPCODE_MAP_SIZE];

    /// Creates a new byte code map.
    public ByteCodeMap() {
      Arrays.fill(byteCodeMap, INVALID);
      for (OpCode op : OpCode.values()) {
        byteCodeMap[op.byteCode() & 0xFF] = op; // Java Bytes are signed
      }
    }
  };

  /// The byte code map.
  private final static ByteCodeMap byteCodeMap = new ByteCodeMap();

  /// The byte code.
  private final byte byteCode;

  /// The instruction name.
  private final String instructionName;


  /// Creates a new OpCode.
  /// @param byteCode The byte code.
  /// @param instructionName The instruction name.
  OpCode(byte byteCode, String instructionName) {
    this.byteCode = byteCode;
    this.instructionName = instructionName;
  }

  /// Returns the byte code.
  /// @return The byte code.
  public byte byteCode() {
    return byteCode;
  }

  /// Returns the instruction name.
  /// @return The instruction name.
  public String instructionName() {
    return instructionName;
  }

  /// Returns the OpCode for the given byte code.
  /// @param byteCode The byte code.
  /// @return The OpCode.
  public static OpCode fromByteCode(byte byteCode) {
    int bcLookup = byteCode & 0xFF; // Java Bytes are signed
    return byteCodeMap.byteCodeMap[bcLookup];
  }
}
