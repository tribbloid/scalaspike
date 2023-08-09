package com.tribbloids.spike

object Summoner {

  // backported from Scala 3, tighter than scala 2's `implicitly[T]`
  def summon[T](
      implicit
      x: T
  ): x.type = x

  { // demo
    trait Foo
    object Foo {
      trait Foo1 extends Foo

      implicit def summon: Foo1 = new Foo1 {}
    }

    {
      val foo = summon[Foo]
      val foo1: Foo.Foo1 = foo
    }

    {
      val foo = implicitly[Foo]
      //      val foo1: Foo.Foo1 = foo // fail to compile
    }
  }
}
