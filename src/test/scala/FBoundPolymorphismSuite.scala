import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.reflect._

/**
  * Created by peng on 05/03/16.
  */

class FBoundPolymorphismSuite extends FunSuite with BeforeAndAfterAll {

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

class FBoundPolymorphismSuite2 extends FunSuite with BeforeAndAfterAll {

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

    override def respond: List[FBoundSubImpl2] = List(new FBoundSubImpl2(this.s + "b"))
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