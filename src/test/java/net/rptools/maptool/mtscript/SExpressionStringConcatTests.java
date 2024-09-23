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
package net.rptools.maptool.mtscript;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import net.rptools.maptool.mtscript.parser.SExpressionCompiler;
import net.rptools.maptool.mtscript.vm.MapToolVM;
import net.rptools.maptool.mtscript.vm.VMGlobals;
import net.rptools.maptool.mtscript.vm.values.StringType;
import org.junit.jupiter.api.Test;

public class SExpressionStringConcatTests {

  /// Tests the addition of two strings.
  @Test
  public void testStringAddition() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(+ \"Hello \" \"World!\")", "main");
    var result = vm.exec(code);
    assertInstanceOf(StringType.class, result);
    assertEquals("Hello World!", ((StringType) result).value());
  }

  /// Tests the addition of multiple strings.
  @Test
  public void testNestedStringAddition() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(+ \"Hello\" (+ \", \" \"World!\"))", "main");
    var result = vm.exec(code);
    assertInstanceOf(StringType.class, result);
    assertEquals("Hello, World!", ((StringType) result).value());
  }
}
