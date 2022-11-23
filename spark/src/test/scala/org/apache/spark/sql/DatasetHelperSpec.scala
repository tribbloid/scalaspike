package org.apache.spark.sql

import org.scalatest.funspec.AnyFunSpec

import scala.io.AnsiColor

class DatasetHelperSpec extends AnyFunSpec {

  it("can print table") {

    val ESC = "\u001b"
    val GS = "\u001D"
    val InitializePrinter = ESC + "@"
//    val BoldOn = ESC + "E" + "\u0001"
//    val BoldOff = ESC + "E" + "\u0000"

    val data = Seq(Seq("a", "b", AnsiColor.RED + "c" + AnsiColor.RESET), Seq(":--", ":--", ":--"), Seq("1", "2", "3"))

    val str = DatasetHelper.formatTable(data)

//    println(InitializePrinter)
    println(str)
  }

//  it("actual length") {
//
//    import scala.io.AnsiColor
//    val str = AnsiColor.RED + "c" + AnsiColor.RESET
//
//    assert(str.length == 1)
//  }
}
