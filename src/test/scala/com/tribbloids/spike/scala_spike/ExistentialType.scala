package com.tribbloids.spike.scala_spike

import scala.language.{existentials, reflectiveCalls}
import ExistentialType._

/**
  * Created by peng on 31/03/16.
  */
class ExistentialType {

  trait LocalBuilder[D <: Domain]
      extends Builder[D,
                      G forSome {
                        type G <: LocalGraph[D]
                      }]

  object BuilderImpl extends LocalBuilder[MLDomain] {

    override val vv: LinearTimeGraph[MLDomain] = null
  }

  trait AnyBuilder[D <: Domain] extends Builder[D, G forSome { type G <: StaticGraph[D] }]
  object BuilderImpl2 extends AnyBuilder[MLDomain] {
    override val vv: RDDGraph[MLDomain] = null
  }

  class MLNodeFn(d: i.NodeType forSome { val i: MLDomain })

  //this will fail
//  trait AnyBuilder2[D <: Domain] extends Builder[D, _]
//  object BuilderImpl3 extends AnyBuilder2[MLDomain] {
//    override val vv: RDDGraph[MLDomain] = ???
//  }
}

object ExistentialType {

  trait Domain {

    type NodeType
  }

  trait MLDomain extends Domain {}

  trait StaticGraph[T <: Domain] {}

  trait LocalGraph[T <: Domain] extends StaticGraph[T] {}
  trait LinearTimeGraph[T <: Domain] extends LocalGraph[T] {}

  trait RDDGraph[T <: Domain] extends StaticGraph[T]

  trait Builder[D <: Domain, G <: StaticGraph[D]] {

    val vv: G
  }

//  trait TT {
//    val x: { type T }
//    val y: x.T // path dependent type: here comes our val
//  }

  type SomeList = List[v.T] forSome { val v: { type T } }

  object X {
    type T = String

    val x: T = "hello"
  }

  val yy: ExistentialType.X.T = X.x
  val yy2: X.T = X.x

  val l1: SomeList = List(yy, yy) // compiles fine

//  val l2: SomeList = List(yy2, yy2) // does not compile
//  val l3: SomeList = List(yy, yy2) // does not compile

//  val l2: SomeList = List("hello")
//  val l2: SomeList = List(123) // does not compile
}
