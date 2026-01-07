package com.tribbloids.spike.zio_spike

import ai.acyclic.prover.commons.testlib.BaseSpec
import zio.blocks.schema.Schema

class ZIOBlocksSpec extends BaseSpec {

  case class Person(name: String, age: Int)

  object Person {
    implicit val schema: Schema[Person] = Schema.derived
  }

  it("inspection") {
    val s = Person.schema
    // println(s)
    val methods = s.getClass.getMethods.map(_.getName).mkString(", ")
    println(methods)

    // Note: zio-blocks-schema-json artifact (mentioned in README) is missing from Maven Central.
    // As of 0.0.1, JSON serialization support seems to be unavailable or requires manual Format implementation.
    // val encoded = s.encode[Json](person)
  }

  it("serialization") {

    val person = Person("John", 42)

//    val json = JsonCodec.encode(person)
//    println(json)
//
//    assert(json == """{"name":"John","age":42}""")
  }

  it("deserialization") {

    val json = """{"name":"John","age":42}"""

//    val person = JsonCodec.decode[Person](json)
//    println(person)
//
//    assert(person == Right(Person("John", 42)))
  }
}
