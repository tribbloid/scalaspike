package com.tribbloids.spike.shapeless_spike.RefinedType

import org.scalatest.funspec.AnyFunSpec
import shapeless.Witness
import shapeless.Witness._

import com.tribbloids.spike.shapeless_spike.RefinedType.Example1._

//TODO: already have shapeless spike for this
object Example1 {

  //  val cache
  //
  //  def wit(v: Int) =

  val two: Lt[Int] = Witness(2)
  val three: Lt[Int] = Witness(3)

  val alsoTwo: Lt[Int] = Witness(2)

  assert(two.getClass.isAssignableFrom(alsoTwo.getClass))

  // SU means 'special unitary group'

  class ComplexVector[D <: Lt[Int]](
      val vs: List[(Double, Double)]
  )(
      implicit ev: Aux[D]
  ) {

    {
      val d = vs.size
      assert(ev.value.value == d)
    }

    def +(v2: ComplexVector[D])(
        implicit ev2: D <:< two.type
    ): ComplexVector[D] = {
      val newVs = this.vs.zip(v2.vs).map {
        case (t1, t2) =>
          (t1._1 + t2._1) -> (t1._2 + t2._2)
      }
      new ComplexVector[D](newVs)
    }
  }

  type Quaternion = ComplexVector[two.type]

}

class Example1 extends AnyFunSpec {

  it("2d") {

    val q = new Quaternion(List(1.0 -> 2.0, 3.0 -> 4.0))
    val q2 = new ComplexVector[alsoTwo.type](List(3.0 -> 4.0, 1.0 -> 2.0))

    q + q

    //TODO: how to make it work?
    //    q + q2
    //
    //    q2 + q2
  }

  it("3d") {

    val vec =
      new ComplexVector[three.type](List(1.0 -> 2.0, 3.0 -> 4.0, 5.0 -> 6.0))

    // This will break
    //  {
    //    val v3 = SU_3.plus(vec, vec)
    //  }
  }
}
