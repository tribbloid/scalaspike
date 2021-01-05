package com.tribbloids.spike.meta.multistage.lms

import cats.Eval
import com.tribbloids.graph.commons.testlib.Benchmark
import org.scalatest.funspec.AnyFunSpec

import scala.language.implicitConversions

object ReverseGrad_TrampolineLogStack {

  trait Stack {

    @volatile var maxStack: Long = 0L

    def increase(): Unit = {

      val v = Thread.currentThread().getStackTrace.length
      if (v > maxStack) maxStack = v
    }
  }

  object Fwd extends Stack {}

  object Rvs extends Stack {}

  trait Shift[O] {

    def getForward: Eval[O]

    final val forward: Eval[O] = {

      getForward.map { v =>
        Fwd.increase()
        v
      }
    }

    def doReverse(cont: O => Unit = _ => {}): Eval[Unit]

    final val reverse: Eval[Unit] = {

      doReverse().map { v =>
        Rvs.increase()
        v
      }
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

class ReverseGrad_TrampolineLogStack extends Benchmark {

  import ReverseGrad_TrampolineLogStack._

  it("simple") {

    val fn = { x: Num =>
      val result = (x + 3) * (x + 4)
      result
    }

    val gg = grad(fn)(3)

    println(gg)
  }

  it("benchmark") {

    for (n <- 1 to Math.pow(2, 6).toInt) {

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
        s"rank = $n,\t diff = $gg,\t time = ${nanoTo - nanoFrom} fwdDepth = ${ReverseGrad_TrampolineLogStack.Fwd.maxStack} " +
          s"rvsDepth = ${ReverseGrad_TrampolineLogStack.Rvs.maxStack}"
      )
    }
  }
}
