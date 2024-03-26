package com.tribbloids.spike.scala_spike.Reflection

import ai.acyclic.prover.commons.refl.Reflection.Runtime.TypeTag
import org.scalatest.funspec.AnyFunSpec

class TypeResolving extends AnyFunSpec {

  import com.tribbloids.spike.scala_spike.Reflection.TypeResolving._

  val example = new Example

  it("can convert") {

    val t1 = implicitly[TypeTag[example.T]]
    println(t1)

    val t2 = implicitly[TypeTag[Map[String, Int]]]
    println(t2)
  }
}

object TypeResolving {

  class Example {

    type T = Map[String, Int]
  }
  val example = new Example
}
