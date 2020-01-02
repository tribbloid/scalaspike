package com.tribbloids.spike.scala_spike.FBound_SelfType

import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.reflect._

class FBoundPolymorphism_2 extends FunSuite with BeforeAndAfterAll {

  trait FBound[+T <: FBound[T]] {

    val ctg: ClassTag[_ <: T]

    def respond: List[T]
  }

  class FBoundImpl2(val s: String) extends FBound[FBoundImpl2] {

    override val ctg: ClassTag[_ <: FBoundImpl2] = classTag[FBoundImpl2]

    override def respond: List[FBoundImpl2] = List()
  }

  class FBoundSubImpl2(override val s: String) extends FBoundImpl2(s) {

    override val ctg: ClassTag[_ <: FBoundSubImpl2] = classTag[FBoundSubImpl2]

    override def respond: List[FBoundSubImpl2] =
      List(new FBoundSubImpl2(this.s + "b"))
  }

  test("1") {

    assert(new FBoundImpl2("s").respond.isEmpty)
  }

  test("2") {

    assert(new FBoundSubImpl2("s").respond.head.s == "sb")

    //looks like covariant notation + doesn't work
//    assert(new FBoundSubImpl("s").getClass.isAssignableFrom(new FBoundImpl("s").getClass))
//    assert(!new FBoundImpl("s").getClass.isAssignableFrom(new FBoundSubImpl("s").getClass))
  }
}
