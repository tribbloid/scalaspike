//package scala.spike.TypeBoundInference
//
//object Example1 {
//
//  trait Domain {}
//
//  trait Impl {
//
//    type DD <: Domain
//    type GG <: StaticGraph[DD]
//
//    final type Builder = Example1.Builder[DD, GG]
//  }
//
//  trait DSL[I <: Impl] {
//
//    val impl: Builder[I#DD, I#GG]
//  }
//
//  trait StaticGraph[T <: Domain] {}
//
//  trait Builder[D <: Domain, G <: StaticGraph[D]] {}
//}
