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
      System.exit(1);
    }
    String program = Files.readString(Path.of(args[0]));
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile(program, "main");
    var result = vm.exec(code);
    System.out.println("done, result = " + result); // TODO: remove
    System.out.println();
    var disassembler = new Disassembler(code, globals);
    disassembler.disassemble(System.out);
  }
}