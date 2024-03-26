package com.tribbloids.spike.shapeless_spike

import ai.acyclic.prover.commons.refl.Reflection.Runtime.TypeTag
import shapeless.ops.record.Values

object RecordProblem2 {

  import shapeless._
  import record._
  import syntax.singleton._

  val book =
    ("author" ->> "Benjamin Pierce") ::
      ("title" ->> "Types and Programming Languages") ::
      ("id" ->> 262162091) ::
      ("price" ->> 44.11) ::
      HNil

  val v1 = book.values

  assert(v1.head == "Benjamin Pierce")

  case class HasValues[T <: HList: TypeTag](v: T) {

    type TT = T
  }

  val hv = HasValues(book)

  val _vs = implicitly[Values[hv.TT]]
  val _vs2: Values.Aux[hv.TT, _vs.Out] = _vs

  val v2 = book.values(_vs2)

//  assert(v2.head == "Benjamin Pierce")

  def main(args: Array[String]): Unit = {}
}
