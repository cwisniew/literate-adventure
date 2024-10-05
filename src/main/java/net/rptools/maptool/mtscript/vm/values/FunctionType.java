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

import java.util.List;

/// Represents a function type in the MTScript VM.
public class FunctionType extends CodeType {

  /// The arity of the function.
  private final int arity;


  /// Creates a new function type.
  public FunctionType(
      String name, byte[] code, List<ValueRecord> constants, List<Integer> jumpLabels, int arity) {
    super(name, code, constants, jumpLabels, null);
    this.arity = arity;
  }

  @Override
  public ValueType valueType() {
    return ValueType.FUNCTION;
  }

  /// Returns the arity of the function.
  /// @return The arity of the function.
  public int arity() {
    return arity;
  }

  @Override
  public String toString() {
    return "FunctionType[" + name() + "]";
  }
}
