package com.tribbloids.spike.shapeless_spike.shapesafe

trait Shape extends Product with Serializable {

  def nameOpt: Option[String] = None
}

object Shape {

  trait Dim extends Product with Serializable

  case object Unknown extends Dim

  case object `1` extends Dim
  case object `2` extends Dim
  case object `3` extends Dim

  case object ShapeNil extends Shape

  case class :*:[L <: Dim, R <: Shape](left: L, right: R)
}
