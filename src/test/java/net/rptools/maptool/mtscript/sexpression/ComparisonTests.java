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
package net.rptools.maptool.mtscript.sexpression;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.rptools.maptool.mtscript.parser.SExpressionCompiler;
import net.rptools.maptool.mtscript.vm.MapToolVM;
import net.rptools.maptool.mtscript.vm.VMGlobals;
import net.rptools.maptool.mtscript.vm.values.BooleanType;
import org.junit.jupiter.api.Test;

public class ComparisonTests {

  /// Tests the true boolean.
  @Test
  public void testTrueBoolean() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(true)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());
  }

  /// Tests the false boolean.
  @Test
  public void testFalseBoolean() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(false)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());
  }

  /// Tests the less than operator.
  @Test
  public void testLessThan() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(< 1 2)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());

    code = compiler.compile("(< 2 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());

    code = compiler.compile("(< 1 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());
  }

  /// Tests the greater than operator.
  @Test
  public void testGreaterThan() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(> 1 2)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());

    code = compiler.compile("(> 2 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());

    code = compiler.compile("(> 1 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());
  }

  /// Tests the less than or equal operator.
  @Test
  public void testLessThanOrEqual() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(<= 1 2)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());

    code = compiler.compile("(<= 2 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());

    code = compiler.compile("(<= 1 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());
  }

  /// Tests the greater than or equal operator.
  @Test
  public void testGreaterThanOrEqual() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(>= 1 2)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());

    code = compiler.compile("(>= 2 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());

    code = compiler.compile("(>= 1 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());
  }

  /// Tests the equal operator.
  @Test
  public void testEqual() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(== 1 1)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());

    code = compiler.compile("(== 1 2)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());
  }

  /// Tests the not equal operator.
  @Test
  public void testNotEqual() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(!= 1 1)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());

    code = compiler.compile("(!= 1 2)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());
  }
}
