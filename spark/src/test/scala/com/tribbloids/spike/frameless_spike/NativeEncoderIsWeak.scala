package com.tribbloids.spike.frameless_spike

import ai.acyclic.prover.commons.spark.TestHelper
import org.apache.spark.sql.SparkSession
import org.scalatest.funspec.AnyFunSpec

import java.util.Date

class NativeEncoderIsWeak extends AnyFunSpec {

  import NativeEncoderIsWeak._

  implicit val spark: SparkSession = TestHelper.TestSparkSession
  import spark.implicits._

  it("native") {

    def now = new java.util.GregorianCalendar()

    Seq(DateRange(now, now)).toDS()
  }

  it("frameless") {

    import frameless.TypedDataset
    import frameless.syntax._

//    def now = new java.util.GregorianCalendar()
    def now = new Date()

//    val ds = TypedDataset.create(Seq(now))
    val ds = TypedDataset.create(Seq(DateRange(now, now)))
    ds.show()
  }
}

object NativeEncoderIsWeak {

//  case class Apartment(city: String, surface: Int, price: Double, bedrooms: Int)

  case class DateRange[T](s: T, e: T)
}
