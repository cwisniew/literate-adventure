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
