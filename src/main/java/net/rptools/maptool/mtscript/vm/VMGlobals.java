package net.rptools.maptool.mtscript.vm;

import java.util.ArrayList;
import net.rptools.maptool.mtscript.vm.values.IntegerType;
import net.rptools.maptool.mtscript.vm.values.Symbol;
import net.rptools.maptool.mtscript.vm.values.ValueRecord;

public class VMGlobals {
  private record SymbolEntry(Symbol symbol, boolean constant) {};

  private final ArrayList<SymbolEntry> globalVariables = new ArrayList<>();


  public Symbol getGlobalVariable(int index) {
    if (index < 0 || index >= globalVariables.size()) {
      throw new RuntimeException("Invalid global variable index: " + index); // TODO: CDW
    }
    var entry = globalVariables.get(index);
    if (entry == null) {
      throw new RuntimeException("Invalid global variable index: " + index); // TODO: CDW
    }
    return entry.symbol();
  }

  public void setGlobalVariable(int index, ValueRecord value) {
    if (index < 0 || index >= globalVariables.size()) {
      throw new RuntimeException("Invalid global variable index: " + index); // TODO: CDW
    }
    var entry = globalVariables.get(index);
    assert entry != null; // TODO: CDW
    if (entry.constant()) {
      throw new RuntimeException("Cannot modify constant variable: " + entry.symbol().name()); // TODO: CDW
    }
    globalVariables.set(index, new SymbolEntry(new Symbol(entry.symbol().name(), value), false));
  }

  public void setGlobalVariable(int index, Symbol variable) {
    if (index < 0 || index >= globalVariables.size()) {
      throw new RuntimeException("Invalid global variable index: " + index); // TODO: CDW
    }
    var entry = globalVariables.get(index);
    assert entry != null && !entry.symbol().name().equals(variable.name()); // TODO: CDW
    if (entry.constant()) {
      throw new RuntimeException("Cannot modify constant variable: " + variable.name()); // TODO: CDW
    }
    globalVariables.set(index, new SymbolEntry(variable, false));
  }

  public int getGlobalSymbolIndex(String name) {
    for (int i = 0; i < globalVariables.size(); i++) {
      if (globalVariables.get(i).symbol.name().equals(name)) {
        return i;
      }
    }
    return -1;
  }

  public int defineGlobalVariable(String name) {
    int index = getGlobalSymbolIndex(name);
    if (index != -1) {
      throw new RuntimeException("Variable already defined: " + name); // TODO: CDW
    }
    // TODO: CDW need null/nil/undefined value
    globalVariables.add(new SymbolEntry(new Symbol(name, new IntegerType(0)), false));
    return globalVariables.size() - 1;
  }

  public int getGlobalVariableCount() {
    return globalVariables.size();
  }

  public void defineConstant(String name, Symbol value) {
    int index = getGlobalSymbolIndex(name);
    if (index != -1) {
      throw new RuntimeException("Variable already defined: " + name); // TODO: CDW
    }
    globalVariables.add(new SymbolEntry(value, true));
  }

}
