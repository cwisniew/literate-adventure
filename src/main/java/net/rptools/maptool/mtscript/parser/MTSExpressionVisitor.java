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

import java.util.List;
import java.util.Stack;
import net.rptools.maptool.mtscript.parser.expr.BinaryOp;
import net.rptools.maptool.mtscript.parser.expr.BooleanValue;
import net.rptools.maptool.mtscript.parser.expr.IntegerValue;
import net.rptools.maptool.mtscript.parser.expr.Op;
import net.rptools.maptool.mtscript.parser.expr.SExpressionExpr;
import net.rptools.maptool.mtscript.parser.expr.SExpressionValue;
import net.rptools.maptool.mtscript.parser.expr.StringValue;
import net.rptools.maptool.mtscript.parser.expr.SymbolOp;
import net.rptools.maptool.mtscript.parser.mtSexpressionParser.AtomContext;
import net.rptools.maptool.mtscript.parser.mtSexpressionParser.DefineFunctionContext;
import net.rptools.maptool.mtscript.parser.mtSexpressionParser.ListContext;
import net.rptools.maptool.mtscript.vm.MapToolVMByteCodeBuilder;
import net.rptools.maptool.mtscript.vm.VMGlobals;
import net.rptools.maptool.mtscript.vm.values.FunctionType;
import net.rptools.maptool.mtscript.vm.values.NativeFunctionType;
import net.rptools.maptool.mtscript.vm.values.ValueType;
import org.antlr.v4.runtime.tree.ParseTree;

/// A visitor for S-expressions that generates a program.
public class MTSExpressionVisitor extends mtSexpressionParserBaseVisitor<SExpressionExpr> {

  /// The byte code builder stack
  private final Stack<MapToolVMByteCodeBuilder> builderStack = new Stack<>();

  /// The current byte code builder.
  private MapToolVMByteCodeBuilder builder;

  /// The global symbol table.
  private final VMGlobals globals;

  /// Creates a new expression visitor.
  /// @param builder The byte code builder.
  public MTSExpressionVisitor(MapToolVMByteCodeBuilder builder, VMGlobals globals) {
    this.globals = globals;
    pushBuilder(builder);
  }

  /// Pushes a new builder onto the stack and sets it as the current builder.
  /// @param builder The builder to push.
  private void pushBuilder(MapToolVMByteCodeBuilder builder) {
    builderStack.push(builder);
    this.builder = builder;
  }

