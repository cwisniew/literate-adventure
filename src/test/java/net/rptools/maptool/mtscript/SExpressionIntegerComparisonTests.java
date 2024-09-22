package net.rptools.maptool.mtscript;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.rptools.maptool.mtscript.parser.SExpressionCompiler;
import net.rptools.maptool.mtscript.vm.MapToolVM;
import net.rptools.maptool.mtscript.vm.VMGlobals;
import net.rptools.maptool.mtscript.vm.values.BooleanType;
import org.junit.jupiter.api.Test;

public class SExpressionIntegerComparisonTests {


  /// Tests the true boolean.
  @Test
  public void testTrueBoolean() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(true)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());
  }

  /// Tests the false boolean.
  @Test
  public void testFalseBoolean() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(false)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());
  }

  /// Tests the less than operator.
  @Test
  public void testLessThan() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(< 1 2)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());

    code = compiler.compile("(< 2 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());

    code = compiler.compile("(< 1 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());
  }

  /// Tests the greater than operator.
  @Test
  public void testGreaterThan() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(> 1 2)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());

    code = compiler.compile("(> 2 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());

    code = compiler.compile("(> 1 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());
  }

  /// Tests the less than or equal operator.
  @Test
  public void testLessThanOrEqual() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(<= 1 2)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());

    code = compiler.compile("(<= 2 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());

    code = compiler.compile("(<= 1 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());
  }

  /// Tests the greater than or equal operator.
  @Test
  public void testGreaterThanOrEqual() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(>= 1 2)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());

    code = compiler.compile("(>= 2 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());

    code = compiler.compile("(>= 1 1)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());
  }

  /// Tests the equal operator.
  @Test
  public void testEqual() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(== 1 1)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());

    code = compiler.compile("(== 1 2)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());
  }

  /// Tests the not equal operator.
  @Test
  public void testNotEqual() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(!= 1 1)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());

    code = compiler.compile("(!= 1 2)", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());
  }

}
