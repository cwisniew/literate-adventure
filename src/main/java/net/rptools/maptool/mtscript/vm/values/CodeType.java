package net.rptools.maptool.mtscript.vm.values;


import java.util.ArrayList;
import java.util.List;

/// Represents a code type in the MTScript VM.
public class CodeType implements ValueRecord {
  /// The name of the code.
  private final String name;

  /// The byte code.
  private final byte[] code;

  /// The constants.
  private final ValueRecord[] constants;

  // The jump labels.
  private final int[] jumpLabels;

  /// Creates a new code type.
  /// @param name The name of the code.
  /// @param code The byte code.
  /// @param constants The constants.
  /// @param jumpTargets The jump labels.
  public CodeType(String name, byte[] code, List<ValueRecord> constants, List<Integer> jumpLabels) {
    this.name = name;
    this.code = new byte[code.length];
    System.arraycopy(code, 0, this.code, 0, code.length);
    this.constants = constants.toArray(new ValueRecord[0]);
    this.jumpLabels = jumpLabels.stream().mapToInt(i -> i).toArray();
  }

  @Override
  public ValueType valueType() {
      return ValueType.CODE;
  }

  /// Returns the name of the code.
  /// @return The name of the code.
  public String name() {
    return name;
  }


  /// Returns the constants at the given index.
  /// @param index The index of the constant.
  /// @return The constant at the given index.
  public ValueRecord getConstant(int index) {
    if (index < 0 || index >= constants.length) {
      throw new IndexOutOfBoundsException("Index out of bounds: " + index); // TODO: CDW
    }
    return constants[index];
  }

  /// Returns the byte at the given index from the byte code.
  /// @param index The index of the byte.
  /// @return The byte at the given index.
  public byte getByte(int index) {
    return code[index];
  }

  /// Returns the jump label at the given index.
  /// @param index The index of the jump label.
  /// @return The jump label at the given index.
  public int getJumpLabel(int index) {
    if (index < 0 || index >= jumpLabels.length) {
      throw new IndexOutOfBoundsException("Index out of bounds: " + index); // TODO: CDW
    }
    return jumpLabels[index];
  }


}
