package com.tribbloids.spike.scala_spike.Annotation

import ai.acyclic.prover.commons.testlib.BaseSpec
import com.tribbloids.spike.scala_spike.Annotation.ShapelessAnnotation.FieldAnnotations
import shapeless.ops.hlist.Zip
import shapeless.ops.record.Keys
import shapeless.{Annotation, Annotations, HList, HNil, LabelledGeneric}

class ShapelessAnnotation extends BaseSpec {

  import Static._
  import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

  it("singular") {

//    val a1 = Annotation.materialize[SA1, Common].apply()
    val a1 = Annotation[SA1, Common].apply()

    a1 shouldBe SA1()

    println(a1)
  }

  it("plural") {
//    println(Annotations.apply[SA2, Common].apply()) // doesn't work

    println(Annotations.apply[SA1, Prod].apply()) // yield nothing
    println(Annotations.apply[SA2, Prod].apply())
  }

  it("summon") {

    val refl = new FieldAnnotations[SA2, Prod]()

    val list = refl.summon

    println(list.asTuples)
  }

  it("... implicitly") {

    object refl extends FieldAnnotations[SA2, Prod]()

    val list = implicitly[refl.Impl]

    println(list.asTuples)
  }

  it("... again") {

    object refl extends FieldAnnotations[SA2, Prod]()
    val list = refl.get

    println(list.asTuples)
  }
}

object ShapelessAnnotation {

  import shapeless.::

  class FieldAnnotations[A, T] {

    trait Impl {

      def asTuples: List[(String, Option[A])]
    }

    def get(implicit e: this.Impl): Impl = implicitly[this.Impl]

    implicit def summon[G <: HList, KS <: HList, VS <: HList](
        implicit
        toGeneric: LabelledGeneric.Aux[T, G],
        toKs: Keys.Aux[G, KS],
        toVs: Annotations.Aux[A, T, VS],
        zipping: Zip[KS :: VS :: HNil]
    ): Impl = {

      new Impl {
        override def asTuples: List[(String, Option[A])] = {

          zipping
            .apply(toKs() :: toVs() :: HNil)
            .runtimeList
            .asInstanceOf[List[(Symbol, Option[A])]]
            .map {
              case (k, v) => (k.name, v)
            }
        }
      }
    }
  }

  object FieldAnnotations {}
}
