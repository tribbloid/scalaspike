package com.tribbloids.spike.zio_spike

import ai.acyclic.prover.commons.testlib.BaseSpec
import zio._

class ZLayerConcurrencySpec extends BaseSpec {

  trait ServiceA
  trait ServiceB
  trait ServiceC
  trait ServiceD

  case class A() extends ServiceA
  case class B() extends ServiceB
  case class C(a: ServiceA, b: ServiceB) extends ServiceC
  case class D(c: ServiceC) extends ServiceD

  // Simulated slow layers
  val layerA = ZLayer.fromZIO {
    ZIO.sleep(1.second).as(A()).tap(_ => ZIO.debug("Initialized A"))
  }

  val layerB = ZLayer.fromZIO {
    ZIO.sleep(1.second).as(B()).tap(_ => ZIO.debug("Initialized B"))
  }

  val layerC = ZLayer.derive[C]

  val layerD = ZLayer.derive[D]

  it("layers A and B should initialize concurrently") {

    // ZLayer.Debug.tree will print the dependency graph.
    // ZLayer.make macro automatically picks up variable names (layerA, layerB, etc.) for the debug graph.
    val layer = ZLayer.make[ServiceD](
      layerA,
      layerB,
      layerC,
      layerD,
      ZLayer.Debug.mermaid
    )

    // Verify concurrent execution
    val program = for {
      start <- Clock.currentTime(java.util.concurrent.TimeUnit.MILLISECONDS)
      _ <- ZIO.service[ServiceD].provide(layer)
      end <- Clock.currentTime(java.util.concurrent.TimeUnit.MILLISECONDS)
      duration = end - start
    } yield {
      println(s"Total initialization time: ${duration}ms")
      assert(duration < 1900)
      assert(duration >= 1000)
    }

    Unsafe.unsafe { implicit unsafe =>
      Runtime.default.unsafe.run(program).getOrThrowFiberFailure()
    }
  }

}
