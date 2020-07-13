package com.tribbloids.spike.scala_spike.FBound_SelfType

import com.tribbloids.spike.BaseSpec
import com.tribbloids.spike.scala_spike.FBound_SelfType.RefinedType_vs_InferredType.{ParamTyped, Typed}
import com.tribbloids.spike.utils.TypeTag
import com.tribbloids.spike.utils.debug.{print_@, ShowType}

class RefinedType_vs_InferredType extends BaseSpec {

  it("can extract both refined & inferred type") {

    val v = Typed()

    print_@(ShowType(v.refinedTTag))
    print_@(ShowType(v.refinedSuperTTag))
    print_@(ShowType(v.inferredTTag))
  }

  it("... even if parameterised") {

    val v = ParamTyped[String, Int]()

    print_@(ShowType(v.refinedTTag))
    print_@(ShowType(v.refinedSuperTTag))
    print_@(ShowType(v.inferredTTag))
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
