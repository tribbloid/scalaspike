package com.tribbloids.spike.scalatest_spike

import org.scalatest.FunSpec

package object facet1 extends AbstractPackageTests {
  override val name: String = "facet 1"

  class Suite3 extends FunSpec {
    describe(name) {

      it("test 5") {}

    }
  }

  //  it("test 6") {}
}
