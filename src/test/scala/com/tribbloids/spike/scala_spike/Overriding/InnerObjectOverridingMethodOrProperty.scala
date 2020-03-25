package com.tribbloids.spike.scala_spike.Overriding

object InnerObjectOverridingMethodOrProperty {

  trait AALike

  object AA_Outer extends AALike

  trait SS1 {

    def AA: AALike
  }

  trait SS_Mixin {

    object AA extends AALike
  }

  case class SSA() extends SS1 with SS_Mixin

  // cannot override final member
  //  object SSA extends SSA {
  //
  //    object AA extends AALike
  //  }

  trait SS2 {

    val AA: AALike
  }

  case class SSB() extends SS2 with SS_Mixin

  // cannot override final member
  //  object SSB extends SSB {
  //
  //    object AA extends AALike
  //  }

  trait SS3 {
    val AA: AALike = AA_Outer
  }
  case class SSC() extends SS3 {

    override object AA extends AALike
  }

}
