package net.rptools.maptool.mtscript.parser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.rptools.maptool.mtscript.parser.expr.BinaryOp;
import net.rptools.maptool.mtscript.parser.expr.BooleanValue;
import net.rptools.maptool.mtscript.parser.expr.IntegerValue;
import net.rptools.maptool.mtscript.parser.expr.Op;
import net.rptools.maptool.mtscript.parser.expr.SExpressionExpr;
import net.rptools.maptool.mtscript.parser.expr.SExpressionValue;
import net.rptools.maptool.mtscript.parser.expr.StringValue;
import net.rptools.maptool.mtscript.parser.mtSexpressionParser.AtomContext;
import net.rptools.maptool.mtscript.parser.mtSexpressionParser.ListContext;
import net.rptools.maptool.mtscript.vm.MapToolVMByteCodeBuilder;
import net.rptools.maptool.mtscript.vm.OpCode;
import net.rptools.maptool.mtscript.vm.values.BooleanType;
import net.rptools.maptool.mtscript.vm.values.CodeType;
import net.rptools.maptool.mtscript.vm.values.IntegerType;
import net.rptools.maptool.mtscript.vm.values.StringType;
import net.rptools.maptool.mtscript.vm.values.ValueRecord;

/// A visitor for S-expressions that generates a program.
public class MTSExpressionVisitor extends mtSexpressionParserBaseVisitor<SExpressionExpr> {

  /// The byte code builder.
  private final MapToolVMByteCodeBuilder builder;


  public MTSExpressionVisitor(MapToolVMByteCodeBuilder builder) {
    this.builder = builder;
  }

  /// Adds a constant to the program.
  /// @param constant The constant to add.
  /// @return The index of the constant.
  private int addConstant(ValueRecord constant) {
    return builder.addConstant(constant);
  }

  /// Emits a byte to the byte code stream.
  /// @param value The byte to emit.
  private void emit(Byte value) {
    builder.writeByte(value);
  }

  /// Emits an opcode to the byte code stream.
  /// @param opcode The opcode to emit.
  private void emit(BinaryOp op) {
    switch (op.op()) {
      case "+" -> emit(OpCode.ADD);
      case "-" -> emit(OpCode.SUB);
      case "*" -> emit(OpCode.MUL);
      case "/" -> emit(OpCode.DIV);
      case "<" -> emit(OpCode.LT);
      case ">" -> emit(OpCode.GT);
      case "<=" -> emit(OpCode.LTE);
      case ">=" -> emit(OpCode.GTE);
      case "==" -> emit(OpCode.EQ);
      case "!=" -> emit(OpCode.NEQ);
      default -> throw new RuntimeException("Unknown operator: " + op.op());
    }
  }


  /// Adds an integer constant to the program.
  /// @param value The integer to add.
  /// @return The index of the constant.
  private int addConstant(int value) {
    return addConstant(new IntegerType(value));
  }

  /// Adds a string constant to the program.
  /// @param value The string to add.
  /// @return The index of the constant.
  private int addConstant(String value) {
    return addConstant(new StringType(value));
  }

  /// Adds a boolean constant to the program.
  /// @param value The boolean to add.
  /// @return The index of the constant.
  private int addConstant(boolean value) {
    return addConstant(BooleanType.valueOf(value));
  }

  /// Adds a constant to the program.
  /// @param value The constant to add.
  /// @return The index of the constant.
  private int addConstant(SExpressionExpr value) {
    if (value instanceof IntegerValue iv) {
      return addConstant(iv.value());
    } else if (value instanceof StringValue sv) {
      return addConstant(sv.value());
    } else if (value instanceof BooleanValue bv) {
      return addConstant(bv.value());
    } else {
      throw new RuntimeException("Unknown constant type: " + value); // TODO: CDW
    }
  }

  @Override
  public SExpressionExpr visitAtom(AtomContext ctx) {
    if (ctx.SYMBOL() != null) {
      String symbol = ctx.SYMBOL().getText();
      return switch (symbol) {
        case "+", "-", "*", "/", "<", ">", "<=", ">=", "==", "!=" -> new BinaryOp(symbol);
        case "true" -> new BooleanValue(true);
        case "false" -> new BooleanValue(false);
        case "if" -> new Op(symbol);
        // TODO: CDW Add more operators and variables
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
        if (left instanceof SExpressionValue lval) {
          emit(OpCode.LOAD_CONST);
          emit((byte) addConstant(lval));
        }
        var right = visit(ctx.item(2));
        if (right instanceof SExpressionValue rval) {
          emit(OpCode.LOAD_CONST);
          emit((byte) addConstant(rval));
        }
        emit(bop);
      } else if (op instanceof SExpressionValue) {
        emit(OpCode.LOAD_CONST);
        emit((byte) addConstant((SExpressionExpr) op));
      } else {
        throw new RuntimeException("Unknown operator: " + op); // TODO: CDW
      }
    } else {
      return visitChildren(ctx); // TODO: CDW This should return null/nil/empty list
    }
    return null;
  }
}
