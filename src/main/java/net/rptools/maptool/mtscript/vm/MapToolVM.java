package net.rptools.maptool.mtscript.vm;


import java.util.Stack;
import net.rptools.maptool.mtscript.vm.values.CodeType;
import net.rptools.maptool.mtscript.vm.values.ValueRecord;
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

  /// Creates a new instance of the `MaptoolVM`class.
  public MapToolVM() {
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
      int opcode = readNextByte();
      switch (opcode) {
        // Constant VM operation
        case OpCode.LOAD_CONST -> push(getConstant());
        // Add VM operation
        case OpCode.ADD -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.add(op2));
        }
        // Subtract VM operation
        case OpCode.SUB -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.subtract(op2));
        }
        // Multiply VM operation
        case OpCode.MUL -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.multiply(op2));
        }
        // Divide VM operation
        case OpCode.DIV ->{
          var op2 = pop();
          var op1 = pop();
          push(op1.divide(op2));
        }
        // Less than VM operation
        case OpCode.LT -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.compareLessThan(op2));
        }
        // Greater than VM operation
        case OpCode.GT -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.compareGreaterThan(op2));
        }
        // Less than or equal VM operation
        case OpCode.LTE -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.compareLessThanOrEqual(op2));
        }
        // Greater than or equal VM operation
        case OpCode.GTE -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.compareGreaterThanOrEqual(op2));
        }
        // Equal VM operation
        case OpCode.EQ -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.compareEqual(op2));
        }
        // Not equal VM operation
        case OpCode.NEQ -> {
          var op2 = pop();
          var op1 = pop();
          push(op1.compareNotEqual(op2));
        }



        // Halt VM operation
        case OpCode.HALT -> {
          log.debug("HALT @ ip = {}", instructionPointer);  // TODO: CDW
          var returnValue = pop();
          stack.clear(); // After the program halts, the values on the stack (if any) are discarded.
          return returnValue;
        }
        default -> throw new RuntimeException("Unknown opcode: " + opcode); // TODO: CDW
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


}
