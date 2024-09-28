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

import java.util.Stack;
import net.rptools.maptool.mtscript.vm.values.BooleanType;
import net.rptools.maptool.mtscript.vm.values.CodeType;
import net.rptools.maptool.mtscript.vm.values.IntegerType;
import net.rptools.maptool.mtscript.vm.values.NativeFunctionType;
import net.rptools.maptool.mtscript.vm.values.Symbol;
import net.rptools.maptool.mtscript.vm.values.ValueRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO CDW: There is a problem where if a multi block program consists of nothing but local
// variable declarations there will be a stack underflow error when the HALT opcode is reached.
// This needs to be fixed but I am leaving this for later as we may just end up optimising these
// types of blocks away.

/// The `MapToolVM` implements the Virtual Machine for the MapTool scripting language.
public class MapToolVM {

  /// The logger for the class.
  private static final Logger log = LogManager.getLogger(MapToolVM.class);

  /// The maximum size of the stack.
  private static final int MAX_STACK_SIZE = 512;

  /// The program to execute.
  private CodeType program;

  /// The instruction pointer.
  private int instructionPointer = -1;

  /// The stack for the VM.
  private final Stack<ValueRecord> stack = new Stack<>();

  /// The stack frame base.
  private int stackFrameBase = 0;

  /// The global environment for the VM.
  private final VMGlobals globals;

  /// Creates a new instance of the `MaptoolVM`class.
  /// @param globals The global environment for the VM.
  public MapToolVM(VMGlobals globals) {
    this.globals = globals;
    setGlobals();
  }

  /// Sets the global variables for the VM.
  private void setGlobals() {
    // Define the global variables
    globals.defineConstant("MT_VM_VERSION", new Symbol("MT_VM_VERSION", new IntegerType(1)));
  }

  // Execute the given program.
  /// @param program The program to execute.
  /// @return The result of the program.
  public ValueRecord exec(CodeType program) {
    this.program = program;
    instructionPointer = 0;
    stackFrameBase = stack.size();

    try {
      return eval();
    } catch (Exception e) {
      // TODO: CDW - Handle exceptions
      var ip = String.format("0x%04x", instructionPointer - 1);
      log.error(
          "\nError executing program: {} @ ip = {} opcode = {} \n",
          e.getMessage(),
          ip,
          OpCode.fromByteCode(program.getByte(instructionPointer)));
      throw e;
    }
  }

