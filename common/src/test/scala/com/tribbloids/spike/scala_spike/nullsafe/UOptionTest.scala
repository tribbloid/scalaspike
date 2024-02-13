package com.tribbloids.spike.scala_spike.nullsafe

import org.scalatest.funsuite.AnyFunSuite

class UOptionTest extends AnyFunSuite {
  import UOption._

  test("caveats ==") {
    assert(USome(5) == 5)
    assert(USome(USome(4)) == 4)
  }

  test("caveats isInstanceOf") {
    def test(x: Any): Int = x match {
      case _: Int        => 1
      case USome(_: Int) => 2 // warning here, so that's good
      case UNone         => 3
      case USome(UNone)  => 4 // warns, but in fact succeeds
    }

    assert(test(5) == 1)
    assert(test(USome(4)) == 1) // caveat here
    assert(test(UNone) == 3)
    assert(test(USome(UNone)) == 4)
  }

  test("isEmpty") {
    assert(UNone.isEmpty)
    assert(!USome(5).isEmpty)
    assert(!USome(UNone).isEmpty)
    assert(!USome(USome(UNone)).get.isEmpty)
    assert(USome(USome(UNone)).get.get.isEmpty)
  }

  test("isDefined") {
    assert(!UNone.isDefined)
    assert(USome(5).isDefined)
    assert(USome(UNone).isDefined)
    assert(USome(USome(UNone)).get.isDefined)
    assert(!USome(USome(UNone)).get.get.isDefined)
  }

  test("getOrElse") {
    assert(UNone.getOrElse(5) == 5)
    assert(UNone.getOrElse(UNone) == UNone)
    assert(UNone.getOrElse(USome(true)) == USome(true))

    assert(USome(3).getOrElse(5) == 3)
    assert(USome(USome(42)).getOrElse(5) == USome(42))
    assert(USome(UNone).getOrElse(42) == UNone)
    assert(USome(USome(UNone)).getOrElse(42) == USome(UNone))
  }

  test("map") {
    val noneInt: UOption[Int] = UNone
    assert(noneInt.map(_ + 1) == UNone)

    val someInt: UOption[Int] = USome(5)
    assert(someInt.map(_ + 1) == USome(6))

    val someSomeInt: UOption[UOption[Int]] = USome(USome(5))
    assert(someSomeInt.map(_.isDefined) == USome(true))
  }

  test("USome(UNone) can be told apart from UNone") {
    def test(opt: UOption[UOption[Int]]): UOption[Boolean] =
      opt.map(_.isDefined)

    assert(test(USome(USome(5))) == USome(true))
    assert(test(USome(UNone)) == USome(false))
    assert(test(UNone) == UNone)
  }

  test("pattern matching") {
    def test(opt: UOption[UOption[Int]]): Int = opt match {
      case UNone           => -1
      case USome(UNone)    => -2
      case USome(USome(x)) => x
    }

    assert(test(UNone) == -1)
    assert(test(USome(UNone)) == -2)
    assert(test(USome(USome(42))) == 42)
  }

  test("UOption constructor") {
    assert(UOption(null: String).isEmpty)
    assert(UOption("hello") == USome("hello"))
    assert(UOption(UNone: UOption[String]) == USome(UNone))
  }
}
