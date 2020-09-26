package com.tribbloids.spike.lms

import com.tribbloids.spike.BaseSpec
import lms.core.{Graph, ScalaCodeGen}

class SimpleCompiler extends BaseSpec {

  import lms.core.stub._

  it("use Rep[Int]") {

    val program: Graph = Adapter.program {
      in =>
        in * in
    }

    val codeGen = new ScalaCodeGen

    val emitted = codeGen.emitAll(program)(manifest[Int], manifest[Int])
  }
}
