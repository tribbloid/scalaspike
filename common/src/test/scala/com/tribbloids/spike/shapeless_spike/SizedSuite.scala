package com.tribbloids.spike.shapeless_spike

import com.tribbloids.spike.BaseSpec
import shapeless.{nat, Sized}

import scala.collection.immutable

class SizedSuite extends BaseSpec {

  it("builder") {

    val v1 = Sized(1, 2, 3, 4)

  }

  it("builder on huge number of arguments") {

    //TODO: oops, doesn't work

    //    val v1 = Sized(
    //      1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8,
    //      9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6,
    //      7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0
    //    )
  }

  it("can be created from List") {

    val v1 = Sized.wrap[List[Int], nat._4](List(1, 2, 3, 4))

    val v2 = Sized.wrap[List[Int], nat._4](List(1, 2, 3)) // TODO: this should fail either compile-time or run-time
  }

  it("cannot be created from non-literal List") {

    val list = (1 to 4).toList

    val v = Sized.wrap(list)

    println(v)
  }
}
