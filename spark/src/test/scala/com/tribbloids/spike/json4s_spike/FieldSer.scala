package com.tribbloids.spike.json4s_spike

import com.fasterxml.jackson.annotation.JsonAlias
import com.tribbloids.spike.json4s_spike.FieldSer.{FieldOnly, Mixed}
import org.json4s.jackson.JsonMethods
import org.json4s.{DefaultFormats, Extraction, FieldSerializer, Formats, JField, JObject}
import org.scalatest.funspec.AnyFunSpec

object FieldSer {

  trait Sup

  case class Mixed(
      a: String,
      b: Int
  ) extends Sup {

    var c: Long = _
    var d: Seq[String] = _

    val e = a + "_"
  }

  class FieldOnly extends Sup {

    val a: String = ""

    @JsonAlias(Array("bb", "bbb")) val b: Option[Int] = None
  }

  val formats: Formats = DefaultFormats + FieldSerializer[Sup]()
}

class FieldSer extends AnyFunSpec {

  describe("round trip") {

    it("Mixed") {

      val mm = {
        val result = Mixed("aa", 1)
        result.c = 2L
        result.d = Seq("d1", "d2")
        result
      }

      implicit def formats = FieldSer.formats

      val jv = Extraction.decompose(mm)

      JsonMethods.pretty(jv)

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

    it("FieldOnly") {

      implicit def formats = FieldSer.formats

      import org.json4s.JsonDSL._

      {
        val jv = JObject(
          JField("a", "ab"),
          JField("b", 2)
        )

        JsonMethods.pretty(jv)

        val mm2 = Extraction.extract[FieldOnly](jv)
        assert(mm2.a -> mm2.b == "ab" -> Some(2))
      }

      {
        val jv = JObject(
          JField("a", "bc"),
          JField("bb", 3)
        )

        JsonMethods.pretty(jv)

        val mm2 = Extraction.extract[FieldOnly](jv)
        assert(mm2.a -> mm2.b == "bc" -> Some(3))
      }

    }
  }
}
