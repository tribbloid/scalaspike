package com.tribbloids.spike.scala_spike

object PartialUnification {
  trait Widen[M[_]] { def widen[A, B >: A](ma: M[A]): M[B] }

  object Widen {
    implicit class Ops[M[_], A](ma: M[A]) {

      def widen[B >: A](implicit ev: Widen[M]): M[B] = ev.widen[A, B](ma)
    }
    // implicit class OpsNothing[M[_]](ma: M[Nothing]) {
    //   def widen[B](implicit ev: Widen[M]): M[B] = ev.widen(ma)
    // }
    implicit val WidenList = new Widen[List] { def widen[A, B >: A](l: List[A]): List[B] = l }
  }

  import Widen._

  List.empty[Some[Int]].widen[Option[Int]]

//  List.empty[Nothing].widen[Int] // does not compile until uncommenting OpsNothing
}
