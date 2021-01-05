package com.tribbloids.spike.json4s_spike

import com.tribbloids.spike.json4s_spike.FieldSer_inherited.Mixed
import org.json4s.{DefaultFormats, Extraction, FieldSerializer, Formats}
import org.json4s.jackson.JsonMethods
import org.scalatest.funspec.AnyFunSpec

object FieldSer_inherited {

  trait Base {

    var c: Long = _
    var d: Seq[String] = _
  }

  case class Mixed(
      a: String,
      b: Int
  ) extends Base {

    val e = a + "_"
  }

  val formats: Formats = DefaultFormats + FieldSerializer[Mixed]()
}

class FieldSer_inherited extends AnyFunSpec {

  it("obj => json => obj") {

    val mm = {
      val result = Mixed("aa", 1)
      result.c = 2L
      result.d = Seq("d1", "d2")
      result
    }

    implicit def formats = FieldSer.formats

    val jv = Extraction.decompose(mm)

    val json = JsonMethods.pretty(jv)

    val mm2 = Extraction.extract[Mixed](jv)

    assert(mm == mm2)
    assert(
      Seq(mm, mm2)
        .map { v =>
          v.c -> v.d
        }
        .distinct
        .size == 1
    )
  }
}
