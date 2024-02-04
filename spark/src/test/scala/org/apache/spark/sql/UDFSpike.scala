package org.apache.spark.sql

import ai.acyclic.prover.commons.spark.TestHelper
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.scalatest.funspec.AnyFunSpec

class UDFSpike extends AnyFunSpec {

  import org.apache.spark.sql.functions.udf

  val spark = TestHelper.TestSparkSession

  it("udf to NULL") {

//    val u1 = udf { x: String =>
//      if (x.length <= 3) 3
//      else null
//    }

    val random: UserDefinedFunction = udf { () =>
      val v = Math.random()
      if (v < 0.5) Some(1)
      else None
    }

    val df = spark.emptyDataFrame
      .select(
        random()
      )

//    spark.udf.register("random", random.asNondeterministic())
//    spark.sql("SELECT random()").show()

    df.show()
    df.printSchema()

    df
  }
}
