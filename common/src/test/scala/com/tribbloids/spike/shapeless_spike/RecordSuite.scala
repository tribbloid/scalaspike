package com.tribbloids.spike.shapeless_spike

import com.tribbloids.graph.commons.testlib.BaseSpec
import com.tribbloids.graph.commons.util.WideTyped
import com.tribbloids.graph.commons.util.debug.print_@
import shapeless.ops.record.Values

class RecordSuite extends BaseSpec {

  import shapeless._
  import syntax.singleton._
  import record._

  val book =
    ("author" ->> "Benjamin Pierce") ::
      ("title" ->> "Types and Programming Languages") ::
      ("id" ->> 262162091) ::
      ("price" ->> 44.11) ::
      HNil

  val bookW = WideTyped(book)

  it("values") {

    val v1 = book.values
    WideTyped(v1).viz.toString.shouldBe()

    assert(v1.head == "Benjamin Pierce")

    val _v = implicitly[Values[bookW.Wide]]
    val _vv = _v: Values.Aux[bookW.Wide, _v.Out]
    val v2 = book.values(_vv)

  }

  it("lookup") {

    print_@(WideTyped(book).viz)
  }

  describe("error cases") {

    val record = {
      ("a" ->> 1) ::
        ("b" ->> 2) ::
        HNil
    }

    def inferKeys[T <: HList](v: T)(implicit keys: shapeless.ops.record.Keys[T]) = keys

    it("1") {

      {
        val keys = record.keys // works
        print(keys)

        inferKeys(record) // works
      }

      {
        val record2: record.type = record
        val keys = record2.keys // works
        print(keys)

        inferKeys(record2) // works ?
//        inferKeys[record.type](record2) // compilation error!
      }
    }

    it("2") {

      {
        type RR = record.type

        val record2: RR = record

//        inferKeys(record2)
//        inferKeys[record.type](record2)
      }
    }
  }

}
