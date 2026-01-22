package chisel.examples

import chisel3.*
import chisel3.util.*

/**
  * Computation Graph Examples in Chisel DSL
  *
  * This file demonstrates various ways to construct computation graphs for digital circuits using Chisel's hardware
  * construction language.
  */

// ============================================================================
// EXAMPLE 1: Basic Computation Graph - Simple Arithmetic
// ============================================================================

/**
  * A basic arithmetic circuit demonstrating Chisel's computation graph construction.
  *
  * Computation graph:
  *   a ----(+)----(×)---- output
  *         |       |
  *   b ----+       |
  *                 |
  *   c ------------+
  *
  * This computes: (a + b) * c
  */
class ArithmeticGraphIO extends Bundle {
  val a: UInt = Input(UInt(8.W))
  val b: UInt = Input(UInt(8.W))
  val c: UInt = Input(UInt(8.W))

  val d: UInt = a + b
  val result: UInt = Output(UInt(8.W))
}

class ArithmeticGraph extends Module {
  val io: ArithmeticGraphIO = IO(new ArithmeticGraphIO)

  // Computation graph construction using Chisel operators
  val sum: UInt = io.a + io.b // Addition node
  io.result := sum * io.c // Multiplication node
}

/**
  * Multiple independent computation paths in parallel.
  *
  * Computation graph: a ──(+)─── mulResult b ─┤ │ ├──(×)─── mulResult c ─┘
  *
  * d ──(-)─── subResult e ─┤
  *
  * Outputs both multiplication and subtraction results
  */
class ParallelComputationsIO extends Bundle {
  val a: SInt = Input(SInt(8.W))
  val b: SInt = Input(SInt(8.W))
  val c: SInt = Input(SInt(8.W))
  val d: SInt = Input(SInt(8.W))
  val e: SInt = Input(SInt(8.W))
  val mulResult: SInt = Output(SInt(16.W)) // Wider for multiplication
  val subResult: SInt = Output(SInt(8.W))
}

class ParallelComputations extends Module {
  val io: ParallelComputationsIO = IO(new ParallelComputationsIO)

  // Parallel computation paths
  io.mulResult := (io.a + io.b) * io.c
  io.subResult := io.d - io.e
}

// ============================================================================
// EXAMPLE 2: Computation Graph with State (Sequential Logic)
// ============================================================================

/**
  * Accumulator circuit demonstrating stateful computation graph.
  *
  * Computation graph: input ──(+)─── output │ [reg] ─┘ (feedback)
  *
  * Computes: output = reg + input reg = output (on clock edge)
  */
class AccumulatorIO(width: Int) extends Bundle {
  val input: SInt = Input(SInt(width.W))
  val output: SInt = Output(SInt(width.W))
  val clear: Bool = Input(Bool()) // Reset accumulator to 0
}

class Accumulator(width: Int = 8) extends Module {
  val io: AccumulatorIO = IO(new AccumulatorIO(width))

  // State register in the computation graph
  val reg: SInt = RegInit(0.S(width.W))

  // Computation with feedback
  when(io.clear) {
    reg := 0.S
  }.otherwise {
    reg := reg + io.input
  }

  io.output := reg
}

/**
  * Moving average filter demonstrating pipelined computation graph.
  *
  * Computation graph: input ──> [delay 0] ──┬──(+)─── output │ │ (+) └──> [delay 1] ┴──> [delay 2] (average)
  */
class MovingAverageFilterIO(windowSize: Int) extends Bundle {
  val in: SInt = Input(SInt(16.W))
  val out: SInt = Output(SInt(16.W))
  val valid: Bool = Input(Bool())
}

class MovingAverageFilter(windowSize: Int = 4) extends Module {
  val io: MovingAverageFilterIO = IO(new MovingAverageFilterIO(windowSize))

  // Create delay line (shift register)
  val delays: Seq[SInt] = Seq.fill(windowSize)(RegInit(0.S(16.W)))

  // Computation graph: shift register + accumulation
  when(io.valid) {
    for (i <- (windowSize - 1) to 1 by -1) {
      delays(i) := delays(i - 1)
    }
    delays(0) := io.in
  }

