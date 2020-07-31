package com.tribbloids.spike.singleton_ops_spike

import com.tribbloids.spike.BaseSpec
import graph.commons.util.WideTyped
import graph.commons.util.debug.print_@
import graph.commons.util.viz.VizType
import shapeless.Witness
import singleton.ops.{==, Require}
import singleton.twoface.TwoFace
import singleton.twoface.impl.TwoFaceAny

import scala.language.existentials
import scala.util.Random

class TwoFaceExample extends BaseSpec {

  it("can be created from literal") {

    val v1 = TwoFace.Int.apply(3)

    print_@(VizType.infer(v1))
    assert(v1.isLiteral)

    val v2 = TwoFace.Int.apply(4)

    print_@(VizType.infer(v2))
    assert(v2.isLiteral)

    val v3 = v1 + v2

    print_@(VizType.infer(v3))
    assert(v3.isLiteral)

    implicitly[Require[v3.Out == Witness.`7`.T]]

  }

  it("can be cast from literal") {

    val v1 = TwoFace.Int.apply(3)

    print_@(VizType.infer(v1))
    assert(v1.isLiteral)

    val v2 = {

      4: TwoFace.Int[_]

      //      NarrowView[TwoFace.Int[_]]().cast(4)

    }

    print_@(VizType.infer(v2))
    assert(!v2.isLiteral)

    val v3 = v1 + v2

    print_@(VizType.infer(v3))
    assert(!v3.isLiteral)

//    implicitly[Require[v3.Out == Witness.`7`.T]] TODO: can it be made to work?

  }

  it("can be created/cast from variable") {

    val v1: TwoFaceAny.Int[Int] = TwoFace.Int(Random.nextInt(5))

    print_@(VizType.infer(v1))
    assert(!v1.isLiteral)

    val v2 = Random.nextInt(6): TwoFaceAny.Int[_]

    print_@(VizType.infer(v2))
    assert(!v2.isLiteral)

    val v3 = v1 + v2

    print_@(VizType.infer(v3))
    assert(!v3.isLiteral)
  }

  it("type can summon instance") {

    val v1 = TwoFace.Int(Random.nextInt(5))

    val v2 = TwoFace.Int(Random.nextInt(5))

    val v3 = v1 + v2

//    print_@(VizType.apply[v3.type])

    val t1 = WideTyped(v1)
    val t2 = WideTyped(v2)
    val t3 = WideTyped(v3)
    print_@(VizType.apply[t3.Wide])

//    type K = AcceptNonLiteral[t1.TT + t2.TT] // NO it cannot
//    val k = implicitly[K]
//    k.value
  }

//  it("an implicit function can be defined to convert Int to TwoFace instance") {
//
//    def convert[Out <: Int](v: Int): TwoFaceAny.Int[Out] = {
//
//      val result = TwoFace.Int.apply[v.type, Out](v)
//      result
//    }
//
//    val v1 = convert(3)
//    assert(v1.isLiteral)
//
//    val v2 = convert(Random.nextInt(5))
//    assert(!v1.isLiteral)
//  }
}
