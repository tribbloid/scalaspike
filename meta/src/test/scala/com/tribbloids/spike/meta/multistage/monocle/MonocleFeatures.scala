package com.tribbloids.spike.meta.multistage.monocle

import ai.acyclic.prover.commons.testlib.BaseSpec

class MonocleFeatures extends BaseSpec {

  import monocle.syntax.all._
  import MonocleFeatures._

  it("plain") {

    anna
      .focus(_.name)
      .replace("Bob")
  }

  it("change type args") {

//    val b2 = b.focus(_.tail.aName).replace(2)
    // TODO: oops, doesn't work that way
    //  Cannot find method tail in com.tribbloids.spike.meta.multistage.monocle.MonocleFeatures.A#B
  }
}

object MonocleFeatures {

  case class User(name: String, address: Address)
  case class Address(streetNumber: Int, streetName: String)

  val anna = User("Anna", Address(12, "high street"))

  trait NamedTuple {

    class :*[H](head: H) extends Cons[this.type, H](this, head)

//    def *:[H](head: H): (H *: this.type) = *:(head, this)
  }

  object NamedTuple extends NamedTuple {}

  class Cons[T <: NamedTuple, H](val tail: T, val head: H) extends NamedTuple

  case class A(aName: Int) extends NamedTuple.:*(aName) {

    case class B(bName: String) extends :*(bName)
  }

  val b: A#B = A(1).B("b")
}
