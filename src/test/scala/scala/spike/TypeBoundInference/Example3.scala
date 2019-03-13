//package scala.spike.TypeBoundInference
//
//object Example3 {
//
//  trait StaticGraph[T]
//
//  trait Impl {
//    type DD
//    type GG <: StaticGraph[DD]
//  }
//
//  def f[I <: Impl](): Unit = { implicitly[I#GG <:< StaticGraph[I#DD]] }
//}
