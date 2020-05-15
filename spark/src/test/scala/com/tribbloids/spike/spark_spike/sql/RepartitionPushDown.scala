package com.tribbloids.spike.spark_spike.sql

import com.tribbloids.spike.spark_spike.TestHelper
import org.scalatest.FunSpec

class RepartitionPushDown extends FunSpec {

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
