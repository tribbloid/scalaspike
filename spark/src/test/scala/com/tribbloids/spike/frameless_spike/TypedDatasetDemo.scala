package com.tribbloids.spike.frameless_spike

import com.tribbloids.spike.spark_spike.TestHelper
import frameless.TypedDataset
import org.scalatest.funspec.AnyFunSpec
import shapeless.record.Record

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

    {
      val c1 = t1(Symbol("city"))

      val t2 = t1.select(
        t1(Symbol("city")),
        t1(Symbol("surface"))
      )

      println(t2.schema.treeString)
      t2.toDF().show()
    }

    {

//      val x = t1('surface).opt.map(_ * 2)

//      val t2 = t1.select(
//        t1(Symbol("surface")).opt
//          .map(v => v * 2)
//      ) // TODO: doesn't work?

//      val t2 = t1.select(
//        t1(Symbol("surface"))
//          .opt
//          .map(v => v * 2)
//      ) // TODO: blocked by a bug

//      t2.printSchema()
//      t2.toDF().show()
    }

//    val t3 = t1
//      .withColumn(lit(List("a", "b", "c")))

//      .withColumn(t1(Symbol("city")))
//      .withColumn(t1('surface) + 1)

//    val k: String = t3
  }

  it("from record") { // TODO: doesn't work, not a shapeless Record

//    import shapeless._
//    import syntax.singleton._
//
////    val record = {
////      ("a" ->> 1) ::
////        ("b" ->> 2) ::
////        HNil
////    }
//
//    val record = Record(
//      a = 1: Int,
//      b = 2: Int
//    )
//
//    val records = Seq(record)
//
//    val t1 = TypedDataset.create(records)
//
//    val t2 = t1.select(
//      t1(Symbol("a"))
//    )
  }
}

object TypedDatasetDemo {

  case class Apartment(city: String, surface: Int, price: Double, bedrooms: Int)

}
