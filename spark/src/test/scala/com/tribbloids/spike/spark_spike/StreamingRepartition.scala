package com.tribbloids.spike.spark_spike

import ai.acyclic.prover.commons.spark.TestHelper
import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.scalatest.funspec.AnyFunSpec

class StreamingRepartition extends AnyFunSpec {

  def sparkContext: SparkContext = TestHelper.TestSC
  val sql: SQLContext = TestHelper.TestSQL

  import sql.implicits._

  describe("repartition doesn't have to wait for iterations to finish in previous stage") {

    it("dataset version") {

      val ds = sql.createDataset(1 to 100).repartition(1)

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

      mapped2.foreach { _ => }
    }

    it("stream version") {

      sql.readStream.load("~/abc")

//      ds.
      // TODO: figure it out
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

      mapped2.foreach { _ => }

    }

  }
}
