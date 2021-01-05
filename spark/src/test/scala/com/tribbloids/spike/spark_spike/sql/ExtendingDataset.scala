package com.tribbloids.spike.spark_spike.sql

import com.tribbloids.spike.spark_spike.TestHelper
import com.tribbloids.spike.spark_spike.sql.DatasetCaching.DSWithAccumulator
import com.tribbloids.spike.spark_spike.sql.ExtendingDataset.Ext
import org.apache.spark.sql.{Dataset, DatasetHelper, SparkSession}
import org.apache.spark.storage.StorageLevel
import org.scalatest.funspec.AnyFunSpec

class ExtendingDataset extends AnyFunSpec {

  val spark: SparkSession = TestHelper.TestSparkSession

  it("can also cache temp view") {

    val fixture = DSWithAccumulator(spark)
    import fixture._

    val dsExt = Ext(ds)

    dsExt.createOrReplaceTempView("tt")

    dsExt.persist()

    val ds2 = TestHelper.TestSQL.table("tt")

    println(ds2.queryExecution)

    assert(ds2.storageLevel != StorageLevel.NONE)
  }
}

object ExtendingDataset {

  case class Ext[T](
      self: Dataset[T]
  ) extends Dataset[T](
        self.sparkSession,
        self.queryExecution.logical,
        DatasetHelper.getEncoder(self)
      ) {}

}
