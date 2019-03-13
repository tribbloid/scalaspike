package scala.spike.FBound_SelfType

import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.reflect._

/**
  * Created by peng on 05/03/16.
  */
class FBoundPolymorphism extends FunSuite with BeforeAndAfterAll {

  abstract class FBound[+T <: FBound[T]: ClassTag] {

    def respond: List[T]
  }

  class FBoundImpl(val s: String) extends FBound[FBoundImpl] {

    override def respond: List[FBoundImpl] = List(new FBoundImpl(this.s + "s"))
  }

  test("1") {

    assert(new FBoundImpl("s").respond.head.s == "ss")
  }
}
