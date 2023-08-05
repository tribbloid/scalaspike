package com.tribbloids.spike.scala_spike

import scala.collection.{mutable, Factory}

object CanBuildFrom {

  case class Dummy() extends mutable.ListMap[String, Int]()

  object Dummy extends Factory[(String, Int), Dummy] {
    override def fromSpecific(it: IterableOnce[(String, Int)]): Dummy = ???

    override def newBuilder: mutable.Builder[(String, Int), Dummy] = ???
  }

  val m1: Map[String, Int] = Map("String" -> 1)

  m1.to(Dummy)
}
