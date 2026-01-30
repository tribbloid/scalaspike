package com.tribbloids.spike.zio

import zio._
import zio.test._
import zio.test.Assertion._

object ZioOperatorCheck extends ZIOSpecDefault {

  def spec = suite("ZIO <* operator behavior")(
    test("ZIO <* is actually zipLeft (discard right)") {
      val left = ZIO.succeed(1)
      val right = ZIO.succeed(2)

      // concept: <* means "do left, do right, keep left"
      // it is an alias for left.zipLeft(right)
      val result = left <* right

      assertZIO(result)(equalTo(1))
    },
    test("ZIO <*> is zip (product)") {
      val left = ZIO.succeed(1)
      val right = ZIO.succeed(2)

      // concept: <*> means "do left, do right, keep both"
      // it is an alias for left.zip(right)
      // NOTE: If this fails to compile, it means ZIO 2.x removed <*> alias for zip
      val result = left <*> right

      assertZIO(result)(equalTo((1, 2)))
    }
  )
}
