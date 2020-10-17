package com.tribbloids.spike.meta.multistage.spark

import org.codehaus.janino.SimpleCompiler

// summoning Reynold Xin: https://gist.github.com/rxin/dc3d2c5ae23ee10a8ec0
// DO NOT CHANGE! should be identical to original
object CodeGenBenchmark {

  def quasiquotes(): Unit = {
    import scala.reflect.runtime.universe._
    import scala.tools.reflect.ToolBox

    val toolBox = runtimeMirror(this.getClass.getClassLoader).mkToolBox()

    var sum = 0
    for (i <- 0 to 50000) {
      val p0 = System.nanoTime()

      val code =
        q"""
        () => 1 + 1 + "134234".toInt * 980 / 50L.toInt
         """

      sum += toolBox.eval(code).asInstanceOf[() => Int].apply()
      val p1 = System.nanoTime()
      if (i < 10 || i % 1000 == 0) {
        println(s"$i: p1 - p0 = ${(p1 - p0)}ns")
      }
    }
    println(sum)
  }

  def janino(): Unit = {
    var sum = 0
    val someInterfaceName = classOf[SomeInterface].getCanonicalName

    for (i <- 0 to 50000) {
      val p0 = System.nanoTime()

      val compiler = new SimpleCompiler()
      compiler.setParentClassLoader(Thread.currentThread().getContextClassLoader)
      compiler.cook(s"""
          |public class MyClass extends $someInterfaceName {
          |  public int eval() {
          |    return 1 + 1 + Integer.parseInt("134234") * 980 / ((int)50L);
          |  }
          |}
        """.stripMargin)

      sum += compiler.getClassLoader.loadClass("MyClass").newInstance().asInstanceOf[SomeInterface].eval()
      val p1 = System.nanoTime()

      //println(s"eval result = $res")
      if (i < 10 || i % 1000 == 0) {
        println(s"$i: p1 - p0 = ${p1 - p0}ns")
      }
    }
    println(sum)
  }

  def main(args: Array[String]): Unit = {
    janino()
    quasiquotes()
  }

  abstract class SomeInterface {
    def eval(): Int
  }
}