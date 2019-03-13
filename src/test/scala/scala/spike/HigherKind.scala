package scala.spike

import scala.language.higherKinds
import scala.spike.HigherKind.{LocalGraph, MLDomain}

/**
  * Created by peng on 31/03/16.
  */
class HigherKind {

  object builder extends HigherKind.Builder[MLDomain, LocalGraph] {

    override val vv: LocalGraph[MLDomain] = ???
  }
}

object HigherKind {

  trait Domain {}

  trait MLDomain extends Domain {}

  trait StaticGraph[T <: Domain] {}

  trait LocalGraph[T <: Domain] extends StaticGraph[T] {}

  trait Builder[D <: Domain, G[T <: Domain] <: StaticGraph[T]] {

    val vv: G[D]
  }
}
