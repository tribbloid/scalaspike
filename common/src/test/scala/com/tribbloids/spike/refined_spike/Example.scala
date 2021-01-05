package com.tribbloids.spike.refined_spike

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.Size
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric.Positive
import org.scalatest.funspec.AnyFunSpec

class Example extends AnyFunSpec {

  import eu.timepit.refined.auto._
  import shapeless.{Witness => W}

  describe("simple") {

    it("Positive") {

      type Nat = Int Refined Positive

      val v1: Nat = 5

      // cannot compile
      //        val v2: Nat = -5
    }
  }

  describe("Size") {

    it("of List") {

      type Vec5 = List[Int] Refined Size[Equal[W.`5`.T]]

      // TODO: doesn't work
//      val v1: Vec5 = List(1, 2, 3, 4, 5)
//
//      val v2: Vec5 = List(1 to 5: _*)
    }

    it("of String") {

      type Str5 = String Refined Size[Equal[W.`5`.T]]

      val v1: Str5 = "abcde"

//      val v2: Str5 = "Abcd" // cannot compile
    }
  }
}
