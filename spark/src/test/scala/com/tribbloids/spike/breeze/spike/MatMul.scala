package com.tribbloids.spike.breeze.spike

import org.scalatest.funspec.AnyFunSpec

class MatMul extends AnyFunSpec {

  import breeze.linalg._

  it("can multiply matrix") {

    val m1 = DenseMatrix.rand[Double](2, 3)

    val m2 = DenseMatrix.rand[Double](4, 3)

    // No compilation error? Outrageous!!!
    m1 * m2

    m1 * m2.t
  }
}
