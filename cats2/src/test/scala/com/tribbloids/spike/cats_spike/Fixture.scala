package com.tribbloids.spike.cats_spike

import cats.effect.{IO, Timer}

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.{duration, ExecutionContext}

object Fixture {

  import duration._
//
  implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)

  def threadText() = s"${Thread.currentThread().getId}:${Thread.currentThread().getName}"

  def threadInfo() = {

    println(s"Thread: ${threadText()}")
  }

//  println(s"root thread is ${threadText()}")

  val inc = new AtomicInteger(0)

  val base: IO[Unit] = IO {
    threadInfo()
  }.delayBy(1.second)

  val three: IO[Unit] = base *> base *> base

  val twenty: IO[Unit] = {

    val srcs = (1 to 20)
      .map { _ =>
        base
      }

    srcs
      .reduce(_ *> _)
  }

  lazy val lasting: IO[Unit] = {

    base.flatMap(_ => base) // this is wrong
  }

}
