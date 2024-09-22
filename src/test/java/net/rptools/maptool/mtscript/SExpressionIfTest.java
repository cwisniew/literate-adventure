package net.rptools.maptool.mtscript;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import net.rptools.maptool.mtscript.parser.SExpressionCompiler;
import net.rptools.maptool.mtscript.vm.MapToolVM;
import net.rptools.maptool.mtscript.vm.VMGlobals;
import net.rptools.maptool.mtscript.vm.values.IntegerType;
import org.junit.jupiter.api.Test;

public class SExpressionIfTest {

  @Test
  public void testIfTrue() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(if true 1 2)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(1, ((IntegerType) result).value());
  }


  @Test
  public void testIfFalse() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(if false 1 2)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(2, ((IntegerType) result).value());
  }

  @Test
  public void testIfIntegerConditions() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(if (< 1 2) 10 20)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(10, ((IntegerType) result).value());

    code = compiler.compile("(if (< 2 1) 10 20)", "main");
    result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(20, ((IntegerType) result).value());

    code = compiler.compile("(if (< 1 1) 10 20)", "main");
    result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(20, ((IntegerType) result).value());

    code = compiler.compile("(if (> 1 2) 10 20)", "main");
    result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(20, ((IntegerType) result).value());
  }

  @Test
  public void testIfNested() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(if true (if true 1 2) 3)", "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(1, ((IntegerType) result).value());

    code = compiler.compile("(if true (if false 1 2) 3)", "main");
    result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(2, ((IntegerType) result).value());

    code = compiler.compile("(if false (if true 1 2) 3)", "main");
    result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(3, ((IntegerType) result).value());
  }
}
