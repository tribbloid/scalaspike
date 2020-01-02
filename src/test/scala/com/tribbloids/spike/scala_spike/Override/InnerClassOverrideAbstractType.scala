package com.tribbloids.spike.scala_spike.Override

object InnerClassOverrideAbstractType {

  trait AALike

  trait SS {

    type AA <: AALike
  }

  class SSA extends SS {

    case class AA() extends AALike
  }

  object SSB extends SS {

    case class AA() extends AALike
  }
}
