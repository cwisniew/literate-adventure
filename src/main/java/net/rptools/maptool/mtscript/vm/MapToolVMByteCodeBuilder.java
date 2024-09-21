package net.rptools.maptool.mtscript.vm;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import net.rptools.maptool.mtscript.vm.values.CodeType;
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
  public void writeByte(byte b) {
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
}
