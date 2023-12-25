package com.tribbloids.spike.xsource3

object TypeArgMatch1 {

  // from shapeless
  trait DepFn1[T] {
    type Out

    def apply(t: T): Out
  }

  abstract class Fn1AsDep[I] extends DepFn1[I] {}

  // this doesn't work in Scala 2
  object Fn1AsDep {

    type Aux[I, O] = Fn1AsDep[I] { type Out = O }

    implicit def only[I, O](
        implicit
        fn: I => O
    ): Fn1AsDep.Aux[I, O] = new Fn1AsDep[I] {

      type Out = O

      override def apply(t: I): O = fn(t)
    }
  }

  def main(args: Array[String]): Unit = {

    implicit def fn1: Int => String = { (i: Int) =>
      s"$i" + "_"
    }

    def read[I](v: I)(
        implicit
        sniffer: Fn1AsDep[I]
    ): sniffer.Out = {
      sniffer.apply(v)
    }

//    println(read(2))
  }
}
