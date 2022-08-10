package com.tribbloids.spike.scala_spike.Implicit

import scala.reflect.ClassTag

object Chained {

  trait Super {

    type A
    implicit val ev: ClassTag[A]
  }

  // TODO: spike is incomplete!
  class Sub1 extends Super {
    override type A = String
    implicit override val ev: ClassTag[String] = implicitly[ClassTag[String]]
  }
}
