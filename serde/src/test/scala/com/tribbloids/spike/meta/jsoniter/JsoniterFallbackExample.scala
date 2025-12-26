package com.tribbloids.spike.meta.jsoniter

import ai.acyclic.prover.commons.debug.print_@
import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.tribbloids.spike.meta.Fixture.User
import org.scalatest.funspec.AnyFunSpec

trait Imp0 {

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
