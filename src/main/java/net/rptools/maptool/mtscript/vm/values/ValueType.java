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

/// Represents the type that the name is in the MTScript VM.
public enum ValueType {
  /// Represents a boolean name.
  BOOLEAN,
  /// Represents an integer name.
  INTEGER,
  /// Represents a string name.
  STRING,
  /// Represents a boolean name.
  OBJECT,
  /// Represents code.
  CODE,
  /// Represents a native function.
  NATIVE_FUNCTION,
  /// Represents a function.
  FUNCTION,
  /// Represents a function reference.
  FUNCTION_REF,
}
