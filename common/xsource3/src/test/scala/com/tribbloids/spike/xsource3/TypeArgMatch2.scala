package com.tribbloids.spike.xsource3

object TypeArgMatch2 {

  trait UnpackFn[F <: Function1[?, ?]] {
    type II
    type OO
  }

  object UnpackFn {

    implicit def only[I, O]: UnpackFn[Function1[I, O]] { type II = I; type OO = O } =
      new UnpackFn[Function1[I, O]] { type II = I; type OO = O }
  }

  def unpackFor[F <: Function1[?, ?]](
      implicit
      ev: UnpackFn[F]
  ): ev.type = ev

  def main(args: Array[String]): Unit = {

    val unpack = {

      unpackFor[Int => String]
//      Summoner.summon[UnpackFn[Int => String]]
    }

    implicitly[unpack.II =:= Int]
    implicitly[unpack.OO =:= String]

  }
}
