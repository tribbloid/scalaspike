package com.tribbloids.spike.shapeless_spike

import ai.acyclic.graph.commons.debug.print_@
import ai.acyclic.graph.commons.testlib.BaseSpec
import ai.acyclic.graph.commons.viz.TypeViz
import shapeless.Witness

import scala.language.implicitConversions
import scala.util.Random

class WitnessSuite extends BaseSpec {

  val randN1: Int = Random.nextInt(5) // path!
  lazy val randN2: Int = Random.nextInt(5) // path!
  def randN3: Int = Random.nextInt(5) // no path

  describe("as magnet") {

    it("of literal Int") {

      val w0: Witness.Lt[Int] = 3
      print_@(TypeViz.infer(w0).toString) // TODO: how to get typetag of w0.T ?

      val w1: Witness.Lt[Int] = randN1
      print_@(TypeViz.infer(w1).toString)

      val w2: Witness.Lt[Int] = randN2
      print_@(TypeViz.infer(w2).toString)

      assertDoesNotCompile(
        """
          |val w: Witness.Lt[Int] = randN3
          |""".stripMargin
      )

      assertDoesNotCompile(
        """
          |val w: Witness.Lt[Int] = Random.nextInt(3)
          |""".stripMargin
      )
    }

    it("... with fallback") {

      trait MayHaveWitness {

        type Lit
      }

      class Some(val w: Witness.Lt[Int]) extends MayHaveWitness {

        type Lit = w.T
      }

      object None extends MayHaveWitness {

        type Lit = Nothing
      }

      trait MayHaveWitness_Implicits0 {

        implicit def fromNonLit(v: Integer): None.type = None
      }

      object MayHaveWitness extends MayHaveWitness_Implicits0 {

        //TODO: oops. doesn't work
//        implicit def fromLit[T <: Integer with Singleton](literal: T): Some =
//          new Some(Witness(literal.intValue()))
      }

      val v1: MayHaveWitness = new Integer(3)
      println(v1.getClass)

      val v2: MayHaveWitness = new Integer(Random.nextInt(3))
      println(v2.getClass)
    }

    it(" of null") {

      val w: Witness.Lt[Null] = Witness(null)

      assert(w.value == null)
    }
  }

  def sameType[T1, T2](implicit ev: T1 =:= T2): Unit = {}

  class Wrap[T]() {

    type TT = T

    def sameType[T2](v2: Wrap[T]): Wrap[T] = this
  }

  it("same type") {

    def Wrap(w: Witness): Wrap[w.T] = new Wrap[w.T]()

    val v1 = Wrap(3)
    val v2 = Wrap(3)
    val v3 = Wrap(4)

    v1.sameType(v2)
    sameType[v1.TT, v2.TT]

    assertDoesNotCompile(
      """
        |v1.sameType(v3)
        |""".stripMargin
    )

    assertDoesNotCompile(
      """
        |sameType[v1.TT, v3.TT]
        |""".stripMargin
    )

  }

//  it("mimicking same type") {
//
//    object Fake {
//
//      implicit def convert(v: Any): Fake = new Fake(v)
//    }
//
//    class Fake(val v: Any) {
//
//      val w: Witness = Witness(v)
//
//      type T = w.T
//    }
//
//    def Wrap(w: Fake): Wrap[w.T] = new Wrap[w.T]()
//
//    val v1 = Wrap(3)
//    val v2 = Wrap(3)
//    val v3 = Wrap(4)
//
////    v1.sameType(v2)
//
//    assertDoesNotCompile(
//      """
//        |v1.sameType(v3)
//        |""".stripMargin
//    )
//  }

  it("summon value from type") {

    val w1 = Witness(3)

    type S1 = w1.T

    val v2 = implicitly[Witness.Aux[S1]]

    assert(v2.value == 3)
  }
}
