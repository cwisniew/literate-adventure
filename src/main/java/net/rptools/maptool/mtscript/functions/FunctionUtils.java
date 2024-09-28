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
package net.rptools.maptool.mtscript.functions;

import net.rptools.maptool.mtscript.vm.MapToolVM;
import net.rptools.maptool.mtscript.vm.values.ValueRecord;


/// Utility functions for MapToolVM functions.
public class FunctionUtils {

  /// The VM to operate on.
  private final MapToolVM vm;

  /// Creates a new function utility.
  public FunctionUtils(MapToolVM vm) {
    this.vm = vm;
  }

  /// Gets the arguments from the VM.
  /// Arguments for the function are the top `count` values on the stack.
  /// These arguments are not removed from the stack.
  /// @param count The number of arguments to get.
  /// @return The arguments.
  public ValueRecord[] getArgs(int count) {
    ValueRecord[] args = new ValueRecord[count];
    for (int i = 0; i < count;  i++) {
      args[i] = vm.peek(i);
    }
    return args;
  }

  /// Sets the return value of the function.
  /// This will add the value to the stack.
  /// @param result The result to return.
  public void returnResult(ValueRecord result) {
    vm.push(result);
  }
}
