package com.tribbloids.spike.meta.multistage.lms

import com.tribbloids.spike.meta.multistage.lms.ReverseGrad_NoCPS.Num.NumShiftFunctions
import org.scalatest.FunSpec

import scala.language.implicitConversions

object ReverseGrad_NoCPS {

//  case class Shift[O](
//      fn: (O => Unit) => Unit
//  ) {}

  trait Shift[O] {

    def forward: O

    def reverse(cont: O => Unit): Unit

    def reverse(): Unit = reverse(_ => {})
  }

  object Shift {

    trait NoTape[O] extends Shift[O] {

      final override def reverse(cont: O => Unit): Unit = {

        cont(forward)
      }
    }
  }

  case class Num(
      x: Double,
      var d: Double = 0.0
  ) extends Shift.NoTape[Num] {

    override def forward: Num = this

  }

  object Num {

    implicit def fromX(x: Double): Num = Num(x)

    implicit class NumShiftFunctions(val ll: Shift[Num]) {

      case class +(rr: Shift[Num]) extends Shift[Num] {

        override lazy val forward: Num = Num(ll.forward.x + rr.forward.x)

        override def reverse(cont: Num => Unit): Unit = {

          cont(forward)

          ll.forward.d += forward.d
          rr.forward.d += forward.d

          ll.reverse()
          rr.reverse()
        }
      }

      case class *(rr: Shift[Num]) extends Shift[Num] {

        override lazy val forward: Num = Num(ll.forward.x * rr.forward.x)

        override def reverse(cont: Num => Unit): Unit = {

          cont(forward)

          ll.forward.d += rr.forward.x * forward.d
          rr.forward.d += ll.forward.x * forward.d

          ll.reverse()
          rr.reverse()
        }
      }
    }
  }

  type CPS[I, O] = I => Shift[O]

  def grad(f: CPS[Num, Num])(x: Num): Double = {

    val _x = x
    val o = f(_x)
    o.reverse { z =>
      z.d = 1.0
    }
    _x.d
  }
}

class ReverseGrad_NoCPS extends FunSpec {

  import ReverseGrad_NoCPS._

  it("simple") {

    val fn = { x: Num =>
      val result = (x + 3) * (x + 4)
      result
    }

    val gg = grad(fn)(3)

    println(gg)
  }
}
