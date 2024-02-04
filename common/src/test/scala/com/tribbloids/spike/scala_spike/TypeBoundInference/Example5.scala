package com.tribbloids.spike.scala_spike.TypeBoundInference

object Example5 {

  trait Domain {}
  trait D1 extends Domain

  trait Impl {

    type DD <: Domain
    type GG[T <: Domain] <: StaticGraph[T]
  }

  trait DSL[I <: Impl] {

    val impl: Builder[I#DD, I#GG]
  }

  trait StaticGraph[T <: Domain] {}

  trait Builder[D <: Domain, G[T <: Domain] <: StaticGraph[T]] {}
}
