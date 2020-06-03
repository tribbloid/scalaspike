package com.tribbloids.spike.singleton_ops_spike

import com.tribbloids.spike.BaseSpec
import shapeless.Nat

class OpsExample extends BaseSpec {

  import singleton.ops._

  class Vec[L] {
    def doubleSize = new Vec[W.`2`.T * L]

    def nSize[N] = new Vec[N * L]

    def getLength(implicit length: SafeInt[L]): Int = length
  }

  object Vec {
    def apply[L]()(implicit check: Require[L > W.`0`.T]): Vec[L] = new Vec[L]()

    def dot_*[L1, L2](v1: Vec[L1], v2: Vec[L2])(implicit ev: Require[L1 == L2]): Unit = {}
  }

  it("simple example") {

    val v1 = Vec.apply[W.`4`.T + W.`1`.T]().doubleSize
    val v2 = Vec[W.`10`.T]
    val v3 = Vec[W.`9`.T]

    Vec.dot_*(v1, v2)

    //    MyVec.mustBeEqual(myVec, myVec3) // fails, as required

    //    val myVec: MyVec[W.`10`.T] = MyVec[W.`4`.T + W.`1`.T].doubleSize  // implicit cast disabled, too slow for the compiler
    //    val myBadVec = MyVec[W.`-1`.T] //fails compilation, as required
  }

  it("Witness.Lt[Nat] should be interoperable with Witness.Lt[Int]") {

    implicitly[Require[Nat._3 == W.`3`.T]]

    val v1 = Vec.apply[Nat._3]()
    val v2 = Vec.apply[W.`3`.T]()
    val v3 = Vec[W.`9`.T]

    Vec.dot_*(v1, v2)

    //    Vec.dot_*(v1, v3) // fails, as required
  }
}
