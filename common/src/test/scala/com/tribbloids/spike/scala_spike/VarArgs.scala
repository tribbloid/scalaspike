package com.tribbloids.spike.scala_spike

object VarArgs {

  def takesN(vs: Int*): Unit = {
    println(vs)
  }

//  takesN {
//    val a = 1
//    val b = 2
//    val c = 3
//    (a, b, c)
//  } oops, doesn't work
}
