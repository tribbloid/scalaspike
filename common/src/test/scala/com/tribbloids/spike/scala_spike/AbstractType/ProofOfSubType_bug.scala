package com.tribbloids.spike.scala_spike.AbstractType

import ai.acyclic.graph.commons.testlib.BaseSpec

class ProofOfSubType_bug extends BaseSpec {

  it("can infer") {

    trait Super1[S] {

      final type Out = this.type
      final val out: Out = this
    }

    trait Super2[S] extends Super1[S] {

      final type SS = S
    }

    case class A[S](k: S) extends Super2[S] {}

    val a = A("abc")

    implicitly[a.type =:= a.out.type]
    // success

    implicitly[a.Out =:= a.out.Out]
    // success

//    implicitly[a.SS =:= a.out.SS]
//    implicitly[a.SS <:< a.out.SS]
//    implicitly[a.out.SS <:< a.SS]
    // oops
  }
}
