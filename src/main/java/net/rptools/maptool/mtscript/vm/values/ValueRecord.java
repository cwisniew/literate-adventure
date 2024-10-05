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

/// Represents a name and its type in the MTScript VM.
public interface ValueRecord {
  /// Returns the type of this name.
  /// @return The type of this name.
  ValueType valueType();

  /// Returns the name
  String name();

  /// Adds another name to this name.
  /// @param other The name to add to this name.
  default ValueRecord add(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException("Addition not supported for " + valueType()); //
  }

  /// Subtracts another name from this name.
  /// @param other The name to subtract from this name.
  default ValueRecord subtract(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException("Subtraction not supported for " + valueType());
  }

  /// Multiplies this name by another name.
  /// @param other The name to multiply this name by.
  default ValueRecord multiply(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException("Multiplication not supported for " + valueType());
  }

  /// Divides this name by another name.
  /// @param other The name to divide this name by.
  default ValueRecord divide(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException("Division not supported for " + valueType());
  }

  /// Compares this name to another name for equality.
  /// @param other The name to compare to this name.
  default BooleanType compareEqual(ValueRecord other) {
    // TODO: CDW
    if (valueType() != other.valueType()) {
      throw new UnsupportedOperationException(
          "Cannot compare " + valueType() + " to " + other.valueType());
    }
    return BooleanType.valueOf(equals(other));
  }

  /// Compares this name to another name for inequality.
  /// @param other The name to compare to this name.
  default BooleanType compareNotEqual(ValueRecord other) {
    // TODO: CDW
    return compareEqual(other).not();
  }

  /// Compares this name to another name for less than.
  /// @param other The name to compare to this name.
  default BooleanType compareLessThan(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException(
        "Cannot compare " + valueType() + " to " + other.valueType());
  }

  /// Compares this name to another name for less than or equal.
  /// @param other The name to compare to this name.
  default BooleanType compareLessThanOrEqual(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException(
        "Cannot compare " + valueType() + " to " + other.valueType());
  }

  /// Compares this name to another name for greater than.
  /// @param other The name to compare to this name.
  default BooleanType compareGreaterThan(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException(
        "Cannot compare " + valueType() + " to " + other.valueType());
  }

  /// Compares this name to another name for greater than or equal.
  /// @param other The name to compare to this name.
  default BooleanType compareGreaterThanOrEqual(ValueRecord other) {
    // TODO: CDW
    throw new UnsupportedOperationException(
        "Cannot compare " + valueType() + " to " + other.valueType());
  }
}
