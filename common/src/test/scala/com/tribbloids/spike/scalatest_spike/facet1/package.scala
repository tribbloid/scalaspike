package com.tribbloids.spike.scalatest_spike

import org.scalatest.funspec.AnyFunSpec

package object facet1 extends AbstractPackageTests {
  override val name: String = "facet 1"

  class Suite3 extends AnyFunSpec {
    describe(name) {

      it("test 5") {}

    }
  }

  //  it("test 6") {}
}
