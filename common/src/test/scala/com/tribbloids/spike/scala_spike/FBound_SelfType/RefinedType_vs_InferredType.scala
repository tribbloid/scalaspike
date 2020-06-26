package com.tribbloids.spike.scala_spike.FBound_SelfType

import com.tribbloids.spike.ScalaReflection.universe
import com.tribbloids.spike.scala_spike.FBound_SelfType.RefinedType_vs_InferredType.{ParamTyped, Typed}
import com.tribbloids.spike.{BaseSpec, DebugUtils, TypeView}

class RefinedType_vs_InferredType extends BaseSpec {

  import DebugUtils._

  it("can extract both refined & inferred type") {

    val v = Typed()

    printEval(TypeView(v.refinedTTag.tpe))
    printEval(TypeView(v.refinedSuperTTag.tpe))
    printEval(TypeView(v.inferredTTag.tpe))
  }

  it("... even if parameterised") {

    val v = ParamTyped[String, Int]()

    printEval(TypeView(v.refinedTTag.tpe))
    printEval(TypeView(v.refinedSuperTTag.tpe))
    printEval(TypeView(v.inferredTTag.tpe))
  }
}

object RefinedType_vs_InferredType {

  import com.tribbloids.spike.ScalaReflection.universe._

  trait TTagInfo {

    def inferredTTag[E >: this.type](implicit ev: TypeTag[E]): universe.TypeTag[E] = implicitly[TypeTag[E]]

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
