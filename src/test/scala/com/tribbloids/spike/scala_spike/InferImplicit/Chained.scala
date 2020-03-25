package com.tribbloids.spike.scala_spike.InferImplicit

import scala.reflect.ClassTag

object Chained {

  trait Super {

    type A
    implicit val ev: ClassTag[A]
  }

//  class Sub1 extends Super {
//    override type A = String
////    override implicit val ev: ClassTag[String] = implicitly[ClassTag[String]]
//  }
}
