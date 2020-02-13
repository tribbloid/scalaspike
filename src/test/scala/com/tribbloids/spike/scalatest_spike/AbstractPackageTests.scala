package com.tribbloids.spike.scalatest_spike

import org.scalatest.FunSpec

trait AbstractPackageTests {

  val name: String

  class Suite1 extends FunSpec {

    describe(name) {

      it("test 1") {}

      it("test 2") {}
    }
  }

  class Suite2 extends FunSpec {
    describe(name) {

      it("test 3") {}

      it("test 4") {}
    }
  }
}
