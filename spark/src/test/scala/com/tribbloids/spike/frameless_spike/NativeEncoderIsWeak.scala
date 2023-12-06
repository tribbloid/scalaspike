package com.tribbloids.spike.frameless_spike

import com.tribbloids.spike.spark_spike.TestHelper
import org.scalatest.funspec.AnyFunSpec

class NativeEncoderIsWeak extends AnyFunSpec {
  import org.apache.spark.sql.Dataset

  import java.util.Calendar

  val spark = TestHelper.TestSparkSession
  import spark.implicits._

  case class DateRange(s: Calendar, e: Calendar)

  it("weak") {

    def now = new java.util.GregorianCalendar()

    val ds: Dataset[DateRange] = Seq(DateRange(now, now)).toDS()
  }
}

object NativeEncoderIsWeak {

//  case class Apartment(city: String, surface: Int, price: Double, bedrooms: Int)

}
