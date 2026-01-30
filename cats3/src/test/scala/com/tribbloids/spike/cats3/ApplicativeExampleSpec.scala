package com.tribbloids.spike.cats3

import cats.implicits.*
import org.scalatest.funspec.AnyFunSpec

class ApplicativeExampleSpec extends AnyFunSpec {

  describe("Applicative usage with <* operator") {

    it("works with Option") {
      val func: Option[Int => Int] = Some((x: Int) => x * 2)
      val value: Option[Int] = Some(10)

      // The <*> operator is an alias for `ap` in Cats.
      // definition: def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
      // syntax: ff <*> fa
      val result = func <*> value

      assert(result == Some(20))
    }

    it("works with List (Cartesian product)") {
      val funcs: List[Int => Int] = List(_ + 1, _ * 2)
      val values: List[Int] = List(1, 2)

      // Applicative for List creates a cartesian product of results
      // i.e., applying every function to every value
      val result = funcs <*> values

      // expected: List((1+1), (2+1), (1*2), (2*2)) -> List(2, 3, 2, 4)
      assert(result == List(2, 3, 2, 4))
    }

    it("can be chained (arity-n)") {
      // To chain <*>, we need a curried function in the context
      val add3: Int => Int => Int => Int = x => y => z => x + y + z
      val f: Option[Int => Int => Int => Int] = Some(add3)
      val v1 = Some(1)
      val v2 = Some(2)
      val v3 = Some(3)

      // result = f.ap(v1).ap(v2).ap(v3)
      // v1 is applied to f, resulting in Some(y => z => 1 + y + z)
      // v2 is applied to that, resulting in Some(z => 1 + 2 + z)
      // v3 is applied to that, resulting in Some(1 + 2 + 3)
      val result = f <*> v1 <*> v2 <*> v3

      assert(result == Some(6))
    }
  }
}
