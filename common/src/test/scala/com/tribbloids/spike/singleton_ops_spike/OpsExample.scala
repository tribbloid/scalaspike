package com.tribbloids.spike.singleton_ops_spike

import ai.acyclic.prover.commons.testlib.BaseSpec
import shapeless.test.illTyped
import shapeless.{Nat, Witness}
import singleton.ops.*

class OpsExample extends BaseSpec {

  class Vec[L] {
    type L2 = 2 * L

    def doubleSize = new Vec[L2]

    def nSize[N] = new Vec[N * L]

    def getLength(
        implicit
        length: SafeInt[L]
    ): Int = length
  }

  object Vec {
    def apply[L]()(
        implicit
        check: Require[L > 0]
    ): Vec[L] = new Vec[L]()

    def dot_*[L1, L2](v1: Vec[L1], v2: Vec[L2])(
        implicit
        ev: Require[L1 == L2]
    ): Unit = {}
  }

  it("simple example") {

    val v1 = Vec.apply[4 + 1]().doubleSize
    val v2 = Vec[10]()
    Vec[9]()

    Vec.dot_*(v1, v2)

    illTyped {
      "MyVec.mustBeEqual(myVec, myVec3)"
    }

    illTyped {
      "MyVec[-1]"
    }

    //    val myVec: MyVec[10] = MyVec[4 + 1].doubleSize  // implicit cast disabled, too slow for the compiler
  }

  it("Nat should be interoperable with Int singleton") {

    implicitly[Require[Nat._3 == 3]]

    val v1 = Vec.apply[Nat._3]()
    val v2 = Vec.apply[3]()
    Vec[9]()

    Vec.dot_*(v1, v2)

    illTyped {
      "Vec.dot_*(v1, v3)"
    }
  }

  it("value can be summoned from result type") {

    type T1 = 3

    val v1 = implicitly[Witness.Aux[T1]]
    assert(v1.value == 3)

    type T2 = T1 + 4

    val v2 = implicitly[T2]
    assert(v2.value == 7)
    assert(v2.isLiteral)
    assert(v2.valueWide == 7)
  }

  describe("conversion can accelerate compile-time computations") {

    it("1") {
      type Big = Nat._3

      type Small = 3

      type R = Require[Big == Small]

      implicitly[R]
    }

    it("2") {

      val nat = Nat(3)

      type Big = nat.N

      type Small = 3

      type Sum = 6
      type NotSum = 7

      implicitly[Require[Big + Small == Sum]]

      illTyped {
        "implicitly[Require[Big + Small == NotSum]]"
      }
    }

    it("3") {

      val nat = Nat(100)

      type Big = nat.N

      type Small = 3

      type Sum = 103
      type NotSum = 105

      implicitly[Require[Big + Small == Sum]]

      illTyped {
        "implicitly[Require[Big + Small == NotSum]]"
      }
    }
  }
}
