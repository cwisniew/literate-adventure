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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Debug {
  private static final Logger log = LogManager.getLogger(Debug.class);

  /// Returns the absolute value of the given number.
  /// abs(n) -> n
  /// @param vm The VM to operate on
  @MTVMFunction(name = "_vm_debug", arity = 1)
  public static void debug(MapToolVM vm) {
    var functionUtils = new FunctionUtils(vm);
    var arg = functionUtils.getArgs(1)[0];
    log.debug("Debug: {}", arg);
    functionUtils.returnResult(arg);
  }
}
