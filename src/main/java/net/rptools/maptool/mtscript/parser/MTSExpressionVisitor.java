package net.rptools.maptool.mtscript.parser;

import net.rptools.maptool.mtscript.parser.expr.BinaryOp;
import net.rptools.maptool.mtscript.parser.expr.BooleanValue;
import net.rptools.maptool.mtscript.parser.expr.IntegerValue;
import net.rptools.maptool.mtscript.parser.expr.Op;
import net.rptools.maptool.mtscript.parser.expr.SExpressionExpr;
import net.rptools.maptool.mtscript.parser.expr.SExpressionValue;
import net.rptools.maptool.mtscript.parser.expr.StringValue;
import net.rptools.maptool.mtscript.parser.expr.SymbolOp;
import net.rptools.maptool.mtscript.parser.mtSexpressionParser.AtomContext;
import net.rptools.maptool.mtscript.parser.mtSexpressionParser.ListContext;
import net.rptools.maptool.mtscript.vm.MapToolVMByteCodeBuilder;
import net.rptools.maptool.mtscript.vm.VMGlobals;
import net.rptools.maptool.mtscript.vm.values.Symbol;

/// A visitor for S-expressions that generates a program.
public class MTSExpressionVisitor extends mtSexpressionParserBaseVisitor<SExpressionExpr> {

  /// The byte code builder.
  private final MapToolVMByteCodeBuilder builder;

  /// The global symbol table.
  private final VMGlobals globals;


  /// Creates a new expression visitor.
  /// @param builder The byte code builder.
  public MTSExpressionVisitor(MapToolVMByteCodeBuilder builder, VMGlobals globals) {
    this.builder = builder;
    this.globals = globals;
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
        case "true" -> {
          emitLoadConstant(new BooleanValue(true));
          yield null;
        }
        case "false" -> {
          emitLoadConstant(new BooleanValue(false));
          yield null;
        }
        case "if", "var", "set" -> new Op(symbol);
        default -> {
          int index = globals.getGlobalSymbolIndex(symbol);
          if (index != -1) {
            builder.emitLoadGlobal(index);
            yield new SymbolOp(symbol, true);
          }
          yield new SymbolOp(symbol, false);
        }
      };
    } else if (ctx.INTEGER_LITERAL() != null) {
      emitLoadConstant(new IntegerValue(Integer.parseInt(ctx.INTEGER_LITERAL().getText())));
      return null;
    } else if (ctx.STRING_LITERAL() != null) {
      String val = ctx.STRING_LITERAL().getText().replaceFirst("^\"", "").replaceFirst("\"$", "");
      emitLoadConstant(new StringValue(val));
      return null;
    } else {
      throw new RuntimeException("Unknown atom: " + ctx.getText()); // TODO: CDW
    }
  }

  @Override
  public SExpressionExpr visitList(ListContext ctx) {
    if (ctx.item().isEmpty()) {
      // TODO: CDW
      throw new RuntimeException("Empty list"); // TODO: CDW
    }

    var firstOp = visit(ctx.item(0));
    if (firstOp instanceof SymbolOp s) {
      if (!s.defined()) {
        throw new RuntimeException("Encountered undefined symbol: " + s.op()); // // TODO: CDW
      }
    } else if (firstOp instanceof BinaryOp bop) {
      handleBinaryOp(ctx, bop);
    } else if (firstOp instanceof Op op) {
      switch (op.op()) {
        case "if" ->  handleIf(ctx);
        case "var" -> handleVar(ctx);
        case "set" -> handleSet(ctx);
        default -> throw new RuntimeException("Unknown operator: " + op.op()); // TODO: CDW
      };
      return null;
    } else {
      for (int i = 1; i < ctx.item().size(); i++) {
        var newOp = visit(ctx.item(i));
      }
    }
    return null;
  }

  private void handleSet(ListContext ctx) {
    if (ctx.item().size() != 3) { // TODO: CDW
      throw new RuntimeException("Set statement must have exactly two operands: " + ctx.getText());
    }
    var symbol = visit(ctx.item(1));
    if (symbol instanceof SymbolOp s) {
      if (!s.defined()) {
        throw new RuntimeException("Undefined symbol: " + s.op()); // TODO: CDW
      }
      int index = globals.getGlobalSymbolIndex(s.op());
      if (index == -1) {
        throw new RuntimeException("Undefined symbol: " + s.op()); // TODO: CDW
      }
      visit(ctx.item(2));
      builder.emitSetGlobal(index);
    } else {
      throw new RuntimeException("Invalid symbol: " + symbol); // TODO: CDW
    }
  }

  private void handleVar(ListContext ctx) {
    if (ctx.item().size() != 3) { // TODO: CDW
      throw new RuntimeException("Var statement must have exactly two operands: " + ctx.getText());
    }
    var symbol = visit(ctx.item(1));
    if (symbol instanceof SymbolOp s) {
      if (s.defined()) {
        throw new RuntimeException("Variable already defined: " + s.op()); // TODO: CDW
      }
      int index = globals.defineGlobalVariable(s.op());
      visit(ctx.item(2));
      builder.emitSetGlobal(index);
    } else {
      throw new RuntimeException("Invalid symbol: " + symbol); // TODO: CDW
    }
  }

  /// Handles an if statement.
  /// @param ctx The context.
  private void handleIf(ListContext ctx) {
    if (ctx.item().size() != 4) { // TODO: CDW
      throw new RuntimeException("If statement must have exactly three operands: " + ctx.getText());
    }
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
    if (ctx.item().size() != 3) { // TODO: CDW
      throw new RuntimeException(
          "Binary operator (" + bop.op() + "must have exactly two operands:" + ctx.getText()
      );
    }
    visit(ctx.item(1));
    visit(ctx.item(2));
    emit(bop);
  }
}
