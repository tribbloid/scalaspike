package com.tribbloids.spike.shapeless_spike

import shapeless.Generic

object InferGeneric {

  class WithGeneric[T](
      implicit
      ev: Generic[T]
  )

  case class Impl() {}

  object Impl {

    object Sub extends WithGeneric[Impl]
  }
}
