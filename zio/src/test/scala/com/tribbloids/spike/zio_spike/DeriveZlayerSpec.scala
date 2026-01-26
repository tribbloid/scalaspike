package com.tribbloids.spike.zio_spike

import ai.acyclic.prover.commons.refl.Reflection.Runtime.TypeTag
import ai.acyclic.prover.commons.testlib.BaseSpec
import zio.*

class DeriveZlayerSpec extends BaseSpec {

  import DeriveZlayerSpec.*

  def printOrigin[I, O](layer: ZLayer[I, Any, O])(
      implicit
      iTag: TypeTag[I],
      oTag: Tag[O]
  ): String = {
    val result = iTag.tpe.toString + " -> " + oTag.tag.toString()

    result
  }

  it("derive") {

    val layer = ZLayer.derive[Goto]

    val origin = printOrigin(layer)
    assert(origin.contains(Goto.getClass.getName.stripSuffix("$")))
  }

  it("fromFunction") {

    val layer = ZLayer.fromFunction((s: String, e: Env) => Goto(s)(e))

    val origin = printOrigin(layer)
    assert(origin.contains(Goto.getClass.getSimpleName.stripSuffix("$")))
  }
}

object DeriveZlayerSpec {

  trait Env
  case class Goto(address: String)(val env: Env) {}

}
