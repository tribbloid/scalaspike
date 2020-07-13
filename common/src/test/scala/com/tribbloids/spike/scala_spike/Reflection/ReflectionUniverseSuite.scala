package com.tribbloids.spike.scala_spike.Reflection

import com.tribbloids.spike.BaseSpec
import com.tribbloids.spike.utils.debug.print_@

class ReflectionUniverseSuite extends BaseSpec {

  import com.tribbloids.spike.utils.ScalaReflection._
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

  import com.tribbloids.spike.utils.ScalaReflection.universe._

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
