package com.tribbloids.spike.frameless_spike

import com.tribbloids.spike.spark_spike.TestHelper
import org.scalatest.funspec.AnyFunSpec
import frameless.TypedDataset

class TypedDatasetDemo extends AnyFunSpec {
  import TypedDatasetDemo._

  implicit val spark = TestHelper.TestSparkSession

  spark.sparkContext.setLogLevel("WARN")

  it("from tuples") {

    val apartments = Seq(
      Apartment("Paris", 50, 300000.0, 2),
      Apartment("Paris", 100, 450000.0, 3),
      Apartment("Paris", 25, 250000.0, 1),
      Apartment("Lyon", 83, 200000.0, 2),
      Apartment("Lyon", 45, 133000.0, 1),
      Apartment("Nice", 74, 325000.0, 3)
    )

    val t1 = TypedDataset.create(apartments)

    val t2 = {

      t1.select(
        t1(Symbol("city")),
        t1(Symbol("surface"))
      )
    }

//    val t3 = t1
//      .withColumn(lit(List("a", "b", "c")))

//      .withColumn(t1(Symbol("city")))
//      .withColumn(t1('surface) + 1)

//    val k: String = t3
  }

}

object TypedDatasetDemo {

  case class Apartment(city: String, surface: Int, price: Double, bedrooms: Int)

}
