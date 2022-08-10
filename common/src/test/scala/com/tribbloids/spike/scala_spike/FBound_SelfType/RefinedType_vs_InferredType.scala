package com.tribbloids.spike.scala_spike.FBound_SelfType

import ai.acyclic.graph.commons.debug.print_@
import ai.acyclic.graph.commons.testlib.BaseSpec
import ai.acyclic.graph.commons.viz.TypeViz
import ai.acyclic.graph.commons.viz.TypeViz.TypeTag
import com.tribbloids.spike.scala_spike.FBound_SelfType.RefinedType_vs_InferredType.{ParamTyped, Typed}

class RefinedType_vs_InferredType extends BaseSpec {

  it("can extract both refined & inferred type") {

    val v = Typed()

    print_@(TypeViz(v.refinedTTag))
    print_@(TypeViz(v.refinedSuperTTag))
    print_@(TypeViz(v.inferredTTag))
  }

  it("... even if parameterised") {

    val v = ParamTyped[String, Int]()

    print_@(TypeViz(v.refinedTTag))
    print_@(TypeViz(v.refinedSuperTTag))
    print_@(TypeViz(v.inferredTTag))
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
