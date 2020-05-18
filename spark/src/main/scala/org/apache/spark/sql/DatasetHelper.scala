package org.apache.spark.sql

import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.QueryExecution
import org.apache.spark.storage.StorageLevel

/**
  * Created by peng on 02/02/17.
  */
object DatasetHelper {

  def fromQueryExecution[T: Encoder](sparkSession: SparkSession, exe: QueryExecution): Dataset[T] = {

    new Dataset[T](sparkSession, exe, implicitly[Encoder[T]])
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
    val result = try {
      v.checkpoint(eager)
    } finally {
      v.unpersist()
    }
    result
  }
}
