package chisel.examples

import chisel3.*
import chiseltest.*
import org.scalatest.flatspec.AnyFlatSpec

/**
  * Tests for Computation Graph Examples
  *
  * Demonstrates how to test and verify Chisel computation graphs
  *
  * NOTE: These tests require the Chisel compiler plugin to be properly configured. See:
  * https://github.com/chipsalliance/chisel3#the-chisel-compiler-plugin
  *
  * To run these tests, you need to add the Chisel compiler plugin to your scalac options. The main code
  * (ComputationGraphExamples.scala) compiles and demonstrates all the computation graph patterns without requiring the
  * plugin.
  */
class ComputationGraphExamplesTest extends AnyFlatSpec with ChiselScalatestTester {

  // ========================================================================
  // Test 1: Arithmetic Graph
  // ========================================================================

  "ArithmeticGraph" should "compute (a + b) * c correctly" in {
    test(new ArithmeticGraph) { c =>
      // Test case 1: (5 + 3) * 2 = 16
      c.io.a.poke(5.U)
      c.io.b.poke(3.U)
      c.io.c.poke(2.U)
      c.io.result.expect(16.U)

      // Test case 2: (10 + 20) * 3 = 90 (truncated to 8 bits)
      c.io.a.poke(10.U)
      c.io.b.poke(20.U)
      c.io.c.poke(3.U)
      c.io.result.expect(90.U)

      // Test case 3: (0 + 0) * 255 = 0
      c.io.a.poke(0.U)
      c.io.b.poke(0.U)
      c.io.c.poke(255.U)
      c.io.result.expect(0.U)
    }
  }

  // ========================================================================
  // Test 2: Parallel Computations
  // ========================================================================

  "ParallelComputations" should "compute multiple results in parallel" in {
    test(new ParallelComputations) { c =>
      // Test case: (5 + 3) * 2 = 16 and 10 - 7 = 3
      c.io.a.poke(5.S)
      c.io.b.poke(3.S)
      c.io.c.poke(2.S)
      c.io.d.poke(10.S)
      c.io.e.poke(7.S)

      c.io.mulResult.expect(16.S)
      c.io.subResult.expect(3.S)
    }
  }

  // ========================================================================
  // Test 3: Accumulator
  // ========================================================================

  "Accumulator" should "accumulate values correctly" in {
    test(new Accumulator(width = 8)) { c =>
      c.io.clear.poke(false.B)

      // Initial state: output = 0
      c.io.output.expect(0.S)

      // Add 5: output = 0 + 5 = 5
      c.io.input.poke(5.S)
      c.clock.step()
      c.io.output.expect(5.S)

      // Add 3: output = 5 + 3 = 8
      c.io.input.poke(3.S)
      c.clock.step()
      c.io.output.expect(8.S)

      // Add 10: output = 8 + 10 = 18
      c.io.input.poke(10.S)
      c.clock.step()
      c.io.output.expect(18.S)

      // Clear accumulator
      c.io.clear.poke(true.B)
      c.clock.step()
      c.io.output.expect(0.S)

      // Verify it stays at 0
      c.io.clear.poke(false.B)
      c.io.input.poke(5.S)
      c.clock.step()
      c.io.output.expect(5.S)
    }
  }

  // ========================================================================
  // Test 4: Moving Average Filter
  // ========================================================================

  "MovingAverageFilter" should "compute moving average correctly" in {
    test(new MovingAverageFilter(windowSize = 4)) { c =>
      c.io.valid.poke(true.B)

      // Input sequence: 10, 20, 30, 40, 50, 60
      // Expected averages after warmup: (10+20+30+40)/4 = 25, etc.

      c.io.in.poke(10.S)
      c.clock.step()
      // Not enough samples yet, will have partial average
      c.io.out.expect(2.S) // 10/4 = 2.5 truncated

      c.io.in.poke(20.S)
      c.clock.step()

      c.io.in.poke(30.S)
      c.clock.step()

      c.io.in.poke(40.S)
      c.clock.step()
      c.io.out.expect(25.S) // (10+20+30+40)/4 = 25

      c.io.in.poke(50.S)
      c.clock.step()
      c.io.out.expect(35.S) // (20+30+40+50)/4 = 35

      c.io.in.poke(60.S)
      c.clock.step()
      c.io.out.expect(45.S) // (30+40+50+60)/4 = 45
    }
  }

  // ========================================================================
  // Test 5: FIR Filter
  // ========================================================================

