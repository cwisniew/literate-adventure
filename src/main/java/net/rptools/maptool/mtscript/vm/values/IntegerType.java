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

/// Represents a number name in the MTScript VM.
/// @param name The name name.
public record IntegerType(double value) implements ValueRecord {
  @Override
  public ValueType valueType() {
    return ValueType.INTEGER;
  }

  @Override
  public IntegerType add(ValueRecord other) {
    if (other instanceof IntegerType otherNumber) {
      return new IntegerType(value + otherNumber.value());
    }
    throw new IllegalArgumentException(
        "Cannot add a " + other.valueType() + " to an " + valueType()); // TODO: CDW
  }

  @Override
  public ValueRecord subtract(ValueRecord other) {
    if (other instanceof IntegerType otherNumber) {
      return new IntegerType(value - otherNumber.value());
    }
    throw new IllegalArgumentException(
        "Cannot add a " + other.valueType() + " to an " + valueType()); // TODO: CDW
  }

  @Override
  public ValueRecord multiply(ValueRecord other) {
    if (other instanceof IntegerType otherNumber) {
      return new IntegerType(value * otherNumber.value());
    }
    throw new IllegalArgumentException(
        "Cannot add a " + other.valueType() + " to an " + valueType()); // TODO: CDW
  }

  @Override
  public ValueRecord divide(ValueRecord other) {
    if (other instanceof IntegerType otherNumber) {
      return new IntegerType(value / otherNumber.value());
    }
    throw new IllegalArgumentException(
        "Cannot add a " + other.valueType() + " to an " + valueType()); // TODO: CDW
  }

  @Override
  public BooleanType compareEqual(ValueRecord other) {
    if (other instanceof IntegerType otherNumber) {
      return BooleanType.valueOf(value == otherNumber.value());
    }
    throw new IllegalArgumentException(
        "Cannot add a " + other.valueType() + " to an " + valueType()); // TODO: CDW
  }

  @Override
  public BooleanType compareNotEqual(ValueRecord other) {
    return compareEqual(other).not();
  }

  @Override
  public BooleanType compareLessThan(ValueRecord other) {
    if (other instanceof IntegerType otherNumber) {
      return BooleanType.valueOf(value < otherNumber.value());
    }
    throw new IllegalArgumentException(
        "Cannot add a " + other.valueType() + " to an " + valueType()); // TODO: CDW
  }

  @Override
  public BooleanType compareLessThanOrEqual(ValueRecord other) {
    if (other instanceof IntegerType otherNumber) {
      return BooleanType.valueOf(value <= otherNumber.value());
    }
    throw new IllegalArgumentException(
        "Cannot add a " + other.valueType() + " to an " + valueType()); // TODO: CDW
  }

  @Override
  public BooleanType compareGreaterThan(ValueRecord other) {
    if (other instanceof IntegerType otherNumber) {
      return BooleanType.valueOf(value > otherNumber.value());
    }
    throw new IllegalArgumentException(
        "Cannot add a " + other.valueType() + " to an " + valueType()); // TODO: CDW
  }

  @Override
  public BooleanType compareGreaterThanOrEqual(ValueRecord other) {
    if (other instanceof IntegerType otherNumber) {
      return BooleanType.valueOf(value >= otherNumber.value());
    }
    throw new IllegalArgumentException(
        "Cannot add a " + other.valueType() + " to an " + valueType()); // TODO: CDW
  }
}
