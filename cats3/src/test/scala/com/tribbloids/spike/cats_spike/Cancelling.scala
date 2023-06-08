//package com.tribbloids.spike.cats_spike
//
//import cats.effect.{IO, Ref}
//import scala.concurrent.duration
//
//object Cancelling {
//
//  import duration._
//
//  case class Mutating(v: String)
//
//  def main(args: Array[String]): Unit = {
//
//    val ref1: IO[Ref[IO, Mutating]] = Ref[IO].of(
//      Mutating("abc")
//    )
//
//    val ref2 = ref1.flatMap {
//      _ref =>
//        IO.delay(10.seconds) *> _ref.set(Mutating("def"))
//    }
//
//    ref2.unsafeRunCancelable{
//
//    }
//
//
//
//      ref1 *> IO {
//
//    }
//
//    ref1.flatMap {
//      cancel =>
//        cancel.
//    }
//  }
//}
