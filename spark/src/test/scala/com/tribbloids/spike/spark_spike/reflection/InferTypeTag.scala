package com.tribbloids.spike.scala_spike.reflection

import scala.reflect.ClassTag

object InferTypeTag {

  import org.apache.spark.sql.catalyst.ScalaReflection.universe._

  def infer(): Unit = {
    type U = (Int, String)

    val ttg1 = implicitly[TypeTag[(Int, String)]]

    // this doesn't compile
//    val ttg2 = implicitly[TypeTag[U]]

    val ctg = implicitly[ClassTag[U]]
  }
}
