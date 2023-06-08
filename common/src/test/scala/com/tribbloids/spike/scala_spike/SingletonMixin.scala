package com.tribbloids.spike.scala_spike

object SingletonMixin {

  trait AA extends Serializable {
    self: Singleton =>

  }

  object A1 extends AA

//  class A2 extends AA // doesn't work not a singleton
}
