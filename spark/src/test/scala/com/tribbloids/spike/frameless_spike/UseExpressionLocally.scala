package com.tribbloids.spike.frameless_spike

import com.tribbloids.spike.spark_spike.TestHelper
import frameless.TypedDataset
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.codegen.{GenerateSafeProjection, GenerateUnsafeProjection}
import org.apache.spark.sql.catalyst.expressions.{Attribute, Expression, Projection, UnsafeProjection}
import org.scalatest.funspec.AnyFunSpec

class UseExpressionLocally extends AnyFunSpec {
  import UseExpressionLocally._

  implicit val spark = TestHelper.TestSparkSession

  it("get elementary cell") {

    import spark.implicits._

    val ds = spark.createDataset[Bean](Seq(Bean(1, 2)))

    val col = {
      val raw = ds("a") + 4 as "c"
      raw
    }

//    val expr: Expression = {
//
////      val raw = col.expr
//
//      val selected = ds.select(col)
//
////      selected.collect()
//
//      val exe = selected.queryExecution
//
//      val plan = exe.analyzed
//
//      val result = plan.expressions.head
//
////      val sparkPlan: SparkPlan = exe.sparkPlan
////
////      sparkPlan.expressions
//
//      assert(result.resolved)
//      result
//    }

    val input = ds.queryExecution.logical.output

    val eval = LocalEval(Seq(col.expr), input)

    val r1 = ds.queryExecution.toRdd.map { internal =>
      val r1 = eval.safe(internal)

      val r2 = eval.unsafe(internal)
      r1 -> r2
    }

    val rows = r1.collect()
    rows.foreach(println)
  }

  it("get compound cell") {

    val ds = TypedDataset
      .create(
        Seq(
          And(Bean(1, 2), "a"),
          And(Bean(3, 4), "b")
        )
      )
      .dataset

    val enc = ds.encoder

    val col = ds("b1")

    val input = ds.queryExecution.optimizedPlan.output

    val eval = LocalEval(Seq(col.expr), input)

    val r1 = ds.queryExecution.toRdd.map { internal =>
      val r1 = eval.safe(internal)

      val r2 = eval.unsafe(internal)
      r1 -> r2
    }

    val rows = r1.collect()
    rows.foreach(println)

//    val eval = {}
//
//    val expr: Expression = {
//
//      val selected = ds.select(col)
//
//      val exe = selected.queryExecution
//
//      val plan = exe.analyzed
//
//      val result = plan.expressions.head
//
//      assert(result.resolved)
//      result
//    }

  }
}

object UseExpressionLocally {

  case class Bean(a: Int, b: Int)

  case class And(b1: Bean, s1: String)

  case class LocalEval(
      exprs: Seq[Expression],
      schema: Seq[Attribute]
  ) {

    @transient lazy val safe: Projection = GenerateSafeProjection.generate(exprs, schema)

    @transient lazy val unsafe: UnsafeProjection = GenerateUnsafeProjection.generate(exprs, schema)
  }
}
