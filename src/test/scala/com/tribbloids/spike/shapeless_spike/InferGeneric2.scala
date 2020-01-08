//package com.tribbloids.spike.shapeless_spike
//
//object InferGeneric2 {
//
//  trait Generic[A] { def mk: A }
//
//  class WithGeneric[T](ev: Generic[T])
//
//  case class Impl()
//
//  object Impl
//      extends WithGeneric[Impl](
//        new Generic[Impl] { def mk: Impl = Impl() }
//      )
//}
