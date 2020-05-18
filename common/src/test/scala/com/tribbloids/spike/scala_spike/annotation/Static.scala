package com.tribbloids.spike.scala_spike.annotation

import org.scalatest.FunSpec

import scala.annotation.StaticAnnotation

class Static extends FunSpec {

  it("can read member") {

    val clazz = classOf[Static.Annotated]

    val annotations = clazz.getAnnotations

    annotations.foreach(println)
  }
}

object Static {

  class Example(a: Int = 1) extends StaticAnnotation

  @Example(a = 1)
  class Annotated extends Serializable
}
