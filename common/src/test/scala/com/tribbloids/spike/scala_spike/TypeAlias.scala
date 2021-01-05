package com.tribbloids.spike.scala_spike

import com.tribbloids.graph.commons.testlib.BaseSpec
import com.tribbloids.graph.commons.util.viz.VizType

class TypeAlias extends BaseSpec {
  import TypeAlias._

  it("example") {

    VizType[String].toString.shouldBe()

    VizType[Alias].toString.shouldBe()
  }
}

object TypeAlias {

  type Alias = String
}
