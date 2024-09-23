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

/// Represents a boolean name in the MTScript VM.
/// @param name The name of the boolean.
public class BooleanType implements ValueRecord {

  /// Represents the true boolean name.
  public static final BooleanType TRUE = new BooleanType(true);

  /// Represents the false boolean name.
  public static final BooleanType FALSE = new BooleanType(false);

  /// The name of the boolean.
  private final boolean value;

  /// Creates a new boolean name.
  /// @param name The name of the boolean.
  private BooleanType(boolean value) {
    this.value = value;
  }

  /// Returns the boolean name of the given boolean.
  /// @param name The boolean name.
  public static BooleanType valueOf(boolean value) {
    return value ? TRUE : FALSE;
  }

  /// Returns the name of the boolean.
  public boolean value() {
    return value;
  }

  @Override
  public ValueType valueType() {
    return ValueType.BOOLEAN;
  }

  /// Returns the result of the logical AND operation between this boolean and another boolean.
  /// @param other The other boolean.
  public BooleanType not() {
    return value ? FALSE : TRUE;
  }

  @Override
  public String toString() {
    return "BooleanType[name=" + value + ']';
  }
}
