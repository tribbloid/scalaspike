package com.tribbloids.spike

package object utils {

  type TypeTag[T] = ScalaReflection.universe.TypeTag[T]
}
