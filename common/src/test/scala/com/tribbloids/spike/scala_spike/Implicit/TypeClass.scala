package com.tribbloids.spike.scala_spike.Implicit

import ai.acyclic.prover.commons.testlib.BaseSpec

class TypeClass extends BaseSpec {

  trait TypeClass[X, Y] {

    type Out
    def v: Out
  }

  object TypeClass {

    class IntClass[X, Y]() extends TypeClass[X, Y] {

      type Out = Int

      override def v = 2
    }

    implicit def summon[X <: Int, Y <: Int]: IntClass[X, Y] = new IntClass[X, Y] {}
  }

  case class Thing[X](v: X) {

    def +[Y](that: Thing[Y])(
        implicit
        typeClass: TypeClass[X, Y]
    ): typeClass.Out = typeClass.v

    def ++[Y, Z](that: Thing[Y])(
        implicit
        t1: TypeClass[X, Y] { type Out <: Z },
        t2: TypeClass[Z, Y]
    ): t2.Out =
      t2.v

//    def +++[Y, Z](that: Thing[Y])(implicit t1: TypeClass[X, Y] { type Out <: Y }, a: Any = this + that + that): a.type =
//      a
  }

  it("+") {

    assert(Thing(1) + Thing(2) == 2)

    val v = implicitly[TypeClass[Int, Int] { type Out <: Int }]

    assert(Thing(1).++(Thing(2)) == 2)
  }
}
