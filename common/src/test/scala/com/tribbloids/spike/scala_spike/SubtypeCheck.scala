package com.tribbloids.spike.scala_spike

object SubtypeCheck {

  trait Conj

  trait Topology

  trait Arrow

  abstract class GraphBuilder[T <: Topology](val topology: T) {

    type Conj_/\

    type _Arrow <: Arrow

    type _Conj = Conj_/\ { type _Arrow = GraphBuilder.this._Arrow }
  }

  object GraphBuilder {

    type Of[C <: Conj] = GraphBuilder[_] { type Conj_/\ <: C }

    def sanity[C <: Conj]: Unit = { // sanity

      val example: Of[C] = ???
s
      implicitly[example._Conj <:< example.Conj_/\]

      implicitly[example.Conj_/\ <:< C]

      implicitly[example._Conj <:< C]
    }
  }

}
