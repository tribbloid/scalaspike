package com.tribbloids.spike.shapeless_spike

import ai.acyclic.prover.commons.meta2.Reflection.Runtime.TypeTag
import shapeless.ops.record.Values

object RecordProblem {

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

//    def vs(implicit v: Values[T]) = v // doesn't work
    def vs(
        implicit
        v: Values[T]
    ): Values.Aux[T, v.Out] = v // works
  }

  val _vs = HasValues(book).vs

  val v2 = book.values(_vs)

  assert(v2.head == "Benjamin Pierce")

  def main(args: Array[String]): Unit = {}
}
