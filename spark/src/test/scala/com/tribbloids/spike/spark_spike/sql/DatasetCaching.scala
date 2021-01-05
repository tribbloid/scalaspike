package com.tribbloids.spike.spark_spike.sql

import com.tribbloids.spike.spark_spike.TestHelper
import com.tribbloids.spike.spark_spike.sql.DatasetCaching.DSWithAccumulator
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel
import org.apache.spark.util.LongAccumulator
import org.scalatest.funspec.AnyFunSpec

class DatasetCaching extends AnyFunSpec {

  val spark: SparkSession = TestHelper.TestSparkSession

  it("can eliminate double execution of derivative internalRDD") {

    val fixture = DSWithAccumulator(spark)
    import fixture._

    ds.persist()

    assert(ds.collect().toSet == (1 to 100).map(v => v * v).toSet)

    acc.reset()

    val rdd1 = ds.queryExecution.toRdd

    val rdd2 = rdd1.map { v =>
      v
    }

    rdd2.collect()
    rdd2.collect()

    assert(acc.value == 0)

//    Thread.sleep(10000000)
  }

  it("can also cache temp view") {

    val fixture = DSWithAccumulator(spark)
    import fixture._

    ds.createOrReplaceTempView("tt")

    ds.persist()

    val ds2 = TestHelper.TestSQL.table("tt")

    println(ds2.queryExecution)

    assert(ds2.storageLevel != StorageLevel.NONE)
  }
}

object DatasetCaching {

  case class DSWithAccumulator(spark: SparkSession) {

    import spark.implicits._

    val acc = new LongAccumulator
    spark.sparkContext.register(acc)

    val ds = spark.createDataset(1 to 100).map { v =>
      acc.add(1)
      v * v
    }
  }
}