  /// Pops a builder from the stack and sets the builder on top of the stack as the current builder.
  private void popBuilder() {
    builderStack.pop();
    this.builder = builderStack.peek();
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
  private SExpressionExpr handleSymbol(String name) {
    var sym = builder.resolveSymbol(name);
    if (sym != null) {
      if (sym.compileTimeConstant()) {
        builder.emitLoadConstant(sym.symbol().value());
      } else if (sym.scopeLevel() == VMGlobals.GLOBAL_VARIABLE_SCOPE) {
        builder.emitLoadGlobal(sym.index());
      } else {
        builder.emitLoadLocal(sym.index());
      }
    }
    return new SymbolOp(name);
  }

  @Override
  public SExpressionExpr visitAtom(AtomContext ctx) {
    if (ctx.SYMBOL() != null) {
      String symbol = ctx.SYMBOL().getText();
      return switch (symbol) {
        case "+", "-", "*", "/", "<", ">", "<=", ">=", "==", "!=" -> new BinaryOp(symbol);
        case "if", "block", "while", "for" -> new Op(symbol);
        default -> new SymbolOp(symbol);
      };
    } else if (ctx.INTEGER_LITERAL() != null) {
      emitLoadConstant(new IntegerValue(Integer.parseInt(ctx.INTEGER_LITERAL().getText())));
      return null;
    } else if (ctx.STRING_LITERAL() != null) {
      String val = ctx.STRING_LITERAL().getText().replaceFirst("^\"", "").replaceFirst("\"$", "");
      emitLoadConstant(new StringValue(val));
      return null;
    } else if (ctx.BOOLEAN_LITERAL() != null) {
      emitLoadConstant(new BooleanValue(Boolean.parseBoolean(ctx.BOOLEAN_LITERAL().getText())));
      return null;
    } else if (ctx.VARIABLE_DEF() != null) {
      return new Op("var");
    } else if (ctx.VARIABLE_ASSIGN() != null) {
      return new Op("set");
    } else {
      throw new RuntimeException("Unknown atom: " + ctx.getText()); // TODO: CDW
    }
  }

  @Override
  public SExpressionExpr visitDefineFunction(DefineFunctionContext ctx) {
    var name = ctx.functionName.getText();
    int arity = ctx.paramList().getChildCount();
    // TODO: CDW handle reserved namespaces
    // TODO: CDW handle adding code object to list of objects for disassembly
    var funcBuilder = new MapToolVMByteCodeBuilder(name, arity, globals, builder);
    pushBuilder(funcBuilder);
    // Parameters are local symbols
    for (int i = 0; i < arity; i++) {
        funcBuilder.defineLocalSymbol(ctx.paramList().getChild(i).getText());
    }
    // Define the function as a local symbol, we use a place holder for the function as the
    // parser just needs to know the type and it will be populated at run time.
    int findex = funcBuilder.defineLocalSymbol( name, new FunctionType(
        name,
        new byte[0],
        List.of(),
        List.of(),
        arity
    )); // Add the function
    // name as a local
    // symbol
    var body = visitAndGenerateLoad(ctx.body); // generate the function body

    popBuilder();
    funcBuilder.exitScope();
    var func = funcBuilder.buildFunction();
    builder.addFunction(name, func);
    return null;
  }

  /// Visits a node and generates code to load the result onto the stack.
  /// @param tree The tree to visit.
  /// @return The result of the visit.
  private SExpressionExpr visitAndGenerateLoad(ParseTree tree) {
    try {
      var res = visit(tree);
      if (res instanceof SymbolOp s) {
        handleSymbol(s.name());
      }
      return res;
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      throw e;
    }
  }

  @Override
  public SExpressionExpr visitList(ListContext ctx) {
    if (ctx.item().isEmpty()) {
      // TODO: CDW
      throw new RuntimeException("Empty list"); // TODO: CDW
    }

    var firstOp = visitAndGenerateLoad(ctx.item(0));
    if (firstOp instanceof SymbolOp s) {
      var symbol = builder.resolveSymbol(s.name());
      if (symbol != null) {
        if (symbol.symbol().valueType() == ValueType.FUNCTION) {
          handleFunctionCall(ctx, (FunctionType) symbol.symbol().value());
          return null;
        } else if (symbol.symbol().valueType() == ValueType.NATIVE_FUNCTION) {
          handleNativeFunctionCall(ctx, (NativeFunctionType) symbol.symbol().value());
          return null;
        }
      }
      throw new RuntimeException("Unknown symbol: " + s.name()); // TODO: CDW
    } else if (firstOp instanceof BinaryOp bop) {
      handleBinaryOp(ctx, bop);
    } else if (firstOp instanceof Op op) {
      switch (op.name()) {
        case "if" -> handleIf(ctx);
        case "var" -> handleVar(ctx);
        case "set" -> handleSet(ctx);
        case "while" -> handleWhile(ctx);
        case "for" -> handleFor(ctx);
        case "block" -> handleBlock(ctx);
        default -> throw new RuntimeException("Unknown operator: " + op.name()); // TODO: CDW
      }
    } else {
      for (int i = 1; i < ctx.item().size(); i++) {
        var newOp = visitAndGenerateLoad(ctx.item(i));
      }
    }
    return firstOp;
  }

  /// Handles a while statement.
  /// @param ctx The context.
  private void handleWhile(ListContext ctx) {
    if (ctx.item().size() != 3) { // TODO: CDW
      throw new RuntimeException(
          "While statement must have exactly two operands: " + ctx.getText());
    }
    int conditionLabel = builder.allocateJumpLabel();
    builder.setJumpLabel(conditionLabel);
    int endLabel = builder.allocateJumpLabel();
    visitAndGenerateLoad(ctx.item(1)); // condition
    builder.emitJumpIfFalse(endLabel);
    visitAndGenerateLoad(ctx.item(2)); // body
    builder.emitJump(conditionLabel);
    builder.setJumpLabel(endLabel);
  }

  /// Handles a for statement.
  /// @param ctx The context.
  private void handleFor(ListContext ctx) {
    if (ctx.item().size() != 5) { // TODO: CDW
      throw new RuntimeException("For statement must have exactly four operands: " + ctx.getText());
    }
    builder.enterScope();
    visitAndGenerateLoad(ctx.item(1)); // init
    int conditionLabel = builder.allocateJumpLabel();
    builder.setJumpLabel(conditionLabel);
    int endLabel = builder.allocateJumpLabel();
    visitAndGenerateLoad(ctx.item(2)); // condition
    builder.emitJumpIfFalse(endLabel);
    visitAndGenerateLoad(ctx.item(4)); // body
    visitAndGenerateLoad(ctx.item(3)); // increment
    builder.emitJump(conditionLabel);
    builder.setJumpLabel(endLabel);
    builder.exitScope();
  }

  /// Handles a block statement.
  /// @param ctx The context.
  private void handleBlock(ListContext ctx) {
    int size = ctx.item().size();
    builder.enterScope();
    for (int i = 1; i < ctx.item().size(); i++) {
      var res = visitAndGenerateLoad(ctx.item(i));
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

  /// Handles a native function call.
  /// @param ctx The context.
  /// @param function The native function.
  private void handleNativeFunctionCall(ListContext ctx, NativeFunctionType function) {
    if (ctx.item().size() != function.arity() + 1) {
      throw new RuntimeException(
          "Native function " + function.name() + " requires " + function.arity() + " arguments");
    }

    // Push the arguments onto the stack in reverse order
    for (int i = ctx.item().size() - 1; i >= 1; i--) {
      visitAndGenerateLoad(ctx.item(i));
    }
    builder.emitNativeFunctionCall(function, function.arity());
  }


  /// Handles a function call.
  /// @param ctx The context.
  /// @param function The function.
  private void handleFunctionCall(ListContext ctx, FunctionType function) {
    if (ctx.item().size() != function.arity() + 1) {
      throw new RuntimeException(
          "Function " + function.name() + " requires " + function.arity() + " arguments");
    }

    // Push the arguments onto the stack in reverse order
    for (int i = ctx.item().size() - 1; i >= 1; i--) {
      visitAndGenerateLoad(ctx.item(i));
    }
    builder.emitFunctionCall(function, ctx.item().size() - 1);
  }

  /// Handles a set statement.
  /// @param ctx The context.
  private void handleSet(ListContext ctx) {
    if (ctx.item().size() != 3) { // TODO: CDW
      throw new RuntimeException("Set statement must have exactly two operands: " + ctx.getText());
    }
    var symbol = visit(ctx.item(1));
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
      visitAndGenerateLoad(ctx.item(2));
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
    // TODO: CDW handle reserved namespaces
    if (ctx.item().size() != 3) { // TODO: CDW
      throw new RuntimeException("Var statement must have exactly two operands: " + ctx.getText());
    }
    var symbol = visit(ctx.item(1));
    if (symbol instanceof SymbolOp s) {
      visitAndGenerateLoad(ctx.item(2));
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
    var condition = visitAndGenerateLoad(ctx.item(1));
    if (condition instanceof SExpressionValue cval) {
      emitLoadConstant(cval);
    }
    int elseLabel = builder.allocateJumpLabel();
    builder.emitJumpIfFalse(elseLabel);
    var trueBranch = visitAndGenerateLoad(ctx.item(2));
    if (trueBranch instanceof SExpressionValue tval) {
      emitLoadConstant(tval);
    }
    int endLabel = builder.allocateJumpLabel();
    builder.emitJump(endLabel);
    builder.setJumpLabel(elseLabel);
    var falseBranch = visitAndGenerateLoad(ctx.item(3));
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
    visitAndGenerateLoad(ctx.item(1));
    visitAndGenerateLoad(ctx.item(2));
    emit(bop);
  }
}
