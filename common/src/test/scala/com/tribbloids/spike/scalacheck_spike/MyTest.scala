package com.tribbloids.spike.scalacheck_spike

import com.tribbloids.spike.scalacheck_spike.MyTest.Foo
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.Assertion
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class MyTest extends AnyFlatSpec with ScalaCheckPropertyChecks {

  it should "sample" in {
    val gen = Gen.alphaChar

//    val seed = Seed.

    val ss = gen.sample

    println(ss)
  }

  it should "work 1" in {
    implicit val arbString = Arbitrary(Gen.alphaStr)
    implicit val arb: Arbitrary[Foo] = Arbitrary(Gen.resultOf(Foo))

    val pp: Assertion = forAll { f: Foo =>
      println(f)

      f.a shouldBe "1"
    }

    pp
  }

  it should "work 2" in {
    implicit val arbString = Arbitrary(Gen.alphaStr)
    implicit val arb: Arbitrary[Foo] = Arbitrary(Gen.resultOf(Foo))

    val pp: Assertion = forAll { f: Foo =>
      println(f)

      f.a shouldBe "1"
    }

    pp
  }
}

object MyTest {

  case class Foo(a: String, b: Int)
}
