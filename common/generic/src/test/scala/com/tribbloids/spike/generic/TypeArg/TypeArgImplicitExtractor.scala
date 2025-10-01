package com.tribbloids.spike.generic.TypeArg

import org.scalatest.funspec.AnyFunSpec

class TypeArgImplicitExtractor extends AnyFunSpec {}

object TypeArgImplicitExtractor {

  trait Vec[+T <: AnyRef]

  trait ArgOf[V <: Vec[?]] {

    type TT
  }

  object Case1 {

    implicit def impl1[V <: Vec[?], T0 <: AnyRef](
        implicit
        ev: V <:< Vec[T0]
    ): ArgOf[V] { type TT = T0 } =
      new ArgOf[V] {
        override type TT = T0
      } // this is written by GitHub copilot

    //    implicit def impl2[T0 <: AnyRef, V <: Vec[T0]]: Extractor[V] { type TT = T0 } =
    //      new Extractor[V] {
    //        override type TT = T0
    //      } // this is written by me

    trait StringVec extends Vec[String]

//    val strEx = summon[ArgOf[Vec[String]]]
    //  val strEx = summon[ArgOf[StringVec]]
    //  implicitly[intEx.TT =:= Int] // disabled temporarily for not explaining type reduction

//    val x: String = ??? : strEx.TT
  }

  object Case2 {
    // improvement based on https://github.com/scala/bug/issues/12841

    object ExtractingTypeArg {

      def apply[V <: Vec[?]](
          implicit
          ev: ArgOf[? >: V]
      ): ev.type = ev

      implicit def impl1[T <: AnyRef]: ArgOf[Vec[T]] { type TT = T } =
        new ArgOf[Vec[T]] {
          override type TT = T
        }

      trait StringVec extends Vec[String]

      val strEx1 = apply[Vec[String]]
      val strEx2 = apply[StringVec]
      val x: String = ??? : strEx1.TT
      val y: String = ??? : strEx2.TT

//      summon[ArgOf[? >: StringVec]]
    }
  }
}
