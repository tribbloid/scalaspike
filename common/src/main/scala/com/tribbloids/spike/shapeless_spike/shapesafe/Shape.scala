package com.tribbloids.spike.shapeless_spike.shapesafe

trait Shape extends Product with Serializable {

  def nameOpt: Option[String] = None
}

object Shape {

  case object ShapeNil extends Shape

  case class :*:[L <: Axis, R <: Shape](left: L, right: R)

  trait Can_*[L <: Shape, R <: Shape] {

    type Out <: Shape
  }
}
