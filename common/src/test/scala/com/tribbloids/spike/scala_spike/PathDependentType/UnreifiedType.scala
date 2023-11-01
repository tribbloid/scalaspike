package com.tribbloids.spike.scala_spike.PathDependentType

import ai.acyclic.prover.commons.debug.print_@
import ai.acyclic.prover.commons.testlib.BaseSpec
import ai.acyclic.prover.commons.viz.TypeViz

import scala.reflect.runtime.universe

object UnreifiedType {

  trait Supe {

    type Out <: Supe
    def out: Out
  }

  class Reif1 extends Supe {

    type Out = Reif1
    override def out: Out = this
  }

  class Reif2 extends Supe {

    type Out >: this.type <: Reif2
    override def out: Out = this
  }

  class Reif3 extends Supe {

    type Out >: this.type <: Supe
    override def out: Out = this
  }
}

class UnreifiedType extends BaseSpec {

  import UnreifiedType._

  it("ttg from type") {

    val rr = new Reif2

    val ttg = universe.typeTag[rr.Out]

    print_@(TypeViz(ttg))
  }

  it("ttg from archetype") {

    {
      val ttg = universe.typeTag[Reif1#Out]
      print_@(TypeViz(ttg))
    }

    {
      // TODO: this doesn't work, why?
      //      val ttg = universe.typeTag[Reif2#Out]
      //      print_@(TypeViz(ttg))
    }
  }
}
