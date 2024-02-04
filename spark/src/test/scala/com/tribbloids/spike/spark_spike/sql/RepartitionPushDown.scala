package com.tribbloids.spike.spark_spike.sql

import ai.acyclic.prover.commons.spark.TestHelper
import org.scalatest.funspec.AnyFunSpec

class RepartitionPushDown extends AnyFunSpec {

  import TestHelper.TestSQL.implicits._

  it("repartitioning twice can be squashed") {

    val df = TestHelper.TestSQL.createDataset((1 to 1000).map { v =>
      v -> v * v
    })

    val repartitioned = df
      .repartition(64)
      .select("_1")
      .repartition(1)

    println(repartitioned.queryExecution)
  }
}
