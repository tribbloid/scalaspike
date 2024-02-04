package com.tribbloids.spike.xsource3

object PolymorphicTypeArgs {

  class Dummy {

    implicit def getThis: this.type = this
    // also found in Tiark Rompf's LMS implementation, too bad Scala won't allow a shorter solution
  }
  object Dummy1 extends Dummy
  object Dummy2 extends Dummy
  object Dummy3 extends Dummy

  class HasFns {

    def fn[T](
        implicit
        d: Dummy1.type
    ): T = ???

    def fn[T, T2](
        implicit
        d: Dummy2.type
    ): T2 = ???

    def fn[T, T2, T3](
        implicit
        d: Dummy3.type
    ): T3 = ???
  }

  object HasFns extends HasFns

  def main(args: Array[String]): Unit = {

    HasFns.fn[Int]
    HasFns.fn[Int, String]
    HasFns.fn[Int, String, Boolean]
  }
}