  // Sum all delayed values
  val sum: SInt = delays.reduce(_ + _)
  io.out := sum >> log2Ceil(windowSize) // Divide by power of 2 using right shift
}

// ============================================================================
// EXAMPLE 3: Complex Computation Graph - FIR Filter
// ============================================================================

/**
  * Finite Impulse Response (FIR) Filter
  *
  * Computation graph: x[n] ──┬──[z^-1]──┬──[z^-1]──┬──[z^-1]──┬──> │ │ │ │ │ │ │ │ (×) (×) (×) (×) │ │ │ │ h[0] h[1]
  * h[2] h[3] │ │ │ │ └──(+)──(+)──(+)──(+)─────────> y[n]
  *
  * Computes: y[n] = sum(h[i] * x[n-i]) for i = 0 to taps-1
  */
class FIRFilterIO(bitWidth: Int) extends Bundle {
  val in: SInt = Input(SInt(bitWidth.W))
  val out: SInt = Output(SInt(bitWidth.W))
  val valid: Bool = Input(Bool())
}

class FIRFilter(taps: Int, bitWidth: Int = 16) extends Module {
  val io: FIRFilterIO = IO(new FIRFilterIO(bitWidth))

  // Coefficients (can be parameterized)
  val coefficients: Seq[SInt] = Seq(
    1.S,
    2.S,
    3.S,
    2.S,
    1.S // Symmetric coefficients
  ).take(taps)

  // Delay line (shift register)
  val delays: Vec[SInt] = RegInit(VecInit(Seq.fill(taps)(0.S(bitWidth.W))))

  // Update delay line
  when(io.valid) {
    for (i <- (taps - 1) to 1 by -1) {
      delays(i) := delays(i - 1)
    }
    delays(0) := io.in
  }

  // Computation graph: multiply-accumulate
  val products: Vec[SInt] = VecInit((0 until taps).map { i =>
    delays(i) * coefficients(i)
  })

  io.out := products.reduce(_ + _)
}

// ============================================================================
// EXAMPLE 4: Computation Graph with Conditional Logic
// ============================================================================

/**
  * Multiplexer-based computation graph.
  *
  * Computation graph: in0 ──┐ ├──[MUX]── output in1 ──┤ │ select in2 ──┤
  *
  * Selects one of multiple inputs based on selector signal
  */
class Multiplexer4to1IO(width: Int) extends Bundle {
  val in0: UInt = Input(UInt(width.W))
  val in1: UInt = Input(UInt(width.W))
  val in2: UInt = Input(UInt(width.W))
  val in3: UInt = Input(UInt(width.W))
  val select: UInt = Input(UInt(2.W))
  val out: UInt = Output(UInt(width.W))
}

class Multiplexer4to1(width: Int = 8) extends Module {
  val io: Multiplexer4to1IO = IO(new Multiplexer4to1IO(width))

  // Computation graph using Mux tree
  io.out := MuxLookup(
    io.select,
    io.in0,
    Seq(
      0.U -> io.in0,
      1.U -> io.in1,
      2.U -> io.in2,
      3.U -> io.in3
    )
  )
}

/**
  * Conditional computation graph (max finder).
  *
  * Computation graph: a ──┬──[COMP]──> if a > b ──┬──[MUX]── max │ │ │ b ──┴──[COMP]──> if b > a ──┘
  */
class MaxFinderIO(width: Int) extends Bundle {
  val a: UInt = Input(UInt(width.W))
  val b: UInt = Input(UInt(width.W))
  val max: UInt = Output(UInt(width.W))
  val isAGreater: Bool = Output(Bool())
}

class MaxFinder(width: Int = 8) extends Module {
  val io: MaxFinderIO = IO(new MaxFinderIO(width))

  // Computation graph with comparison
  io.isAGreater := io.a > io.b
  io.max := Mux(io.isAGreater, io.a, io.b)
}

// ============================================================================
// EXAMPLE 5: Tree-Structured Computation Graph
// ============================================================================

/**
  * Binary tree reduction circuit.
  *
  * Computation graph: level 0 level 1 level 2 in0 ──┬──(+)─┬────(+)─────────────────┐ │ │ │ in1 ──┘ │ │ │ │ in2
  * ──┬──(+)─┘ │ │ │ in3 ──┘ │ (+)── result in4 ──┬──(+)─┬─────────────────────────┘ │ │ in5 ──┘ │ │ in6 ──┬──(+)─┘ │
  * in7 ──┘
  */
