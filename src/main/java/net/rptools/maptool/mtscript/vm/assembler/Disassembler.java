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
package net.rptools.maptool.mtscript.vm.assembler;

import java.io.PrintStream;
import net.rptools.maptool.mtscript.vm.OpCode;
import net.rptools.maptool.mtscript.vm.VMGlobals;
import net.rptools.maptool.mtscript.vm.values.CodeType;

/// Disassembler for the MapToolVM bytecode.
/// This class is used to convert the bytecode into a human-readable format.
/// This is useful for debugging and testing.
public class Disassembler {

  /// The code to disassemble.
  private CodeType code;

  /// The current instruction pointer.
  private int instructionPointer = 0;

  /// The globals for the VM.
  private final VMGlobals globals;

  /// Creates a new disassembler for the given code.
  public Disassembler(CodeType code, VMGlobals globals) {
    this.code = code;
    this.globals = globals;
  }

  /// Disassembles the code and writes the output to the given stream.
  /// @param out The stream to write the disassembled code to.
  public void disassemble(PrintStream out) {
    out.printf("Program: %s, Code Size = %d bytes\n", code.name(), code.codeLength());
    out.println("Global Variables: " + globals.getGlobalVariableCount());
    out.println("Constants: " + code.constants().size());
    out.println();
    while (instructionPointer < code.codeLength()) {
      disassembleInstruction(out);
    }
  }

  private void dumpByteCode(PrintStream out, byte... bytes) {
    for (byte b : bytes) {
      out.printf("%02x ", b);
    }
    for (int i = bytes.length; i < 4; i++) {
      out.print("   ");
    }
  }

  /// Disassembles the next instruction.
  /// @param out The stream to write the disassembled instruction to.
  private void disassembleInstruction(PrintStream out) {
    out.printf("%04x: ", instructionPointer);
    OpCode op = readNextOpCode();
    switch (op) {
      case ADD, SUB, MULT, DIV, EQ, NEQ, LT, GT, LTE, GTE, HALT, POP -> {
        dumpByteCode(out, op.byteCode());
        out.printf("%-15s", op.instructionName());
      }
      case LOAD_CONST -> {
        byte constInd = readNextByte();
        var constant = code.getConstant(constInd);
        dumpByteCode(out, op.byteCode(), constInd);
        out.printf("%-15s     %02x          ; %s", op.instructionName(), constInd, constant);
      }
      case LOAD_GLOBAL, SET_GLOBAL -> {
        byte globalInd = readNextByte();
        dumpByteCode(out, op.byteCode(), globalInd);
        out.printf(
            "%-15s     %02x          ; Var = %s",
            op.instructionName(), globalInd, globals.getGlobalVariable(globalInd).name());
      }
      case LOAD_LOCAL, SET_LOCAL -> {
        byte localInd = readNextByte();
        dumpByteCode(out, op.byteCode(), localInd);
        out.printf(
            "%-15s     %02x          ; Local Ind = 0x%04x",
            op.instructionName(), localInd, localInd);
      }
      case LOAD_LABEL -> {
        byte label = readNextByte();
        int addr = code.getJumpLabel(label);
        dumpByteCode(out, op.byteCode(), label);
        out.printf("%-15s     %02x          ; addr = %04x", op.instructionName(), label, addr);
      }
      case JUMP, JUMP_IF_FALSE -> {
        byte label = readNextByte();
        int addr = code.getJumpLabel(label);
        dumpByteCode(out, op.byteCode(), label);
        out.printf("%-15s     %02x          ; Jump to 0x%04x", op.instructionName(), label, addr);
      }
      case EXIT_SCOPE -> {
        byte stackToPop = readNextByte();
        dumpByteCode(out, op.byteCode(), stackToPop);
        out.printf(
            "%-15s     %02x          ; Exit Scope - Pop %d local vars",
            op.instructionName(), stackToPop, stackToPop);
      }
      case CALL -> {
        byte numArgs = readNextByte();
        dumpByteCode(out, op.byteCode(), numArgs);
        out.printf("%-15s     %02x          ; Call with %d args", op.instructionName(), numArgs, numArgs);
      }
      default -> throw new IllegalStateException("Unexpected name: " + op); // TODO: CDW
    }
    out.println();
  }

  /// Reads the next byte from the code.
  /// @return The next byte.
  private byte readNextByte() {
    return code.getByte(instructionPointer++);
  }

  /// Reads the next opcode from the code.
  /// @return The next opcode.
  private OpCode readNextOpCode() {
    return OpCode.fromByteCode(readNextByte());
  }
}
