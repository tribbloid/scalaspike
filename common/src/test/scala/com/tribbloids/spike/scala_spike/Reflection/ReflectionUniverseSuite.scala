package com.tribbloids.spike.scala_spike.Reflection

import com.tribbloids.spike.{BaseSpec, DebugUtils}

class ReflectionUniverseSuite extends BaseSpec {

  import com.tribbloids.spike.ScalaReflection._
  import ReflectionUniverseSuite._
  import DebugUtils._

  describe("reify") {

    val str = "String"

    it("inferred type") {

      {
        val tt = Typed(str)
        val rr = universe.reify(tt)

        printEval(tt.inferredTTag)
        printEval(rr)
      }

      {
        val tt = Typed[str.type](str)
        val rr = universe.reify(tt)
        printEval(tt.inferredTTag)
        printEval(rr)
      }
    }

    it("inferred singleton type") {

      val tt = Typed(str)
      val rr = universe.reify(tt)
      printEval(tt.refinedTTag)
      printEval(rr)

    }
  }

}

object ReflectionUniverseSuite {

  import com.tribbloids.spike.ScalaReflection.universe._

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
