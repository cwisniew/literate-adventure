package net.rptools.maptool.mtscript;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.rptools.maptool.mtscript.parser.SExpressionCompiler;
import net.rptools.maptool.mtscript.vm.MapToolVM;
import net.rptools.maptool.mtscript.vm.VMGlobals;
import net.rptools.maptool.mtscript.vm.values.BooleanType;
import org.junit.jupiter.api.Test;

public class SExpressionStringComparisonTests {

  /// Tests the equality of two strings.
  @Test
  public void testStringEquality() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(== \"Hello\" \"Hello\")", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());

    code = compiler.compile("(== \"Hello\" \"World\")", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());
  }

  /// Tests the inequality of two strings.
  @Test
  public void testStringInequality() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(!= \"Hello\" \"Hello\")", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());

    code = compiler.compile("(!= \"Hello\" \"World\")", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());
  }

  /// Tests the less than operator.
  @Test
  public void testLessThan() {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var code = compiler.compile("(< \"a\" \"b\")", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());

    code = compiler.compile("(< \"b\" \"a\")", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());

    code = compiler.compile("(< \"a\" \"a\")", "main");
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
    var code = compiler.compile("(> \"a\" \"b\")", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());

    code = compiler.compile("(> \"b\" \"a\")", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());

    code = compiler.compile("(> \"a\" \"a\")", "main");
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
    var code = compiler.compile("(<= \"a\" \"b\")", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());

    code = compiler.compile("(<= \"b\" \"a\")", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());

    code = compiler.compile("(<= \"a\" \"a\")", "main");
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
    var code = compiler.compile("(>= \"a\" \"b\")", "main");
    var result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertFalse(((BooleanType) result).value());

    code = compiler.compile("(>= \"b\" \"a\")", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());

    code = compiler.compile("(>= \"a\" \"a\")", "main");
    result = vm.exec(code);
    assertInstanceOf(BooleanType.class, result);
    assertTrue(((BooleanType) result).value());
  }

}
