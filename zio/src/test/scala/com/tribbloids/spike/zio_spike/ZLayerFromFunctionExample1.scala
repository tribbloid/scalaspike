package com.tribbloids.spike.zio_spike

import ai.acyclic.prover.commons.graph.Arrow
import ai.acyclic.prover.commons.graph.local.Local
import ai.acyclic.prover.commons.graph.viz.Hierarchy
import ai.acyclic.prover.commons.testlib.BaseSpec
import zio.*

object ZLayerFromFunctionExample1 {

  val l1: ZLayer[Int, Nothing, String] =
    ZLayer.fromFunction { (v: Int) =>
      v.toString
    }

  val l2: ZLayer[String, Nothing, Int] =
    ZLayer.fromFunction { (v: String) =>
      v.length
    }

}

class ZLayerFromFunctionExample1 extends BaseSpec {

  it("l1 >>> l2") {

    val l1ThenL2: ZLayer[Int, Nothing, Int] =
      ZLayerFromFunctionExample1.l1 >>> ZLayerFromFunctionExample1.l2

    val provided: ZLayer[Any, Nothing, Int] =
      ZLayer.succeed(123) >>> l1ThenL2

    case class TraceNode(
        override val value: String,
        children: Seq[(Arrow.Outbound, TraceNode)] = Nil
    ) extends Local.Diverging.Tree.Node_[String] {

      override lazy val inductions: Seq[(Arrow.Outbound, TraceNode)] = children
    }

    val compositionTrace: Local.Diverging.Poset[String] =
      Local.Diverging.Tree.makeExact(
        TraceNode(
          "provided: ZLayer[Any, Nothing, Int]",
          Seq(
            Arrow.Outbound.OfText(Some("base")) -> TraceNode(
              "ZLayer.succeed(123): ZLayer[Any, Nothing, Int]"
            ),
            Arrow.Outbound.OfText(Some(">>>")) -> TraceNode(
              "l1ThenL2: ZLayer[Int, Nothing, Int]",
              Seq(
                Arrow.Outbound.OfText(Some("base")) -> TraceNode(
                  "l1: ZLayer[Int, Nothing, String]"
                ),
                Arrow.Outbound.OfText(Some(">>>")) -> TraceNode(
                  "l2: ZLayer[String, Nothing, Int]"
                )
              )
            )
          )
        )
      )

    val _ = provided
    println(Hierarchy.Indent2.show(compositionTrace).text)

  }
}
