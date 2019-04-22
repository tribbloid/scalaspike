package scala.spike.TypeBoundInference

import scala.language.existentials

object Example1 {

  trait Domain {}
  trait D1 extends Domain

  trait Impl {

    type DD <: Domain
    type GG <: StaticGraph[DD]
  }

  trait DSL[I <: Impl] {

    //this will fail
//    val impl: Builder[I#DD, I#GG]

    val impl: Builder[i.DD, i.GG] forSome { val i: Impl }
  }

  trait StaticGraph[T <: Domain] {}

  trait Builder[D <: Domain, G <: StaticGraph[D]] {}

  val i1: Impl = new Impl {

    type DD = Domain
    type GG = StaticGraph[Domain]
  }

  val i2: Impl = new Impl {

    type DD = D1
    type GG = StaticGraph[D1]
  }
}
