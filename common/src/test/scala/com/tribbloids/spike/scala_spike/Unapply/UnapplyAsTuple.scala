package com.tribbloids.spike.scala_spike.Unapply

import ai.acyclic.prover.commons.testlib.BaseSpec

import scala.reflect.ClassTag

class UnapplyAsTuple extends BaseSpec {
  import UnapplyAsTuple.*

  it("can apply to tuple") {

    val t1 = T1(1, 2)
    val (x, y) = t1
  }
}

object UnapplyAsTuple {

  case class T1(a: Int, b: Int)

  // TODO: implement an unapply function to fix the compilation, do not change the test case
  object T1 {
    def apply(a: Int, b: Int): (Int, Int) = (a, b)
    def unapply(arg: T1): Option[(Int, Int)] = Some((arg.a, arg.b))
  }

  {
    case class Expr[T]() {}
    object Expr {

      def unapply[X: ClassTag, Y: ClassTag](arg: Expr[(X, Y)]): Option[(Expr[X], Expr[Y])] = ???
    }

    val a: Expr[(Int, Int)] = ???
    a match {
      case Expr(b, c) => ???
    }
  }
}
