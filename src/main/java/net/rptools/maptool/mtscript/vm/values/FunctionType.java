package net.rptools.maptool.mtscript.vm.values;

import java.util.List;

/// Represents a function type in the MTScript VM.
public class FunctionType extends CodeType {

  /// The arity of the function.
  private final int arity;


  /// Creates a new function type.
  public FunctionType(String name, byte[] code, List<ValueRecord> constants,
      List<Integer> jumpLabels, int arity) {
    super(name, code, constants, jumpLabels);
    this.arity = arity;
  }

  /// Returns the arity of the function.
  /// @return The arity of the function.
  public int arity() {
    return arity;
  }
}
