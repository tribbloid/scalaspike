package scala.spike.Overriding

object InnerCaseClassOverridingBoth {

  trait AALike

  trait SS {

    type AA <: AALike

    def AA(): AnyRef
  }

  trait SS_Clear extends SS {

    def AA(): AnyRef
  }

  class SSA extends SS_Clear {

    case class AA() extends AALike
  }
  object SSA extends SSA {}

  trait SS_Parameterised extends SS {

    def AA(ii: Int): AnyRef
  }

  class SSB extends SS_Parameterised {

    case class AA(ii: Int) extends AALike

    override def AA(ii: Int): AnyRef = new AA(ii)
  }

  object SSB extends SSA {}
}
