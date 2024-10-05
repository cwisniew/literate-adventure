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
import net.rptools.maptool.mtscript.vm.values.FunctionType;
import net.rptools.maptool.mtscript.vm.values.IntegerType;
import net.rptools.maptool.mtscript.vm.values.StringType;
import org.junit.jupiter.api.Test;

public class FunctionTests {

  @Test
  public void testNativeFunction() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(_vm_debug \"Test\")", "main");
    var result = vm.exec(code);
    assertInstanceOf(StringType.class, result);
    assertEquals("Test", ((StringType) result).value());
  }

  @Test
  public void testDefineFunction() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(def square (x) (* x x))", "main");
    var result = vm.exec(code);
    assertInstanceOf(FunctionType.class, result);
    assertEquals("square", result.name());
  }

  @Test
  public void testCallFunction() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(def square (x) (* x x)) (square 5)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(25, ((IntegerType) result).value());
  }

  @Test
  public void testCallFunctionWithBlock() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(def square (x) (block (* x x)) (square 5)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(25, ((IntegerType) result).value());
  }

  @Test
  public void testCallFunction2Args() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(def add (x y) (+ x y)) (add 5 6)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(11, ((IntegerType) result).value());
  }

  @Test
  public void testCallFunction2ArgsWithBlock() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(def add (x y) (block (+ x y))) (add 5 6)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(11, ((IntegerType) result).value());
  }


  @Test
  public void testRecursiveFunction() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(def fact (n) (if (== n 0) 1 (* n (fact (- n 1))))) (fact 5)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(120, ((IntegerType) result).value());
  }
}
