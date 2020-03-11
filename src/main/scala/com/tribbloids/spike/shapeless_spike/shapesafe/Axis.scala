//package com.tribbloids.spike.shapeless_spike.shapesafe
//
//import shapeless.{HNil, Witness}
//import shapeless.Witness.Lt
//
//import scala.language.implicitConversions
//
//trait ArityTypeMagnet[W <: Witness.Lt[Int]] extends Serializable {
//
//  def witness: W
//}
//
//object ArityTypeMagnet {
//
//  case class _Witness[W <: Witness.Lt[Int]](witness: W) extends Vector[W] {}
//
//  implicit def fromInt[W <: Int](v: W): _Witness[Lt[W]] = _Witness(Witness(v))
//
//  implicit def fromWitness[W <: Witness.Lt[Int]](witness: W): _Witness[W] = _Witness(witness)
//
//  implicit def fromH1(v: _ :: HNil) = _Witness(Witness(1))
//  implicit def fromH2(v: _ :: _ :: HNil) = _Witness(Witness(2))
//  implicit def fromH3(v: _ :: _ :: _ :: HNil) = _Witness(Witness(3))
//}
//
//trait Axis extends Serializable
//
//case object UnknownAxis extends Axis
//
//trait KnownAxis[W <: Witness.Lt[Int]] extends Axis {
//
//  def n: Int
//
//  def ++(that: KnownAxis[W]): Unit = {}
//}
//
//object KnownAxis {
//
//  object Attempt0 {
//
//    val w1: Lt[Int] = Witness(1)
//    val w2: Lt[Int] = Witness(2)
//
//    case class K1(n: Witness.`1`.T) extends KnownAxis[w1.type]
//    case class K2(n: Witness.`2`.T) extends KnownAxis[w2.type]
//
//    //  K2(2) ++ K1(1) // breaks
//
//    K2(2) ++ K2(2)
//
//  }
//
//  object Attempt1 {
//
//    case class KN[W <: Witness.Lt[Int]](magnet: Vector[W]) extends KnownAxis[W] {
//
//      val n = magnet.witness.value
//    }
//
//    val v1_simple: KN[Lt[Int]] = KN(1)
//
//    // looking good, works as intended
//    KN(1) ++ KN(1)
//    KN(Witness(1)) ++ KN(2)
//    KN(Witness(1)) ++ KN(Witness(2))
//
//    KN(1) ++ KN(2) // should break
//    KN(1) ++ KN(Witness(2)) // should break
//  }
//}
