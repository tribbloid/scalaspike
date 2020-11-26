package com.tribbloids.spike.meta.multistage.lms

import cats.Eval
import org.scalatest.FunSpec

import scala.language.implicitConversions

object ReverseGrad_Trampoline {


  trait Shift[O] {

    def getForward: Eval[O]

    final val forward: Eval[O] = {

      getForward
    }

    def doReverse(cont: O => Unit = _ => {}): Eval[Unit]

    final val reverse: Eval[Unit] = {

      doReverse()
    }
  }

  object Shift {

    trait NoTape[O] extends Shift[O] {

      final override def doReverse(cont: O => Unit): Eval[Unit] = {

        forward.map { v =>
          cont(v)
        }
      }
    }
  }

  case class Num(
      x: Double,
      var d: Double = 0.0
  ) extends Shift.NoTape[Num] {

    override def getForward: Eval[Num] = Eval.later(this)
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
    }.value

    _x.d
  }

  implicit class NumShiftFunctions(val ll: Shift[Num]) {

    case class +(rr: Shift[Num]) extends Shift[Num] {

      override def getForward: Eval[Num] = {

        val result = for (llf <- ll.forward;
                          rrf <- rr.forward) yield {
          Num(llf.x + rrf.x)
        }

        result.memoize
      }

      override def doReverse(cont: Num => Unit): Eval[Unit] = {

        val base = forward.map { v =>
          cont(v)

          ll.forward.value.d += v.d
          rr.forward.value.d += v.d
        }


        for (_ <- base;
             _ <- ll.reverse;
             _ <- rr.reverse) yield {

          Unit
        }
      }
    }

    case class *(rr: Shift[Num]) extends Shift[Num] {

      override def getForward: Eval[Num] = {

        val result = for (llf <- ll.forward;
                          rrf <- rr.forward) yield {

          Num(llf.x * rrf.x)
        }

        result.memoize
      }

      override def doReverse(cont: Num => Unit): Eval[Unit] = {

        val base = forward.map { v =>
          cont(v)

          ll.forward.value.d += rr.forward.value.x * v.d
          rr.forward.value.d += ll.forward.value.x * v.d
        }

        for (_ <- base;
             _ <- ll.reverse;
             _ <- rr.reverse) yield {

          Unit
        }
      }
    }
  }
}

class ReverseGrad_Trampoline extends FunSpec {

  import ReverseGrad_Trampoline._

  it("simple") {

    val fn = { x: Num =>
      val result = (x + 3) * (x + 4)
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

      println(
        s"rank = $n,\t diff = $gg,\t time = ${nanoTo - nanoFrom}"
      )
    }
  }
}
