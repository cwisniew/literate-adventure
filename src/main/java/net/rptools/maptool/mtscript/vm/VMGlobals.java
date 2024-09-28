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

import java.lang.reflect.Method;
import java.util.ArrayList;
import net.rptools.maptool.mtscript.functions.MTVMFunction;
import net.rptools.maptool.mtscript.functions.VMFunction;
import net.rptools.maptool.mtscript.vm.values.IntegerType;
import net.rptools.maptool.mtscript.vm.values.NativeFunctionType;
import net.rptools.maptool.mtscript.vm.values.Symbol;
import net.rptools.maptool.mtscript.vm.values.ValueRecord;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

/// The global symbol table.
public class VMGlobals {

  /// The scope level for global variables.
  public static final int GLOBAL_VARIABLE_SCOPE = 1;

  /// The list of global variables.
  private final ArrayList<SymbolEntry> globalVariables = new ArrayList<>();


  public VMGlobals() {
    Reflections reflections = new Reflections(
        new ConfigurationBuilder()
            .forPackage("net.rptools.maptool.mtscript.functions")
            .setScanners(Scanners.MethodsAnnotated)
    );;
    var functions =
        reflections.get(Scanners.MethodsAnnotated.with(MTVMFunction.class).as(Method.class));
    for (var function : functions) {
      var annotation = function.getAnnotation(MTVMFunction.class);
      addBuiltInFunction(annotation.name(), annotation.arity(), (vm) -> {
        try {
          function.invoke(null, vm);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    }
  }

  /// Creates a new instance of the `VMGlobals` class.
  /// @param index The index of the global variable.
  /// @return The global variable at the given index.
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

  /// Sets the global variable at the given index.
  /// @param index The index of the global variable.
  /// @param name The name to set the global variable to.
  public void setGlobalVariable(int index, ValueRecord value) {
    if (index < 0 || index >= globalVariables.size()) {
      throw new RuntimeException("Invalid global variable index: " + index); // TODO: CDW
    }
    var entry = globalVariables.get(index);
    assert entry != null; // TODO: CDW
    if (entry.constant()) {
      throw new RuntimeException(
          "Cannot modify constant variable: " + entry.symbol().name()); // TODO: CDW
    }
    globalVariables.set(
        index,
        new SymbolEntry(new Symbol(entry.symbol().name(), value), false, GLOBAL_VARIABLE_SCOPE));
  }

  /// Sets the global variable at the given index.
  /// @param index The index of the global variable.
  /// @param variable The variable to set the global variable to.
  public void setGlobalVariable(int index, Symbol variable) {
    if (index < 0 || index >= globalVariables.size()) {
      throw new RuntimeException("Invalid global variable index: " + index); // TODO: CDW
    }
    var entry = globalVariables.get(index);
    assert entry != null && !entry.symbol().name().equals(variable.name()); // TODO: CDW
    if (entry.constant()) {
      throw new RuntimeException(
          "Cannot modify constant variable: " + variable.name()); // TODO: CDW
    }
    globalVariables.set(index, new SymbolEntry(variable, false, GLOBAL_VARIABLE_SCOPE));
  }

  /// Gets the index of the global variable with the given name.
  /// @param name The name of the global variable.
  /// @return The index of the global variable or -1 if not found.
  public int getGlobalSymbolIndex(String name) {
    for (int i = 0; i < globalVariables.size(); i++) {
      if (globalVariables.get(i).symbol().name().equals(name)) {
        return i;
      }
    }
    return -1;
  }

  /// Gets the global variable with the given name.
  /// @param name The name of the global variable.
  /// @return The global variable with the given name.
  public Symbol getGlobalSymbol(String name) {
    int index = getGlobalSymbolIndex(name);
    if (index == -1) {
      throw new RuntimeException("Variable not defined: " + name); // TODO: CDW
    }
    return getGlobalVariable(index);
  }

  /// Defines a new global variable.
  /// @param name The name of the global variable.
  /// @return The index of the global variable.
  public int defineGlobalVariable(String name) {
    int index = getGlobalSymbolIndex(name);
    if (index != -1) {
      throw new RuntimeException("Variable already defined: " + name); // TODO: CDW
    }
    // TODO: CDW need null/nil/undefined name
    globalVariables.add(
        new SymbolEntry(new Symbol(name, new IntegerType(0)), false, GLOBAL_VARIABLE_SCOPE));
    return globalVariables.size() - 1;
  }

  /// Gets the number of global variables.
  /// @return The number of global variables.
  public int getGlobalVariableCount() {
    return globalVariables.size();
  }

  /// Defines a new constant.
  /// @param name The name of the constant.
  /// @param name The name of the constant.
  public void defineConstant(String name, Symbol value) {
    int index = getGlobalSymbolIndex(name);
    if (index != -1) {
      throw new RuntimeException("Variable already defined: " + name); // TODO: CDW
    }
    globalVariables.add(new SymbolEntry(value, true, GLOBAL_VARIABLE_SCOPE));
  }

  /// Adds a built-in function to the global symbol table.
  /// @param name The name of the function.
  /// @param arity The number of arguments the function takes.
  public void addBuiltInFunction(String name, int arity, VMFunction function) {
    globalVariables.add(
        new SymbolEntry(
            new Symbol(name, new NativeFunctionType(name, arity, function)),
            true,
            GLOBAL_VARIABLE_SCOPE));
  }
}
