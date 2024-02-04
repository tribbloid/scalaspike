package com.tribbloids.spike.spark_spike

import ai.acyclic.prover.commons.spark.TestHelper
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.scalatest.funspec.AnyFunSpec

import scala.util.Random

class RepeatedBroadcast extends AnyFunSpec {

  val sc: SparkContext = TestHelper.TestSC

  it("100 times") {

    val blob: Seq[String] = Seq.fill(Int.MaxValue / 64)("A")

    val bcs: Seq[Broadcast[Seq[String]]] = for (i <- 0 until 100) yield {
      sc.broadcast(blob)
    }

    sc
      .parallelize(1 to 16)
      .map { i =>
        i + bcs(Random.nextInt(100)).value.hashCode()
      }
      .collect()

    println("broadcasted and used")

    bcs.last.unpersist(true)

//    val usable = bcs.filterNot { v =>
//      _SQLHelper.br(v)
//      v.isValid
//    }
  }
}
