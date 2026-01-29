package com.tribbloids.spike.scala_spike

import org.scalatest.funspec.AnyFunSpec

class TraitLinearization extends AnyFunSpec {

  describe("Trait Linearization") {

    it("should invoke super methods in linearization order (Right-first for mixins)") {

      trait Base {
        def foo(): String = "Base"
      }

      trait Left extends Base {
        override def foo(): String = "Left -> " + super.foo()
      }

      trait Right extends Base {
        override def foo(): String = "Right -> " + super.foo()
      }

      // Linearization order for Bottom: Bottom -> Right -> Left -> Base
      class Bottom extends Left with Right {
        override def foo(): String = "Bottom -> " + super.foo()
      }

      val bottom = new Bottom
      val result = bottom.foo()

      // Explanation:
      // 1. Bottom.foo calls super.foo -> refers to Right.foo (next in linearization)
      // 2. Right.foo calls super.foo -> refers to Left.foo (next in linearization)
      // 3. Left.foo calls super.foo -> refers to Base.foo (next in linearization)

      val expected = "Bottom -> Right -> Left -> Base"

      println(s"Result: $result")
      assert(result == expected)
    }

    it("what if the order is swapped?") {
      trait Base {
        def foo(): String = "Base"
      }

      trait Left extends Base {
        override def foo(): String = "Left -> " + super.foo()
      }

      trait Right extends Base {
        override def foo(): String = "Right -> " + super.foo()
      }

      // Linearization order for Bottom: Bottom -> Left -> Right -> Base
      class Bottom extends Right with Left {
        override def foo(): String = "Bottom -> " + super.foo()
      }

      val bottom = new Bottom
      val result = bottom.foo()
      val expected = "Bottom -> Left -> Right -> Base"

      println(s"Result: $result")
      assert(result == expected)
    }
  }
}
