package com.tribbloids.spike.singleton_ops_spike

import com.tribbloids.spike.BaseSpec
import shapeless.{Nat, Witness}
import shapeless.test.illTyped
import singleton.ops.{*, +, ==, >, Require, SafeInt}

class OpsExample extends BaseSpec {

  import singleton.ops.W

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

    illTyped {
      "MyVec.mustBeEqual(myVec, myVec3)"
    }

    illTyped {
      "MyVec[W.`-1`.T]"
    }

    //    val myVec: MyVec[W.`10`.T] = MyVec[W.`4`.T + W.`1`.T].doubleSize  // implicit cast disabled, too slow for the compiler
  }

  it("Witness.Lt[Nat] should be interoperable with Witness.Lt[Int]") {

    implicitly[Require[Nat._3 == W.`3`.T]]

    val v1 = Vec.apply[Nat._3]()
    val v2 = Vec.apply[W.`3`.T]()
    val v3 = Vec[W.`9`.T]

    Vec.dot_*(v1, v2)

    illTyped {
      "Vec.dot_*(v1, v3)"
    }
  }

  it("value can be summoned from result type") {

    type T1 = W.`3`.T

    val v1 = implicitly[Witness.Aux[T1]]
    assert(v1.value == 3)

    type T2 = T1 + W.`4`.T

    val v2 = implicitly[T2]
    assert(v2.value == 7)
    assert(v2.isLiteral)
    assert(v2.valueWide == 7)
  }

  describe("conversion can accelerate compile-time computations") {

    it("1") {
      type Big = Nat._3

      type Small = Witness.`3`.T

      type R = Require[Big == Small]

      implicitly[R]
    }

    it("2") {

      val nat = Nat(3)

      type Big = nat.N

      type Small = Witness.`3`.T

      type Sum = Witness.`6`.T
      type NotSum = Witness.`7`.T

      implicitly[Require[Big + Small == Sum]]

      illTyped {
        "implicitly[Require[Big + Small == NotSum]]"
      }
    }

    it("3") {

      val nat = Nat(100)

      type Big = nat.N

      type Small = Witness.`3`.T

      type Sum = Witness.`103`.T
      type NotSum = Witness.`105`.T

      implicitly[Require[Big + Small == Sum]]

      illTyped {
        "implicitly[Require[Big + Small == NotSum]]"
      }
    }
  }
}
