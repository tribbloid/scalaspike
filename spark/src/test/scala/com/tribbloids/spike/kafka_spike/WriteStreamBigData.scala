package com.tribbloids.spike.kafka_spike

import com.tribbloids.spike.spark_spike.TestHelper
import org.apache.spark.sql.Dataset
import org.scalatest.funspec.AnyFunSpec

import scala.util.Random

class WriteStreamBigData extends AnyFunSpec with EmbeddedKafkaFixture {

  import TestHelper.TestSQL.implicits._

  lazy val bigDS: Dataset[String] = {

    val seed = TestHelper.TestSQL.createDataset(1 to 1000 * 1000)

    val ds = seed.map { _ =>
      Random.nextString(1000)
    }

    val result = ds.checkpoint(true)

    ds
  }

  //  lazy val ds: Dataset[String] = {
  //
  //    val content = {
  //
  //      val src = scala.io.Source.fromURL("http://ichart.finance.yahoo.com/table.csv?s=FB")
  //
  //      try {
  //        src.mkString
  //      } finally {
  //        src.close()
  //      }
  //    }
  //
  //    val list = content.split("\n").filter(_ != "")
  //
  //    val result = TestHelper.TestSQL.createDataset(list)
  //    result
  //  }

  it("can write big dataset") {

    val batchID = 1000

    //    KafkaHelper.sink()
    // TODO: incmplete
  }
}
