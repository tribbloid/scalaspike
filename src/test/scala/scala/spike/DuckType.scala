package scala.spike

import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.language.reflectiveCalls

/**
  * Created by peng on 31/03/16.
  */
class DuckType extends FunSuite with BeforeAndAfterAll {

  import DuckType._

  test("1") {

    val a1 = new Abs {
      override def base: Double = 2.0
      override type Out = Int
    }

    val a2 = new Abs {
      override def base: Double = 3.0
      override type Out = Long
    }

    assert(fun(a1).isInstanceOf[Int])
    assert(fun(a1) == 2)
    assert(fun2(a1) == 2)

    //this will trigger a compilation error
//    assert(fun(a2) == 3L)
//    assert(fun2(a2) == 3L)
  }
}

object DuckType {

  trait Abs {

    type Out

    def base: Double
    val out: Out = base.asInstanceOf[Out]
  }

  def fun(abs: Abs { type Out = Int }): Int =
    abs.base.toInt

  def fun2(abs: Abs { def out: Int }): abs.Out =
    abs.out

  // this will trigger an error
//  def fun3(abs: Abs { def out: Int }): Int =
//    abs.out

//  def fun4(abs: Abs { def out: Int }): abs.Out =
//    abs.out
}
