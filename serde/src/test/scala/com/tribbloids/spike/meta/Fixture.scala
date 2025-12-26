package com.tribbloids.spike.meta

import java.nio.file.Path

object Fixture {

  trait ULike

  case class User(
      name: String,
      age: Int,
      path: Path = Path.of("http://google.com")
  ) extends ULike

  case class Msg(
      ulike: ULike,
      content: String
  )
}
