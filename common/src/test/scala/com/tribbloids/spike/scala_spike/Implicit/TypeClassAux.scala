package com.tribbloids.spike.scala_spike.Implicit

import com.tribbloids.graph.commons.testlib.BaseSpec
import shapeless.{HList, HNil}

class TypeClassAux extends BaseSpec {

  trait Base {

    type Out
    def v: Out
  }

  object Base {

    type Aux[T] = Base { type Out = T }
    type Lt[T] = Base { type Out <: T }

    class ForH() extends Base {

      final type Out = HNil

      override def v: Out = HNil
    }

    object ForH extends ForH
  }

  trait TypeClasses {

    class TypeClass[B]

    def summon[B](b: B)(implicit ev: TypeClass[B]): TypeClass[B] = ev
  }

  object T1 extends TypeClasses {

    implicit def t1: TypeClass[Base.Aux[HNil]] = new TypeClass[Base.Aux[HNil]]

    implicit def t2: TypeClass[Int] = new TypeClass[Int]
  }

  object T2 extends TypeClasses {

    implicit def t1[T <: Base.Aux[HNil]]: TypeClass[T] = new TypeClass[T]
  }

  object T3 extends TypeClasses {

    implicit def t1[
        H <: HList,
        T <: Base.Lt[H]
    ]: TypeClass[T] = new TypeClass[T] {

      type HH = H
    }
  }

  object T4 extends TypeClasses {

    implicit def t1[
        H <: HList,
        T <: Base.Aux[H]
    ]: TypeClass[T] = new TypeClass[T] {

      type HH = H
    }
  }

  it("No Aux") {

    val v = 2

    T1.summon(v) // works
  }

  it("Aux1") {

    val v = new Base.ForH()

//    T1.summon(v) // oops
//    T1.summon(Base.ForH) // oops

    val v2 = new Base.ForH(): Base.Aux[HNil]
    T1.summon(v2) // works!
  }

  it("Aux2") {

    val v = new Base.ForH()

    T2.summon(v) // works
    T2.summon(Base.ForH) // works

    val v2 = new Base.ForH(): Base.Aux[HNil]
    T2.summon(v2) // works
  }

  it("Aux3") {

    val v = new Base.ForH()

//    T3.summon(v) // oops
//    T3.summon(Base.ForH) // oops

    val v2 = new Base.ForH(): Base.Aux[HNil]
//    T3.summon(v2) // oops
  }

  it("Aux4") {

    val v = new Base.ForH()

//    T4.summon(v) // oops
//    T4.summon(Base.ForH) // oops

    val v2 = new Base.ForH(): Base.Aux[HNil]
//    T4.summon(v2) // oops
  }
}
