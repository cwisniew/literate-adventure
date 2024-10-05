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

/// Represents a string name in the MTScript VM.
/// @param name The string name.
public record StringType(String value) implements ValueRecord {
  @Override
  public ValueType valueType() {
    return ValueType.STRING;
  }

  @Override
  public String name() {
    return value;
  }

  @Override
  public StringType add(ValueRecord other) {
    if (other instanceof StringType otherString) {
      return new StringType(value + otherString.value());
    }
    throw new IllegalArgumentException("Cannot add a string to a non-string"); // TODO: CDW
  }

  @Override
  public BooleanType compareLessThan(ValueRecord other) {
    if (other instanceof StringType otherString) {
      return BooleanType.valueOf(value.compareTo(otherString.value) < 0);
    }
    throw new IllegalArgumentException("Cannot compare a string to a non-string"); // TODO: CDW
  }

  @Override
  public BooleanType compareLessThanOrEqual(ValueRecord other) {
    if (other instanceof StringType otherString) {
      return BooleanType.valueOf(value.compareTo(otherString.value) <= 0);
    }
    throw new IllegalArgumentException("Cannot compare a string to a non-string"); // TODO: CDW
  }

  @Override
  public BooleanType compareGreaterThan(ValueRecord other) {
    if (other instanceof StringType otherString) {
      return BooleanType.valueOf(value.compareTo(otherString.value) > 0);
    }
    throw new IllegalArgumentException("Cannot compare a string to a non-string"); // TODO: CDW
  }

  @Override
  public BooleanType compareGreaterThanOrEqual(ValueRecord other) {
    if (other instanceof StringType otherString) {
      return BooleanType.valueOf(value.compareTo(otherString.value) >= 0);
    }
    throw new IllegalArgumentException("Cannot compare a string to a non-string"); // TODO: CDW
  }
}
