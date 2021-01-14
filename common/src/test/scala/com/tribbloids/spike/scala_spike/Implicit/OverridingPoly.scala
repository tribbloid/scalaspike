package com.tribbloids.spike.scala_spike.Implicit

import com.tribbloids.graph.commons.testlib.BaseSpec

class OverridingPoly extends BaseSpec {

  import OverridingPoly._

  it("implicits in companion is in scope") {

//    val hh = implicitly[Sys1.Handler[Int]]
    // Doesn't work
  }

  it("implicits in outer object is in scope") {

    val hh = implicitly[Sys1.Handler[Long]]
  }
}

object OverridingPoly {

  trait System {

    trait Handler[T]
  }

  object Sys1 extends System {

    object Handler {

      implicit def handle1: Handler[Int] = new Handler[Int] {}
    }

    implicit def handle2: Handler[Long] = new Handler[Long] {}
  }
}
