package net.rptools.maptool.mtscript.sexpression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.IOException;
import net.rptools.maptool.mtscript.parser.SExpressionCompiler;
import net.rptools.maptool.mtscript.vm.MapToolVM;
import net.rptools.maptool.mtscript.vm.VMGlobals;
import net.rptools.maptool.mtscript.vm.values.IntegerType;
import org.junit.jupiter.api.Test;

public class ScopeTest {

  @Test
  public void testScope() throws IOException {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var program = SExpressionTestUtil.readSExpressionTestFile("local_var.mtsx");
    var code = compiler.compile(program, "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(49, ((IntegerType) result).value());
  }

  @Test
  public void testScopeWithGlobal() throws IOException {
    var globals = new VMGlobals();
    MapToolVM vm = new MapToolVM(globals);
    var compiler = new SExpressionCompiler(globals);
    var program = SExpressionTestUtil.readSExpressionTestFile("local_var_with_global.mtsx");
    var code = compiler.compile(program, "main");
    var result = vm.exec(code);
    assertInstanceOf(IntegerType.class, result);
    assertEquals(225, ((IntegerType) result).value());
  }
}
