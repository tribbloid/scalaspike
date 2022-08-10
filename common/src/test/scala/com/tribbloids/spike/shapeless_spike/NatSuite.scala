package com.tribbloids.spike.shapeless_spike

import org.scalatest.funspec.AnyFunSpec
import shapeless.ops.{hlist, tuple}
import shapeless.{Generic, HList, Nat, Witness}

import scala.language.implicitConversions

class NatSuite extends AnyFunSpec {

  it("can encode large number") {

    val nat: Nat = Nat(100)

    println(nat)
    println(nat.getClass)

    //    println(classOf[nat.N])
  }

  //  it("can encode HUGE number") {
  // TODO: XAXA no it cannot

  //    val nat: Nat = Nat(999999999)
  //  }

  it("can be deducted from arity") {
    trait OfSize[A, N <: Nat]
    object OfSize {

      implicit def evidence[A, Repr <: HList, N <: Nat](
          implicit
          gen: Generic.Aux[A, Repr],
          length: hlist.Length.Aux[Repr, N]
      ): OfSize[A, N] = new OfSize[A, N] {}
    }

    case class HasLength[P <: Product, N <: Nat](v: P)(
        implicit
        proof: P OfSize N
    ) {

      type NN = N
    }

    val v = HasLength((1, 2, 3))

    println(Nat.toInt[v.NN])
  }

  it("... DIRECTLY") {
    trait OfSize[A, N <: Nat]
    object OfSize {

      implicit def evidence[A, N <: Nat](
          implicit
          length: tuple.Length.Aux[A, N]
      ): OfSize[A, N] = new OfSize[A, N] {}
    }

    case class HasLength[P <: Product, N <: Nat](v: P)(
        implicit
        proof: P OfSize N
    ) {

      type NN = N
    }

    val v = HasLength((1, 2, 3))

    println(Nat.toInt[v.NN])
  }

  it("can be the broker to deduce Witness from arity ") {

    case class OfSize[A, W <: Witness.Lt[Nat]]()
    object OfSize {

      implicit def evidence[A, N <: Nat](
          implicit
          length: tuple.Length.Aux[A, N]
      ): OfSize[A, Witness.Aux[N]] = OfSize[A, Witness.Aux[N]]()

    }

    case class HasLength[P <: Product, W <: Witness.Lt[Nat]](v: P)(
        implicit
        proof: P OfSize W
    ) {
      type WW = W

    }

    val v = HasLength((1, 2, 3))

    println()
  }
}

object NatSuite {}
