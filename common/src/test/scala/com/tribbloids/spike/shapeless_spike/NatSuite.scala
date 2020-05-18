package com.tribbloids.spike.shapeless_spike

import org.scalatest.FunSpec
import shapeless.Nat

object NatSuite {}

class NatSuite extends FunSpec {

  it("can encode large number") {

    val nat: Nat = Nat(100)

    println(nat)
  }

  it("can encode HUGE number") {
    // XAXA no it cannot

//    val nat: Nat = Nat(999999999)
//
//    println(nat)
  }
}
