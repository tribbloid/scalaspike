package com.tribbloids.spike.meta.multistage.lms

import com.tribbloids.spike.Benchmark

import scala.language.implicitConversions

object ReverseGrad_CPSImproved {

  import scala.util.continuations._

  case class Num(
      x: Double,
      var d: Double = 0.0
  ) {

    def +(that: Num) = shift { (cont: Num => Unit) =>
      val y = Num(x + that.x)

      cont(y)

      this.d += y.d
      that.d += y.d
    }

    def *(that: Num) = shift { (cont: Num => Unit) =>
      val y = Num(x * that.x)

      cont(y)

      this.d += that.x * y.d
      that.d += this.x * y.d
    }
  }

  object Num {

    implicit def fromX(x: Double): Num = Num(x)
  }

  def grad(f: Num => Num @cps[Unit])(x: Double): Double = {

    val _x = Num(x)
    reset { f(_x).d = 1.0 }

    _x.d
  }
}

class ReverseGrad_CPSImproved extends Benchmark {

  import ReverseGrad_CPSImproved._

  import scala.util.continuations._

  it("simple") {

    val fn: Num => Num @cps[Unit] = { x: Num =>
      val result = (x + 3) * (x + 4)

      result
    }

    val gg = grad(fn)(3)

    println(gg)
  }

  it("benchmark") {

    profile.run {
      for (n <- 1 to Math.pow(2, 8).toInt) {

        //      var fn: Num => Num @cps[Unit] = { x: Num =>
        //        x + 1
        //      }
        //
        //      for (j <- 2 to n) {
        //
        //        fn = fn.andThen { x =>
        //          val result = x * (x + j)
        //          result
        //        }
        //      }

        //      val fn = { x: Num =>
        //        val result = (2 to n).foldLeft(x + 1) { (half, j) =>
        //          half * (x + j)
        //        }
        //
        //        result
        //      }

        def fn(base: Int = 1)(x: Num): Num @cps[Unit] = {

          if (base >= n) x + base
          else (x + base) * fn(base + 1)(x)
        }

        val nanoFrom = System.nanoTime()
        val gg = grad(fn())(3)
        val nanoTo = System.nanoTime()

//        println(s"rank = $n, diff = $gg,\t time = ${nanoTo - nanoFrom}")
      }
    }
      .log()
  }
}
