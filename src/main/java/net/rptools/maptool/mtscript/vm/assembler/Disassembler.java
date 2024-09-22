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
      case ADD, SUB, MULT, DIV, EQ, NEQ, LT, GT, LTE, GTE, HALT -> {
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
        out.printf("%-15s     %02x          ; Var = %s", op.instructionName(), globalInd,
            globals.getGlobalVariable(globalInd).name());
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
        out.printf("%-15s     %02x          ; Jump to %04x", op.instructionName(), label, addr);

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
