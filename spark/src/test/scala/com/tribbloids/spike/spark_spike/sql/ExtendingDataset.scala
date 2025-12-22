package com.tribbloids.spike.spark_spike.sql

import ai.acyclic.prover.commons.spark.TestHelper
import com.tribbloids.spike.spark_spike.sql.DatasetCaching.DSWithAccumulator
import org.apache.spark.sql.{Dataset, SparkSession, classic}
import org.apache.spark.storage.StorageLevel
import org.scalatest.funspec.AnyFunSpec

class ExtendingDataset extends AnyFunSpec {
  import ExtendingDataset.*

  val spark: SparkSession = TestHelper.TestSparkSession

  it("can also cache temp view") {

    val fixture = DSWithAccumulator(spark)
    import fixture.*

    val dsExt = Ext(ds)

    dsExt.createOrReplaceTempView("tt")

    dsExt.persist()

    val ds2 = TestHelper.TestSQL.table("tt")

    println(ds2.queryExecution)

    assert(ds2.storageLevel != StorageLevel.NONE) //
  }
}

object ExtendingDataset {

  case class Ext[T](
      self: Dataset[T]
  ) extends classic.Dataset[T](
        self.sparkSession.sqlContext,
        self.queryExecution.logical,
        self.encoder
      ) {}

}
