package com.tribbloids.spike.zio_spike

import ai.acyclic.prover.commons.testlib.BaseSpec

class ZIOCompositionExample extends BaseSpec {

  it("sequential composition") {

    import zio.*

    lazy val step1 = ZIO.service[Int].map(x => x + 1)

    // a => b
    def step2 = new (Int => UIO[Int]) {

      {
        println("step2!")
      }

      override def toString = {
        "step2"
      }
      def apply(a: Int) = ZIO.succeed(a + 2)
    }

    // b => c
    def step3 = new (Int => URIO[Double, Double]) {

      {
        println("step2!")
      }

      override def toString = {
        "step3"
      }
      def apply(b: Int) = ZIO.service[Double].map(y => b * y)
    }

    val program = for {
      a <- step1
      b <- step2(a)
      c <- step3(b)
    } yield c

    val dual = step1.flatMap { a =>
      step2(a).flatMap { b =>
        step3(b).map { c =>
          c
        }
      }
    }

    printTree(program)

    println("----")
    printTree(dual)

    val provided = program.provide(
      ZLayer.succeed(10),
      ZLayer.succeed(2.0)
    )

    val result = Unsafe.unsafe { implicit unsafe =>
      Runtime.default.unsafe.run(provided).getOrThrowFiberFailure()
    }

    assert(result == 26.0)
  }

  it("sequential composition 2") {

    import zio.*

    val step1 = ZIO.service[Int].map(x => x + 1)

    // a => b
    val step2 = new (Int => UIO[Int]) {
      override def toString = "step2"
      def apply(a: Int) = ZIO.succeed(a + 2)
    }

    // b => c
    val step3 = new (Int => URIO[Double, Double]) {
      override def toString = "step3"
      def apply(b: Int) = ZIO.service[Double].map(y => b * y)
    }

    val program = for {
      a <- step1
      b <- step2(a)
      c <- step3(b)
    } yield c

    printTree(program)

    val provided = program.provide(
      ZLayer.succeed(10),
      ZLayer.succeed(2.0)
    )

    val result = Unsafe.unsafe { implicit unsafe =>
      Runtime.default.unsafe.run(provided).getOrThrowFiberFailure()
    }

    assert(result == 26.0)
  }

  def printTree(effect: _root_.zio.ZIO[_, _, _]): Unit = {
    def printStruct(z: Any, indent: Int): Unit = {
      val pad = " " * indent
      if (indent > 20) {
        println(s"$pad... (max depth)")
        return
      }
      if (z == null) {
        println(s"$pad null")
        return
      }

      val cls = z.getClass
      val simpleName = cls.getSimpleName

      val str =
        try {
          val s = z.toString
          if (s.length > 100 || s.contains("\n") || s.startsWith(cls.getName)) "" else s
        } catch { case _: Throwable => "" }

      val label = if (str.nonEmpty) s"($str)" else ""
      println(s"$pad$simpleName $label")

      try {
        val fields = cls.getDeclaredFields
        for (f <- fields) {
          f.setAccessible(true)
          val name = f.getName
          if (name == "trace") {
            val trace = f.get(z)
            println(s"$pad  trace: $trace")
          } else {
            val value = f.get(z)
            // Check if it looks like a ZIO node OR a relevant usage closure (Lambda in our package)
            val clsName = if (value != null) value.getClass.getName else ""

            val shouldRecurse = value != null && (try {
              // Is it a ZIO?
              val isZIO = clsName.startsWith("zio.") && !clsName.contains("$$Lambda") && !clsName.endsWith("Trace")
              // Is it a class from our package (likely a closure capturing steps)?
              val isLocal = clsName.startsWith("com.tribbloids.spike")

              isZIO || isLocal
            } catch { case _: Throwable => false })

            if (shouldRecurse) {
              println(s"$pad  $name:")
              printStruct(value, indent + 2)
            } else {
              println(s"$pad  $name: $value")
            }
          }
        }
      } catch {
        case e: Exception => println(s"$pad <error: $e>")
      }
    }

    println(s"\n--- ZIO Tree Diagram ---")
    printStruct(effect, 0)
    println("------------------------\n")
  }

}
