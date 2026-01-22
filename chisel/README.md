# Chisel Computation Graph Examples

This directory contains comprehensive examples demonstrating how to use Chisel DSL to construct computation graphs for digital circuits.

## Overview

Chisel (Constructing Hardware In a Scala Embedded Language) is an open-source hardware construction language embedded in Scala. This collection shows various patterns for building computation graphs ranging from simple arithmetic to complex filters and memory systems.

## Key Concepts

### 1. **Computation Graph Construction in Chisel**

In Chisel, you construct computation graphs by connecting hardware primitives using operators:

```scala
// Simple arithmetic graph
val sum = io.a + io.b        // Creates an addition node
io.result := sum * io.c      // Creates a multiplication node
```

### 2. **Types of Computation Graphs**

#### **Combinational Logic (No State)**
- Instant computation from inputs to outputs
- Examples: `ArithmeticGraph`, `ParallelComputations`, `Multiplexer4to1`
- No registers or memory elements

#### **Sequential Logic (With State)**
- Computation depends on previous values
- Uses `Reg()` and `RegInit()` for state
- Examples: `Accumulator`, `MovingAverageFilter`, `PulseGenerator`

#### **Pipelined Computation**
- Multiple stages of computation
- Registers separate stages
- Example: `MovingAverageFilter`

#### **Tree-Structured Computation**
- Hierarchical reduction operations
- Parallel computation paths
- Example: `TreeReduction`

#### **Feedback-Based Computation**
- Output depends on previous outputs
- Cyclic computation graphs
- Example: `Accumulator`

### 3. **Common Chisel Operators**

| Operation | Description | Example |
|-----------|-------------|---------|
| `+`, `-`, `*`, `/` | Arithmetic | `a + b` |
| `&`, `\|`, `^` | Bitwise logic | `a & b` |
| `===`, `=/=` | Comparison | `a === b` |
| `Mux`, `MuxLookup` | Multiplexing | `Mux(sel, a, b)` |
| `Cat` | Concatenation | `Cat(a, b)` |
| `Reg`, `RegInit` | Registers | `RegInit(0.U)` |
| `Mem` | Memory | `Mem(16, UInt(8.W))` |

### 4. **Computation Graph Patterns**

#### **Pattern 1: Feed-Forward Chain**
```scala
// a -> add -> mul -> output
// b -> ─┘
// c --------┘
val temp = io.a + io.b
io.result := temp * io.c
```

#### **Pattern 2: Parallel Branches**
```scala
// Independent computations in parallel
io.result1 := io.a + io.b
io.result2 := io.c * io.d
```

#### **Pattern 3: Feedback Loop**
```scala
// State with feedback
val reg = RegInit(0.S(8.W))
reg := reg + io.input  // Feedback
io.output := reg
```

#### **Pattern 4: Reduction Tree**
```scala
// Tree reduction for efficient summation
val sum = io.in.reduce(_ + _)
```

#### **Pattern 5: Conditional Computation**
```scala
// Mux-based selection
io.result := Mux(io.sel, io.a, io.b)
```

## Examples

### 1. **ArithmeticGraph** (`ArithmeticGraph`)
- **Purpose**: Basic arithmetic operations
- **Computation**: `(a + b) * c`
- **Key Features**: Operator chaining, intermediate values

### 2. **ParallelComputations** (`ParallelComputations`)
- **Purpose**: Multiple independent computation paths
- **Computations**: `(a + b) * c` and `d - e`
- **Key Features**: Parallel evaluation, multiple outputs

### 3. **Accumulator** (`Accumulator`)
- **Purpose**: Stateful accumulation
- **Computation**: `sum = sum + input` (on each clock)
- **Key Features**: Registers, feedback, control signals

### 4. **MovingAverageFilter** (`MovingAverageFilter`)
- **Purpose**: Sliding window average
- **Computation**: Average of last N samples
- **Key Features**: Shift register, pipelining, fixed-point arithmetic

### 5. **FIRFilter** (`FIRFilter`)
- **Purpose**: Finite Impulse Response filter
- **Computation**: `y[n] = Σ(h[i] * x[n-i])`
- **Key Features**: Multiply-accumulate, coefficient multiplication

