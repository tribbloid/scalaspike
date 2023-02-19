package com.tribbloids.spike.scala_spike

object MatchExtractTypeArg {

  import scala.reflect.runtime.universe.TypeTag

  def mapType(k: TypeTag[_], v: TypeTag[_]) = {

    (k, v) match {
      case (k: TypeTag[a], v: TypeTag[b]) =>
        implicit val kkk = k
        implicit val vvv = v
        implicitly[TypeTag[Map[a, b]]]
    }
  }

  def main(args: Array[String]): Unit = {

    val t1 = implicitly[TypeTag[Int]]
    val t2 = implicitly[TypeTag[String]]

    val r = mapType(t1, t2)
    println(r)
  }
}
