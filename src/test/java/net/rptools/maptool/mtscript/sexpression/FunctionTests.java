package net.rptools.maptool.mtscript.sexpression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import net.rptools.maptool.mtscript.parser.SExpressionCompiler;
import net.rptools.maptool.mtscript.vm.MapToolVM;
import net.rptools.maptool.mtscript.vm.VMGlobals;
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
  public void testFunction() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(def square (x) (* x x))", "main");
    var result = vm.exec(code);
    assertInstanceOf(StringType.class, result);
    assertEquals("Test", ((StringType) result).value());
  }


}