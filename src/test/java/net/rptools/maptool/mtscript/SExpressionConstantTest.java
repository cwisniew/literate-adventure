package net.rptools.maptool.mtscript;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import net.rptools.maptool.mtscript.parser.SExpressionCompiler;
import net.rptools.maptool.mtscript.vm.MapToolVM;
import net.rptools.maptool.mtscript.vm.VMGlobals;
import net.rptools.maptool.mtscript.vm.values.IntegerType;
import net.rptools.maptool.mtscript.vm.values.StringType;
import org.junit.jupiter.api.Test;

public class SExpressionConstantTest {

  @Test
  public void testSExpressionConstantInteger() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(2)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(2, ((IntegerType) result).value());

    code = compiler.compile("(42)", "main");
    result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(42, ((IntegerType) result).value());
  }

  @Test
  public void testSExpressionConstantString() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(\"hello\")", "main");
    var result = vm.exec(code);
    assertInstanceOf(StringType.class, result);
    assertEquals("hello", ((StringType)result).value());

    code = compiler.compile("(\"world\")", "main");
    result = vm.exec(code);
    assertInstanceOf(StringType.class, result);
    assertEquals("world", ((StringType) result).value());
  }

}