  /// Evaluates the program.
  /// @return The result of the program.
  private ValueRecord eval() {
    while (true) {
      var opcode = readNextOpCode();
      switch (opcode) {
        // Constant VM operation
        case LOAD_CONST -> push(getConstant());
        // Add VM operation
        case ADD -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.add(op2));
        }
        // Subtract VM operation
        case SUB -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.subtract(op2));
        }
        // Multiply VM operation
        case MULT -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.multiply(op2));
        }
        // Divide VM operation
        case DIV -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.divide(op2));
        }
        // Less than VM operation
        case LT -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.compareLessThan(op2));
        }
        // Greater than VM operation
        case GT -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.compareGreaterThan(op2));
        }
        // Less than or equal VM operation
        case LTE -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.compareLessThanOrEqual(op2));
        }
        // Greater than or equal VM operation
        case GTE -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.compareGreaterThanOrEqual(op2));
        }
        // Equal VM operation
        case EQ -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.compareEqual(op2));
        }
        // Not equal VM operation
        case NEQ -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.compareNotEqual(op2));
        }
        // Jump VM operation
        case JUMP -> {
          int labelIndex = readNextByte();
          instructionPointer = program.getJumpLabel(labelIndex);
        }
        // Jump if false VM operation
        case JUMP_IF_FALSE -> {
          int labelIndex = readNextByte();
          var value = pop();
          if (value instanceof BooleanType bool) {
            if (!bool.value()) {
              instructionPointer = program.getJumpLabel(labelIndex);
            }
          } else {
            throw new RuntimeException("Expected boolean name on stack"); // TODO: CDW
          }
        }
        // Load label VM operation TODO: CDW Do we need this?
        case LOAD_LABEL -> {
          int labelIndex = readNextByte();
          push(new IntegerType(labelIndex)); // TODO: CDW
        }
        // Load global Value on the stack
        case LOAD_GLOBAL -> {
          int globalIndex = readNextByte();
          push(globals.getGlobalVariable(globalIndex).value());
        }
        // Set global Value from the stack
        case SET_GLOBAL -> {
          int globalIndex = readNextByte();
          var value = peek();
          globals.setGlobalVariable(globalIndex, value);
        }
        // Pop top name from the stack
        case POP -> {
          pop();
          dumpDebug(OpCode.POP, "after");
        }
        // Load local Value on the top of the stack
        case LOAD_LOCAL -> {
          int localIndex = readNextByte();
          push(stack.get(stackFrameBase + localIndex));
        }
        // Set local Value from the top of the stack
        case SET_LOCAL -> {
          int localIndex = readNextByte();
          var value = peek();
          stack.set(stackFrameBase + localIndex, value);
        }
        // Exit the current scope
        case EXIT_SCOPE -> {
          dumpDebug(OpCode.EXIT_SCOPE, "before");
          int stackToPop = readNextByte();
          var returnValue = pop();
          for (int i = 0; i < stackToPop; i++) { // TODO: CDW
            pop();
          }
          push(returnValue);
          dumpDebug(OpCode.EXIT_SCOPE, "after");
        }
        // Call VM operation
        case CALL -> {
          int numArgs = readNextByte();
          var function = peek(numArgs);
          if (function instanceof NativeFunctionType nativeFunction) {
            nativeFunction.vmFunction().call(this);
            var result = pop();
            pop(numArgs + 1); // Pop the arguments and the function
            push(result); // Push the result of the function back onto the stack
          } else {
            throw new RuntimeException("Expected function on stack"); // TODO: CDW
          }
        }

        // Halt VM operation
        case HALT -> {
          dumpDebug(OpCode.HALT, "before");
          var returnValue = pop();
          stack.clear(); // After the program halts, the values on the stack (if any) are discarded.
          return returnValue;
        }
        default ->
            throw new RuntimeException(
                "Unhandled opcode: " + opcode + ", ip = " + instructionPointer); // TODO: CDW
      }
    }
  }

  private void dumpDebug(OpCode opCode, String message) {
    var ip = String.format("0x%04x", instructionPointer - 1);
    log.debug("{} {} @ ip = {}", message, opCode.instructionName(), ip); // TODO: CDW
    log.debug("  Stack:");
    for (int i = 0; i < stack.size(); i++) {
      log.debug("    {}: {}", i, stack.get(i)); // TODO: CDW
    }
    log.debug("  Globals:");
    for (int i = 0; i < globals.getGlobalVariableCount(); i++) {
      log.debug("    {}: {}", i, globals.getGlobalVariable(i)); // TODO: CDW
    }
  }

  /// Gets the constant name at the current instruction pointer.
  /// @return The constant name.
  private ValueRecord getConstant() {
    int idx = readNextByte();
    return program.getConstant(idx);
  }

  /// Reads the next byte from the code and returns it as an integer as Java Bytes are signed.
  /// @return The next byte (as an integer).
  private int readNextByte() {
    return program.getByte(instructionPointer++) & 0xFF; // Java Bytes are signed
  }

  /// Reads the next byte from the code.
  /// @return The next byte.
  private byte readNextByteRaw() {
    return program.getByte(instructionPointer++);
  }

  /// Reads the next opcode from the code.
  /// @return The next opcode.
  public OpCode readNextOpCode() {
    return OpCode.fromByteCode(readNextByteRaw());
  }

  /// Pushes a name onto the stack.
  /// @param name The name to push.
  public void push(ValueRecord value) {
    if (stack.size() >= MAX_STACK_SIZE) {
      throw new RuntimeException("Stack overflow"); // TODO: CDW
    }
    stack.push(value);
  }

  /// Pops a name from the stack.
  /// @return The name popped from the stack.
  public ValueRecord pop() {
    if (stack.isEmpty()) {
      throw new RuntimeException("Stack underflow"); // TODO: CDW
    }
    return stack.pop();
  }

  /// Pops the given number of names from the stack.
  /// @param count The number of names to pop.
  public void pop(int count) {
    for (int i = 0; i < count; i++) {
      pop();
    }
  }

  /// Peeks at the top of the stack.
  /// @return The name at the top of the stack.
  public ValueRecord peek() {
    if (stack.isEmpty()) {
      throw new RuntimeException("Stack underflow"); // TODO: CDW
    }
    return stack.peek();
  }

  /// Peeks at the given index from the top of the stack.
  /// @param index The index from the top of the stack.
  /// @return The name at the given index from the top of the stack.
  public ValueRecord peek(int index) {
    if (index < 0 || index >= stack.size()) {
      throw new RuntimeException("Invalid stack index: " + index); // TODO: CDW
    }
    return stack.get(stack.size() - index - 1);
  }
}
