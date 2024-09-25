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
package net.rptools.maptool.mtscript.vm;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import net.rptools.maptool.mtscript.vm.values.BooleanType;
import net.rptools.maptool.mtscript.vm.values.CodeType;
import net.rptools.maptool.mtscript.vm.values.IntegerType;
import net.rptools.maptool.mtscript.vm.values.StringType;
import net.rptools.maptool.mtscript.vm.values.Symbol;
import net.rptools.maptool.mtscript.vm.values.ValueRecord;

/// Class for building byte code for the MTScript VM.
public class MapToolVMByteCodeBuilder {

  private record IfElseLabels(int elseLabel, int endIfLabel) {}
  ;

  private final Stack<IfElseLabels> ifElseLabels = new Stack<>();

  /// The byte code stream.
  private final ByteArrayOutputStream byteCodeStream = new ByteArrayOutputStream();

  /// The list of constants.
  private final List<ValueRecord> constants = new ArrayList<>();

  /// The list of jump labels.
  private final List<Integer> jumpLabels = new ArrayList<>();

  //// The current scope level.
  private int scopeLevel = 0;

  /// List of Local Symbols
  private final List<SymbolEntry> localSymbols = new ArrayList<>();

  /// The name of the byte code.
  private final String name;

  /// Creates a new byte code builder.
  /// @param name The name of the byte code.
  public MapToolVMByteCodeBuilder(String name) {
    this.name = name;
  }

  /// Enters a new scope.
  public void enterScope() {
    scopeLevel++;
  }

  /// Exits the current scope.
  public void exitScope() {
    int numberOfSymbols = removeSymbolsInCurrentScope();
    if (!isInGlobalScope()) {
      emitExitScope(numberOfSymbols);
    }
    scopeLevel--;
  }

  /// Removes symbols in the current scope.
  /// @return The number of symbols removed.
  private int removeSymbolsInCurrentScope() {
    int count = 0;
    while (!localSymbols.isEmpty() && localSymbols.getLast().scopeLevel() == scopeLevel) {
      localSymbols.removeLast();
      count++;
    }
    return count;
  }

  /// Returns the current scope level.
  /// @return The current scope level.
  public int getScopeLevel() {
    return scopeLevel;
  }

  /// Returns true if the current scope is the global scope.
  /// @return True if the current scope is the global scope.
  public boolean isInGlobalScope() {
    return scopeLevel == VMGlobals.GLOBAL_VARIABLE_SCOPE;
  }

  /// Defines a global symbol.
  /// @param name The name of the symbol.
  /// @return The index of the symbol.
  public int defineLocalSymbol(String name) {
    var scopeSymbols = getSymbolsNamesForCurrentScope();
    if (scopeSymbols.contains(name)) {
      throw new RuntimeException("Symbol " + name + " already defined in scope"); // TODO CDW
    }
    localSymbols.add(new SymbolEntry(new Symbol(name, new IntegerType(0)), false, scopeLevel));
    return localSymbols.size() - 1;
  }

  private Set<String> getSymbolsNamesForCurrentScope() {
    return localSymbols.stream()
        .filter(s -> s.scopeLevel() == scopeLevel)
        .map(s -> s.symbol().name())
        .collect(Collectors.toSet());
  }

  /// Returns the index of a local symbol.
  /// @param name The name of the symbol.
  /// @return The index of the symbol or -1 if not found.
  public int getLocalSymbolIndex(String name) {
    // We work backwards to find the most recent version of the symbol
    for (int i = localSymbols.size() - 1; i >= 0; i--) {
      if (localSymbols.get(i).symbol().name().equals(name)) {
        return i;
      }
    }
    return -1;
  }

  /// Writes a byte to the byte code stream.
  /// @param b The byte to write.
  private void writeByte(byte b) {
    byteCodeStream.write(b);
  }

  /// Writes a word to the byte code stream.
  /// @param w The word to write.
  private void writeWord(int w) {
    writeByte((byte) (w & 0xFF));
    writeByte((byte) ((w >> 8) & 0xFF));
  }

  /// Emits an opcode to the byte code stream.
  /// @param op The opcode to emit.
  public void emit(OpCode op) {
    writeByte(op.byteCode());
  }

  /// Adds a constant to the program.
  /// @param constant The constant to add.
  /// @return The index of the constant in the constant pool.
  public int addConstant(ValueRecord constant) {
    if (constants.contains(constant)) {
      return constants.indexOf(constant);
    }
    constants.add(constant);
    return constants.size() - 1;
  }

