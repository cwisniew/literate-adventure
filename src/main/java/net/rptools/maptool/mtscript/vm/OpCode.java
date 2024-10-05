/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package net.rptools.maptool.mtscript.vm;

import java.util.Arrays;

/// Enumerates the opcodes for the MapToolVM.
public enum OpCode {

  /// Halts the program
  HALT((byte) 0x20, "halt"),

  // Constants
  /// Pushes a constant name onto the stack
  /// LOAD_CONST <constant index>
  LOAD_CONST((byte) 0x21, "const_load"),

  // Mathematical Operations
  /// Adds the top two values on the stack
  /// ADD
  /// Pops top two values from the stack adds them together and places the result on the stack.
  ADD((byte) 0x22, "add"),

  /// Subtracts the top two values on the stack
  /// SUB
  /// Pops top two values from the stack subtracts the first from the second and places the result
  // on the stack.
  SUB((byte) 0x23, "sub"),

  /// Multiplies the top two values on the stack
  /// MUL
  /// Pops top two values from the stack multiplies them together and places the result on the
  // stack.
  MULT((byte) 0x24, "mult"),

  /// Divides the top two values on the stack
  /// DIV
  /// Pops top two values from the stack divides the second by the first and places the result on
  // the stack.
  DIV((byte) 0x25, "div"),

  // Comparison Operations
  /// Compares the top two values on the stack for equality
  /// EQ
  /// Pops top two values from the stack compares them for equality and places the boolean result
  // on the stack.
  EQ((byte) 0x26, "eq"),

  /// Compares the top two values on the stack for inequality
  /// NEQ
  /// Pops top two values from the stack compares them for inequality and places the boolean
  // result on the stack.
  NEQ((byte) 0x27, "not_eq"),

  /// Compares the top two values on the stack for less than
  /// LT
  /// Pops top two values from the stack checks if the second is less than the first and places
  // the boolean result on the stack.
  LT((byte) 0x28, "lt"),

  /// Compares the top two values on the stack for less than or equal
  /// LTE
  /// Pops top two values from the stack checks if the second is less than or equal to the first
  // and places the boolean result on the stack.
  LTE((byte) 0x29, "lt_eq"),

  /// Compares the top two values on the stack for greater than
  /// GT
  /// Pops top two values from the stack checks if the second is greater than the first and places
  // the boolean result on the stack.
  GT((byte) 0x2A, "gt"),

  /// Compares the top two values on the stack for greater than or equal
  /// GTE
  /// Pops top two values from the stack checks if the second is greater than or equal to the
  // first and places the boolean result on the stack.
  GTE((byte) 0x2B, "gt_eq"),

  // Jump Operations
  /// Loads a label onto the stack
  /// LOAD_LABEL <label index>
  LOAD_LABEL((byte) 0x2C, "label_load"),

  /// Jumps to the given label if the top name on the stack is false
  /// JUMP_IF_FALSE <label index>
  /// If the top name on the stack is false, this instruction jumps to the label.
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
  /// The name is not popped from the stack.
  /// SET_GLOBAL <global symbol index>
  SET_GLOBAL((byte) 0x30, "global_set"),

  /// Loads a symbol from the local variable table onto the stack
  /// LOAD_LOCAL <local symbol index>
  /// This instruction loads a symbol from the local variable table onto the stack.
  LOAD_LOCAL((byte) 0x32, "local_load"),

  /// Sets a symbol in the local variable table from the top of the stack.
  /// The name is not popped from the stack.
  /// SET_LOCAL <local symbol index>
  SET_LOCAL((byte) 0x33, "local_set"),

  // Stack Operations
  /// Pops the top name from the stack
  /// POP
  POP((byte) 0x31, "pop"),

  /// Exists the current scope.
  /// This will pop the top value from the stack, then pop the number of stack
  /// values specified by the argument. The value popped from the stack initially
  /// is added back to the top of the stack.
  /// EXIT_SCOPE <stack to pop>
  EXIT_SCOPE((byte) 0x34, "exit_scope"),

  // Function Operations
  /// Calls the function at the top of the stack
  /// CALL
  CALL((byte) 0x35, "call"),

  /// Returns from the current function
  /// RETURN
  RETURN((byte) 0x36, "return"),

  /// No operation
  /// NOOP
  NOOP((byte) 0xFD, "noop"),

  /// Invalid opcode
  /// INVALID
  /// This opcode is used to indicate an invalid opcode.
  INVALID((byte) 0xFE, "* invalid *");

  /// A class to map from byte code to OpCode.
  private static class ByteCodeMap {
    /// The maximum opcode name.
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
  }
  ;

  /// The byte code map.
  private static final ByteCodeMap byteCodeMap = new ByteCodeMap();

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