  "FIRFilter" should "filter input correctly" in {
    test(new FIRFilter(taps = 5)) { c =>
      c.io.valid.poke(true.B)

      // Coefficients: [1, 2, 3, 2, 1]
      // Input impulse response test
      c.io.in.poke(10.S)
      c.clock.step()
      c.io.out.expect(10.S) // 10*1

      c.io.in.poke(0.S)
      c.clock.step()
      c.io.out.expect(20.S) // 10*2

      c.io.in.poke(0.S)
      c.clock.step()
      c.io.out.expect(30.S) // 10*3

      c.io.in.poke(0.S)
      c.clock.step()
      c.io.out.expect(20.S) // 10*2

      c.io.in.poke(0.S)
      c.clock.step()
      c.io.out.expect(10.S) // 10*1

      c.io.in.poke(0.S)
      c.clock.step()
      c.io.out.expect(0.S) // No more input
    }
  }

  // ========================================================================
  // Test 6: Multiplexer
  // ========================================================================

  "Multiplexer4to1" should "select correct input based on select signal" in {
    test(new Multiplexer4to1()) { c =>
      c.io.in0.poke(10.U)
      c.io.in1.poke(20.U)
      c.io.in2.poke(30.U)
      c.io.in3.poke(40.U)

      // Select in0
      c.io.select.poke(0.U)
      c.io.out.expect(10.U)

      // Select in1
      c.io.select.poke(1.U)
      c.io.out.expect(20.U)

      // Select in2
      c.io.select.poke(2.U)
      c.io.out.expect(30.U)

      // Select in3
      c.io.select.poke(3.U)
      c.io.out.expect(40.U)
    }
  }

  // ========================================================================
  // Test 7: Max Finder
  // ========================================================================

  "MaxFinder" should "find maximum of two numbers" in {
    test(new MaxFinder()) { c =>
      // Test case 1: a > b
      c.io.a.poke(100.U)
      c.io.b.poke(50.U)
      c.io.max.expect(100.U)
      c.io.isAGreater.expect(true.B)

      // Test case 2: b > a
      c.io.a.poke(30.U)
      c.io.b.poke(70.U)
      c.io.max.expect(70.U)
      c.io.isAGreater.expect(false.B)

      // Test case 3: a == b
      c.io.a.poke(50.U)
      c.io.b.poke(50.U)
      c.io.max.expect(50.U)
      c.io.isAGreater.expect(false.B) // Greater than, not greater-or-equal
    }
  }

  // ========================================================================
  // Test 8: Tree Reduction
  // ========================================================================

  "TreeReduction" should "sum all inputs correctly" in {
    test(new TreeReduction(numInputs = 8)) { c =>
      // Test case 1: All ones
      for (i <- 0 until 8) {
        c.io.in(i).poke(1.U)
      }
      c.io.sum.expect(8.U)

      // Test case 2: Sequential values
      for (i <- 0 until 8) {
        c.io.in(i).poke(i.U)
      }
      c.io.sum.expect(28.U) // 0+1+2+3+4+5+6+7 = 28

      // Test case 3: All zeros
      for (i <- 0 until 8) {
        c.io.in(i).poke(0.U)
      }
      c.io.sum.expect(0.U)

      // Test case 4: Powers of 2
      c.io.in(0).poke(1.U)
      c.io.in(1).poke(2.U)
      c.io.in(2).poke(4.U)
      c.io.in(3).poke(8.U)
      c.io.in(4).poke(16.U)
      c.io.in(5).poke(32.U)
      c.io.in(6).poke(64.U)
      c.io.in(7).poke(128.U)
      c.io.sum.expect(255.U) // Sum of 2^0 through 2^7
    }
  }

  // ========================================================================
  // Test 9: Simple Memory
  // ========================================================================

  "SimpleMemory" should "read and write data correctly" in {
    test(new SimpleMemory(depth = 16, width = 8)) { c =>
      // Write some values
      c.io.write_enable.poke(true.B)

      for (i <- 0 until 5) {
        c.io.address.poke(i.U)
        c.io.write_data.poke((i * 10).U)
        c.clock.step()
      }

      // Read back the values
      c.io.write_enable.poke(false.B)

      for (i <- 0 until 5) {
        c.io.address.poke(i.U)
        c.clock.step()
        c.io.read_data.expect((i * 10).U)
      }
    }
  }

  // ========================================================================
  // Test 10: Pulse Generator
  // ========================================================================

  "PulseGenerator" should "generate pulse at correct interval" in {
    test(new PulseGenerator(maxCount = 5)) { c =>
      c.io.clear.poke(false.B)

      // Count from 0 to 5, pulse at count = 5
      for (cycle <- 0 until 20) {
        val expectedPulse = (cycle % 6) == 5
        c.io.pulse.expect(expectedPulse.B)
        c.clock.step()
      }

      // Test clear
      c.io.clear.poke(true.B)
      c.clock.step()
      c.io.clear.poke(false.B)

      // Should restart counting
      for (cycle <- 0 until 5) {
        c.io.pulse.expect(false.B)
        c.clock.step()
      }
      c.io.pulse.expect(true.B)
    }
  }
}
