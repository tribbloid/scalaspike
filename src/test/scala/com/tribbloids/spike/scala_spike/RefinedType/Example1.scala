package com.tribbloids.spike.scala_spike.RefinedType

object Example1 {

  val `2d` = Dim(2)
  val `3d` = Dim(3)

  trait SU_n[D <: Dim] {

    def plus(v1: Quaternion, v2: Quaternion)(
        implicit ev: D <:< `2d`.type
    ): Quaternion = Quaternion(v1.w * v2.w)
  }

  case class Quaternion(w: Int) {}

  case class Dim(d: Int) {}

  val alsoThreeD = Dim(3)

  object SU_2 extends SU_n[alsoThreeD.type] {}
  object SU_3 extends SU_n[`3d`.type] {}

  //TODO: how to fix it?
  {
//    val v2 = SU_2.plus(Quaternion(1), Quaternion(2))
//
//    val v3 = SU_3.plus(Quaternion(1), Quaternion(2))
  }
}
