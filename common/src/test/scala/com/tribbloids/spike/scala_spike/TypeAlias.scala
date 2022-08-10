package com.tribbloids.spike.scala_spike

import ai.acyclic.graph.commons.testlib.BaseSpec
import ai.acyclic.graph.commons.viz.TypeViz

class TypeAlias extends BaseSpec {
  import TypeAlias._

  it("example") {

    TypeViz[String].toString.shouldBe()

    TypeViz[Alias].toString.shouldBe()
  }
}

object TypeAlias {

  type Alias = String
}
