package com.tribbloids.spike.scala_spike

object TypeClass {

  case class Cat[Food](food: Food, foods: List[Food])

  val cat: Cat[Int] = Cat(1, List(2, 3))
}
