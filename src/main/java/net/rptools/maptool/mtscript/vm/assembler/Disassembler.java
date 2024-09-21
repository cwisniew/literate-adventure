package net.rptools.maptool.mtscript.vm.assembler;

import java.io.PrintStream;
import net.rptools.maptool.mtscript.vm.OpCode;
import net.rptools.maptool.mtscript.vm.values.CodeType;

/// Disassembler for the MapToolVM bytecode.
/// This class is used to convert the bytecode into a human-readable format.
/// This is useful for debugging and testing.
public class Disassembler {

  /// The code to disassemble.
  private CodeType code;

  /// The current instruction pointer.
  private int instructionPointer = 0;

  /// Creates a new disassembler for the given code.
  public Disassembler(CodeType code) {
    this.code = code;
  }


  /// Disassembles the code and writes the output to the given stream.
  /// @param out The stream to write the disassembled code to.
  public void disassemble(PrintStream out) {
    out.println("Program: " + code.name());
    out.println("Constants: " + code.constants().size());
    out.println();
    while (instructionPointer < code.codeLength()) {
      disassembleInstruction(out);
    }
  }

  /// Disassembles the next instruction.
  /// @param out The stream to write the disassembled instruction to.
  private void disassembleInstruction(PrintStream out) {
    out.printf("0x%04x: ", instructionPointer);
    OpCode op = readNextOpCode();
    switch (op) {
      case ADD, SUB, MULT, DIV, EQ, NEQ, LT, GT, LTE, GTE, HALT ->
          out.printf("%-10s", op.instructionName());
      case LOAD_CONST -> {
        int constInd = readNextByte();
        var constant = code.getConstant(constInd);
        out.printf("%-10s     0x%02x          ; %s", op.instructionName(), constInd, constant);
      }
      case LOAD_LABEL -> {
        int label = readNextByte();
        int addr = code.getJumpLabel(label);
        out.printf("%-10s     0x%02x          ; addr = 0x%04x", op.instructionName(), label, addr);

      }
      case JUMP, JUMP_IF_FALSE -> {
        int label = readNextByte();
        int addr = code.getJumpLabel(label);
        out.printf("%-10s     0x%02x          ; Jump to 0x%04x", op.instructionName(), label, addr);

      }
      default -> throw new IllegalStateException("Unexpected value: " + op); // TODO: CDW
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
