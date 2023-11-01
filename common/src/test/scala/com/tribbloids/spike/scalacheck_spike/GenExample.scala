package com.tribbloids.spike.scalacheck_spike

import org.scalacheck.{Gen, Prop}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class GenExample extends AnyFlatSpec with ScalaCheckPropertyChecks {

  "An IP4 address" should "belong to just one class" in {

    val addrs: Gen[String] = for {
      a <- Gen.choose(0, 255)
      b <- Gen.choose(0, 255)
      c <- Gen.choose(0, 255)
      d <- Gen.choose(0, 255)
    } yield s"$a.$b.$c.$d"

    val prop = Prop.forAll(addrs) { addr =>
      addr.length == 8
    }

    prop
  }
}
