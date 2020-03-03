package com.tribbloids.spike.shapeless_spike.shapesafe

import shapeless.Witness

trait Axis extends Serializable

case object UnknownAxis extends Axis

trait KnownAxis[W <: Witness.Lt[Int]] extends Axis {

  def n: Int

  def ++(that: KnownAxis[W]): Unit = {}
}

object KnownAxis {

  val w1 = Witness(1)
  val w2 = Witness(2)

  case class K1(n: Witness.`1`.T) extends KnownAxis[w1.type]
  case class K2(n: Witness.`2`.T) extends KnownAxis[w2.type]

//  K2(2) ++ K1(1) // doesn't compile

  K2(2) ++ K2(2)

  // more complex

//  case class KN[S <: Int, W <: Witness.Aux[Int]](n: S)(implicit ev: S => W) extends KnownAxis[W]

//  val arch = Seq(w1.value, w2.value)

  case class KN[W <: Witness.Lt[Int]](n: W#T) extends KnownAxis[W]

  KN(1)
}

//object Axis {

//  val ii = Witness.`1`
//
//  val _1 = KnownAxis(1)
//
//  val _2 = KnownAxis(2)
//
//  val also2 = KnownAxis(2)
//
////  print(_1.w)
////
////  print(_2.w)
//
//  _1 + _2 // this should break compilation
//
//  _2 + also2
//}
// use Witness as a type parameter
