package net.rptools.maptool.mtscript;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import net.rptools.maptool.mtscript.parser.SExpressionCompiler;
import net.rptools.maptool.mtscript.vm.MaptoolVM;
import net.rptools.maptool.mtscript.vm.values.StringType;
import org.junit.jupiter.api.Test;

public class SExpressionStringConcatTests {


  /// Tests the addition of two strings.
  @Test
  public void testStringAddition() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(+ \"Hello \" \"World!\")", "main");
    var result = vm.exec(code);
    assertInstanceOf(StringType.class, result);
    assertEquals("Hello World!", ((StringType) result).value());
  }

  /// Tests the addition of multiple strings.
  @Test
  public void testNestedStringAddition() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(+ \"Hello\" (+ \", \" \"World!\"))", "main");
    var result = vm.exec(code);
    assertInstanceOf(StringType.class, result);
    assertEquals("Hello, World!", ((StringType) result).value());
  }


}
