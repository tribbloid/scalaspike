package com.tribbloids.spike.spark_spike

import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.funspec.AnyFunSpec

import scala.collection.concurrent.TrieMap
import scala.util.Random

object TransientCreation {

  def apply(): Unit = {
    import org.apache.log4j.{Level, Logger}

    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    println("test")

    val conf = new SparkConf().setAppName("test").setMaster("local[*]")
    val sqlC: SQLContext = SparkSession.builder().config(conf).getOrCreate().sqlContext
    val sc: SparkContext = sqlC.sparkContext

    val tokens: Seq[OriginalToken] = {
      (1 to 200).flatMap { i =>
        (1 to 100).map { j =>
          OriginalToken(i, j)
        }
      }
    }

    println("dataset size: " + tokens.size)

    val acc: LongAccumulator = sc.longAccumulator("test")

    import sqlC.implicits._

    val rdd = sc.parallelize(tokens).repartition(200)
    rdd.toDF

    val toCloseOver = TrieMap[String, SomeClass]("a" -> SomeClass("aString", acc))
    val broadcasted = sc.broadcast(toCloseOver)

//    {
//      val closureUDF = udf { i: Int =>
//        val res = toCloseOver("a")
//        res.addSuffix(i.toString)
//      }
//
//      val newDf = df.withColumn("newCol", closureUDF(df("original")))
//
//      newDf.foreach(_ => {})
//    }

    {
      val newRDD = rdd.map { v =>
        val tt = broadcasted.value
        val res: SomeClass = tt("a")
        val suffixed = res.addSuffix(v.original.toString)
        import res._

        println(s"${randomID} \t:\t ${endString}")
        suffixed
      }
      newRDD.foreach(_ => {})
    }

    println("\nacc value " + acc.value)
  }

  case class SomeClass(string: String, acc: LongAccumulator) {

    @transient lazy val randomID: Long = Math.abs(Random.nextLong())

    @transient lazy val endString: String = {
      acc.add(1)
      val endString = string + "_" + randomID

      println(s"LAZY!!!! ${randomID} \t:\t ${endString}")
      endString
    }

    def addSuffix(s: String): String = {
      s + "_" + endString
    }
  }

  case class OriginalToken(original: Int, token: Int)
}

class TransientCreation extends AnyFunSpec {
  it("count object creation attempts") {

    TransientCreation.apply()
  }
}
