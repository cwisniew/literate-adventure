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

/// Represents a variable in the MTScript VM.
/// Variables are used to store values that can be modified.
/// @param name The name of the variable.
/// @param name The name of the variable.
public record Symbol(String name, ValueRecord value) implements ValueRecord {
  @Override
  public ValueType valueType() {
    return value.valueType();
  }

  @Override
  public ValueRecord add(ValueRecord other) {
    return ValueRecord.super.add(other);
  }

  @Override
  public ValueRecord subtract(ValueRecord other) {
    return ValueRecord.super.subtract(other);
  }

  @Override
  public ValueRecord multiply(ValueRecord other) {
    return ValueRecord.super.multiply(other);
  }

  @Override
  public ValueRecord divide(ValueRecord other) {
    return ValueRecord.super.divide(other);
  }

  @Override
  public BooleanType compareEqual(ValueRecord other) {
    return ValueRecord.super.compareEqual(other);
  }

  @Override
  public BooleanType compareNotEqual(ValueRecord other) {
    return ValueRecord.super.compareNotEqual(other);
  }

  @Override
  public BooleanType compareLessThan(ValueRecord other) {
    return ValueRecord.super.compareLessThan(other);
  }

  @Override
  public BooleanType compareLessThanOrEqual(ValueRecord other) {
    return ValueRecord.super.compareLessThanOrEqual(other);
  }

  @Override
  public BooleanType compareGreaterThan(ValueRecord other) {
    return ValueRecord.super.compareGreaterThan(other);
  }

  @Override
  public BooleanType compareGreaterThanOrEqual(ValueRecord other) {
    return ValueRecord.super.compareGreaterThanOrEqual(other);
  }
}
