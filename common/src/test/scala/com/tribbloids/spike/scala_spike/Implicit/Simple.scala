package com.tribbloids.spike.scala_spike.Implicit

import scala.language.implicitConversions

object Simple {

  case class Src(ss: String) {

    implicit def toTgt1: Tgt1 = Tgt1(ss)
  }

  object Src {

    implicit def toTgt2(v: Src): Tgt2 = Tgt2(v.ss)
  }

  case class Tgt1(v: String) {

    def isTgt1 = true
  }

  case class Tgt2(v: String) {

    def isTgt2 = true
  }

  val src = Src("abc")

  src.isTgt2

  // this will trigger a compilation error
//  src.isTgt1
}
