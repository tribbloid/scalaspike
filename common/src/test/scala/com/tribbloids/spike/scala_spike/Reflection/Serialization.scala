package com.tribbloids.spike.scala_spike.Reflection

import ai.acyclic.prover.commons.meta.ScalaReflection
import ai.acyclic.prover.commons.meta.ScalaReflection.TypeTag
import ai.acyclic.prover.commons.testlib.BaseSpec

import java.io.{ByteArrayOutputStream, ObjectOutputStream}

class Serialization extends BaseSpec {

  describe("can serialize and deserialize") {

    it("TypeTag") {

      val bOStream = new ByteArrayOutputStream()
      val oOStream = new ObjectOutputStream(bOStream)
      Serialization.ttg.tpe
      oOStream.writeObject(Serialization.ttg)
    }

    it("Type") {

      val bOStream = new ByteArrayOutputStream()
      val oOStream = new ObjectOutputStream(bOStream)
      oOStream.writeObject(Serialization.tp)
    }

  }
}

object Serialization {

  val ttg: ScalaReflection.TypeTag[Seq[String]] = {
    implicitly[TypeTag[Seq[String]]]
  }

  val tp: ScalaReflection.universe.Type = ttg.tpe
}
