import org.scalatest.exceptions.TestFailedException
import org.scalatest.{BeforeAndAfterAll, FunSuite}

/**
  * Created by peng on 31/03/16.
  */
trait Abs {

  def base: Double

  type Out
}

object SS {

  def fun(abs: Abs {type Out = Int}): Int =

    abs.base.toInt
}

class AbstractTypeInheritance extends FunSuite with BeforeAndAfterAll {

  test("1") {

    val a1 = new Abs {
      override def base: Double = 2.0
      override type Out = Int
    }

    val a2 = new Abs {
      override def base: Double = 3.0
      override type Out = Long
    }

    assert(SS.fun(a1).isInstanceOf[Int])
    assert(SS.fun(a1) == 2)

    //this will trigger a compilation error
    assert(SS.fun(a2).isInstanceOf[Long])
    assert(SS.fun(a2) == 3L)
  }
}