class TreeReductionIO(numInputs: Int, width: Int) extends Bundle {
  val in: Vec[UInt] = Input(Vec(numInputs, UInt(width.W)))
  val sum: UInt = Output(UInt((width + 3).W)) // Wider to prevent overflow
}

class TreeReduction(numInputs: Int = 8, width: Int = 8) extends Module {
  val io: TreeReductionIO = IO(new TreeReductionIO(numInputs, width))

  // Tree reduction using foldLeft
  // Computation graph constructed recursively
  def reduceTree(inputs: Seq[UInt]): UInt = {
    if (inputs.length == 1) {
      inputs.head
    } else {
      val pairs = inputs.grouped(2).toSeq
      val reduced = pairs.map { pair =>
        if (pair.length == 2) pair(0) + pair(1)
        else pair(0)
      }
      reduceTree(reduced)
    }
  }

  io.sum := reduceTree(io.in.toSeq)
}

// ============================================================================
// EXAMPLE 6: Computation Graph with Memory Access
// ============================================================================

/**
  * Memory-based computation graph.
  *
  * Computation graph:
  *   address ──> [MEMORY] ──> data_out
  *     |                        |
  *   write_data ────────────────┘
  *     |
  *   write_enable
  */
class SimpleMemoryIO(depth: Int, width: Int) extends Bundle {
  val address: UInt = Input(UInt(log2Ceil(depth).W))
  val write_data: UInt = Input(UInt(width.W))
  val write_enable: Bool = Input(Bool())
  val read_data: UInt = Output(UInt(width.W))
}

class SimpleMemory(depth: Int = 16, width: Int = 8) extends Module {
  val io: SimpleMemoryIO = IO(new SimpleMemoryIO(depth, width))

  // Memory in the computation graph
  val memory = Mem(depth, UInt(width.W))

  // Read/write logic
  when(io.write_enable) {
    memory.write(io.address, io.write_data)
    io.read_data := 0.U // Write-first behavior
  }.otherwise {
    io.read_data := memory.read(io.address) // Read port
  }
}

// ============================================================================
// EXAMPLE 7: Computation Graph with Counters and Control
// ============================================================================

/**
  * Pulse generator with counter-based computation.
  *
  * Computation graph: [counter] ──(compare)──> pulse │ [max_value]
  */
class PulseGeneratorIO extends Bundle {
  val pulse: Bool = Output(Bool())
  val clear: Bool = Input(Bool())
}

class PulseGenerator(maxCount: Int = 100) extends Module {
  val io: PulseGeneratorIO = IO(new PulseGeneratorIO)

  val counter: UInt = RegInit(0.U(log2Ceil(maxCount + 1).W))

  when(io.clear) {
    counter := 0.U
    io.pulse := false.B
  }.otherwise {
    when(counter === maxCount.U) {
      counter := 0.U
      io.pulse := true.B
    }.otherwise {
      counter := counter + 1.U
      io.pulse := false.B
    }
  }
}

// ============================================================================
// Verifier for testing examples
// ============================================================================

/**
  * Test harness to verify computation graph examples
  */
object ComputationGraphVerifier extends App {
  // This would be used with chisel-testers2 for verification
  // Example: chisel3.iotesters.Driver(() => new ArithmeticGraph(), "verilator")

  println("Computation Graph Examples in Chisel")
  println("=====================================")
  println("\nAvailable examples:")
  println("1. ArithmeticGraph - Basic arithmetic operations")
  println("2. ParallelComputations - Parallel computation paths")
  println("3. Accumulator - Stateful accumulation")
  println("4. MovingAverageFilter - Pipelined filter")
  println("5. FIRFilter - Finite impulse response filter")
  println("6. Multiplexer4to1 - Multiplexer logic")
  println("7. MaxFinder - Comparison-based computation")
  println("8. TreeReduction - Tree-structured reduction")
  println("9. SimpleMemory - Memory access")
  println("10. PulseGenerator - Counter-based control")
}
