package com.tribbloids.spike.shapeless_spike.shapesafe

import shapeless.{Generic, Witness}

import scala.language.implicitConversions

trait Vector[W <: Witness.Lt[Int]] extends Serializable {

  def witness: W

  def <*>(that: Vector[W]): Vector[W] = ???
}

object Vector {

  case class Impl[W <: Witness.Lt[Int]](witness: W) extends Vector[W] {}

  def zeros[W <: Witness.Lt[Int]](witness: W): Impl[W] = Impl(witness)

  object Attempt1 {

    def values(v: (Double)) = Impl(Witness(1))
    def values(v: (Double, Double)) = Impl(Witness(2))
    def values(v: (Double, Double, Double)) = Impl(Witness(3))

    Vector.zeros(Witness(2)) <*> values(2.0, 3.0)

    Vector.zeros(Witness(3)) <*> values(2.0, 3.0) // breaks as expected
  }

  // ...

  object Attempt2 {
    type HInts = Int :: HInts

    def values[T <: Product](v: T)(implicit aux: Generic.Aux[T, HInts]) = {
      ???
    }
  }
}
