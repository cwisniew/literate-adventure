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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import net.rptools.maptool.mtscript.parser.SExpressionCompiler;
import net.rptools.maptool.mtscript.vm.MapToolVM;
import net.rptools.maptool.mtscript.vm.VMGlobals;
import net.rptools.maptool.mtscript.vm.values.IntegerType;
import org.junit.jupiter.api.Test;

/// Tests for the SExpression class.
public class IntegerMathTests {

  /// Tests the addition of two numbers.
  @Test
  public void testIntegerAddition() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(+ 7 12)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(19, ((IntegerType) result).value());
  }

  /// Tests the addition of multiple numbers.
  @Test
  public void testNestedIntegerAddition() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(+ 7 (+ 8 12))", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(27, ((IntegerType) result).value());
  }

  /// Tests the subtraction of two numbers.
  @Test
  public void testSubtraction() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(- 7 12)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(-5, ((IntegerType) result).value());
  }

  /// Tests the subtraction of multiple numbers.
  @Test
  public void testNestedSubtraction() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(- 7 (- 8 12))", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(11, ((IntegerType) result).value());
  }

  /// Tests the multiplication of two numbers.
  @Test
  public void testMultiplication() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(* 7 12)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(84, ((IntegerType) result).value());
  }

  /// Tests the multiplication of multiple numbers.
  @Test
  public void testNestedMultiplication() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(* 7 (* 8 12))", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(672, ((IntegerType) result).value());
  }

  /// Tests the division of two numbers.
  @Test
  public void testDivision() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(/ 77 7)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(11, ((IntegerType) result).value());
  }

  /// Tests the division of multiple numbers.
  @Test
  public void testNestedDivision() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(/ 120 (/ 8 2))", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(30, ((IntegerType) result).value());
  }

  /// Tests the addition and subtraction of numbers.
  @Test
  public void testAdditionAndSubtraction() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(- (+ 7 8) 12)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(3, ((IntegerType) result).value());

    compiler = new SExpressionCompiler(globals);
    code = compiler.compile("(+ 12 (- 7 8))", "main");
    result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(11, ((IntegerType) result).value());
  }

  /// Tests the addition and multiplication of numbers.
  @Test
  public void testAdditionAndMultiplication() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(* (+ 7 8) 12)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(180, ((IntegerType) result).value());

    compiler = new SExpressionCompiler(globals);
    code = compiler.compile("(+ 12 (* 7 8))", "main");
    result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(68, ((IntegerType) result).value());
  }

  /// Tests the addition and division of numbers.
  @Test
  public void testAdditionAndDivision() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(/ (+ 7 8) 2)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(7.5, ((IntegerType) result).value());

    compiler = new SExpressionCompiler(globals);
    code = compiler.compile("(+ 12 (/ 8 2))", "main");
    result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(16, ((IntegerType) result).value());
  }

  /// Tests the addition, subtraction, multiplication, and division of numbers.
  @Test
  public void testAddSubMultDiv() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(/ (* (+ 7 8) 12) (- 12 7))", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(36, ((IntegerType) result).value());
  }

  // TODO: Add negative tests
}
