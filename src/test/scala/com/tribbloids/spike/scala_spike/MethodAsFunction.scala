package com.tribbloids.spike.scala_spike

import org.scalatest.FunSpec

object MethodAsFunction {

  import scala.reflect.runtime.universe._

  def m1[T](v: T): String = v.toString

  case class C1(v: String)

  val f1: String => C1 = C1

  case class C2[T](v: T)

  val f2: C2.type = C2

  def typesOf[T: TypeTag](v: T): List[Type] =
    typeOf[T].baseClasses.map(typeOf[T].baseType)
}

class MethodAsFunction extends FunSpec {

  import MethodAsFunction._

  it("case class companion obj") {

    println(typesOf(C1))
  }

  it("generic case class companion obj") {

    println(typesOf(C2))
  }
}
