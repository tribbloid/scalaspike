package com.tribbloids.spike.shapeless_spike

import shapeless.Generic

object InferGeneric3 {

  trait Codec {
    def ev: Generic[_]
  }

  case class Impl() {}

  object Impl extends Codec {

    override val ev: Generic[Impl] = Generic[Impl]
  }

  trait HasCodec extends Product {

//    type Self <: HasCodec
//    def ev: Generic[Self]
  }

  object HasCodec {

    class AdHocCodec[T <: HasCodec](
        implicit
        val ev: Generic[T]
    ) extends Codec {}

//    implicit def AdHocCodec[T <: HasCodec](v: T)(
//        implicit ev: Generic[T]): Codec = {
//
//      new Codec {
//        override def ev: Generic[_] = ev
//      }
//    }
  }

  case class Impl2() extends HasCodec {

//    type Self = Impl2
//    def ev: Generic[Impl2] = implicitly[Generic[Impl2]]
  }
}
