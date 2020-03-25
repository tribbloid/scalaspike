//package com.tribbloids.spike.spike
//
//import org.scalatest.FunSpec
//
//import scala.language.implicitConversions
//import com.tribbloids.spike.spike.PrecedingImplicit.B
//
//object PrecedingImplicit {
//
//  trait Companion {
//
//    implicit def str(a: A): String =
//      s"${this.getClass.getSimpleName}: %d" format a.n
//  }
//
//  class A(val n: Int)
//  object A extends Companion {}
//
//  class B(val x: Int, y: Int) extends A(y)
//  object B extends Companion {}
//}
//
//class PrecedingImplicit extends FunSpec {
//
//  it("") {
//
//    val b = new B(5, 2)
//    val s: String = b
//    println(s)
//  }
//}
