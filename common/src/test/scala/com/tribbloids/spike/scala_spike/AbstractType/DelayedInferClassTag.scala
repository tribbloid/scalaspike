package com.tribbloids.spike.scala_spike.AbstractType

import org.scalatest.funspec.AnyFunSpec

import scala.reflect.ClassTag

object DelayedInferClassTag {

  trait SS {

    type TT <: Any

    def ctg(implicit tag: ClassTag[TT]): ClassTag[TT] = implicitly[ClassTag[TT]]

    val fakeCtg: ClassTag[None.type] = implicitly[ClassTag[None.type]]

  }

  class Sub1 extends SS {

    override final type TT = Int
  }

  class Sub2 extends SS {

    override final type TT = Double
  }

  class Sub3 extends SS {

    override final type TT = String
  }

}

class DelayedInferClassTag extends AnyFunSpec {

  import DelayedInferClassTag._

  it("1") {

    val sub1 = new Sub1()
    println(sub1.fakeCtg)
    println(sub1.ctg)

    val sub2 = new Sub2()
    println(sub2.fakeCtg)
    println(sub2.ctg)

    val sub3 = new Sub3()
    println(sub3.fakeCtg)
    println(sub3.ctg)
  }
}
