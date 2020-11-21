package com.tribbloids.spike.meta.multistage.lms

import cats.Eval
import org.scalatest.FunSpec

import scala.language.implicitConversions

object ReverseGrad_Trampolining {

  trait Shift[O] {

    def forward: O

    def reverse(cont: O => Unit = _ => {}): Eval[Unit]

    final val reverseRaw: Eval[Unit] = reverse()
  }

  object Shift {

    trait NoTape[O] extends Shift[O] {

      final override def reverse(cont: O => Unit): Eval[Unit] = {

        Eval.always {
          cont(forward)
        }
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
    o.reverse { z =>
      z.d = 1.0
    }
      .value
    _x.d
  }

  implicit class NumShiftFunctions(val ll: Shift[Num]) {

    case class +(rr: Shift[Num]) extends Shift[Num] {

      override lazy val forward: Num = Num(ll.forward.x + rr.forward.x)

      override def reverse(cont: Num => Unit): Eval[Unit] = {

        val base = Eval.always {

          cont(forward)

          ll.forward.d += forward.d
          rr.forward.d += forward.d
        }

        base.map { _ =>
          ll.reverseRaw.value
          rr.reverseRaw.value
        }
      }
    }

    case class *(rr: Shift[Num]) extends Shift[Num] {

      override lazy val forward: Num = Num(ll.forward.x * rr.forward.x)

      override def reverse(cont: Num => Unit): Eval[Unit] = {

        val base = Eval.always {

          cont(forward)

          ll.forward.d += rr.forward.x * forward.d
          rr.forward.d += ll.forward.x * forward.d
        }

        base.map { _ =>
          ll.reverseRaw.value
          rr.reverseRaw.value
        }

      }
    }
  }
}

class ReverseGrad_Trampolining extends FunSpec {

  import ReverseGrad_Trampolining._

  it("simple") {

    val fn = { x: Num =>
      val result = (x + Num.fromX(3)) * (x + Num.fromX(4))
      result
    }

    val gg = grad(fn)(3)

    println(gg)
  }

  it("benchmark") {

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

      println(s"rank = $n,\t diff = $gg,\t time = ${nanoTo - nanoFrom}")
    }
  }
}
