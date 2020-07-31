package com.tribbloids.spike.scala_spike.Implicit

import org.scalatest.FunSpec

import scala.reflect.ClassTag
import com.tribbloids.spike.scala_spike.Implicit.InferClassTag._

object InferClassTag {

  def infer[T: ClassTag](vs: Option[T]*): ClassTag[T] = {
    val ctg = implicitly[ClassTag[T]]
    ctg
  }

  trait Super

  trait Sub1 extends Super

  trait Sub2 extends Super

  class Sub11 extends Sub1

  class Sub12 extends Sub1

  class Sub22 extends Sub2

}

class InferClassTag extends FunSpec {

  it("1") {
    val ctg1 = infer(Some(new Sub11), Some(new Sub12), None)
    assert(ctg1.toString == s"${classOf[InferClassTag].getCanonicalName}$$Sub1")

    val ctg2 = infer(Some(new Sub11), Some(new Sub22), None)
    assert(ctg2.toString == s"${classOf[InferClassTag].getCanonicalName}$$Super")
  }
}
