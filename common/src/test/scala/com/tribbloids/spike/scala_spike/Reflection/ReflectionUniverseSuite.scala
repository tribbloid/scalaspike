package com.tribbloids.spike.scala_spike.Reflection

import ai.acyclic.prover.commons.debug.print_@
import ai.acyclic.prover.commons.testlib.BaseSpec
import ai.acyclic.prover.commons.viz.TypeViz.{universe, TypeTag}

class ReflectionUniverseSuite extends BaseSpec {

  import ReflectionUniverseSuite._

  describe("reify") {

    val str = "String"

    it("inferred type") {

      {
        val tt = Typed(str)
        val rr = universe.reify(tt)

        print_@(tt.inferredTTag)
        print_@(rr)
      }

      {
        val tt = Typed[str.type](str)
        val rr = universe.reify(tt)
        print_@(tt.inferredTTag)
        print_@(rr)
      }
    }

    it("inferred singleton type") {

      val tt = Typed(str)
      val rr = universe.reify(tt)
      print_@(tt.refinedTTag)
      print_@(rr)

    }
  }

}

object ReflectionUniverseSuite {

  case class Typed[T <: AnyRef: TypeTag](v: T) {

    def inferredTTag: TypeTag[T] = implicitly[TypeTag[T]]

    def refinedTTag: TypeTag[v.type] = implicitly[TypeTag[v.type]]
  }

  case class SelfTyped(v: String) {

    type This = this.type

    class Container[T >: this.type](v: T = SelfTyped.this) {

      type This = T
    }
  }
}
