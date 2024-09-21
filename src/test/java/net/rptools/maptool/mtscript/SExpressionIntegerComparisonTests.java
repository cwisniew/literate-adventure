package net.rptools.maptool.mtscript;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.rptools.maptool.mtscript.parser.SExpressionCompiler;
import net.rptools.maptool.mtscript.vm.MaptoolVM;
import net.rptools.maptool.mtscript.vm.values.BooleanType;
import org.junit.jupiter.api.Test;

public class SExpressionIntegerComparisonTests {


  /// Tests the true boolean.
  @Test
  public void testTrueBoolean() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(true)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());
  }

  /// Tests the false boolean.
  @Test
  public void testFalseBoolean() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(false)", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());
  }

  /// Tests the less than operator.
  @Test
  public void testLessThan() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
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
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
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
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
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
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
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
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
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
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
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
