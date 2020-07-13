package com.tribbloids.spike.utils

case class InferType[T](v: T) {

  type TT = T
}
