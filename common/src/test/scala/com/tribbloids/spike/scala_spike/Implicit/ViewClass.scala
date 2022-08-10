package com.tribbloids.spike.scala_spike.Implicit

import ai.acyclic.graph.commons.testlib.BaseSpec

class ViewClass extends BaseSpec {

  it("can chain summoning") {

    implicit class Thing1(v: String) {

      def print1(): Unit = println(v)
    }

    implicit class Thing2[T](v: T)(
        implicit
        bound: T => Thing1
    ) {

      def print2(): Unit = v.print1()
    }

    implicit class Thing3[TT](v: TT)(
        implicit
        bound: TT => Thing2[_]
    ) {

      def print3(): Unit = v.print2()
    }

//    trait

    "abc".print1()

    "abc".print2()

    "abc".print3()
  }
}

object ViewClass {

  case class ~~>[-A, +P](dim: Int)

  object ~~> {

    implicit def summon[A, P]: A ~~> P = ~~>[A, P](10)
  }

  case class Vector[A1](data: Array[A1]) {}

  object Vector {

    implicit class Reify[A1, P](
        self: Vector[A1]
    )(
        implicit
        prove: A1 ~~> P
    ) {

      def dim: Int = {

        prove.dim
      }
    }
  }

  val v = Vector(Array(10.0))

  v.dim
}
