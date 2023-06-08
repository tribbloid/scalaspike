package com.tribbloids.spike.cats_spike

import cats.effect.{IO, Timer}
import org.scalatest.funspec.AnyFunSpec

import scala.concurrent.ExecutionContext

class InterruptSyncIO extends AnyFunSpec {

//  import Fixture._
//  import scala.concurrent.duration._
//
//  implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)

  it("cancelling") {

    import scala.concurrent.duration._

    implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)

    val base: IO[Unit] = IO {
      println("...")
    }.delayBy(1.second)

    val twenty: IO[Unit] = {

      val srcs = (1 to 20)
        .map { _ =>
          base
        }

      srcs.reduce(_ *> _)
    }

    val start = twenty.runCancelable {
      case Left(ee) =>
        Fixture.threadInfo()
        IO {
          Fixture.threadInfo()

          println(s"FAILUE: ${ee.toString}")
        }
      case Right(kk) =>
        Fixture.threadInfo()
        IO {
          Fixture.threadInfo()
          println("SUCCESS!")
        }
    }

    val cancelling = start.unsafeRunSync()

    Thread.sleep(30000)

    cancelling.unsafeRunSync()

    println("finished")
//    Thread.sleep(3000)
//    val cancelling = lasting.runCancelable {
//      case Left(ee)  => IO.delay(println(ee))
//      case Right(tt) => IO.delay(println(tt))
//    }
//
//    Thread.sleep(20000)

  }

}
