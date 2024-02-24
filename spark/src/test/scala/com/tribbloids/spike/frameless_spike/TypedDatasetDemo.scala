package com.tribbloids.spike.frameless_spike

import ai.acyclic.prover.commons.spark.TestHelper
import frameless.TypedDataset
import org.apache.spark.sql.SparkSession
import org.scalatest.funspec.AnyFunSpec
import shapeless.HNil

class TypedDatasetDemo extends AnyFunSpec {

  import TypedDatasetDemo.Apartment

  implicit val spark: SparkSession = TestHelper.TestSparkSession

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
      t1(Symbol("city"))

      val t2 = t1.select(
        t1(Symbol("city")),
        t1(Symbol("surface"))
      )

      println(t2.schema.treeString)
      t2.toDF().show()
    }

  }

  it("from record") { // TODO: doesn't work, not a shapeless Record

    import shapeless.record.Record
    import shapeless.syntax.singleton._

    // Define a Record type
    type Person = Record.`'name -> String, 'age -> Int`.T

    // Create a Dataset of this Record
    val people = Seq(
      ('name ->> "Alice") :: ('age ->> 25) :: HNil,
      ('name ->> "Bob") :: ('age ->> 29) :: HNil
    )

    // Convert to TypedDataset
//    val typedDS = TypedDataset.create(people) // TODO: doesn't work
  }
}

object TypedDatasetDemo {

  case class Apartment(city: String, surface: Int, price: Double, bedrooms: Int)

}
