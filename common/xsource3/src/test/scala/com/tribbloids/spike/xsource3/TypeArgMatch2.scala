package com.tribbloids.spike.xsource3

import com.tribbloids.spike.xsource3.Summoner.summon

object TypeArgMatch2 {

  {
    trait FnUnpack[F <: Function1[?, ?]] {
      type II
      type OO
    }

    object FnUnpack {

      implicit def only[I, O]: FnUnpack[I => O] { type II = I; type OO = O } =
        null
    }

    val u = summon[FnUnpack[Int => String]]
    summon[u.II =:= Int]
  }

  {
    trait FnUnpack[-F <: Function1[?, ?]] {
      type II
      type OO
    }

    object FnUnpack {

      implicit def only[I, O]: FnUnpack[I => O] { type II = I; type OO = O } =
        null
    }

    summon[FnUnpack[Int => String]]
//    summon[u.II =:= Int] // TODO: only works in scala3
  }

  {
    trait FnUnpack[F] {
      type II
      type OO
    }

    object FnUnpack {
      // Use an implicit def with a type bound
      implicit def only[F <: Function1[I, O], I, O]: FnUnpack[F] { type II = I; type OO = O } = null
    }

    // Test with a function type
//    val u = summon[FnUnpack[Int => String]]
//    summon[u.II =:= Int] // TODO: only works in scala3
  }
}
