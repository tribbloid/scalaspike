package com.tribbloids.spike.shapeless_spike

import ai.acyclic.prover.commons.debug.print_@
import ai.acyclic.prover.commons.testlib.BaseSpec
import ai.acyclic.prover.commons.viz.WideTyped
import shapeless.labelled.KeyTag
import shapeless.ops.record.Values

class RecordSuite extends BaseSpec {

  import shapeless._
  import record._
  import syntax.singleton._

  describe("name ->> singleton") {

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

      def inferKeys[T <: HList](v: T)(
          implicit
          keys: shapeless.ops.record.Keys[T]
      ) = keys

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

        type RR = record.type

        val record2: RR = record

        //        inferKeys(record2)
        //        inferKeys[record.type](record2)
      }
    }
  }

  it("name ->> type") {

    type Book = Record.`'author -> String, 'title -> String, 'id -> Int, 'price -> Double`.T

    val book: Book = Record( // TODO: why does it break?
      author = "Benjamin Pierce",
      title = "Types and Programming Languages",
      id = 262162091,
      price = 44.11: Double
    )

    import shapeless.syntax.RecordOps
    import shapeless.record._

    val book1 = new RecordOps(book).updated("author", "Benjamin Pierce")
  }

  it("special tag") {

    object Special extends Serializable

    val book =
      (Special ->> "Benjamin Pierce") ::
        ("id" ->> 262162091) ::
        ("price" ->> 44.11) ::
        HNil

    book.get(Special).shouldBe("Benjamin Pierce")

//    book.get("Special").shouldBe(None) cannot compile
  }
}
