package net.rptools.maptool.mtscript;

import net.rptools.maptool.mtscript.parser.SExpressionCompiler;
import net.rptools.maptool.mtscript.vm.MaptoolVM;

/// The main class for the MapTool scripting language.
/// Really only used for testing during development.
public class Main {

  /// The main entry point.
  public static void main(String[] args) {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    //var code = compiler.compile("(+ 7 (+ 8 12))", "main");
    var code = compiler.compile("(+ \"Hello\" (+ \", \" \"World!\"))", "main");
    var result = vm.exec(code);
    System.out.println("done, result = " + result); // TODO: remove
  }
}