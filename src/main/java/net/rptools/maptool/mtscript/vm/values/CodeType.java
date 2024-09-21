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
  private final List<ValueRecord> constants = new ArrayList<>();

  // The jump labels.
  private final List<Integer> jumpLabels = new ArrayList<>();

  /// Creates a new code type.
  /// @param name The name of the code.
  /// @param code The byte code.
  /// @param constants The constants.
  /// @param jumpTargets The jump labels.
  public CodeType(String name, byte[] code, List<ValueRecord> constants, List<Integer> jumpLabels) {
    this.name = name;
    this.code = new byte[code.length];
    this.jumpLabels.addAll(jumpLabels);
    System.arraycopy(code, 0, this.code, 0, code.length);
    this.constants.addAll(constants);
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
    return constants.get(index);
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
    return jumpLabels.get(index);
  }


}
