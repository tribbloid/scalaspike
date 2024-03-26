package com.tribbloids.spike.scala_spike

class NonConcreatType {}

object NonConcreteType {

  type <[T, I] >: T

  val x: Int < String = 1

  implicitly[x.type <:< (Int < String)]
}
