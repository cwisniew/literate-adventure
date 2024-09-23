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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.rptools.maptool.mtscript.parser.SExpressionCompiler;
import net.rptools.maptool.mtscript.vm.MapToolVM;
import net.rptools.maptool.mtscript.vm.VMGlobals;
import net.rptools.maptool.mtscript.vm.assembler.Disassembler;

/// The main class for the MapTool scripting language.
/// Really only used for testing during development.
public class Main {

  /// The main entry point.
  public static void main(String[] args) throws IOException {
    if (args.length < 1) {
      System.out.println("Usage: <filename>");
      System.out.println("Usage: -p <program>");
      System.exit(1);
    }
    String program = "";
    if (args.length > 1) {
      if (args[0].equals("-p")) {
        program = args[1];
      } else {
        System.out.println("Usage: -p <program>");
        System.exit(1);
      }
    } else {
      program = Files.readString(Path.of(args[0]));
    }
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile(program, "main");
    var disassembler = new Disassembler(code, globals);
    System.out.println();
    disassembler.disassemble(System.out);
    System.out.println();
    var result = vm.exec(code);
    System.out.println("done, result = " + result); // TODO: remove
  }
}
