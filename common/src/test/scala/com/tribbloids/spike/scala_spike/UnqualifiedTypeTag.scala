package com.tribbloids.spike.scala_spike

import scala.reflect.api.Universe

object UnqualifiedTypeTag {

  val RuntimeUniverse = scala.reflect.runtime.universe

  trait HasUniverse {

    val universe: Universe with Singleton

    def uType: RuntimeUniverse.TypeTag[universe.type] = implicitly
  }

  object HasRuntime extends HasUniverse {

    override val universe: RuntimeUniverse.type = RuntimeUniverse
  }

  def main(args: Array[String]): Unit = {
    println(HasRuntime.uType)
  }
}
