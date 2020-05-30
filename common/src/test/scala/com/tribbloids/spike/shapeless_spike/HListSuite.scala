package com.tribbloids.spike.shapeless_spike

import com.tribbloids.spike.BaseSpec
import shapeless.HNil

class HListSuite extends BaseSpec {

  it("can be mapped with T => R, where T is the common type") {

    import shapeless.poly._

    val hh = 11 :: 22 :: 33 :: HNil

    //    val hh2 = hh.map { v: Int =>
    //      v + 1
    //    } //TODO: why it doesn't work?

    //    println(hh2)
  }
}
