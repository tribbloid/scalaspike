package com.tribbloids.spike.meta.multistage.lms

import com.tribbloids.graph.commons.testlib.BaseSpec
import lms.core.{GenericCodeGen, Graph, ScalaCodeGen}

class SimpleLMS extends BaseSpec {

  import lms.core.stub._
  import Adapter._

  it("use INT") {

    val pp: Graph = program { in =>
      in * in
    }

    val codeGen = new ScalaCodeGen
//    codeGen.apply(pp)
    val emitted = codeGen.emitAll(pp)(manifest[Int], manifest[Int])

//    val codeGen = new GenericCodeGen
//    val emitted = codeGen.apply(program)
  }

  describe("WHILE loop") {

    it("evaluated") {

      val pp: Graph = program { in: INT =>
        var i = 0 // do not participate in compilation
        var v = in

        while (i < 10) {

          v = v * (v + i)

          i = i + 1
          PRINT(v)
        }

        in * in
      }

      val codeGen = new ScalaCodeGen
      val emitted = codeGen.emitAll(pp)(manifest[Int], manifest[Int])
    }

    it("not evaluated") {

      val pp = program { in: INT =>
        val i = VAR(0)
        val v = VAR(in)

        WHILE(i() !== 10) {

          v.update(v() * (v() + i()))
          i.update(i() + 1)

          PRINT(v())
        }

        in * in
      }

      val codeGen = new ScalaCodeGen
      val emitted = codeGen.emitAll(pp)(manifest[Int], manifest[Int])
    }
  }
}
