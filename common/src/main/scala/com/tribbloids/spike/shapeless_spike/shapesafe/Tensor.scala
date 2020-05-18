package com.tribbloids.spike.shapeless_spike.shapesafe

import com.tribbloids.spike.shapeless_spike.shapesafe.Shape.Can_*
import shapeless.Witness
import shapeless.syntax.SingletonOps

case class Tensor[S <: Shape]() {

  def *[R <: Shape](other: Tensor[R])(implicit can: Can_*[S, R]): Tensor[can.Out] = {

    Tensor[can.Out]()
  }
}

object Tensor {

//  def rand(v: Int): Tensor[Shape] = {
//
//    import shapeless.syntax.singleton._
//
//    val vv: Witness.Aux[SingletonOps#T] = v.witness
//
//    val vvv: SingletonOps#T = vv.value
//
//    ???
//  }
}
