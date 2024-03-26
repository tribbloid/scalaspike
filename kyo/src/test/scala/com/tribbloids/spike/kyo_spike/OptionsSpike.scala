package com.tribbloids.spike.kyo_spike

import ai.acyclic.prover.commons.testlib.BaseSpec
import kyo.{<, Options}

class OptionsSpike extends BaseSpec {

  it("") {

    val v: Int < Options = 3

//    implicitly[v.type <:< Int] // oops
  }
}
