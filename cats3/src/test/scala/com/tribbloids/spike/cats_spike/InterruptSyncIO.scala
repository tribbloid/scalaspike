package com.tribbloids.spike.cats_spike

import cats.effect.IO
import cats.effect.kernel.Outcome
import cats.effect.std.Dispatcher
import cats.effect.unsafe.IORuntime
import org.scalatest.funspec.AnyFunSpec

import scala.concurrent.Future

class InterruptSyncIO extends AnyFunSpec {

//  import Fixture._
//  import scala.concurrent.duration._
//
//  implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)

  it("cancelling") {

    import scala.concurrent.duration._
    implicit val ioRuntime: IORuntime = cats.effect.unsafe.IORuntime.builder().build()

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

    val withCallback = twenty.guaranteeCase {
      case Outcome.Succeeded(_) =>
        IO {
          println("SUCCESS!")
        }
      case Outcome.Errored(ee) =>
        IO {
          println(s"FAILUE: ${ee.toString}")
        }
      case _ => IO.pure(()) // do nothing
    }

    def timeoutIn(milliS: Int): Unit = {
//      val cancelling = withCallback.unsafeRunCancelable()

      val cancelling = {

        val start: IO[() => Future[Unit]] = Dispatcher.sequential[IO](false).use { dispatcher =>
          def result = dispatcher.unsafeRunCancelable(
            withCallback
          )
          IO.delay(result)
        }

        start.unsafeRunSync()
      }

      Thread.sleep(milliS)

      cancelling()
      println("finished")
    }

    timeoutIn(30000)
  }
}
