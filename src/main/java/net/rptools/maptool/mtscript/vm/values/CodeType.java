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
package net.rptools.maptool.mtscript.vm.values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.rptools.maptool.mtscript.vm.SymbolEntry;

/// Represents a code type in the MTScript VM.
public class CodeType implements ValueRecord {
  /// The name of the code.
  private final String name;

  /// The byte code.
  private final byte[] code;

  /// The constants.
  private final ValueRecord[] constants;

  /// The jump labels.
  private final int[] jumpLabels;

  /// The functions that have been defined.
  private final FunctionType[] functions;



  /// Creates a new code type.
  /// @param name The name of the code.
  /// @param code The byte code.
  /// @param constants The constants.
  /// @param jumpTargets The jump labels.
  /// @param functions The functions.
  public CodeType(String name, byte[] code, List<ValueRecord> constants, List<Integer> jumpLabels
      , List <FunctionType> functions) {
    this.name = name;
    this.code = new byte[code.length];
    System.arraycopy(code, 0, this.code, 0, code.length);
    this.constants = constants.toArray(new ValueRecord[0]);
    this.jumpLabels = jumpLabels.stream().mapToInt(i -> i).toArray();
    if (functions == null) {
      this.functions = new FunctionType[0];
    } else {
      this.functions = functions.toArray(new FunctionType[0]);
    }
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

  /// Returns the length of the byte code.
  /// @return The length of the byte code.
  public int codeLength() {
    return code.length;
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

  /// Returns the constants that have been defined.
  /// @return The constants.
  public List<ValueRecord> constants() {
    return List.of(constants);
  }

  /// Returns the jump labels that have been defined.
  /// @return The jump labels.
  public List<Integer> jumpLabels() {
    return Arrays.stream(jumpLabels).boxed().toList();
  }

  /// Returns the functions that have been defined.
  /// @return The functions.
  public List<FunctionType> functions() {
    return List.of(functions);
  }
}
