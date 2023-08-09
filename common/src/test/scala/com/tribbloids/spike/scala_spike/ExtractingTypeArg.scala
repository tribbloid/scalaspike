package com.tribbloids.spike.scala_spike

import com.tribbloids.spike.Summoner
import org.scalatest.funspec.AnyFunSpec

class ExtractingTypeArg extends AnyFunSpec {}

object ExtractingTypeArg {

  trait Vec[+T]

  trait Extractor[V <: Vec[_]] {

    type TT
  }

  object Extractor {

    implicit def impl1[V <: Vec[_], T0](implicit ev: V =:= Vec[T0]): Extractor[V] { type TT = T0 } =
      new Extractor[V] {
        override type TT = T0
      } // this is written by GitHub copilot

//    implicit def impl2[T0, V <: Vec[T0]]: Extractor[V] { type TT = T0 } =
//      new Extractor[V] {
//        override type TT = T0
//      } // this is written by me
  }

  val intEx = Summoner.summon[Extractor[Vec[Int]]]
//  implicitly[intEx.TT =:= Int] // disabled temporarily for not explaining type reduction

  val x: Int = ??? : intEx.TT
}
