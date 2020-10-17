package com.tribbloids.spike.meta.multistage.lms

import com.tribbloids.spike.BaseSpec

import scala.language.implicitConversions

object ReverseGrad_CPS {

  case class Num(
      x: Double,
      var d: Double = 0.0
  ) {

    def +(that: Num)(cont: Num => Unit): Unit = {
      val y = Num(x + that.x)

      cont(y)

      this.d += y.d
      that.d += y.d
    }

    def *(that: Num)(cont: Num => Unit): Unit = {

      val y = Num(x * that.x)

      cont(y)

      this.d += that.x * y.d
      that.d += this.x * y.d
    }
  }

  object Num {

//    implicit def fromX(x: Double): Num =Num(x)
  }

  type CPS[I, O] = I => (O => Unit) => Unit

  def grad(f: CPS[Num, Num])(x: Double): Double = {

    val _x = Num(x)
    f(_x) { z =>
      z.d = 1.0
    }
    _x.d
  }
}

class ReverseGrad_CPS extends BaseSpec {

  import ReverseGrad_CPS._

  it("simple") {

    // CPS
    val fn: CPS[Num, Num] = { x: Num => (cont: Num => Unit) =>
      x.+(Num(3.0)) { x2: Num =>
        x.+(Num(4.0)) { x3: Num =>
          val result = x2.*(x3)(cont)

          result
        }
      }
    }

    val gg = grad(fn)(3)

    println(gg)

  }
}
