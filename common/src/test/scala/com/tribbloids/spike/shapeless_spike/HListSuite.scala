package com.tribbloids.spike.shapeless_spike

import ai.acyclic.prover.commons.testlib.BaseSpec
import shapeless.{Generic, HList, HNil}

class HListSuite extends BaseSpec {

  it("from tuple") {

    val tuple = (1, 2, 3, 4)

    val hh = HList.apply(tuple)

    println(hh)
  }

  def getHList[P <: Product, F, L <: HList](p: P)(
      implicit
      gen: Generic.Aux[P, L]
  ): L = {
    gen.to(p)
  }

  it("from varargs") {

    //    import shapeless.syntax.std.function._
    //    import shapeless.ops.function._

    getHList(1, 2, 3, 4, 5, 6, 7, 8, 9)
  }

  // TODO: need example for ProductArgs

  it("can be mapped with T => R, where T is the common type") {

    11 :: 22 :: 33 :: HNil

    //    val hh2 = hh.map { v: Int =>
    //      v + 1
    //    } //TODO: why it doesn't work?

    //    println(hh2)
  }
}
