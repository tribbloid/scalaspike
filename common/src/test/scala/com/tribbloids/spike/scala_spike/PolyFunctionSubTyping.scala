package com.tribbloids.spike.scala_spike

object PolyFunctionSubTyping {

  trait T1

  trait T2 extends T1

  trait T3 extends T2

  trait Universe[T] {

    def apply: T => Seq[T]
  }

  trait Cov[+T] {

    def apply: Any => Seq[T]
  }

  trait Contra[-T] {

    def apply(v: T): Any
  }

  trait MidBound {

    def apply[T <: T2]: T => Seq[T]
  }

  trait LeastBound {

    def apply[T <: Nothing]: T => Any
  }

  object HasBetterBound {

    trait X extends MidBound {

      override def apply[T <: T1]: T => Seq[T]
    }

    trait Y extends LeastBound {

      override def apply[T <: T1]: T => Seq[T]
    }

//    trait Z extends LeastBound {
//
//      type RR[T] = T match {
//        case Option[i] => Seq[i]
//      }
//
//      override def apply[T]: T => RR[T] = ???
//    }
//
//    object Z extends Z

//    Z.apply(3) won't work
  }

//  trait HasWorserBound extends HasBound {
//
//    override def apply[T <: T3]: T => Seq[T] // doesn't work
//  }
}
