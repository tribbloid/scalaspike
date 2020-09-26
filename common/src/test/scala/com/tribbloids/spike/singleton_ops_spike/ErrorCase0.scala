package com.tribbloids.spike.singleton_ops_spike

import shapeless.Witness
import singleton.ops.+

import scala.language.implicitConversions

object ErrorCase0 {

  trait Proof extends Serializable {

    type Out <: Arity
    def out: Out
  }

  trait Arity extends Proof {

    override type Out = this.type
    override def out: Out = this
  }

  object Arity {

    trait Const[S] extends Arity with Proof {

      type SS = S
    }

    case class FromLiteral[S <: Int]() extends Const[S] {}
  }

  val v1 = Arity.FromLiteral[Witness.`6`.T]()
  implicitly[v1.SS + Witness.`3`.T]

  val v2 = v1.out
//  implicitly[v2.SS + Witness.`3`.T]
}
