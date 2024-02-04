package com.tribbloids.spike.spark_spike

import org.apache.spark.SparkContext
import org.scalatest.funspec.AnyFunSpec

class SerializeUnlift extends AnyFunSpec {

  import ai.acyclic.prover.commons.spark.TestHelper._
  import SerializeUnlift._

  val sc: SparkContext = TestSC

  it("can use Unlifted function in spark closure") {

    val src = VAsUnlift("abc")

    val rdd = sc.parallelize(Seq(src))

    val r = rdd.map { vv =>
      vv.value
    }

    r.collect().foreach(println)
  }
}

object SerializeUnlift {

  case class VAsUnlift(v: String) {

    val unlift: PartialFunction[Any, String] = Function.unlift { _: Any =>
      Some(v)
    }

    val value: String = unlift.apply(())
  }

}
