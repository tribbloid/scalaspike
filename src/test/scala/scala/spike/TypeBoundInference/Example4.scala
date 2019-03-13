package scala.spike.TypeBoundInference

object Example4 {

  trait Domain {}

  trait Impl {

    type DD <: Domain
    type GG <: StaticGraph[DD]

    final type Builder = Example4.Builder[DD, GG]
  }

  trait DSL[I <: Impl] {

    val impl: I#Builder
  }

  trait StaticGraph[T <: Domain] {}

  trait Builder[D <: Domain, G <: StaticGraph[D]] {}
}
