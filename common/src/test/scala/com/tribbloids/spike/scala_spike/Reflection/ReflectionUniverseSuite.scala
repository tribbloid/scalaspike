package com.tribbloids.spike.scala_spike.Reflection

import com.tribbloids.spike.BaseSpec
import com.tribbloids.spike.scala_spike.Reflection.ScalaReflection.universe

class ReflectionUniverseSuite extends BaseSpec {

  import ScalaReflection._
  import ReflectionUniverseSuite._

  it("reify") {

    val str = "String"

    {
      val tt = Typed(str)
      val rr = universe.reify(tt)
      println(rr)
      println(tt.ttag)
    }

    {
      val tt = Typed[str.type](str)
      val rr = universe.reify(tt)
      println(rr)
      println(tt.ttag)
    }
  }
}

object ReflectionUniverseSuite {

  import ScalaReflection.universe.TypeTag

  case class Typed[T: TypeTag](v: T) {

    def ttag: universe.TypeTag[T] = implicitly[TypeTag[T]]
  }
}
