package com.tribbloids.spike.scala_spike

import org.scalatest.funspec.AnyFunSpec

import scala.language.higherKinds

class LazyCollections extends AnyFunSpec {

  import LazyCollections._

  val base: Seq[Int] = 1 to 10

  def run(name: String)(fn: StringBuilder => Unit): Unit = {

    val test = Test(fn)

    it(s"$name is ${test.typeOfExecution}") {}
  }

  def render(vs: TraversableOnce[_]): Unit = {
    vs.foreach(_ => ())
  }

  run("Iterator") { log =>
    val vs = base.iterator

    val step1 = vs.map { v =>
      log append "1"
      v
    }
    val step2 = step1.map { v =>
      log append "2"
      v
    }

    render(step2)
  }

  run("Stream") { log =>
    val vs = base.toStream

    val step1 = vs.map { v =>
      log append "1"
      v
    }
    val step2 = step1.map { v =>
      log append "2"
      v
    }

    render(step2)
  }

  run("Iterable") { log =>
    val vs = base.toIterable

    val step1 = vs.map { v =>
      log append "1"
      v
    }
    val step2 = step1.map { v =>
      log append "2"
      v
    }

    render(step2)
  }

  run("Array") { log =>
    val vs = base.toArray

    val step1 = vs.map { v =>
      log append "1"
      v
    }
    val step2 = step1.map { v =>
      log append "2"
      v
    }

    render(step2)
  }

  run("View") { log =>
    val vs = base.view

    val step1 = vs.map { v =>
      log append "1"
      v
    }
    val step2 = step1.map { v =>
      log append "2"
      v
    }

    render(step2)
  }

  run("Seq") { log =>
    val vs = base.toSeq

    val step1 = vs.map { v =>
      log append "1"
      v
    }
    val step2 = step1.map { v =>
      log append "2"
      v
    }

    render(step2)
  }

  run("List") { log =>
    val vs = base.toList

    val step1 = vs.map { v =>
      log append "1"
      v
    }
    val step2 = step1.map { v =>
      log append "2"
      v
    }

    render(step2)
  }
}

object LazyCollections {

  // size must >= 2 otherwise cannot detect
  case class Test[T](
      fn: StringBuilder => T
  ) {

    lazy val typeOfExecution: String = {

      val logger = StringBuilder.newBuilder

      fn(logger)

      val log = logger.mkString

      require(log.nonEmpty, "empty log! not executed!")

      val isLazy = log.replaceAll("12", "").length == 0

      val isEager = {

        val (first, second) = log.splitAt(log.length / 2)

        first.replaceAll("1", "").isEmpty &&
        second.replaceAll("2", "").isEmpty
      }

      if (isLazy) "LAZY"
      else if (isEager) "EAGER"
      else "INDEFINITE"
    }
  }
}
