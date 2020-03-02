package com.tribbloids.spike.shapeless_spike.shapesafe

import shapeless.Witness

trait Axis extends Serializable

case object UnknownAxis extends Axis

//case object `1` extends Axis
//case object `2` extends Axis
//case object `3` extends Axis

case class KnownAxis1[S <: Int](s: S) extends Axis {

  val w: Witness.Lt[S] = Witness(s) // useless, runtime only

//  type V = w.T
}

case class KnownAxis2[S <: Int, W <: Witness.Lt[S]](s: S) extends Axis {

  val w: Witness.Lt[S] = Witness(s)
}

object Axis {

  val _1 = KnownAxis1(1)

  val _2 = KnownAxis2(2)

  print(_1.w)

  print(_2.w)

  def main(args: Array[String]): Unit = {}

//  val w: Lt[Int] = Witness(1)

//  def getSingleton[W <: Witness.Aux[Int]](v: Int): KnownAxis1[W] = {
//
//    val ww = v.witness
//
//    val vvv = ww.value
//
//    val result = new KnownAxis1[W](ww)
//
//    result
//  }
}
