package com.tribbloids.spike.spark_spike

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.scalatest.FunSpec

class StreamingRepartition extends FunSpec {

  import TestHelper.TestSQL.implicits._

  def sparkContext: SparkContext = TestHelper.TestSC
  def sqlContext: SQLContext = TestHelper.TestSQL

  describe(
    "repartition doesn't have to wait for iterations to finish in previous stage") {

    it("dataset version") {

      val ds = sqlContext.createDataset(1 to 100).repartition(1)

      val mapped = ds.mapPartitions { itr =>
        itr.map { ii =>
          Thread.sleep(100)
          println(ii)
          ii
        }
      }

      val mapped2 = mapped
        .repartition(10)
        .map { ii =>
          println(f"repartitioned - ${ii}")
          ii
        }

      mapped2.foreach { _ =>
        }
    }

    it("stream version") {

      val ds: DataFrame = sqlContext.readStream.load("~/abc")

//      ds.
      //TODO: figure it out
    }

    it("rdd version") {

      val rdd = sparkContext.parallelize(1 to 100, 1)

      val mapped = rdd.mapPartitions { itr =>
        itr.map { ii =>
          Thread.sleep(10)
          println(ii)
          ii
        }
      }

      val mapped2 = mapped
        .repartition(100)
        .map { ii =>
          Thread.sleep(100)
          println(f"repartitioned - ${ii}")
          ii
        }

      mapped2.foreach { _ =>
        }

    }

  }
}
