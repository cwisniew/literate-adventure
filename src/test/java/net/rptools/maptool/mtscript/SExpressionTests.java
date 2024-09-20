package net.rptools.maptool.mtscript;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import net.rptools.maptool.mtscript.parser.SExpressionCompiler;
import net.rptools.maptool.mtscript.vm.MaptoolVM;
import net.rptools.maptool.mtscript.vm.values.NumberType;
import net.rptools.maptool.mtscript.vm.values.StringType;
import org.junit.jupiter.api.Test;

/// Tests for the SExpression class.
public class SExpressionTests {

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


  /// Tests the addition of two numbers.
  @Test
  public void testNumericAddition() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(+ 7 12)", "main");
    var result = vm.exec(code);
    assertInstanceOf(NumberType.class, result);
    assertEquals(19, ((NumberType) result).value());
  }


  /// Tests the addition of multiple numbers.
  @Test
  public void testNestedNumericAddition() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(+ 7 (+ 8 12))", "main");
    var result = vm.exec(code);
    assertInstanceOf(NumberType.class, result);
    assertEquals(27, ((NumberType) result).value());
  }


  /// Tests the subtraction of two numbers.
  @Test
  public void testSubtraction() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(- 7 12)", "main");
    var result = vm.exec(code);
    assertInstanceOf(NumberType.class, result);
    assertEquals(-5, ((NumberType) result).value());
  }

  /// Tests the subtraction of multiple numbers.
  @Test
  public void testNestedSubtraction() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(- 7 (- 8 12))", "main");
    var result = vm.exec(code);
    assertInstanceOf(NumberType.class, result);
    assertEquals(11, ((NumberType) result).value());
  }


  /// Tests the multiplication of two numbers.
  @Test
  public void testMultiplication() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(* 7 12)", "main");
    var result = vm.exec(code);
    assertInstanceOf(NumberType.class, result);
    assertEquals(84, ((NumberType) result).value());
  }


  /// Tests the multiplication of multiple numbers.
  @Test
  public void testNestedMultiplication() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(* 7 (* 8 12))", "main");
    var result = vm.exec(code);
    assertInstanceOf(NumberType.class, result);
    assertEquals(672, ((NumberType) result).value());
  }

  /// Tests the division of two numbers.
  @Test
  public void testDivision() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(/ 77 7)", "main");
    var result = vm.exec(code);
    assertInstanceOf(NumberType.class, result);
    assertEquals(11, ((NumberType) result).value());
  }


  /// Tests the division of multiple numbers.
  @Test
  public void testNestedDivision() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(/ 120 (/ 8 2))", "main");
    var result = vm.exec(code);
    assertInstanceOf(NumberType.class, result);
    assertEquals(30, ((NumberType) result).value());
  }



  /// Tests the addition and subtraction of numbers.
  @Test
  public void testAdditionAndSubtraction() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(- (+ 7 8) 12)", "main");
    var result = vm.exec(code);
    assertInstanceOf(NumberType.class, result);
    assertEquals(3, ((NumberType) result).value());

    compiler = new SExpressionCompiler();
    code = compiler.compile("(+ 12 (- 7 8))", "main");
    result = vm.exec(code);
    assertInstanceOf(NumberType.class, result);
    assertEquals(11, ((NumberType) result).value());
  }

  /// Tests the addition and multiplication of numbers.
  @Test
  public void testAdditionAndMultiplication() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(* (+ 7 8) 12)", "main");
    var result = vm.exec(code);
    assertInstanceOf(NumberType.class, result);
    assertEquals(180, ((NumberType) result).value());

    compiler = new SExpressionCompiler();
    code = compiler.compile("(+ 12 (* 7 8))", "main");
    result = vm.exec(code);
    assertInstanceOf(NumberType.class, result);
    assertEquals(68, ((NumberType) result).value());
  }

  /// Tests the addition and division of numbers.
  @Test
  public void testAdditionAndDivision() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(/ (+ 7 8) 2)", "main");
    var result = vm.exec(code);
    assertInstanceOf(NumberType.class, result);
    assertEquals(7.5, ((NumberType) result).value());

    compiler = new SExpressionCompiler();
    code = compiler.compile("(+ 12 (/ 8 2))", "main");
    result = vm.exec(code);
    assertInstanceOf(NumberType.class, result);
    assertEquals(16, ((NumberType) result).value());
  }

  /// Tests the addition, subtraction, multiplication, and division of numbers.
  @Test
  public void testAddSubMultDiv() {
    MaptoolVM vm = new MaptoolVM();
    var compiler = new SExpressionCompiler();
    var code = compiler.compile("(/ (* (+ 7 8) 12) (- 12 7))", "main");
    var result = vm.exec(code);
    assertInstanceOf(NumberType.class, result);
    assertEquals(36, ((NumberType) result).value());
  }



  // TODO: Add negative tests
}
