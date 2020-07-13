package com.tribbloids.spike.utils.debug

import com.tribbloids.spike.utils.debug.Debug.CallStackRef

case class print_@[T](v: T) {

  val ref: CallStackRef = CallStackRef(exclude = Seq(this.getClass))

  println(
    s"""
         |${v.toString}
         |\tat ${ref.showStr}
         """.stripMargin
  )
}
