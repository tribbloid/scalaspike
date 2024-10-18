package com.tribbloids.spike.xsource3

object TransitiveConversion {
  import scala.language.implicitConversions

  trait Conversion[A, B] extends (A => B)

  object OldStyle {

    class A(val value: Int)

    class B(val a: A)

    class C(val b: B)

    trait Conv[A, B] extends Conversion[A, B]

    implicit def ab: Conv[A, B] = { (a: A) =>
      new B(a)
    }

    implicit def bc: Conv[B, C] = { (b: B) =>
      new C(b)
    }

    object ConvUtils {
      implicit def hypotheticalSyllogism[A, B, C](
          implicit
          ab: Conv[A, B],
          bc: Conv[B, C]
      ): Conv[A, C] = {

        new Conv[A, C] {
          def apply(a: A): C = bc(ab(a))
        }
      }
    }
    import ConvUtils.hypotheticalSyllogism

    def demo(): Unit = {
      val a = new A(42)
      val c: C = a
      println(c.b.a.value) // Outputs: 42
    }
  }

}
