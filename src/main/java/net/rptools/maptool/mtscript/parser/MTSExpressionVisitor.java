package net.rptools.maptool.mtscript.parser;

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
import net.rptools.maptool.mtscript.vm.values.ValueRecord;

/// A visitor for S-expressions that generates a program.
public class MTSExpressionVisitor extends mtSexpressionParserBaseVisitor<SExpressionExpr> {

  /// The byte code builder.
  private final MapToolVMByteCodeBuilder builder;


  /// Creates a new expression visitor.
  /// @param builder The byte code builder.
  public MTSExpressionVisitor(MapToolVMByteCodeBuilder builder) {
    this.builder = builder;
  }

  /// Emits an opcode to the byte code stream.
  /// @param opcode The opcode to emit.
  private void emit(BinaryOp op) {
    switch (op.op()) {
      case "+" -> builder.emitAdd();
      case "-" -> builder.emitSubtract();
      case "*" -> builder.emitMultiply();
      case "/" -> builder.emitDivide();
      case "<" -> builder.emitLessThan();
      case ">" -> builder.emitGreaterThan();
      case "<=" -> builder.emitLessThanOrEqual();
      case ">=" -> builder.emitGreaterThanOrEqual();
      case "==" -> builder.emitEqual();
      case "!=" -> builder.emitNotEqual();
      default -> throw new RuntimeException("Unknown operator: " + op.op());
    }
  }


  /// Emits a load constant instruction.
  /// @param constant The constant to load.
  private void emitLoadConstant(SExpressionValue value) {
    if (value instanceof IntegerValue iv) {
      builder.emitLoadConstant(iv.value());
    } else if (value instanceof StringValue sv) {
      builder.emitLoadConstant(sv.value());
    } else if (value instanceof BooleanValue bv) {
      builder.emitLoadConstant(bv.value());
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
      var nextOp = visit(ctx.item(0));
      if (nextOp instanceof BinaryOp bop) { // Handle binary operators
        handleBinaryOp(ctx, bop);
      } else if (nextOp instanceof SExpressionValue) {  // Handle constants
        emitLoadConstant((SExpressionValue) nextOp);
      } else if (nextOp instanceof Op op) {  // Handle special operators
        if (op.op().equals("if")) {
          handleIf(ctx);
        } else {
          throw new RuntimeException("Unknown operator: " + op.op()); // TODO: CDW
        }
      } else {
        throw new RuntimeException("Unknown operator: " + nextOp); // TODO: CDW
      }
    } else {
      return visitChildren(ctx); // TODO: CDW This should return null/nil/empty list
    }
    return null;
  }

  /// Handles an if statement.
  /// @param ctx The context.
  private void handleIf(ListContext ctx) {
    var condition = visit(ctx.item(1));
    if (condition instanceof SExpressionValue cval) {
      emitLoadConstant(cval);
    }
    int elseLabel = builder.allocateJumpLabel();
    builder.emitJumpIfFalse(elseLabel);
    var trueBranch = visit(ctx.item(2));
    if (trueBranch instanceof SExpressionValue tval) {
      emitLoadConstant(tval);
    }
    int endLabel = builder.allocateJumpLabel();
    builder.emitJump(endLabel);
    builder.setJumpLabel(elseLabel);
    var falseBranch = visit(ctx.item(3));
    if (falseBranch instanceof SExpressionValue fval) {
      emitLoadConstant(fval);
    }
    builder.setJumpLabel(endLabel);
  }

  /// Handles a binary operator.
  /// @param ctx The context.
  /// @param bop The binary operator.
  private void handleBinaryOp(ListContext ctx, BinaryOp bop) {
    var left = visit(ctx.item(1));
    if (left instanceof SExpressionValue lval) {
      emitLoadConstant(lval);
    }
    var right = visit(ctx.item(2));
    if (right instanceof SExpressionValue rval) {
      emitLoadConstant(rval);
    }
    emit(bop);
  }
}
