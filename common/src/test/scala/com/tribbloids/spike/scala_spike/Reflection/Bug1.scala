package com.tribbloids.spike.scala_spike.Reflection

import com.tribbloids.graph.commons.util.ScalaReflection.WeakTypeTag
import shapeless.Witness

object Bug1 {

  val w1: Witness.Lt[Int] = Witness(1)
  val v1 = w1.value

  def main(args: Array[String]): Unit = {
    def infer[T](v: T)(implicit ev: WeakTypeTag[T]): WeakTypeTag[T] = ev

    val tag = infer(v1)

    tag.tpe
  }

}
