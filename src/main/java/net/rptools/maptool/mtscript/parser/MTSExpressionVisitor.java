/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
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

/// A visitor for S-expressions that generates a program.
public class MTSExpressionVisitor extends mtSexpressionParserBaseVisitor<SExpressionExpr> {

  /// The byte code builder.
  private final MapToolVMByteCodeBuilder builder;

  /// The global symbol table.
  private final VMGlobals globals;

  /// Whether we are parsing a variable write, if we are we don't need to load the variable onto
  // the stack.
  private boolean parsingVariableWrite = false;

  /// Creates a new expression visitor.
  /// @param builder The byte code builder.
  public MTSExpressionVisitor(MapToolVMByteCodeBuilder builder, VMGlobals globals) {
    this.builder = builder;
    this.globals = globals;
  }

  /// Emits an opcode to the byte code stream.
  /// @param opcode The opcode to emit.
  private void emit(BinaryOp op) {
    switch (op.name()) {
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
      default -> throw new RuntimeException("Unknown operator: " + op.name());
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

  /// Handles a symbol.
  /// @param name The name of the symbol.
  /// @param generateLoad Whether to generate a load instruction.
  private SExpressionExpr handleSymbol(String name, boolean generateLoad) {
    // TODO: CDW This works but is in desperate need of refactoring.
    int index = -1;
    boolean isGlobal = false;
    if (builder.isInGlobalScope()) {
      index = globals.getGlobalSymbolIndex(name);
      isGlobal = true;
    } else {
      index = builder.getLocalSymbolIndex(name);
      if (index == -1) {
        index = globals.getGlobalSymbolIndex(name);
        isGlobal = true;
      }
    }
    if (index != -1) {
      if (generateLoad) {
        if (isGlobal) {
          builder.emitLoadGlobal(index);
        } else {
          builder.emitLoadLocal(index);
        }
      }
      return new SymbolOp(name, true);
    }
    return new SymbolOp(name, false);
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
        case "if", "var", "set", "begin" -> new Op(symbol);
        default -> handleSymbol(symbol, !parsingVariableWrite);
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
        throw new RuntimeException("Encountered undefined symbol: " + s.name()); // // TODO: CDW
      }
    } else if (firstOp instanceof BinaryOp bop) {
      handleBinaryOp(ctx, bop);
    } else if (firstOp instanceof Op op) {
      switch (op.name()) {
        case "if" -> handleIf(ctx);
        case "var" -> handleVar(ctx);
        case "set" -> handleSet(ctx);
        case "begin" -> {
          int size = ctx.item().size();
          builder.enterScope();
          for (int i = 1; i < ctx.item().size(); i++) {
            var res = visit(ctx.item(i));
            boolean localSymbolDec = false;
            if (res != null && !builder.isInGlobalScope() && res.name().equals("var")) {
              localSymbolDec = true;
              System.out.println("Local Symbol Declaration: " + ctx.item(i).getText());
            }
            if (i < size - 1 && !localSymbolDec) {
              // The result of the block is the result of the last expression in the block. All
              // other results are popped unless they are local symbol declarations as
              // local symbols live on the stack until the end of the block.
              builder.emitPop();
            }
          }
          builder.exitScope();
        }
        default -> throw new RuntimeException("Unknown operator: " + op.name()); // TODO: CDW
      }
    } else {
      for (int i = 1; i < ctx.item().size(); i++) {
        var newOp = visit(ctx.item(i));
      }
    }
    return firstOp;
  }

  /// Handles a set statement.
  /// @param ctx The context.
  private void handleSet(ListContext ctx) {
    if (ctx.item().size() != 3) { // TODO: CDW
      throw new RuntimeException("Set statement must have exactly two operands: " + ctx.getText());
    }
    parsingVariableWrite = true;
    var symbol = visit(ctx.item(1));
    parsingVariableWrite = false;
    if (symbol instanceof SymbolOp s) {
      // dont bother checking if the symbol is defined, as we need to find it in any case
      int index = -1;
      boolean isGlobal = false;
      if (builder.isInGlobalScope()) {
        index = globals.getGlobalSymbolIndex(s.name());
        isGlobal = true;
      } else {
        index = builder.getLocalSymbolIndex(s.name());
        if (index == -1) {
          index = globals.getGlobalSymbolIndex(s.name());
          isGlobal = true;
        }
      }
      if (index == -1) {
        throw new RuntimeException("Undefined symbol: " + s.name()); // TODO: CDW
      }
      visit(ctx.item(2));
      if (isGlobal) {
        builder.emitSetGlobal(index);
      } else {
        builder.emitSetLocal(index);
      }
    } else {
      throw new RuntimeException("Invalid symbol: " + symbol); // TODO: CDW
    }
  }

  /// Handles a var statement.
  /// @param ctx The context.
  private void handleVar(ListContext ctx) {
    if (ctx.item().size() != 3) { // TODO: CDW
      throw new RuntimeException("Var statement must have exactly two operands: " + ctx.getText());
    }
    var symbol = visit(ctx.item(1));
    if (symbol instanceof SymbolOp s) {
      visit(ctx.item(2));
      if (builder.isInGlobalScope()) {
        int index = globals.defineGlobalVariable(s.name());
        builder.emitSetGlobal(index);
      } else {
        int index = builder.defineLocalSymbol(s.name());
        builder.emitSetLocal(index);
      }
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
          "Binary operator (" + bop.name() + "must have exactly two operands:" + ctx.getText());
    }
    visit(ctx.item(1));
    visit(ctx.item(2));
    emit(bop);
  }
}
