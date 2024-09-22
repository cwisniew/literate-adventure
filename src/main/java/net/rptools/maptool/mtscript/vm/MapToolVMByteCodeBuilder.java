package net.rptools.maptool.mtscript.vm;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import net.rptools.maptool.mtscript.vm.values.BooleanType;
import net.rptools.maptool.mtscript.vm.values.CodeType;
import net.rptools.maptool.mtscript.vm.values.IntegerType;
import net.rptools.maptool.mtscript.vm.values.StringType;
import net.rptools.maptool.mtscript.vm.values.ValueRecord;

/// Class for building byte code for the MTScript VM.
public class MapToolVMByteCodeBuilder {

  private record IfElseLabels(int elseLabel, int endIfLabel) {};

  private final Stack<IfElseLabels> ifElseLabels = new Stack<>();

  /// The byte code stream.
  private final ByteArrayOutputStream byteCodeStream = new ByteArrayOutputStream();

  /// The list of constants.
  private final List<ValueRecord> constants = new ArrayList<>();

  /// The list of jump labels.
  private final List<Integer> jumpLabels = new ArrayList<>();


  /// The name of the byte code.
  private final String name;

  /// Creates a new byte code builder.
  /// @param name The name of the byte code.
  public MapToolVMByteCodeBuilder(String name) {
    this.name = name;
  }


  /// Writes a byte to the byte code stream.
  /// @param b The byte to write.
  private void writeByte(byte b) {
    byteCodeStream.write(b);
  }

  /// Emits an opcode to the byte code stream.
  /// @param op The opcode to emit.
  public void emit(OpCode op) {
    writeByte(op.byteCode());
  }

  /// Adds a constant to the program.
  /// @param constant The constant to add.
  /// @return The index of the constant in the constant pool.
  public int addConstant(ValueRecord constant) {
    if (constants.contains(constant)) {
      return constants.indexOf(constant);
    }
    constants.add(constant);
    return constants.size() - 1;
  }

  /// Allocates a jump label.
  /// @return The index of the jump label.
  public int allocateJumpLabel() {
    jumpLabels.add(-1);
    return jumpLabels.size() - 1;
  }

  /// Sets the jump label to the next byte code instruction.
  /// @param label The index of the jump label.
  public void setJumpLabel(int label) {
    jumpLabels.set(label, byteCodeStream.size());
  }

  /// Builds the code type.
  /// @return The code type.
  public CodeType build() {
    emit(OpCode.HALT);
    return new CodeType(name, byteCodeStream.toByteArray(), constants, jumpLabels);
  }

  /// Emits a load constant instruction.
  /// @param constant The constant to load.
  public void emitLoadConstant(ValueRecord constant) {
    emit(OpCode.LOAD_CONST);
    writeByte((byte) addConstant(constant));  // TODO: CDW Handle > 256 constants
  }

  /// Emits a load constant instruction for an integer.
  /// @param value The integer to load.
  public void emitLoadConstant(int value) {
    emitLoadConstant(new IntegerType(value));
  }

  /// Emits a load constant instruction for a string.
  /// @param value The string to load.
  public void emitLoadConstant(String value) {
    emitLoadConstant(new StringType(value));
  }

  /// Emits a load constant instruction for a boolean.
  /// @param value The boolean to load.
  public void emitLoadConstant(boolean value) {
    emitLoadConstant(value ? BooleanType.TRUE : BooleanType.FALSE);
  }

  /// Emits an add instruction.
  public void emitAdd() {
    emit(OpCode.ADD);
  }

  /// Emits a subtract instruction.
  public void emitSubtract() {
    emit(OpCode.SUB);
  }

  /// Emits a multiply instruction.
  public void emitMultiply() {
    emit(OpCode.MULT);
  }

  /// Emits a divide instruction.
  public void emitDivide() {
    emit(OpCode.DIV);
  }

  /// Emits an equality comparison instruction.
  public void emitEqual() {
    emit(OpCode.EQ);
  }

  /// Emits a not equal comparison instruction.
  public void emitNotEqual() {
    emit(OpCode.NEQ);
  }

  /// Emits a less than comparison instruction.
  public void emitLessThan() {
    emit(OpCode.LT);
  }

  /// Emits a less than or equal comparison instruction.
  public void emitLessThanOrEqual() {
    emit(OpCode.LTE);
  }

  /// Emits a greater than comparison instruction.
  public void emitGreaterThan() {
    emit(OpCode.GT);
  }

  /// Emits a greater than or equal comparison instruction.
  public void emitGreaterThanOrEqual() {
    emit(OpCode.GTE);
  }

  /// Emits a jump instruction.
  public void emitJumpIfFalse(int label) {
    emit(OpCode.JUMP_IF_FALSE);
    writeByte((byte) label); // TODO CDW Handle > 256 labels
  }

  /// Emits a jump instruction.
  public void emitJump(int label) {
    emit(OpCode.JUMP);
    writeByte((byte) label); // TODO CDW Handle > 256 labels
  }

  /// Emits a load global symbol instruction.
  /// @param index The index of the global symbol.
  public void emitLoadGlobal(int index) {
    emit(OpCode.LOAD_GLOBAL);
    writeByte((byte) index); // TODO CDW Handle > 256 globals
  }

  /// Emits a set global symbol instruction.
  /// @param index The index of the global symbol.
  public void emitSetGlobal(int index) {
    emit(OpCode.SET_GLOBAL);
    writeByte((byte) index); // TODO CDW Handle > 256 globals
  }

}
