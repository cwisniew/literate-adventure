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
    return value.add(other);
  }

  @Override
  public ValueRecord subtract(ValueRecord other) {
    return value.subtract(other);
  }

  @Override
  public ValueRecord multiply(ValueRecord other) {
    return value.multiply(other);
  }

  @Override
  public ValueRecord divide(ValueRecord other) {
    return value.divide(other);
  }

  @Override
  public BooleanType compareEqual(ValueRecord other) {
    return value.compareEqual(other);
  }

  @Override
  public BooleanType compareNotEqual(ValueRecord other) {
    return value.compareNotEqual(other);
  }

  @Override
  public BooleanType compareLessThan(ValueRecord other) {
    return value.compareLessThan(other);
  }

  @Override
  public BooleanType compareLessThanOrEqual(ValueRecord other) {
    return value.compareLessThanOrEqual(other);
  }

  @Override
  public BooleanType compareGreaterThan(ValueRecord other) {
    return value.compareGreaterThan(other);
  }

  @Override
  public BooleanType compareGreaterThanOrEqual(ValueRecord other) {
    return value.compareGreaterThanOrEqual(other);
  }

}
