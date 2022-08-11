package com.tribbloids.spike.scala_spike.Annotation

import ai.acyclic.graph.commons.testlib.BaseSpec
import shapeless.{Annotation, Annotations}

class ShapelessAnnotation extends BaseSpec {

  import Static._
  import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

  it("singular") {

//    val a1 = Annotation.materialize[SA1, Common].apply()
    val a1 = Annotation[SA1, Common].apply()

    a1 shouldBe SA1()

    println(a1)
  }

  it("plural") {
//    println(Annotations.apply[SA2, Common].apply()) // doesn't work

    println(Annotations.apply[SA1, Prod].apply())
    println(Annotations.apply[SA2, Prod].apply())
  }
}
