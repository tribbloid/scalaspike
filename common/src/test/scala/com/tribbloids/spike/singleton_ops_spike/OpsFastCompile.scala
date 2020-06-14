package com.tribbloids.spike.singleton_ops_spike

import com.tribbloids.spike.BaseSpec
import shapeless.ops.hlist
import shapeless.{HList, Nat}
import singleton.ops.{SafeInt, ToInt}

class OpsFastCompile extends BaseSpec {

  object big {

    val nat = Nat(100)
  }

  it("summon Nat => ToInt") {

    val op1 = implicitly[ToInt[big.nat.N]]

    def getOps[D <: HList, T <: Nat](v: D)(
        implicit
        ofLength: hlist.Length.Aux[D, T],
        asInt: ToInt[T]
    ): ToInt[T] = {
      asInt
    }

    val filled = HList.fill[Double](100)(0.0)

    val op2 = getOps(filled)
  }

  // a singletonOps bug triggered a stackoverflow on this one
//  it("summon Nat => SafeInt") {
//
//    val op1 = implicitly[SafeInt[big.nat.N]]
//
//    def getOps[D <: HList, T <: Nat](v: D)(
//        implicit
//        ofLength: hlist.Length.Aux[D, T],
//        asInt: SafeInt[T]
//    ): SafeInt[T] = {
//      asInt
//    }
//
//    val filled = HList.fill[Double](100)(0.0)
//
//    val op2 = getOps(filled)
//  }
}
