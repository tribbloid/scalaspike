package com.tribbloids.spike.scala_spike.Implicit

import com.tribbloids.graph.commons.testlib.BaseSpec

class Precedence extends BaseSpec {

  it("can override + in Predef") {

    case class A(v: Int)

    object A {

      implicit class AFns(self: A) {

        def +(v2: Int): A = A(self.v + v2)
      }
    }

    val a1 = A(3)

    {

      // this caused the following error
      // found   : Int(4)
      // required: String
      //one error found

//      val b = a1 + 4
//
//      assert(b == A(7))
    }
  }
}