### 6. **Multiplexer4to1** (`Multiplexer4to1`)
- **Purpose**: Data routing
- **Computation**: Select one of four inputs
- **Key Features**: Mux tree, control signals

### 7. **MaxFinder** (`MaxFinder`)
- **Purpose**: Comparison-based computation
- **Computation**: `max(a, b)`
- **Key Features**: Comparison operators, conditional logic

### 8. **TreeReduction** (`TreeReduction`)
- **Purpose**: Efficient parallel reduction
- **Computation**: Sum of N inputs using tree structure
- **Key Features**: Recursive tree construction, parallel evaluation

### 9. **SimpleMemory** (`SimpleMemory`)
- **Purpose**: Memory access patterns
- **Computation**: Read/write to memory
- **Key Features**: Memory primitives, address decoding

### 10. **PulseGenerator** (`PulseGenerator`)
- **Purpose**: Counter-based control
- **Computation**: Generate periodic pulse
- **Key Features**: Counters, comparison, control logic

## Building and Running

### Prerequisites
- Scala 2.12 or 2.13
- Java 8 or higher
- Gradle (already configured)

### Build the Project
```bash
cd /home/peng/git/scalaspike
./gradlew :chisel:build
```

### Run Tests
```bash
./gradlew :chisel:test
```

**Note:** Chisel 3.6+ requires a compiler plugin for runtime execution. Tests require additional setup. See [Chisel Compiler Plugin](https://github.com/chipsalliance/chisel3#the-chisel-compiler-plugin) for details.

To verify the code without running tests:
```bash
./gradlew :chisel:compileScala
```

### Generate Verilog
```bash
# Use Chisel's main object to generate Verilog
sbt "runMain chisel.examples.ComputationGraphVerifier"
```

## Understanding the Examples

### How to Read the Computation Graphs

Each example includes ASCII diagrams showing the computation graph structure:

```
Computation graph:
   a ----(+)----(×)---- output
         |       |
   b ----+       |
                 |
   c ------------+
```

This represents:
1. Addition node: receives `a` and `b`
2. Multiplication node: receives sum and `c`
3. Output: result of multiplication

### Key Chisel Constructs

#### **Module Definition**
```scala
class MyModule extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(8.W))
    val out = Output(UInt(8.W))
  })
  // Computation graph here
}
```

#### **Register Declaration**
```scala
// Uninitialized register
val reg = Reg(UInt(8.W))

// Initialized register
val regInit = RegInit(0.U)
```

#### **Conditional Logic**
```scala
when(condition) {
  // True branch
}.otherwise {
  // False branch
}
```

#### **For Loops (Unrolled at Elaboration Time)**
```scala
for (i <- 0 until n) {
  // Creates n copies of hardware
}
```

## Best Practices

1. **Use Type Annotations**: Always specify bit widths explicitly
   ```scala
   val signal = UInt(8.W)  // Good
   val signal = UInt()     // Bad - inference issues
   ```

2. **Avoid Combinational Loops**: Ensure no feedback without registers
   ```scala
   // Bad: Combinational loop
   val a = io.in
   a := a + 1.U

   // Good: Register breaks loop
   val reg = RegInit(0.U)
   reg := reg + io.in
   ```

3. **Use Appropriate Bit Widths**: Account for operation growth
   ```scala
   // Multiplication doubles width
   val product = io.a * io.b  // 8-bit * 8-bit = 16-bit
   ```

4. **Document Your Graphs**: Use comments and ASCII diagrams
   ```scala
   // Computation graph: add -> mul -> output
   val sum = io.a + io.b
   io.result := sum * io.c
   ```

## Further Reading

- [Chisel Official Documentation](https://www.chisel-lang.org/)
- [Chisel GitHub](https://github.com/chipsalliance/chisel)
- [Digital Design with Chisel](https://github.com/schoeberl/chisel-book)

## Contributing

Feel free to add more examples demonstrating:
- Complex control logic
- State machines
- Pipelined processors
- Bus interfaces
- Arbitration schemes

Remember: The key is to show how to **construct** computation graphs, not just what they compute!
