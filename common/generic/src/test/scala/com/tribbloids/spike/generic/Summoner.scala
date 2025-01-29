package com.tribbloids.spike.generic

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
      foo
    }

    {
//      val foo: Foo.Foo1 = implicitly[Foo] // doesn't work
    }
  }
}
