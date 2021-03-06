package com.tribbloids.spike.spark_spike.sql

import com.tribbloids.spike.spark_spike.TestHelper
import org.scalatest.funspec.AnyFunSpec

class DtaasetForEach extends AnyFunSpec {

  import TestHelper.TestSQL.implicits._

  it("foreach") {

    val df = TestHelper.TestSQL.createDataset((1 to 1000).map { v =>
      v -> v * v
    })

    df.foreach(_ => {})
  }
}
