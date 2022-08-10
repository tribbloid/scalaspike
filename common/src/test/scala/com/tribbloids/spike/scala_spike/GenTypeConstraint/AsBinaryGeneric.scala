package com.tribbloids.spike.scala_spike.GenTypeConstraint

class AsBinaryGeneric {}

object AsBinaryGeneric {

  trait TakeAny[A <: AnyVal] {

    def v: A

    def assertInt[B](
        implicit
        ev: A <:< Int
    ): Unit = {}
  }

  object TakeInt extends TakeAny[Int] {
    override def v: Int = 1
  }

  TakeInt.assertInt

  // this will fail
  //  object TakeLong extends TakeAny[Long] {
  //    override def v: Long = 1L
  //  }
  //  TakeLong.assertInt
}
