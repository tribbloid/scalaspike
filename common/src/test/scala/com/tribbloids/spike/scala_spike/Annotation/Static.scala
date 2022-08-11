package com.tribbloids.spike.scala_spike.Annotation

import org.scalatest.funspec.AnyFunSpec

import scala.annotation.StaticAnnotation

class Static extends AnyFunSpec {

  it("cannot read by java reflection") {

    val clazz = classOf[Static.Common]

    val annotations = clazz.getAnnotations

    // empty, this can only get Java annotations, scala ones serves a different purpose
    annotations.foreach(println)
  }
}

object Static {

  case class SA1(a: Int = 1) extends StaticAnnotation

  case class SA2(a: Int = 2) extends StaticAnnotation

  @SA1()
  class Common(
      @SA2() a: Int = 1
  ) extends Serializable

  @SA1()
  case class Prod(
      @SA2() a: Int = 1
  ) extends Serializable

}
