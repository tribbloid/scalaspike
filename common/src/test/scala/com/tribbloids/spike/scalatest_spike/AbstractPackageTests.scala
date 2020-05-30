package com.tribbloids.spike.scalatest_spike

import org.scalatest.FunSpec

// TODO: this pattern DOES NOT work
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

object AbstractPackageTests {

  val clz: Class[_root_.com.tribbloids.spike.scalatest_spike.facet1.Suite1] = classOf[facet1.Suite1]
}
