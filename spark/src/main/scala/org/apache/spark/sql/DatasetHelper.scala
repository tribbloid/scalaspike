package org.apache.spark.sql

import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.QueryExecution
import org.apache.spark.storage.StorageLevel

/**
  * Created by peng on 02/02/17.
  */
object DatasetHelper {

  def fromQueryExecution[T: Encoder](exe: QueryExecution): Dataset[T] = {

    new Dataset[T](exe, implicitly[Encoder[T]])
  }

  def ofRows(
      sparkSession: SparkSession,
      logicalPlan: LogicalPlan
  ): DataFrame = {

    Dataset.ofRows(sparkSession, logicalPlan)
  }

  def getEncoder[T](ds: Dataset[T]): ExpressionEncoder[T] = ds.exprEnc

  def showString[T](ds: Dataset[T], numRows: Int, truncate: Int = 20): String =
    ds.showString(numRows, truncate)

  def checkpointImpl[T](v: Dataset[T], eager: Boolean = true): Dataset[T] = {
    v.persist(StorageLevel.DISK_ONLY)
    val result =
      try {
        v.checkpoint(eager)
      } finally {
        v.unpersist()
      }
    result
  }

  case class Cell(v: Any) {

    override lazy val toString: String = "" + v

    lazy val displayLength: Int = {

      val escapeRemoved = v.toString.replaceAll("\u001b\\[[;\\d]*m", "")
      escapeRemoved.length
    }
  }

  def formatTable(table: Seq[Seq[Any]]): String = {
    if (table.isEmpty) ""
    else {
      val _table = table.map { row =>
        row.map { v =>
          Cell(v)
        }
      }

      val colWidths = _table.transpose.map(_.map { cell =>
        cell.displayLength
      }.max + 2)

      val rows = _table.map(
        _.zip(colWidths)
          .map {
            case (cell, size) =>
              s" ${cell.toString} " + Array.fill(size - cell.displayLength - 2)(' ').mkString

          }
          .mkString("|", "|", "|"))

//      val separator = colWidths.map("-" * _).mkString("+", "+", "+")
      // Put the table together and return
//      (separator +: rows.head +: separator +: rows.tail :+ separator).mkString("\n")

      (rows.head +: rows.tail).mkString("\n")
    }
  }
}
