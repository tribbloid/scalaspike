package zio_spike

import ai.acyclic.prover.commons.graph.Arrow
import ai.acyclic.prover.commons.graph.local.Local
import ai.acyclic.prover.commons.graph.viz.Hierarchy
import ai.acyclic.prover.commons.testlib.BaseSpec
import zio.*
import zio_spike.ZIOFromFunctionExample1.{f1, f2}

object ZIOFromFunctionExample1 {

  val f1: URIO[Int, String] =
    ZIO.fromFunction { (v: Int) =>
      v.toString
    }

  val f2: URIO[String, Int] =
    ZIO.fromFunction { (v: String) =>
      v.length
    }

}

class ZIOFromFunctionExample1 extends BaseSpec {

  it("f1 then f2") {

    val f1ThenF2: URIO[Int, Int] =
      for {
        s <- f1
        n <- f2.provideEnvironment(ZEnvironment(s))
      } yield n

    case class TraceNode(
        override val value: String,
        children: Seq[(Arrow.Outbound, TraceNode)] = Nil
    ) extends Local.Diverging.Tree.Node_[String] {

      override lazy val inductions: Seq[(Arrow.Outbound, TraceNode)] = children
    }

    val compositionTrace: Local.Diverging.Poset[String] =
      Local.Diverging.Tree.makeExact(
        TraceNode(
          "f1ThenF2: URIO[Int, Int]",
          Seq(
            Arrow.Outbound.OfText(Some("s <-")) -> TraceNode("f1: URIO[Int, String]"),
            Arrow.Outbound.OfText(Some("n <-")) -> TraceNode(
              "f2.provideEnvironment(ZEnvironment(s)): URIO[Int, Int]",
              Seq(
                Arrow.Outbound.OfText(Some("base")) -> TraceNode("f2: URIO[String, Int]"),
                Arrow.Outbound.OfText(Some("env")) -> TraceNode("ZEnvironment(s)")
              )
            ),
            Arrow.Outbound.OfText(Some("yield")) -> TraceNode("n")
          )
        )
      )

    val _ = f1ThenF2
    println(Hierarchy.Indent2.show(compositionTrace).text)

  }
}
