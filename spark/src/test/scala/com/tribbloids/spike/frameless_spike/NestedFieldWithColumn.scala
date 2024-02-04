package com.tribbloids.spike.frameless_spike

import ai.acyclic.prover.commons.debug.print_@
import ai.acyclic.prover.commons.spark.TestHelper
import frameless.TypedDataset
import org.apache.spark.sql.SparkSession
import org.scalatest.funspec.AnyFunSpec

class NestedFieldWithColumn extends AnyFunSpec {

  import NestedFieldWithColumn._

  implicit val spark: SparkSession = TestHelper.TestSparkSession

  describe("encoder for") {

    it("nested column") {

      import org.apache.spark.sql.functions._

      val rows = Seq(
        Outer1(
          Inner(1, "a", "b"),
          "c",
          "d"
        ),
        Outer1(
          Inner(1, "a", "b"),
          "c",
          "d"
        )
      )

//      val ds = spark.createDataset(rows) // fails
      val ds2 = TypedDataset.create(rows)
      print_@(ds2.dataset.schema.treeString)

      val ds3 = ds2.dataset.groupBy("aux1").agg(collect_list("in") as "ins")
      print_@(ds3.schema.treeString)
      ds3.show(false)

      val ds4 = ds3.select(
        col("aux1"),
        transform(
          col("ins"),
          { (ss, _) =>
            struct(ss("v"), ss("b"), col("aux1"))
//            ???
            //          (v: Seq[Record]) => v.map(_.get[Int]("v"))
          }
        ) as "ins"
      )

      print_@(ds4.schema.treeString)
      ds4.show(false)

//      ds2.select(ds2(Symbol("in.a"))).show()
    }

    describe("nested column with ArrayType") {

      val rows = Seq(
        OuterN(
          Seq(
            Inner(1, "a", "b"),
            Inner(1, "a", "b")
          ),
          "c",
          "d"
        ),
        OuterN(
          Seq(
            Inner(1, "a", "b"),
            Inner(1, "a", "b")
          ),
          "c",
          "d"
        )
      )

      val ds2 = TypedDataset.create(rows) // TODO: don't upgrade Spark until bug fix

      print_@(ds2.dataset.schema.treeString)
      ds2.dataset.show(false)

      it("spark") {

        import org.apache.spark.sql.functions._

        val ds4 = ds2.dataset.select(
          col("aux1"),
          transform(
            col("ins"),
            { (ss, _) =>
              struct(ss("v"), ss("b"), col("aux1"))
              //            ???
              //          (v: Seq[Record]) => v.map(_.get[Int]("v"))
            }
          ) as "ins"
        )

        print_@(ds4.schema.treeString)
        ds4.show(false)
      }

      it("frameless") {

        ds2.select(
          ds2(Symbol("aux1"))
//          transform(
//            col("ins"),
//            { (ss, ii) =>
//              struct(ss("v"), ss("b"), col("aux1"))
//              //            ???
//              //          (v: Seq[Record]) => v.map(_.get[Int]("v"))
//            }
//          ).typedColumn // TODO: don't know to do this

//          ds2.explode()
        )
      }
    }
  }

}

object NestedFieldWithColumn {

  case class Inner(
      v: Int,
      a: String,
      b: String
  )

  case class Outer1(
      in: Inner,
      aux1: String,
      aux2: String
  )

  case class OuterN(
      ins: Seq[Inner],
      aux1: String,
      aux2: String
  )

}
