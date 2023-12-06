package com.tribbloids.spike.frameless_spike

//import com.tribbloids.spike.frameless_spike.TypedDatasetDemo.Apartment
import com.tribbloids.spike.spark_spike.TestHelper
import frameless.TypedDataset
import org.scalatest.funspec.AnyFunSpec
import shapeless.HNil

class TypedDatasetDemo extends AnyFunSpec {

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
//      val t2 = t1.select(
//        t1(Symbol("surface"))
//          .opt[Int]
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

    import frameless.TypedDataset
    import shapeless.record.Record
    import shapeless.syntax.singleton._

    // Define a Record type
    type Person = Record.`'name -> String, 'age -> Int`.T

    // Create a Dataset of this Record
    val people: Seq[Person] = Seq(
      ('name ->> "Alice") :: ('age ->> 25) :: HNil,
      ('name ->> "Bob") :: ('age ->> 29) :: HNil
    )

    // Convert to TypedDataset
    val typedDS = TypedDataset.create(people)(RecordEncoder) // TODO: doesn't work

  }
}

object TypedDatasetDemo {

  case class Apartment(city: String, surface: Int, price: Double, bedrooms: Int)

}
