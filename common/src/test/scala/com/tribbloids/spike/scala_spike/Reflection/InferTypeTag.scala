package com.tribbloids.spike.scala_spike.Reflection

import ai.acyclic.prover.commons.testlib.BaseSpec
import ai.acyclic.prover.commons.viz.TypeViz.{TypeTag, WeakTypeTag}
import shapeless.Witness

import scala.reflect.ClassTag

class InferTypeTag extends BaseSpec {

  import InferTypeTag._

  type U2 = (Int, String)

  it("from type") {

    val ttg = implicitly[TypeTag[(Int, String)]]
    val ctg = implicitly[ClassTag[(Int, String)]]
  }

  it("from path dependent type") {

    {
      val ttg = implicitly[TypeTag[U1]]
      val ctg = implicitly[ClassTag[U1]]
    }
    {
      val ttg = implicitly[TypeTag[U2]]
      val ctg = implicitly[ClassTag[U2]]
    }
  }

  it("from local type") {

    type U = (Int, String)

//    val ttg = implicitly[TypeTag[U]] // not working
    val ctg = implicitly[ClassTag[U]]
  }

  it("from Witness") {

    val ttg = implicitly[TypeTag[Witness.Lt[String]]]
    val ctg = implicitly[ClassTag[Witness.Lt[String]]]
  }

  it("... from macro") {

    val ttg = implicitly[TypeTag[Witness.`1`.T]]
    val ctg = implicitly[ClassTag[Witness.`1`.T]]
  }

  it("... doesn't work") {

//    val ttg = implicitly[TypeTag[w1.T]]
//    val ctg = implicitly[ClassTag[w2.T]]
  }

  it("works but ...") {

//    val ttg = implicitly[WeakTypeTag[w1.T]]

    val w1: Witness.Lt[Int] = Witness(1)
    val v1 = w1.value

    def infer[T](v: T)(
        implicit
        ev: WeakTypeTag[T]
    ) = ev

    val tag = infer(v1)

    tag.tpe
  }
}

object InferTypeTag {

  type U1 = (Int, String)

  val w1: Witness.Lt[Int] = Witness(1)
  val w2 = Witness(2)
}