  /// Allocates a jump label.
  /// @return The index of the jump label.
  public int allocateJumpLabel() {
    jumpLabels.add(-1);
    return jumpLabels.size() - 1;
  }

  /// Sets the jump label to the next byte code instruction.
  /// @param label The index of the jump label.
  public void setJumpLabel(int label) {
    jumpLabels.set(label, byteCodeStream.size());
  }

  /// Builds the code type.
  /// @return The code type.
  public CodeType build() {
    emit(OpCode.HALT);
    return new CodeType(name, byteCodeStream.toByteArray(), constants, jumpLabels);
  }

  /// Emits a load constant instruction.
  /// @param constant The constant to load.
  public void emitLoadConstant(ValueRecord constant) {
    emit(OpCode.LOAD_CONST);
    writeByte((byte) addConstant(constant)); // TODO: CDW Handle > 256 constants
  }

  /// Emits a load constant instruction for an integer.
  /// @param name The integer to load.
  public void emitLoadConstant(int value) {
    emitLoadConstant(new IntegerType(value));
  }

  /// Emits a load constant instruction for a string.
  /// @param name The string to load.
  public void emitLoadConstant(String value) {
    emitLoadConstant(new StringType(value));
  }

  /// Emits a load constant instruction for a boolean.
  /// @param name The boolean to load.
  public void emitLoadConstant(boolean value) {
    emitLoadConstant(value ? BooleanType.TRUE : BooleanType.FALSE);
  }

  /// Emits an add instruction.
  public void emitAdd() {
    emit(OpCode.ADD);
  }

  /// Emits a subtract instruction.
  public void emitSubtract() {
    emit(OpCode.SUB);
  }

  /// Emits a multiply instruction.
  public void emitMultiply() {
    emit(OpCode.MULT);
  }

  /// Emits a divide instruction.
  public void emitDivide() {
    emit(OpCode.DIV);
  }

  /// Emits an equality comparison instruction.
  public void emitEqual() {
    emit(OpCode.EQ);
  }

  /// Emits a not equal comparison instruction.
  public void emitNotEqual() {
    emit(OpCode.NEQ);
  }

  /// Emits a less than comparison instruction.
  public void emitLessThan() {
    emit(OpCode.LT);
  }

  /// Emits a less than or equal comparison instruction.
  public void emitLessThanOrEqual() {
    emit(OpCode.LTE);
  }

  /// Emits a greater than comparison instruction.
  public void emitGreaterThan() {
    emit(OpCode.GT);
  }

  /// Emits a greater than or equal comparison instruction.
  public void emitGreaterThanOrEqual() {
    emit(OpCode.GTE);
  }

  /// Emits a jump instruction.
  public void emitJumpIfFalse(int label) {
    emit(OpCode.JUMP_IF_FALSE);
    writeByte((byte) label); // TODO CDW Handle > 256 labels
  }

  /// Emits a jump instruction.
  public void emitJump(int label) {
    emit(OpCode.JUMP);
    writeByte((byte) label); // TODO CDW Handle > 256 labels
  }

  /// Emits a load global symbol instruction.
  /// @param index The index of the global symbol.
  public void emitLoadGlobal(int index) {
    emit(OpCode.LOAD_GLOBAL);
    writeByte((byte) index); // TODO CDW Handle > 256 globals
  }

  /// Emits a set global symbol instruction.
  /// @param index The index of the global symbol.
  public void emitSetGlobal(int index) {
    emit(OpCode.SET_GLOBAL);
    writeByte((byte) index); // TODO CDW Handle > 256 globals
  }

  /// Emits a load local symbol instruction.
  /// @param index The index of the local symbol.
  public void emitLoadLocal(int index) {
    emit(OpCode.LOAD_LOCAL);
    writeByte((byte) index); // TODO CDW Handle > 256 locals
  }

  /// Emits a set local symbol instruction.
  /// @param index The index of the local symbol.
  public void emitSetLocal(int index) {
    emit(OpCode.SET_LOCAL);
    writeByte((byte) index); // TODO CDW Handle > 256 locals
  }

  /// Emits the pop instruction.
  public void emitPop() {
    emit(OpCode.POP);
  }

  /// Emits the enter scope instruction.
  /// @param numberOfSymbols The number of symbols in the scope.
  private void emitExitScope(int numberOfSymbols) {
    emit(OpCode.EXIT_SCOPE);
    writeByte((byte) numberOfSymbols); // TODO CDW Handle > 256 locals
  }
}
