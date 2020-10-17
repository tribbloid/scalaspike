package com.tribbloids.spike.meta.multistage.lms

import com.tribbloids.spike.BaseSpec

import scala.language.implicitConversions
import scala.util.continuations._

object ReverseGrad_CPSImproved {

  case class Num(
      x: Double,
      var d: Double = 0.0
  ) {

    def +(that: Num): Num = {
      val impl: (Num => Unit) => Unit = { (cont: Num => Unit) =>
        val y = Num(x + that.x)

        cont(y)

        this.d += y.d
        that.d += y.d
      }

      shift(impl)
    }

    def *(that: Num): Num = shift { (cont: Num => Unit) =>
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

class ReverseGrad_CPSImproved extends BaseSpec {

  import ReverseGrad_CPSImproved._

  it("simple") {

    // CPS
    val fn = { x: Num =>
      (x + 3) * (x + 4)
    }

    val gg = grad(fn)(3)

    println(gg)
  }
}
