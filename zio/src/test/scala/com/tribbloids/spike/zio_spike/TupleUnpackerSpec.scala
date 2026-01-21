package com.tribbloids.spike.zio_spike

import ai.acyclic.prover.commons.testlib.BaseSpec

class TupleUnpackerSpec extends BaseSpec {

  def check[A, ExpectedHead, ExpectedTail](
      implicit
      u: TupleUnpacker.Aux[A, ExpectedHead, ExpectedTail]
  ): Unit = {}

  describe("TupleUnpacker") {

    it("unpacks A to Head=A, Tail=Unit") {
      check[Int, Int, Unit]
      check[String, String, Unit]
    }

    it("unpacks (A, B) to Head=A, Tail=B") {
      check[(Int, String), Int, String]
      check[(String, Int), String, Int]
    }

    it("unpacks (A, B, C) to Head=A, Tail=(B, C)") {
      check[(Int, String, Double), Int, (String, Double)]
    }

    it("unpacks (A, B, C, D) to Head=A, Tail=(B, C, D)") {
      check[(Int, String, Double, Boolean), Int, (String, Double, Boolean)]
    }

    it("unpacks nested tuples correctly") {
      // ( (A,B), C ) -> Head=(A,B), Tail=C
      check[((Int, String), Double), (Int, String), Double]
    }
  }
}
