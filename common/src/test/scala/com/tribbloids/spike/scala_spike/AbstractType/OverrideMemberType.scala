package com.tribbloids.spike.scala_spike.AbstractType

import com.tribbloids.graph.commons.testlib.BaseSpec
import com.tribbloids.graph.commons.util.viz.VizType

class OverrideMemberType extends BaseSpec {

  import OverrideMemberType._

  it("can override path dependent type") {

    VizType[Sub1.abc.type].toString.shouldBe(
      """
        |-+ com.tribbloids.spike.scala_spike.AbstractType.OverrideMemberType.Sub1.abc.type
        | !-+ com.tribbloids.spike.scala_spike.AbstractType.OverrideMemberType.Sub1
        |   !-+ com.tribbloids.spike.scala_spike.AbstractType.OverrideMemberType.Sup
        |     !-+ Object
        |       !-- Any
        |""".stripMargin.trim
    )

    VizType[Sub2.abc.type].toString.shouldBe(
      """
        |-+ com.tribbloids.spike.scala_spike.AbstractType.OverrideMemberType.Sub2.abc.type
        | !-+ com.tribbloids.spike.scala_spike.AbstractType.OverrideMemberType.Sub2
        |   !-+ com.tribbloids.spike.scala_spike.AbstractType.OverrideMemberType.Sup
        |     !-+ Object
        |       !-- Any
        |""".stripMargin.trim
    )

    VizType[Sub3.abc.type].toString.shouldBe(
      """
        |-+ com.tribbloids.spike.scala_spike.AbstractType.OverrideMemberType.Sub3.abc.type
        | !-+ com.tribbloids.spike.scala_spike.AbstractType.OverrideMemberType.Sub3
        |   !-+ com.tribbloids.spike.scala_spike.AbstractType.OverrideMemberType.Sup
        |     !-+ Object
        |       !-- Any
        |""".stripMargin.trim
    )

    VizType[Sub4.abc.type].toString.shouldBe(
      """
        |-+ com.tribbloids.spike.scala_spike.AbstractType.OverrideMemberType.Sub4.abc.type
        | !-+ com.tribbloids.spike.scala_spike.AbstractType.OverrideMemberType.Sup
        |   !-+ Object
        |     !-- Any
        |""".stripMargin.trim
    )
  }
}

object OverrideMemberType {

  trait Sup {

    val abc: Sup
  }

  class Sub1 extends Sup {
    override val abc = this
  }
  val Sub1 = new Sub1

  class Sub2 extends Sup {
    override val abc: Sub2 = this
  }
  val Sub2 = new Sub2

  class Sub3 extends Sup {
    override val abc: this.type = this
  }
  val Sub3 = new Sub3

  class Sub4 extends Sup {
    override val abc: Sup = this
  }
  val Sub4 = new Sub4

}
