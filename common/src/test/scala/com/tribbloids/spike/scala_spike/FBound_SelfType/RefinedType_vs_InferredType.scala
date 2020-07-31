package com.tribbloids.spike.scala_spike.FBound_SelfType

import com.tribbloids.spike.BaseSpec
import com.tribbloids.spike.scala_spike.FBound_SelfType.RefinedType_vs_InferredType.{ParamTyped, Typed}
import graph.commons.util.TypeTag
import graph.commons.util.debug.print_@
import graph.commons.util.viz.VizType

class RefinedType_vs_InferredType extends BaseSpec {

  it("can extract both refined & inferred type") {

    val v = Typed()

    print_@(VizType(v.refinedTTag))
    print_@(VizType(v.refinedSuperTTag))
    print_@(VizType(v.inferredTTag))
  }

  it("... even if parameterised") {

    val v = ParamTyped[String, Int]()

    print_@(VizType(v.refinedTTag))
    print_@(VizType(v.refinedSuperTTag))
    print_@(VizType(v.inferredTTag))
  }
}

object RefinedType_vs_InferredType {

  trait TTagInfo {

    def inferredTTag[E >: this.type](implicit ev: TypeTag[E]): TypeTag[E] = implicitly[TypeTag[E]]

    def refinedSuperTTag: TypeTag[this.type] = implicitly[TypeTag[this.type]]
  }

  case class Typed() extends TTagInfo {

    def refinedTTag: TypeTag[this.type] = implicitly[TypeTag[this.type]]
  }

  case class ParamTyped[T, R]() extends (T => R) with TTagInfo {

    def refinedTTag: TypeTag[this.type] = implicitly[TypeTag[this.type]]

    override def apply(v1: T): R = ???
  }
}
