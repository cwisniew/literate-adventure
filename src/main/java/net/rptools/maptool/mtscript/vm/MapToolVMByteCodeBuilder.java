package net.rptools.maptool.mtscript.vm;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import net.rptools.maptool.mtscript.vm.values.BooleanType;
import net.rptools.maptool.mtscript.vm.values.CodeType;
import net.rptools.maptool.mtscript.vm.values.IntegerType;
import net.rptools.maptool.mtscript.vm.values.StringType;
import net.rptools.maptool.mtscript.vm.values.ValueRecord;

/// Class for building byte code for the MTScript VM.
public class MapToolVMByteCodeBuilder {

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
    writeByte(OpCode.HALT);
    return new CodeType(name, byteCodeStream.toByteArray(), constants, jumpLabels);
  }

  /// Emits a load constant instruction.
  /// @param constant The constant to load.
  public void emitLoadConstant(ValueRecord constant) {
    writeByte(OpCode.LOAD_CONST);
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
    writeByte(OpCode.ADD);
  }

  /// Emits a subtract instruction.
  public void emitSubtract() {
    writeByte(OpCode.SUB);
  }

  /// Emits a multiply instruction.
  public void emitMultiply() {
    writeByte(OpCode.MUL);
  }

  /// Emits a divide instruction.
  public void emitDivide() {
    writeByte(OpCode.DIV);
  }

  /// Emits an equality comparison instruction.
  public void emitEqual() {
    writeByte(OpCode.EQ);
  }

  /// Emits a not equal comparison instruction.
  public void emitNotEqual() {
    writeByte(OpCode.NEQ);
  }

  /// Emits a less than comparison instruction.
  public void emitLessThan() {
    writeByte(OpCode.LT);
  }

  /// Emits a less than or equal comparison instruction.
  public void emitLessThanOrEqual() {
    writeByte(OpCode.LTE);
  }

  /// Emits a greater than comparison instruction.
  public void emitGreaterThan() {
    writeByte(OpCode.GT);
  }

  /// Emits a greater than or equal comparison instruction.
  public void emitGreaterThanOrEqual() {
    writeByte(OpCode.GTE);
  }


}
