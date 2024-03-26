package com.tribbloids.spike.scala_spike.Reflection

import ai.acyclic.prover.commons.testlib.BaseSpec
import ai.acyclic.prover.commons.refl.Reflection.Runtime.{TypeTag, WeakTypeTag}
import shapeless.Witness

import scala.reflect.ClassTag

class InferTypeTag extends BaseSpec {

  import InferTypeTag._

  type U2 = (Int, String)

  it("from type") {

    implicitly[TypeTag[(Int, String)]]
    implicitly[ClassTag[(Int, String)]]
  }

  it("from path dependent type") {

    {
      implicitly[TypeTag[U1]]
      implicitly[ClassTag[U1]]
    }
    {
      implicitly[TypeTag[U2]]
      implicitly[ClassTag[U2]]
    }
  }

  it("from local type") {

    type U = (Int, String)

//    val ttg = implicitly[TypeTag[U]] // not working
    implicitly[ClassTag[U]]
  }

  it("from Witness") {

    implicitly[TypeTag[Witness.Lt[String]]]
    implicitly[ClassTag[Witness.Lt[String]]]
  }

  it("... from macro") {

    implicitly[TypeTag[Witness.`1`.T]]
    implicitly[ClassTag[Witness.`1`.T]]
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
