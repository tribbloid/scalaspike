package com.tribbloids.spike.scala_spike.PathDependentType

import ai.acyclic.prover.commons.testlib.BaseSpec

class ProveSameType extends BaseSpec {

  trait SS {

    type II
  }

  class SS1() extends SS {}

  def sameType[T1, T2](
      implicit
      ev: T1 =:= T2
  ): Unit = {}

  it("1 object, 2 variables, their dependent type should be identical") {

    val a = new SS1()

    val b = new SS1()

    val c = a

    sameType[a.II, a.II]

    assertDoesNotCompile(
      """
        |sameType[a.II, b.II]
        |""".stripMargin
    ) // of course it happens

//    sameType[a.II, c.II] // really?
  }
}

object ProveSameType {}
