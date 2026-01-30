package com.tribbloids.spike.meta.circe

import org.scalatest.funspec.AnyFunSpec

import io.circe.Codec

trait Imp0 {

  implicit lazy val pojoCodec: Codec[AnyRef] = ??? // fallback to jackson here
}

object CirceFallbackExample extends Imp0 {

//  val e1 = implicitly[Encoder[User]] // TODO: neither works
//  val d1 = implicitly[Decoder[User]]
}

class CirceFallbackExample extends AnyFunSpec {

//  describe("roundtrip") {
//    it("1") {
//
////      case class Person(name: String)
////      case class Greeting(salutation: String, person: Person, exclamationMarks: Int)
////
////      val greeting = Greeting("Hey", Person("Chris"), 3)
////      val json = greeting.asJson
//
//      val user: User = User(
//        "a",
//        1
//      )
//
////      val c1Both = implicitly[Codec[User]]
//
////      val c2 = c1Both.or(c1Both)
//
//      val j1 = user.asJson.toString()
//      val back = parser
//        .parse(j1)
//        .map { json =>
//          json.as[User]
//        }
//
//      val user2 = back.flatten[Exception, User].toOption.get
//
//      val j2 = user2.asJson.toString()
//
//      // Example JSON strings with different field names
////      val json1 = """{"name": "John", "age": 30}"""
////      val json2 = """{"username": "John", "years": 30}"""
//
//      // Parse and decode both JSON formats
////      val result1 = parser.parse(json1).flatMap(_.as[User])
////      val result2 = parser.parse(json2).flatMap(_.as[User])
//
//      print_@(s"Parsing standard JSON: $j1")
//      print_@(s"Again: $j2")
//
//    }
//  }
}
