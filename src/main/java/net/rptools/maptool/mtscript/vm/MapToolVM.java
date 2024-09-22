package net.rptools.maptool.mtscript.vm;


import java.util.Stack;
import net.rptools.maptool.mtscript.vm.values.BooleanType;
import net.rptools.maptool.mtscript.vm.values.CodeType;
import net.rptools.maptool.mtscript.vm.values.IntegerType;
import net.rptools.maptool.mtscript.vm.values.ValueRecord;
import net.rptools.maptool.mtscript.vm.values.Symbol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/// The `MaptoolVM` implements the Virtual Machine for the MapTool scripting language.
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

    try {
      return eval();
    } catch (Exception e) {
      // TODO: CDW - Handle exceptions
      log.error("Error executing program: {}", e.getMessage());
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
        case DIV ->{
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
            throw new RuntimeException("Expected boolean value on stack"); // TODO: CDW
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
        /// Set global Value from the stack
        case SET_GLOBAL -> {
          int globalIndex = readNextByte();
          var value = peek();
          globals.setGlobalVariable(globalIndex, value);
        }




        // Halt VM operation
        case HALT -> {
          log.debug("HALT @ ip = {}", instructionPointer);  // TODO: CDW
          var returnValue = pop();
          stack.clear(); // After the program halts, the values on the stack (if any) are discarded.
          return returnValue;
        }
        default -> throw new RuntimeException("Unhandled opcode: " + opcode + ", ip = " + instructionPointer); // TODO: CDW
      }
    }
  }

  /// Gets the constant value at the current instruction pointer.
  /// @return The constant value.
  private ValueRecord getConstant() {
    int idx = readNextByte();
    return program.getConstant(idx);
  }

  /// Reads the next byte from the code.
  /// @return The next byte.
  private byte readNextByte() {
    return program.getByte(instructionPointer++);
  }

  /// Reads the next opcode from the code.
  /// @return The next opcode.
  public OpCode readNextOpCode() {
    return OpCode.fromByteCode(readNextByte());
  }

  /// Pushes a value onto the stack.
  /// @param value The value to push.
  private void push(ValueRecord value) {
    if (stack.size() >= MAX_STACK_SIZE) {
      throw new RuntimeException("Stack overflow"); // TODO: CDW
    }
    stack.push(value);
  }


  /// Pops a value from the stack.
  /// @return The value popped from the stack.
  private ValueRecord pop() {
    if (stack.isEmpty()) {
      throw new RuntimeException("Stack underflow"); // TODO: CDW
    }
    return stack.pop();
  }

  /// Peeks at the top of the stack.
  /// @return The value at the top of the stack.
  private ValueRecord peek() {
    if (stack.isEmpty()) {
      throw new RuntimeException("Stack underflow"); // TODO: CDW
    }
    return stack.peek();
  }


}
