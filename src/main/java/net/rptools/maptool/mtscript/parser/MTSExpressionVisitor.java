package net.rptools.maptool.mtscript.parser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.rptools.maptool.mtscript.parser.expr.BinaryOp;
import net.rptools.maptool.mtscript.parser.expr.IntegerValue;
import net.rptools.maptool.mtscript.parser.expr.SExpressionExpr;
import net.rptools.maptool.mtscript.parser.expr.StringValue;
import net.rptools.maptool.mtscript.parser.mtSexpressionParser.AtomContext;
import net.rptools.maptool.mtscript.parser.mtSexpressionParser.ListContext;
import net.rptools.maptool.mtscript.vm.OpCode;
import net.rptools.maptool.mtscript.vm.values.CodeType;
import net.rptools.maptool.mtscript.vm.values.NumberType;
import net.rptools.maptool.mtscript.vm.values.StringType;
import net.rptools.maptool.mtscript.vm.values.ValueRecord;

/// A visitor for S-expressions that generates a program.
public class MTSExpressionVisitor extends mtSexpressionParserBaseVisitor<SExpressionExpr> {

  /// The byte code stream.
  private final ByteArrayOutputStream codeStream = new ByteArrayOutputStream();

  /// The list of constants.
  private final List<ValueRecord> constants = new ArrayList<>();

  /// The map of constants to their index.
  private final Map<ValueRecord, Integer> constantIndexMap = new HashMap<>();

  /// Returns the compiled program.
  /// @param name The name to attribute to the program.
  public CodeType getProgram(String name) {
    emit(OpCode.HALT);
    return new CodeType(name, codeStream.toByteArray(), constants);
  }

  /// Adds a constant to the program.
  /// @param constant The constant to add.
  /// @return The index of the constant.
  private int addConstant(ValueRecord constant) {
    if (constantIndexMap.containsKey(constant)) {
      return constantIndexMap.get(constant);
    }
    constants.add(constant);
    int ind =  constants.size() - 1;
    constantIndexMap.put(constant, ind);
    return ind;
  }

  /// Emits a byte to the byte code stream.
  /// @param value The byte to emit.
  private void emit(Byte value) {
    codeStream.write(value);
  }

  /// Emits an opcode to the byte code stream.
  /// @param opcode The opcode to emit.
  private void emit(BinaryOp op) {
    switch (op.op()) {
      case "+" -> emit(OpCode.ADD);
      case "-" -> emit(OpCode.SUB);
      case "*" -> emit(OpCode.MUL);
      case "/" -> emit(OpCode.DIV);
      default -> throw new RuntimeException("Unknown operator: " + op.op());
    }
  }


  /// Adds an integer constant to the program.
  /// @param value The integer to add.
  /// @return The index of the constant.
  private int addConstant(int value) {
    return addConstant(new NumberType(value));
  }

  /// Adds a string constant to the program.
  /// @param value The string to add.
  /// @return The index of the constant.
  private int addConstant(String value) {
    return addConstant(new StringType(value));
  }

  /// Adds a constant to the program.
  /// @param value The constant to add.
  /// @return The index of the constant.
  private int addConstant(SExpressionExpr value) {
    if (value instanceof IntegerValue iv) {
      return addConstant(iv.value());
    } else if (value instanceof StringValue sv) {
      return addConstant(sv.value());
    } else {
      throw new RuntimeException("Unknown constant type: " + value); // TODO: CDW
    }
  }

  @Override
  public SExpressionExpr visitAtom(AtomContext ctx) {
    if (ctx.SYMBOL() != null) {
      return switch (ctx.SYMBOL().getText()) {
        case "+" -> new BinaryOp("+");
        case "-" -> new BinaryOp("-");
        case "*" -> new BinaryOp("*");
        case "/" -> new BinaryOp("/");
        // TODO: CDW Add more operators
        default -> throw new RuntimeException("Unknown operator: " + ctx.SYMBOL().getText());
      };
    } else if (ctx.INTEGER_LITERAL() != null) {
      return new IntegerValue(Integer.parseInt(ctx.INTEGER_LITERAL().getText()));
    } else if (ctx.STRING_LITERAL() != null) {
      String val = ctx.STRING_LITERAL().getText().replaceFirst("^\"", "").replaceFirst("\"$", "");
      return new StringValue(val);
    } else {
      throw new RuntimeException("Unknown atom: " + ctx.getText()); // TODO: CDW
    }
  }

  @Override
  public SExpressionExpr visitList(ListContext ctx) {
    if (!ctx.item().isEmpty()) {
      var op = visit(ctx.item(0));
      if (op instanceof BinaryOp bop) {
        var left = visit(ctx.item(1));
        if (left instanceof IntegerValue || left instanceof StringValue) {
          emit(OpCode.CONST);
          emit((byte) addConstant((SExpressionExpr) left));
        }
        var right = visit(ctx.item(2));
        if (right instanceof IntegerValue || right instanceof StringValue) {
          emit(OpCode.CONST);
          emit((byte) addConstant((SExpressionExpr) right));
        }
        emit (bop);
      } else {
        throw new RuntimeException("Unknown operator: " + op); // TODO: CDW
      }
    } else {
      return visitChildren(ctx); // TODO: CDW This should return null/nil/empty list
    }
    return null;
  }
}
