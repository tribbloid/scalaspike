package com.tribbloids.spike.generic.jsoniter

import ai.acyclic.prover.commons.debug.print_@
import org.scalatest.funspec.AnyFunSpec
import com.github.plokhotnyuk.jsoniter_scala.macros.*
import com.github.plokhotnyuk.jsoniter_scala.core.*

import java.nio.file.Path

trait Imp0 {

  case class User(
      name: String,
      age: Int,
      path: Path = Path.of("http://google.com")
  )

  implicit def pojoCodec[T <: AnyRef]: JsonValueCodec[T] = {

    print_@("pojo")
    ???
  } // fallback to jackson here

  implicit lazy val pojoCodec2: JsonValueCodec[AnyRef] = {

    print_@("pojo")
    ???
  } // fallback to jackson here
}

object JsoniterFallbackExample extends Imp0 {

  // Generate codec for Person (automatically handles Address too)
//  implicit lazy val personCodec: JsonValueCodec[User] = JsonCodecMaker.make // Both custom implicits will fail
}

class JsoniterFallbackExample extends AnyFunSpec {

  import JsoniterFallbackExample.*

  it("1") {
    val user: User = User(
      "a",
      1
    )

    // Encoding: Object to JSON string
    val jsonString = writeToString(user)
    println(s"Encoded JSON: $jsonString")

    // Decoding: JSON string to Object
    val decodedPerson = readFromString[User](jsonString)
    println(s"Decoded person: $decodedPerson")
  }
}
