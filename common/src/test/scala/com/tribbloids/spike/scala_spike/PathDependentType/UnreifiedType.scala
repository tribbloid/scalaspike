package com.tribbloids.spike.scala_spike.PathDependentType

import com.tribbloids.graph.commons.testlib.BaseSpec
import com.tribbloids.graph.commons.util.debug.print_@
import com.tribbloids.graph.commons.util.viz.VizType

import scala.language.existentials

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
  import com.tribbloids.graph.commons.util.ScalaReflection.universe

  it("ttg from type") {

    val rr = new Reif2

    val ttg = universe.typeTag[rr.Out]

    print_@(VizType(ttg))
  }

  it("ttg from archetype") {

    {
      val ttg = universe.typeTag[Reif1#Out]
      print_@(VizType(ttg))
    }

    {
      // TODO: this doesn't work, why?
      //      val ttg = universe.typeTag[Reif2#Out]
      //      print_@(VizType(ttg))
    }
  }
}
