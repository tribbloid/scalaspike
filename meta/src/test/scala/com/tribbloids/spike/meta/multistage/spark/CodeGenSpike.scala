package com.tribbloids.spike.meta.multistage.spark

import ai.acyclic.graph.commons.testlib.BaseSpec
import com.tribbloids.spike.meta.multistage.spark.CodeGenBenchmark.SomeInterface
import com.tribbloids.spike.meta.multistage.spark.CodeGenSpike.{Janino, MacroV1}
import org.codehaus.janino.SimpleCompiler

import scala.reflect.runtime.universe

class CodeGenSpike extends BaseSpec {

  def benchmark(fn: () => Unit): Unit = {

    val o = System.nanoTime()

    for (i <- 0 to 5000) {
      val p0 = System.nanoTime()

      fn()

      val p1 = System.nanoTime()

      if (i < 10 || i % 1000 == 0) {
        println(s"$i: p1 - p0 = ${p1 - p0}ns \t p1 - o = ${p1 - o}ns")
      }
    }
  }

  describe("compile-time :") {

    it("Janino") {
      benchmark { () =>
        Janino().eval()
      }
    }

    it("Macro V1") {

      benchmark { () =>
        MacroV1().eval()
      }
    }
  }

  describe("run-time :") {

    it("Janino") {
      val cc = Janino()
      cc.compileOnce()

      benchmark { () =>
        cc.eval()
      }
    }

    it("Macro V1") {
      val cc = MacroV1()
      cc.compileOnce()

      benchmark { () =>
        cc.eval()
      }
    }
  }

}

object CodeGenSpike {

  trait Staged {

    def compileOnce(): Unit

    def eval(): Unit
  }

  case class MacroV1() extends Staged {
    import MacroV1._

    import scala.reflect.runtime.universe._

    lazy val compiled: () => Int = {
      val code =
        q"""
        () => 1 + 1 + "134234".toInt * 980 / 50L.toInt
         """

      toolBox.eval(code).asInstanceOf[() => Int]
    }

    def compileOnce(): Unit = {
      compiled
    }

    def eval(): Unit = {

      compiled.apply()
    }
  }

  object MacroV1 {

    import scala.reflect.runtime.universe._
    import scala.tools.reflect.ToolBox

    val toolBox: ToolBox[universe.type] = runtimeMirror(this.getClass.getClassLoader).mkToolBox()
  }

  case class Janino() extends Staged {
    import Janino._

    lazy val compiled: SomeInterface = {
      val compiler = new SimpleCompiler()
      compiler.setParentClassLoader(Thread.currentThread().getContextClassLoader)
      compiler.cook(s"""
                       |public class MyClass extends $someInterfaceName {
                       |  public int eval() {
                       |    return 1 + 1 + Integer.parseInt("134234") * 980 / ((int)50L);
                       |  }
                       |}
                     """.stripMargin)

      compiler.getClassLoader.loadClass("MyClass").newInstance().asInstanceOf[SomeInterface]
    }

    override def compileOnce(): Unit = { compiled }

    override def eval(): Unit = {
      compiled.eval()
    }
  }

  object Janino {

    val someInterfaceName: String = classOf[SomeInterface].getCanonicalName
  }
}
