package com.tribbloids.spike.meta.multistage.lms

import com.tribbloids.spike.Benchmark

import scala.language.implicitConversions

object ReverseGrad_NoCPS {

  trait Shift[O] {

    def forward: O

    def doReverse(cont: O => Unit = _ => {}): Unit

    def reverse(): Unit = doReverse()
  }

  object Shift {

    trait NoTape[O] extends Shift[O] {

      final override def doReverse(cont: O => Unit): Unit = {

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
  }

  type CPS[I, O] = I => Shift[O]

  def grad(f: CPS[Num, Num])(x: Num): Double = {

    val _x = x
    val o = f(_x)
    o.doReverse { z =>
      z.d = 1.0
    }
    _x.d
  }

  implicit class NumShiftFunctions(val ll: Shift[Num]) {

    case class +(rr: Shift[Num]) extends Shift[Num] {

      override lazy val forward: Num = Num(ll.forward.x + rr.forward.x)

      override def doReverse(cont: Num => Unit): Unit = {

        cont(forward)

        ll.forward.d += forward.d
        rr.forward.d += forward.d

        ll.reverse()
        rr.reverse()
      }
    }

    case class *(rr: Shift[Num]) extends Shift[Num] {

      override lazy val forward: Num = Num(ll.forward.x * rr.forward.x)

      override def doReverse(cont: Num => Unit): Unit = {

        cont(forward)

        ll.forward.d += rr.forward.x * forward.d
        rr.forward.d += ll.forward.x * forward.d

        ll.reverse()
        rr.reverse()
      }
    }
  }
}

class ReverseGrad_NoCPS extends Benchmark {

  import ReverseGrad_NoCPS._

  it("simple") {

    val fn = { x: Num =>
      val result = (x + 3) * (x + 4)
      result
    }

    val gg = grad(fn)(3)

    println(gg)
  }

  it("benchmark") {

    profile
      .run {
        for (n <- 1 to Math.pow(2, 8).toInt) {

          val fn = { x: Num =>
            var result: Shift[Num] = x + 1

            for (j <- 2 to n) {
              result = result * (x + j)
            }

            result
          }

          val nanoFrom = System.nanoTime()
          val gg = grad(fn)(3)
          val nanoTo = System.nanoTime()

//        println(s"rank = $n,\t diff = $gg,\t time = ${nanoTo - nanoFrom}")
        }
      }
      .log()

  }
}
