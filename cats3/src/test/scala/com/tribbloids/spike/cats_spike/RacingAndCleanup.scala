package com.tribbloids.spike.cats_spike

import cats.effect.IO
import cats.effect.unsafe.implicits.global

import java.util.concurrent.TimeUnit
import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Random

object RacingAndCleanup {

//  object Example extends IO[String]

  val buffer = mutable.Buffer.empty[String]

  def repeat[T](io: IO[T], n: Int = 10): IO[T] = {

    var result = io
    for (i <- 1 to n) {
      result = result >> io
    }
    result
  }

  val e1: IO[Int] = repeat(
    IO.delay {
      buffer += "1"
      println("1")
      1
    } <* IO.sleep(Duration(Random.nextInt(1000), "millis"))
  )

  val e2: IO[Int] = repeat(
    IO {
      buffer += "2"
      println("2")
      2
    } <* IO.sleep(Duration(Random.nextInt(1000), "millis"))
  )
  val e4: IO[Either[Int, Int]] = IO.race(e1, e2)

  def main(args: Array[String]): Unit = {

    val ff = e4.unsafeToFuture()

    val r = Await.result(ff, Duration.create(20, TimeUnit.SECONDS))

    println(s"result is $r")
  }
}
